package com.example.d_plan;

public class Disaster_List {

    @com.google.gson.annotations.SerializedName("Disaster_name")
    private String dname;

    /**
     * Item Id
     */
    @com.google.gson.annotations.SerializedName("id")
    private String did;

    @com.google.gson.annotations.SerializedName("Disaster_place")
    private String dplace;

    /**
     * Indicates if the item is completed
     */
    @com.google.gson.annotations.SerializedName("complete")
    private boolean mComplete;

    /**
     * ToDoItem constructor
     */
    public Disaster_List() {

    }

    @Override
    public String toString() {
        return getText_name();
    }

    /**
     * Initializes a new ToDoItem
     *
     * @param name
     *            The item text
     * @param id
     *            The item id
     */
    public Disaster_List(String name,String place, String id) {
        this.setText(name,place);
        this.setId(id);
    }

    /**
     * Returns the item text
     */
    public String getText_name() {
        return dname;
    }

    public String getText_place(){
        return dplace;
    }
    /**
     * Sets the item text
     *
     * @param text
     *            text to set
     */
    public final void setText(String text,String place) {
        dname = text;
        dplace = place;
    }

    /**
     * Returns the item id
     */
    public String getId() {
        return did;
    }

    /**
     * Sets the item id
     *
     * @param id
     *            id to set
     */
    public final void setId(String id) {
        did = id;
    }

    /**
     * Indicates if the item is marked as completed
     */
    public boolean isComplete() {
        return mComplete;
    }

    /**
     * Marks the item as completed or incompleted
     */
    public void setComplete(boolean complete) {
        mComplete = complete;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Disaster_List && ((Disaster_List) o).did == did;
    }
}
