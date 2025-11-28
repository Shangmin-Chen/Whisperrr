/**
 * Simplified Transcription API Service for direct audio transcription.
 * 
 * This service provides instant transcription by communicating directly with
 * the backend API without job queuing or polling. It handles file uploads
 * and returns transcription results immediately.
 * 
 * Key Features:
 *   - Direct file upload and transcription
 *   - Instant results without polling
 *   - Simplified error handling
 *   - Type-safe API communication
 * 
 * Usage Flow:
 *   1. Upload audio file via transcribeAudio()
 *   2. Receive transcription results immediately
 * 
 * Benefits:
 *   - No job management complexity
 *   - Immediate feedback to users
 *   - Simplified state management
 *   - Reduced API calls
 * 
 * @author shangmin
 * @version 2.0
 * @since 2024
 */

import apiClient from './api';
import {
  TranscriptionResultResponse
} from '../types/transcription';

/**
 * Service class for direct transcription API operations.
 * 
 * This class provides static methods for instant audio transcription,
 * handling file upload and returning results immediately without any
 * job queuing or status polling mechanisms.
 */
export class TranscriptionService {
  /**
   * Upload and transcribe an audio file instantly.
   * 
   * This method uploads an audio file to the backend API and returns
   * the transcription results immediately. The file is processed
   * synchronously and results are available without polling.
   * 
   * Supported file formats:
   *   - MP3 (.mp3) - Most common compressed format
   *   - WAV (.wav) - Uncompressed, highest quality
   *   - M4A (.m4a) - Apple's compressed format
   *   - FLAC (.flac) - Lossless compression
   *   - OGG (.ogg) - Open-source compressed format
   *   - WMA (.wma) - Windows Media Audio
   * 
   * File constraints:
   *   - Maximum size: 25MB
   *   - Must be valid audio content
   *   - Supported audio MIME types
   * 
   * Response includes:
   *   - Complete transcribed text
   *   - Detected language code
   *   - Confidence score (0.0-1.0)
   *   - Audio duration in seconds
   *   - Processing time and metadata
   *   - Whisper model used
   * 
   * @param file The audio file to upload and transcribe
   * @returns Promise<TranscriptionResultResponse> Complete transcription result
   * @throws Error if file validation fails or transcription encounters issues
   */
  static async transcribeAudio(file: File): Promise<TranscriptionResultResponse> {
    const formData = new FormData();
    formData.append('audioFile', file);

    const response = await apiClient.post<TranscriptionResultResponse>('/audio/transcribe', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });

    return response.data;
  }

  /**
   * Perform a health check on the transcription service.
   * 
   * This method checks if the backend transcription service is available
   * and responding. It can be used to verify service connectivity before
   * attempting file uploads.
   * 
   * @returns Promise<{status: string}> Service health status
   * @throws Error if service is unavailable
   */
  static async healthCheck(): Promise<{ status: string }> {
    const response = await apiClient.get('/audio/health');
    return response.data;
  }
}

export default TranscriptionService;