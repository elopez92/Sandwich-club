package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String JSON_NAME = "name";
    private static final String JSON_MAIN_NAME = "mainName";
    private static final String JSON_AKA = "alsoKnownAs";
    private static final String JSON_POO = "placeOfOrigin";
    private static final String JSON_DESC = "description";
    private static final String JSON_IMAGE = "image";
    private static final String JSON_ING = "ingredients";

    public static Sandwich parseSandwichJson(String json) {
        Sandwich newSandwich = null;
        try {
            JSONObject sandwich = new JSONObject(json);
            JSONObject name = sandwich.optJSONObject(JSON_NAME);
            String mainName = name.optString(JSON_MAIN_NAME);

            List<String> alsoKnownAs = new ArrayList<>();
            if(name.has(JSON_AKA)) {
                JSONArray jAKA = name.optJSONArray(JSON_AKA);
                if (jAKA != null) {
                    for (int i = 0; i < jAKA.length(); i++) {
                        alsoKnownAs.add(jAKA.getString(i));
                    }
                }
            }
            String placeOfOrigin = sandwich.optString(JSON_POO);
            String description = sandwich.optString(JSON_DESC);
            String image = sandwich.optString(JSON_IMAGE);

            List<String> ingredients = new ArrayList<>();
            if(sandwich.has(JSON_ING)) {
                JSONArray jIngredients = sandwich.optJSONArray(JSON_ING);
                if (jIngredients != null) {
                    for (int i = 0; i < jIngredients.length(); i++) {
                        ingredients.add(jIngredients.getString(i));
                    }
                }
            }
            newSandwich = new Sandwich(mainName, alsoKnownAs, placeOfOrigin, description,
                    image, ingredients);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return newSandwich;
    }
}
