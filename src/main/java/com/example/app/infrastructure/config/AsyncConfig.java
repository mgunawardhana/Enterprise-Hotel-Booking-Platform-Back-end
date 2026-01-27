package com.example.app.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Enable async processing for audit logging
 */
@Configuration
@EnableAsync
public class AsyncConfig {
}
