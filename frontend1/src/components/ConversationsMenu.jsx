// eslint-disable-next-line no-unused-vars
import React, { useEffect, useState } from "react";
import axiosInstance from "../AxiosInstance";

// eslint-disable-next-line react/prop-types
const ConversationsMenu = ({ onSelectConversation }) => {
    const [conversations, setConversations] = useState([]);

    useEffect(() => {
        const fetchConversations = async () => {
            try {
                const response = await axiosInstance.get("/conversations");
                setConversations(response.data.conversations);
            } catch (error) {
                console.error("Failed to fetch conversations:", error);
            }
        };

        fetchConversations();
    }, []);

    return (
        <div style={{ padding: "20px" }}>
            <h2>Conversations</h2>
            <ul style={{ listStyle: "none", padding: 0 }}>
                {conversations.map((conversation) => (
                    <li
                        key={conversation.id}
                        style={{ marginBottom: "10px", cursor: "pointer" }}
                        onClick={() => onSelectConversation(conversation.id)}
                    >
                        <span style={{ color: "#007bff", textDecoration: "underline" }}>{conversation.title}</span>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default ConversationsMenu;
