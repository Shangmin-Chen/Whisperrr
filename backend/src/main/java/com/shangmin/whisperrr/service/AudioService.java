package com.shangmin.whisperrr.service;

import com.shangmin.whisperrr.dto.AudioUploadResponse;
import com.shangmin.whisperrr.dto.TranscriptionResultResponse;
import com.shangmin.whisperrr.dto.TranscriptionStatusResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for direct audio transcription operations.
 * 
 * <p>This interface defines a simplified transcription service that processes audio files
 * directly without persistence. It provides instant transcription by communicating directly
 * with the Python transcription service and returning results immediately.</p>
 * 
 * <h3>Service Responsibilities:</h3>
 * <ul>
 *   <li><strong>File Validation:</strong> Validate audio file format, size, and content</li>
 *   <li><strong>Direct Processing:</strong> Send files directly to Python service for transcription</li>
 *   <li><strong>Result Handling:</strong> Return transcription results immediately</li>
 *   <li><strong>Error Management:</strong> Handle validation and processing errors gracefully</li>
 * </ul>
 * 
 * <h3>Simplified Workflow:</h3>
 * <ol>
 *   <li><strong>Validation:</strong> Validate uploaded audio file</li>
 *   <li><strong>Processing:</strong> Send file directly to Python transcription service</li>
 *   <li><strong>Response:</strong> Return transcription results immediately</li>
 * </ol>
 * 
 * <h3>Integration Architecture:</h3>
 * <p>This service acts as a lightweight proxy between:</p>
 * <ul>
 *   <li><strong>Frontend (React):</strong> Receives requests via REST controller</li>
 *   <li><strong>Python Service (FastAPI):</strong> Performs actual transcription processing</li>
 * </ul>
 * 
 * <h3>Performance Benefits:</h3>
 * <ul>
 *   <li><strong>Instant Results:</strong> No polling required, immediate response</li>
 *   <li><strong>Simplified Architecture:</strong> No database overhead</li>
 *   <li><strong>Reduced Latency:</strong> Direct communication with transcription service</li>
 *   <li><strong>Stateless Operation:</strong> No persistent state management</li>
 * </ul>
 * 
 * @author shangmin
 * @version 2.0
 * @since 2024
 * 
 * @see com.shangmin.whisperrr.controller.AudioController
 */
public interface AudioService {

    /**
     * Process an audio file for direct transcription.
     * 
     * <p>This method handles the complete transcription workflow including file validation
     * and direct communication with the Python transcription service. Results are returned
     * immediately without any persistence.</p>
     * 
     * <h4>Processing Steps:</h4>
     * <ol>
     *   <li>Validate file format, size, and content type</li>
     *   <li>Send file directly to Python transcription service</li>
     *   <li>Receive and return transcription results</li>
     * </ol>
     * 
     * <h4>File Validation Rules:</h4>
     * <ul>
     *   <li>Supported formats: MP3, WAV, M4A, FLAC, OGG, WMA</li>
     *   <li>Maximum file size: 25MB (configurable)</li>
     *   <li>Valid audio content type required</li>
     *   <li>Non-empty file with readable content</li>
     * </ul>
     * 
     * @param audioFile the multipart audio file uploaded by the client
     *                  Must be a valid audio file within size and format constraints
     * @return TranscriptionResultResponse containing the transcription text,
     *         language detection, confidence scores, and processing metadata
     * 
     * @throws FileValidationException if the file fails any validation checks
     * @throws TranscriptionProcessingException if the transcription service fails
     * @throws IllegalArgumentException if the audioFile parameter is null or invalid
     * 
     * @see #validateAudioFile(MultipartFile)
     */
    TranscriptionResultResponse transcribeAudio(MultipartFile audioFile);
    
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
     *   <li><strong>WMA:</strong> Windows Media Audio (.wma)</li>
     * </ul>
     * 
     * <h4>Size and Quality Constraints:</h4>
     * <ul>
     *   <li>Maximum file size: 25MB (configurable via application properties)</li>
     *   <li>Minimum file size: 1KB (to prevent empty uploads)</li>
     *   <li>Recommended duration: Up to 2 hours for optimal processing</li>
     * </ul>
     * 
     * @param audioFile the multipart file to validate
     *                  Must be a non-null MultipartFile instance
     * 
     * @throws FileValidationException with specific error details if validation fails
     * @throws IllegalArgumentException if the audioFile parameter is null
     * 
     * @see com.shangmin.whisperrr.exception.FileValidationException
     */
    void validateAudioFile(MultipartFile audioFile);
}
