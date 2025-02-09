import { useEffect, useState } from "react";
import { useParams } from "react-router-dom"; // Import pour récupérer les paramètres d'URL
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import axios from "axios";
import "../styles/CalendarView.css"; // Import du fichier CSS pour les styles

const CalendarView = () => {
    // Récupère l'ID de l'utilisateur à partir des paramètres de l'URL
    const { userId } = useParams();

    // State pour stocker les événements du calendrier
    const [events, setEvents] = useState([]);

    // Effet déclenché lorsque l'ID utilisateur change
    useEffect(() => {
        if (userId) {
            axios
                .get(`http://localhost:8081/api/calendar/user/${userId}/calendar`) // Requête pour récupérer les événements
                .then((response) => {
                    // Formater les événements avant de les afficher dans le calendrier
                    const formattedEvents = response.data.map((event) => ({
                        title: event.title,
                        start: event.start,
                        end: event.end,
                        description: event.description,
                        url: event.link,
                    }));
                    setEvents(formattedEvents); // Mise à jour du state avec les événements formatés
                })
                .catch((error) => {
                    console.error("Erreur lors de la récupération des événements :", error);
                });
        }
    }, [userId]); // L'effet se réexécute lorsque l'ID utilisateur change

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
                            left: 'prev,next today',
                            center: 'title',
                            right: 'dayGridMonth,dayGridWeek,dayGridDay'
                        }}
                        events={events}
                        eventContent={(eventInfo) => (
                            <div style={{ whiteSpace: 'normal', padding: '10px' }}>
                                <strong>{eventInfo.timeText}</strong>
                                <div>{eventInfo.event.title}</div> {/*exp:meeting with psychologist*/}
                            </div>
                        )}
                        locale="eng"
                        height='20px'
                        dayCellDidMount={(info) => {
                            info.el.style.height = '20px'; // Set the desired height
                        }}
                        buttonText={{
                            today: "Today",
                            month: 'Month',
                            week: 'Week',
                            day: 'Day',
                        }}
                        dayHeaderFormat={{
                            weekday: 'short',
                        }}
                        dayMaxEventRows={3} // Limit to 3 events per day
                        moreLinkText={(num) => `+${num} more`} // Customize "more" link text

                        eventClick={(info) => {
                            alert(`Titre : ${info.event.title}\nDescription : ${info.event.extendedProps.description}`);
                        }}
                        viewClassNames="calendar-view"
                    />
                </div>
            </div>
        </div>
    );
};

export default CalendarView;
