import sys
import os
from flask import Flask, request, jsonify, make_response
import multiprocessing
from datetime import datetime
from transformers import AutoTokenizer, AutoModelForSeq2SeqLM
import psycopg2
import json
from flask_cors import CORS
import requests  # Added to make API calls


app = Flask(__name__)
CORS(app, resources={r"/api/*": {"origins": "http://localhost:5173"}})
# Configuration des chemins et de la base de données PostgreSQL
DB_HOST = "localhost"
DB_NAME = "Calmify"
DB_USER = "postgres"
DB_PASSWORD = "AZERTY"

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
API_KEY = "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"  # Replace with your actual API key
MODEL_NAME = "mistralai/Mistral-7B-Instruct-v0.3"
API_URL = f"https://api-inference.huggingface.co/models/{MODEL_NAME}"
print("Using Hugging Face API for the chatbot model.")


# Function to Get Response from Hugging Face API
def get_chatbot_response(user_input):
    prompt = f"""
You are a caring and attentive assistant.

The user said: "{user_input}"

Guidelines:
- Do not repeat the user's question word for word.
- Provide a clear and concise response.
- Structure your answer as a list with bullet points or numbers for better readability.
- Ensure that the advice is relevant, kind, and constructive.

Expected format:

- Tip 1: ...
- Tip 2: ...
- Tip 3: ...

Your response:
"""
    headers = {"Authorization": f"Bearer {API_KEY}",
               "Content-Type": "application/json"}
    data = {
        "inputs": prompt,
        "parameters": {
            "max_new_tokens": 200,
            "temperature": 0.7,
            "top_p": 0.9,
            "return_full_text": False,  # si disponible pour votre modèle
            "repetition_penalty": 1.2,
            "stop_sequences": ["\n\n"]  # Forcer le modèle à bien structurer les paragraphes
        }
    }
    try:
        response = requests.post(API_URL, headers=headers, json=data)
        print("Hugging Face API Response:", response.json())  # Log the API response

        if response.status_code == 200:
            response_json = response.json()
            if isinstance(response_json, list) and len(response_json) > 0:
                raw_text = response_json[0].get("generated_text", "No response generated.")
                # Post-traitement pour enlever la question si répétée au début
                cleaned_text = remove_repetition(raw_text, user_input)
                formatted_text = format_for_html(cleaned_text)  # Conversion pour affichage HTML
                return formatted_text
            return "Unable to generate a valid response."
        else:
            print(f"API Error {response.status_code}: {response.json()}")
            return "Error in generating response. Please try again later."
    except Exception as e:
        print(f"Error in API call: {e}")
        return "An error occurred while processing your request."
def format_for_html(text):
    return text.replace("\n", "<br>")

def remove_repetition(generated_text, user_input):
    # Supprime la répétition exacte du prompt s’il est au début
    trimmed_user_input = user_input.strip()
    trimmed_generated = generated_text.strip()
    if trimmed_generated.startswith(trimmed_user_input):
        return trimmed_generated[len(trimmed_user_input):].strip()
    return trimmed_generated

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
# conversations = [
#     {"id": 1, "title": "Conversation 1"},
#     {"id": 2, "title": "Conversation 2"},
#     {"id": 3, "title": "Conversation 3"},
# ]
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

        chatbot_response = get_chatbot_response(user_input)

        if not active_conversation_id:
            active_conversation_id = save_new_conversation(start_time, "ongoing")

        messages = [
            {"role": "user", "message": user_input, "timestamp": datetime.now().isoformat()},
            {"role": "bot", "message": chatbot_response, "timestamp": datetime.now().isoformat()}
        ]
        update_conversation_messages(active_conversation_id, messages)

        response = make_response(jsonify({
            "user_input": user_input,
            "chatbot_response": chatbot_response.replace("\n", "<br>"),
            "emotions": emotion_scores,
            "stress_level": stress_level,
            "conversation_id": active_conversation_id
        }))
        response.set_cookie("active_conversation_id", str(active_conversation_id))
        return response
    except Exception as e:
        print(f"Erreur dans l'endpoint /chat : {e}")
        return jsonify({"error": str(e)}), 500

@app.route('/api/analyze', methods=['POST'])
def analyze_text():
    try:
        data = request.get_json()
        if not data or "text" not in data:
            return jsonify({"error": "Invalid input: 'text' is required"}), 400

        user_text = data["text"]

        # Analyse des émotions et stress
        emotion_scores = predict_emotions(user_text)
        stress_level = map_emotions_to_stress(emotion_scores)

        # Génération de la réponse du chatbot
        chatbot_response = get_chatbot_response(user_text)

        # Retourner les résultats
        return jsonify({
            "feelings": json.dumps(emotion_scores),
            "stressLevel": stress_level,
            "chatbot_response": chatbot_response
        })

    except Exception as e:
        print(f"Erreur dans /api/analyze : {e}")
        return jsonify({"error": str(e)}), 500


if __name__ == "__main__":
    multiprocessing.freeze_support()
    try:
        print("Démarrage du serveur Flask...")
        app.run(host="0.0.0.0", port=5000, debug=True)
    except Exception as e:
        print(f"Erreur lors du démarrage du serveur Flask : {e}")