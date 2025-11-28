package com.shangmin.whisperrr.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for transcription result response.
 * 
 * <p>This DTO represents the complete transcription result returned to clients
 * after successful audio processing. It contains the transcribed text along with
 * metadata about the transcription process, quality metrics, and timing information.</p>
 * 
 * <h3>Response Content:</h3>
 * <ul>
 *   <li><strong>Transcription Text:</strong> Complete transcribed text from audio</li>
 *   <li><strong>Language Detection:</strong> Detected language code (e.g., "en", "es")</li>
 *   <li><strong>Quality Metrics:</strong> Confidence scores and reliability indicators</li>
 *   <li><strong>Processing Metadata:</strong> Model used, processing time, duration</li>
 *   <li><strong>Status Information:</strong> Completion status and timestamps</li>
 * </ul>
 * 
 * <h3>Quality Assessment:</h3>
 * <p>The confidence score helps assess transcription quality:</p>
 * <ul>
 *   <li><strong>High Confidence (>0.8):</strong> Very reliable transcription</li>
 *   <li><strong>Medium Confidence (0.5-0.8):</strong> Good quality, minor review may be needed</li>
 *   <li><strong>Low Confidence (<0.5):</strong> May contain errors, review recommended</li>
 * </ul>
 * 
 * <h3>Usage:</h3>
 * <p>This response is returned directly from the transcription endpoint in the
 * simplified architecture, providing immediate results without polling.</p>
 * 
 * @author shangmin
 * @version 2.0
 * @since 2024
 * 
 * @see com.shangmin.whisperrr.controller.AudioController
 * @see com.shangmin.whisperrr.service.AudioService
 */
public class TranscriptionResultResponse {
    
    /** The transcribed text content from the audio file */
    private String transcriptionText;
    
    /** Detected language code (ISO 639-1 format, e.g., "en", "es", "fr") */
    private String language;
    
    /** Confidence score from 0.0 to 1.0 indicating transcription quality */
    private Double confidence;
    
    /** Duration of the audio file in seconds */
    private Double duration;
    
    /** Whisper model used for transcription (e.g., "base", "large") */
    private String modelUsed;
    
    /** Processing time in seconds taken to transcribe the audio */
    private Double processingTime;
    
    /** Timestamp when transcription was completed */
    private LocalDateTime completedAt;
    
    /** Status of the transcription (should always be COMPLETED for this response) */
    private TranscriptionStatus status;
    
    /**
     * Default constructor for JSON deserialization.
     */
    public TranscriptionResultResponse() {}
    
    /**
     * Constructor with essential fields.
     * 
     * @param transcriptionText the transcribed text
     * @param language detected language code
     * @param confidence quality confidence score
     * @param completedAt completion timestamp
     * @param status transcription status
     */
    public TranscriptionResultResponse(String transcriptionText, String language, Double confidence, 
                                     LocalDateTime completedAt, TranscriptionStatus status) {
        this.transcriptionText = transcriptionText;
        this.language = language;
        this.confidence = confidence;
        this.completedAt = completedAt;
        this.status = status;
    }
    
    /**
     * Gets the transcribed text content.
     * 
     * @return String the complete transcribed text from the audio file
     */
    public String getTranscriptionText() {
        return transcriptionText;
    }
    
    /**
     * Sets the transcribed text content.
     * 
     * @param transcriptionText the transcribed text to set
     */
    public void setTranscriptionText(String transcriptionText) {
        this.transcriptionText = transcriptionText;
    }
    
    /**
     * Gets the detected language code.
     * 
     * @return String language code in ISO 639-1 format (e.g., "en", "es", "fr")
     */
    public String getLanguage() {
        return language;
    }
    
    /**
     * Sets the detected language code.
     * 
     * @param language the language code to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }
    
    /**
     * Gets the confidence score for transcription quality.
     * 
     * @return Double confidence score from 0.0 to 1.0, or null if not available
     */
    public Double getConfidence() {
        return confidence;
    }
    
    /**
     * Sets the confidence score for transcription quality.
     * 
     * @param confidence the confidence score to set (0.0 to 1.0)
     */
    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }
    
    /**
     * Gets the duration of the audio file.
     * 
     * @return Double duration in seconds, or null if not available
     */
    public Double getDuration() {
        return duration;
    }
    
    /**
     * Sets the duration of the audio file.
     * 
     * @param duration the duration in seconds to set
     */
    public void setDuration(Double duration) {
        this.duration = duration;
    }
    
    /**
     * Gets the Whisper model used for transcription.
     * 
     * @return String model name (e.g., "base", "large"), or null if not available
     */
    public String getModelUsed() {
        return modelUsed;
    }
    
    /**
     * Sets the Whisper model used for transcription.
     * 
     * @param modelUsed the model name to set
     */
    public void setModelUsed(String modelUsed) {
        this.modelUsed = modelUsed;
    }
    
    /**
     * Gets the processing time taken for transcription.
     * 
     * @return Double processing time in seconds, or null if not available
     */
    public Double getProcessingTime() {
        return processingTime;
    }
    
    /**
     * Sets the processing time taken for transcription.
     * 
     * @param processingTime the processing time in seconds to set
     */
    public void setProcessingTime(Double processingTime) {
        this.processingTime = processingTime;
    }
    
    /**
     * Gets the completion timestamp.
     * 
     * @return LocalDateTime when the transcription was completed
     */
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
    
    /**
     * Sets the completion timestamp.
     * 
     * @param completedAt the completion timestamp to set
     */
    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
    
    /**
     * Gets the transcription status.
     * 
     * @return TranscriptionStatus the current status
     */
    public TranscriptionStatus getStatus() {
        return status;
    }
    
    /**
     * Sets the transcription status.
     * 
     * @param status the transcription status to set
     */
    public void setStatus(TranscriptionStatus status) {
        this.status = status;
    }
}
