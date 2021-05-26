package com.wufel.playground.marvelapi.infrastructure.mappers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wufel.playground.marvelapi.domain.entity.Character;
import com.wufel.playground.marvelapi.infrastructure.GsonProvider;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class JsonMapperUtil {

    public static Character mapToCharacter(String jsonString) {
        JsonObject result = responseBodyToResultJsonArray(jsonString)
                .get(0).getAsJsonObject();
        return GsonProvider.getInstance().fromJson(result, Character.class);
    }

    public static Set<BigDecimal> mapToIdSet(String jsonString) {
        JsonArray results = responseBodyToResultJsonArray(jsonString);
        return StreamSupport.stream(results.spliterator(), false)
                .map(jsonElement -> jsonElement.getAsJsonObject().get("id").getAsBigDecimal())
                .collect(Collectors.toSet());
    }

    private static JsonArray responseBodyToResultJsonArray(String jsonString) {
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        return jsonObject
                .getAsJsonObject("data")
                .getAsJsonArray("results");
    }

}
