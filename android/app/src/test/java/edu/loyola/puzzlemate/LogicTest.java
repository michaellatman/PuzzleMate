package edu.loyola.puzzlemate;

import org.junit.Test;
import java.util.regex.Pattern;

import edu.loyola.puzzlemate.models.PuzzleBoard;
import edu.loyola.puzzlemate.models.PuzzlePieces;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.*;
/**
 * Created by dougrobie on 12/17/17.
 */

public class LogicTest {
    @Test
    public void testConstructor() {
        PuzzlePieces[][]p=new PuzzlePieces[3][3];
        PuzzleBoard b=new PuzzleBoard(p);
        assertNotNull(b);
    }

    @Test
    public void testSwapPiece(){
        PuzzlePieces[][] thePuzzle=new PuzzlePieces[4][4];
        for(int i=0;i<thePuzzle.length;i++)
        {
            for(int j=0;j<thePuzzle.length;j++)
            {
                thePuzzle[i][j]=new PuzzlePieces(null,i,j);

            }
        }

        PuzzleBoard board=new PuzzleBoard(thePuzzle);
        board.shuffleBoard();
        board.switchPieces(board.getThisPiece(0, 0), board.getThisPiece(2, 2));

        assertNotNull(board);
    }

    @Test
    public void testIsWinnerFalse(){
        PuzzlePieces[][] thePuzzle=new PuzzlePieces[4][4];
        for(int i=0;i<thePuzzle.length;i++)
        {
            for(int j=0;j<thePuzzle.length;j++)
            {
                thePuzzle[i][j]=new PuzzlePieces(null,i,j);

            }
        }

        PuzzleBoard board=new PuzzleBoard(thePuzzle);
        board.shuffleBoard();
        board.switchPieces(board.getThisPiece(0, 0), board.getThisPiece(2, 2));
        boolean answer=board.isWinner();
        assertFalse(answer);
        assertNotNull(board);
    }

    @Test
    public void testIsWinnerTrue(){
        PuzzlePieces[][] thePuzzle=new PuzzlePieces[4][4];
        for(int i=0;i<thePuzzle.length;i++)
        {
            for(int j=0;j<thePuzzle.length;j++)
            {
                thePuzzle[i][j]=new PuzzlePieces(null,i,j);

            }
        }

        PuzzleBoard board=new PuzzleBoard(thePuzzle);
        board.shuffleBoard();
        board.switchPieces(board.getThisPiece(0, 0), board.getThisPiece(2, 2));
        board.makeWinner();
        boolean answer=board.isWinner();
        assertTrue(answer);
        assertNotNull(board);
    }

}
