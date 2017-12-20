package edu.loyola.puzzlemate.utilities;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import java.util.ArrayList;

import edu.loyola.puzzlemate.models.PuzzlePieces;

public class ImageSpliter {
    PuzzlePieces[][] boardToBeSet;
    ImageView image;


    public ImageSpliter(ImageView newImage, PuzzlePieces[][] newBoardToBeSet) {
        image = newImage;
        boardToBeSet = newBoardToBeSet;
    }

    public void splitImage(int chunkNumbers) {


        //For the number of rows and columns of the grid to be displayed
        int rows, cols;

        //For height and width of the small image chunks
        int chunkHeight, chunkWidth;

        //To store all the small image chunks in bitmap format in this list
        ArrayList<Bitmap> chunkedImages = new ArrayList<Bitmap>(chunkNumbers);

        //Getting the scaled bitmap of the source image
        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

        rows = cols = (int) Math.sqrt(chunkNumbers);
        chunkHeight = bitmap.getHeight() / rows;
        chunkWidth = bitmap.getWidth() / cols;

        //xCoord and yCoord are the pixel positions of the image chunks
        int yCoord = 0;
        for (int x = 0; x < rows; x++) {
            int xCoord = 0;
            for (int y = 0; y < cols; y++) {
                chunkedImages.add(Bitmap.createBitmap(scaledBitmap, xCoord, yCoord, chunkWidth, chunkHeight));
                xCoord += chunkWidth;
            }
            yCoord += chunkHeight;
        }

        boardToBeSet[0][0].setPieceImage(chunkedImages.get(0));
        boardToBeSet[0][1].setPieceImage(chunkedImages.get(1));
        boardToBeSet[0][2].setPieceImage(chunkedImages.get(2));
        boardToBeSet[0][3].setPieceImage(chunkedImages.get(3));
        boardToBeSet[1][0].setPieceImage(chunkedImages.get(4));
        boardToBeSet[1][1].setPieceImage(chunkedImages.get(5));
        boardToBeSet[1][2].setPieceImage(chunkedImages.get(6));
        boardToBeSet[1][3].setPieceImage(chunkedImages.get(7));
        boardToBeSet[2][0].setPieceImage(chunkedImages.get(8));
        boardToBeSet[2][1].setPieceImage(chunkedImages.get(9));
        boardToBeSet[2][2].setPieceImage(chunkedImages.get(10));
        boardToBeSet[2][3].setPieceImage(chunkedImages.get(11));
        boardToBeSet[3][0].setPieceImage(chunkedImages.get(12));
        boardToBeSet[3][1].setPieceImage(chunkedImages.get(13));
        boardToBeSet[3][2].setPieceImage(chunkedImages.get(14));
        boardToBeSet[3][3].setPieceImage(chunkedImages.get(15));


        /* Now the chunkedImages has all the small image chunks in the form of Bitmap class.
         * You can do what ever you want with this chunkedImages as per your requirement.
         * I pass it to a new Activity to show all small chunks in a grid for demo.
         * You can get the source code of this activity from my Google Drive Account.
         */


    }

    public PuzzlePieces[][] getBoardToBeSet() {
        return boardToBeSet;
    }

}