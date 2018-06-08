package com.example.jihun.dn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GroupMainActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String master;
    Button btnSetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_main);
        Intent intent = getIntent();
        final String nick = intent.getExtras().getString("userNick");
        final String groupName = intent.getExtras().getString("groupName");
        ((TextView)findViewById(R.id.tvGroupName)).setText(groupName);
        ((TextView)findViewById(R.id.tvNick)).setText(nick);
        btnSetting = (Button)findViewById(R.id.btnSetting);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("GROUP").child(groupName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey().equals("master")){
                    Log.d("MASTER",dataSnapshot.getValue(String.class));
                    master = dataSnapshot.getValue(String.class);
                    if(master.equals(nick)){
                        btnSetting.setVisibility(View.VISIBLE);
                    }
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
                /////////////////////사진//////////////////////////
        ((Button)findViewById(R.id.btnPicture)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupMainActivity.this,PictureActivity.class);
                intent.putExtra("groupName",groupName);
                intent.putExtra("userNick",nick);
                startActivity(intent);

            }
        });

        ////////////////////////////////익명게시판///////////////////////
        ((Button)findViewById(R.id.btnAnony)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //////////////////////////////자유게시판////////////////////////
        ((Button)findViewById(R.id.btnBoard)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ////////////////////게임////////////////////////////////
        ((Button)findViewById(R.id.btnGame)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ////////////////////////GPS///////////////////////
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ////////////////////설정////////////////
        ((Button)findViewById(R.id.btnSetting)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /////////////////유저리스트 만들어야돼
                Intent intent1 = new Intent(GroupMainActivity.this,FindGroupActivity.class);
                intent1.putExtra("userNick",nick);
                startActivity(intent1);

            }
        });

    }
}
