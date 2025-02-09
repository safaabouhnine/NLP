from lime.lime_text import LimeTextExplainer
from .emotion_analysis import predict_emotions
import numpy as np

# Initialize Lime Explainer with class names
explainer = LimeTextExplainer(class_names=["anger", "fear", "joy", "love", "sadness", "surprise"])

def lime_explain(text):
    """
    Explain predictions using Lime for a given text.

    Args:
        text (str): The input text to analyze.

    Returns:
        list of tuples: Feature importance (word, importance).
    """
    def lime_predict(texts):
        """
        Lime-compatible prediction function.

        Args:
            texts (list): List of texts for prediction.

        Returns:
            np.array: Scores for each emotion class.
        """
        predictions = []
        for txt in texts:
            scores = predict_emotions(txt)
            # Create an ordered list of scores for each class
            class_scores = [scores.get(cls, 0.0) for cls in explainer.class_names]
            predictions.append(class_scores)
        return np.array(predictions)

    try:
        explanation = explainer.explain_instance(
            text_instance=text,
            classifier_fn=lime_predict,
            num_features=5
        )
        return explanation.as_list()  # Returns list of (word, importance)
    except Exception as e:
        print(f"Error during explanation generation: {e}")
        return []
