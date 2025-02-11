"use client";

import { useState } from "react";
import { ArrowLeft, Plus, Search, Menu, Trash, MessageSquareText } from "lucide-react";
import "../styles/ChatSidebar.css";

const ChatSidebar = ({
                         onShowMainMenu,
                         conversations,
                         setConversations,
                         activeChat,
                         setActiveChat,
                         setMessages
                     }) => {
    const [isOpen, setIsOpen] = useState(true);

    // ✅ Création d'une nouvelle conversation
    const handleNewChat = () => {
        const newChat = {
            id: Date.now(),
            title: `New Chat ${conversations.length + 1}`,
            messages: [],
        };

        setConversations((prev) => {
            const updatedChats = [...prev, newChat];
            setActiveChat(newChat.id);
            setMessages([]); // Reset messages for new chat
            return updatedChats;
        });
    };

    // ✅ Sélection d'un chat existant
    const handleSelectChat = (id) => {
        const selectedChat = conversations.find((chat) => chat.id === id);
        if (selectedChat) {
            setActiveChat(id);
            setMessages(selectedChat.messages);
        }
    };

    // ✅ Suppression des conversations
    const handleClearChats = () => {
        setConversations([]);
        setMessages([]);
        setActiveChat(null);
    };

    return (
        <>
            <button className="chat-sidebar-toggle" onClick={() => setIsOpen(!isOpen)} aria-label="Toggle chat sidebar">
                <Menu size={20} />
            </button>

            <div className={isOpen ? "chat-sidebar open" : "chat-sidebar"}>
                <div className="chat-sidebar-header">
                    <h1 className="chat-logo">Calmify</h1>
                    <button onClick={onShowMainMenu} className="back-to-main">
                        <ArrowLeft size={20}/>
                        <span>Show original menu</span>
                    </button>
                </div>

                <div className="chat-sidebar-content">
                    <button className="new-chat" onClick={handleNewChat}>
                        <Plus size={20}/>
                        <span>New chat</span>
                    </button>

                    <div className="chat-conversations-list">
                        <h2>Your conversations</h2>
                        <ul>
                            {conversations.map((chat) => (
                                <li
                                    key={chat.id}
                                    className={activeChat === chat.id ? "active" : ""}
                                    onClick={() => handleSelectChat(chat.id)}
                                >
                                    <MessageSquareText size={20}/>
                                    <span>{chat.title}</span>
                                </li>
                            ))}
                        </ul>
                    </div>

                    <button className="clear-chats" onClick={handleClearChats}>
                        <Trash size={20}/>
                        <span>Clear All Chats</span>
                    </button>

                </div>
            </div>
        </>
    );
};

export default ChatSidebar;
