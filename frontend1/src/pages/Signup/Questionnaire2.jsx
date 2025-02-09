// eslint-disable-next-line no-unused-vars
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../../styles/Questionnaire2.css";
import axiosInstance from "../../Utils/axios-instance";


const Questionnaire2 = () => {
    const navigate = useNavigate();
    const [answers, setAnswers] = useState({
        concentration: "",
        pressure: "",
        support: "",
        overload: "",
        fatigue: "",
    });

    const handleAnswerChange = (question, value) => {
        setAnswers((prev) => ({
            ...prev,
            [question]: value,
        }));
    };

    const handleSubmit = async () => {
        try {
            const userId = localStorage.getItem("userId");

            if (!userId || isNaN(parseInt(userId))) {
                alert("Erreur : Utilisateur non connecté ou ID invalide !");
                return;
            }

            const userIdInt = parseInt(userId);

            // Construire le payload
            const payload = Object.entries(answers).map(([, value], index) => ({
                user: { id: userIdInt },
                question: { idq: index + 6 }, // Ajustez les IDs des questions si nécessaire
                value: parseInt(value) || 0, // Valeur par défaut si non remplie
            }));

            console.log("Payload envoyé :", payload);

            // Envoyer les données au backend
            const response = await axiosInstance.post("/answers", payload);
            console.log("Réponses enregistrées :", response.data);

            alert("Réponses enregistrées avec succès !");
            navigate("/dashboard");
        } catch (error) {
            console.error("Erreur lors de l'envoi des réponses :", error.response?.data || error.message);
            alert("Une erreur est survenue lors de l'envoi des réponses.");
        }
    };

    const questions = [
        { id: "concentration", text: "Do you have difficulty concentrating on a task or staying focused for a long time?" },
        { id: "pressure", text: "Do you feel a constant sense of pressure, even when you have nothing urgent to do?" },
        { id: "support", text: "Do you think you need external support to better manage your stress?" },
        { id: "overload", text: "Do you often experience frequent or permanent work overload?" },
        { id: "fatigue", text: "Do you often feel unexplained fatigue, even after a full night’s sleep?" },
    ];

    const options = [
        { value: "1", label: "Not at all" },
        { value: "2", label: "Slightly" },
        { value: "3", label: "Somewhat" },
        { value: "4", label: "Fairly" },
        { value: "5", label: "A lot" },
        { value: "6", label: "Extremely" },
    ];

    return (
        <div className="questionnaire-step2-container">
            <div className="questionnaire-step2-content">
                <h1>Stress Questionnaire</h1>
                <p className="subtitle-step2">Please answer the following questions :</p>
                <div className="questions-step2-container">
                    {questions.map((question, index) => (
                        <div key={question.id} className="question-step2-block">
                            <p className="question-step2-text">{`${index + 1}. ${question.text}`}</p>
                            <div className="options-step2-container">
                                {options.map((option) => (
                                    <label key={`${question.id}-${option.value}`} className="radio-step2-label">
                                        <input
                                            type="radio"
                                            name={question.id}
                                            value={option.value}
                                            checked={answers[question.id] === option.value}
                                            onChange={() => handleAnswerChange(question.id, option.value)}
                                        />
                                        <span className="radio-step2-text">{option.label}</span>
                                    </label>
                                ))}
                            </div>
                        </div>
                    ))}
                </div>
                <button className="submit-step2-button" onClick={handleSubmit}>
                    Submit
                </button>
            </div>
        </div>
    );
};

// Retirer PropTypes car aucune prop n'est utilisée
export default Questionnaire2;