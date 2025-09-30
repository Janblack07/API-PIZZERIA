package com.example.apipizzeria.Configuration.firebase;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "firebase")
public class FirebaseProperties {
    private String credentialsPath;                           // file:... o classpath:...
    private boolean requireEmailVerified = true;
    private List<String> allowedProviders = Arrays.asList("google.com", "microsoft.com");

    public String getCredentialsPath() { return credentialsPath; }
    public void setCredentialsPath(String credentialsPath) { this.credentialsPath = credentialsPath; }
    public boolean isRequireEmailVerified() { return requireEmailVerified; }
    public void setRequireEmailVerified(boolean requireEmailVerified) { this.requireEmailVerified = requireEmailVerified; }
    public List<String> getAllowedProviders() { return allowedProviders; }
    public void setAllowedProviders(List<String> allowedProviders) { this.allowedProviders = allowedProviders; }
}