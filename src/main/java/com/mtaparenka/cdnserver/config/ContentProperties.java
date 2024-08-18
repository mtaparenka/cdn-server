package com.mtaparenka.cdnserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("content")
public record ContentProperties(Images images) {

    public record Images(String path) {
    }
}
