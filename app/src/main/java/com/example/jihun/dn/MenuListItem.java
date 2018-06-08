package com.example.jihun.dn;

/**
 * Created by jihun on 2017-07-02.
 */

public class MenuListItem {
    int images;
    String groupName;

    public MenuListItem(int images, String groupName) {
        this.images = images;
        this.groupName = groupName;
    }

    public int getImages() { return images; }

    public void setImages(int images) { this.images = images; }

    public String getGroupName() { return groupName;}

    public void setGroupName(String groupName) {this.groupName = groupName;}
}
