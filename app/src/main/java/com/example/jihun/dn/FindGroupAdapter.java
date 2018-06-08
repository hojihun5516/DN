package com.example.jihun.dn;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jihun on 2017-07-06.
 */

public class FindGroupAdapter extends BaseAdapter {
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String email, em, phone, nick, pwd, name;
    ArrayList<String> grna;
    Context context;
    ArrayList<String> groupName;
    final StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://donong-d7193.appspot.com/");

    public FindGroupAdapter(Context context, ArrayList<String> groupName) {
        this.context = context;

        this.groupName = groupName;
    }

    @Override
    public int getCount() {
        /*int getCount()는 이 리스트뷰가 몇개의 아이템을 가지고있는지를 알려주는 함수입니다.
        우리는 arraylist의 size(갯수) 만큼 가지고있으므로 return 0 ; ->      this.list_itemArrayList.size();
        으로 변경합니다.*/
        return this.groupName.size();
    }

    //현재어던포지션인지를 알려주고우리는 그 어레이의 해당포지션을가져올것이다
    @Override
    public Object getItem(int position) {
        return this.groupName.get(position);
    }

    //현재 어떤 포지션인지를 물어봄
    @Override
    public long getItemId(int position) {
        return position;
    }

    //가장중요한부분!!! xml연결시켜줄거다
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        email = mAuth.getCurrentUser().getEmail();
        email = email.replace(".", "_");
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.group_list, null);
        }
        ((TextView) convertView.findViewById(R.id.tvGroupName)).setText(groupName.get(position));
        ((Button) convertView.findViewById(R.id.btnJoin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("EMAIl", email);
                Log.d("POSITION", groupName.get(position));
                databaseReference.child("USER").child(email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String gN = groupName.get(position);
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if (child.getKey().equals("nick")) {
                                nick = (String) child.getValue();
                            } else if (child.getKey().equals("email")) {
                                email=(String)child.getValue();
                            } else if (child.getKey().equals("groupName")) {
                                grna = (ArrayList<String>) child.getValue();
                            } else if (child.getKey().equals("name")) {
                                name = (String) child.getValue();
                            } else if (child.getKey().equals("phone")) {
                                phone = (String) child.getValue();
                            } else if (child.getKey().equals("pwd")) {
                                pwd = (String) child.getValue();
                            }
                        }
                        email=email.replace(".","_");
                        UserInfo userInfo = new UserInfo(em,name,nick,pwd,phone,grna);
                        Map<String, Object> userValues = userInfo.toMap(gN);
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/USER/" +email, userValues);
                        databaseReference.updateChildren(childUpdates);
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        return convertView;
    }
}
