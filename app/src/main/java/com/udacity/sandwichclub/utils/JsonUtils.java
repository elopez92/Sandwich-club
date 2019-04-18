package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {
        Sandwich newSandwich = null;
        try {
            JSONObject sandwich = new JSONObject(json);
            JSONObject name = sandwich.getJSONObject("name");
            String mainName = name.getString("mainName");

            List<String> alsoKnownAs = new ArrayList<>();
            if(name.has("alsoKnownAs")) {
                JSONArray jAKA = name.getJSONArray("alsoKnownAs");
                if (jAKA != null) {
                    for (int i = 0; i < jAKA.length(); i++) {
                        alsoKnownAs.add(jAKA.getString(i));
                    }
                }
            }
            String placeOfOrigin = sandwich.getString("placeOfOrigin");
            String description = sandwich.getString("description");
            String image = sandwich.getString("image");

            List<String> ingredients = new ArrayList<>();
            if(sandwich.has("ingredients")) {
                JSONArray jIngredients = (JSONArray) sandwich.get("ingredients");
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
