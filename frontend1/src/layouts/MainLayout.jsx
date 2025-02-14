import { useState } from "react";
import React from "react";
import { Outlet, useLocation } from "react-router-dom";
import Sidebar from "../components/Sidebar";
import ChatSidebar from "../components/ChatSidebar";
import "./MainLayout.css";

const MainLayout = () => {
    const location = useLocation();
    const [showMainSidebar, setShowMainSidebar] = useState(true);
    const isChatRoute = location.pathname === "/chatbot";
    const showChatSidebar = isChatRoute && !showMainSidebar;

    // ✅ États partagés pour les conversations et messages
    const [conversations, setConversations] = useState([]);
    const [activeChat, setActiveChat] = useState(null);
    const [messages, setMessages] = useState([]);

    return (
        <div className="app-layout">
            <div className="app-content">
                {showMainSidebar && <Sidebar />}
                {showChatSidebar && (
                    <ChatSidebar
                        onShowMainMenu={() => setShowMainSidebar(true)}
                        conversations={conversations}
                        setConversations={setConversations}
                        activeChat={activeChat}
                        setActiveChat={setActiveChat}
                        setMessages={setMessages}
                    />
                )}
                <main className="main-content">
                    <Outlet
                        context={{
                            setShowMainSidebar,
                            conversations,
                            setConversations,
                            activeChat,
                            setActiveChat,
                            messages,
                            setMessages
                        }}
                    />
                </main>
            </div>
        </div>
    );
};

export default MainLayout;
