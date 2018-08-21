package com.wordpress.gistschool.raji.farmchat.Adapters;

public class comments {
    private String mName, mText, mImage;

    public comments() {

    }

    public comments(String name, String mText, String mImage) {


        this.mName = name;
        this.mText = mText;
        this.mImage = mImage;

    }



    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String name) {
        this.mName = name;
    }

}
