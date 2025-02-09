#chatbot_service.py
import os
from transformers import AutoTokenizer, AutoModelForCausalLM

# Get the absolute path to the models directory
current_dir = os.path.dirname(os.path.abspath(__file__))
MODEL_PATH = os.path.abspath(os.path.join(
    current_dir, '..', '..', 'models', 'LLaMa',
    'models--NousResearch--Llama-2-7b-chat-hf',
    'snapshots',
    '351844e75ed0bcbbe3f10671b3c808d2b83894ee'
))
# Verify the path exists
if not os.path.exists(MODEL_PATH):
    raise FileNotFoundError(f"Model directory not found at {MODEL_PATH}")

# Load model with proper error handling
try:
    tokenizer = AutoTokenizer.from_pretrained(MODEL_PATH, local_files_only=True)
    model = AutoModelForCausalLM.from_pretrained(MODEL_PATH, local_files_only=True)
except Exception as e:
    print(f"Error loading model: {str(e)}")
    raise

def generate_response(user_input, emotions, stress_level):
    """
    Generate a response using Llama based on user input, emotions, and stress level.
    """
    prompt = f"""
    You are a supportive and empathetic therapist chatbot.
    The user is feeling these emotions: {emotions}.
    Stress level: {stress_level}.
    User input: {user_input}
    Bot:"""

        # Tokenize with truncation
    print("Tokenizing input...")
    inputs = tokenizer(prompt, return_tensors="pt", truncation=True, padding=True, max_length=512)
    # Generate the response with appropriate parameters

    print("Generating response...")
    output = model.generate(
        inputs["input_ids"],
        attention_mask=inputs["attention_mask"],  # Include attention mask
        max_length=100,
        temperature=0.7, # Controls randomness
        top_p=0.9,
        do_sample=True,   # Enable sampling for faster generation
        pad_token_id=tokenizer.eos_token_id
    )


    print("Decoding response...")
    response = tokenizer.decode(output[0], skip_special_tokens=True)
    bot_response = response.split("Bot:")[-1].strip()
    return bot_response