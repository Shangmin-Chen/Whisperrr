package com.shangmin.whisperrr.service;

import com.shangmin.whisperrr.dto.AudioUploadResponse;
import com.shangmin.whisperrr.dto.TranscriptionResultResponse;
import com.shangmin.whisperrr.dto.TranscriptionStatusResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for audio transcription operations.
 * 
 * <p>This interface defines the core business logic for the audio transcription system.
 * It serves as the main service layer that orchestrates the entire transcription workflow,
 * from file upload and validation to job management and result retrieval.</p>
 * 
 * <h3>Service Responsibilities:</h3>
 * <ul>
 *   <li><strong>File Management:</strong> Upload, validation, and storage of audio files</li>
 *   <li><strong>Job Orchestration:</strong> Creation and management of transcription jobs</li>
 *   <li><strong>Status Tracking:</strong> Real-time monitoring of job progress and states</li>
 *   <li><strong>Result Management:</strong> Storage and retrieval of transcription results</li>
 *   <li><strong>Integration:</strong> Communication with Python transcription service</li>
 *   <li><strong>Error Handling:</strong> Comprehensive error management and recovery</li>
 * </ul>
 * 
 * <h3>Workflow Overview:</h3>
 * <ol>
 *   <li><strong>Upload Phase:</strong>
 *       <ul>
 *         <li>Validate audio file format, size, and content</li>
 *         <li>Generate unique job identifier</li>
 *         <li>Store file metadata in database</li>
 *         <li>Queue job for processing</li>
 *       </ul>
 *   </li>
 *   <li><strong>Processing Phase:</strong>
 *       <ul>
 *         <li>Send audio file to Python transcription service</li>
 *         <li>Monitor processing status and progress</li>
 *         <li>Handle processing errors and retries</li>
 *         <li>Update job status in real-time</li>
 *       </ul>
 *   </li>
 *   <li><strong>Completion Phase:</strong>
 *       <ul>
 *         <li>Receive transcription results from Python service</li>
 *         <li>Store results in database with metadata</li>
 *         <li>Update job status to completed</li>
 *         <li>Make results available for retrieval</li>
 *       </ul>
 *   </li>
 * </ol>
 * 
 * <h3>Integration Architecture:</h3>
 * <p>This service acts as the central coordinator between:</p>
 * <ul>
 *   <li><strong>Frontend (React):</strong> Receives requests via REST controller</li>
 *   <li><strong>Database (PostgreSQL):</strong> Persists jobs, files, and results</li>
 *   <li><strong>Python Service (FastAPI):</strong> Performs actual transcription processing</li>
 *   <li><strong>File Storage:</strong> Manages temporary and permanent file storage</li>
 * </ul>
 * 
 * <h3>Error Handling Strategy:</h3>
 * <ul>
 *   <li><strong>Validation Errors:</strong> Immediate feedback with specific error messages</li>
 *   <li><strong>Processing Errors:</strong> Automatic retry logic with exponential backoff</li>
 *   <li><strong>Service Errors:</strong> Graceful degradation and error reporting</li>
 *   <li><strong>Timeout Handling:</strong> Configurable timeouts with proper cleanup</li>
 * </ul>
 * 
 * <h3>Performance Considerations:</h3>
 * <ul>
 *   <li><strong>Asynchronous Processing:</strong> Non-blocking job execution</li>
 *   <li><strong>Resource Management:</strong> Efficient memory and storage usage</li>
 *   <li><strong>Concurrent Processing:</strong> Support for multiple simultaneous jobs</li>
 *   <li><strong>Caching:</strong> Intelligent caching of results and metadata</li>
 * </ul>
 * 
 * @author shangmin
 * @version 1.0
 * @since 2024
 * 
 * @see com.shangmin.whisperrr.controller.AudioController
 * @see com.shangmin.whisperrr.entity.Job
 * @see com.shangmin.whisperrr.entity.AudioFile
 * @see com.shangmin.whisperrr.entity.Transcription
 */
public interface AudioService {
    
