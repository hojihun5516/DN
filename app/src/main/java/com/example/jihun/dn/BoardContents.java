package com.example.jihun.dn;

import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jihun on 2017-07-19.
 */

public class BoardContents {
    String nick;
    Date date;
    String title;
    String content;
    String image;
    String reply;

    public BoardContents() {

    }

    public BoardContents(String nick, Date date, String title, String content, String image) {
        this.nick = nick;
        this.date = date;
        this.title = title;
        this.content = content;
        this.image = image;
        this.reply=null;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
