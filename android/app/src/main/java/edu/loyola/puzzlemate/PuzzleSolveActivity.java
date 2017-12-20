/**
 * This is the driver of the puzzle logic this handles the views and the handlers
 *
 * @author Michael Latman, Billy Quintano, Doug Robie
 */

package edu.loyola.puzzlemate;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;

import edu.loyola.puzzlemate.models.PuzzleBoard;
import edu.loyola.puzzlemate.models.PuzzlePieces;
import edu.loyola.puzzlemate.utilities.ImageSpliter;

public class PuzzleSolveActivity extends AppCompatActivity {

    ImageView button1, button2, button3, button4, button5, button6, button7, button8, button9, button10, button11, button12, button13, button14, button15, button16;
    ImageView puzzleImage;

    PuzzleBoard theBoard;

    PuzzlePieces swapablePiece1 = null;
    PuzzlePieces swapablePiece2 = null;

    private Toast mToast = null;

    /**
     * Overides the generic on create, builds the starting screen
     *
     * @param savedInstanceState inherits the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_screen);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        button1 = findViewById(R.id.imageButton);
        button2 = findViewById(R.id.imageButton2);
        button3 = findViewById(R.id.imageButton3);
        button4 = findViewById(R.id.imageButton4);
        button5 = findViewById(R.id.imageButton5);
        button6 = findViewById(R.id.imageButton6);
        button7 = findViewById(R.id.imageButton7);
        button8 = findViewById(R.id.imageButton8);
        button9 = findViewById(R.id.imageButton9);
        button10 = findViewById(R.id.imageButton10);
        button11 = findViewById(R.id.imageButton11);
        button12 = findViewById(R.id.imageButton12);
        button13 = findViewById(R.id.imageButton13);
        button14 = findViewById(R.id.imageButton14);
        button15 = findViewById(R.id.imageButton15);
        button16 = findViewById(R.id.imageButton16);


        /*
        button1.setVisibility(View.INVISIBLE);

        /*
        button2.setVisibility(View.INVISIBLE);
        button3.setVisibility(View.INVISIBLE);
        button4.setVisibility(View.INVISIBLE);
        button5.setVisibility(View.INVISIBLE);
        button6.setVisibility(View.INVISIBLE);
        button7.setVisibility(View.INVISIBLE);
        button8.setVisibility(View.INVISIBLE);
        button9.setVisibility(View.INVISIBLE);
        button10.setVisibility(View.INVISIBLE);
        button11.setVisibility(View.INVISIBLE);
        button12.setVisibility(View.INVISIBLE);
        button13.setVisibility(View.INVISIBLE);
        button14.setVisibility(View.INVISIBLE);
        button15.setVisibility(View.INVISIBLE);

        button16.setVisibility(View.INVISIBLE);
        */


        puzzleImage = findViewById(R.id.imageView);


        // button1.setVisibility(View.INVISIBLE);


        AsyncHttpClient client = new AsyncHttpClient();
        client.get(getIntent().getStringExtra(PuzzleDetailFragment.ARG_ITEM_IMAGE_URL), new FileAsyncHttpResponseHandler(/* Context */ this) {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, File file) {

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, File file) {
                puzzleImage.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));

                PuzzlePieces[][] thePuzzle = new PuzzlePieces[4][4];
                for (int i = 0; i < thePuzzle.length; i++) {
                    for (int j = 0; j < thePuzzle.length; j++) {
                        thePuzzle[i][j] = new PuzzlePieces(null, i, j);

                    }
                }


                ImageSpliter split = new ImageSpliter(puzzleImage, thePuzzle);
                split.splitImage(16);
                thePuzzle = split.getBoardToBeSet();
                theBoard = new PuzzleBoard(thePuzzle);
                if (thePuzzle[0][0].getPieceImage() == null) {
                    button1.setVisibility(View.INVISIBLE);
                }

