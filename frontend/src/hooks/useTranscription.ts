/**
 * Simplified React hook for direct audio transcription.
 * 
 * This hook provides a streamlined interface for instant audio transcription
 * without job queuing or polling. It handles file uploads and returns
 * transcription results immediately, simplifying the user experience.
 * 
 * Key Features:
 *   - Direct transcription without job management
 *   - Instant results without polling
 *   - Simplified state management
 *   - Type-safe error handling
 *   - Integration with React Query for caching
 * 
 * Workflow:
 *   1. Idle: Ready to accept file upload
 *   2. Processing: File being transcribed
 *   3. Completed: Transcription results available
 *   4. Failed: Error occurred during transcription
 * 
 * Benefits:
 *   - No complex job state management
 *   - Immediate feedback to users
 *   - Reduced API complexity
 *   - Simplified error handling
 * 
 * @author shangmin
 * @version 2.0
 * @since 2024
 */

import { useState, useCallback } from 'react';
import { useMutation } from '@tanstack/react-query';
import { TranscriptionService } from '../services/transcription';
import { 
  TranscriptionResultResponse,
  TranscriptionStatus 
} from '../types/transcription';

/**
 * Return type interface for the simplified useTranscription hook.
 * 
 * This interface defines the streamlined state and actions provided
 * by the hook for direct transcription operations.
 */
export interface UseTranscriptionReturn {
  // State - Current transcription state
  /** Final transcription result when completed */
  transcriptionResult: TranscriptionResultResponse | null;
  /** Current error message if transcription failed */
  error: string | null;
  
  // Actions - Functions to control transcription
  /** Upload and transcribe an audio file instantly */
  transcribeAudio: (file: File) => Promise<void>;
  /** Clear current error message */
  clearError: () => void;
  /** Reset to initial state */
  reset: () => void;
  
  // Computed - Derived state for UI logic
  /** Whether transcription is in progress */
  isTranscribing: boolean;
  /** Whether transcription completed successfully */
  isCompleted: boolean;
  /** Whether transcription failed */
  isFailed: boolean;
  /** Whether the hook is in idle state (ready for new transcription) */
  isIdle: boolean;
}

/**
 * Custom hook for direct audio transcription.
 * 
 * This hook encapsulates the logic for instant audio transcription,
 * providing a simple interface for components to upload files and
 * receive transcription results immediately.
 * 
 * State Management:
 *   - Uses React state for transcription results and errors
 *   - Integrates with React Query mutation for API calls
 *   - Provides computed values for UI state
 *   - Simple state transitions: idle → transcribing → completed/failed
 * 
 * API Integration:
 *   - Single mutation for direct transcription
 *   - Proper error handling and user feedback
 *   - Type-safe response handling
 * 
 * Usage Example:
 *   ```tsx
 *   const { transcribeAudio, transcriptionResult, isTranscribing, error } = useTranscription();
 *   
 *   const handleFileUpload = async (file: File) => {
 *     await transcribeAudio(file);
 *   };
 *   ```
 * 
 * @returns UseTranscriptionReturn Simplified transcription interface
 */
export const useTranscription = (): UseTranscriptionReturn => {
  const [transcriptionResult, setTranscriptionResult] = useState<TranscriptionResultResponse | null>(null);
  const [error, setError] = useState<string | null>(null);

  // Direct transcription mutation
  const transcriptionMutation = useMutation({
    mutationFn: (file: File) => TranscriptionService.transcribeAudio(file),
    onSuccess: (data: TranscriptionResultResponse) => {
      setTranscriptionResult(data);
      setError(null);
    },
    onError: (error: any) => {
      setError(error.response?.data?.message || error.message || 'Transcription failed');
      setTranscriptionResult(null);
    },
  });

  // Actions
  const transcribeAudio = useCallback(async (file: File) => {
    setError(null);
    setTranscriptionResult(null);
    await transcriptionMutation.mutateAsync(file);
  }, [transcriptionMutation]);

  const clearError = useCallback(() => {
    setError(null);
  }, []);

  const reset = useCallback(() => {
    setTranscriptionResult(null);
    setError(null);
    transcriptionMutation.reset();
  }, [transcriptionMutation]);

  // Computed values
  const isTranscribing = transcriptionMutation.isPending;
  const isCompleted = transcriptionResult?.status === TranscriptionStatus.COMPLETED;
  const isFailed = transcriptionMutation.isError || transcriptionResult?.status === TranscriptionStatus.FAILED;
  const isIdle = !isTranscribing && !isCompleted && !isFailed;

  return {
    // State
    transcriptionResult,
    error: error || transcriptionMutation.error?.message,
    
    // Actions
    transcribeAudio,
    clearError,
    reset,
    
    // Computed
    isTranscribing,
    isCompleted,
    isFailed,
    isIdle,
  };
};
