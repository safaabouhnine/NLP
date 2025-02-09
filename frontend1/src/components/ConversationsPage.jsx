import { useState, useEffect } from "react";
import axiosInstance from "../AxiosInstance";
import '../styles/ConversationsPage.css';

const ConversationsPage = () => {
    const [conversations, setConversations] = useState([]);
    const [selectedConversation, setSelectedConversation] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchConversations = async () => {
            try {
                const response = await axiosInstance.get('/conversations');
                setConversations(response.data.conversations);
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

    return (
        <div style={{ display: "flex", height: "100vh" }}>
            {/* Liste des conversations */}
            <div style={{ width: "30%", borderRight: "1px solid #ccc", padding: "1rem", overflowY: "auto" }}>
                <h2>Conversations</h2>
                {conversations.map((conv) => (
                    <div
                        key={conv.id}
                        style={{
                            padding: "1rem",
                            cursor: "pointer",
                            backgroundColor: selectedConversation?.id === conv.id ? "#f0f0f0" : "white",
                        }}
                        onClick={() => setSelectedConversation(conv)}
                    >
                        <p><strong>Début :</strong> {new Date(conv.start_time).toLocaleString()}</p>
                        <p><strong>Status :</strong> {conv.status}</p>
                    </div>
                ))}
            </div>

            {/* Affichage de la conversation */}
            <div style={{ flex: 1, padding: "1rem", overflowY: "auto" }}>
                {selectedConversation ? (
                    <>
                        <h2>Conversation</h2>
                        <p><strong>Début :</strong> {new Date(selectedConversation.start_time).toLocaleString()}</p>
                        <p><strong>Fin :</strong> {selectedConversation.end_time ? new Date(selectedConversation.end_time).toLocaleString() : "En cours"}</p>
                        <p><strong>Status :</strong> {selectedConversation.status}</p>
                        <h3>Messages</h3>
                        <div style={{ border: "1px solid #ccc", padding: "1rem", borderRadius: "5px", backgroundColor: "#f9f9f9" }}>
                            {selectedConversation.messages.map((msg, index) => (
                                <div key={index} style={{ marginBottom: "1rem" }}>
                                    <strong>{msg.role}:</strong> {msg.message}
                                    <br />
                                    <em>({new Date(msg.timestamp).toLocaleString()})</em>
                                </div>
                            ))}
                        </div>
                    </>
                ) : (
                    <p>Sélectionnez une conversation pour afficher les détails.</p>
                )}
            </div>
        </div>
    );
};

export default ConversationsPage;