                dumpWinningBoard(thePuzzle);
            }


        });


        //PuzzleBoard theBoard = new PuzzleBoard (this, thePuzzle);


        //theBoard.shuffleBoard();
        //dumpCurrentBoard(theBoard);

    }

    /**
     * Changes the image views to the pieces in a solved manner
     * @param board is a two dimensional array of puzzle pieces
     */
    public void dumpWinningBoard(PuzzlePieces[][] board) {
        button1.setImageBitmap(board[0][0].getPieceImage());
        button2.setImageBitmap(board[0][1].getPieceImage());
        button3.setImageBitmap(board[0][2].getPieceImage());
        button4.setImageBitmap(board[0][3].getPieceImage());
        button5.setImageBitmap(board[1][0].getPieceImage());
        button6.setImageBitmap(board[1][1].getPieceImage());
        button7.setImageBitmap(board[1][2].getPieceImage());
        button8.setImageBitmap(board[1][3].getPieceImage());
        button9.setImageBitmap(board[2][0].getPieceImage());
        button10.setImageBitmap(board[2][1].getPieceImage());
        button11.setImageBitmap(board[2][2].getPieceImage());
        button12.setImageBitmap(board[2][3].getPieceImage());
        button13.setImageBitmap(board[3][0].getPieceImage());
        button14.setImageBitmap(board[3][1].getPieceImage());
        button15.setImageBitmap(board[3][2].getPieceImage());
        button16.setImageBitmap(board[3][3].getPieceImage());
    }

    /**
     * Changes the image views on the button to the current board of pieces
     * @param board a two dimensional array of puzzle pieces
     */
    public void dumpCurrentBoard(PuzzleBoard board) {
        button1.setImageBitmap((board.getThisPiece(0, 0)).getPieceImage());
        button2.setImageBitmap((board.getThisPiece(0, 1)).getPieceImage());
        button3.setImageBitmap((board.getThisPiece(0, 2)).getPieceImage());
        button4.setImageBitmap((board.getThisPiece(0, 3)).getPieceImage());
        button5.setImageBitmap((board.getThisPiece(1, 0)).getPieceImage());
        button6.setImageBitmap((board.getThisPiece(1, 1)).getPieceImage());
        button7.setImageBitmap((board.getThisPiece(1, 2)).getPieceImage());
        button8.setImageBitmap((board.getThisPiece(1, 3)).getPieceImage());
        button9.setImageBitmap((board.getThisPiece(2, 0)).getPieceImage());
        button10.setImageBitmap((board.getThisPiece(2, 1)).getPieceImage());
        button11.setImageBitmap((board.getThisPiece(2, 2)).getPieceImage());
        button12.setImageBitmap((board.getThisPiece(2, 3)).getPieceImage());
        button13.setImageBitmap((board.getThisPiece(3, 0)).getPieceImage());
        button14.setImageBitmap((board.getThisPiece(3, 1)).getPieceImage());
        button15.setImageBitmap((board.getThisPiece(3, 2)).getPieceImage());
        button16.setImageBitmap((board.getThisPiece(3, 3)).getPieceImage());
    }

    /**
     * Handles the shuffle button when clicked the shuffle method from the board will be activated
     * @param view the current view of the screen
     */
    public void shuffleButtonClickHandler(View view) {
        //Log.d("Teststarting",getIntent().getStringExtra(PuzzleDetailFragment.ARG_ITEM_ID));
        PuzzleRestClient.startPuzzle(PuzzleSolveActivity.this, getIntent().getStringExtra(PuzzleDetailFragment.ARG_ITEM_ID));
        theBoard.shuffleBoard();
        dumpCurrentBoard(theBoard);
    }

    /**
     * Handeles the swap if the top left image is clicked
     * @param view the view of the screen
     */
    public void pieceOneClickHandler(View view) {
        if (swapablePiece1 == null) {

            swapablePiece1 = theBoard.getThisPiece(0, 0);

        } else if (swapablePiece2 == null) {

            swapablePiece2 = theBoard.getThisPiece(0, 0);
            theBoard.switchPieces(swapablePiece1, swapablePiece2);
            dumpCurrentBoard(theBoard);
            swapablePiece1 = null;
            swapablePiece2 = null;

            if (theBoard.isWinner()) {
                makeWinningToast();
            }

        }
    }

    /**
     * Handeles the swap if the top left center image is clicked
     * @param view the view of the screen
     */
    public void pieceTwoClickHandler(View view) {
        if (swapablePiece1 == null) {

            swapablePiece1 = theBoard.getThisPiece(0, 1);

        } else if (swapablePiece2 == null) {

            swapablePiece2 = theBoard.getThisPiece(0, 1);
            theBoard.switchPieces(swapablePiece1, swapablePiece2);
            dumpCurrentBoard(theBoard);
            swapablePiece1 = null;
            swapablePiece2 = null;

            if (theBoard.isWinner()) {
                makeWinningToast();
            }

        }
    }

    /**
     * Handeles the swap if the top right center image is clicked
     * @param view the view of the screen
     */
    public void pieceThreeClickHandler(View view) {
        if (swapablePiece1 == null) {

            swapablePiece1 = theBoard.getThisPiece(0, 2);

        } else if (swapablePiece2 == null) {

            swapablePiece2 = theBoard.getThisPiece(0, 2);
            theBoard.switchPieces(swapablePiece1, swapablePiece2);
            dumpCurrentBoard(theBoard);
            swapablePiece1 = null;
            swapablePiece2 = null;

            if (theBoard.isWinner()) {
                makeWinningToast();
            }

        }
    }

    /**
     * Handeles the swap if the top right image is clicked
     * @param view the view of the screen
     */
    public void pieceFourClickHandler(View view) {
        if (swapablePiece1 == null) {

            swapablePiece1 = theBoard.getThisPiece(0, 3);

        } else if (swapablePiece2 == null) {

            swapablePiece2 = theBoard.getThisPiece(0, 3);
            theBoard.switchPieces(swapablePiece1, swapablePiece2);
            dumpCurrentBoard(theBoard);
            swapablePiece1 = null;
            swapablePiece2 = null;

            if (theBoard.isWinner()) {
                makeWinningToast();
            }

        }
    }

    /**
     * Handeles the swap if the second row left image is clicked
     * @param view the view of the screen
     */
    public void pieceFiveClickHandler(View view) {
        if (swapablePiece1 == null) {

            swapablePiece1 = theBoard.getThisPiece(1, 0);

        } else if (swapablePiece2 == null) {

            swapablePiece2 = theBoard.getThisPiece(1, 0);
            theBoard.switchPieces(swapablePiece1, swapablePiece2);
            dumpCurrentBoard(theBoard);
            swapablePiece1 = null;
            swapablePiece2 = null;

            if (theBoard.isWinner()) {
                makeWinningToast();
            }

        }
    }

    /**
     * Handeles the swap if the second row left center image is clicked
     * @param view the view of the screen
     */
    public void pieceSixClickHandler(View view) {
        if (swapablePiece1 == null) {

            swapablePiece1 = theBoard.getThisPiece(1, 1);

        } else if (swapablePiece2 == null) {

            swapablePiece2 = theBoard.getThisPiece(1, 1);
            theBoard.switchPieces(swapablePiece1, swapablePiece2);
            dumpCurrentBoard(theBoard);
            swapablePiece1 = null;
            swapablePiece2 = null;

            if (theBoard.isWinner()) {
                makeWinningToast();
            }

        }
    }

    /**
     * Handeles the swap if the second row right center image is clicked
     * @param view the view of the screen
     */
    public void pieceSevenClickHandler(View view) {
        if (swapablePiece1 == null) {

            swapablePiece1 = theBoard.getThisPiece(1, 2);

        } else if (swapablePiece2 == null) {

            swapablePiece2 = theBoard.getThisPiece(1, 2);
            theBoard.switchPieces(swapablePiece1, swapablePiece2);
            dumpCurrentBoard(theBoard);
            swapablePiece1 = null;
            swapablePiece2 = null;

            if (theBoard.isWinner()) {
                makeWinningToast();
            }

        }
    }

    /**
     * Handeles the swap if the second row right image is clicked
     * @param view the view of the screen
     */
    public void pieceEightClickHandler(View view) {
        if (swapablePiece1 == null) {

            swapablePiece1 = theBoard.getThisPiece(1, 3);

        } else if (swapablePiece2 == null) {

            swapablePiece2 = theBoard.getThisPiece(1, 3);
            theBoard.switchPieces(swapablePiece1, swapablePiece2);
            dumpCurrentBoard(theBoard);
            swapablePiece1 = null;
            swapablePiece2 = null;

            if (theBoard.isWinner()) {
                makeWinningToast();
            }

        }
    }

    /**
     * Handeles the swap if the third row left image is clicked
     * @param view the view of the screen
     */
    public void pieceNineClickHandler(View view) {
        if (swapablePiece1 == null) {

            swapablePiece1 = theBoard.getThisPiece(2, 0);

        } else if (swapablePiece2 == null) {

            swapablePiece2 = theBoard.getThisPiece(2, 0);
            theBoard.switchPieces(swapablePiece1, swapablePiece2);
            dumpCurrentBoard(theBoard);
            swapablePiece1 = null;
            swapablePiece2 = null;

            if (theBoard.isWinner()) {
                makeWinningToast();
            }

        }
    }

    /**
     * Handeles the swap if the third row left center image is clicked
     * @param view the view of the screen
     */
    public void pieceTenClickHandler(View view) {
        if (swapablePiece1 == null) {

            swapablePiece1 = theBoard.getThisPiece(2, 1);

        } else if (swapablePiece2 == null) {

            swapablePiece2 = theBoard.getThisPiece(2, 1);
            theBoard.switchPieces(swapablePiece1, swapablePiece2);
            dumpCurrentBoard(theBoard);
            swapablePiece1 = null;
            swapablePiece2 = null;

            if (theBoard.isWinner()) {
                makeWinningToast();
            }

        }
    }

    /**
     * Handeles the swap if the third row right center image is clicked
     * @param view the view of the screen
     */
    public void pieceElevenClickHandler(View view) {
        if (swapablePiece1 == null) {

            swapablePiece1 = theBoard.getThisPiece(2, 2);

        } else if (swapablePiece2 == null) {

            swapablePiece2 = theBoard.getThisPiece(2, 2);
            theBoard.switchPieces(swapablePiece1, swapablePiece2);
            dumpCurrentBoard(theBoard);
            swapablePiece1 = null;
            swapablePiece2 = null;

            if (theBoard.isWinner()) {
                makeWinningToast();
            }

        }
    }

    /**
     * Handeles the swap if the third row right image is clicked
     * @param view the view of the screen
     */
    public void pieceTweleveClickHandler(View view) {
        if (swapablePiece1 == null) {

            swapablePiece1 = theBoard.getThisPiece(2, 3);

        } else if (swapablePiece2 == null) {

            swapablePiece2 = theBoard.getThisPiece(2, 3);
            theBoard.switchPieces(swapablePiece1, swapablePiece2);
            dumpCurrentBoard(theBoard);
            swapablePiece1 = null;
            swapablePiece2 = null;

            if (theBoard.isWinner()) {
                makeWinningToast();
            }

        }
    }

    /**
     * Handeles the swap if the fourth row left image is clicked
     * @param view the view of the screen
     */
    public void pieceThirteenClickHandler(View view) {
        if (swapablePiece1 == null) {

            swapablePiece1 = theBoard.getThisPiece(3, 0);

        } else if (swapablePiece2 == null) {

            swapablePiece2 = theBoard.getThisPiece(3, 0);
            theBoard.switchPieces(swapablePiece1, swapablePiece2);
            dumpCurrentBoard(theBoard);
            swapablePiece1 = null;
            swapablePiece2 = null;

            if (theBoard.isWinner()) {
                makeWinningToast();
            }

        }
    }

    /**
     * Handeles the swap if the fourth row left center image is clicked
     * @param view the view of the screen
     */
    public void pieceFourteenClickHandler(View view) {
        if (swapablePiece1 == null) {

            swapablePiece1 = theBoard.getThisPiece(3, 1);

        } else if (swapablePiece2 == null) {

            swapablePiece2 = theBoard.getThisPiece(3, 1);
            theBoard.switchPieces(swapablePiece1, swapablePiece2);
            dumpCurrentBoard(theBoard);
            swapablePiece1 = null;
            swapablePiece2 = null;

            if (theBoard.isWinner()) {
                makeWinningToast();
            }

        }
    }

    /**
     * Handeles the swap if the fourth row right center image is clicked
     * @param view the view of the screen
     */
    public void pieceFifteenClickHandler(View view) {
        if (swapablePiece1 == null) {

            swapablePiece1 = theBoard.getThisPiece(3, 2);

        } else if (swapablePiece2 == null) {

            swapablePiece2 = theBoard.getThisPiece(3, 2);
            theBoard.switchPieces(swapablePiece1, swapablePiece2);
            dumpCurrentBoard(theBoard);
            swapablePiece1 = null;
            swapablePiece2 = null;

            if (theBoard.isWinner()) {
                makeWinningToast();
            }

        }
    }

    /**
     * Handeles the swap if the fourth row right image is clicked
     * @param view the view of the screen
     */
    public void pieceSixteenClickHandler(View view) {
        if (swapablePiece1 == null) {

            swapablePiece1 = theBoard.getThisPiece(3, 3);

        } else if (swapablePiece2 == null) {

            swapablePiece2 = theBoard.getThisPiece(3, 3);
            theBoard.switchPieces(swapablePiece1, swapablePiece2);
            dumpCurrentBoard(theBoard);
            swapablePiece1 = null;
            swapablePiece2 = null;

            if (theBoard.isWinner()) {
                makeWinningToast();
            }

        }
    }

    /**
     * Handles the toast if a winning situcation is reached
     */
    public void makeWinningToast() {
        // if(mToast!=null) mToast.cancel();
        PuzzleRestClient.completePuzzle(PuzzleSolveActivity.this, getIntent().getStringExtra(PuzzleDetailFragment.ARG_ITEM_ID));
        mToast = Toast.makeText(getApplicationContext(), "Congrats you completed the puzzle!!", Toast.LENGTH_LONG);
        mToast.show();
    }


    /**
     * Item selected
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, PuzzleListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
