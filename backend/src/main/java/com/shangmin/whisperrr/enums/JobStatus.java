package com.shangmin.whisperrr.enums;

/**
 * Enumeration representing the lifecycle status of a transcription job.
 * 
 * <p>This enum defines all possible states that a transcription job can be in
 * throughout its lifecycle, from initial creation to final completion or failure.
 * It provides a clear state machine for job processing and enables proper
 * status tracking and client communication.</p>
 * 
 * <h3>Job Lifecycle Flow:</h3>
 * <p>A typical job progresses through these states:</p>
 * <ol>
 *   <li><strong>PENDING:</strong> Job created and queued for processing</li>
 *   <li><strong>PROCESSING:</strong> Transcription service is actively working on the job</li>
 *   <li><strong>COMPLETED:</strong> Transcription finished successfully with results available</li>
 * </ol>
 * 
 * <h3>Alternative Outcomes:</h3>
 * <ul>
 *   <li><strong>FAILED:</strong> Processing encountered an error and could not complete</li>
 *   <li><strong>CANCELLED:</strong> Job was cancelled by user or system before completion</li>
 * </ul>
 * 
 * <h3>State Categories:</h3>
 * <ul>
 *   <li><strong>Active States:</strong> PENDING, PROCESSING - job is still being worked on</li>
 *   <li><strong>Terminal States:</strong> COMPLETED, FAILED, CANCELLED - job is finished</li>
 * </ul>
 * 
 * <h3>Client Polling Strategy:</h3>
 * <p>Clients should poll job status until reaching a terminal state:</p>
 * <ul>
 *   <li>Continue polling for PENDING and PROCESSING states</li>
 *   <li>Stop polling and retrieve results for COMPLETED state</li>
 *   <li>Stop polling and handle errors for FAILED and CANCELLED states</li>
 * </ul>
 * 
 * <h3>Database Storage:</h3>
 * <p>This enum is stored as a string in the database using JPA's @Enumerated(EnumType.STRING)
 * annotation, ensuring human-readable values and database schema stability.</p>
 * 
 * <h3>Error Handling:</h3>
 * <p>Each status provides context for appropriate error handling:</p>
 * <ul>
 *   <li><strong>PENDING:</strong> Normal waiting state, no action needed</li>
 *   <li><strong>PROCESSING:</strong> Normal active state, continue monitoring</li>
 *   <li><strong>COMPLETED:</strong> Success state, retrieve results</li>
 *   <li><strong>FAILED:</strong> Error state, check error message and possibly retry</li>
 *   <li><strong>CANCELLED:</strong> Termination state, inform user of cancellation</li>
 * </ul>
 * 
 * @author shangmin
 * @version 1.0
 * @since 2024
 * 
 * @see com.shangmin.whisperrr.entity.Job
 * @see com.shangmin.whisperrr.dto.TranscriptionStatusResponse
 */
public enum JobStatus {
    /**
     * Job has been created and is waiting in the queue for processing.
     * 
     * <p>This is the initial state when a job is first created. The job is
     * queued and waiting for an available worker to begin transcription.</p>
     */
    PENDING("Pending processing"),
    
    /**
     * Job is currently being processed by the transcription service.
     * 
     * <p>The audio file has been sent to the Python transcription service
     * and Whisper is actively transcribing the audio content.</p>
     */
    PROCESSING("Currently processing"),
    
    /**
     * Job has completed successfully with results available.
     * 
     * <p>Transcription has finished successfully and the results are stored
     * in the database. Clients can now retrieve the transcription text.</p>
     */
    COMPLETED("Successfully completed"),
    
    /**
     * Job processing failed due to an error.
     * 
     * <p>An error occurred during transcription processing. The error message
     * should provide details about what went wrong. The job may be retryable
     * depending on the error type.</p>
     */
    FAILED("Processing failed"),
    
    /**
     * Job was cancelled before completion.
     * 
     * <p>The job was cancelled either by user request or system intervention
     * before transcription could complete. No results are available.</p>
     */
    CANCELLED("Job cancelled");
    
    private final String description;
    
    /**
     * Constructor for JobStatus enum values.
     * 
     * <p>Each enum value is initialized with a human-readable description
     * that can be displayed to users or used in logging and error messages.</p>
     * 
     * @param description human-readable description of the job status
     *                   Used for user interfaces and logging
     */
    JobStatus(String description) {
        this.description = description;
    }
    
    /**
     * Gets the human-readable description of the job status.
     * 
     * <p>This description is suitable for displaying to end users in
     * user interfaces, status messages, and notifications. It provides
     * clear, understandable information about the current job state.</p>
     * 
     * @return String user-friendly description of the current status
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Checks if the job is in a terminal state.
     * 
     * <p>Terminal states are final states where the job has finished processing
     * and will not change status again. These states indicate that clients
     * should stop polling for status updates.</p>
     * 
     * <h4>Terminal States:</h4>
     * <ul>
     *   <li><strong>COMPLETED:</strong> Job finished successfully</li>
     *   <li><strong>FAILED:</strong> Job failed with an error</li>
     *   <li><strong>CANCELLED:</strong> Job was cancelled</li>
     * </ul>
     * 
     * <h4>Usage:</h4>
     * <p>Use this method to determine when to stop polling for status updates
     * and when to take final actions (retrieve results, handle errors, etc.).</p>
     * 
     * @return true if the job is in a terminal state (COMPLETED, FAILED, or CANCELLED),
     *         false if the job is still active (PENDING or PROCESSING)
     */
    public boolean isTerminal() {
        return this == COMPLETED || this == FAILED || this == CANCELLED;
    }
    
    /**
     * Checks if the job is in an active processing state.
     * 
     * <p>Processing states indicate that the job is still being worked on
     * and clients should continue polling for status updates. These are
     * non-terminal states where the job status may change.</p>
     * 
     * <h4>Processing States:</h4>
     * <ul>
     *   <li><strong>PENDING:</strong> Job is queued and waiting to start</li>
     *   <li><strong>PROCESSING:</strong> Job is actively being transcribed</li>
     * </ul>
     * 
     * <h4>Usage:</h4>
     * <p>Use this method to determine when to continue polling for status
     * updates and when to show progress indicators to users.</p>
     * 
     * @return true if the job is still being processed (PENDING or PROCESSING),
     *         false if the job has reached a terminal state
     */
    public boolean isProcessing() {
        return this == PENDING || this == PROCESSING;
    }
}
