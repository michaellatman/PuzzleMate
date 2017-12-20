package edu.loyola.puzzlemate;

/**
 *  Puzzle creator class
 *  @author Michael Latman, Billy Quintano, Doug Robie
 */

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import edu.loyola.puzzlemate.models.Puzzle;

public class PuzzleCreateActivity extends AppCompatActivity {

    /**
     * the integer request code picker
     */
    private static final Integer REQUEST_CODE_PICKER = 123;
    /**
     * the new text view for puzzle name
     */
    TextView puzzleNameView;
    /**
     * the new image view for puzzle image
     */
    ImageView puzzleImageView;
    /**
     * the new puzzle image error view
     */
    TextView puzzleImageErrorView;
    /**
     * the new textview puzzle tag entry
     */
    TextView puzzleTagEntryView;
    /**
     * The new tagview
     */
    TagView puzzleTagView;
    /**
     * the new puzzle desc view
     */
    TextView puzzleDescView;
    /**
     * the new puzzle create button
     */
    Button puzzleCreateButton;
    /**
     * the new boolean image set
     */
    boolean imageSet;

    /**
     * the new file image set to null
     */
    File image = null;


    /**
     * overrides the oncreate of the activity
     * @param savedInstanceState the new instance state for the object
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_puzzle_create);
        puzzleNameView = findViewById(R.id.puzzle_name);
        puzzleImageView = (ImageButton) findViewById(R.id.puzzle_image);
        puzzleImageErrorView = findViewById(R.id.puzzle_image_error);
        puzzleTagEntryView = findViewById(R.id.tag_field);
        puzzleTagView = findViewById(R.id.puzzle_tags);
        puzzleDescView = findViewById(R.id.puzzle_Description);
        puzzleCreateButton = findViewById(R.id.button_create_puzzle);

        imageSet = false;
        puzzleImageErrorView.setVisibility(View.INVISIBLE);

        puzzleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub

                ImagePicker.create(PuzzleCreateActivity.this)
                        .returnAfterFirst(true) // set whether pick or camera action should return immediate result or not. For pick image only work on single mode
                        .folderMode(true) // folder mode (false by default)
                        .folderTitle("Folder") // folder selection title
                        .imageTitle("Tap to select") // image selection title
                        .single() // single mode
                        .limit(1) // max images can be selected (99 by default)
                        .showCamera(true) // show camera or not (true by default)
                        .start(REQUEST_CODE_PICKER); // start image picker activity with request code

                //Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivityForResult(intent, 0);
            }
        });

        puzzleTagEntryView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    Tag t = new Tag(textView.getText().toString().trim());
                    t.isDeletable = true;
                    puzzleTagView.addTag(t);
                    textView.setText("");
                    return true;
                }
                return false;
            }
        });

        //set click listener
        puzzleTagView.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {
            }
        });

        //set delete listener
        puzzleTagView.setOnTagDeleteListener(new TagView.OnTagDeleteListener() {
            @Override
            public void onTagDeleted(final TagView view, final Tag tag, final int position) {
                puzzleTagView.remove(position);
            }
        });

        //set long click listener
        puzzleTagView.setOnTagLongClickListener(new TagView.OnTagLongClickListener() {
            @Override
            public void onTagLongClick(Tag tag, int position) {
            }
        });

        puzzleCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptCreate();
            }
        });
    }

    /**
     * the activity result of the item
     * @param requestCode the new int for the request code
     * @param resultCode the new int for the result code
     * @param data the intent for the dada
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = (ArrayList<Image>) ImagePicker.getImages(data);
            Image first = images.get(0);
            File file = new File(first.getPath());
            image = file;
            puzzleImageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            imageSet = true;

            setProgressBarIndeterminateVisibility(true);
            @SuppressLint("StaticFieldLeak") AsyncTask<File, String, List<EntityAnnotation>> execute = new AsyncTask<File, String, List<EntityAnnotation>>() {

                @Override
                protected List<EntityAnnotation> doInBackground(File... files) {
                    Vision.Builder visionBuilder = new Vision.Builder(
                            new NetHttpTransport(),
                            new AndroidJsonFactory(),
                            null);

                    visionBuilder.setVisionRequestInitializer(
                            new VisionRequestInitializer("API_KEY"));

                    byte[] photoData;
                    try {
                        photoData = FileUtils.readFileToByteArray(image);

                        com.google.api.services.vision.v1.model.Image img = new com.google.api.services.vision.v1.model.Image();
                        img.encodeContent(photoData);
                        Vision vision = visionBuilder.build();
                        Feature desiredFeature = new Feature();
                        desiredFeature.setType("LABEL_DETECTION");

                        AnnotateImageRequest request = new AnnotateImageRequest();
                        request.setImage(img);
                        request.setFeatures(Arrays.asList(desiredFeature));

                        BatchAnnotateImagesRequest batchRequest =
                                new BatchAnnotateImagesRequest();

                        batchRequest.setRequests(Arrays.asList(request));

                        BatchAnnotateImagesResponse batchResponse =
                                vision.images().annotate(batchRequest).execute();
                        List<AnnotateImageResponse> bir = batchResponse.getResponses();
                        if (bir.size() > 0) {
                            List<EntityAnnotation> labels = batchResponse.getResponses()
                                    .get(0).getLabelAnnotations();
                            return labels;
                        }
                        return null;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(List<EntityAnnotation> labels) {
                    super.onPostExecute(labels);

                    if (labels.size() > 0) {
                        puzzleTagView.removeAll();
                    }
                    for (EntityAnnotation label : labels) {
                        if (label.getScore() > 0.75) {
                            Tag t = new Tag(label.getDescription());
                            t.isDeletable = true;
                            puzzleTagView.addTag(t);
                        }
                    }
                }
            };
            execute.execute(image);


        }

    }

    /**
     * Attempts to create the puzzle specified by the create puzzle form.
     * If there are form errors (missing fields, etc.), the
     * errors are presented and no actual creation attempt is made.
     */
    private void attemptCreate() {

        //set error if no image was selected
        if (!imageSet) {
            puzzleImageErrorView.setVisibility(View.VISIBLE);
            return;
        }
        puzzleImageErrorView.setVisibility(View.INVISIBLE);

        // Reset errors.
        puzzleNameView.setError(null);

        // Store values at the time of the login attempt.
        String title = puzzleNameView.getText().toString();

        boolean first = true;

        StringBuilder tagList = new StringBuilder();
        for (Tag tag : puzzleTagView.getTags()) {
            if (!first) {
                tagList.append(",");
            }
            tagList.append(tag.text);
            first = false;
        }

        String finalTagList = tagList.toString();
        String description = puzzleDescView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid fields.
        if (TextUtils.isEmpty(title)) {
            puzzleNameView.setError(getString(R.string.error_field_required));
            focusView = puzzleNameView;
            cancel = true;
        }
        if (TextUtils.isEmpty(description)) {
            puzzleDescView.setError(getString(R.string.error_field_required));
            focusView = puzzleNameView;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(PuzzleCreateActivity.this);
            builder.setMessage("One sec...");
            builder.setCancelable(false);
            // Create the AlertDialog object and return it
            final AlertDialog alert = builder.create();
            alert.show();

            // Kick off a background task to perform the puzzle create attempt.
            PuzzleRestClient.doCreatePuzzle(this, title, image, finalTagList, description, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.d("Create Puzzle", "Pass");
                    Intent intent = new Intent(PuzzleCreateActivity.this, PuzzleDetailActivity.class);
                    JSONObject body = null;
                    alert.hide();
                    try {

                        body = new JSONObject(new String(responseBody));
                        Gson gson = new Gson();
                        Puzzle item = gson.fromJson(body.getJSONObject("data").toString(), Puzzle.class);

                        intent.putExtra(PuzzleDetailFragment.ARG_ITEM_ID, "" + item.getId());
                        intent.putExtra(PuzzleDetailFragment.ARG_ITEM_NAME, "" + item.getName());
                        intent.putExtra(PuzzleDetailFragment.ARG_ITEM_IMAGE_URL, "" + item.getImage_url());
                        intent.putExtra(PuzzleDetailFragment.ARG_ITEM_DESCRIPTION, "" + item.getDescription());

                        PuzzleCreateActivity.this.startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    alert.hide();
                    Toast.makeText(PuzzleCreateActivity.this, "Something went wrong :(", Toast.LENGTH_LONG).show();
                    Log.d("Create Puzzle", "Fail " + new String(responseBody));
                }
            });

        }
    }
}
