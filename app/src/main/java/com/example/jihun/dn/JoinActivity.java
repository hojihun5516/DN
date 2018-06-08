package com.example.jihun.dn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JoinActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    LinearLayout llFirst, llSecond;

    FirebaseDatabase firebaseDatabase;
    EditText edtEmail, edtPasswd, edtName, edtPhone, edtNick;
    Button btnNext, btnCreate, btnNickCheck;
    private FirebaseAuth mAuth;
    ProgressDialog progDialog;
    String email;
    String passwd;
    DatabaseReference databaseReference;
    String email_mod;
    int index;
    boolean nickCheck = false;
    ArrayList<String> nicks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        Log.d("TAG", "뭐냐이건");
        getSupportActionBar().hide();
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPasswd = (EditText) findViewById(R.id.edtPasswd);
        btnNext = (Button) findViewById(R.id.btnnext);
        llFirst = (LinearLayout) findViewById(R.id.llfirst);
        llSecond = (LinearLayout) findViewById(R.id.llsecond);
        edtNick = (EditText) findViewById(R.id.edtnick);
        edtName = (EditText) findViewById(R.id.edtname);
        edtPhone = (EditText) findViewById(R.id.edtphone);
        btnCreate = (Button) findViewById(R.id.btnCreate);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        btnNickCheck = (Button) findViewById(R.id.btnNickCheck);
        progDialog = new ProgressDialog(JoinActivity.this);
        mAuth = FirebaseAuth.getInstance();
        nicks = new ArrayList<String>();

        if (mAuth.getCurrentUser() != null) {
            Log.d(TAG, "Current User:" + mAuth.getCurrentUser().getEmail());
            // Go to Main Page
//            GotoMainPage();
        } else {
            Log.d(TAG, "Log out State");
        }
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = edtEmail.getText().toString().trim();
                passwd = edtPasswd.getText().toString().trim();
                Log.d(TAG, "Email:" + email + " Password:" + passwd);
                if (isValidEmail(email) && isValidPasswd(passwd)) {
                    createAccount(email, passwd);
                } else {
                    Toast.makeText(JoinActivity.this,
                            "Check Email or Password",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        btnNickCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                databaseReference.child("USER").child("NICKS").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String message = "You Can't use";
                        if (dataSnapshot.getValue() != null) {

                            ArrayList<String> arr = (ArrayList<String>) dataSnapshot.getValue();

                            for (int i = 0; i < arr.size(); i++) {
                                Log.d("NICKIMSI", arr.get(i) + ":::::MyNick" + edtNick.getText().toString());
                                if (arr.get(i).equals(edtNick.getText().toString().trim())) {
                                    nickCheck = false;
                                    break;
                                } else {
                                    nickCheck = true;
                                }
                            }
                            Log.d("TTAG", nickCheck + "");
                            if (nickCheck == true) {
                                edtNick.setFocusable(false);
                                edtNick.setClickable(false);
                                message = "You Can use";
                            }
                            Toast.makeText(JoinActivity.this,
                                    message, Toast.LENGTH_LONG).show();
                            return;
                        }else{
                            nickCheck=true;
                            if (nickCheck == true) {
                                message = "You Can use";
                            }
                            Toast.makeText(JoinActivity.this,
                                    message, Toast.LENGTH_LONG).show();
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString().trim();
                String nick = edtNick.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();
                if (name.length() > 0 && nick.length() > 0 && phone.length() > 0) {
                    if (nickCheck) {
                        UserInfo userInfo = new UserInfo(email, name, nick, passwd, phone);
//                    Map<String, Object> userValues = userInfo.toMap(null);
//                    Map<String, Object> childUpdates = new HashMap<>();
                        getArray(nick);
                        email_mod = email.replace(".", "_");
//                    childUpdates.put("/USER/" +email_mod, userValues);
//                    databaseReference.updateChildren(childUpdates);
                            databaseReference.child("USER").child(email_mod).setValue(userInfo);

                        GotoMainPage();
                    } else {

                        Toast.makeText(JoinActivity.this,
                                "you have to check Nick", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void phoneCheck() {

    }

    private boolean isValidPasswd(String str) {
        if (str == null || TextUtils.isEmpty(str)) {
            return false;
        } else {
            if (str.length() > 4)
                return true;
            else
                return false;
        }
    }

    private boolean isValidEmail(String str) {
        if (str == null || TextUtils.isEmpty(str)) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(str).matches();
        }
    }

    private void createAccount(String email, String passwd) {
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setMessage("Create User Account....");
        progDialog.show();
        mAuth.createUserWithEmailAndPassword(email, passwd)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "Create Account:" + task.isSuccessful());
                                progDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Account Create Complete");
                                    Log.d(TAG, "Current User:" + mAuth.getCurrentUser().getEmail());
                                    llFirst.setVisibility(View.INVISIBLE);
                                    llSecond.setVisibility(View.VISIBLE);
                                    // Go go Main
//                                    GotoMainPage();
                                } else {
                                    Toast.makeText(JoinActivity.this,
                                            "Create Account Failed", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
    }

    private void signinAccount(String email, String passwd) {
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setMessage("User Account Log in ....");
        progDialog.show();
        mAuth.signInWithEmailAndPassword(email, passwd)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "Sing in Account:" + task.isSuccessful());
                                progDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Account Log in  Complete");
                                    Log.d(TAG, "Current User:" + mAuth.getCurrentUser().getEmail());
                                    // Go go Main
//                                    GotoMainPage();
                                } else {
                                    Toast.makeText(JoinActivity.this,
                                            "Log In Failed", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

    }

    private void GotoMainPage() {
        Intent intent = new Intent(JoinActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void getArray(final String nick) {
        databaseReference.child("USER").child("NICKS").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    nicks.add(child.getValue(String.class));
                }
                nicks.add(nick);
                Log.d("NICKCHECK", nicks + "");
                databaseReference.child("USER").child("NICKS").setValue(nicks);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
