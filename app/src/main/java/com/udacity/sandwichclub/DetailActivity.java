package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import org.json.JSONException;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    Sandwich mSandwich;
    TextView mAlsoKnownAsTextView;
    TextView mOriginTextView;
    TextView mDescriptionTextView;
    TextView mIngredientsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);
        mAlsoKnownAsTextView = findViewById(R.id.also_known_tv);
        mOriginTextView = findViewById(R.id.origin_tv);
        mDescriptionTextView = findViewById(R.id.description_tv);
        mIngredientsTextView = findViewById(R.id.ingredients_tv);

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
        try {
            mSandwich = JsonUtils.parseSandwichJson(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mSandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI();
        Picasso.get()
                .load(mSandwich.getImage())
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.no_image)
                .into(ingredientsIv);

        setTitle(mSandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI( ) {
        populateAlsoKnownAs();
        populatePlaceOfOrigin();
        populateDescription();
        populateIngredients();
    }

    private void populateAlsoKnownAs( ) {
        final String SEPARATOR = ",";

        StringBuilder aliases = new StringBuilder();
        List<String> listOfAliases = mSandwich.getAlsoKnownAs();
        if (listOfAliases.size() > 0) {
            for (String alias : listOfAliases) {
                aliases.append(alias);
                aliases.append(SEPARATOR);
            }
            mAlsoKnownAsTextView.append("\n" + aliases.toString().replaceAll(",$", ""));

        } else {
            mAlsoKnownAsTextView.setHeight(0);
        }
    }

    private void populatePlaceOfOrigin() {
        String placeOfOrigin = mSandwich.getPlaceOfOrigin();
        if (!placeOfOrigin.isEmpty()) {
            mOriginTextView.append("\n" + placeOfOrigin);
        } else {
            mOriginTextView.setHeight(0);
        }
    }

    private void populateDescription() {
        String description = mSandwich.getDescription();
        if (!description.isEmpty()) {
            mDescriptionTextView.append("\n" + description);
        } else {
            mDescriptionTextView.setHeight(0);
        }
    }

    private void populateIngredients() {
        List<String> ingredients = mSandwich.getIngredients();
        if (ingredients.size() > 0) {
            for (String ingredient : ingredients) {
                mIngredientsTextView.append("\n• " + ingredient);
            }

        } else {
            mIngredientsTextView.setHeight(0);
        }
    }

}
