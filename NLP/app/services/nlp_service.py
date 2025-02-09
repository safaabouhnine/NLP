#AOUBLIER CE FILE, LEFT HERE JUST IN CASE NEEDED
from flask import Flask, request, jsonify
from transformers import AutoModelForCausalLM, AutoTokenizer
import time  # Pour simuler un délai (optionnel)

app = Flask(__name__)

MODEL_NAME = "microsoft/DialoGPT-medium"
tokenizer = AutoTokenizer.from_pretrained(MODEL_NAME)
model = AutoModelForCausalLM.from_pretrained(MODEL_NAME)

@app.route('/chat', methods=['POST'])
def chat():
    data = request.json
    text = data.get("text", "")

    if not text:
        return jsonify({"error": "Texte non fourni"}), 400

    # Envoyer une réponse provisoire
    response_placeholder = {
        "response": "La réponse est en cours de génération...",
        "loading": True
    }
    time.sleep(1)  # Optionnel : Simule un délai
    # (Frontend peut utiliser cette réponse pour afficher un indicateur de chargement)

    # Générer une réponse complète
    input_ids = tokenizer.encode(text, return_tensors="pt")
    chat_history_ids = model.generate(input_ids, max_length=1000, pad_token_id=tokenizer.eos_token_id)
    dialogpt_response = tokenizer.decode(chat_history_ids[:, input_ids.shape[-1]:][0], skip_special_tokens=True)

    # Réponse finale
    full_response = {
        "response": dialogpt_response,
        "loading": False  # L'indicateur de chargement s'arrête
    }
    return jsonify(full_response)

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
