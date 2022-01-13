package com.autotrade.connector.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "connector")
@Data
public class ConnectionSettings {
    private String configPath;
    private String demoConfigPath;
}
