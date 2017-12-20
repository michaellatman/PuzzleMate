package edu.loyola.puzzlemate;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;

/**
 * A fragment representing a single Puzzle detail screen.
 * This fragment is either contained in a {@link PuzzleListActivity}
 * in two-pane mode (on tablets) or a {@link PuzzleDetailActivity}
 * on handsets.
 *  @author Michael Latman, Billy Quintano, Doug Robie
 */
public class PuzzleDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_NAME = "item_name";
    public static final String ARG_ITEM_IMAGE_URL = "item_image_url";
    public static final String ARG_ITEM_DESCRIPTION = "item_description";
    ImageView imageView = null;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PuzzleDetailFragment() {
    }

    /**
     * Overrides the on create for the activity
     * @param savedInstanceState the new bundle for the saved instance
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(getArguments().getString(ARG_ITEM_NAME));
        }
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.


        }


    }

    /**
     * The new onCreate view
     * @param inflater the new Layout Inflater
     * @param container the new viewgroup of the container
     * @param savedInstanceState the new bundle for the saved instance state
     * @return the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.puzzle_detail, container, false);

        ((TextView) rootView.findViewById(R.id.puzzle_detail)).setText(getArguments().getString(ARG_ITEM_DESCRIPTION));
        imageView = rootView.findViewById(R.id.imagePreview);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(getArguments().getString(ARG_ITEM_IMAGE_URL), new FileAsyncHttpResponseHandler(getContext()) {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, File file) {

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, File file) {
                imageView.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
            }
        });





        return rootView;
    }
}
