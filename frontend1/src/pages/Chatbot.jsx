import { useState, useEffect } from "react";
import { useOutletContext } from "react-router-dom";
import "../styles/Chatbot.css";
import axiosInstance from "../AxiosInstance";

const Chatbot = () => {
    const { setShowMainSidebar, conversations, setConversations, activeChat, setActiveChat, messages, setMessages } = useOutletContext();
    const [input, setInput] = useState("");

    useEffect(() => {
        setShowMainSidebar(false);
        return () => setShowMainSidebar(true);
    }, [setShowMainSidebar]);

    // ✅ Met à jour les messages lorsque la conversation active change
    useEffect(() => {
        const chat = conversations.find((c) => c.id === activeChat);
        setMessages(chat ? chat.messages : []);
    }, [activeChat, conversations]);

    // 🔹 Envoi de message
    const handleSendMessage = async () => {
        if (!input.trim()) return;

        const userMessage = { id: Date.now(), content: input, sender: "user" };
        setMessages((prev) => [...prev, userMessage]);
        setInput("");

        try {
            const response = await axiosInstance.post("/chat", { text: input });

            const botMessage = {
                id: Date.now(),
                content: response.data.chatbot_response || "No response from server.",
                sender: "bot",
            };

            setMessages((prev) => [...prev, botMessage]);

            // ✅ Mise à jour de la conversation
            setConversations((prev) =>
                prev.map((chat) =>
                    chat.id === activeChat ? { ...chat, messages: [...chat.messages, userMessage, botMessage] } : chat
                )
            );
        } catch {
            setMessages((prev) => [...prev, { id: Date.now(), content: "Error: Could not connect to the server.", sender: "bot" }]);
        }
    };

    return (
        <div className="chatbot-container">
            <div className="chat-main">
                <div className="chat-messages">
                    {messages.map((message) => (
                        <div key={message.id} className={`chat-message ${message.sender}`}>
                            {message.content}
                        </div>
                    ))}
                </div>

                <div className="chat-input-container">
                    <input
                        type="text"
                        value={input}
                        onChange={(e) => setInput(e.target.value)}
                        placeholder="Type your message..."
                        onKeyPress={(e) => e.key === "Enter" && handleSendMessage()}
                        className="chat-input"
                    />
                    <button onClick={handleSendMessage} className="chat-send-button">
                        Send
                    </button>
                </div>
            </div>
        </div>
    );
};

export default Chatbot;