    /**
     * Upload and process an audio file for transcription.
     * 
     * <p>This method handles the complete upload workflow including file validation,
     * metadata extraction, database storage, and job queue initialization. It serves
     * as the primary entry point for starting new transcription jobs.</p>
     * 
     * <h4>Processing Steps:</h4>
     * <ol>
     *   <li>Validate file format, size, and content type</li>
     *   <li>Extract audio metadata (duration, format, etc.)</li>
     *   <li>Generate unique job identifier</li>
     *   <li>Store file metadata in database</li>
     *   <li>Create transcription job record</li>
     *   <li>Queue job for asynchronous processing</li>
     *   <li>Return job details to client</li>
     * </ol>
     * 
     * <h4>File Validation Rules:</h4>
     * <ul>
     *   <li>Supported formats: MP3, WAV, M4A, FLAC, OGG</li>
     *   <li>Maximum file size: 25MB (configurable)</li>
     *   <li>Valid audio content type required</li>
     *   <li>Non-empty file with readable content</li>
     * </ul>
     * 
     * @param audioFile the multipart audio file uploaded by the client
     *                  Must be a valid audio file within size and format constraints
     * @return AudioUploadResponse containing the generated job ID, upload timestamp,
     *         and confirmation message for successful uploads
     * 
     * @throws FileValidationException if the file fails any validation checks
     * @throws DatabaseException if there's an error storing the job or file metadata
     * @throws TranscriptionProcessingException if the job cannot be queued for processing
     * @throws IllegalArgumentException if the audioFile parameter is null or invalid
     * 
     * @see #validateAudioFile(MultipartFile)
     */
    AudioUploadResponse uploadAudio(MultipartFile audioFile);
    
    /**
     * Get the current status of a transcription job.
     * 
     * <p>This method provides real-time status information for transcription jobs,
     * allowing clients to track progress from initial upload through completion.
     * It's designed for polling-based status updates.</p>
     * 
     * <h4>Status Information Provided:</h4>
     * <ul>
     *   <li>Current job status (PENDING, PROCESSING, COMPLETED, FAILED, CANCELLED)</li>
     *   <li>Last update timestamp</li>
     *   <li>Progress indicators and estimated completion time</li>
     *   <li>Error messages and details if applicable</li>
     *   <li>Processing metadata (model used, duration, etc.)</li>
     * </ul>
     * 
     * <h4>Polling Guidelines:</h4>
     * <ul>
     *   <li>Recommended polling interval: 2-5 seconds</li>
     *   <li>Stop polling when job reaches terminal state</li>
     *   <li>Implement exponential backoff for failed requests</li>
     *   <li>Handle network timeouts gracefully</li>
     * </ul>
     * 
     * @param jobId the unique job identifier returned from uploadAudio()
     *              Must be a valid UUID string format
     * @return TranscriptionStatusResponse containing current status information,
     *         timestamps, progress indicators, and any error details
     * 
     * @throws TranscriptionNotFoundException if the job ID doesn't exist in the system
     * @throws IllegalArgumentException if the job ID format is invalid
     * @throws DatabaseException if there's an error retrieving job information
     * 
     * @see com.shangmin.whisperrr.enums.JobStatus
     */
    TranscriptionStatusResponse getTranscriptionStatus(String jobId);
    
