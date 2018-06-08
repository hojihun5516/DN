package com.example.jihun.dn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;

public class PictureActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    ListView listView;
    PictureAdapter pictureAdapter;
    Button btnAddPicture;
    String title, content;
    int count;
    ArrayList<PictureListItem> pictureListItems;
    FirebaseAuth mAuth;
    String nick, groupName, pictures;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    Date d;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        Intent intent = getIntent();
        nick = intent.getExtras().getString("userNick");
        groupName = intent.getExtras().getString("groupName");
        listView = (ListView) findViewById(R.id.lvPictureList);
        ///추가////////////
        mAuth = FirebaseAuth.getInstance();
        String email = mAuth.getCurrentUser().getEmail();
        email = email.replace(".", "_");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://donong-d7193.appspot.com/");
        ///////////////
        btnAddPicture = (Button) findViewById(R.id.btnAddPicture);
        btnAddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PictureActivity.this, UploadPicture.class);
                intent.putExtra("groupName", groupName);
                intent.putExtra("userNick", nick);
                startActivity(intent);
                finish();
            }
        });

    }
    public String getGroupName(){
        return groupName;
    }
    @Override
    protected void onResume() {
        super.onResume();
        databaseReference.child("BOARD").child(groupName).child("picture").addChildEventListener(childEventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        databaseReference.child("BOARD").child(groupName).child("picture").removeEventListener(childEventListener);
    }

    //////////////////////////////////////////////////////////////////////////////
    //여기서 내가 데이터베이스에 index를 가져오고싶어함
    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Log.d("TAG", "KEY:" + dataSnapshot.getKey());
            if (dataSnapshot.getKey().equals("count")) {
                count = dataSnapshot.getValue(Integer.class);
            }
            updateList(storageRef);
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
    };

    private void updateList(StorageReference storageRef) {

//        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progDialog.setMessage("Log in ....");
//        progDialog.show();
        pictureListItems = new ArrayList<PictureListItem>();
        pictureAdapter = new PictureAdapter(PictureActivity.this, pictureListItems);
        listView.setAdapter(pictureAdapter);
        if (count != 0) {
            for (int i = 1; i <= count; i++) {

                Log.d("Group", "GET KEY :" + databaseReference.child("BOARD").child(groupName).child("picture").child(String.valueOf(i)).getKey());

                DatabaseReference dr = databaseReference.child("BOARD").child(groupName).child("picture").child(String.valueOf(i));
                Log.d("PICTuREACTIVITY PAth", String.valueOf(dr.getKey()));
                dr.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        PictureListItem p = dataSnapshot.getValue(PictureListItem.class);
                        if (p != null) {
                            Log.d("Group", "KEYDATA: " + p.getTitle());
                            pictureListItems.add(p);
                            pictureAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            ///////////////////////////////////////////////////////////////////////////////////////////////////
        }
    }

}