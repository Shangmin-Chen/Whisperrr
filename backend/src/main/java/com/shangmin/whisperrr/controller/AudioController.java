package com.shangmin.whisperrr.controller;

import com.shangmin.whisperrr.dto.AudioUploadResponse;
import com.shangmin.whisperrr.dto.TranscriptionResultResponse;
import com.shangmin.whisperrr.dto.TranscriptionStatusResponse;
import com.shangmin.whisperrr.service.AudioService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST controller for audio transcription operations.
 * 
 * <p>This controller serves as the main entry point for all audio transcription-related
 * HTTP requests. It provides a RESTful API that allows clients (primarily the React frontend)
 * to upload audio files, track transcription job status, and retrieve transcription results.</p>
 * 
 * <h3>API Endpoints:</h3>
 * <ul>
 *   <li><strong>POST /api/audio/upload</strong> - Upload an audio file for transcription</li>
 *   <li><strong>GET /api/audio/status/{jobId}</strong> - Get the status of a transcription job</li>
 *   <li><strong>GET /api/audio/result/{jobId}</strong> - Get the transcription result for a completed job</li>
 *   <li><strong>GET /api/audio/health</strong> - Health check endpoint</li>
 * </ul>
 * 
 * <h3>Request/Response Flow:</h3>
 * <ol>
 *   <li>Client uploads an audio file via POST /upload</li>
 *   <li>Controller validates the file and delegates to AudioService</li>
 *   <li>Service creates a job and returns a job ID</li>
 *   <li>Client polls GET /status/{jobId} to track progress</li>
 *   <li>When complete, client retrieves result via GET /result/{jobId}</li>
 * </ol>
 * 
 * <h3>Error Handling:</h3>
 * <p>All exceptions are handled by the GlobalExceptionHandler, which provides
 * consistent error responses with appropriate HTTP status codes:</p>
 * <ul>
 *   <li>400 Bad Request - Invalid file format or validation errors</li>
 *   <li>404 Not Found - Job ID not found</li>
 *   <li>413 Payload Too Large - File size exceeds limits</li>
 *   <li>500 Internal Server Error - Processing failures</li>
 * </ul>
 * 
 * <h3>CORS Configuration:</h3>
 * <p>The controller is configured to accept cross-origin requests from the frontend
 * application through the CorsConfig class, enabling seamless communication between
 * the React frontend and this Spring Boot API.</p>
 * 
 * <h3>Logging:</h3>
 * <p>All operations are logged with appropriate levels for monitoring and debugging:
 * - INFO: Successful operations and job tracking
 * - ERROR: Failures and exceptions with full stack traces</p>
 * 
 * @author shangmin
 * @version 1.0
 * @since 2024
 * 
 * @see com.shangmin.whisperrr.service.AudioService
 * @see com.shangmin.whisperrr.dto.AudioUploadResponse
 * @see com.shangmin.whisperrr.dto.TranscriptionStatusResponse
 * @see com.shangmin.whisperrr.dto.TranscriptionResultResponse
 * @see com.shangmin.whisperrr.exception.GlobalExceptionHandler
 */
@RestController
@RequestMapping("/api/audio")
public class AudioController {
    
    private static final Logger logger = LoggerFactory.getLogger(AudioController.class);
    
    private final AudioService audioService;
    
    @Autowired
    public AudioController(AudioService audioService) {
        this.audioService = audioService;
    }
    
