package com.wufel.playground.marvelapi.rest;

import com.wufel.playground.marvelapi.domain.entity.Character;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

import static com.wufel.playground.marvelapi.infrastructure.mappers.JsonMapperUtil.*;
import static com.wufel.playground.marvelapi.rest.RestConfig.CHARACTER_DETAIL_CACHE;
import static com.wufel.playground.marvelapi.rest.RestConfig.CHARACTER_ID_LIST_CACHE;

@RestController
public class MarvelApiController {
    public static Logger LOG = LoggerFactory.getLogger(MarvelApiController.class);

    public static final String CHARACTERS_REQUEST_PATH = "/characters";
    public static final String CHARACTER_ID = "characterId";

    private final MarvelRestCommunication marvelRestCommunicationService;
    private final String marvelApiPath;

    MarvelApiController(@Autowired MarvelRestCommunication marvelRestCommunicationService,
                        @Value("${marvel.api.public.http}") String marvelApiPath) {
        this.marvelRestCommunicationService = marvelRestCommunicationService;
        this.marvelApiPath = marvelApiPath;
    }

    @GetMapping(CHARACTERS_REQUEST_PATH)
    @Cacheable(CHARACTER_ID_LIST_CACHE)
    public Set<BigDecimal> getCharacters() {
        LOG.info("fetching list of character ids");
        ResponseEntity<String> stringResponseEntity = marvelRestCommunicationService.makeGetRequest(marvelApiPath + CHARACTERS_REQUEST_PATH);
        checkStatus(stringResponseEntity);
        return mapToIdSet(stringResponseEntity.getBody());
    }

    @GetMapping(CHARACTERS_REQUEST_PATH + "/{" + CHARACTER_ID + "}")
    @Cacheable(CHARACTER_DETAIL_CACHE)
    public Character getCharacterById(@PathVariable(CHARACTER_ID) Integer characterId) {
        LOG.info("fetching character detail for id={}", characterId);
        if (Objects.isNull(characterId) || characterId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("id=%d parsed in is not valid!", characterId));
        }
        ResponseEntity<String> stringResponseEntity = marvelRestCommunicationService.makeGetRequest(marvelApiPath + CHARACTERS_REQUEST_PATH + "/" + characterId);
        checkStatus(stringResponseEntity);
        return mapToCharacter(stringResponseEntity.getBody());
    }

    private void checkStatus(ResponseEntity<String> stringResponseEntity) {
        if (!stringResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
            throw new ResponseStatusException(stringResponseEntity.getStatusCode(), stringResponseEntity.getBody());
        }
    }

}
