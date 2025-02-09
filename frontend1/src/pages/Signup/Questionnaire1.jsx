// eslint-disable-next-line no-unused-vars
import React, { useState } from "react";
import PropTypes from "prop-types"; // Import PropTypes
import { useNavigate } from "react-router-dom";
import "../../styles/Questionnaire1.css";
import axiosInstance from "../../Utils/axios-instance";
import * as response from "autoprefixer"; // Import Axios for API calls

const Questionnaire1 = ({ setQuestionnaireData }) => {
    const navigate = useNavigate(); // Hook to navigate between pages
    const [answers, setAnswers] = useState({
        headaches: "",
        sleep: "",
        timeManagement: "",
        irritability: "",
        appointments: "",
    });

    // Handle response change
    const handleAnswerChange = (question, value) => {
        setAnswers((prev) => ({
            ...prev,
            [question]: value,
        }));
    };

    // Function triggered when "Next" is clicked
    const handleNext = async () => {
        try {
            // Récupérer l'ID de l'utilisateur connecté
            const userId = localStorage.getItem("userId"); // Assurez-vous que l'ID est bien stocké

            if (!userId || isNaN(parseInt(userId))) {
                alert("Erreur : Utilisateur non connecté ou ID invalide !");
                return;
            }
            const userIdInt = parseInt(userId);
            // Sauvegarder les réponses localement pour les transférer à la page suivante
            setQuestionnaireData((prev) => ({
                ...prev,
                ...answers,
            }));

            // Construire le payload avec l'ID utilisateur dynamique
            const payload = Object.entries(answers).map(([, value], index) => ({
                user: { id: userIdInt }, // Utiliser l'ID utilisateur converti
                question: { idq: index + 1 }, // Utiliser l'ID de la question
                value: parseInt(value), // Convertir la réponse en entier
            }));

            console.log("Payload envoyé :", payload); // Vérifiez que le payload est correct

            // Envoyer les réponses au backend
            await axiosInstance.post("/answers", payload);
            console.log("Réponses enregistrées :", response.data);
            // alert("Réponses enregistrées avec succès !");
            // Rediriger vers la page suivante
            navigate("/Questionnaire2");
        } catch (error) {
            console.error("Erreur lors de l'envoi des réponses :", error);
        }
    };

    const questions = [
        { id: "headaches", text: "Do you experience headaches or migraines?" },
        { id: "sleep", text: "Is your sleep disrupted (waking up, insomnia, hypersomnia)?" },
        { id: "timeManagement", text: "Do you feel like you never have enough time to get things done?" },
        { id: "irritability", text: "Do other people irritate you?" },
        { id: "appointments", text: "Do you often forget appointments?" },
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
        <div className="questionnaire-container">
            <div className="questionnaire-content">
                <h1>Stress Questionnaire</h1>
                <p className="subtitle">Please answer the following questions:</p>
                <div className="questions-container">
                    {questions.map((question, index) => (
                        <div key={question.id} className="question-block">
                            <p className="question-text">{`${index + 1}. ${question.text}`}</p>
                            <div className="options-container">
                                {options.map((option) => (
                                    <label key={`${question.id}-${option.value}`} className="radio-label">
                                        <input
                                            type="radio"
                                            name={question.id}
                                            value={option.value}
                                            checked={answers[question.id] === option.value}
                                            onChange={() => handleAnswerChange(question.id, option.value)}
                                        />
                                        <span className="radio-text">{option.label}</span>
                                    </label>
                                ))}
                            </div>
                        </div>
                    ))}
                </div>
                <button className="next-button" onClick={handleNext}>
                    Next
                </button>
            </div>
        </div>
    );
};

// Prop validation
Questionnaire1.propTypes = {
    setQuestionnaireData: PropTypes.func.isRequired,
};

export default Questionnaire1;