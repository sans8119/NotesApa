package com.pp.asn.model;

public class NoteData {
    private String title;
    private String text;
    private int hearted;//1 for true 0 for false
    private int starred;//1 for true 0 for false
    private int poem;//1 for true 0 for false
    private int story;//1 for true 0 for false
    private long time;
    private int id = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getHearted() {
        return hearted;
    }

    public void setHearted(int hearted) {
        this.hearted = hearted;
    }

    public int getStarred() {
        return starred;
    }

    public void setStarred(int starred) {
        this.starred = starred;
    }

    public int getPoem() {
        return poem;
    }

    public void setPoem(int poem) {
        this.poem = poem;
    }

    public int getStory() {
        return story;
    }

    public void setStory(int story) {
        this.story = story;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "NoteData{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", hearted=" + hearted +
                ", starred=" + starred +
                ", poem=" + poem +
                ", story=" + story +
                ", time=" + time +
                ", id=" + id +
                '}';
    }

}
