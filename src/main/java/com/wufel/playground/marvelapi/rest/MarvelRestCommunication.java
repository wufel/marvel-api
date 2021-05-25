package com.wufel.playground.marvelapi.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static com.wufel.playground.marvelapi.rest.MarvelApiKeyConfig.PRIVATE_KEY;
import static com.wufel.playground.marvelapi.rest.MarvelApiKeyConfig.PUBLIC_KEY;

@Configuration
@Import(MarvelApiKeyConfig.class)
public class MarvelRestCommunication {
    public static Logger LOG = LoggerFactory.getLogger(MarvelRestCommunication.class);

    private final RestOperations restOperations;
    private final String publicKey;
    private final String privateKey;

    public MarvelRestCommunication(@Autowired RestOperations restOperations,
                                   @Qualifier(PUBLIC_KEY) String publicKey,
                                   @Qualifier(PRIVATE_KEY) String privateKey) {
        this.restOperations = restOperations;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public ResponseEntity<String> makeGetRequest(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        String time = LocalDateTime.now().toString();
        String md5Hash = DigestUtils.md5DigestAsHex(time.concat(privateKey).concat(publicKey).getBytes(StandardCharsets.UTF_8));
        String uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("ts", time)
                .queryParam("apikey", publicKey)
                .queryParam("hash", md5Hash)
                .toUriString();

        HttpEntity<?> entity = new HttpEntity<>(headers);
        LOG.info("request ready to fire {}", uri);

        try {
            return restOperations.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    String.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }
}
