package com.example.leiwang.messagecardview.model;

/**
 * Created by lei.wang on 3/1/2017.
 */

public class NewsMessage {
    int imageID;
    String message_id;
    String channel;
    String contents;
    String title;
    String source_name;
    String pub_date;
    String imageLink;
    String contentsLink;
    int width;
    int height;
    int has_image;

    public String getContentsLink() {
        return contentsLink;
    }

    public void setContentsLink(String contentsLink) {
        this.contentsLink = contentsLink;
    }

    public NewsMessage(String message_id, String source_name, String title, String pub_date, String imageLink, String contentsLink, String contents, int has_image, int image_width, int image_height) {
        this.message_id = message_id;
        this.contents = contents;
        this.title = title;
        this.source_name = source_name;
        this.pub_date = pub_date;
        this.imageLink = imageLink;
        this.contentsLink = contentsLink;
        this.has_image = has_image;
        this.width = image_width;
        this.height = image_height;
    }

    public NewsMessage(String message_id, String source_name, String channel, String title,  String contentsLink, int has_image, String pub_date, String imageLink,  int width, int height) {
        this.message_id = message_id;
        this.channel = channel;
        this.title = title;
        this.contentsLink = contentsLink;
        this.source_name = source_name;
        this.pub_date = pub_date;
        this.imageLink = imageLink;
        this.width = width;
        this.height = height;
        this.has_image = has_image;
    }


    public NewsMessage(String message_id, String source_name, String channel, String title,  String contentsLink, String pub_date, String imageLink) {
        this.message_id = message_id;
        this.channel = channel;
        this.title = title;
        this.contentsLink = contentsLink;
        this.source_name = source_name;
        this.pub_date = pub_date;
        this.imageLink = imageLink;

    }

    public NewsMessage() {
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int isHas_image() {
        return has_image;
    }

    @Override
    public String toString() {
        return "NewsMessage{" +
                "imageID=" + imageID +
                ", message_id='" + message_id + '\'' +
                ", contents='" + contents + '\'' +
                ", title='" + title + '\'' +
                ", source_name='" + source_name + '\'' +
                ", pub_date='" + pub_date + '\'' +
                ", imageLink='" + imageLink + '\'' +
                ", contentsLink='" + contentsLink + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", has_image=" + has_image +
                '}';
    }

    public int getHas_image() {
        return has_image;
    }

    public void setHas_image(int has_image) {
        this.has_image = has_image;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
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

    public String getPub_date() {
        return pub_date;
    }

    public void setPub_date(String time) {
        this.pub_date = pub_date;
    }


}
