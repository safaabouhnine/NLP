// src/App.jsx
import  { useState } from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import AuthLayout from "./layouts/AuthLayout";
import MainLayout from "./layouts/MainLayout";
import Signup from "./pages/Signup/Signup.jsx";
import Login from "./pages/Login.jsx";
import Questionnaire1 from "./pages/Signup/Questionnaire1.jsx";
import ForgetPassword from "./pages/ForgetPassword";
import ResetPassword from "./pages/ResetPassword";
import Questionnaire2 from "./pages/Signup/Questionnaire2.jsx";
//import Dashboard from './pages/Dashboard';
import CalendarView from "./pages/CalendarView.jsx";
import Recommendations from "./pages/Recommendations.jsx";
import Chatbot from "./pages/Chatbot.jsx";
import './App.css';
import {Sidebar} from "lucide-react";
import Dashboard from "./pages/Dashboard.jsx";
import LandingPage from "./pages/index.tsx"

const App = () => {
    const [questionnaireData, setQuestionnaireData] = useState({});
    return (
        <Router>
            <Routes>
                <Route path="/" element={<LandingPage />} />

                {/* Auth Pages (No Sidebar) */}
                <Route element={<AuthLayout />}>

                    <Route path="/login" element={<Login />} />
                    <Route path="/signup" element={<Signup />} />
                    <Route path="/forget-password" element={<ForgetPassword />} />
                    <Route path="/reset-password" element={<ResetPassword />} />
                </Route>

                {/* Main Pages (With Sidebar) */}
                <Route element={<MainLayout />}>
                    <Route path="/dashboard" element={<Dashboard/>} />
                    <Route path="/calendrier/:userId" element={<CalendarView/>} />
                    <Route path="/recommendations" element={<Recommendations />} />
                    {/* Chatbot avec Sidebar */}
                    <Route path="/chatbot" element={<Chatbot />} />
                    {/*<Route path="/conversations/:id" element={<ConversationDetails />} />*/}
                </Route>

                {/* Other Pages */}
                <Route path="/Questions" element={<Questionnaire1 />} />
                <Route path="/Questionnaire2" element={<Questionnaire2 />} />
                <Route path="/Questionnaire1" element={<Questionnaire1 setQuestionnaireData={setQuestionnaireData} />} />
                <Route path="/Questionnaire2" element={<Questionnaire2 questionnaireData={questionnaireData} />} />
            </Routes>
        </Router>
    );
};

export default App;