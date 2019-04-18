package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private Sandwich sandwich = null;

    private TextView placeOfOriginTitleTv;
    private TextView placeOfOriginTv;
    private TextView alsoKnownAsTitleTv;
    private TextView alsoKnownAsTv;
    private TextView descriptionTv;
    private TextView ingredientsTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final ImageView ingredientsIv = findViewById(R.id.image_iv);
        placeOfOriginTitleTv = findViewById(R.id.place_of_origin_title_tv);
        placeOfOriginTv = findViewById(R.id.origin_tv);
        alsoKnownAsTitleTv = findViewById(R.id.also_known_title_tv);
        alsoKnownAsTv = findViewById(R.id.also_known_tv);
        descriptionTv = findViewById(R.id.description_tv);
        ingredientsTv = findViewById(R.id.ingredients_tv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI();
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv, new Callback.EmptyCallback() {
                    @Override
                    public void onError() {
                        ingredientsIv.setVisibility(View.GONE);
                    }
                });

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI() {
        if (!sandwich.getPlaceOfOrigin().isEmpty()) {
            placeOfOriginTv.setText(sandwich.getPlaceOfOrigin());
        } else {
            placeOfOriginTitleTv.setVisibility(View.GONE);
            placeOfOriginTv.setVisibility(View.GONE);
        }


        if (sandwich.getAlsoKnownAs().size() > 0) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                String aka = String.join(", ", sandwich.getAlsoKnownAs());
                alsoKnownAsTv.setText(aka);
            } else {
                StringBuilder sb = new StringBuilder();
                for(String i : sandwich.getAlsoKnownAs()){
                    sb.append(i).append(", ");
                }
                sb.delete(sb.length()-2, sb.length());
                alsoKnownAsTv.setText(sb.toString());
            }
        } else {
            alsoKnownAsTitleTv.setVisibility(View.GONE);
            alsoKnownAsTv.setVisibility(View.GONE);
        }

        descriptionTv.setText(sandwich.getDescription());

        if (sandwich.getIngredients().size() > 0) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                String ingredients = String.join(", ", sandwich.getIngredients());
                ingredientsTv.setText(ingredients);
            } else {
                StringBuilder sb = new StringBuilder();
                for(String i : sandwich.getIngredients()){
                    sb.append(i).append(", ");
                }
                sb.delete(sb.length()-2, sb.length());
                ingredientsTv.setText(sb.toString());
            }
        }
    }
}
