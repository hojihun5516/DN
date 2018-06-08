package com.example.jihun.dn;

import android.net.Uri;

import java.util.Date;

/**
 * Created by jihun on 2017-07-05.
 */

public class GroupListItem {
    private String groupName;
    private String groupImage;

    public GroupListItem(String groupName, String groupImage) {
        this.groupName = groupName;
        this.groupImage = groupImage;
    }

    public GroupListItem() {
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
}