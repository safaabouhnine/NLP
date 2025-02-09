import React, {useState} from "react";
import '../styles/ForgetPassword.css';
import axiosInstance from "../Utils/axios-instance.js";

export default function ForgetPassword() {
    // State to manage the email input and feedback messages
    const [email, setEmail] = useState(""); // Stores the email entered by the user
    const [message, setMessage] = useState(""); // Success message
    const [error, setError] = useState(""); // Error message

    // Function to handle form submission
    const handleSubmit = async (e) => {
        e.preventDefault(); // Prevent the default form submission behavior
        setMessage(""); // Reset success message
        setError(""); // Reset error message
        try {
            // Make the POST request to the backend API for password reset
            const response = await axiosInstance.post("/users/reset-password/request", { email });
            setMessage("Password reset email sent successfully! Please check your inbox."); // Set success message
        } catch (err) {
            // Handle errors from the backend and display an error message
            setError(err.response?.data || "An error occurred. Please try again.");
        }
    };

    return (
        <div className="background">
            <div className="card">
                <h1 className="heading">Forgot your password?</h1>
                <p className="subheading">
                    Don&#39;t worry, it happens to the best of us! Let&#39;s get you back in.
                </p>
                <form className="form" onSubmit={handleSubmit}>
                    <label htmlFor="email" className="input-label">
                        Enter your email to recover your account:
                    </label>
                    {/* Floating Label Wrapper */}
                    <div className="floating-label">
                    <input
                        type="email"
                        id="email"
                        name="email"
                        className="floating-input"
                        placeholder=" "
                        value={email} // Bind input value to email state
                        onChange={(e) => setEmail(e.target.value)} // Update email state on input change
                        required // Make the input field required
                    />
                        <label htmlFor="email" className="floating-label-text">
                            Your email
                        </label>
                    </div>
                    {/* Submit Button */}
                    <button type="submit" className="submit-button">
                        Send Recovery link
                    </button>
                </form>

                {/* Display success or error messages */}
                {message && <p style={{ color: "green", marginTop: "20px" }}>{message}</p>} {/* Success message */}
                {error && <p style={{ color: "red", marginTop: "20px" }}>{error}</p>} {/* Error message */}
            </div>
        </div>
    );
}
