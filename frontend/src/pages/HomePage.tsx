/**
 * Home Page Component for direct audio transcription.
 * 
 * This component provides the main interface for users to upload audio files
 * and receive instant transcription results. It uses the simplified workflow
 * without job queuing or polling, providing immediate feedback.
 * 
 * Key Features:
 *   - File upload with drag-and-drop interface
 *   - Instant transcription results display
 *   - Error handling and user feedback
 *   - Responsive design for all devices
 * 
 * Workflow:
 *   1. User selects audio file
 *   2. User clicks transcribe button
 *   3. File is processed instantly
 *   4. Results are displayed immediately
 * 
 * @author shangmin
 * @version 2.0
 * @since 2024
 */

import React, { useState } from 'react';
import { FileUpload } from '../components/upload/FileUpload';
import { Button } from '../components/common/Button';
import { ResultsView } from '../components/results/ResultsView';
import { useTranscription } from '../hooks/useTranscription';
import { Upload, Mic, Zap, Shield, ArrowLeft } from 'lucide-react';

/**
 * HomePage component that handles the complete transcription workflow.
 * 
 * This component manages the user interface for file upload, transcription
 * processing, and results display in a single, streamlined experience.
 */
export const HomePage: React.FC = () => {
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  
  const {
    transcribeAudio,
    transcriptionResult,
    isTranscribing,
    isCompleted,
    error,
    clearError,
    reset,
  } = useTranscription();

  /**
   * Handle file selection from the upload component.
   * 
   * @param file The selected audio file
   */
  const handleFileSelect = (file: File) => {
    setSelectedFile(file);
    clearError();
  };

  /**
   * Handle transcription initiation.
   * 
   * Starts the direct transcription process for the selected file.
   */
  const handleTranscribe = async () => {
    if (!selectedFile) return;

    try {
      await transcribeAudio(selectedFile);
    } catch (error) {
      console.error('Transcription failed:', error);
    }
  };

  /**
   * Handle starting over with a new file.
   * 
   * Resets the entire workflow to allow for a new transcription.
   */
  const handleStartOver = () => {
    setSelectedFile(null);
    reset();
  };

  return (
    <div className="space-y-8">
      {/* Show results if transcription is completed */}
      {isCompleted && transcriptionResult ? (
        <div className="space-y-6">
          {/* Back to Upload Button */}
          <div className="flex justify-center">
            <Button
              onClick={handleStartOver}
              variant="outline"
              className="flex items-center gap-2"
            >
              <ArrowLeft className="h-4 w-4" />
              Transcribe Another File
            </Button>
          </div>
          
          {/* Results Display */}
          <ResultsView result={transcriptionResult} />
        </div>
      ) : (
        <>
          {/* Hero Section */}
          <div className="text-center space-y-6">
            <div className="flex justify-center">
              <div className="p-4 bg-blue-100 dark:bg-blue-900/20 rounded-full">
                <Mic className="h-12 w-12 text-blue-600 dark:text-blue-400" />
              </div>
            </div>
            
            <h1 className="text-4xl font-bold text-gray-900 dark:text-white">
              Transform Audio to Text Instantly
            </h1>
            
            <p className="text-xl text-gray-600 dark:text-gray-300 max-w-2xl mx-auto">
              Upload your audio files and get accurate transcriptions immediately. 
              No waiting, no polling - instant results powered by AI.
            </p>
          </div>

          {/* Features */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div className="text-center p-6 bg-white dark:bg-gray-800 rounded-xl shadow-sm border border-gray-200 dark:border-gray-700">
              <div className="flex justify-center mb-4">
                <Zap className="h-8 w-8 text-yellow-500" />
              </div>
              <h3 className="text-lg font-semibold text-gray-900 dark:text-white mb-2">
                Instant Results
              </h3>
              <p className="text-gray-600 dark:text-gray-300">
                Get your transcriptions immediately - no waiting or polling required
              </p>
            </div>
            
            <div className="text-center p-6 bg-white dark:bg-gray-800 rounded-xl shadow-sm border border-gray-200 dark:border-gray-700">
              <div className="flex justify-center mb-4">
                <Shield className="h-8 w-8 text-green-500" />
              </div>
              <h3 className="text-lg font-semibold text-gray-900 dark:text-white mb-2">
                Secure & Private
              </h3>
              <p className="text-gray-600 dark:text-gray-300">
                Your files are processed securely and not stored permanently
              </p>
            </div>
            
            <div className="text-center p-6 bg-white dark:bg-gray-800 rounded-xl shadow-sm border border-gray-200 dark:border-gray-700">
              <div className="flex justify-center mb-4">
                <Upload className="h-8 w-8 text-blue-500" />
              </div>
              <h3 className="text-lg font-semibold text-gray-900 dark:text-white mb-2">
                Multiple Formats
              </h3>
              <p className="text-gray-600 dark:text-gray-300">
                Support for MP3, WAV, M4A, FLAC, OGG, and WMA files up to 25MB
              </p>
            </div>
          </div>

          {/* Upload Section */}
          <div className="max-w-2xl mx-auto">
            <div className="card p-8">
              <div className="space-y-6">
                <div className="text-center">
                  <h2 className="text-2xl font-bold text-gray-900 dark:text-white mb-2">
                    Upload Your Audio
                  </h2>
                  <p className="text-gray-600 dark:text-gray-300">
                    Drag and drop your audio file or click to browse
                  </p>
                </div>

                <FileUpload
                  onFileSelect={handleFileSelect}
                  disabled={isTranscribing}
                />

                {selectedFile && !isTranscribing && (
                  <div className="space-y-4">
                    <div className="flex justify-center">
                      <Button
                        onClick={handleTranscribe}
                        size="lg"
                        className="w-full md:w-auto"
                      >
                        Transcribe Audio
                      </Button>
                    </div>
                    
                    <div className="text-center">
                      <Button
                        variant="ghost"
                        onClick={handleStartOver}
                      >
                        Choose Different File
                      </Button>
                    </div>
                  </div>
                )}

                {isTranscribing && (
                  <div className="text-center space-y-4">
                    <div className="flex justify-center">
                      <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
                    </div>
                    <p className="text-gray-600 dark:text-gray-300">
                      Transcribing your audio... This may take a moment.
                    </p>
                  </div>
                )}

                {error && (
                  <div className="p-4 bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 rounded-lg">
                    <p className="text-sm text-red-700 dark:text-red-300 text-center">
                      {error}
                    </p>
                  </div>
                )}
              </div>
            </div>
          </div>

          {/* Instructions */}
          <div className="max-w-4xl mx-auto">
            <div className="card p-6">
              <h3 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">
                How it works
              </h3>
              
              <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                <div className="text-center">
                  <div className="w-12 h-12 bg-blue-100 dark:bg-blue-900/20 rounded-full flex items-center justify-center mx-auto mb-3">
                    <span className="text-blue-600 dark:text-blue-400 font-bold">1</span>
                  </div>
                  <h4 className="font-medium text-gray-900 dark:text-white mb-2">
                    Upload Audio
                  </h4>
                  <p className="text-sm text-gray-600 dark:text-gray-300">
                    Select your audio file using drag & drop or file browser
                  </p>
                </div>
                
                <div className="text-center">
                  <div className="w-12 h-12 bg-blue-100 dark:bg-blue-900/20 rounded-full flex items-center justify-center mx-auto mb-3">
                    <span className="text-blue-600 dark:text-blue-400 font-bold">2</span>
                  </div>
                  <h4 className="font-medium text-gray-900 dark:text-white mb-2">
                    Instant AI Processing
                  </h4>
                  <p className="text-sm text-gray-600 dark:text-gray-300">
                    Our AI transcribes your audio immediately with high accuracy
                  </p>
                </div>
                
                <div className="text-center">
                  <div className="w-12 h-12 bg-blue-100 dark:bg-blue-900/20 rounded-full flex items-center justify-center mx-auto mb-3">
                    <span className="text-blue-600 dark:text-blue-400 font-bold">3</span>
                  </div>
                  <h4 className="font-medium text-gray-900 dark:text-white mb-2">
                    View Results
                  </h4>
                  <p className="text-sm text-gray-600 dark:text-gray-300">
                    See your transcription instantly with quality metrics
                  </p>
                </div>
              </div>
            </div>
          </div>
        </>
      )}
    </div>
  );
};
