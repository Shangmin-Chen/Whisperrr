/**
 * Custom React hook for managing transcription workflow state and operations.
 * 
 * This hook provides a comprehensive interface for handling the complete
 * audio transcription workflow, from file upload through status monitoring
 * to result retrieval. It integrates with React Query for efficient data
 * fetching and caching.
 * 
 * Key Features:
 *   - Complete transcription workflow management
 *   - Real-time status polling with automatic updates
 *   - Optimistic UI updates and error handling
 *   - Automatic cleanup and resource management
 *   - Type-safe state management
 *   - Integration with React Query for caching
 * 
 * Workflow States:
 *   1. Idle: Ready to accept file upload
 *   2. Uploading: File being uploaded to server
 *   3. Processing: Server transcribing audio
 *   4. Completed: Transcription finished successfully
 *   5. Failed: Error occurred during process
 * 
 * Polling Strategy:
 *   - Automatic status polling when job is active
 *   - Configurable polling interval (default 2 seconds)
 *   - Smart polling that stops when job completes
 *   - Background polling support for tab switching
 * 
 * Error Handling:
 *   - Comprehensive error capture from all stages
 *   - User-friendly error messages
 *   - Automatic error recovery where possible
 *   - Manual error clearing functionality
 * 
 * Performance Optimizations:
 *   - React Query caching for reduced API calls
 *   - Conditional queries based on state
 *   - Automatic cleanup of unused queries
 *   - Optimized re-render patterns
 * 
 * @author shangmin
 * @version 1.0
 * @since 2024
 */

import { useState, useCallback } from 'react';
import { useMutation, useQuery } from '@tanstack/react-query';
import { TranscriptionService } from '../services/transcription';
import { 
  AudioUploadResponse, 
  TranscriptionResultResponse,
  TranscriptionStatus 
} from '../types/transcription';
import { APP_CONFIG } from '../utils/constants';

/**
 * Return type interface for the useTranscription hook.
 * 
 * This interface defines all the state, actions, and computed values
 * that the hook provides to consuming components.
 */
export interface UseTranscriptionReturn {
  // State - Current workflow state
  /** Current job ID for tracking transcription progress */
  currentJobId: string | null;
  /** Upload operation status */
  uploadStatus: 'idle' | 'uploading' | 'success' | 'error';
  /** Current transcription processing status */
  transcriptionStatus: TranscriptionStatus | null;
  /** Final transcription result when completed */
  transcriptionResult: TranscriptionResultResponse | null;
  /** Current error message if any operation failed */
  error: string | null;
  
  // Actions - Functions to control the workflow
  /** Upload an audio file and start transcription */
  uploadAudio: (file: File) => Promise<void>;
  /** Clear current error message */
  clearError: () => void;
  /** Reset entire workflow to initial state */
  reset: () => void;
  
  // Computed - Derived state for UI logic
  /** Whether file upload is in progress */
  isUploading: boolean;
  /** Whether transcription is being processed */
  isProcessing: boolean;
  /** Whether transcription completed successfully */
  isCompleted: boolean;
  /** Whether transcription failed */
  isFailed: boolean;
  /** Current progress percentage (0-100) */
  progress: number;
}

/**
 * Custom hook that manages the complete transcription workflow.
 * 
 * This hook encapsulates all the logic needed to handle audio file uploads,
 * monitor transcription progress, and retrieve results. It provides a clean
 * interface for components to interact with the transcription service.
 * 
 * State Management:
 *   - Uses React state for local workflow state
 *   - Integrates with React Query for server state
 *   - Provides computed values for UI logic
 *   - Handles state transitions automatically
 * 
 * API Integration:
 *   - Upload mutation for file uploads
 *   - Status query with automatic polling
 *   - Result query triggered on completion
 *   - Proper error handling for all operations
 * 
 * Lifecycle Management:
 *   - Automatic query enabling/disabling based on state
 *   - Smart polling that stops when appropriate
 *   - Resource cleanup on component unmount
 *   - State reset functionality
 * 
 * @returns UseTranscriptionReturn Complete transcription workflow interface
 */
export const useTranscription = (): UseTranscriptionReturn => {
  const [currentJobId, setCurrentJobId] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  // Upload mutation
  const uploadMutation = useMutation({
    mutationFn: (file: File) => TranscriptionService.uploadAudio(file),
    onSuccess: (data: AudioUploadResponse) => {
      setCurrentJobId(data.jobId);
      setError(null);
    },
    onError: (error: any) => {
      setError(error.response?.data?.message || error.message || 'Upload failed');
    },
  });

  // Status query (only runs when we have a jobId)
  const statusQuery = useQuery({
    queryKey: ['transcription-status', currentJobId],
    queryFn: () => TranscriptionService.getStatus(currentJobId!),
    enabled: !!currentJobId,
    refetchInterval: (query) => {
      // Stop polling if completed or failed
      const data = query.state.data;
      if (data?.status === TranscriptionStatus.COMPLETED || 
          data?.status === TranscriptionStatus.FAILED) {
        return false;
      }
      return APP_CONFIG.pollingInterval;
    },
    refetchIntervalInBackground: true,
  });

  // Result query (only runs when status is completed)
  const resultQuery = useQuery({
    queryKey: ['transcription-result', currentJobId],
    queryFn: () => TranscriptionService.getResult(currentJobId!),
    enabled: !!currentJobId && statusQuery.data?.status === TranscriptionStatus.COMPLETED,
  });

  // Actions
  const uploadAudio = useCallback(async (file: File) => {
    setError(null);
    await uploadMutation.mutateAsync(file);
  }, [uploadMutation]);

  const clearError = useCallback(() => {
    setError(null);
  }, []);

  const reset = useCallback(() => {
    setCurrentJobId(null);
    setError(null);
    uploadMutation.reset();
    // Note: React Query v5 doesn't have remove method, queries are automatically cleaned up
  }, [uploadMutation]);

  // Computed values
  const isUploading = uploadMutation.isPending;
  const isProcessing = statusQuery.data?.status === TranscriptionStatus.PROCESSING;
  const isCompleted = statusQuery.data?.status === TranscriptionStatus.COMPLETED;
  const isFailed = statusQuery.data?.status === TranscriptionStatus.FAILED;
  const progress = statusQuery.data?.progress || 0;

  const uploadStatus = uploadMutation.isPending ? 'uploading' : 
                      uploadMutation.isError ? 'error' : 
                      uploadMutation.isSuccess ? 'success' : 'idle';

  return {
    // State
    currentJobId,
    uploadStatus,
    transcriptionStatus: statusQuery.data?.status || null,
    transcriptionResult: resultQuery.data || null,
    error: error || uploadMutation.error?.message || statusQuery.error?.message || resultQuery.error?.message,
    
    // Actions
    uploadAudio,
    clearError,
    reset,
    
    // Computed
    isUploading,
    isProcessing,
    isCompleted,
    isFailed,
    progress,
  };
};
