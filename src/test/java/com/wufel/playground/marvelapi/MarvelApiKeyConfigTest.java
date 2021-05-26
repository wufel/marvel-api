package com.wufel.playground.marvelapi;

import com.wufel.playground.marvelapi.rest.MarvelApiKeyConfig;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MarvelApiKeyConfigTest {

    @Test
    public void testKeyFileReadOk() throws URISyntaxException, FileNotFoundException {
        Path path = Paths.get(ClassLoader.getSystemResource("test-api-key.json").toURI());
        MarvelApiKeyConfig marvelApiKeyConfig = new MarvelApiKeyConfig(path.toString());
        assertEquals(marvelApiKeyConfig.privateKey(), "1234");
        assertEquals(marvelApiKeyConfig.publicKey(), "abcd");
    }

}
