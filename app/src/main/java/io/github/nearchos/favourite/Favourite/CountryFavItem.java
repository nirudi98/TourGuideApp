package io.github.nearchos.favourite.Favourite;

public class CountryFavItem {

    private String item_title;
    private String key_id;
    private String username;

    public CountryFavItem() {
    }

    public CountryFavItem(String item_title, String key_id,String username) {
        this.item_title = item_title;
        this.key_id = key_id;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getItem_title() {
        return item_title;
    }

    public void setItem_title(String item_title) {
        this.item_title = item_title;
    }

    public String getKey_id() {
        return key_id;
    }

    public void setKey_id(String key_id) {
        this.key_id = key_id;
    }


}
