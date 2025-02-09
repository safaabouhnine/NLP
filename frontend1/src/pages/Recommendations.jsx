import  { useEffect, useState } from "react";
import axios from "../Utils/axios-instance"; // Assurez-vous que l'import est correct
import "../styles/Recommendations.css";

const Recommendations = () => {
    const [videos, setVideos] = useState([]);

    useEffect(() => {
        // Appel à l'API pour récupérer les recommandations
        axios
            .get("http://localhost:8081/api/recommendations/generate", {
                params: {
                    nlpAnalysisId: 5, // Exemple de paramètre
                    userId: 1,        // Exemple de paramètre
                },
            })
            .then((response) => {
                setVideos(response.data.videos || []); // Mettez à jour l'état avec les vidéos
            })
            .catch((error) => {
                console.error("Failed to fetch recommendations", error);
            });
    }, []);

    return (
        <div className="recommendations-container">
            <h2>Video Recommendations</h2>
            <div className="video-list">
                {videos.length > 0 ? (
                    videos.map((video) => (
                        <div key={video.idVR} className="video-card">
                            <h3>{video.title}</h3>
                            <p>{video.description}</p>
                            <a href={video.videoLink} target="_blank" rel="noopener noreferrer">
                                Watch Video
                            </a>
                        </div>
                    ))
                ) : (
                    <p>No recommendations available.</p>
                )}
            </div>
        </div>
    );
};

export default Recommendations;