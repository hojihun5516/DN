package com.example.jihun.dn;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UploadPicture extends AppCompatActivity {
    View v;
    EditText edtTitle,edtContent;
    Button btnFind;
    final int REQ_CODE_SELECT_IMAGE = 100;
    ImageView image;
    Button btnUpload;
    Uri uri;
    UploadTask uploadTask;
    String master;
    //groupdatabase
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth;
    StorageReference mStorageRef;
    String email,groupName,nick;
    GroupInfo groupInfo;
    int index;
    MutableData mutal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_picture);
        //복사해오자 필요한것만 메이크 그룹액티비티에서
        Intent intent = getIntent();
        nick = intent.getExtras().getString("userNick");
        groupName = intent.getExtras().getString("groupName");

        edtTitle = (EditText) findViewById(R.id.edtTitle);
        edtContent = (EditText) findViewById(R.id.edtContent);
        btnFind = (Button) findViewById(R.id.btnFind);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        final StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://donong-d7193.appspot.com/");
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();


////////////////////////////
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        //////////////////////////////////////////////////////////////////////////

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
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("UPload", "update setOnClickListener");
                //progressDialog를 띄어주고
                StorageReference riversRef = storageRef.child("board/" + groupName + "/pictureBoard/"+ uri.getLastPathSegment());
                uploadTask = riversRef.putFile(uri);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(UploadPicture.this, "Fail", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getIndex();

                    }
                });
            }
        });
    }
    private void updateDB() {
        Log.d("UPload", "updateDB");

        /////////////////
        //여기서 내가 데이터베이스에 index를 가져오고싶어함
        //그래서 저장할때 index를 같이저장해주고싶은데 방법을찾는중임
        ///////////////////////////////////////////////////////////////////////////////////////////////////
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String strNow = sdfNow.format(date);
        PictureListItem pictureListItem = new PictureListItem(nick,uri.getLastPathSegment(),
                edtTitle.getText().toString(), edtContent.getText().toString(),date,groupName,index);

        Log.d("NOW_Index","index"+index+"groupName"+groupName+"Title"+edtTitle.getText().toString());

        databaseReference.child("BOARD").child(groupName).child("picture")
                .child(String.valueOf(index)).setValue(pictureListItem).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent1 = new Intent(UploadPicture.this,PictureActivity.class);
                intent1.putExtra("groupName",groupName);
                intent1.putExtra("userNick",nick);
                startActivity(intent1);
                finish();
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
    private void getIndex(){
        Log.d("UPload", "getIndex");
        databaseReference.child("BOARD").child(groupName).child("picture").child("count").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                if (mutableData.getValue() == null) {
                    mutableData.setValue(1);
                    //databaseReference.child("BOARD").child("TEST").setValue(test);
                } else {
                    int count =  mutableData.getValue(Integer.class);
                    mutableData.setValue(count + 1);
                    Log.d("MyTest","groupName : Org :" + count + " Mod:" + String.valueOf(count + 1));
                    //databaseReference.child("BOARD").child("TEST").setValue(test);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean success, DataSnapshot dataSnapshot) {
                // Analyse databaseError for any error during increment
                int count = dataSnapshot.getValue(Integer.class);
                index=count;
                Log.d("MyTest","groupName :onComplete :" + count);
                Log.d("MyTest","onComplete Success :" + success);
                updateDB();

            }
        });
    }

}