package com.caseybrugna.nyc_events.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.caseybrugna.nyc_events.service.SpotifyAPIClient;

@Configuration
public class SpotifyAPIConfig {

    @Bean
    public SpotifyAPIClient spotifyAPIClient() {
        return new SpotifyAPIClient();
    }
}
