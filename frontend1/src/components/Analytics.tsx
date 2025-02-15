import React, { useEffect } from "react";
import { useLocation } from "react-router-dom"; // Replace useRouter with useLocation
import { Helmet } from "react-helmet-async"; // Replace next/script
import * as gtag from "../lib/gtag"; // Keep your Google Analytics tracking file

const App = () => {
    const location = useLocation(); // Get the current route

    useEffect(() => {
        const handleRouteChange = (url: string) => {
            gtag.pageview(url); // Send pageview event to GA
        };

        handleRouteChange(location.pathname); // Track pageview on mount

        return () => {
            // Cleanup if needed
        };
    }, [location]); // Run when route changes

    return (
        <>
            {/* Google Analytics & Ads Scripts */}
            <Helmet>
                {/* Google Analytics */}
                <script async src={`https://www.googletagmanager.com/gtag/js?id=${gtag.GA_TRACKING_ID}`} />
                <script>
                    {`
            window.dataLayer = window.dataLayer || [];
            function gtag(){dataLayer.push(arguments);}
            gtag('js', new Date());
            gtag('config', '${gtag.GA_TRACKING_ID}', {
              page_path: window.location.pathname,
            });
          `}
                </script>

                {/* Google Ads */}
                <script async src={gtag.GA_ADS_ID} crossOrigin="anonymous"></script>
            </Helmet>
        </>
    );
};

export default App;
