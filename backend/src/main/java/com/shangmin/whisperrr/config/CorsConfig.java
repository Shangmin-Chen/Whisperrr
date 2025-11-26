package com.shangmin.whisperrr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

/**
 * Cross-Origin Resource Sharing (CORS) configuration for frontend communication.
 * 
 * <p>This configuration class enables the Spring Boot backend to accept cross-origin
 * requests from the React frontend application. CORS is essential for modern web
 * applications where the frontend and backend are served from different origins
 * (different protocols, domains, or ports).</p>
 * 
 * <h3>CORS Configuration Overview:</h3>
 * <p>The Whisperrr application requires CORS configuration because:</p>
 * <ul>
 *   <li><strong>Frontend:</strong> React app running on http://localhost:3000 (development)</li>
 *   <li><strong>Backend:</strong> Spring Boot API running on http://localhost:8080</li>
 *   <li><strong>Production:</strong> Frontend and backend may be on different domains</li>
 * </ul>
 * 
 * <h3>Security Considerations:</h3>
 * <ul>
 *   <li><strong>Allowed Origins:</strong> Configurable list of trusted frontend URLs</li>
 *   <li><strong>Allowed Methods:</strong> Limited to necessary HTTP methods (GET, POST, etc.)</li>
 *   <li><strong>Allowed Headers:</strong> Controlled set of permitted request headers</li>
 *   <li><strong>Credentials:</strong> Configurable support for cookies and authentication</li>
 *   <li><strong>Preflight Caching:</strong> Optimized preflight request handling</li>
 * </ul>
 * 
 * <h3>Configuration Sources:</h3>
 * <p>CORS settings are externalized in application.properties for easy environment-specific
 * configuration without code changes:</p>
 * <ul>
 *   <li>cors.allowed-origins: Comma-separated list of allowed frontend URLs</li>
 *   <li>cors.allowed-methods: HTTP methods permitted for cross-origin requests</li>
 *   <li>cors.allowed-headers: Headers that can be sent in cross-origin requests</li>
 *   <li>cors.allow-credentials: Whether to include cookies/auth in requests</li>
 * </ul>
 * 
 * <h3>Development vs Production:</h3>
 * <ul>
 *   <li><strong>Development:</strong> Permissive settings for local development</li>
 *   <li><strong>Production:</strong> Restrictive settings with specific domain whitelist</li>
 *   <li><strong>Testing:</strong> Configurable for different testing environments</li>
 * </ul>
 * 
 * <h3>Implementation Details:</h3>
 * <p>This class provides two complementary CORS configurations:</p>
 * <ol>
 *   <li><strong>WebMvcConfigurer:</strong> Global CORS mapping for all endpoints</li>
 *   <li><strong>CorsConfigurationSource Bean:</strong> Fine-grained control for specific paths</li>
 * </ol>
 * 
 * <h3>Preflight Request Handling:</h3>
 * <p>The configuration optimizes CORS preflight requests by:</p>
 * <ul>
 *   <li>Setting appropriate max-age for preflight caching (1 hour)</li>
 *   <li>Minimizing preflight requests for simple requests</li>
 *   <li>Providing clear error responses for invalid origins</li>
 * </ul>
 * 
 * @author shangmin
 * @version 1.0
 * @since 2024
 * 
 * @see org.springframework.web.cors.CorsConfiguration
 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurer
 * @see org.springframework.web.cors.CorsConfigurationSource
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Value("${cors.allowed-methods}")
    private String allowedMethods;

    @Value("${cors.allowed-headers}")
    private String allowedHeaders;

    @Value("${cors.allow-credentials}")
    private boolean allowCredentials;

    /**
     * Configure CORS mappings for all API endpoints.
     * 
     * <p>This method sets up global CORS rules that apply to all endpoints under the
     * /api/** path pattern. It uses externalized configuration properties to allow
     * environment-specific CORS settings without code changes.</p>
     * 
     * <h4>Path Mapping:</h4>
     * <ul>
     *   <li><strong>/api/**:</strong> All REST API endpoints for audio transcription</li>
     *   <li>Excludes static resources and actuator endpoints</li>
     *   <li>Covers upload, status, and result endpoints</li>
     * </ul>
     * 
     * <h4>Configuration Properties Used:</h4>
     * <ul>
     *   <li><strong>cors.allowed-origins:</strong> Frontend URLs (e.g., "http://localhost:3000,https://myapp.com")</li>
     *   <li><strong>cors.allowed-methods:</strong> HTTP methods (e.g., "GET,POST,PUT,DELETE,OPTIONS")</li>
     *   <li><strong>cors.allowed-headers:</strong> Request headers (e.g., "*" or specific headers)</li>
     *   <li><strong>cors.allow-credentials:</strong> Cookie/auth support (true/false)</li>
     * </ul>
     * 
     * <h4>Preflight Optimization:</h4>
     * <p>Sets max-age to 3600 seconds (1 hour) to cache preflight responses and reduce
     * the number of OPTIONS requests from browsers, improving performance.</p>
     * 
     * @param registry the CORS registry to configure mappings
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(allowedOrigins.split(","))
                .allowedMethods(allowedMethods.split(","))
                .allowedHeaders(allowedHeaders.split(","))
                .allowCredentials(allowCredentials)
                .maxAge(3600);
    }

    /**
     * Create a CORS configuration source bean for fine-grained control.
     * 
     * <p>This bean provides an alternative CORS configuration method that offers more
     * detailed control over CORS settings. It complements the WebMvcConfigurer approach
     * and can be used by Spring Security or other components that need programmatic
     * access to CORS configuration.</p>
     * 
     * <h4>Configuration Features:</h4>
     * <ul>
     *   <li><strong>Origin Patterns:</strong> Uses allowedOriginPatterns for flexible matching</li>
     *   <li><strong>Method Control:</strong> Explicit control over allowed HTTP methods</li>
     *   <li><strong>Header Management:</strong> Fine-grained header permission control</li>
     *   <li><strong>Credential Handling:</strong> Configurable credential support</li>
     *   <li><strong>Caching:</strong> Optimized preflight response caching</li>
     * </ul>
     * 
     * <h4>URL Pattern Matching:</h4>
     * <p>Registers CORS configuration specifically for /api/** endpoints, ensuring
     * that CORS rules only apply to API endpoints and not to static resources or
     * other application paths.</p>
     * 
     * <h4>Security Considerations:</h4>
     * <ul>
     *   <li>Uses origin patterns instead of wildcards for better security</li>
     *   <li>Explicit method and header allowlists</li>
     *   <li>Configurable credential support based on security requirements</li>
     *   <li>Reasonable cache timeout to balance performance and security</li>
     * </ul>
     * 
     * <h4>Integration:</h4>
     * <p>This bean can be used by:</p>
     * <ul>
     *   <li>Spring Security for authentication-aware CORS handling</li>
     *   <li>Custom filters that need CORS configuration access</li>
     *   <li>Testing frameworks for CORS validation</li>
     *   <li>Monitoring tools for CORS policy inspection</li>
     * </ul>
     * 
     * @return CorsConfigurationSource configured with application-specific CORS rules
     *         for API endpoints, ready for use by Spring Security or other components
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        List<String> origins = Arrays.asList(allowedOrigins.split(","));
        configuration.setAllowedOriginPatterns(origins);
        configuration.setAllowedMethods(Arrays.asList(allowedMethods.split(",")));
        configuration.setAllowedHeaders(Arrays.asList(allowedHeaders.split(",")));
        configuration.setAllowCredentials(allowCredentials);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}
