package edu.loyola.puzzlemate;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.loyola.puzzlemate.models.Puzzle;
import edu.loyola.puzzlemate.utilities.ProviderResponse;
import edu.loyola.puzzlemate.utilities.PuzzleProvider;

/**
 * An activity representing a list of Puzzles. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link PuzzleDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class SearchableActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    /**
     * Search query text view
     */
    private TextView mQueryView;


    /**
     * On create of the activity lifecycle
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("CURRENT_ACTIVITY", "SEARCHABLE");
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().setTitle("Search");
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_searchable);

        mQueryView = findViewById(R.id.query_view);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //toolbar.setTitle(getTitle());

        FloatingActionButton fab = findViewById(R.id.search_fab);
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

        final View recyclerView = findViewById(R.id.puzzle_list);
        assert recyclerView != null;

        final Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            setupRecyclerView((RecyclerView) recyclerView, query);
            mQueryView.setText(query);
        } else {
            setupRecyclerView((RecyclerView) recyclerView);
        }
        TextView.OnEditorActionListener l = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                setupRecyclerView((RecyclerView) recyclerView, textView.getText().toString());
                return true;
            }
        };
        mQueryView.setOnEditorActionListener(l);
    }

    /**
     * Setup menus
     * @param MenuItem item to be inflated
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                finish();
                return true;
            case R.id.app_bar_search:
                onSearchRequested();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    /**
     * Setup recyclerView originally with no query
     * @param recyclerView view to inflate
     */
    private void setupRecyclerView(@NonNull final RecyclerView recyclerView) {
        PuzzleProvider.fetchPuzzles(getBaseContext(), new ProviderResponse<List<Puzzle>>() {
            @Override
            public void success(List<Puzzle> items) {
                Log.d("Good", "To go");
                recyclerView.setAdapter(new SimplePuzzleRecyclerViewAdapter(
                        SearchableActivity.this, items, mTwoPane));
            }
        });
    }


    /**
     * Setup recycler view with new search
     * @param recyclerView view to inflate with list items
     * @param query query string to use for search
     */
    private void setupRecyclerView(@NonNull final RecyclerView recyclerView, String query) {
        PuzzleProvider.queryPuzzles(getBaseContext(), query, new ProviderResponse<List<Puzzle>>() {
            @Override
            public void success(List<Puzzle> items) {
                Log.d("Good", "To go");
                recyclerView.setAdapter(new SimplePuzzleRecyclerViewAdapter(
                        SearchableActivity.this, items, mTwoPane));
            }
        });
    }

    /**
     * Adapts our puzzle items to the search recycler view
     */
    public static class SimplePuzzleRecyclerViewAdapter
            extends RecyclerView.Adapter<SimplePuzzleRecyclerViewAdapter.ViewHolder> {

        /**
         * The activity we wish to search (parent)
         */
        private final SearchableActivity mParentActivity;

        /**
         * List of puzzles that will be shown
         */
        private final List<Puzzle> mValues;

        /**
         * Keep track if we are in two pane mode
         */
        private final boolean mTwoPane;

        /**
         * Intercept the click on the table view cell
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
                    arguments.putString(PuzzleDetailFragment.ARG_ITEM_IMAGE_URL, "" + item.getImage_url());
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
                    intent.putExtra(PuzzleDetailFragment.ARG_ITEM_DESCRIPTION, "" + item.getDescription());
                    intent.putExtra(PuzzleDetailFragment.ARG_ITEM_IMAGE_URL, "" + item.getImage_url());
                    context.startActivity(intent);
                }
            }
        };

        /**
         * Constructor
         * @param parent parent view
         * @param items items to display
         * @param twoPane two pane
         */
        SimplePuzzleRecyclerViewAdapter(SearchableActivity parent, List<Puzzle> items, boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        /**
         * onCreateViewHolder
         * @param parent parent view
         * @param viewType view type
         * @return viewholder representing the puzzle
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.puzzle_list_content, parent, false);
            return new ViewHolder(view);
        }

        /**
         * Bind views and set content of views
         * @param holder holder that we will inflate
         * @param position position in the index
         */
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText("" + mValues.get(position).getName());
            holder.mContentView.setText(mValues.get(position).getDescription());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        /**
         * Get the item count
         * @return the item count
         */
        @Override
        public int getItemCount() {
            return mValues.size();
        }

        /**
         * ViewHolder that will store our views
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
