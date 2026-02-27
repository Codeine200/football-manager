package org.example.footballmanager.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "uploads")
public class UploadProperties {
    private String teamsPhotosFolder;
    private String playersPhotosFolder;
}