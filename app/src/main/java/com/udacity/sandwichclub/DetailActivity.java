package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private static final String NODATA = "No data available";

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
        setStringText(placeOfOriginTv, sandwich.getPlaceOfOrigin());
        setListText(alsoKnownAsTv, sandwich.getAlsoKnownAs());
        setStringText(descriptionTv, sandwich.getDescription());
        setListText(ingredientsTv, sandwich.getIngredients());
    }

    private void setListText(TextView v, List<String> list){
        if(list.size() != 0) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                String string = TextUtils.join(", ", list);
                v.setText(string);
            } else {
                StringBuilder sb = new StringBuilder();
                for (String i : list) {
                    sb.append(i).append(", ");
                }
                sb.delete(sb.length() - 2, sb.length());
                v.setText(sb.toString());
            }
        }else{
            v.setText(NODATA);
        }
    }

    private void setStringText(TextView v, String string){
        if(!string.equals("")){
            v.setText(string);
        }else
            v.setText(NODATA);
    }
}
