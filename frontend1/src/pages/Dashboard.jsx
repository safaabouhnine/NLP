import { useEffect, useState } from 'react';
import axiosInstance from "../Utils/axios-instance";
import ShareLevel from '../components/ShareLevel';
import StressLevelChart from '../components/StressLevelChart';
import Tips from '../components/Tips';
import '../styles/Dashboard.css';


const Dashboard = () => {
    const [userName, setUserName] = useState(null); //Etat pour stocker le prénom de l'utilisateur
    const [stressLevel, setStressLevel] = useState(null); // État pour stocker le stress level


    useEffect(() => {
        const fetchUserName = async () => {
            try {
                const response = await axiosInstance.get(`/users/me`); // Appel à l'API
                console.log('Données utilisateur pour Dashboard:', response.data); // Log pour debugging
                setUserName(response.data.firstName); // Récupère uniquement le prénom
            } catch (error) {
                console.error('Erreur lors de la récupération du nom utilisateur :', error);

                // Rediriger vers la page de connexion en cas de problème avec le token
                if (error.response?.status === 401) {
                    localStorage.removeItem('token');
                    window.location.href = '/login';
                }
            }
        };

        fetchUserName();
    }, []); // Exécuté une seule fois au montage du composant

    // Récupération du stress level pour l'utilisateur
    useEffect(() => {
        const fetchStressLevel = async () => {
            try {
                const userId = localStorage.getItem('userId'); // Récupérer l'ID utilisateur stocké
                const response = await axiosInstance.get(`/answers/user/${userId}/stress-level`);
                console.log('Stress level récupéré :', response.data.stressLevel); // Debugging
                setStressLevel(response.data.stressLevel); // Stocker le stress level
            } catch (error) {
                console.error('Erreur lors de la récupération du stress level :', error);

                // Gérer l'erreur si aucun score n'est trouvé
                if (error.response?.status === 404) {
                    setStressLevel(0); // Pas de score trouvé, afficher 0
                }
            }
        };

        fetchStressLevel();
    }, []); // Exécuté une seule fois au montage

    return (
        <div className="dashboard">
            <div className="dashboard-header">
                {/* Affiche "Good morning, Aya!" ou "Loading..." si les données ne sont pas encore prêtes */}
                <h2>Good morning, {userName || 'Loading'}!</h2>
                <div className="header-actions">
                    <span className="plan-badge">Free plan</span>
                    <button className="upgrade-button">Upgrade now</button>
                </div>
            </div>

            <div className="dashboard-content">
                <div className="metrics-row">
                    {/* Passer le stressLevel comme prop */}
                    <ShareLevel stressLevel={stressLevel} />
                    <div className="metrics-card">
                        <h3>Your Joy level Today</h3>
                        <div className="metric-value">3</div>
                    </div>
                    <div className="metrics-card">
                        <h3>Your Anger level Today</h3>
                        <div className="metric-value">3</div>
                    </div>
                    <div className="metrics-card">
                        <h3>Your Fear level Today</h3>
                        <div className="metric-value">4</div>
                    </div>
                </div>

                <div className="chart-section">
                    <StressLevelChart />
                </div>

                <div className="tips-section">
                    <Tips />
                </div>
            </div>
        </div>
    );
};

export default Dashboard;