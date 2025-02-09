import { Line } from 'react-chartjs-2';
import '../styles/ShareLevel.css';

import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend,
} from 'chart.js';

ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend
);

const StressLevelChart = () => {
    const data = {
        labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
        datasets: [
            {
                label: 'Stress Level',
                data: [40, 50, 60, 70, 80, 90],
                fill: false,
                borderColor: '#93C7D3',
                tension: 0.4,
                pointBackgroundColor: '#93C7D3',
                pointBorderColor: '#93C7D3',
                pointRadius: 4,
            },
        ],
    };

    const options = {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: {
                display: false,
            },
            title: {
                display: true,
                text: 'Your Stress Level Over Time',
                color: '#666',
                font: {
                    size: 16,
                    family: "'Playfair Display', serif",
                    weight: 'bold',
                },
                padding: {
                    bottom: 10
                }
            },
        },
        scales: {
            y: {
                beginAtZero: true,
                max: 100,
                grid: {
                    color: '#F5F5F5',
                },
                ticks: {
                    color: '#666',
                    font: {
                        size: 12
                    }
                }
            },
            x: {
                grid: {
                    display: false,
                },
                ticks: {
                    color: '#666',
                    font: {
                        size: 12
                    }
                }
            },
        },
    };

    return (
        // <div className="chart-card" style={{ height: '200px' ,}}>
        <div className="chart-card" style={{height: '300px',}}>
            <Line data={data} options={options}/>
        </div>
    );
};

export default StressLevelChart;

