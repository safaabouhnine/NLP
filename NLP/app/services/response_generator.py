from transformers import AutoTokenizer, AutoModelForSeq2SeqLM

class ChatbotResponseGenerator:
    
    def __init__(self):
        self.model_name = "google/flan-t5-base"
        self.tokenizer = AutoTokenizer.from_pretrained(self.model_name)
        self.model = AutoModelForSeq2SeqLM.from_pretrained(self.model_name)
    
    def build_prompt(self, user_input, analysis):
        """Construct context-aware prompt"""
        return f"""Generate supportive response for a stressed student:
Stress Level: {analysis['stress_level']}
Dominant Emotion: {analysis['main_emotion']}
Message: "{user_input[:200]}"

Guidelines:
1. Validate the emotion
2. Offer 1 simple and practical advice
3. Keep it under 3 sentences
4. Use simple English
5. End the response with an encouragement.

Response:"""
    
    def generate_response(self, user_input, analysis):
        """Generate context-appropriate response"""
        prompt = self.build_prompt(user_input, analysis)
        
        inputs = self.tokenizer(
            prompt,
            return_tensors="pt",
            max_length=300,
            truncation=True
        )
        
        outputs = self.model.generate(
            inputs.input_ids,
            max_new_tokens=150,
            # temperature=0.7,
            # top_p=0.9,
            repetition_penalty=1.2,
            do_sample=True
        )
        
        raw_response = self.tokenizer.decode(outputs[0], skip_special_tokens=True)
        return self.clean_response(raw_response)
    
    def clean_response(self, text):
        """Remove prompt artifacts"""
        return text.split("Response:")[-1].strip().replace('"', '')
    
# === PARTIE TEST ===
if __name__ == "__main__":
    generator = ChatbotResponseGenerator()
    
    user_input = "I'm feeling very anxious about my upcoming exams."
    analysis = {"stress_level": "high", "main_emotion": "anxiety"}
    
    try:
        response = generator.generate_response(user_input, analysis)
        print("Generated Response:", response)
    except Exception as e:
        print("Error during response generation:", e)
    