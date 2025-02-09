import random

SAFE_RESPONSES = {
    "high": [
        "I see that this situation is weighing on you. Let's start with a deep breath: inhale for 4 seconds, hold for 4 seconds, exhale for 6 seconds.",
        "Take a moment to recenter yourself. Would you like me to guide you through a short 2-minute meditation?",
        "It's important to take care of yourself right now. Maybe take a break and call a loved one?"
    ],
    "medium": [
        "How about taking 5 minutes to jot down what's on your mind? It might help clear your thoughts.",
        "A quick 10-minute walk could help you see things more clearly.",
        "Here's a quick exercise: name 3 things you see, hear, and physically feel."
    ],
    "low": [
        "Keep up the positive attitude! Perhaps plan a fun activity for this week?",
        "Continue practicing your wellness routines! A gratitude journal might be helpful.",
        "It's great to see you're managing stress well. Here's an idea to maintain this balance..."
    ]
}

def get_safe_response(stress_level, emotion):
    try:
        if stress_level not in SAFE_RESPONSES:
            stress_level = "medium"
        return random.choice(SAFE_RESPONSES[stress_level])
    except:
        return "Thank you for sharing your feelings. How can I provide you with comfort at this moment?"
