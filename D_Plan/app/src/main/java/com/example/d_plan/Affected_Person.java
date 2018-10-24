package com.example.d_plan;

public class Affected_Person {

    @com.google.gson.annotations.SerializedName("Name")
    private String pname;

    @com.google.gson.annotations.SerializedName("id")
    private String pid;

    @com.google.gson.annotations.SerializedName("place")
    private String pplace;

    @com.google.gson.annotations.SerializedName("Mobile_number")
    private String pmob;

    @com.google.gson.annotations.SerializedName("latitude")
    private String plat;

    @com.google.gson.annotations.SerializedName("longitude")
    private String plong;

    @com.google.gson.annotations.SerializedName("Disaster_id")
    private String pdid;

    @com.google.gson.annotations.SerializedName("complete")
    private boolean mComplete;

    public Affected_Person() {

    }

    @Override
    public String toString() {
        return getText_name();
    }

    public Affected_Person(String name, String mob, String lat, String lng, String did,String place, String id) {
        this.setText(name,mob,lat,lng,did,place);
        this.setId(id);
    }

    public String getText_name() {
        return pname;
    }

    public String getText_mob(){
        return pmob;
    }

    public String getText_lat() {
        return plat;
    }

    public String getText_long(){
        return plong;
    }

    public String getText_did(){
        return pdid;
    }

    public String getPlace(){
        return pplace;
    }

    public final void setText(String name,String mob,String lat,String lng,String did,String place) {
        pname = name;
        pmob = mob;
        plat = lat;
        plong = lng;
        pdid = did;
        pplace = place;
    }

    public String getId() {
        return pid;
    }

    public final void setId(String id) {
        pid = id;
    }

    public boolean isComplete() {
        return mComplete;
    }

    public void setComplete(boolean complete) {
        mComplete = complete;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Affected_Person && ((Affected_Person) o).pid == pid;
    }
}

