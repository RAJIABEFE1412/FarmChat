package com.wordpress.gistschool.raji.farmchat.Adapters;

public class blogHome {
    private String posterName,datePosted,DocPosted,posterImg,postDescription,mPostKey;

    public blogHome(){

    }
    public blogHome(String posterName, String datePosted,
                    String docPosted, String postedImg,
                    String postDescription, String mPostKey){

        this.posterName = posterName;
        this.datePosted = datePosted;
        DocPosted = docPosted;
        this.posterImg = postedImg;
        this.postDescription = postDescription;
        this.mPostKey = mPostKey;
    }

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getDocPosted() {
        return DocPosted;
    }

    public void setDocPosted(String docPosted) {
        DocPosted = docPosted;
    }

    public String getPosterImg() {
        return posterImg;
    }

    public void setPosterImg(String posterImg) {
        this.posterImg = posterImg;
    }

    public String getmPostKey() {
        return mPostKey;
    }

    public void setmPostKey(String mPostKey) {
        this.mPostKey = mPostKey;
    }
}
