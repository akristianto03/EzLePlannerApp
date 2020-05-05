package com.uc.ezleplanner.fragment;

import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uc.ezleplanner.Login;
import com.uc.ezleplanner.R;

import javax.annotation.Nullable;

import static com.uc.ezleplanner.Register.TAG;

public class AccountFragment extends Fragment {

    TextView uname,fname,pnumber,age;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){

        uname = getView().findViewById(R.id.username);
        fname = getView().findViewById(R.id.fname);
        pnumber = getView().findViewById(R.id.pnumber);
        age = getView().findViewById(R.id.age);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        userID = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String username = documentSnapshot.getString("uname");
                    String fullname = documentSnapshot.getString("fName");
                    String phone = documentSnapshot.getString("phone");
                    String agee = documentSnapshot.getString("age");

                    uname.setText(username);
                    fname.setText(fullname);
                    pnumber.setText(phone);
                    age.setText(agee);
                }else{
                    Toast.makeText(getActivity(),"document doesn't exist",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return inflater.inflate(R.layout.activity_account_fragment,container,false);
    }

    public void logout (View view){
        FirebaseAuth.getInstance().signOut(); //logout
        startActivity(new Intent(getActivity().getApplicationContext(), Login.class));
    }
}
