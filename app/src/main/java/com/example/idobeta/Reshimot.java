package com.example.idobeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import static com.example.idobeta.FBref.refFamily;

public class Reshimot extends AppCompatActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {
    AlertDialog.Builder addList;
    LinearLayout listDialog;
    TextView title;
    EditText Nreshima;
    EditText ListDate;
    EditText ListTime;
    Button NewList;
    ListView BigList;
    Intent t4;
    String familyName1;
    ArrayList<String> ShopList = new ArrayList<>();
    ArrayList<NewList> ShopValues = new ArrayList<>();
    String listNhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reshimot);
        NewList = (Button) findViewById(R.id.NewList);
        BigList = (ListView) findViewById(R.id.ListOfLists);
        title=(TextView)findViewById(R.id.textView) ;
        SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
        title.setText("welcome to the amazing "+settings.getString("currentFamily","")+" family!");
        t4 = getIntent();
        familyName1 = t4.getExtras().getString("a");
        Query query = refFamily.orderByChild("familyUname").equalTo(familyName1);
        ValueEventListener shopListListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                ShopValues.clear();
                ShopList.clear();
                for (DataSnapshot data : ds.getChildren()) {
                    Family family = data.getValue(Family.class);
                    ShopValues = family.getLists();
                    while (!ShopValues.isEmpty()) {
                        NewList list = ShopValues.remove(0);
                        if(!list.getListName().equals("")) {
                            ShopList.add(list.getListName());
                        }
                    }
                }
                ArrayAdapter<String> adp = new ArrayAdapter<String>(Reshimot.this, R.layout.support_simple_spinner_dropdown_item, ShopList);
                BigList.setAdapter(adp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Reshimot", "Failed to read value.", databaseError.toException());
            }
        };
        query.addValueEventListener(shopListListener);
        BigList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        BigList.setOnItemClickListener(this);
        BigList.setOnItemLongClickListener(this);
    }

    public void newList(View view) {
        listDialog = (LinearLayout) getLayoutInflater().inflate(R.layout.add_list_dialog, null);
        Nreshima = (EditText) listDialog.findViewById(R.id.Nreshima);
        ListDate = (EditText) listDialog.findViewById(R.id.ListDate);
        ListTime = (EditText) listDialog.findViewById(R.id.ListTime);
        addList = new AlertDialog.Builder(this);
        addList.setView(listDialog);
        addList.setTitle("Create new shop list");
        addList.setPositiveButton("OK", OKclick);
        addList.show();
    }

    DialogInterface.OnClickListener OKclick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (i == DialogInterface.BUTTON_POSITIVE) {
                final String Lname = Nreshima.getText().toString();
                final String Ldatime = ListDate.getText().toString() + " " + ListTime.getText().toString();
                Query query = refFamily.orderByChild("familyUname").equalTo(familyName1);
                ValueEventListener addList = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot ds) {
                        for (DataSnapshot data : ds.getChildren()) {
                            Family family = data.getValue(Family.class);
                            NewList list = new NewList(Lname, Ldatime, true);
                            Product product = new Product("", "", "");
                            list.addProduct(product);
                            family.addList(list);
                            refFamily.child(familyName1).setValue(family);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w("Reshimot", "Failed to read value.", databaseError.toException());
                    }
                };
                query.addListenerForSingleValueEvent(addList);
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> Adapter, View view, int i, long l) {
        Intent chosenList = new Intent(this, ChosenList.class);
        chosenList.putExtra("a", (String) BigList.getItemAtPosition(i));
        chosenList.putExtra("b", familyName1);
        startActivity(chosenList);
    }
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        listNhelper = (String) BigList.getItemAtPosition(i);
        Query query = refFamily.orderByChild("familyUname").equalTo(familyName1);
        query.addListenerForSingleValueEvent(Fvel);
        return true;
    }

    com.google.firebase.database.ValueEventListener Fvel = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                Family family = data.getValue(Family.class);
                ArrayList<NewList> listsValues = family.getLists();
                ArrayList<NewList> listsHelper = new ArrayList<>();
                listsHelper.add(listsValues.remove(0));
                while (!listsValues.isEmpty()) {
                    NewList list = listsValues.remove(0);
                    String str = list.getListName();
                    if (!str.equals(listNhelper)) {
                        listsHelper.add(list);
                    }
                }
                family.setLists(listsHelper);
                refFamily.child(familyName1).setValue(family);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.w("YourFamily", "Failed to read value.", databaseError.toException());
        }
    };
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem Item){
        final SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
        String st=Item.getTitle().toString();
        if(st.equals("family tasks")){
            Intent go = new Intent(this, Matalot.class);
            go.putExtra("a",settings.getString("currentFamily",""));
            startActivity(go);
        }
        if (st.equals("family lists")){
           Intent go = new Intent(this, Reshimot.class);
           go.putExtra("a",settings.getString("currentFamily",""));
           startActivity(go);
        }
        if (st.equals("log out")){
            SharedPreferences.Editor editor=settings.edit();
            editor.putBoolean("haveFamily",false);
            editor.putBoolean("stayConnect",false);
            editor.commit();
            Intent go = new Intent(this, Connect.class);
            startActivity(go);
        }
        if (st.equals("leave family")){
            Query query=refFamily.orderByChild("familyUname").equalTo(settings.getString("currentFamily",""));
            ValueEventListener leaveFamily=new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot data:dataSnapshot.getChildren()) {
                        Family family = data.getValue(Family.class);
                            ArrayList<User> UserValue2 = family.getUsers();
                            ArrayList<User> usersHelper = new ArrayList<>();
                            User user = null;
                            while (!UserValue2.isEmpty()) {
                                user = UserValue2.remove(0);
                                if (!user.getUid().equals(settings.getString("currentUser", ""))) {
                                    usersHelper.add(user);
                                }
                            }
                            family.setUsers(usersHelper);
                            if (family.getUsers().isEmpty()) {
                                refFamily.child(familyName1).removeValue();
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putBoolean("haveFamily", false);
                                editor.putString("currentFamily", "");
                                editor.commit();
                                returnToFamily(user);
                            }
                            else {
                                refFamily.child(familyName1).setValue(family);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putBoolean("haveFamily", false);
                                editor.putString("currentFamily", "");
                                editor.commit();
                                returnToFamily(user);
                            }
                        }
                    }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            query.addListenerForSingleValueEvent(leaveFamily);
        }
        if(st.equals("change family")){
            Query query=refFamily.orderByChild("familyUname").equalTo(settings.getString("currentFamily",""));
            ValueEventListener leaveFamily=new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot data:dataSnapshot.getChildren()){
                        Family family=data.getValue(Family.class);
                        User user = null;
                        while (!family.getUsers().isEmpty()) {
                            user = (User) family.getUsers().remove(0);
                            if (user.getUid().equals(settings.getString("currentUser",""))) {
                                returnToFamily(user);
                            }
                        }

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            query.addListenerForSingleValueEvent(leaveFamily);
        }
        if(st.equals("new requests")){
            Intent go=new Intent(this,Requests.class);
            go.putExtra("a",familyName1);
            startActivity(go);
        }
        return true;
    }
    public void returnToFamily(User user1){
        Intent getUser = new Intent(this, YourFamily.class);
        getUser.putExtra("a", user1.getUid());
        getUser.putExtra("b", user1.getName());
        getUser.putExtra("c", user1.getPassword());
        getUser.putExtra("d", user1.getEmail());
        startActivity(getUser);
    }
}