    /**
     * Get the transcription result for a completed job.
     * 
     * <p>This method retrieves the final transcription output including the transcribed
     * text, quality metrics, timing information, and detailed segment data. It should
     * only be called after confirming the job status is COMPLETED.</p>
     * 
     * <h4>Result Components:</h4>
     * <ul>
     *   <li><strong>Transcribed Text:</strong> Complete text content from audio</li>
     *   <li><strong>Language Detection:</strong> Detected language with confidence</li>
     *   <li><strong>Quality Metrics:</strong> Confidence scores and reliability indicators</li>
     *   <li><strong>Timing Data:</strong> Processing duration and completion timestamps</li>
     *   <li><strong>Segment Information:</strong> Word-level timing and confidence data</li>
     *   <li><strong>Model Metadata:</strong> Whisper model version and parameters used</li>
     * </ul>
     * 
     * <h4>Quality Assessment:</h4>
     * <ul>
     *   <li><strong>High Confidence (>0.8):</strong> Highly reliable transcription</li>
     *   <li><strong>Medium Confidence (0.5-0.8):</strong> Good quality, minor review needed</li>
     *   <li><strong>Low Confidence (<0.5):</strong> May contain errors, review recommended</li>
     * </ul>
     * 
     * <h4>Data Format:</h4>
     * <p>Results include both human-readable text and machine-readable segment data
     * with precise timing information for advanced use cases like subtitle generation
     * or audio synchronization.</p>
     * 
     * @param jobId the unique job identifier for a completed transcription job
     *              Must correspond to a job in COMPLETED status
     * @return TranscriptionResultResponse containing the complete transcription result
     *         with text, metadata, quality metrics, and timing information
     * 
     * @throws TranscriptionNotFoundException if the job ID doesn't exist
     * @throws IllegalStateException if the job is not in COMPLETED status
     * @throws DatabaseException if there's an error retrieving the transcription result
     * @throws IllegalArgumentException if the job ID format is invalid
     * 
     * @see com.shangmin.whisperrr.entity.Transcription
     */
    TranscriptionResultResponse getTranscriptionResult(String jobId);
    
    /**
     * Validate an audio file against system requirements and constraints.
     * 
     * <p>This method performs comprehensive validation of uploaded audio files to ensure
     * they meet all system requirements before processing. It checks file format,
     * size constraints, content type, and basic file integrity.</p>
     * 
     * <h4>Validation Checks Performed:</h4>
     * <ul>
     *   <li><strong>File Existence:</strong> Ensures file is not null or empty</li>
     *   <li><strong>Size Validation:</strong> Checks against maximum file size limit (25MB)</li>
     *   <li><strong>Format Validation:</strong> Verifies supported audio format extensions</li>
     *   <li><strong>Content Type:</strong> Validates MIME type is audio-related</li>
     *   <li><strong>File Integrity:</strong> Basic checks for file corruption or damage</li>
     *   <li><strong>Filename Validation:</strong> Ensures valid filename and extension</li>
     * </ul>
     * 
     * <h4>Supported Audio Formats:</h4>
     * <ul>
     *   <li><strong>MP3:</strong> MPEG Audio Layer III (.mp3)</li>
     *   <li><strong>WAV:</strong> Waveform Audio File Format (.wav)</li>
     *   <li><strong>M4A:</strong> MPEG-4 Audio (.m4a)</li>
     *   <li><strong>FLAC:</strong> Free Lossless Audio Codec (.flac)</li>
     *   <li><strong>OGG:</strong> Ogg Vorbis (.ogg)</li>
     * </ul>
     * 
     * <h4>Size and Quality Constraints:</h4>
     * <ul>
     *   <li>Maximum file size: 25MB (configurable via application properties)</li>
     *   <li>Minimum file size: 1KB (to prevent empty uploads)</li>
     *   <li>Recommended duration: Up to 2 hours for optimal processing</li>
     * </ul>
     * 
     * <h4>Error Handling:</h4>
     * <p>Validation failures result in specific FileValidationException instances
     * with detailed error messages indicating the exact validation failure reason.
     * This allows clients to provide meaningful feedback to users.</p>
     * 
     * @param audioFile the multipart file to validate
     *                  Must be a non-null MultipartFile instance
     * 
     * @throws FileValidationException with specific error details if validation fails:
     *         - "Audio file is required" if file is null or empty
     *         - "File size exceeds maximum allowed size of 25MB" if too large
     *         - "Unsupported file type. Supported types: [mp3, wav, m4a, flac, ogg]" if format invalid
     *         - "File must be an audio file" if content type is not audio
     *         - "File must have a valid name" if filename is missing or invalid
     * @throws IllegalArgumentException if the audioFile parameter is null
     * 
     * @see com.shangmin.whisperrr.exception.FileValidationException
     */
    void validateAudioFile(MultipartFile audioFile);
}
