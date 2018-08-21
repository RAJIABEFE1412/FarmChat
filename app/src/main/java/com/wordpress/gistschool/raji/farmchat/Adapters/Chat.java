package com.wordpress.gistschool.raji.farmchat.Adapters;

public class Chat {
    private String name, mText, mImage, mTime,mUid;

    public Chat() {

    }

    public Chat(String name, String mText, String mImage, String mTime, String mUid) {


        this.name = name;
        this.mText = mText;
        this.mImage = mImage;
        this.mTime = mTime;
        this.mUid = mUid;
    }


    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getmUid() {
        return mUid;
    }

    public void setmUid(String mUid) {
        this.mUid = mUid;
    }
}
