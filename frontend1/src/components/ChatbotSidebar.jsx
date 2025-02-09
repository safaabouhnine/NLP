// eslint-disable-next-line no-unused-vars
import React, { useState } from "react";
import { FiArrowLeft, FiPlus, FiMenu } from "react-icons/fi";
import "../styles/Sidebar.css";

// eslint-disable-next-line react/prop-types
const ChatbotSidebar = ({
                            // eslint-disable-next-line react/prop-types
                            conversations,
                            // eslint-disable-next-line react/prop-types
                            onNewConversation,
                            // eslint-disable-next-line react/prop-types
                            activeConversation,
                            // eslint-disable-next-line react/prop-types
                            setActiveConversation,
                        }) => {
    const [isSidebarOpen, setIsSidebarOpen] = useState(true);

    const toggleSidebar = () => {
        setIsSidebarOpen(!isSidebarOpen); // Basculer l'état ouvert/fermé
    };

    return (
        <div className={`sidebar-container ${isSidebarOpen ? "open" : "closed"}`}>
            {/* Bouton toujours visible pour basculer la barre latérale */}
            <button
                className={`sidebar-toggle-button`}
                onClick={toggleSidebar}
                style={{
                    position: "absolute",
                    top: "20px",
                    left: isSidebarOpen ? "260px" : "20px", // Position du bouton selon l'état
                    zIndex: "1001",
                    backgroundColor: "#fff",
                    border: "1px solid #ddd",
                    borderRadius: "5px",
                    padding: "5px",
                }}
            >
                <FiMenu size={20} />
            </button>

            {/* Contenu de la barre latérale */}
            <div className={`sidebar ${isSidebarOpen ? "open" : "closed"}`}>
                {isSidebarOpen && (
                    <div>
                        <div className="sidebar-header">
                            <h1 className="logo">Calmify</h1>
                        </div>

                        {/* Bouton Nouvelle Conversation */}
                        <button className="new-conversation" onClick={onNewConversation}>
                            <FiPlus size={20} /> Nouvelle conversation
                        </button>

                        {/* Section des conversations */}
                        <h2>Historique</h2>
                        <ul>
                            {/* eslint-disable-next-line react/prop-types */}
                            {conversations.length > 0 ? (
                                // eslint-disable-next-line react/prop-types
                                conversations.map((conversation) => (
                                    <li
                                        key={conversation.id}
                                        className={
                                            activeConversation === conversation.id
                                                ? "active"
                                                : ""
                                        }
                                        onClick={() => setActiveConversation(conversation.id)}
                                    >
                                        {conversation.title ||
                                            `Conversation ${conversation.id}`}
                                    </li>
                                ))
                            ) : (
                                <p>Aucune conversation disponible</p>
                            )}
                        </ul>

                        {/* Bouton pour revenir au dashboard */}
                        <button
                            className="back-to-dashboard"
                            onClick={() => (window.location.href = "/dashboard")}
                        >
                            <FiArrowLeft size={20} /> Back to Dashboard
                        </button>
                    </div>
                )}
            </div>
        </div>
    );
};

export default ChatbotSidebar;
