// eslint-disable-next-line no-unused-vars
import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import axiosInstance from "../Utils/axios-instance";
import "../styles/ConversationDetails.css";

const ConversationDetails = () => {
    const { id } = useParams();
    const [messages, setMessages] = useState([]);

    useEffect(() => {
        const fetchConversationDetails = async () => {
            try {
                const response = await axiosInstance.get(`/conversations/${id}`);
                setMessages(response.data.messages || []);
            } catch (error) {
                console.error("Failed to fetch conversation details:", error);
            }
        };

        fetchConversationDetails();
    }, [id]);

    return (
        <div className="conversation-details">
            <h2>Conversation Details</h2>
            <div className="messages">
                {messages.map((message, index) => (
                    <div
                        key={index}
                        className={`message ${
                            message.sender === "user" ? "user-message" : "bot-message"
                        }`}
                    >
                        {message.content}
                    </div>
                ))}
            </div>
        </div>
    );
};

export default ConversationDetails;
