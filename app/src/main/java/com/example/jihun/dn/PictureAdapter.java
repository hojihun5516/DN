package com.example.jihun.dn;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jihun on 2017-07-06.
 */

public class PictureAdapter extends BaseAdapter {
    Button btnDelete, btnShowReply, btnReply;
    Context context;
    ArrayList<PictureListItem> pictureListItem;
    final StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://donong-d7193.appspot.com/");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth;
    String email, nick, master;
    EditText edtReply ;
    int index;
    ReplyItem replyItem;

    public PictureAdapter(Context context, ArrayList<PictureListItem> pictureListItem) {
        this.context = context;
        this.pictureListItem = pictureListItem;
    }

    @Override
    public int getCount() {
        /*int getCount()는 이 리스트뷰가 몇개의 아이템을 가지고있는지를 알려주는 함수입니다.
        우리는 arraylist의 size(갯수) 만큼 가지고있으므로 return 0 ; ->      this.list_itemArrayList.size();
        으로 변경합니다.*/
        return this.pictureListItem.size();
    }

    //현재어던포지션인지를 알려주고우리는 그 어레이의 해당포지션을가져올것이다
    @Override
    public Object getItem(int position) {
        return this.pictureListItem.get(position);
    }

    //현재 어떤 포지션인지를 물어봄
    @Override
    public long getItemId(int position) {
        return position;
    }

    //가장중요한부분!!! xml연결시켜줄거다
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        String groupName = pictureListItem.get(position).getGroupName();
        Log.d("please", "board/" + pictureListItem.get(position).getGroupName() + "/pictureBoard/" + pictureListItem.get(position).getPictures());
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.pictureitem, null);

        }
        Date d = pictureListItem.get(position).getWrite_date();
        final int a = position;
        ((TextView) convertView.findViewById(R.id.tvTitle)).setText(pictureListItem.get(position).getTitle());
        ((TextView) convertView.findViewById(R.id.tvDate)).setText(sdf.format(d));
        ((TextView) convertView.findViewById(R.id.tvContent)).setText(pictureListItem.get(position).getContent());
        edtReply = (EditText)convertView.findViewById(R.id.edtReply);
        btnReply = (Button) convertView.findViewById(R.id.btnReply);
        btnShowReply = (Button) convertView.findViewById(R.id.btnShowReply);
        btnDelete = (Button) convertView.findViewById(R.id.btnDelete);
        /////////////////////////////////////////////////////////////////////////////
//        글의 nick과 나의 nick이 같을 때와 master와 나의 nick이 같을 때
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        email = mAuth.getCurrentUser().getEmail();
        email = email.replace(".", "_");
        databaseReference.child("USER").child(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.getKey().equals("nick")) {
                        nick = (String) child.getValue();
                    }
                    if ((pictureListItem.get(position).getNick() != null && pictureListItem.get(position).getNick().equals(nick))) {
                        btnDelete.setVisibility(View.VISIBLE);
                        Log.d("Boolean", String.valueOf(pictureListItem.get(position).getNick() != null && pictureListItem.get(position).getNick().equals(nick)));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("GROUP").child(pictureListItem.get(position).getGroupName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.getKey().equals("master")) {
                        master = (String) child.getValue();
                    }
                    if (master != null && nick != null && master.equals(nick)) {
                        btnDelete.setVisibility(View.VISIBLE);
                        Log.d("Boolean2", String.valueOf((master != null && nick != null && master.equals(nick))));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /////////////////////////////////////////////////////////////////////////////////
        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("UPload", "getIndex");
                databaseReference.child("REPLY").child(pictureListItem.get(a).getGroupName()).child("picture")
                        .child(String.valueOf(pictureListItem.get(position).getIndex())).child("count").runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        if (mutableData.getValue() == null) {
                            mutableData.setValue(1);

                        } else {
                            int count = mutableData.getValue(Integer.class);
                            mutableData.setValue(count + 1);
                            Log.d("PictureAdapter", "groupName : " + count + " Mod:" + String.valueOf(count + 1));
                        }
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean success, DataSnapshot dataSnapshot) {
                        // Analyse databaseError for any error during increment
                        int count = dataSnapshot.getValue(Integer.class);
                        index = count;
                        Log.d("MyTest", "groupName :onComplete :" + count);
                        Log.d("MyTest", "onComplete Success :" + success);
                        Log.d("MYTEST","what is a"+a);
                        updateDB(a);


                    }
                });

            }
        });
        btnShowReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        /////////////////////////////////////////////////////////////////////////////////
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference();
                String groupName = pictureListItem.get(position).getGroupName();
                databaseReference.child("BOARD").child(groupName).child("picture").child(String.valueOf(a)).removeValue();
            }
        });
        Glide.with(context).using(new FirebaseImageLoader())
                .load(storageRef.child("board/" + pictureListItem.get(position).getGroupName() + "/pictureBoard/" + pictureListItem.get(position).getPictures()))
                .into((ImageView) convertView.findViewById(R.id.ivUpload));

        return convertView;
    }

    private void updateDB(int position) {
        Log.d("WHATISTHIS", "IIIIIIII");
//        String reply = edtReply.getText().toString();
            Log.d("WHATISTHIS", edtReply.getText().toString());
            ReplyItem replyItem1 = new ReplyItem(nick, edtReply.getText().toString());
            Log.d("WHATISTHIS22", replyItem1.getContent());
            databaseReference.child("REPLY").child(pictureListItem.get(position).getGroupName()).child("picture")
                    .child(String.valueOf(pictureListItem.get(position).getIndex())).child(String.valueOf(index)).setValue(replyItem1).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
//                    Log.d("WHATISTHIS33", replyItem1.getContent());
                    edtReply.setText("");
//                    Log.d("WHATISTHIS44", replyItem1.getContent());

                    }
            });
    }

}
