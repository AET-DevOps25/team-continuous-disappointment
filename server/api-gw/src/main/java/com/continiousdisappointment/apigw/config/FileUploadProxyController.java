package com.continiousdisappointment.apigw.config;

import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import lombok.RequiredArgsConstructor;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;

@RestController
@RequiredArgsConstructor
public class FileUploadProxyController {

    private final Environment environment;
    private final RestTemplate restTemplate;

    @PostMapping("/genai/**")
    public ResponseEntity<?> proxyMultipartRequest(
            HttpServletRequest request,
            @RequestHeader HttpHeaders headers) {

        try {
            // Extract the remaining path after /genai
            String requestPath = request.getRequestURI();
            String targetPath = requestPath; // Keep the full path including /genai

            // Build target URL
            String genaiServiceUrl = getGenAiServiceUrl();
            String targetUrl = genaiServiceUrl + targetPath;

            // Prepare headers for forwarding (exclude host and content-length)
            HttpHeaders forwardHeaders = new HttpHeaders();
            headers.forEach((name, values) -> {
                String lowerName = name.toLowerCase();
                if (!lowerName.equals("host") && !lowerName.equals("content-length")) {
                    forwardHeaders.put(name, values);
                }
            });

            // Check if this is a multipart request
            if (request instanceof MultipartHttpServletRequest multipartRequest) {
                // Handle multipart request
                MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

                // Add all multipart files
                multipartRequest.getFileMap().forEach((name, file) -> {
                    body.add(name, file.getResource());
                });

                // Add all form parameters
                multipartRequest.getParameterMap().forEach((name, values) -> {
                    for (String value : values) {
                        body.add(name, value);
                    }
                });

                forwardHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

                HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, forwardHeaders);
                return restTemplate.postForEntity(targetUrl, entity, Object.class);
            } else {
                // Handle non-multipart request
                HttpEntity<Object> entity = new HttpEntity<>(forwardHeaders);
                return restTemplate.postForEntity(targetUrl, entity, Object.class);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Proxy error: " + e.getMessage());
        }
    }

    private String getGenAiServiceUrl() {
        if (Arrays.asList(environment.getActiveProfiles()).contains("dev")) {
            return "http://localhost:8000";
        }
        return "http://genai-service:8000";
    }
}