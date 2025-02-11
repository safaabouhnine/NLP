// Sidebar.jsx
// eslint-disable-next-line no-unused-vars
import { useState } from "react"
import { Link, useLocation } from "react-router-dom"
import { LayoutDashboard, Calendar, Lightbulb, MessageSquare, Menu, Bot} from "lucide-react"
import "../styles/Sidebar.css";

// eslint-disable-next-line react/prop-types
const Sidebar = ({ user }) => {
    const location = useLocation();
    const [isOpen, setIsOpen] = useState(true);

/*    const toggleSidebar = () => {
        setIsSidebarOpen(!isSidebarOpen); // Basculer l'état de la barre latérale
    };*/

    const navItems = [
        { icon: LayoutDashboard, label: "Dashboard", path: "/dashboard" },
        { icon: Calendar, label: "Calendar", path: "/calendrier/:userId" },
        { icon: Lightbulb, label: "Recommandations", path: "/recommendations" },
        { icon: Bot, label: "Chatbot", path: "/chatbot" },
    ];

    return (
        <>
            {/* Bouton pour basculer la barre latérale */}
            {/*<button className={`sidebar-toggle ${isSidebarOpen ? "open" : "closed"}`} onClick={toggleSidebar}>
                <Menu size={20} />
            </button>*/}
            {/*<button className="sidebar-toggle" onClick={() => setIsOpen(!isOpen)} aria-label="Toggle sidebar">*/}
            <button className="app-sidebar-toggle" onClick={() => setIsOpen(!isOpen)} aria-label="Toggle sidebar">
                <Menu size={20}/>
            </button>

            {/*<div className={`sidebar ${isOpen ? "open" : ""}`}>
                <div className="sidebar-header">
                    <h1 className="logo">Calmify</h1>*/}
            <div className={`app-sidebar ${isOpen ? "open" : ""}`}>
                <div className="app-sidebar-header">
                    <h1 className="app-logo">Calmify</h1>
                </div>

                <nav className="app-sidebar-nav">
                    <ul>
                        {navItems.map((item) => (
                            <li key={item.path}>
                                <Link to={item.path} className={location.pathname === item.path ? "active" : ""}>
                                    <item.icon className="app-sidebar-icon" size={20}/>
                                    <span>{item.label}</span>
                                </Link>
                            </li>
                        ))}
                    </ul>
                </nav>

                <div className="app-sidebar-footer">
                    <button className="app-user-profile">
                        <div className="app-avatar">
                            <span>{user?.firstName?.[0] || "A"}</span>
                        </div>
                        <span className="app-username">
                            {user ? `${user.firstName} ${user.lastName}` : "Loading..."}
                        </span>
                    </button>
                </div>
            </div>
        </>
    )
}

export default Sidebar;


{/*  Barre latérale
            <div className={`sidebar ${isSidebarOpen ? "open" : "closed"}`}>
                <div className="sidebar-header">
                    <h1 className="logo">Calmify</h1>
                </div>
                <nav className="sidebar-nav">
                    <ul>
                        {navItems.map((item) => (
                            <li key={item.path}>
                                <Link
                                    to={item.path}
                                    className={location.pathname === item.path ? "active" : ""}
                                >
                                    <item.icon className="sidebar-icon" size={20}/>
                                    <span>{item.label}</span>
                                </Link>
                            </li>
                        ))}
                    </ul>
                </nav>
                <div className="sidebar-footer">
                    <button className="user-profile">
                        <div className="avatar">
                             eslint-disable-next-line react/prop-types
                            <span>{user?.firstName?.[0] || "A"}</span>
                        </div>
                        <span className="username">
                             eslint-disable-next-line react/prop-types
                            {user ? `${user.firstName} ${user.lastName}` : "Loading..."}
                        </span>
                        <ChevronDown size={16}/>
                    </button>
                </div>
            </div>
        </>
    );
};

export default Sidebar;*/}
