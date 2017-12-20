package edu.loyola.puzzlemate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;
import edu.loyola.puzzlemate.models.Puzzle;
import edu.loyola.puzzlemate.models.StatResponse;
import edu.loyola.puzzlemate.utilities.ProviderResponse;
import edu.loyola.puzzlemate.utilities.PuzzleDataUtil;
import edu.loyola.puzzlemate.utilities.PuzzleProvider;

/**
 * An activity representing a list of Puzzles. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link PuzzleDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 *  @author Michael Latman, Billy Quintano, Doug Robie
 */
public class PuzzleListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    /**
     * the viewgroup mstarted
     */
    private ViewGroup mStarted;
    /**
     * view group for completed
     */
    private ViewGroup mCompleted;
    /**
     * viewgroup for not logged in
     */
    private ViewGroup mNotLoggedIn;

    /**
     * the textview for started
     */
    private TextView startedTextView;
    /**
     * the textview for complete text view
     */
    private TextView completeTextView;
    private FloatingActionButton fab;

    /**
     * Overrides the oncreate method for the application
     * @param savedInstanceState the saved instance state for the application
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        mCompleted = findViewById(R.id.completed);
        mNotLoggedIn = findViewById(R.id.notLoggedIn);
        mStarted = findViewById(R.id.started);


        startedTextView = findViewById(R.id.startedCounter);
        completeTextView = findViewById(R.id.completedCounter);


        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, PuzzleCreateActivity.class);
                context.startActivity(intent);
            }
        });

        if (findViewById(R.id.puzzle_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.puzzle_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    /**
     * sets the not logged in
     */
    private void setNotLoggedIn() {
        mCompleted.setVisibility(View.GONE);
        mStarted.setVisibility(View.GONE);
        mNotLoggedIn.setVisibility(View.VISIBLE);
        fab.setVisibility(View.GONE);
    }

    /**
     * sets the logged in
     */
    private void setLoggedIn() {
        mCompleted.setVisibility(View.VISIBLE);
        mStarted.setVisibility(View.VISIBLE);
        mNotLoggedIn.setVisibility(View.GONE);
        fab.setVisibility(View.VISIBLE);

    }

    /**
     * overrides the oncreate options menu
     * @param menu the menu item
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * overrides the onstart option
     */
    @Override
    protected void onStart() {
        super.onStart();

    }

    /**
     * overrides the onresume object
     */
    @Override
    protected void onResume() {
        super.onResume();
        updateStats();
    }

    /**
     * updates the stats
     */
    private void updateStats() {
        if (PuzzleDataUtil.hasAuthToken(this)) {
            Header head = new BasicHeader("Authorization", "Bearer " + PuzzleDataUtil.getAuthToken(this));

            PuzzleRestClient.get(this, "users/me/statistics", new Header[]{head}, null, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.d("Stats", "got stats!");
                    try {
                        setLoggedIn();
                        JSONObject json = new JSONObject(new String(responseBody));
                        JSONObject data = json.getJSONObject("data");
                        Gson gson = new Gson();
                        StatResponse stats = gson.fromJson(data.toString(), StatResponse.class);
                        completeTextView.setText("" + stats.getCompleted());
                        startedTextView.setText("" + stats.getStarted());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("Stats", "failed");
                    if (statusCode == 403) {
                        setNotLoggedIn();
                        PuzzleDataUtil.clearAuthToken(PuzzleListActivity.this);
                        Toast.makeText(PuzzleListActivity.this, "You have been logged out", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            setNotLoggedIn();
        }

    }

    /**
     * overrides the on prepare options menu
     * @param menu the menu option
     * @return super.onPrepareOptionsMenu
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!PuzzleDataUtil.hasAuthToken(this)) {
            menu.getItem(1).setTitle("Login");
        }

        return super.onPrepareOptionsMenu(menu);

    }

    /**
     * overrides the option selected
     * @param item the menu item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            PuzzleRestClient.doLogout(getBaseContext(), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        JSONObject json = new JSONObject(new String(responseBody));
                        if (json.has("message")) {
                            String message = json.getString("message");
                            Log.d("LOGOUT_MESSAGE", message.toString());
                        }
                        json.get("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody,
                                      Throwable error) {
                }
            });

            PuzzleDataUtil.clearAuthToken(getBaseContext());
            Intent myIntent = new Intent(PuzzleListActivity.this,
                    LoginActivity.class);
            (PuzzleListActivity.this).startActivity(myIntent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.app_bar_search) {
            onSearchRequested();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Fill the RecyclerView with puzzles to be displayed
     *
     * @param recyclerView
     */
    private void setupRecyclerView(@NonNull final RecyclerView recyclerView) {
        PuzzleProvider.fetchPuzzles(getBaseContext(), new ProviderResponse<List<Puzzle>>() {
            @Override
            public void success(List<Puzzle> items) {
                Log.d("Good", "To go");
                recyclerView.setAdapter(new SimplePuzzleRecyclerViewAdapter(
                        PuzzleListActivity.this, items, mTwoPane));
            }
        });
    }

    /**
     *  @author Michael Latman, Billy Quintano, Doug Robie
     */
    public static class SimplePuzzleRecyclerViewAdapter
            extends RecyclerView.Adapter<SimplePuzzleRecyclerViewAdapter.ViewHolder> {

        private final PuzzleListActivity mParentActivity;
        private final List<Puzzle> mValues;
        private final boolean mTwoPane;
        /**
         * sets the onclick listener
         */
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Puzzle item = (Puzzle) view.getTag();
                Log.d("click", item.getName());
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(PuzzleDetailFragment.ARG_ITEM_ID, "" + item.getId());
                    arguments.putString(PuzzleDetailFragment.ARG_ITEM_NAME, "" + item.getName());
                    arguments.putString(PuzzleDetailFragment.ARG_ITEM_DESCRIPTION, "" + item.getDescription());
                    PuzzleDetailFragment fragment = new PuzzleDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.puzzle_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, PuzzleDetailActivity.class);
                    intent.putExtra(PuzzleDetailFragment.ARG_ITEM_ID, "" + item.getId());
                    intent.putExtra(PuzzleDetailFragment.ARG_ITEM_NAME, "" + item.getName());
                    intent.putExtra(PuzzleDetailFragment.ARG_ITEM_IMAGE_URL, "" + item.getImage_url());
                    intent.putExtra(PuzzleDetailFragment.ARG_ITEM_DESCRIPTION, "" + item.getDescription());

                    context.startActivity(intent);
                }
            }
        };

        /**
         * sets the simple puzzle Recycler View
         * @param parent puzzle list activity
         * @param items items options
         * @param twoPane the panes
         */
        SimplePuzzleRecyclerViewAdapter(PuzzleListActivity parent, List<Puzzle> items, boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        /**
         * overrides the view holder
         * @param parent viewgroup object of the parent
         * @param viewType the new viewtype
         * @return view holder
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.puzzle_list_content, parent, false);
            return new ViewHolder(view);
        }

        /**
         * overrides the on BindViewHolder
         * @param holder the new view holder
         * @param position
         */
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText("" + mValues.get(position).getName());
            holder.mContentView.setText(mValues.get(position).getDescription());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        /**
         * gets the item count
         * @return mValues item
         */
        @Override
        public int getItemCount() {
            return mValues.size();
        }

        /**
         * the viewholder class for the class
         */
        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = view.findViewById(R.id.id_text);
                mContentView = view.findViewById(R.id.content);
            }
        }
    }
}
