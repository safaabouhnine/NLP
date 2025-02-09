// eslint-disable-next-line no-unused-vars
import React from 'react';
import '../styles/Chatbot.css';

const Chatbot = () => {
    return (
        <div className="chatbot-container">
            <iframe
                src="http://localhost:8000"
                title="Chatbot Interface"
            ></iframe>
        </div>
    );
};

export default Chatbot;
