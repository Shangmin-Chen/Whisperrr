package com.shangmin.whisperrr.controller;

import com.shangmin.whisperrr.dto.TranscriptionResultResponse;
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
 * REST controller for direct audio transcription operations.
 * 
 * <p>This controller serves as a lightweight proxy for instant audio transcription.
 * It provides a simplified RESTful API that allows clients (primarily the React frontend)
 * to upload audio files and receive transcription results immediately without any
 * job queuing or polling mechanisms.</p>
 * 
 * <h3>API Endpoints:</h3>
 * <ul>
 *   <li><strong>POST /api/audio/transcribe</strong> - Upload and transcribe audio file instantly</li>
 *   <li><strong>GET /api/audio/health</strong> - Health check endpoint</li>
 * </ul>
 * 
 * <h3>Simplified Request/Response Flow:</h3>
 * <ol>
 *   <li>Client uploads an audio file via POST /transcribe</li>
 *   <li>Controller validates the file and delegates to AudioService</li>
 *   <li>Service sends file directly to Python transcription service</li>
 *   <li>Results are returned immediately to the client</li>
 * </ol>
 * 
 * <h3>Benefits of Direct Processing:</h3>
 * <ul>
 *   <li><strong>Instant Results:</strong> No polling or waiting required</li>
 *   <li><strong>Simplified Architecture:</strong> No job management or database overhead</li>
 *   <li><strong>Better Performance:</strong> Direct communication reduces latency</li>
 *   <li><strong>Easier Debugging:</strong> Simplified error handling and logging</li>
 * </ul>
 * 
 * <h3>Error Handling:</h3>
 * <p>All exceptions are handled by the GlobalExceptionHandler, which provides
 * consistent error responses with appropriate HTTP status codes:</p>
 * <ul>
 *   <li>400 Bad Request - Invalid file format or validation errors</li>
 *   <li>413 Payload Too Large - File size exceeds limits</li>
 *   <li>500 Internal Server Error - Processing failures</li>
 *   <li>503 Service Unavailable - Python service not accessible</li>
 * </ul>
 * 
 * <h3>CORS Configuration:</h3>
 * <p>The controller is configured to accept cross-origin requests from the frontend
 * application through the CorsConfig class, enabling seamless communication between
 * the React frontend and this Spring Boot API.</p>
 * 
 * <h3>Logging:</h3>
 * <p>All operations are logged with appropriate levels for monitoring and debugging:
 * - INFO: Successful operations and transcription completion
 * - ERROR: Failures and exceptions with full stack traces</p>
 * 
 * @author shangmin
 * @version 2.0
 * @since 2024
 * 
 * @see com.shangmin.whisperrr.service.AudioService
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
     * Upload and transcribe an audio file instantly.
     * 
     * <p>This endpoint accepts multipart file uploads and processes them immediately,
     * returning transcription results directly without any job queuing or polling.
     * The uploaded file is validated and sent directly to the Python transcription service.</p>
     * 
     * <h4>Supported File Formats:</h4>
     * <ul>
     *   <li>MP3 (.mp3) - Most common compressed format</li>
     *   <li>WAV (.wav) - Uncompressed, highest quality</li>
     *   <li>M4A (.m4a) - Apple's compressed format</li>
     *   <li>FLAC (.flac) - Lossless compression</li>
     *   <li>OGG (.ogg) - Open-source compressed format</li>
     *   <li>WMA (.wma) - Windows Media Audio</li>
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
     *   <li>Send file directly to Python transcription service</li>
     *   <li>Receive transcription results immediately</li>
     *   <li>Return complete transcription response to client</li>
     * </ol>
     * 
     * <h4>Response:</h4>
     * <p>Returns HTTP 200 OK with complete transcription results for successful processing.
     * No polling is required - results are available immediately.</p>
     * 
     * @param audioFile the multipart audio file to upload and transcribe
     *                  Must be a valid audio file within size limits
     * @return TranscriptionResultResponse containing:
     *         - transcriptionText: the complete transcribed text
     *         - language: detected language code
     *         - confidence: quality confidence score (0.0-1.0)
     *         - duration: audio duration in seconds
     *         - modelUsed: Whisper model used for transcription
     *         - processingTime: time taken to process
     *         - completedAt: completion timestamp
     *         - status: transcription status (COMPLETED)
     * 
     * @throws FileValidationException if the file fails validation (format, size, etc.)
     * @throws TranscriptionProcessingException if the transcription service fails
     * 
     * @see com.shangmin.whisperrr.service.AudioService#transcribeAudio(MultipartFile)
     * @see com.shangmin.whisperrr.dto.TranscriptionResultResponse
     */
    @PostMapping(value = "/transcribe", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TranscriptionResultResponse> transcribeAudio(
            @RequestParam("audioFile") @Valid MultipartFile audioFile) {
        
        logger.info("Received direct transcription request for file: {}", audioFile.getOriginalFilename());
        
        try {
            TranscriptionResultResponse response = audioService.transcribeAudio(audioFile);
            logger.info("Direct transcription completed successfully for file: {}", audioFile.getOriginalFilename());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error processing direct transcription: {}", e.getMessage(), e);
            throw e; // Let GlobalExceptionHandler handle it
        }
    }
    
    
    /**
     * Health check endpoint for monitoring service availability.
     * 
     * <p>This endpoint provides a simple health check to verify that the audio transcription
     * proxy service is running and responsive. It can be used by load balancers, monitoring
     * systems, and clients to verify service availability.</p>
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
     * <p><strong>Note:</strong> For more comprehensive health information including
     * Python service connectivity and detailed metrics, use the Spring Boot Actuator endpoints:</p>
     * <ul>
     *   <li>/actuator/health - Detailed health information</li>
     *   <li>/actuator/info - Application information</li>
     *   <li>/actuator/metrics - Performance metrics</li>
     * </ul>
     * 
     * @return ResponseEntity<String> with HTTP 200 OK and confirmation message
     *         "Direct audio transcription service is running" when healthy
     * 
     * @see org.springframework.boot.actuator.health.HealthEndpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Direct audio transcription service is running");
    }
}
