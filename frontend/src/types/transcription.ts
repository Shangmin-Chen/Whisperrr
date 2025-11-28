/**
 * TypeScript type definitions for the simplified Whisperrr transcription system.
 * 
 * This module defines all the data structures used in the direct transcription
 * workflow, including API request/response types and domain models. The types
 * are designed for instant transcription without job queuing or polling.
 * 
 * Key Changes from Previous Version:
 *   - Removed job-based types (AudioUploadResponse, TranscriptionStatusResponse)
 *   - Simplified TranscriptionResultResponse for direct results
 *   - Streamlined TranscriptionResult interface
 *   - Maintained compatibility with backend API
 * 
 * @author shangmin
 * @version 2.0
 * @since 2024
 */

/**
 * Response interface for direct transcription results.
 * 
 * This interface represents the complete transcription response returned
 * immediately after processing an audio file. It contains all transcription
 * data and metadata without any job-related information.
 */
export interface TranscriptionResultResponse {
  /** The complete transcribed text from the audio file */
  transcriptionText: string;
  
  /** Detected language code in ISO 639-1 format (e.g., "en", "es", "fr") */
  language?: string;
  
  /** Confidence score from 0.0 to 1.0 indicating transcription quality */
  confidence?: number;
  
  /** Duration of the audio file in seconds */
  duration?: number;
  
  /** Whisper model used for transcription (e.g., "base", "large") */
  modelUsed?: string;
  
  /** Processing time in seconds taken to transcribe the audio */
  processingTime?: number;
  
  /** Timestamp when transcription was completed */
  completedAt: string;
  
  /** Status of the transcription (should always be COMPLETED) */
  status: TranscriptionStatus;
}

/**
 * Individual segment of transcription with timing information.
 * 
 * This interface represents a time-bounded segment of the transcription,
 * useful for creating subtitles or synchronized playback.
 */
export interface TranscriptionSegment {
  /** Start time of the segment in seconds */
  startTime: number;
  
  /** End time of the segment in seconds */
  endTime: number;
  
  /** Transcribed text for this time segment */
  text: string;
  
  /** Confidence score for this segment (0.0 to 1.0) */
  confidence?: number;
}

/**
 * Enumeration of possible transcription statuses.
 * 
 * In the simplified system, most responses will have COMPLETED status,
 * but FAILED is used for error cases.
 */
export enum TranscriptionStatus {
  /** Transcription completed successfully */
  COMPLETED = 'COMPLETED',
  
  /** Transcription failed due to an error */
  FAILED = 'FAILED'
}

/**
 * Interface representing an audio file for upload.
 * 
 * This interface is used for client-side file handling and validation
 * before sending to the transcription service.
 */
export interface AudioFileInfo {
  /** Original filename of the uploaded file */
  filename: string;
  
  /** File size in bytes */
  size: number;
  
  /** Audio format/extension (e.g., "mp3", "wav") */
  format: string;
  
  /** MIME type of the audio file */
  mimeType: string;
  
  /** Duration of the audio file in seconds (if available) */
  duration?: number;
}

/**
 * Error response interface for API error handling.
 * 
 * This interface represents error responses from the transcription API,
 * providing structured error information for client handling.
 */
export interface TranscriptionError {
  /** Type of error that occurred */
  errorType: string;
  
  /** Human-readable error message */
  message: string;
  
  /** Additional error details */
  details?: Record<string, any>;
  
  /** Request correlation ID for debugging */
  correlationId?: string;
  
  /** Timestamp when the error occurred */
  timestamp: string;
}

/**
 * Configuration interface for transcription parameters.
 * 
 * This interface defines optional parameters that can be sent
 * with transcription requests to customize processing.
 */
export interface TranscriptionOptions {
  /** Preferred Whisper model size (tiny, base, small, medium, large) */
  modelSize?: 'tiny' | 'base' | 'small' | 'medium' | 'large';
  
  /** Language hint for better accuracy (ISO 639-1 code) */
  language?: string;
  
  /** Temperature for sampling (0.0 = deterministic, higher = more random) */
  temperature?: number;
  
  /** Task type: transcribe or translate to English */
  task?: 'transcribe' | 'translate';
}