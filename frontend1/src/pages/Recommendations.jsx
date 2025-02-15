
import { useEffect, useState } from "react";
import axios from "../Utils/axios-instance";
import "../styles/Recommendations.css";

const Recommendations = () => {
    const [userId, setUserId] = useState(null);
    const [recommendations, setRecommendations] = useState({});
    const [nlpAnalysisId, setNlpAnalysisId] = useState(null);

    // 🔹 Fonction pour récupérer l'ID de l'utilisateur connecté
    const fetchUserId = async () => {
        try {
            const token = localStorage.getItem("token");
            if (!token) {
                throw new Error("Utilisateur non authentifié");
            }

            const response = await axios.get("http://localhost:8081/api/users/me", {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            console.log("✅ User ID récupéré :", response.data.id);
            setUserId(response.data.id);
        } catch (err) {
            console.error("❌ Erreur lors de la récupération de l'ID utilisateur :", err);
        }
    };

    // 🔹 Récupération de l'ID de l'utilisateur au chargement
    useEffect(() => {
        fetchUserId();
    }, []);

    // 🔹 Récupération de l'analyse NLP pour cet utilisateur
    useEffect(() => {
        if (userId) {
            axios
                .get(`http://localhost:8081/api/nlp/latest/${userId}`) // Ajout de userId dans l'URL
                .then((response) => {
                    console.log("✅ NLP Analysis ID reçu :", response.data.id);
                    setNlpAnalysisId(response.data.id);
                })
                .catch((error) => {
                    console.error("❌ Échec de récupération de l'ID NLP", error);
                });
        }
    }, [userId]);

    // 🔹 Récupération des recommandations lorsque NLP Analysis est prêt
    useEffect(() => {
        if (nlpAnalysisId && userId) {
            axios
                .get("http://localhost:8081/api/recommendations/generate", {
                    params: {
                        nlpAnalysisId: nlpAnalysisId,
                        userId: userId,
                    },
                })
                .then((response) => {
                    console.log("✅ Données de recommandations reçues :", response.data);
                    setRecommendations(response.data);
                })
                .catch((error) => {
                    console.error("❌ Échec de récupération des recommandations", error);
                });
        }
    }, [nlpAnalysisId, userId]);

    return (
        <div className="recommendations-container">
            <h2>Recommendations</h2>
            {recommendations.advices && recommendations.advices.length > 0 && (
                <div className="advice-list">
                    <h3>Advice</h3>
                    {recommendations.advices.map((advice) => (
                        <div key={advice.idAd} className="advice-card">
                            <h4>{advice.title}</h4>
                            <p>{advice.description}</p>
                        </div>
                    ))}
                </div>
            )}
            {recommendations.videos && recommendations.videos.length > 0 && (
                <div className="video-list">
                    <h3>Videos</h3>
                    {recommendations.videos.map((video) => (
                        <div key={video.idVR} className="video-card">
                            <h4>{video.title}</h4>
                            <a href={video.videoLink} target="_blank" rel="noopener noreferrer">
                                Watch Video
                            </a>
                        </div>
                    ))}
                </div>
            )}
            {recommendations.meeting && (
                <div className="meeting-section">
                    <h3>Scheduled Meeting</h3>
                    <p><strong>Meeting ID:</strong> {recommendations.meeting.idM}</p>
                    {recommendations.meeting.student && (
                        <p><strong>Student ID:</strong> {recommendations.meeting.student.id}</p>
                    )}
                    <p><strong>Meeting Link:</strong>
                        <a href={recommendations.meeting.meetLink} target="_blank" rel="noopener noreferrer">
                            Join Meeting
                        </a>
                    </p>
                </div>
            )}
            {!recommendations.meeting &&
                !recommendations.advices?.length &&
                !recommendations.videos?.length &&
                !recommendations.psychologists?.length && (
                    <p className="no-recommendation">Please talk to the chatbot to receive recommendations based on your stress level.</p>
                )}
        </div>
    );
};

export default Recommendations;
