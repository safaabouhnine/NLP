import chainlit as cl
import requests
import json
import os
import logging

SAVE_FILE = "conversations.json"

logging.basicConfig(level=logging.INFO, format="%(asctime)s - %(levelname)s - %(message)s")

def load_conversations():
    if os.path.exists(SAVE_FILE):
        try:
            with open(SAVE_FILE, "r", encoding="utf-8") as file:
                return json.load(file)
        except json.JSONDecodeError:
            logging.error("Erreur lors du chargement des conversations.")
            return []
    return []

def save_conversations(conversations):
    try:
        with open(SAVE_FILE, "w", encoding="utf-8") as file:
            json.dump(conversations, file, indent=4, ensure_ascii=False)
    except Exception as e:
        logging.error(f"Erreur lors de la sauvegarde : {e}")

conversations = load_conversations()

@cl.on_chat_start
async def start():
    await cl.Message(content="Bienvenue sur Calmify ! Comment pouvons-nous vous aider aujourd'hui ?").send()

    # Ajouter les conversations à la barre latérale
    for idx, conv in enumerate(conversations):
        await cl.Sidebar.add_item(
            f"Conversation {idx + 1}",
            on_click=lambda conv=conv: load_conversation(conv)
        )

async def load_conversation(conversation):
    """Charge une conversation spécifique dans la fenêtre de chat."""
    await cl.Message(content="Chargement de la conversation...").send()
    for message in conversation.get("messages", []):
        if message.get("role") == "user":
            await cl.Message(content=message.get("content"), is_user=True).send()
        else:
            await cl.Message(content=message.get("content")).send()

@cl.on_message
async def main(message):
    API_URL = "http://127.0.0.1:5000/chat"

    try:
        logging.info(f"Message reçu : {message.content}")
        response = requests.post(API_URL, json={"text": message.content}, timeout=30)

        if response.status_code == 200:
            data = response.json()
            chatbot_response = data.get("chatbot_response", "Pas de réponse.")
            recommendation = data.get("recommendation", "")

            new_conversation = {
                "messages": [
                    {"role": "user", "content": message.content},
                    {"role": "bot", "content": chatbot_response}
                ],
                "recommendation": recommendation
            }
            conversations.append(new_conversation)
            save_conversations(conversations)

            await cl.Message(content=f"Chatbot: {chatbot_response}").send()
            if recommendation:
                await cl.Message(content=f"Recommandation: {recommendation}").send()

            # Ajouter la nouvelle conversation à la barre latérale
            await cl.Sidebar.add_item(
                f"Conversation {len(conversations)}",
                on_click=lambda conv=new_conversation: load_conversation(conv)
            )
        else:
            logging.error(f"Erreur API : {response.status_code}, détail : {response.text}")
            await cl.Message(content=f"Erreur de l'API : {response.status_code}, détail : {response.text}").send()

    except requests.ConnectionError:
        logging.error("Impossible de se connecter à l'API Flask.")
        await cl.Message(content="Impossible de se connecter à l'API. Assurez-vous que le serveur Flask est en cours d'exécution.").send()
    except requests.Timeout:
        logging.error("Le délai de connexion à l'API a été dépassé.")
        await cl.Message(content="Le délai de connexion à l'API a été dépassé.").send()
    except Exception as e:
        logging.error(f"Erreur lors de l'appel de l'API : {e}")
        await cl.Message(content=f"Erreur lors de l'appel de l'API : {e}").send()
