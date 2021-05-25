package com.wufel.playground.marvelapi;

import com.google.common.collect.ImmutableSet;
import com.wufel.playground.marvelapi.domain.entity.Character;
import com.wufel.playground.marvelapi.domain.entity.Thumbnail;
import com.wufel.playground.marvelapi.infrastructure.GsonProvider;
import com.wufel.playground.marvelapi.rest.MarvelApiController;
import com.wufel.playground.marvelapi.rest.MarvelRestCommunication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MarvelApiController.class)
class MarvelApiControllerTests {

    private static final String CHARACTER_DETAIL_MOCK_RETURN = "{\"data\":{\"results\":[{\"thumbnail\":{\"path\":\"/a/path\",\"extension\":\"jpg\"},\"id\":0,\"name\":\"something\",\"description\":\"nothing\"}]}}";
    private static final String CHARACTERS_MOCK_RETURN = "{\"data\":{\"results\":[{\"id\":1},{\"id\":2},{\"id\":3},{\"id\":4}]}}";
    private static final MockHttpServletRequestBuilder CHARACTER_DETAIL_REQUEST_BUILDER =
            request(HttpMethod.GET, "/characters/-1")
                    .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .accept(APPLICATION_JSON);
    private static final MockHttpServletRequestBuilder CHARACTERS_REQUEST_BUILDER =
            request(HttpMethod.GET, "/characters")
                    .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .accept(APPLICATION_JSON);
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MarvelRestCommunication restCommunicationService;

    @Test
    public void testGettingCharacterDetailSuccessfully() throws Exception {
        when(restCommunicationService.makeGetRequest(any())).thenReturn(ResponseEntity.of(Optional.of(CHARACTER_DETAIL_MOCK_RETURN)));
        Character expectedCharacter = new Character(new BigDecimal(0), "something", "nothing", new Thumbnail("/a/path", "jpg"));
        String expectedJsonString = GsonProvider.getInstance().toJson(expectedCharacter);
        mockMvc.perform(CHARACTER_DETAIL_REQUEST_BUILDER)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJsonString));
    }

    @Test
    public void testGettingCharactersIdSetSuccessfully() throws Exception {
        when(restCommunicationService.makeGetRequest(any())).thenReturn(ResponseEntity.of(Optional.of(CHARACTERS_MOCK_RETURN)));
        ImmutableSet<Integer> expectedSet = ImmutableSet.of(1, 2, 3, 4);
        String expectedJsonString = GsonProvider.getInstance().toJson(expectedSet);
        mockMvc.perform(CHARACTERS_REQUEST_BUILDER)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJsonString));
    }

    @Test
    public void testGettingBadRequest() throws Exception {
        when(restCommunicationService.makeGetRequest(any())).thenReturn(ResponseEntity.badRequest().body("This is a bad request!"));
        ImmutableSet<Integer> expectedSet = ImmutableSet.of(1, 2, 3, 4);
        mockMvc.perform(CHARACTERS_REQUEST_BUILDER)
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("This is a bad request!")));
        mockMvc.perform(CHARACTER_DETAIL_REQUEST_BUILDER)
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("This is a bad request!")));
    }

}
