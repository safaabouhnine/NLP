import { useEffect, useState } from "react";
import axiosInstance from "../AxiosInstance";

const Conversations = () => {
    const [conversations, setConversations] = useState([]);
    const [loading, setLoading] = useState(true);
    useEffect(() => {
        const fetchConversations = async () => {
            try {
                const response = await axiosInstance.get('/conversations');
                console.log("Conversations récupérées dans React :", response.data.conversations);
                setConversations(response.data.conversations); // Met à jour l'état
            } catch (error) {
                console.error("Erreur lors de la récupération des conversations :", error);
            } finally {
                setLoading(false);
            }
        };
        fetchConversations();
    }, []);

    if (loading) {
        return <div>Chargement des conversations...</div>;
    }
    // if (conversations.length === 0) {
    //     return <div>Aucune conversation trouvée.</div>;
    // }
    return (
        <div>
            <h1>Historique des Conversations</h1>
            <ul>
                {conversations.map((conv) => (
                    <li key={conv.id}>
                        <p><strong>Début :</strong> {new Date(conv.start_time).toLocaleString()}</p>
                        <p><strong>Fin :</strong> {conv.end_time ? new Date(conv.end_time).toLocaleString() : "En cours"}</p>
                        <p><strong>Status :</strong> {conv.status}</p>
                        <p><strong>Messages :</strong></p>
                        <ul>
                            {conv.messages.map((msg, index) => (
                                <li key={index}>
                                    <strong>{msg.role}:</strong> {msg.message}
                                    <em> ({new Date(msg.timestamp).toLocaleString()})</em>
                                </li>
                            ))}
                        </ul>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default Conversations;
