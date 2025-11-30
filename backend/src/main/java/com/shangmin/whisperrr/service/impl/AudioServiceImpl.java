package com.shangmin.whisperrr.service.impl;

import com.shangmin.whisperrr.dto.*;
import com.shangmin.whisperrr.exception.FileValidationException;
import com.shangmin.whisperrr.exception.TranscriptionProcessingException;
import com.shangmin.whisperrr.service.AudioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import jakarta.annotation.PostConstruct;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Simplified implementation of AudioService for direct audio transcription.
 * 
 * <p>This implementation provides instant transcription by communicating directly
 * with the Python transcription service without any persistence layer. It validates
 * uploaded audio files and forwards them to the Python service for processing,
 * returning results immediately.</p>
 * 
 * <h3>Key Features:</h3>
 * <ul>
 *   <li><strong>Direct Processing:</strong> No job queuing or status tracking</li>
 *   <li><strong>Instant Results:</strong> Synchronous transcription with immediate response</li>
 *   <li><strong>Stateless Operation:</strong> No persistent storage or job management</li>
 *   <li><strong>Simplified Architecture:</strong> Direct proxy to Python transcription service</li>
 * </ul>
 * 
 * <h3>Processing Flow:</h3>
 * <ol>
 *   <li>Validate uploaded audio file (format, size, content type)</li>
 *   <li>Forward file to Python transcription service via HTTP</li>
 *   <li>Receive transcription results from Python service</li>
 *   <li>Return formatted results to client immediately</li>
 * </ol>
 * 
 * <h3>Error Handling:</h3>
 * <ul>
 *   <li><strong>Validation Errors:</strong> Immediate feedback for invalid files</li>
 *   <li><strong>Service Errors:</strong> Proper error mapping from Python service</li>
 *   <li><strong>Network Errors:</strong> Timeout and connectivity error handling</li>
 *   <li><strong>Processing Errors:</strong> Transcription failure error reporting</li>
 * </ul>
 * 
 * <h3>Performance Benefits:</h3>
 * <ul>
 *   <li>No database overhead or connection management</li>
 *   <li>No job queuing or polling mechanisms</li>
 *   <li>Direct HTTP communication with minimal latency</li>
 *   <li>Simplified error handling and debugging</li>
 * </ul>
 * 
 * @author shangmin
 * @version 2.0
 * @since 2024
 * 
 * @see com.shangmin.whisperrr.service.AudioService
 * @see com.shangmin.whisperrr.controller.AudioController
 */
@Service
public class AudioServiceImpl implements AudioService {
    
    private static final Logger logger = LoggerFactory.getLogger(AudioServiceImpl.class);
    
    private RestTemplate restTemplate;
    
    @Value("${whisperrr.service.url}")
    private String pythonServiceUrl;
    
    @Value("${whisperrr.service.timeout:300000}")
    private int serviceTimeout;
    
    // Supported file types and size limit
    private static final Set<String> SUPPORTED_EXTENSIONS = Set.of("mp3", "wav", "m4a", "flac", "ogg", "wma");
    private static final long MAX_FILE_SIZE = 25 * 1024 * 1024; // 25MB
    
