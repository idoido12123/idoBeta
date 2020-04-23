package com.example.idobeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import static com.example.idobeta.FBref.refUsers;

public class YourFamily extends AppCompatActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {
    Switch switch2;
    EditText lastName;
    EditText UFname;
    ListView listV;
    TextView tv1;
    TextView title;
    ArrayList<Family> FamilyValues = new ArrayList<Family>();
    ArrayList<String> FamilyList = new ArrayList<String>();
    Button signIn;
    Intent t3;
    String userName;
    String userID;
    String userEmail;
    String userPass;
    User user;
    boolean flag = false;
    boolean flag2 = false;
    Family family1;
    String str;
    ArrayList<User> userHelper = new ArrayList<>();
    int position;
    Family family;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_family);
        lastName = (EditText) findViewById(R.id.lastName);
        UFname = (EditText) findViewById(R.id.UName);
        listV = (ListView) findViewById(R.id.listV);
        switch2 = (Switch) findViewById(R.id.switch2);
        tv1 = (TextView) findViewById(R.id.tv1);
        signIn = (Button) findViewById(R.id.signIn);
        UFname.setVisibility(View.INVISIBLE);
        lastName.setVisibility(View.INVISIBLE);
        signIn.setVisibility(View.INVISIBLE);
        title=(TextView)findViewById(R.id.textView2);
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
        t3 = getIntent();
        userID = t3.getExtras().getString("a");
        userName = t3.getExtras().getString("b");
        userEmail = t3.getExtras().getString("d");
        userPass = t3.getExtras().getString("c");
        user = new User(userID, userName, userPass, userEmail);
    }

    /**
     * change the activity to "create family" or "choose family".
     * @param view
     */
    public void onSwitch2(View view) {
        if (!switch2.isChecked()) {
            listV.setVisibility(View.VISIBLE);
            tv1.setVisibility(View.VISIBLE);
            lastName.setVisibility(View.INVISIBLE);
            signIn.setVisibility(View.INVISIBLE);
            UFname.setVisibility(View.INVISIBLE);
            title.setText("choose family");
            switch2.setText("want to create new family? click on!");
        } else {
            lastName.setVisibility(View.VISIBLE);
            signIn.setVisibility(View.VISIBLE);
            UFname.setVisibility(View.VISIBLE);
            tv1.setVisibility(View.INVISIBLE);
            listV.setVisibility(View.INVISIBLE);
            title.setText("create family");
            switch2.setText("Your family already signed up? click off and join them!");
        }
    }

    /**
     * check if the family user name is exist,if not it create new family.
     * @param view
     */
    public void click4(View view) {
        str = UFname.getText().toString();
        Query query = refFamily.orderByChild("familyUname").equalTo(str);
        query.addListenerForSingleValueEvent(addFamily);
    }

    ValueEventListener addFamily = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            {
                if (dataSnapshot.exists()) {
                    flag = true;
                }
                if (flag == true) {
                    Toast.makeText(YourFamily.this, "enter another family name", Toast.LENGTH_SHORT).show();
                }
                if (flag == false) {
                    family = new Family(lastName.getText().toString(), str,0);
                    family.addUser(user);
                    Task task = new Task("", "", "", "", true,false);
                    NewList list = new NewList("", "");
                    User request=new User("","","","");
                    family.addTask(task);
                    family.addList(list);
                    family.addRequest(request);
                    family.setNotificition(3);
                    refFamily.child(family.getFamilyUname()).setValue(family);
                    flag2 = true;
                    moveToReshimot();
                }
                flag = false;
                flag2 = false;
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.w("YourFamily", "Failed to read value.", databaseError.toException());
        }
    };

    /**
     * check if the user is in the family,if yes go to family lists, if not make toast.
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        position=i;
        Query query= refFamily.orderByChild("familyUname").equalTo((String) listV.getItemAtPosition(i));
        ValueEventListener checkUser=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userHelper.clear();
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    family=data.getValue(Family.class);
                    while(!family.getUsers().isEmpty()){
                        User user= (User) family.getUsers().remove(0);
                        if(user.getEmail().equals(userEmail)){
                            flag2=true;
                        }
                        userHelper.add(user);
                    }
                    family.setUsers(userHelper);
                    moveToReshimot();
                    flag2=false;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        query.addListenerForSingleValueEvent(checkUser);
    }

    /**
     * send the chosen family requests to join her.
     * @param adapterView
     * @param view
     * @param i
     * @param l
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        position=i;
        Query query=refFamily.orderByChild("familyUname").equalTo((String) listV.getItemAtPosition(i));
        query.addListenerForSingleValueEvent(Fvel);
        return true;
    }
    com.google.firebase.database.ValueEventListener Fvel=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            userHelper.clear();
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                family1 = data.getValue(Family.class);
                ArrayList<User>usersHelper=new ArrayList<>();
                while (!family1.getUsers().isEmpty()) {
                    User user1 = (User) family1.getUsers().remove(0);
                    if (user1.getEmail().equals(userEmail)) {
                        flag2 = true;
                    }
                    usersHelper.add(user1);
                }
                family1.setUsers(usersHelper);
            }
            if (flag2 == false) {
                family1.addRequest(user);
                refFamily.child((String) listV.getItemAtPosition(position)).setValue(family1);
                Toast.makeText(YourFamily.this, "your request sent!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(YourFamily.this, "you are member in this family", Toast.LENGTH_SHORT).show();
            }
            flag2=false;
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.w("YourFamily", "Failed to read value.", databaseError.toException());
        }
    };

    /**
     * if flag2 is true, go to family lists, if not make toast.
     */
    public void moveToReshimot(){
        if (flag2==false){
            Toast.makeText(this, "you are not a member in this family", Toast.LENGTH_SHORT).show();
        }
        else{
            SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
            SharedPreferences.Editor editor=settings.edit();
            editor.putBoolean("haveFamily",true);
            editor.putString("currentFamily",family.getFamilyUname());
            editor.commit();
            family.setNotificition(3);
            refFamily.child(family.getFamilyUname()).setValue(family);
            Intent serviceIntent = new Intent(this, ExmpleService.class);
            startService(serviceIntent);
            t3=new Intent(this,Reshimot.class);
            t3.putExtra("a",(String) family.getFamilyUname());
            startActivity(t3);
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.yourFamilies).setVisible(false);
        menu.findItem(R.id.leaveFamily).setVisible(false);
        menu.findItem(R.id.logOut).setVisible(false);
        menu.findItem(R.id.requests).setVisible(false);
        menu.findItem(R.id.menuTasks).setVisible(false);
        menu.findItem(R.id.menuLists).setTitle("return to current family lists");
        return true;
    }

    /**
     * start menu.
     * @param Item
     * @return true
     */
    public boolean onOptionsItemSelected(MenuItem Item) {
        final SharedPreferences settings = getSharedPreferences("PREFS_NAME", MODE_PRIVATE);
        String st = Item.getTitle().toString();
        if(!settings.getString("currentFamily","").equals("")) {
            if (st.equals("return to current family lists")) {
                Intent go = new Intent(this, Reshimot.class);
                go.putExtra("a", settings.getString("currentFamily", ""));
                startActivity(go);
            }
        }
        else {
            Toast.makeText(this, "you don't have family", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}