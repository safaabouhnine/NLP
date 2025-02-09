import axios from "axios";

// Configuration d'une instance Axios
const axiosInstance = axios.create({
    baseURL: "http://localhost:8081/api", // URL de base pour le backend
    headers: {
        "Content-Type": "application/json", // Type de contenu JSON pour toutes les requêtes
    },
});

// Intercepteur pour ajouter le token d'authentification à chaque requête
axiosInstance.interceptors.request.use((config) => {
    const token = localStorage.getItem("token"); // Récupère le token depuis localStorage
    if (token) {
        config.headers.Authorization = `Bearer ${token}`; // Ajoute le token dans l'en-tête
    }
    return config;
});

export default axiosInstance;
