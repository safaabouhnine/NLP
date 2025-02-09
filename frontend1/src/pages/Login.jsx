// eslint-disable-next-line no-unused-vars
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import meditationImage from "../assets/meditation.png";
import "../styles/Login.css";

// Import Material UI icons
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";
import axiosInstance from "../Utils/axios-instance.js"; // Import de l'instance Axios

export default function Login() {
    const [email, setEmail] = useState(""); // État pour l'email
    const [password, setPassword] = useState(""); // État pour le mot de passe
    const [error, setError] = useState(null); // État pour les erreurs
    const [showPassword, setShowPassword] = useState(false); // État pour afficher/masquer le mot de passe
    const navigate = useNavigate();

    // Fonction pour basculer la visibilité du mot de passe
    const togglePasswordVisibility = () => {
        setShowPassword(!showPassword);
    };

    // Fonction pour gérer la soumission du formulaire
    const handleLogin = async (e) => {
        e.preventDefault(); // Empêche le rafraîchissement de la page
        try {
            // Appel de l'API avec les données d'email et de mot de passe
            const response = await axiosInstance.post("users/login", { email, password });
            const { token, user } = response.data;

            // Sauvegarder le token dans localStorage
            localStorage.setItem("token", token);

            // Redirect to /dashboard
            navigate("/dashboard");

            // Affiche un message de succès ou redirige vers une autre page
            alert(`Welcome ${user.firstName} ${user.lastName}!`);
        } catch (err) {
            console.error("Erreur lors de la connexion :", err.response?.data || err.message);
            setError("Invalid email or password"); // Affiche un message d'erreur
        }
    };

    return (
        <section className="login-container">
            <div className="login-wrapper">
                {/* Left Section: Illustration */}
                <div className="illustration-section">
                    <img
                        src={meditationImage}
                        alt="Meditation Illustration"
                        className="illustration-image"
                    />
                </div>

                {/* Right Section: Form */}
                <div className="form-section">
                    <div className="form-wrapper">
                        <h1 className="form-title">Welcome Back!</h1>
                        <p className="form-subtitle">
                            If you already have an account, please fill in this Login Form:
                        </p>

                        {/* Formulaire de connexion */}
                        <form onSubmit={handleLogin}>
                            {/* Email input */}
                            <div className="floating-label">
                                <input
                                    type="email"
                                    id="email"
                                    name="email"
                                    className="floating-input"
                                    placeholder=" "
                                    value={email} // Liaison avec l'état
                                    onChange={(e) => setEmail(e.target.value)} // Met à jour l'état
                                />
                                <label htmlFor="email" className="floating-label-text">
                                    Email Address
                                </label>
                            </div>

                            {/* Password input */}
                            <div className="floating-label">
                                <input
                                    type={showPassword ? "text" : "password"}
                                    id="password"
                                    name="password"
                                    className="floating-input"
                                    placeholder=" "
                                    value={password} // Liaison avec l'état
                                    onChange={(e) => setPassword(e.target.value)} // Met à jour l'état
                                />
                                <label htmlFor="password" className="floating-label-text">
                                    Password
                                </label>
                                <span className="password-toggle" onClick={togglePasswordVisibility}>
                                    {showPassword ? (
                                        <Visibility className="password-icon" />
                                    ) : (
                                        <VisibilityOff className="password-icon" />
                                    )}
                                </span>
                            </div>

                            {/* Affichage des erreurs */}
                            {error && <p className="error">{error}</p>}

                            {/* Login Button */}
                            <div className="form-group">
                                <button type="submit" className="login-button">
                                    Login
                                </button>
                            </div>

                            {/* Sign Up Link */}
                            <p className="signup-text">
                                If you don’t have an account, you can sign up here:{" "}
                                <a href="/Signup" className="signup-link">
                                    Sign Up
                                </a>
                            </p>
                        </form>
                    </div>
                </div>
            </div>
        </section>
    );
}
