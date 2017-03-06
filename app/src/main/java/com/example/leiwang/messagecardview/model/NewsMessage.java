package com.example.leiwang.messagecardview.model;

/**
 * Created by lei.wang on 3/1/2017.
 */

public class NewsMessage {
    int imageID;
    String message_id;
    String contents;
    String title;
    String source_name;
    String time;
    String link;


    public NewsMessage(String message_id, String source_name, String title, String time, String link, String contents) {
        this.message_id = message_id;
        this.contents = contents;
        this.title = title;
        this.source_name = source_name;
        this.time = time;
        this.link = link;
    }

    public NewsMessage() {
    }

    @Override
    public String toString() {
        return "NewsMessage{" +
                "imageID=" + imageID +
                ", message_id='" + message_id + '\'' +
                ", contents='" + contents + '\'' +
                ", title='" + title + '\'' +
                ", source_name='" + source_name + '\'' +
                ", time='" + time + '\'' +
                ", link='" + link + '\'' +
                '}';
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource_name() {
        return source_name;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
