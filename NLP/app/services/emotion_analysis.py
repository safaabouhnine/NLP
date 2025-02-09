from transformers import pipeline

# Charger le modèle NLP
emotion_classifier = pipeline(
    "text-classification",
    model="bhadresh-savani/distilbert-base-uncased-emotion",
    return_all_scores=True,  # Permet d'obtenir tous les scores des émotions
    framework="pt"
)

def predict_emotions(text):
    """
    Analyse le texte et retourne les scores de toutes les émotions détectées.
    """
    results = emotion_classifier(text[:512])  # Troncature pour éviter trop de tokens
    emotion_scores = {res['label']: res['score'] for res in results[0]}  # Extraire les émotions
    return emotion_scores

def map_emotions_to_stress(emotion_scores):
    """
    Convertit les scores d'émotions en un niveau de stress : 'low', 'medium', 'high'.
    Pondère chaque émotion pour calculer un score de stress.
    """
    stress_weights = {
        "sadness": 0.85,
        "fear": 0.90,
        "anger": 0.80,
        "surprise": 0.40,
        "joy": -0.70,
        "love": -0.60
    }
    # Calcul du score total de stress
    total_stress_score = sum(
        score * stress_weights.get(emotion, 0)  # Si l'émotion n'est pas dans stress_weights, on considère un poids de 0
        for emotion, score in emotion_scores.items()
    )

    # Détermination du niveau de stress en fonction du score total
    if total_stress_score >= 0.75:
        return "high"
    elif total_stress_score >= 0.35:
        return "medium"
    return "low"