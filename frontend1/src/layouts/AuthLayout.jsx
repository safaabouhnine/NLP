// src/layouts/AuthLayout.jsx
import PropTypes from 'prop-types'; // Add PropTypes for validation
import { Outlet } from 'react-router-dom'; // Import Outlet

const AuthLayout = () => {
    return (
        <div>
            <Outlet /> {/* Renders child routes */}
        </div>
    );
};

// Add PropTypes validation
AuthLayout.propTypes = {
    children: PropTypes.node, // node is required
};

export default AuthLayout;