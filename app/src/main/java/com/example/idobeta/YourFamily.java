package com.example.idobeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.idobeta.FBref.refFamily;

public class YourFamily extends AppCompatActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {
    Switch switch2;
    EditText lastName;
    EditText UFname;
    ListView listV;
    TextView tv1;
    ArrayList<Family> FamilyValues = new ArrayList<Family>();
    ArrayList<String> FamilyList = new ArrayList<String>();
    Button signIn;
    Intent t3;
    Button BTN;
    String userName;
    String userID;
    String userEmail;
    String userPass;
    User user;
    boolean flag=false;
    Family family1;
    String str;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_family);
        lastName = (EditText) findViewById(R.id.lastName);
        UFname = (EditText) findViewById(R.id.UName);
        listV = (ListView) findViewById(R.id.listV);
        switch2 = (Switch) findViewById(R.id.switch2);
        tv1 = (TextView) findViewById(R.id.tv1);
        BTN = (Button) findViewById(R.id.button3);
        signIn = (Button) findViewById(R.id.signIn);
        listV.setVisibility(View.INVISIBLE);
        tv1.setVisibility(View.INVISIBLE);
        ValueEventListener FamilyListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                FamilyValues.clear();
                FamilyList.clear();
                for (DataSnapshot data : ds.getChildren()) {
                    String Fname = (String) data.getKey();
                    Family family = data.getValue(Family.class);
                    FamilyValues.add(family);
                    FamilyList.add(Fname);
                }

                ArrayAdapter<String> adp = new ArrayAdapter<String>(YourFamily.this, R.layout.support_simple_spinner_dropdown_item, FamilyList);
                listV.setAdapter(adp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("YourFamily", "Failed to read value.", databaseError.toException());
            }
        };
        refFamily.addValueEventListener(FamilyListener);
        listV.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listV.setOnItemClickListener(this);
        listV.setOnItemLongClickListener(this);
        t3=getIntent();
        userID=t3.getExtras().getString("a");
        userName=t3.getExtras().getString("b");
        userEmail=t3.getExtras().getString("d");
        userPass=t3.getExtras().getString("c");
        user=new User(userID,userName,userPass,userEmail,false,false);
    }


    public void onSwitch2(View view) {
        if (switch2.isChecked()) {
            listV.setVisibility(View.VISIBLE);
            tv1.setVisibility(View.VISIBLE);
            lastName.setVisibility(View.INVISIBLE);
            signIn.setVisibility(View.INVISIBLE);
            UFname.setVisibility(View.INVISIBLE);
            switch2.setText("click off if you want to create new family");
        } else {
            lastName.setVisibility(View.VISIBLE);
            signIn.setVisibility(View.VISIBLE);
            UFname.setVisibility(View.VISIBLE);
            tv1.setVisibility(View.INVISIBLE);
            listV.setVisibility(View.INVISIBLE);
            switch2.setText("click on if you your family is already sign in");
        }
    }

    public void click4(View view) {
        str=UFname.getText().toString();
        Query query=refFamily.orderByChild("familyUname").equalTo(str);
        query.addListenerForSingleValueEvent(addFamily);
    }
    ValueEventListener addFamily = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            {
                if(dataSnapshot.exists()){
                    flag=true;
                }
                if (flag==true){
                    Toast.makeText(YourFamily.this, "enter another family name", Toast.LENGTH_SHORT).show();
                }
                if(flag==false){
                    Family family = new Family(lastName.getText().toString(), str);
                    user.setManager(true);
                    family.addUser(user);
                    refFamily.child(family.getFamilyUname()).setValue(family);
                }
                flag=false;
            }

        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.w("YourFamily", "Failed to read value.", databaseError.toException());
        }
    };

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Query query=refFamily.orderByChild("familyUname").equalTo((String) listV.getItemAtPosition(i)).limitToFirst(1);
        query.addListenerForSingleValueEvent(Fvel);

    }
    com.google.firebase.database.ValueEventListener Fvel=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                family1 = data.getValue(Family.class);
            }
            family1.addUser(user);
            refFamily.child(family1.getFamilyUname()).setValue(family1);
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.w("YourFamily", "Failed to read value.", databaseError.toException());
        }
    };

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        t3=new Intent(this,Reshimot.class);
        t3.putExtra("a",family1.getFamilyUname());
        startActivity(t3);
        return true;
    }


    //public void click5(View view){
     //   t3=new Intent(this,Reshimot.class);
    //    startActivity(t3);
   // }
}