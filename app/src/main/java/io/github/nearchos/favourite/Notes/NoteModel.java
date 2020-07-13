package io.github.nearchos.favourite.Notes;

public class NoteModel
{
    private long ID;
    private String title;
    private String content;
    private String date;
    private String time;
    private String username;

    private String country;

    public NoteModel()
    {

    }

    public NoteModel(long ID, String title, String content, String country,String data,String time,String username) {
        this.ID = ID;
        this.title = title;
        this.content = content;
        this.country = country;
        this.date = data;
        this.time = time;
        this.username = username;
    }

    public NoteModel(String title, String content, String country,String date,String time,String username) {
        this.title = title;
        this.content = content;
        this.country = country;
        this.date =  date;
        this.time = time;
        this.username = username;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
