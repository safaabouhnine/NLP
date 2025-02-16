
import { useState, useEffect, useRef } from "react";
import { useOutletContext } from "react-router-dom";
import "../styles/Chatbot.css";
import axiosInstance from "../AxiosInstance";

const Chatbot = () => {

    const { setShowMainSidebar, conversations, activeChat, setActiveChat, messages, setMessages } = useOutletContext();


    const [input, setInput] = useState("");
    const messagesEndRef = useRef(null);
    const userId = Number(localStorage.getItem("userId")); // ✅ Conversion en nombre
    const [correctConversationId, setCorrectConversationId] = useState(null);

    useEffect(() => {
        setShowMainSidebar(false);
        return () => setShowMainSidebar(true);
    }, [setShowMainSidebar]);

    useEffect(() => {
        if (correctConversationId) {
            console.log("✅ Utilisation de la conversation correcte :", correctConversationId);
            setActiveChat(correctConversationId); // ✅ Assure-toi que c'est bien 78
        }
    }, [correctConversationId]);


    // ✅ Met à jour les messages lorsque la conversation active change
    useEffect(() => {
        const chat = conversations.find((c) => c.id === activeChat);
        if (!chat) {
            console.log("⚠️ Conversation non trouvée dans la liste des conversations !");
        } else {
            console.log("✅ Conversation trouvée : ", chat);
            setCorrectConversationId(chat.id);
        }
        setMessages(chat ? chat.messages : []);
    }, [activeChat, conversations]);
    const analyzeMessage = async (text, conversationId) => {
        try {
            const response = await axiosInstance.post("http://localhost:8081/api/nlp/analyze", {
                text: text,
                studentId: userId,
                conversationId: conversationId,
            });

            console.log("✅ Analyse NLP stockée avec succès :", response.data);
        } catch (error) {
            console.error("❌ Erreur lors de l'analyse NLP :", error);
        }
    };

    // ✅ Auto-scroll to the last message when messages change
    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
    }, [messages]);

    // 🔹 Récupérer la dernière conversation stockée en base
    const fetchLatestConversationId = async () => {
        try {
            const response = await axiosInstance.get(`http://localhost:8081/api/conversations/latest`);
            setCorrectConversationId(response.data.idC);
            console.log("✅ Dernière conversation correcte récupérée :", response.data.idC);
        } catch (error) {
            console.error("❌ Erreur lors de la récupération de la dernière conversation :", error);
        }
    };

    useEffect(() => {
        fetchLatestConversationId();
    }, [userId, activeChat]); // Met à jour quand l'utilisateur ou la conversation active change

    // 🔹 Envoi de message
    const handleSendMessage = async () => {
        if (!input.trim()) return;

        // 🟢 Vérifier si correctConversationId est bien récupéré avant d'envoyer
        if (!correctConversationId) {
            console.warn("⚠️ Aucun ID de conversation disponible ! Récupération en cours...");
            await fetchLatestConversationId(); // ✅ Forcer la récupération avant d'envoyer
        }

        console.log("🔍 Vérification avant l'envoi :", {
            studentId: userId,
            conversationId: correctConversationId, // ✅ Devrait être 78
        });

        const nlpAnalysisData = {
            text: input,
            studentId: userId,
            conversationId: correctConversationId, // ✅ Vérifie qu'on envoie bien 78 ici
        };

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

            console.log("✅ Vérification studentId et conversationId :", {
                studentId: userId,
                conversationId: correctConversationId,
            });

            // 🔥 Forcer la récupération correcte avant d'envoyer NLP
            if (!correctConversationId) {
                console.warn("⚠️ L'ID de conversation est toujours null ! Tentative de correction...");
                await fetchLatestConversationId();
            }

            // 🔥 Envoi de l'analyse NLP avec le bon ID de conversation
            const nlpResponse = await axiosInstance.post("/nlp/analyze", nlpAnalysisData);
            console.log("✅ Analyse NLP stockée avec succès :", nlpResponse.data);
            analyzeMessage(input, correctConversationId);
        } catch (error) {
            console.error("❌ Erreur lors de l'envoi du message :", error);
            setMessages((prev) => [...prev, { id: Date.now(), content: "Error: Could not connect to the server.", sender: "bot" }]);
        }
    };

    return (
        <div className="chatbot-container">
            <div className="chat-main">
                <div className="chat-messages">
                    {messages.map((message) => (
                        <div key={message.id} className={`chat-message ${message.sender}`}>
                            <div dangerouslySetInnerHTML={{ __html: message.content }} />
                        </div>
                    ))}
                    {/* ✅ Invisible div to ensure scrolling to latest message */}
                    <div ref={messagesEndRef} />
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
