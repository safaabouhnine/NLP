// eslint-disable-next-line no-unused-vars
import React from "react";

// eslint-disable-next-line react/prop-types
const ChatHistory = ({ messages }) => {
    return (
        <div className="chat-history">
            {/* eslint-disable-next-line react/prop-types */}
            {messages.map((msg, index) => (
                <div
                    key={index}
                    className={`message ${msg.role === "user" ? "user-message" : "bot-message"}`}
                >
                    {msg.content}
                </div>
            ))}
        </div>
    );
};

export default ChatHistory;
