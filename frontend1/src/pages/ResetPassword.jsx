import React, {useEffect, useState} from "react";
import "../styles/ResetPassword.css";
import { useSearchParams } from "react-router-dom";
import axiosInstance from "../Utils/axios-instance";

// Import Material UI Icons (or use any other icon library)
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";

export default function ResetPassword() {
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);

    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const [message, setMessage] = useState("");
    const [error, setError] = useState("");

    const [searchParams] = useSearchParams();
    const token = searchParams.get("token"); // Extract token from the URL

    const togglePasswordVisibility = () => {
        setShowPassword(!showPassword);
    };

    const toggleConfirmPasswordVisibility = () => {
        setShowConfirmPassword(!showConfirmPassword);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage("");
        setError("");
        // Validate passwords
        if (newPassword !== confirmPassword) {
            setError("Passwords do not match!");
            return;
        }
        // Send token and new password to the backend
        try {
            const response = await axiosInstance.post("/users/reset-password/confirm", {
                token,
                newPassword,
            });
            setMessage("Password reset successfully! You will be redirected to the login page.");
        } catch (err) {
            setError(err.response?.data || "An error occurred. Please try again.");
        }
    };
    // Redirect to login page after success
    useEffect(() => {
        if (message) {
            const timer = setTimeout(() => {
                window.location.href = "/login"; // Redirect to login page
            }, 3000);
            return () => clearTimeout(timer); // Cleanup timer on unmount
        }
    }, [message]);

    return (
        <div className="background">
            <div className="card">
                <h1 className="heading">Reset your password</h1>
                <p className="subheading">
                    You&#39;re almost there! Let&#39;s secure your account with a new password.
                </p>
                <form className="form"  onSubmit={handleSubmit}>
                    {/* New Password Input */}
                    <div className="floating-label">
                        <input
                            type={showPassword ? "text" : "password"}
                            id="new-password"
                            name="new-password"
                            className="floating-input"
                            placeholder=" "
                            value={newPassword}
                            onChange={(e) => setNewPassword(e.target.value)}
                            required
                        />
                        <label htmlFor="new-password" className="floating-label-text">
                            Enter a new secure password
                        </label>
                        <span
                            className="password-toggle"
                            onClick={togglePasswordVisibility}
                        >
                          {showPassword ? (
                              <Visibility className="password-icon"/>
                          ) : (
                              <VisibilityOff className="password-icon"/>
                          )}
            </span>
                    </div>

                    {/* Confirm Password Input */}
                    <div className="floating-label">
                        <input
                            type={showConfirmPassword ? "text" : "password"}
                            id="confirm-password"
                            name="confirm-password"
                            className="floating-input"
                            placeholder=" "
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            required
                        />
                        <label htmlFor="confirm-password" className="floating-label-text">
                            Re-enter your password to confirm
                        </label>
                        <span
                            className="password-toggle"
                            onClick={toggleConfirmPasswordVisibility}
                        >
                        {showConfirmPassword ? (
                            <Visibility className="password-icon"/>
                        ) : (
                           <VisibilityOff className="password-icon"/>
                        )}
            </span>
            </div>

            {/* Submit Button */}
            <button type="submit" className="submit-button">
                Reset your password
            </button>
        </form>

                {/* Display success or error messages */}
                {message && <p style={{ color: "green", marginTop: "20px" }}>{message}</p>}
                {error && <p style={{ color: "red", marginTop: "20px" }}>{error}</p>}
</div>
</div>
)
    ;
}
