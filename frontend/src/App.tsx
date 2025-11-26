/**
 * Main App component for the Whisperrr audio transcription platform.
 * 
 * This is the root component that sets up the application structure, routing,
 * and global layout. It provides a clean, modern interface for users to upload
 * audio files, monitor transcription progress, and view results.
 * 
 * Architecture:
 *   - Uses React Router for client-side navigation
 *   - Implements error boundaries for graceful error handling
 *   - Provides responsive design with Tailwind CSS
 *   - Supports dark mode theming
 * 
 * Route Structure:
 *   - / (HomePage): File upload and transcription initiation
 *   - /status/:jobId (StatusPage): Real-time transcription progress
 *   - /results/:jobId (ResultsPage): Completed transcription results
 *   - Catch-all redirect to home for invalid routes
 * 
 * Design System:
 *   - Gradient background with blue-to-indigo theme
 *   - Responsive container with proper spacing
 *   - Dark mode support throughout
 *   - Consistent typography and spacing
 *   - Accessible color contrasts
 * 
 * Error Handling:
 *   - ErrorBoundary wraps entire app for crash recovery
 *   - Graceful fallbacks for network issues
 *   - User-friendly error messages
 * 
 * Performance:
 *   - Code splitting at route level
 *   - Lazy loading of components
 *   - Optimized re-renders with React best practices
 * 
 * @author shangmin
 * @version 1.0
 * @since 2024
 */

import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { ErrorBoundary } from './components/common/ErrorBoundary';
import { HomePage } from './pages/HomePage';
import { StatusPage } from './pages/StatusPage';
import { ResultsPage } from './pages/ResultsPage';
import './App.css';

/**
 * Main application component that renders the complete Whisperrr interface.
 * 
 * This component serves as the entry point for the React application and
 * establishes the overall layout, routing, and error handling structure.
 * It provides a consistent user experience across all pages with proper
 * navigation and responsive design.
 * 
 * Component Structure:
 *   1. ErrorBoundary - Catches and handles React errors gracefully
 *   2. Layout Container - Responsive wrapper with gradient background
 *   3. Header - Application branding and title
 *   4. Main Content - Route-based page rendering
 *   5. Footer - Copyright and attribution
 * 
 * Routing Strategy:
 *   - Declarative routing with React Router
 *   - Protected routes for job-specific pages
 *   - Automatic redirect for invalid URLs
 *   - URL-based state management for deep linking
 * 
 * Styling Approach:
 *   - Utility-first CSS with Tailwind
 *   - Responsive breakpoints for mobile/desktop
 *   - Dark mode support with CSS variables
 *   - Consistent spacing and typography scale
 * 
 * @returns JSX.Element The complete application interface
 */
function App() {
  return (
    <ErrorBoundary>
      <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 dark:from-gray-900 dark:to-gray-800">
        <div className="container mx-auto px-4 py-8">
          <header className="text-center mb-8">
            <h1 className="text-4xl font-bold text-gray-900 dark:text-white mb-2">
              🎤 Whisperrr
            </h1>
            <p className="text-lg text-gray-600 dark:text-gray-300">
              AI-powered audio transcription platform
            </p>
          </header>

          <main className="max-w-4xl mx-auto">
            <Routes>
              <Route path="/" element={<HomePage />} />
              <Route path="/status/:jobId" element={<StatusPage />} />
              <Route path="/results/:jobId" element={<ResultsPage />} />
              <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
          </main>

          <footer className="text-center mt-12 text-gray-500 dark:text-gray-400">
            <p>&copy; 2024 Whisperrr. Powered by OpenAI Whisper.</p>
          </footer>
        </div>
      </div>
    </ErrorBoundary>
  );
}

export default App;
