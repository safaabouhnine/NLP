// eslint-disable-next-line no-unused-vars
import React, { useState } from "react";
import ChatbotSidebar from "./ChatbotSidebar";
import "../styles/Chatbot.css";
import axiosInstance from "../AxiosInstance";

const Chatbot = () => {
    const [messages, setMessages] = useState([]);
    const [input, setInput] = useState("");
    const [conversations, setConversations] = useState([]);
    const [activeConversation, setActiveConversation] = useState(null);

    const startNewConversation = () => {
        const newConversation = {
            id: Date.now(),
            title: `Conversation ${conversations.length + 1}`,
            messages: [],
        };
        setConversations((prev) => [...prev, newConversation]);
        setMessages([]); // Réinitialiser les messages
        setActiveConversation(newConversation.id);
    };

    const loadConversation = (id) => {
        const conversation = conversations.find((conv) => conv.id === id);
        if (conversation) {
            setMessages(conversation.messages);
            setActiveConversation(id);
        }
    };

    const sendMessage = async () => {
        if (!input.trim()) return;

        const userMessage = { role: "user", content: input };
        setMessages((prev) => [...prev, userMessage]);

        try {
            const response = await axiosInstance.post("/chat", { text: input });
            const botResponse = {
                role: "bot",
                content: response.data.chatbot_response || "No response from server.",
            };
            setMessages((prev) => [...prev, botResponse]);

            setConversations((prev) =>
                prev.map((conv) =>
                    conv.id === activeConversation
                        ? { ...conv, messages: [...conv.messages, userMessage, botResponse] }
                        : conv
                )
            );
        } catch {
            setMessages((prev) => [
                ...prev,
                { role: "bot", content: "Error: Could not connect to the server." },
            ]);
        }
        setInput("");
    };

    return (
        <div className="chat-container">
            <ChatbotSidebar
                conversations={conversations}
                onNewConversation={startNewConversation}
                activeConversation={activeConversation}
                setActiveConversation={loadConversation}
            />
            <div className="chat-window">
                <div className="messages">
                    {messages.map((msg, index) => (
                        <div key={index} className={`message ${msg.role}`}>
                            {msg.content}
                        </div>
                    ))}
                </div>
                <div className="input-area">
                    <input
                        type="text"
                        value={input}
                        onChange={(e) => setInput(e.target.value)}
                        placeholder="Type your message..."
                    />
                    <button onClick={sendMessage}>Send</button>
                </div>
            </div>
        </div>
    );
};

export default Chatbot;