    /**
     * Upload an audio file for transcription.
     * 
     * <p>This endpoint accepts multipart file uploads and initiates the transcription process.
     * The uploaded file is validated for format, size, and content type before processing.</p>
     * 
     * <h4>Supported File Formats:</h4>
     * <ul>
     *   <li>MP3 (.mp3)</li>
     *   <li>WAV (.wav)</li>
     *   <li>M4A (.m4a)</li>
     *   <li>FLAC (.flac)</li>
     *   <li>OGG (.ogg)</li>
     * </ul>
     * 
     * <h4>File Validation Rules:</h4>
     * <ul>
     *   <li>Maximum file size: 25MB</li>
     *   <li>Must have valid audio content type</li>
     *   <li>Must have supported file extension</li>
     *   <li>File must not be empty or corrupted</li>
     * </ul>
     * 
     * <h4>Processing Flow:</h4>
     * <ol>
     *   <li>Validate uploaded file against size and format constraints</li>
     *   <li>Generate unique job ID for tracking</li>
     *   <li>Store file metadata in database</li>
     *   <li>Queue transcription job for processing</li>
     *   <li>Return job ID and initial status to client</li>
     * </ol>
     * 
     * <h4>Response:</h4>
     * <p>Returns HTTP 202 Accepted with job details for successful uploads.
     * The client should use the returned job ID to poll for status updates.</p>
     * 
     * @param audioFile the multipart audio file to upload and transcribe
     *                  Must be a valid audio file within size limits
     * @return AudioUploadResponse containing:
     *         - jobId: unique identifier for tracking the transcription job
     *         - timestamp: when the upload was processed
     *         - message: confirmation message about the upload status
     * 
     * @throws FileValidationException if the file fails validation (format, size, etc.)
     * @throws DatabaseException if there's an error storing the job in the database
     * @throws TranscriptionProcessingException if the transcription service is unavailable
     * 
     * @see com.shangmin.whisperrr.service.AudioService#uploadAudio(MultipartFile)
     * @see com.shangmin.whisperrr.dto.AudioUploadResponse
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AudioUploadResponse> uploadAudio(
            @RequestParam("audioFile") @Valid MultipartFile audioFile) {
        
        logger.info("Received audio upload request for file: {}", audioFile.getOriginalFilename());
        
        try {
            AudioUploadResponse response = audioService.uploadAudio(audioFile);
            logger.info("Audio upload successful with job ID: {}", response.getJobId());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
            
        } catch (Exception e) {
            logger.error("Error processing audio upload: {}", e.getMessage(), e);
            throw e; // Let GlobalExceptionHandler handle it
        }
    }
    
    /**
     * Get the current status of a transcription job.
     * 
     * <p>This endpoint allows clients to poll for the current status of a transcription job
     * using the job ID returned from the upload endpoint. The status indicates the current
     * stage of processing and provides timing information.</p>
     * 
     * <h4>Job Status Values:</h4>
     * <ul>
     *   <li><strong>PENDING:</strong> Job is queued and waiting to be processed</li>
     *   <li><strong>PROCESSING:</strong> Transcription is currently in progress</li>
     *   <li><strong>COMPLETED:</strong> Transcription finished successfully</li>
     *   <li><strong>FAILED:</strong> Transcription failed due to an error</li>
     *   <li><strong>CANCELLED:</strong> Job was cancelled by user or system</li>
     * </ul>
     * 
     * <h4>Polling Strategy:</h4>
     * <p>Clients should poll this endpoint at regular intervals (recommended: 2-5 seconds)
     * until the job reaches a terminal state (COMPLETED, FAILED, or CANCELLED).
     * Avoid polling too frequently to prevent unnecessary server load.</p>
     * 
     * <h4>Response Information:</h4>
     * <ul>
     *   <li>Current job status and descriptive message</li>
     *   <li>Last updated timestamp</li>
     *   <li>Processing progress indicators</li>
     *   <li>Error details if the job failed</li>
     * </ul>
     * 
     * @param jobId the unique job identifier returned from the upload endpoint
     *              Must be a valid UUID string format
     * @return TranscriptionStatusResponse containing:
     *         - jobId: the job identifier
     *         - status: current job status enum value
     *         - updatedAt: timestamp of last status update
     *         - message: human-readable status description
     *         - errorMessage: error details if status is FAILED (optional)
     * 
     * @throws TranscriptionNotFoundException if the job ID doesn't exist
     * @throws IllegalArgumentException if the job ID format is invalid
     * 
     * @see com.shangmin.whisperrr.service.AudioService#getTranscriptionStatus(String)
     * @see com.shangmin.whisperrr.dto.TranscriptionStatusResponse
     * @see com.shangmin.whisperrr.enums.JobStatus
     */
    @GetMapping("/status/{jobId}")
    public ResponseEntity<TranscriptionStatusResponse> getTranscriptionStatus(@PathVariable String jobId) {
        
        logger.info("Getting transcription status for job: {}", jobId);
        
        try {
            TranscriptionStatusResponse response = audioService.getTranscriptionStatus(jobId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error getting transcription status: {}", e.getMessage(), e);
            throw e; // Let GlobalExceptionHandler handle it
        }
    }
    
    /**
     * Get the transcription result for a completed job.
     * 
     * <p>This endpoint returns the final transcription result once a job has completed
     * successfully. It provides the transcribed text along with metadata about the
     * transcription process and quality metrics.</p>
     * 
     * <h4>Prerequisites:</h4>
     * <ul>
     *   <li>Job must exist and be in COMPLETED status</li>
     *   <li>Transcription must have been processed successfully</li>
     *   <li>Result data must be available in the database</li>
     * </ul>
     * 
     * <h4>Result Content:</h4>
     * <ul>
     *   <li><strong>Transcribed Text:</strong> Full text content from the audio</li>
     *   <li><strong>Language Detection:</strong> Detected language of the audio</li>
     *   <li><strong>Confidence Score:</strong> Quality metric (0.0 to 1.0)</li>
     *   <li><strong>Timing Information:</strong> Processing duration and completion time</li>
     *   <li><strong>Segments:</strong> Detailed timing and text segments (if available)</li>
     *   <li><strong>Model Information:</strong> Whisper model used for transcription</li>
     * </ul>
     * 
     * <h4>Quality Indicators:</h4>
     * <ul>
     *   <li><strong>High Confidence (>0.8):</strong> Very reliable transcription</li>
     *   <li><strong>Medium Confidence (0.5-0.8):</strong> Good quality, may need review</li>
     *   <li><strong>Low Confidence (<0.5):</strong> May contain errors, manual review recommended</li>
     * </ul>
     * 
     * <h4>Usage Notes:</h4>
     * <p>This endpoint should only be called after confirming the job status is COMPLETED
     * via the status endpoint. Calling this endpoint on incomplete jobs will result in
     * a 404 Not Found response.</p>
     * 
     * @param jobId the unique job identifier for the completed transcription
     *              Must correspond to a job in COMPLETED status
     * @return TranscriptionResultResponse containing:
     *         - jobId: the job identifier
     *         - text: complete transcribed text content
     *         - language: detected language code (e.g., "en", "es", "fr")
     *         - confidence: overall confidence score (0.0 to 1.0)
     *         - duration: audio duration in seconds
     *         - segments: detailed segment information with timestamps
     *         - completedAt: timestamp when transcription finished
     *         - processingTime: time taken to process the audio
     *         - modelUsed: Whisper model version used
     *         - wordCount: number of words in transcription
     *         - characterCount: number of characters in transcription
     * 
     * @throws TranscriptionNotFoundException if the job ID doesn't exist
     * @throws IllegalStateException if the job is not in COMPLETED status
     * @throws DatabaseException if there's an error retrieving the result
     * 
     * @see com.shangmin.whisperrr.service.AudioService#getTranscriptionResult(String)
     * @see com.shangmin.whisperrr.dto.TranscriptionResultResponse
     * @see com.shangmin.whisperrr.entity.Transcription
     */
    @GetMapping("/result/{jobId}")
    public ResponseEntity<TranscriptionResultResponse> getTranscriptionResult(@PathVariable String jobId) {
        
        logger.info("Getting transcription result for job: {}", jobId);
        
        try {
            TranscriptionResultResponse response = audioService.getTranscriptionResult(jobId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error getting transcription result: {}", e.getMessage(), e);
            throw e; // Let GlobalExceptionHandler handle it
        }
    }
    
    /**
     * Health check endpoint for monitoring service availability.
     * 
     * <p>This endpoint provides a simple health check to verify that the audio transcription
     * service is running and responsive. It can be used by load balancers, monitoring systems,
     * and clients to verify service availability.</p>
     * 
     * <h4>Health Check Information:</h4>
     * <ul>
     *   <li>Service availability and responsiveness</li>
     *   <li>Basic connectivity to the API layer</li>
     *   <li>Controller layer functionality</li>
     * </ul>
     * 
     * <h4>Usage:</h4>
     * <ul>
     *   <li><strong>Load Balancers:</strong> Use for health checks and traffic routing</li>
     *   <li><strong>Monitoring:</strong> Include in uptime monitoring and alerting</li>
     *   <li><strong>Client Apps:</strong> Verify service availability before operations</li>
     *   <li><strong>Development:</strong> Quick test that the service is running</li>
     * </ul>
     * 
     * <h4>Response:</h4>
     * <p>Returns HTTP 200 OK with a simple text message when the service is healthy.
     * Any other response code indicates a service issue.</p>
     * 
     * <p><strong>Note:</strong> This is a basic health check. For more comprehensive
     * health information including database connectivity, external service status,
     * and detailed metrics, use the Spring Boot Actuator endpoints:</p>
     * <ul>
     *   <li>/actuator/health - Detailed health information</li>
     *   <li>/actuator/info - Application information</li>
     *   <li>/actuator/metrics - Performance metrics</li>
     * </ul>
     * 
     * @return ResponseEntity<String> with HTTP 200 OK and confirmation message
     *         "Audio transcription service is running" when healthy
     * 
     * @see org.springframework.boot.actuator.health.HealthEndpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Audio transcription service is running");
    }
}
