import '../styles/ShareLevel.css';
import PropTypes from 'prop-types';

const ShareLevel = ({ stressLevel }) => {
    return (
        <div className="metrics-card">
            <h3>Your Stress Level Today</h3>
            <div className="metric-value">
                {/* Affichage conditionnel pour le stress level */}
                {stressLevel !== null ? stressLevel : 'Loading...'}
            </div>
            <p className="metric-description">
                Remember you stress level today based on your discussion with the chatbot
            </p>
        </div>
    );
};

// Validation des props
ShareLevel.propTypes = {
    stressLevel: PropTypes.oneOfType([PropTypes.number, PropTypes.oneOf([null])]), // Accepte un nombre ou null
};

export default ShareLevel;

