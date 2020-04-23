package com.example.idobeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.idobeta.FBref.FBDB;
import static com.example.idobeta.FBref.refFamily;
import static com.example.idobeta.FBref.refUsers;

public class Connect extends AppCompatActivity {
    CheckBox checkBox;
    Switch switch1;
    EditText name;
    EditText email;
    EditText pass;
    private FirebaseAuth mAuth1;
    String email1;
    String password;
    Button finish1;
    TextView tvtitle;
    User user1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        name = (EditText) findViewById(R.id.name);
        pass = (EditText) findViewById(R.id.pass);
        email = (EditText) findViewById(R.id.email);
        switch1 = (Switch) findViewById(R.id.switch1);
        finish1 = (Button) findViewById(R.id.finish);
        tvtitle = (TextView) findViewById(R.id.textV);
        mAuth1 = FirebaseAuth.getInstance();
    }

    /**
     * change the activity to "login" or "Register".
     * @param view
     */
    public void OnSwitch(View view) {
        if (switch1.isChecked()) {
            switch1.setText("don't have an account? click off and join us!");
            tvtitle.setText("Login");
            name.setVisibility(View.INVISIBLE);
            finish1.setText("Login!");
        } else {
            switch1.setText("already have an account? click on and log in!");
            tvtitle.setText("Register");
            name.setVisibility(View.VISIBLE);
            finish1.setText("join us!");
        }

    }

    /**
     * create new user or login user.
     * @param view
     */
    public void click(View view) {
        if (finish1.getText().equals("join us!")) {
            email1 = email.getText().toString();
            password = pass.getText().toString();
            mAuth1.createUserWithEmailAndPassword(email1, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("Connect", "createUserWithEmail:success");
                                FirebaseUser fuser = mAuth1.getCurrentUser();
                                String uid = fuser.getUid();
                                String Uname = name.getText().toString();
                                User userDB = new User(uid, Uname, password, email1);
                                refUsers.child(uid).setValue(userDB);
                                SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
                                SharedPreferences.Editor editor=settings.edit();
                                editor.putString("currentUser",userDB.getUid());
                                editor.commit();
                                Toast.makeText(Connect.this, "thank you for joining us!", Toast.LENGTH_LONG).show();
                                user1 = userDB;
                            } else {
                                Log.w("Connect", "createUserWithEmail:failure", task.getException());
                                Toast.makeText((Connect.this), "this email already exist", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        } else {
            final String pass1 = pass.getText().toString();
            final String email2 = email.getText().toString();
            FirebaseUser fbuser = mAuth1.getCurrentUser();
            String uid = fbuser.getUid();
            Query query = refUsers.orderByChild("uid").equalTo(uid);
            ValueEventListener VEL = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dS) {
                    if (dS.exists()) {
                        for (DataSnapshot data : dS.getChildren()) {
                            user1 = data.getValue(User.class);
                            if (user1.getEmail().equals(email2) && user1.getPassword().equals(pass1)) {
                                    SharedPreferences settings = getSharedPreferences("PREFS_NAME", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putBoolean("stayConnect", true);
                                    editor.commit();
                                    ChooseFamily(user1);
                            } else {
                                Toast.makeText(Connect.this, "incorrect email or password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            query.addListenerForSingleValueEvent(VEL);
        }
    }

    /**
     * remove to activity "YourFamily".
     * @param user1
     */
    public void ChooseFamily(User user1) {
        Intent getUser = new Intent(this, YourFamily.class);
        getUser.putExtra("a", user1.getUid());
        getUser.putExtra("b", user1.getName());
        getUser.putExtra("c", user1.getPassword());
        getUser.putExtra("d", user1.getEmail());
        startActivity(getUser);
    }
}