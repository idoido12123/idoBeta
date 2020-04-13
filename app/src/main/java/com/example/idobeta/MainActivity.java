package com.example.idobeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.idobeta.FBref.refUsers;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth1;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SharedPreferences settings = getSharedPreferences("PREFS_NAME", MODE_PRIVATE);
        boolean isChecked = settings.getBoolean("stayConnect", false);
        boolean haveFamily = settings.getBoolean("haveFamily", false);
        if (isChecked) {
            if (haveFamily) {
                gotoReshimot(settings.getString("currentFamily", ""));
            }
            else {
               getUser();
            }
        }
       else{
           gotoConnect();
       }
    }
    public void gotoConnect(){
        Intent t1 = new Intent(this, Connect.class);
        startActivity(t1);
    }
    public void gotoReshimot(String familyName){
        Intent gotoReshimot1 = new Intent(this, Reshimot.class);
        gotoReshimot1.putExtra("a", familyName);
        startActivity(gotoReshimot1);
    }
    public void getUser(){
        mAuth1 = FirebaseAuth.getInstance();
        FirebaseUser fbuser = mAuth1.getCurrentUser();
        String uid = fbuser.getUid();
        Query query = refUsers.orderByChild("uid").equalTo(uid);
        ValueEventListener getfamily = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                     user = data.getValue(User.class);
                     gotoYourFamily(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        query.addListenerForSingleValueEvent(getfamily);
    }
    public void gotoYourFamily(User user){
        Intent getUser = new Intent(this, YourFamily.class);
        getUser.putExtra("a", user.getUid());
        getUser.putExtra("b", user.getName());
        getUser.putExtra("c", user.getPassword());
        getUser.putExtra("d", user.getEmail());
        startActivity(getUser);
    }
}
