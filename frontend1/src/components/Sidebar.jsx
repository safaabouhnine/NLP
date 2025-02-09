// Sidebar.jsx
// eslint-disable-next-line no-unused-vars
import React, { useState } from "react";
import { Link, useLocation } from "react-router-dom";
import {
    LayoutDashboard,
    Calendar,
    Lightbulb,
    Bot,
    ChevronDown,
    Menu,
} from "lucide-react";
import "../styles/Sidebar.css";

// eslint-disable-next-line react/prop-types
const Sidebar = ({ user }) => {
    const location = useLocation();
    const [isSidebarOpen, setIsSidebarOpen] = useState(true);

    const toggleSidebar = () => {
        setIsSidebarOpen(!isSidebarOpen); // Basculer l'état de la barre latérale
    };

    const navItems = [
        { icon: LayoutDashboard, label: "Dashboard", path: "/dashboard" },
        { icon: Calendar, label: "Calendrier", path: "/calendrier/:userId" },
        { icon: Lightbulb, label: "Recommandations", path: "/recommendations" },
        { icon: Bot, label: "Chatbot", path: "/chatbot" },
    ];

    return (
        <div>
            {/* Bouton pour basculer la barre latérale */}
            <button className={`sidebar-toggle ${isSidebarOpen ? "open" : "closed"}`} onClick={toggleSidebar}>
                <Menu size={20} />
            </button>

            {/* Barre latérale */}
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
                                    <item.icon className="sidebar-icon" size={20} />
                                    <span>{item.label}</span>
                                </Link>
                            </li>
                        ))}
                    </ul>
                </nav>
                <div className="sidebar-footer">
                    <button className="user-profile">
                        <div className="avatar">
                            {/* eslint-disable-next-line react/prop-types */}
                            <span>{user?.firstName?.[0] || "A"}</span>
                        </div>
                        <span className="username">
                            {/* eslint-disable-next-line react/prop-types */}
                            {user ? `${user.firstName} ${user.lastName}` : "Loading..."}
                        </span>
                        <ChevronDown size={16} />
                    </button>
                </div>
            </div>
        </div>
    );
};

export default Sidebar;
