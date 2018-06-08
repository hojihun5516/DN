package com.example.jihun.dn;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tvNick)
    TextView tvNick;
    @BindView(R.id.btnMake)
    Button btnMake;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<String> groupName;
    StorageReference mStorageRef;
    Uri gI;
    Button btnFind;
    ArrayList<GroupListItem> groupListItems;
    GroupAdapter groupAdapter;
    ListView groupList;
    String groupImage;
    String nick;
    int test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        groupList = (ListView) findViewById(R.id.lvGroupList);
        //이걸 꼭 호출해줘야 한다
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        String email = mAuth.getCurrentUser().getEmail();
        email = email.replace(".", "_");
        btnFind=(Button)findViewById(R.id.btnFind);
        firebaseDatabase = FirebaseDatabase.getInstance();
        ////////////////////////////////////////////////////////////////////////////////////////////
        databaseReference = firebaseDatabase.getReference();
        final StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://donong-d7193.appspot.com/");
        //Log.d("TAG",databaseReference.child("USER").child(email).getKey());
        ////////////////////////////////////////////////////////////////////////////////////////////
        mStorageRef = FirebaseStorage.getInstance().getReference();

        databaseReference.child("USER").child(email).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("TAG", "KEY:" + dataSnapshot.getKey());
                if (dataSnapshot.getKey().equals("nick")) {
                    nick = (String) dataSnapshot.getValue();
                    tvNick.setText(nick + "님");
                } else if (dataSnapshot.getKey().equals("groupName")) {
                    groupName = (ArrayList<String>) dataSnapshot.getValue();
                    Log.d("TAG", "ArrayList:" + groupName.get(0));
                    updateList(storageRef);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        btnMake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MakeGroupActivity.class);
                intent.putExtra("userNick",nick);
                startActivity(intent);
            }
        });
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this, FindGroupActivity.class);
                intent1.putExtra("groupNames",groupName);
                startActivity(intent1);
            }
        });
        // My top posts by number of stars


        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //데이터베이스를 보드로 만들어라
                Intent intent = new Intent(MainActivity.this, GroupMainActivity.class);
                intent.putExtra("userNick", nick);
                intent.putExtra("groupName", groupName.get(i));
                startActivity(intent);

            }
        });

    }

    private void updateList(StorageReference storageRef) {
        groupListItems = new ArrayList<GroupListItem>();
        groupAdapter = new GroupAdapter(MainActivity.this, groupListItems);
        groupList.setAdapter(groupAdapter);

        if (groupName.size() > 0) {
            for (int i=0;i<groupName.size();i++){
            Log.d("Group", "GET KEY :" + databaseReference.child("GROUP").child(groupName.get(i)).getKey());
                final int finalI = i;
                databaseReference.child("GROUP").child(groupName.get(i)).child("groupImage").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("Group" ,"KEY : " + dataSnapshot.getKey() );
                    if (dataSnapshot.getKey().equals("groupImage")) {

                                groupImage = (String) dataSnapshot.getValue();
                                String imsi = (groupName.get(finalI));
                                groupListItems.add(new GroupListItem(imsi, groupImage));

                                Log.d("Group", "Group:" + imsi  + " Image:" + groupImage );

                                groupAdapter.notifyDataSetChanged();
                            }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        }
    }


}

