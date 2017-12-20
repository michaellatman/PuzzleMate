/**
 * This handles the interaction between boards and pieces
 *
 * @author Michael Latman, Billy Quintano, Doug Robie
 */

package edu.loyola.puzzlemate.models;



import java.util.Random;


public class PuzzleBoard { //extends MainActivity {

    private PuzzlePieces[][] board;
    //private MainActivity myActivity;


    /**
     * Constructor of the puzzle board
     *
     * @param inheritedBoard this assign the board to the class
     */
    public PuzzleBoard(PuzzlePieces[][] inheritedBoard) {

        board = inheritedBoard;

    }


    /*
    // parameterized overloaded constructor
    PuzzleBoard(MainActivity newActivity, PuzzlePieces[][] newBoard){
        myActivity = newActivity;
        board=newBoard;
    }
    */

    /**
     * This method switches too pieces
     *
     * @param a this is the first puzzle piece to be swapped
     * @param b this is the second puzzle piece to be swapped
     */
    public void switchPieces(PuzzlePieces a, PuzzlePieces b) {
        int tempRowA = a.getCurrentRow();
        int tempColA = a.getCurrentCol();
        int tempRowB = b.getCurrentRow();
        int tempColB = b.getCurrentCol();

        a.setCurrentRow(tempRowB);
        a.setCurrentCol(tempColB);

        b.setCurrentRow(tempRowA);
        b.setCurrentCol(tempColA);


    }

    /**
     * Shuffles the board randomly
     */
    public void shuffleBoard() {
        Random randX = new Random();
        Random randY = new Random();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {


                int possibleX = randX.nextInt(board.length);
                int possibleY = randY.nextInt(board[i].length);
                /*
                System.out.print(possibleX);
				System.out.print(possibleY);
				System.out.println("");

				if(!isPieceTaken(possibleX,possibleY))
				{
					board[i][j].setCurrentRow(possibleX);
					board[i][j].setCurrentCol(possibleY);
				}
				*/
                while (isPieceTaken(possibleX, possibleY)) {
                    possibleX = randX.nextInt(board.length);
                    possibleY = randY.nextInt(board[i].length);
                }

                board[i][j].setCurrentRow(possibleX);
                board[i][j].setCurrentCol(possibleY);
                //System.out.println("Puzzle piece "+board[i][j].getID()+" now is in row "+ board[i][j].getCurrentRow()+ "and at column "+board[i][j].getCurrentCol());;


            }
        }
    }

    /**
     * Checks to see if the piece is taken in the current board
     *
     * @param tempX the row location of the piece to be checked
     * @param tempY the coulmn location of the piece to be checked
     * @return boolean true if the piece is taken false if the piece is not taken
     */
    public boolean isPieceTaken(int tempX, int tempY) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].getCurrentRow() == tempX && board[i][j].getCurrentCol() == tempY) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Gets a piece from the puzzle
     *
     * @param tempX the row of the piece that you want
     * @param tempY the column of the piece that you want
     * @return the puzzle piece
     */
    public PuzzlePieces getThisPiece(int tempX, int tempY) {

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].getCurrentRow() == tempX && board[i][j].getCurrentCol() == tempY) {
                    return board[i][j];
                }
            }
        }
        return null;

    }


    /**
     * Checks to see if there is a winning board
     *
     * @return true if there is a winning board false if there is not
     */
    public boolean isWinner() {
        int totalPieces = 0;
        int piecesInRightLocation = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                totalPieces++;
            }
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j].getWinningRow() == board[i][j].getCurrentRow() && board[i][j].getWinningCol() == board[i][j].getCurrentCol()) {
                    piecesInRightLocation++;
                }
            }
        }

        return totalPieces == piecesInRightLocation;

    }

    /**
     * this makes the board into a winning soultion to check to see if the is winner method works
     */
    public void makeWinner() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                board[i][j].setCurrentRow(board[i][j].getWinningRow());
                board[i][j].setCurrentCol(board[i][j].getWinningCol());
            }
        }
    }


}

