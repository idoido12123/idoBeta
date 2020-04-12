package com.example.idobeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import static com.example.idobeta.FBref.refFamily;
import static com.example.idobeta.FBref.refShopLists;

public class Reshimot extends AppCompatActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {
    AlertDialog.Builder addList;
    LinearLayout listDialog;
    EditText Nreshima;
    EditText ListDate;
    EditText ListTime;
    Date date = new Date();
    Button NewList;
    ListView BigList;
    Intent t4;
    NewList reshima1;
    String familyName1;
    ArrayList<String> ShopList = new ArrayList<>();
    ArrayList<NewList> ShopValues = new ArrayList<>();
    boolean flag=false;
    String listNhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reshimot);
        NewList = (Button) findViewById(R.id.NewList);
        BigList = (ListView) findViewById(R.id.ListOfLists);
        t4=getIntent();
        familyName1=t4.getExtras().getString("a");
        Query query=refFamily.orderByChild("familyUname").equalTo(familyName1);
        ValueEventListener shopListListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                ShopValues.clear();
                ShopList.clear();
                for (DataSnapshot data : ds.getChildren()) {
                    Family family=data.getValue(Family.class);
                    ShopValues=family.getLists();
                    while (!ShopValues.isEmpty()) {
                        NewList list = ShopValues.remove(0);
                        ShopList.add(list.getListName());
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
                Query query=refFamily.orderByChild("familyUname").equalTo(familyName1);
                ValueEventListener addList = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot ds) {
                        for (DataSnapshot data:ds.getChildren()){
                            Family family = data.getValue(Family.class);
                            NewList list = new NewList(Lname, Ldatime,true);
                            Product product=new Product("","","");
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
        chosenList.putExtra("b",familyName1);
        startActivity(chosenList);
    }
    public void click6(View view) {
        Intent t5 = new Intent(this, Matalot.class);
        t5.putExtra("a",familyName1);
        startActivity(t5);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        listNhelper=(String) BigList.getItemAtPosition(i);
        Query query = refFamily.orderByChild("familyUname").equalTo(familyName1);
        query.addListenerForSingleValueEvent(Fvel);
        return true;
    }

    com.google.firebase.database.ValueEventListener Fvel = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                Family family=data.getValue(Family.class);
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
}
