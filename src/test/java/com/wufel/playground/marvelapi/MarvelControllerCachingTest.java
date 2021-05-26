package com.wufel.playground.marvelapi;

import com.wufel.playground.marvelapi.domain.entity.Character;
import com.wufel.playground.marvelapi.rest.MarvelApiController;
import com.wufel.playground.marvelapi.rest.MarvelRestCommunication;
import com.wufel.playground.marvelapi.rest.RestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.Set;

import static com.wufel.playground.marvelapi.MarvelApiControllerTests.CHARACTERS_MOCK_RETURN;
import static com.wufel.playground.marvelapi.MarvelApiControllerTests.CHARACTER_DETAIL_MOCK_RETURN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RestConfig.class, MarvelApiController.class})
public class MarvelControllerCachingTest {

    @Autowired
    private MarvelApiController controller;

    @MockBean
    private MarvelRestCommunication restCommunicationService;

    @Test
    public void testControllerCharactersCachingAsExpected() throws Exception {
        when(restCommunicationService.makeGetRequest(any())).thenReturn(ResponseEntity.of(Optional.of(CHARACTERS_MOCK_RETURN)));
        Set<BigDecimal> characters = controller.getCharacters();
        assertEquals(4, characters.size());

        when(restCommunicationService.makeGetRequest(any())).thenReturn(ResponseEntity.badRequest().body("This is a bad request!"));
        characters = controller.getCharacters();
        assertEquals(4, characters.size());

        verify(restCommunicationService, times(1)).makeGetRequest(any());
    }

    @Test
    public void testControllerCharacterIdFetchCachingAsExpected() throws Exception {
        when(restCommunicationService.makeGetRequest(any())).thenReturn(ResponseEntity.of(Optional.of(CHARACTER_DETAIL_MOCK_RETURN)));
        Character character = controller.getCharacterById(1);
        assertEquals(BigInteger.valueOf(0), character.getId());

        when(restCommunicationService.makeGetRequest(any())).thenReturn(ResponseEntity.badRequest().body("This is a bad request!"));
        character = controller.getCharacterById(1);
        assertEquals(BigInteger.valueOf(0), character.getId());

        verify(restCommunicationService, times(1)).makeGetRequest(any());
    }

}
