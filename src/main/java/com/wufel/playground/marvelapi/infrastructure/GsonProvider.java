package com.wufel.playground.marvelapi.infrastructure;

import com.google.gson.Gson;

import java.util.Objects;

public final class GsonProvider {

    private static Gson GSON;
    private GsonProvider() {}

    public static Gson getInstance() {
        if(Objects.isNull(GSON)){
            GSON = new Gson();
        }
        return GSON;
    }
}
