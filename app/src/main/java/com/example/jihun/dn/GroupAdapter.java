package com.example.jihun.dn;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by jihun on 2017-07-06.
 */

public class GroupAdapter extends BaseAdapter {

    Context context;
    ArrayList<GroupListItem> groupListItems;
    final StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://donong-d7193.appspot.com/");
    public GroupAdapter(Context context, ArrayList<GroupListItem> groupListItems) {
        this.context = context;

        this.groupListItems = groupListItems;
    }

    @Override
    public int getCount() {
        /*int getCount()는 이 리스트뷰가 몇개의 아이템을 가지고있는지를 알려주는 함수입니다.
        우리는 arraylist의 size(갯수) 만큼 가지고있으므로 return 0 ; ->      this.list_itemArrayList.size();
        으로 변경합니다.*/
        return this.groupListItems.size();
    }

    //현재어던포지션인지를 알려주고우리는 그 어레이의 해당포지션을가져올것이다
    @Override
    public Object getItem(int position) {
        return this.groupListItems.get(position);
    }

    //현재 어떤 포지션인지를 물어봄
    @Override
    public long getItemId(int position) {
        return position;
    }

    //가장중요한부분!!! xml연결시켜줄거다
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.groupitem, null);
        }
        ((TextView)convertView.findViewById(R.id.tvGroupName)).setText(groupListItems.get(position).getGroupName());
        //((ImageView)convertView.findViewById(R.id.ivGroupImage)).setImageURI(groupListItems.get(position).getGroupImage());

        Glide.with(context).using(new FirebaseImageLoader())
                .load(storageRef.child("group/" +  (groupListItems.get(position).getGroupName()) + "/" +groupListItems.get(position).getGroupImage()))
                .into((ImageView)convertView.findViewById(R.id.ivGroupImage));

        Log.d("Group","group/" +  (groupListItems.get(position).getGroupName()) + "/" +groupListItems.get(position).getGroupImage());
        //왜 이미지를 못가져오냐


        return convertView;
    }
}
