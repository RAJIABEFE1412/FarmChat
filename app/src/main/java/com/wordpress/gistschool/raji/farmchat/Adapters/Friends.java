package com.wordpress.gistschool.raji.farmchat.Adapters;

public class Friends
{
    private String name,image,profession,uId;
        public Friends(){

        }

        public Friends(String name, String image, String profession, String uId){


            this.name = name;
            this.image = image;
            this.profession = profession;
            this.uId = uId;
        }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }
}
