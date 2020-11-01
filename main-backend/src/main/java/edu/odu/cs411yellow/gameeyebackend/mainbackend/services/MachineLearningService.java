package edu.odu.cs411yellow.gameeyebackend.mainbackend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Service for querying the machine learning backend.
 */
@Service
public class MachineLearningService {
    private final WebClient webClient;

    public MachineLearningService(@Value("${ml.server.host}") String serverHost, @Value("${ml.server.port}") Integer serverPort) {
        this.webClient = WebClient.builder()
                .baseUrl(String.format("%s:%d", serverHost, serverPort))
                .build();
    }

    /**
     * Predicts the importance for a list of article titles.
     * Queries the machine learning backend.
     *
     * @param articleTitles List of article titles to get predictions for.
     * @return List of impact scores.
     */
    public List<Boolean> predictArticleImportance(final List<String> articleTitles) {
        return this.webClient.post()
                .uri("/predict/article/importance")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(articleTitles), new ParameterizedTypeReference<>(){})
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Boolean>>() {})
                .block();
    }
}