    /**
     * Initialize the REST template for Python service communication
     * with configured timeout settings after dependency injection.
     */
    @PostConstruct
    public void initRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000); // 5 seconds connection timeout
        factory.setReadTimeout(serviceTimeout); // Use configured timeout for read operations
        this.restTemplate = new RestTemplate(factory);
    }
    
    @Override
    public TranscriptionResultResponse transcribeAudio(MultipartFile audioFile) {
        logger.info("Processing direct transcription for file: {}", audioFile.getOriginalFilename());
        
        // Validate the file
        validateAudioFile(audioFile);
        
        try {
            // Prepare multipart request for Python service
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            
            // Create multipart body
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            
            // Convert MultipartFile to ByteArrayResource for RestTemplate
            ByteArrayResource fileResource = new ByteArrayResource(audioFile.getBytes()) {
                @Override
                public String getFilename() {
                    return audioFile.getOriginalFilename();
                }
            };
            
            body.add("file", fileResource);
            
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            
            // Call Python transcription service
            String transcribeUrl = pythonServiceUrl + "/transcribe";
            logger.info("Sending transcription request to: {}", transcribeUrl);
            
            ResponseEntity<Map> response = restTemplate.postForEntity(
                transcribeUrl, 
                requestEntity, 
                Map.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> result = response.getBody();
                
                // Extract transcription data from Python service response with null safety
                String transcriptionText = result.get("text") != null ? (String) result.get("text") : "";
                String language = result.get("language") != null ? (String) result.get("language") : "unknown";
                Double confidence = result.get("confidence_score") != null ? 
                    ((Number) result.get("confidence_score")).doubleValue() : null;
                Double duration = result.get("duration") != null ? 
                    ((Number) result.get("duration")).doubleValue() : null;
                String modelUsed = result.get("model_used") != null ? (String) result.get("model_used") : "unknown";
                Double processingTime = result.get("processing_time") != null ? 
                    ((Number) result.get("processing_time")).doubleValue() : null;
                
                // Validate that we have at least transcription text
                if (transcriptionText == null || transcriptionText.isEmpty()) {
                    throw new TranscriptionProcessingException("Python service returned empty transcription result");
                }
                
                logger.info("Transcription completed successfully for file: {}", audioFile.getOriginalFilename());
                
                // Create and return result response
                TranscriptionResultResponse resultResponse = new TranscriptionResultResponse();
                resultResponse.setTranscriptionText(transcriptionText);
                resultResponse.setLanguage(language);
                resultResponse.setConfidence(confidence);
                resultResponse.setDuration(duration);
                resultResponse.setModelUsed(modelUsed);
                resultResponse.setProcessingTime(processingTime);
                resultResponse.setCompletedAt(LocalDateTime.now());
                resultResponse.setStatus(TranscriptionStatus.COMPLETED);
                
                return resultResponse;
            } else {
                throw new TranscriptionProcessingException("Python service returned unexpected response: " + 
                    (response.getStatusCode() != null ? response.getStatusCode() : "null status"));
            }
            
        } catch (ResourceAccessException e) {
            logger.error("Failed to connect to Python transcription service: {}", e.getMessage(), e);
            throw new TranscriptionProcessingException("Transcription service is unavailable: " + e.getMessage(), e);
        } catch (HttpClientErrorException e) {
            logger.error("Python service returned client error ({}): {}", e.getStatusCode(), e.getMessage(), e);
            throw new TranscriptionProcessingException("Transcription service client error: " + e.getMessage(), e);
        } catch (HttpServerErrorException e) {
            logger.error("Python service returned server error ({}): {}", e.getStatusCode(), e.getMessage(), e);
            throw new TranscriptionProcessingException("Transcription service server error: " + e.getMessage(), e);
        } catch (TranscriptionProcessingException e) {
            // Re-throw processing exceptions as-is
            throw e;
        } catch (Exception e) {
            logger.error("Transcription failed for file: {}", audioFile.getOriginalFilename(), e);
            throw new TranscriptionProcessingException("Transcription processing failed: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void validateAudioFile(MultipartFile audioFile) {
        if (audioFile == null || audioFile.isEmpty()) {
            throw new FileValidationException("Audio file is required");
        }
        
        // Check file size
        if (audioFile.getSize() > MAX_FILE_SIZE) {
            throw new FileValidationException("File size exceeds maximum allowed size of 25MB");
        }
        
        // Check file extension
        String originalFilename = audioFile.getOriginalFilename();
        if (originalFilename == null) {
            throw new FileValidationException("File must have a valid name");
        }
        
        String extension = getFileExtension(originalFilename).toLowerCase();
        if (!SUPPORTED_EXTENSIONS.contains(extension)) {
            throw new FileValidationException("Unsupported file type. Supported types: " + SUPPORTED_EXTENSIONS);
        }
        
        // Check content type
        String contentType = audioFile.getContentType();
        if (contentType == null || !contentType.startsWith("audio/")) {
            throw new FileValidationException("File must be an audio file");
        }
        
        logger.debug("File validation passed for: {}", originalFilename);
    }
    
    /**
     * Extract file extension from filename.
     * 
     * @param filename the filename to extract extension from
     * @return the file extension without the dot, or empty string if no extension
     */
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex == -1 ? "" : filename.substring(lastDotIndex + 1);
    }
}
