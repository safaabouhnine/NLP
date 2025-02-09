import sys
import os
from flask import Flask, request, jsonify, make_response
import multiprocessing
from datetime import datetime
from transformers import AutoTokenizer, AutoModelForSeq2SeqLM
import psycopg2
import json
from flask_cors import CORS

app = Flask(__name__)
CORS(app, resources={r"/api/*": {"origins": "http://localhost:5173"}})
# Configuration des chemins et de la base de données PostgreSQL
DB_HOST = "localhost"
DB_NAME = "Calmify"
DB_USER = "postgres"
DB_PASSWORD = "safaa123"
# hello ana kanjareb had version

# Ajout du répertoire des services au PATH
service_dir = os.path.join(os.path.dirname(__file__), "services")
if service_dir not in sys.path:
    sys.path.append(service_dir)

print("Répertoire des services ajouté au PATH Python :", sys.path)

# Importation des services personnalisés
try:
    from services.emotion_analysis import predict_emotions, map_emotions_to_stress
    print("Services importés avec succès.")
except ImportError as e:
    print(f"Erreur lors de l'importation des services : {e}")
    raise

# Initialisation de Flask


# Configuration du modèle Flan-T5
MODEL_NAME = "google/flan-t5-base"
print("Chargement du modèle Flan-T5...")
tokenizer = AutoTokenizer.from_pretrained(MODEL_NAME)
model = AutoModelForSeq2SeqLM.from_pretrained(MODEL_NAME)
print("Modèle Flan-T5 et tokenizer chargés avec succès.")

# Fonction pour créer une nouvelle conversation
def save_new_conversation(start_time, status):
    try:
        connection = psycopg2.connect(
            host=DB_HOST,
            database=DB_NAME,
            user=DB_USER,
            password=DB_PASSWORD
        )
        cursor = connection.cursor()

        query = """
            INSERT INTO conversation (start_time, status) VALUES (%s, %s) RETURNING idc
        """
        cursor.execute(query, (start_time, status))
        connection.commit()

        conversation_id = cursor.fetchone()[0]
        return conversation_id
    except Exception as e:
        print(f"Erreur lors de la création de la conversation : {e}")
        return None
    finally:
        if connection:
            cursor.close()
            connection.close()

# Fonction pour mettre à jour les messages d'une conversation existante
def update_conversation_messages(conversation_id, messages):
    try:
        connection = psycopg2.connect(
            host=DB_HOST,
            database=DB_NAME,
            user=DB_USER,
            password=DB_PASSWORD
        )
        cursor = connection.cursor()

        # Récupérer les messages existants
        query = "SELECT messages FROM conversation WHERE idc = %s"
        cursor.execute(query, (conversation_id,))
        existing_messages = cursor.fetchone()[0] or []

        # Ajouter les nouveaux messages
        updated_messages = existing_messages + messages
        query = "UPDATE conversation SET messages = %s WHERE idc = %s"
        cursor.execute(query, (json.dumps(updated_messages), conversation_id))
        connection.commit()
    except Exception as e:
        print(f"Erreur lors de la mise à jour des messages : {e}")
    finally:
        if connection:
            cursor.close()
            connection.close()

# Exemple de données
conversations = [
    {"id": 1, "title": "Conversation 1"},
    {"id": 2, "title": "Conversation 2"},
    {"id": 3, "title": "Conversation 3"},
]
# Endpoint pour récupérer toutes les conversations
@app.route('/api/conversations', methods=['GET'])
def get_conversations():
    try:
        connection = psycopg2.connect(
            host=DB_HOST,
            database=DB_NAME,
            user=DB_USER,
            password=DB_PASSWORD
        )
        cursor = connection.cursor()

        query = "SELECT idc, end_time, start_time, status, messages FROM conversation ORDER BY start_time DESC"
        cursor.execute(query)
        rows = cursor.fetchall()

        conversations = []
        for row in rows:
            messages = row[4] if isinstance(row[4], list) else json.loads(row[4]) if row[4] else []
            conversations.append({
                "id": row[0],
                "end_time": row[1],
                "start_time": row[2],
                "status": row[3],
                "messages": messages
            })

        cursor.close()
        connection.close()

        return jsonify({"conversations": conversations})
    except Exception as e:
        print(f"Erreur dans /conversations : {e}")
        return jsonify({"error": str(e)}), 500

@app.route('/api/chat', methods=['POST'])
def chat():
    try:
        print("Endpoint /chat appelé")
        
        # Récupérer ou créer une conversation active
        active_conversation_id = request.cookies.get("active_conversation_id")
        start_time = datetime.now()

        data = request.get_json()
        if not data or "text" not in data or not isinstance(data["text"], str):
            return jsonify({"error": "Donnée invalide : 'text' doit être une chaîne"}), 400

        user_input = data["text"]
        print("Données reçues :", user_input)

        print("Analyse des émotions...")
        emotion_scores = predict_emotions(user_input)
        stress_level = map_emotions_to_stress(emotion_scores)

        print("Génération de la réponse du chatbot...")
        input_ids = tokenizer(
            user_input,
            return_tensors="pt",
            padding=True,
            truncation=True,
            max_length=50
        )
        outputs = model.generate(
            input_ids["input_ids"],
            max_length=50,
            temperature=0.7,
            top_p=0.9,
            do_sample=True
        )
        chatbot_response = tokenizer.decode(outputs[0], skip_special_tokens=True)

        if not active_conversation_id:
            active_conversation_id = save_new_conversation(start_time, "ongoing")

        messages = [
            {"role": "user", "message": user_input, "timestamp": datetime.now().isoformat()},
            {"role": "bot", "message": chatbot_response, "timestamp": datetime.now().isoformat()}
        ]
        update_conversation_messages(active_conversation_id, messages)

        response = make_response(jsonify({
            "user_input": user_input,
            "chatbot_response": chatbot_response,
            "emotions": emotion_scores,
            "stress_level": stress_level,
            "conversation_id": active_conversation_id
        }))
        response.set_cookie("active_conversation_id", str(active_conversation_id))
        return response
    except Exception as e:
        print(f"Erreur dans l'endpoint /chat : {e}")
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    multiprocessing.freeze_support()
    try:
        print("Démarrage du serveur Flask...")
        app.run(host="0.0.0.0", port=5000, debug=True)
    except Exception as e:
        print(f"Erreur lors du démarrage du serveur Flask : {e}")
