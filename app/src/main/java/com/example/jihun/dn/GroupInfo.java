package com.example.jihun.dn;

import java.util.ArrayList;

/**
 * Created by jihun on 2017-07-19.
 */

public class GroupInfo {
    String master;
    String groupName;
    String groupImage;
    ArrayList<String> memberNick;
    ArrayList<String> boards;

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    public ArrayList<String> getMemberNick() {
        return memberNick;
    }

    public void setMemberNick(ArrayList<String> memberNick) {
        this.memberNick = memberNick;
    }

    public ArrayList<String> getBoards() {
        return boards;
    }

    public void setBoards(ArrayList<String> boards) {
        this.boards = boards;
    }

    public GroupInfo() {
    this.memberNick=new ArrayList<>();
    this.boards =new ArrayList<>();
    }
}
