package com.example.jihun.dn;

import android.util.Log;

/**
 * Created by jihun on 2017-08-04.
 */

public class ReplyItem {
    private String nick;
    private String content;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ReplyItem(String nick, String content) {
        this.nick = nick;
        this.content = content;
        Log.d("ReplyItem","nick : "+nick+" content :"+content);
    }


    public ReplyItem() {
    }

}
