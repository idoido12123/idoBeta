package com.example.idobeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.idobeta.FBref.refFamily;

public class Requests extends AppCompatActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{
    ListView requests;
    String familyName;
    ArrayList<User> requestsValues = new ArrayList<>();
    ArrayList<String> requestsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        requests = (ListView) findViewById(R.id.requests);
        Intent get = getIntent();
        familyName = get.getExtras().getString("a");
        Query query = refFamily.orderByChild("familyUname").equalTo(familyName);
        ValueEventListener newRequests = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requestsValues.clear();
                requestsList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Family family = data.getValue(Family.class);
                    requestsValues = family.getRequests();
                    while (!requestsValues.isEmpty()) {
                        User user = requestsValues.remove(0);
                        if(!user.getName().equals("")) {
                            requestsList.add(user.getName());
                        }
                    }
                }
                ArrayAdapter<String> adp;
                adp = new ArrayAdapter<String>(Requests.this, R.layout.support_simple_spinner_dropdown_item, requestsList);
                requests.setAdapter(adp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        query.addValueEventListener(newRequests);
        requests.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        requests.setOnItemClickListener(this);
        requests.setOnItemLongClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
        Query query = refFamily.orderByChild("familyUname").equalTo(familyName);
        ValueEventListener addUser = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Family family = data.getValue(Family.class);
                    User user = null;
                    ArrayList<User> familyRequests = new ArrayList<>();
                    while (!family.getRequests().isEmpty()) {
                        user = family.getRequests().remove(0);
                        if (!user.getName().equals(requests.getItemAtPosition(i))) {
                            familyRequests.add(user);
                        }
                        else {
                            family.addUser(user);
                        }
                    }
                    family.setRequests(familyRequests);
                    refFamily.child(familyName).setValue(family);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        query.addListenerForSingleValueEvent(addUser);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        final String userHelper= (String) requests.getItemAtPosition(i);
        Query query=refFamily.orderByChild("familyUname").equalTo(familyName);
        ValueEventListener ignore=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Family family = data.getValue(Family.class);
                    ArrayList<User> RequestValue = family.getRequests();
                    ArrayList<User> RequestsHelper = new ArrayList<>();
                    RequestsHelper.add(RequestValue.remove(0));
                    while (!RequestValue.isEmpty()) {
                        User user = RequestValue.remove(0);
                        if (!user.getName().equals(userHelper)) {
                            RequestsHelper.add(user);
                        }
                    }
                    family.setRequests(RequestsHelper);
                    refFamily.child(familyName).setValue(family);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        query.addListenerForSingleValueEvent(ignore);
        return true;
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.yourFamilies).setVisible(false);
        menu.findItem(R.id.leaveFamily).setVisible(false);
        menu.findItem(R.id.logOut).setVisible(false);
        menu.findItem(R.id.requests).setVisible(false);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem Item) {
        final SharedPreferences settings = getSharedPreferences("PREFS_NAME", MODE_PRIVATE);
        String st = Item.getTitle().toString();
        if (st.equals("family tasks")) {
            Intent go = new Intent(this, Matalot.class);
            go.putExtra("a", settings.getString("currentFamily", ""));
            startActivity(go);
        }
        if (st.equals("family lists")){
            Intent go = new Intent(this, Reshimot.class);
            go.putExtra("a",settings.getString("currentFamily",""));
            startActivity(go);
        }
        return true;
    }
}
