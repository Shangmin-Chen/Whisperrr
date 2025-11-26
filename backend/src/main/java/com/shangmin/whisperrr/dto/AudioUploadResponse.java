package com.shangmin.whisperrr.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for audio upload response.
 * 
 * <p>This DTO represents the response sent to clients after successfully uploading
 * an audio file for transcription. It contains essential information that clients
 * need to track the transcription job and poll for status updates.</p>
 * 
 * <h3>Response Structure:</h3>
 * <p>The response provides three key pieces of information:</p>
 * <ul>
 *   <li><strong>Job ID:</strong> Unique identifier for tracking the transcription job</li>
 *   <li><strong>Timestamp:</strong> When the upload was processed and job was created</li>
 *   <li><strong>Message:</strong> Human-readable confirmation message</li>
 * </ul>
 * 
 * <h3>Client Usage Pattern:</h3>
 * <ol>
 *   <li>Client uploads audio file via POST /api/audio/upload</li>
 *   <li>Server responds with this DTO containing job ID</li>
 *   <li>Client stores job ID for subsequent status polling</li>
 *   <li>Client uses job ID to call GET /api/audio/status/{jobId}</li>
 *   <li>Client retrieves results via GET /api/audio/result/{jobId}</li>
 * </ol>
 * 
 * <h3>Job ID Format:</h3>
 * <p>The job ID is a UUID string that uniquely identifies the transcription job
 * throughout its lifecycle. It's used for:</p>
 * <ul>
 *   <li>Status polling and progress tracking</li>
 *   <li>Result retrieval when transcription completes</li>
 *   <li>Error reporting and debugging</li>
 *   <li>Job cancellation (if implemented)</li>
 * </ul>
 * 
 * <h3>Timestamp Information:</h3>
 * <p>The timestamp indicates when the server processed the upload and created
 * the transcription job. This is useful for:</p>
 * <ul>
 *   <li>Client-side timeout calculations</li>
 *   <li>Audit logging and tracking</li>
 *   <li>Performance monitoring</li>
 *   <li>User interface display</li>
 * </ul>
 * 
 * <h3>JSON Serialization:</h3>
 * <p>This DTO is automatically serialized to JSON by Spring Boot's Jackson
 * configuration, producing responses like:</p>
 * <pre>
 * {
 *   "jobId": "550e8400-e29b-41d4-a716-446655440000",
 *   "timestamp": "2024-01-15T10:30:45.123",
 *   "message": "Audio file uploaded successfully and transcription job started"
 * }
 * </pre>
 * 
 * @author shangmin
 * @version 1.0
 * @since 2024
 * 
 * @see com.shangmin.whisperrr.controller.AudioController#uploadAudio(MultipartFile)
 * @see com.shangmin.whisperrr.dto.TranscriptionStatusResponse
 * @see com.shangmin.whisperrr.dto.TranscriptionResultResponse
 */
public class AudioUploadResponse {
    
    private String jobId;
    private LocalDateTime timestamp;
    private String message;
    
    /**
     * Default constructor for JSON deserialization.
     * 
     * <p>Required by Jackson for JSON deserialization and Spring Boot's
     * automatic request/response handling.</p>
     */
    public AudioUploadResponse() {}
    
    /**
     * Constructor with all required fields.
     * 
     * <p>Creates a complete audio upload response with all necessary information
     * for client tracking and user feedback.</p>
     * 
     * @param jobId unique identifier for the created transcription job
     *              Must be a valid UUID string for proper tracking
     * @param timestamp when the upload was processed and job was created
     *                  Should be the current server time in UTC
     * @param message human-readable confirmation message for user display
     *                Should provide clear feedback about the upload status
     */
    public AudioUploadResponse(String jobId, LocalDateTime timestamp, String message) {
        this.jobId = jobId;
        this.timestamp = timestamp;
        this.message = message;
    }
    
    /**
     * Gets the unique job identifier for the transcription job.
     * 
     * <p>This ID is used by clients to track the job status and retrieve
     * results when transcription completes. It remains constant throughout
     * the job lifecycle.</p>
     * 
     * @return String unique job identifier (UUID format)
     */
    public String getJobId() {
        return jobId;
    }
    
    /**
     * Sets the unique job identifier.
     * 
     * <p>Typically called by the service layer when creating the response
     * after generating a new job ID.</p>
     * 
     * @param jobId unique job identifier to set
     */
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
    
    /**
     * Gets the timestamp when the upload was processed.
     * 
     * <p>This timestamp indicates when the server successfully processed
     * the upload and created the transcription job. Useful for client-side
     * timeout calculations and audit logging.</p>
     * 
     * @return LocalDateTime when the job was created
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    /**
     * Sets the processing timestamp.
     * 
     * <p>Should be set to the current server time when the job is created.</p>
     * 
     * @param timestamp when the upload was processed
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    /**
     * Gets the human-readable confirmation message.
     * 
     * <p>This message provides user-friendly feedback about the upload
     * status and can be displayed directly in the user interface.</p>
     * 
     * @return String confirmation message for user display
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Sets the confirmation message.
     * 
     * <p>Should provide clear, actionable feedback to the user about
     * the upload status and next steps.</p>
     * 
     * @param message confirmation message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
