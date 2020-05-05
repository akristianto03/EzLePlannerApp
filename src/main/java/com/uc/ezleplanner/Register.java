package com.uc.ezleplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText inFullName,inUsername,inPhone,inAge,inEmailReg,inPasswordReg;
    Button btnReg;
    TextView btnLog;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inFullName = findViewById(R.id.inFullName);
        inUsername = findViewById(R.id.inUsername);
        inPhone = findViewById(R.id.inPhone);
        inAge = findViewById(R.id.inAge);
        inEmailReg = findViewById(R.id.inEmailReg);
        inPasswordReg = findViewById(R.id.inPasswordReg);
        btnReg = findViewById(R.id.btnReg);
        btnLog = findViewById(R.id.btnLog);
        progressBar = findViewById(R.id.progressBar);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fullname = inFullName.getText().toString().trim();
                final String username = inUsername.getText().toString().trim();
                final String phone = inPhone.getText().toString().trim();
                final String age = inAge.getText().toString().trim();
                final String email = inEmailReg.getText().toString().trim();
                String password = inPasswordReg.getText().toString().trim();

                if(TextUtils.isEmpty(fullname)){
                    inFullName.setError("Fullname is Required!");
                    return;
                }

                if(TextUtils.isEmpty(username)){
                    inUsername.setError("Username is Required!");
                    return;
                }

                if(TextUtils.isEmpty(phone)){
                    inPhone.setError("Phonenumber is Required!");
                    return;
                }

                if(TextUtils.isEmpty(age)){
                    inAge.setError("Age is Required!");
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    inEmailReg.setError("Email is Required!");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    inPasswordReg.setError("Password is Required!");
                    return;
                }

                if(password.length() < 6){
                    inPasswordReg.setError("Password must be at least 6 Characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //Register user to firebase

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(Register.this, "User Created.", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("fName", fullname);
                            user.put("uname", username);
                            user.put("phone", phone);
                            user.put("age", age);
                            user.put("email", email);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for " + userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        }else{
                            Toast.makeText(Register.this,"Error !"+task.getException(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });

            }
        });

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

    }
}
