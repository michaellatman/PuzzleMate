package edu.loyola.puzzlemate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

/**
 * An activity representing a single Puzzle detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link PuzzleListActivity}.
 *  @author Michael Latman, Billy Quintano, Doug Robie
 */
public class PuzzleDetailActivity extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;

    /**
     * Overrides the on create object
     * @param savedInstanceState the bundle of the saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_detail);

        MobileAds.initialize(this,
                "ca-app-pub-3940256099942544~3347511713");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                Log.d("Ads", "Loaded");
                mInterstitialAd.show();
            }

        });


        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(PuzzleDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(PuzzleDetailFragment.ARG_ITEM_ID));

            arguments.putString(PuzzleDetailFragment.ARG_ITEM_NAME,
                    getIntent().getStringExtra(PuzzleDetailFragment.ARG_ITEM_NAME));

            arguments.putString(PuzzleDetailFragment.ARG_ITEM_DESCRIPTION,
                    getIntent().getStringExtra(PuzzleDetailFragment.ARG_ITEM_DESCRIPTION));
            arguments.putString(PuzzleDetailFragment.ARG_ITEM_IMAGE_URL,
                    getIntent().getStringExtra(PuzzleDetailFragment.ARG_ITEM_IMAGE_URL));

            PuzzleDetailFragment fragment = new PuzzleDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.puzzle_detail_container, fragment)
                    .commit();
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(PuzzleDetailActivity.this,
                        PuzzleSolveActivity.class);

                myIntent.putExtra(PuzzleDetailFragment.ARG_ITEM_IMAGE_URL,
                        getIntent().getStringExtra(PuzzleDetailFragment.ARG_ITEM_IMAGE_URL));
                myIntent.putExtra(PuzzleDetailFragment.ARG_ITEM_ID,
                        getIntent().getStringExtra(PuzzleDetailFragment.ARG_ITEM_ID));

                myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                (PuzzleDetailActivity.this).startActivity(myIntent);
            }
        });
    }

    /**
     * override on the onoptionsitemselected
     * @param item the new menu item object
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, PuzzleListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
