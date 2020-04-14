package com.example.idobeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
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

public class Requests extends AppCompatActivity implements AdapterView.OnItemClickListener {
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
                        requestsList.add(user.getName());
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
}
