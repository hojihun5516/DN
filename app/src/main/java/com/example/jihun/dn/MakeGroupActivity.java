package com.example.jihun.dn;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MakeGroupActivity extends AppCompatActivity {


    View v;
    EditText edtGroupName;
    Button btnFind;
    final int REQ_CODE_SELECT_IMAGE = 100;
    ImageView image;
    Button btnCreate;
    Uri uri;
    UploadTask uploadTask;
    String master;
    //groupdatabase
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth;
    StorageReference mStorageRef;
    String email;
    GroupInfo groupInfo;
    String em, name, nick, pwd, phone, getNick;
    ArrayList<String> grna;
    ProgressDialog progDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_group);
        edtGroupName = (EditText) findViewById(R.id.edtGroupName);
        btnFind = (Button) findViewById(R.id.btnFind);
        btnCreate = (Button) findViewById(R.id.btnCreate);
        final StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://donong-d7193.appspot.com/");
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        grna = new ArrayList<String>();
        Intent intent = getIntent();
        getNick = intent.getExtras().getString("userNick");
////////////////////////////
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        //////////////////////////////////////////////////////////////////////////
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                progDialog.setMessage("Creating");
//                progDialog.show();
                final String groupName = edtGroupName.getText().toString();
                StorageReference riversRef = storageRef.child("group/" + groupName + "/" + uri.getLastPathSegment());
                uploadTask = riversRef.putFile(uri);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(MakeGroupActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        updateDB();

                    }
                });
            }
        });
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //버튼 클릭시 처리로직
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);


            }
        });
    }

    private void updateDB() {
        UserInfo userInfo = new UserInfo();
        email = mAuth.getCurrentUser().getEmail();
        email = email.replace(".", "_");
        final String groupName = edtGroupName.getText().toString();

        master = getNick;
        groupInfo = new GroupInfo();
        groupInfo.setMaster(master);
        groupInfo.setGroupName(groupName);
        groupInfo.setGroupImage(uri.getLastPathSegment());
        databaseReference.child("GROUP").child(groupName).setValue(groupInfo);
        //방금 push뺐음
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        databaseReference.child("USER").child(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //겟벨류면 어레이리스트로받아라 나중에
                ////////////////////////////////여기부분
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.getKey().equals("nick")) {
                        Log.d("realNick", (String) child.getValue());
                        nick = (String) child.getValue();
                    } else if (child.getKey().equals("email")) {
                        Log.d("realemail", (String) child.getValue());
                        email = (String) child.getValue();
                    } else if (child.getKey().equals("groupName")) {
                        grna = (ArrayList<String>) child.getValue();
                        Log.d("ARRAY55", "" + grna);
                    } else if (child.getKey().equals("name")) {
                        name = (String) child.getValue();
                    } else if (child.getKey().equals("phone")) {
                        phone = (String) child.getValue();
                    } else if (child.getKey().equals("pwd")) {
                        pwd = (String) child.getValue();
                    }
                }
                UserInfo userInfo = new UserInfo(em, name, nick, pwd, phone, grna);
                Log.d("TAGlll", String.valueOf(userInfo.getGroupName()));
                email = email.replace(".", "_");
                Map<String, Object> userValues = userInfo.toMap(groupName);
                Log.d("TAGlll222", String.valueOf(userInfo.getGroupName()));
                Map<String, Object> childUpdates = new HashMap<>();
                Log.d("TAGlll2223333", String.valueOf(userInfo.getGroupName()));
                childUpdates.put("/USER/" + email, userValues);
                databaseReference.updateChildren(childUpdates);
//                    UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
//                    String key = dataSnapshot.getKey();
//                    Log.d("Please",key);
//                Map<String, Object> userValues = userInfo.toMap(groupName);
//                Map<String, Object> childUpdates = new HashMap<>();
//                childUpdates.put("/USER/" +email, userValues);
//
//                databaseReference.updateChildren(childUpdates);
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                Log.e("TAG", "" + groupInfo.getGroupImage() + groupInfo.getMaster());
                Intent intent = new Intent(MakeGroupActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        Toast.makeText(getBaseContext(), "resultCode : " + resultCode, Toast.LENGTH_SHORT).show();

        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                uri = data.getData();
                image = (ImageView) findViewById(R.id.vvvvv);
                image.setImageURI(uri);
            }
        }
    }


}
