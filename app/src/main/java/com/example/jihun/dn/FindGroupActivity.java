package com.example.jihun.dn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FindGroupActivity extends AppCompatActivity {
    ListView lvGroupList;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<String> groupName = new ArrayList<>();
    ArrayList<String> myGroupName = new ArrayList<>();
    FindGroupAdapter findGroupAdapter;
    String email, gN;
    Button btnGoToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_group);
        lvGroupList = (ListView) findViewById(R.id.lvGroupList);
        mAuth = FirebaseAuth.getInstance();
        email = mAuth.getCurrentUser().getEmail();
        email = email.replace(".", "_");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        findGroupAdapter = new FindGroupAdapter(this, groupName);
        DatabaseReference dr = databaseReference.child("GROUP");
        lvGroupList.setAdapter(findGroupAdapter);
        Intent intent = getIntent();
        myGroupName = intent.getStringArrayListExtra("groupNames");
        btnGoToMain = ((Button) findViewById(R.id.btnGoToMain));
        btnGoToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindGroupActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Log.d("MYGROUPNAMESSS", String.valueOf(myGroupName));
        dr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("GETSNAP", dataSnapshot.getKey());
                Log.d("MYGROUPNAMESSS", String.valueOf(myGroupName));
                if (myGroupName != null) {
                    for (int i = 0; i < myGroupName.size(); i++) {
                        gN = dataSnapshot.getKey();
                        if (gN != null && myGroupName.get(i).equals(gN) == true) {
                            break;
                        } else if (myGroupName.get(i).equals(gN)==false&&(i+1)== myGroupName.size()) {
                            groupName.add(gN);
                        }
                    }
                } else {
                    Log.d("HowMany", String.valueOf(groupName));
                    groupName.add(gN);
                    Log.d("HowMany2", String.valueOf(groupName));
                    Log.d("HowMany3", String.valueOf(groupName));
                }
                findGroupAdapter.notifyDataSetChanged();
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
    }

}
