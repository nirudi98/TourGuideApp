package io.github.nearchos.favourite.Country;

import java.util.Comparator;

public class ModelCountryList
{
    private String username_country;
    private byte[] img;
    private String key_id;
    private String favStatus;
    private String username;


    public ModelCountryList()
    {

    }

    public ModelCountryList(String username_country, byte[] img,String favStatus,String key_id,String username)
    {
        this.username_country = username_country;
        this.img = img;
        this.favStatus = favStatus;
        this.key_id = key_id;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername_country()
    {
        return username_country;
    }

    public void setUsername_country(String username_country)
    {
        this.username_country = username_country;
    }

    public byte[] getImg()
    {
        return img;
    }

    public void setImg(byte[] img)
    {
        this.img = img;
    }

    public String getFavStatus() {
        return favStatus;
    }

    public void setFavStatus(String favStatus) {
        this.favStatus = favStatus;
    }

    public String getKey_id() {
        return key_id;
    }

    public void setKey_id(String key_id) {
        this.key_id = key_id;
    }


    public static final Comparator<ModelCountryList> BY_USERNAME_ASCENDING = new Comparator<ModelCountryList>() {
        @Override
        public int compare(ModelCountryList o1, ModelCountryList o2) {
            return o1.getUsername_country().compareTo(o2.getUsername_country());
        }
    };


    public static final Comparator<ModelCountryList> BY_USERNAME_DESCENDING = new Comparator<ModelCountryList>() {
        @Override
        public int compare(ModelCountryList o1, ModelCountryList o2) {
            return o2.getUsername_country().compareTo(o1.getUsername_country());
        }
    };
}
