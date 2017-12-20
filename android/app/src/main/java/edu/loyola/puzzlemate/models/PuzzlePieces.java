/**
 * Puzzle piece class, with constructor, getters and setters
 *
 * @author Michael Latman, Billy Quintano, Doug Robie
 */
package edu.loyola.puzzlemate.models;

/**
 * Created by dougrobie on 12/10/17.
 */

import android.graphics.Bitmap;


public class PuzzlePieces {
    private int ID;
    private Bitmap pieceImage;
    private int winningRow;
    private int winningCol;
    private int currentRow;
    private int currentCol;

    /**
     * Constructor of the puzzle piece class, current row and col will be set to -1 to show they havent been set yet
     *
     * @param newImage      to initiate the bitmap image of each piece
     * @param newWinningRow which row in the puzzle the piece belongs
     * @param newWinningCol which col in the puzzle teh piece belongs
     */
    public PuzzlePieces(Bitmap newImage, int newWinningRow, int newWinningCol) {
        pieceImage = newImage;
        winningRow = newWinningRow;
        winningCol = newWinningCol;
        currentRow = -1;
        currentCol = -1;

    }

    /**
     * not used
     *
     * @return ID thats not used
     */
    public int getID() {
        return ID;
    }

    /**
     * not used
     *
     * @param newID not used
     */
    public void setID(int newID) {
        ID = newID;
    }

    /**
     * Returns the image of each piece
     *
     * @return pieceImage the image of each piece
     */
    public Bitmap getPieceImage() {
        return pieceImage;
    }

    /**
     * Sets the image of the piece
     *
     * @param newImageView the new image to be used with the piece
     */
    public void setPieceImage(Bitmap newImageView) {
        pieceImage = newImageView;
    }

    /**
     * Gets the winning row of the piece
     *
     * @return winningRow the winning row of the piece
     */
    public int getWinningRow() {
        return winningRow;
    }

    /**
     * Sets the new winning row of the piece
     *
     * @param newWinningRow the indicator of the new winning row
     */
    public void setWinningRow(int newWinningRow) {
        winningRow = newWinningRow;
    }

    /**
     * Gets the winning column of the piece
     *
     * @return winningCol the winning column of the pice
     */
    public int getWinningCol() {
        return winningCol;
    }

    /**
     * Sets the new winning column of the piece
     *
     * @param newWinningCol the new winning column of the piece
     */
    public void setWinningCol(int newWinningCol) {
        winningCol = newWinningCol;
    }

    /**
     * gets the current row of the piece
     *
     * @return currentRow the current row of the piece
     */
    public int getCurrentRow() {
        return currentRow;
    }

    /**
     * Sets the current row of the piece in the puzzle
     *
     * @param newCurrentRow the new row of the piece
     */
    public void setCurrentRow(int newCurrentRow) {
        currentRow = newCurrentRow;
    }

    /**
     * Gets the current column of the piece
     *
     * @return currentCol the current column of the piece
     */
    public int getCurrentCol() {
        return currentCol;
    }

    /**
     * Sets the current column of the piece in the puzzle
     *
     * @param newCurrentCol the current column in the piece
     */
    public void setCurrentCol(int newCurrentCol) {
        currentCol = newCurrentCol;
    }


}

