package com.example.d_plan;

public class Local_help {

    @com.google.gson.annotations.SerializedName("username")
    private String gname;

    @com.google.gson.annotations.SerializedName("user_email")
    private String gemail;

    @com.google.gson.annotations.SerializedName("id")
    private String gid;

    @com.google.gson.annotations.SerializedName("mob_num")
    private String gmob;

    @com.google.gson.annotations.SerializedName("latitude")
    private String glat;

    @com.google.gson.annotations.SerializedName("longitude")
    private String glong;

    @com.google.gson.annotations.SerializedName("Disaster_id")
    private String gdid;

    @com.google.gson.annotations.SerializedName("capacity_hold")
    private int max_hold;

    @com.google.gson.annotations.SerializedName("current_hold")
    private int curr_hold;

    @com.google.gson.annotations.SerializedName("place")
    private String gplace;

    @com.google.gson.annotations.SerializedName("complete")
    private boolean mComplete;

    @com.google.gson.annotations.SerializedName("Authorized")
    private boolean gauth;


    public Local_help() {

    }

    @Override
    public String toString() {
        return getText_name();
    }

    public Local_help(String name, String mob, String lat, String lng, String did,int maxhold,int currhold, String email,String place,Boolean auth,String id) {
        this.setText(name,mob,lat,lng,did,maxhold,currhold,email,place,auth);
        this.setId(id);
    }

    public String getText_name() {
        return gname;
    }

    public String getText_mob(){
        return gmob;
    }

    public String getText_lat() {
        return glat;
    }

    public String getText_long(){
        return glong;
    }

    public String getText_did(){
        return gdid;
    }

    public int getText_currentcapacity(){
        return curr_hold;
    }

    public int getText_maxcapacity(){
        return max_hold;
    }

    public String getPlace(){
        return gplace;
    }

    public boolean getAuth(){
        return gauth;
    }

    public final void setText(String name,String mob,String lat,String lng,String did,Integer maxcap,Integer currentcap,String email,String place,Boolean auth) {
        gname = name;
        gmob = mob;
        glat = lat;
        glong = lng;
        gdid = did;
        curr_hold = currentcap;
        max_hold = maxcap;
        gemail = email;
        gplace = place;
        gauth = auth;
    }

    public String getId() {
        return gid;
    }

    public final void setId(String id) {
        gid = id;
    }

    public boolean isComplete() {
        return mComplete;
    }

    public void setComplete(boolean complete) {
        mComplete = complete;
    }

    public void updatecurrent(Integer count){
        curr_hold = count;
    }

    public  void updatelatitude(String lati){
        glat = lati;
    }

    public void updatelongitude(String longi){
        glong = longi;
    }

    public void updatename(String name){
        gname = name;
    }

    public void updatenumber(String number){
        gmob = number;
    }

    public void updatemax(Integer max){
        max_hold = max;
    }

    public void updateplace(String place){
        gplace = place;
    }

    public void update_auth(Boolean auth){
        gauth = auth;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Local_help && ((Local_help) o).gid == gid;
    }
}
