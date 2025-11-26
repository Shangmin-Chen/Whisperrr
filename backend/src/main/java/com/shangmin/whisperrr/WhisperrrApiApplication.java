package com.shangmin.whisperrr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Whisperrr Audio Transcription API.
 * 
 * <p>This is the entry point for the Spring Boot application that provides a REST API
 * for audio transcription services. The application serves as a middleware layer between
 * the React frontend and the Python transcription service, handling:</p>
 * 
 * <ul>
 *   <li>Audio file upload and validation</li>
 *   <li>Transcription job management and status tracking</li>
 *   <li>Database operations for users, audio files, jobs, and transcriptions</li>
 *   <li>Communication with the Python FastAPI transcription service</li>
 *   <li>CORS configuration for frontend communication</li>
 *   <li>Comprehensive error handling and logging</li>
 * </ul>
 * 
 * <h3>Architecture Overview:</h3>
 * <p>The application follows a layered architecture pattern:</p>
 * <ul>
 *   <li><strong>Controller Layer:</strong> REST endpoints for client communication</li>
 *   <li><strong>Service Layer:</strong> Business logic and orchestration</li>
 *   <li><strong>Repository Layer:</strong> Data access and persistence</li>
 *   <li><strong>Entity Layer:</strong> JPA entities representing database tables</li>
 *   <li><strong>DTO Layer:</strong> Data transfer objects for API communication</li>
 * </ul>
 * 
 * <h3>Key Features:</h3>
 * <ul>
 *   <li>RESTful API with comprehensive error handling</li>
 *   <li>PostgreSQL database with Flyway migrations</li>
 *   <li>File upload validation and processing</li>
 *   <li>Asynchronous job processing</li>
 *   <li>Health monitoring and metrics</li>
 *   <li>CORS configuration for cross-origin requests</li>
 * </ul>
 * 
 * <h3>Database Schema:</h3>
 * <p>The application manages four main entities:</p>
 * <ul>
 *   <li><strong>Users:</strong> User accounts and authentication</li>
 *   <li><strong>AudioFiles:</strong> Uploaded audio file metadata</li>
 *   <li><strong>Jobs:</strong> Transcription job tracking and status</li>
 *   <li><strong>Transcriptions:</strong> Transcription results and metadata</li>
 * </ul>
 * 
 * <h3>Configuration:</h3>
 * <p>The application is configured through application.properties and supports:</p>
 * <ul>
 *   <li>Database connection settings</li>
 *   <li>CORS configuration for frontend communication</li>
 *   <li>Python service integration settings</li>
 *   <li>File upload limits and validation rules</li>
 *   <li>Logging and monitoring configuration</li>
 * </ul>
 * 
 * @author shangmin
 * @version 1.0
 * @since 2024
 * 
 * @see com.shangmin.whisperrr.controller.AudioController
 * @see com.shangmin.whisperrr.service.AudioService
 * @see com.shangmin.whisperrr.config.CorsConfig
 * @see com.shangmin.whisperrr.config.JpaConfig
 */
@SpringBootApplication
public class WhisperrrApiApplication {

	/**
	 * Main method to start the Spring Boot application.
	 * 
	 * <p>This method initializes the Spring application context, sets up all
	 * configured beans, starts the embedded Tomcat server, and begins listening
	 * for HTTP requests on the configured port (default: 8080).</p>
	 * 
	 * <p>The application will automatically:</p>
	 * <ul>
	 *   <li>Connect to the PostgreSQL database</li>
	 *   <li>Run any pending Flyway migrations</li>
	 *   <li>Initialize JPA repositories and entities</li>
	 *   <li>Configure CORS for frontend communication</li>
	 *   <li>Set up REST endpoints for audio transcription</li>
	 *   <li>Enable health monitoring and metrics</li>
	 * </ul>
	 * 
	 * @param args command line arguments passed to the application
	 *             Common arguments include:
	 *             --server.port=8080 (change server port)
	 *             --spring.profiles.active=dev (activate dev profile)
	 *             --logging.level.com.shangmin.whisperrr=DEBUG (enable debug logging)
	 */
	public static void main(String[] args) {
		SpringApplication.run(WhisperrrApiApplication.class, args);
	}

}
