import { useEffect, useState } from "react";
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import axios from "axios";
import "../styles/CalendarView.css"; // Import du fichier CSS pour les styles

const CalendarView = () => {
    const [userId, setUserId] = useState(null); // État pour stocker l'ID utilisateur
    const [events, setEvents] = useState([]);    // État pour les événements

    // 🔹 Fonction pour récupérer l'ID de l'utilisateur connecté (similaire à Recommendations.jsx)
    const fetchUserId = async () => {
        try {
            const token = localStorage.getItem("token");
            if (!token) {
                throw new Error("Utilisateur non authentifié");
            }

            const response = await axios.get("http://localhost:8081/api/users/me", {
                headers: { Authorization: `Bearer ${token}` },
            });

            console.log("✅ User ID récupéré :", response.data.id);
            setUserId(response.data.id); // Stocke l'ID dans le state
        } catch (err) {
            console.error("❌ Erreur lors de la récupération de l'ID utilisateur :", err);
        }
    };

    // 🔹 Récupération de l'ID utilisateur au chargement du composant
    useEffect(() => {
        fetchUserId(); // Appelle la fonction pour récupérer l'ID de l'utilisateur
    }, []);

    // 🔹 Récupération des événements liés aux recommandations lorsque userId est disponible
    useEffect(() => {
        if (!userId) {
            console.warn("⏳ En attente de l'ID utilisateur...");
            return;
        }

        axios
            .get(`http://localhost:8081/api/recommendations/user/${userId}/with-events`)
            .then((response) => {
                const formattedEvents = response.data
                    .filter((rec) => rec.event) // Ne prend que les recommandations avec événement
                    .map((rec) => ({
                        title: rec.event.title,
                        start: rec.event.start,
                        end: rec.event.end,
                        description: rec.event.description,
                        url: rec.event.link,
                        color: rec.type === "Meeting" ? "#FF6B6B" : rec.type === "Video" ? "#4ECDC4" : "#FFD93D", // Colorie par type
                    }));

                console.log("✅ Événements récupérés :", formattedEvents);
                setEvents(formattedEvents); // Met à jour les événements pour le calendrier
            })
            .catch((error) => {
                console.error("❌ Erreur lors de la récupération des événements :", error);
            });
    }, [userId]); // L'effet se déclenche lorsque userId est disponible

    return (
        <div className="calendar-wrapper">
            <div className="calendar-container">
                <h1 className="calendar-title">EVENT CALENDAR</h1>
                <p className="calendar-subtitle">
                    Welcome to your calendar, where you can check your events!
                </p>
                <div className="calendar-content">
                    <FullCalendar
                        plugins={[dayGridPlugin]}
                        initialView="dayGridMonth"
                        headerToolbar={{
                            left: "prev,next today",
                            center: "title",
                            right: "dayGridMonth,dayGridWeek,dayGridDay",
                        }}
                        events={events}
                        eventContent={(eventInfo) => (
                            <div style={{ whiteSpace: "normal", padding: "10px" }}>
                                <strong>{eventInfo.timeText}</strong>
                                <div>{eventInfo.event.title}</div>
                                <small>{eventInfo.event.extendedProps.description}</small>
                            </div>
                        )}
                        locale="eng"
                        dayMaxEventRows={3}
                        moreLinkText={(num) => `+${num} more`}
                        eventClick={(info) => {
                            alert(`Titre : ${info.event.title}\nDescription : ${info.event.extendedProps.description}`);
                        }}
                    />
                </div>
            </div>
        </div>
    );
};

export default CalendarView;
