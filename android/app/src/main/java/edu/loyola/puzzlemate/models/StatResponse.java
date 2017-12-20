package edu.loyola.puzzlemate.models;

import com.google.gson.annotations.SerializedName;

/**
 *Show the stat response for the application
 *  Created by Syncrobits LLC on 11/19/17.
 */

public class StatResponse {
    /**
     * int completed for the application
     */
    @SerializedName("completed")
    private int completed;

    /**
     * int for showing the started of the puzzle
     */
    @SerializedName("started")
    private int started;

    /**
     * The constructor for the class
     * @param started the int representing the started
     * @param completed int representing the completed
     */
    public StatResponse(int started, int completed) {
        this.started = started;
        this.completed = completed;
    }

    /**
     * returns the value of getCompleted
     * @return the int value of completed
     */
    public int getCompleted() {
        return completed;
    }

    /**
     * sets the completed value for the class
     * @param completed the int representing the completed
     */
    public void setCompleted(int completed) {
        this.completed = completed;
    }

    /**
     * returns the getStarted value
     * @return the int value of started
     */
    public int getStarted() {
        return started;
    }

    /**
     * sets the started int value for the class
     * @param started int new started value
     */
    public void setStarted(int started) {
        this.started = started;
    }
}
