package com.example.jihun.dn;

import java.util.Date;

/**
 * Created by jihun on 2017-07-05.
 */

public class PictureListItem {
    private String nick;
    private String pictures;
    private String title;
    private String content;
    private Date write_date;
    private String groupName;
    private String reply;
    private int index;
    public PictureListItem(){}

    public PictureListItem(String nick, String pictures, String title, String content, Date write_date,String groupName,int index) {
        this.nick = nick;
        this.pictures = pictures;
        this.title = title;
        this.content = content;
        this.write_date = write_date;
        this.groupName=groupName;
        this.reply =null;
        this.index=index;
    }


    public String getGroupName() {
        return groupName;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
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

    public Date getWrite_date() {
        return write_date;
    }

    public void setWrite_date(Date write_date) {
        this.write_date = write_date;
    }
}
