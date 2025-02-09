// eslint-disable-next-line no-unused-vars
import React, { useState } from 'react';
import '../../Styles/Signup.css';
import lotusIcon from '../../assets/lotus.svg';
import waveTop from '../../assets/wave-top.svg';
import waveBottom from '../../assets/wave-bottom.svg';
import arc from '../../assets/arc.svg';
import meditation from '../../assets/meditation.svg';
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";
import axiosInstance from "../../Utils/axios-instance";
import {useNavigate} from "react-router-dom";


const Signup = () => {
    // State for password visibility toggle
    const [showPassword, setShowPassword] = useState(false);

    // State for form data and error messages
    const [formData, setFormData] = useState({
        fullName: '',
        email: '',
        password: '',
        phoneNumber: '',
        dateOfBirth: '',
        gender: '',
        educationalLevel: '',
        university: ''
    });

    const [error, setError] = useState(null); // State for error handling
    const navigate = useNavigate();
    const [success, setSuccess] = useState(false); // State for success message

    // Split fullName into firstName and lastName
    const splitName = () => {
        const names = formData.fullName.trim().split(" ");
        const firstName = names[0] || "";
        const lastName = names.slice(1).join(" ") || "";
        return { firstName, lastName };
    };

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null); // Reset error message
        setSuccess(false); // Reset success message

        const { firstName, lastName } = splitName();

        // Prepare data to send to the API
        const requestData = {
            firstName,
            lastName,
            email: formData.email,
            password: formData.password,
            phoneNumber: formData.phoneNumber,
            birthDate: formData.dateOfBirth,
            gender: formData.gender,
            educationLevel: formData.educationalLevel,
            university: formData.university,
            questionnaireCompleted: true // Assuming questionnaire is completed
        };

        try {
            // Call the API with Axios
            const response = await axiosInstance.post("/users/register", requestData);
            setSuccess(true); // Show success message
            console.log("User registered successfully:", response.data);

            const userId = response.data.user.id; // Correction ici
            localStorage.setItem("userId", userId);
            console.log(localStorage.getItem("userId"));
            // Stocker l'ID utilisateur dans le localStorage
            alert("Inscription réussie !");// Optionnel : Redirigez l'utilisateur ou effacez les champs du formulaire
            setFormData({
                fullName: '',
                email: '',
                password: '',
                phoneNumber: '',
                dateOfBirth: '',
                gender: '',
                educationalLevel: '',
                university: ''
            });


            navigate("/Questionnaire1");
        } catch (err) {
            console.error("Erreur lors de l'inscription :", err.response?.data || err.message);
            setError(err.response?.data || "An error occurred during signup.");
        }
    };

    return (
        <div className="signup-container">
            <div className="signup-card">
                {/* Left Side */}
                <div className="left-side">
                    <img src={arc} alt="Decorative arc" className="arc" />
                    <img src={lotusIcon} alt="Lotus" className="lotus-icon" />
                    <img src={waveTop} alt="" className="wave wave-top" />
                    <img src={waveBottom} alt="" className="wave wave-bottom" />
                    <img src={meditation} alt="" className="meditation-icon" />
                    <h2>Join our stress-free community<br />for a balanced life!</h2>
                </div>

                {/* Right Side */}
                <div className="right-side">
                    <div className="form-container">
                        <h1>Create an account</h1>
                        <p className="subtitle">Let&#39;s start your journey to balance and calmness.</p>

                        {/* Success and Error Messages */}
                        {success && <p className="success-message">Signup successful! Please login.</p>}
                        {error && <p className="error-message">{error}</p>}

                        <form onSubmit={handleSubmit}>
                            {/* Full Name */}
                            <div className="floating-label-Signup">
                                <input
                                    type="text"
                                    name="fullName"
                                    className="floating-input-Signup"
                                    placeholder=" "
                                    value={formData.fullName}
                                    onChange={handleChange}
                                    required
                                />
                                <label className="floating-label-text-Signup">FirstName LastName</label>
                            </div>

                            {/* Email Address */}
                            <div className="floating-label-Signup">
                                <input
                                    type="email"
                                    name="email"
                                    className="floating-input-Signup"
                                    placeholder=" "
                                    value={formData.email}
                                    onChange={handleChange}
                                    required
                                />
                                <label className="floating-label-text-Signup">Email Address</label>
                            </div>

                            {/* Password with toggle */}
                            <div className="floating-label-Signup">
                                <input
                                    type={showPassword ? "text" : "password"}
                                    name="password"
                                    className="floating-input-Signup"
                                    placeholder=" "
                                    value={formData.password}
                                    onChange={handleChange}
                                    required
                                />
                                <label className="floating-label-text-Signup">Password</label>
                                <span className="password-toggle" onClick={() => setShowPassword(!showPassword)}>
                                    {showPassword ? <Visibility className="password-icon" /> : <VisibilityOff className="password-icon" />}
                                </span>
                            </div>

                            {/* Other fields */}
                            <div className="floating-label-Signup">
                                <input
                                    type="tel"
                                    name="phoneNumber"
                                    className="floating-input-Signup"
                                    placeholder=" "
                                    value={formData.phoneNumber}
                                    onChange={handleChange}
                                    required
                                />
                                <label className="floating-label-text-Signup">Phone Number</label>
                            </div>

                            <div className="date-gender-container">
                                <div className="floating-label-Signup">
                                    <input
                                        type="date"
                                        name="dateOfBirth"
                                        className="floating-input-Signup"
                                        placeholder=" "
                                        value={formData.dateOfBirth}
                                        onChange={handleChange}
                                        required
                                    />
                                    <label className="floating-label-text-Signup">Date of Birth</label>
                                </div>

                                <div className="floating-label-Signup">
                                    <select
                                        name="gender"
                                        className="floating-input-Signup"
                                        value={formData.gender}
                                        onChange={handleChange}
                                        required
                                    >
                                        <option value="" disabled>Gender</option>
                                        <option value="Male">Male</option>
                                        <option value="Female">Female</option>
                                    </select>
                                    <label className="floating-label-text-Signup">Gender</label>
                                </div>
                            </div>

                            <div className="floating-label-Signup">
                                <input
                                    type="text"
                                    name="educationalLevel"
                                    className="floating-input-Signup"
                                    placeholder=" "
                                    value={formData.educationalLevel}
                                    onChange={handleChange}
                                    required
                                />
                                <label className="floating-label-text-Signup">Educational Level</label>
                            </div>

                            <div className="floating-label-Signup">
                                <input
                                    type="text"
                                    name="university"
                                    className="floating-input-Signup"
                                    placeholder=" "
                                    value={formData.university}
                                    onChange={handleChange}
                                    required
                                />
                                <label className="floating-label-text-Signup">University</label>
                            </div>

                            <div className="form-footer">
                                <p className="learn-more">Learn how Calmify helps you manage stress</p>

                                <button  type="submit" className="continue-btn">Continue</button>

                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Signup;