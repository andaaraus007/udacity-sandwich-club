package com.udacity.sandwichclub.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    public static final String KEY_NAME = "name";
    public static final String KEY_MAIN_NAME = "mainName";
    public static final String KEY_ALSO_KNOWN_AS = "alsoKnownAs";
    public static final String KEY_PLACE_OF_ORIGIN = "placeOfOrigin";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_INGREDIENTS = "ingredients";

    public static Sandwich parseSandwichJson(String json) throws JSONException {
        Sandwich sandwich = null;
        JSONObject sandwichJsonObj = new JSONObject(json);

        if (sandwichJsonObj instanceof JSONObject) {
            String mainName = null;
            List<String> alsoKnownAs = new ArrayList<>();

            JSONObject nameJsonObj = optJSONObject(sandwichJsonObj, KEY_NAME);

            if (nameJsonObj != null) {
                mainName = optString(nameJsonObj, KEY_MAIN_NAME);
                alsoKnownAs = optJSONStringList(nameJsonObj, KEY_ALSO_KNOWN_AS);
            }

            String placeOfOrigin = optString(sandwichJsonObj, KEY_PLACE_OF_ORIGIN);
            String description = optString(sandwichJsonObj, KEY_DESCRIPTION);
            String image = optString(sandwichJsonObj, KEY_IMAGE);
            List<String> ingredients = optJSONStringList(sandwichJsonObj, KEY_INGREDIENTS);

            sandwich = new Sandwich(mainName, alsoKnownAs, placeOfOrigin, description, image, ingredients);
        }

        return sandwich;
    }
    @Nullable
    public static String optString(@NonNull JSONObject json, @NonNull String parameter) throws JSONException {
        if (isParameterValid(json, parameter)) {
            return json.getString(parameter);
        }
        return null;
    }

    @Nullable
    public static JSONObject optJSONObject(@NonNull JSONObject json, @NonNull String parameter) throws JSONException {
        if (isParameterValid(json, parameter)) {
            JSONObject response = json.optJSONObject(parameter);
            if (response == null) {
                response = new JSONObject(json.getString(parameter));
            }
            return response;
        }
        return null;
    }

    @NonNull
    public static List<String> optJSONStringList(@NonNull JSONObject json, @NonNull String parameter) throws JSONException {
        List<String> strings = new ArrayList<>();
        if (isParameterValid(json, parameter)) {
            JSONArray array = json.optJSONArray(parameter);
            if (array != null) {
                for (int i = 0; i < array.length(); i++) {
                    strings.add(array.getString(i));
                }
            } else {
                strings.add(json.getString(parameter));
            }
        }
        return strings;
    }

    private static boolean isParameterValid(@NonNull JSONObject json, @NonNull String parameter) {
        return json.has(parameter) && !json.isNull(parameter);
    }

}
