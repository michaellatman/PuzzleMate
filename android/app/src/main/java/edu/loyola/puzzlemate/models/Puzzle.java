package edu.loyola.puzzlemate.models;

import com.google.gson.annotations.SerializedName;

/**
 * This is the Puzzle object class
 *  @author Michael Latman, Billy Quintano, Doug Robie
 */

public class Puzzle {
    /**
     * The ID of the puzzle
     */
    @SerializedName("id")
    private int id;
    /**
     * The name of the puzzle
     */
    @SerializedName("name")
    private String name;
    /**
     * The Description of the puzzle
     */
    @SerializedName("description")
    private String description;

    /**
     * The User ID of the puzzle
     */
    @SerializedName("user_id")
    private int user_id;

    /**
     * The image URL for the puzzle
     */
    @SerializedName("image_url")
    private String image_url;

    /**
     * The constructor for the puzzle
     * @param id the id of the puzzle
     * @param name name of the puzzle
     * @param description description of the puzzle
     * @param user_id user id of who created the puzzle
     * @param image_url the image of the puzzle
     */
    public Puzzle(int id, String name, String description, int user_id, String image_url) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.user_id = user_id;
        this.image_url = image_url;
    }

    /**
     * Gets The ID of the Puzzle
     * @return the ID of the puzzle
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of the puzzle
     * @return name the name of the puzzle
     */
    public String getName() {
        return name;
    }

    /**
     * gets the description of the puzzle
     * @return description of the puzzle
     */
    public String getDescription() {
        return description;
    }

    /**
     * gets the user id of the puzzle
     * @return the puzzle id
     */
    public int getUser_id() {
        return user_id;
    }

    /**
     * returns the image url
     * @return the image url
     */
    public String getImage_url() {
        return image_url;
    }
}
