package com.wufel.playground.marvelapi.rest;

import com.wufel.playground.marvelapi.domain.entity.ApiKeys;
import com.wufel.playground.marvelapi.infrastructure.GsonProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;
import java.io.FileReader;

@Configuration
public class MarvelApiKeyConfig {

    public static final String PUBLIC_KEY = "PUBLIC_KEY";
    public static final String PRIVATE_KEY = "PRIVATE_KEY";

    @Value("${marvel.api.key.file.path}")
    private String apiKeyFilePath;

    private ApiKeys getApiKeys() throws FileNotFoundException {
        FileReader reader = new FileReader(apiKeyFilePath);
        return GsonProvider.getInstance().fromJson(reader, ApiKeys.class);
    }

    @Bean(name = PUBLIC_KEY)
    public String publicKey() throws FileNotFoundException {
        return getApiKeys().getPublicKey();
    }

    @Bean(name = PRIVATE_KEY)
    public String privateKey() throws FileNotFoundException {
        return getApiKeys().getPrivateKey();
    }

}
