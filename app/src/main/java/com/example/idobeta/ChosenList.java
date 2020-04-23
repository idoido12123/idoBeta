package com.example.idobeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.idobeta.FBref.refFamily;
import static com.example.idobeta.FBref.refShopLists;

public class ChosenList extends AppCompatActivity implements AdapterView.OnItemLongClickListener,AdapterView.OnItemClickListener {
    AlertDialog.Builder addProduct1;
    AlertDialog.Builder changeAmount;
    AlertDialog.Builder editList1;
    LinearLayout editListDialog;
    LinearLayout productDialog;
    LinearLayout changeAmountDialog;
    Button addProduct;
    ListView products;
    TextView title;
    String lName;
    String familyName;
    Intent getList1;
    ArrayList<Product> productsValues = new ArrayList<>();
    ArrayList<String> productsList = new ArrayList<>();
    EditText productName;
    EditText productAmount;
    EditText orderName;
    EditText newAmount;
    EditText editListName;
    String date="";
    ArrayList<NewList>shopValues=new ArrayList<>();
    ArrayList<NewList>listsHelper=new ArrayList<>();
    ArrayList<Product>productsHelper=new ArrayList<>();
    Button editList;
    int position;
    boolean flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_list);
        addProduct = (Button) findViewById(R.id.addProduct);
        products = (ListView) findViewById(R.id.products);
        title = (TextView) findViewById(R.id.title);
        editList=(Button)findViewById(R.id.editList);
        getList1 = getIntent();
        lName = getList1.getExtras().getString("a");
        familyName=getList1.getExtras().getString("b");
         Query query = refFamily.orderByChild("familyUname").equalTo(familyName);
         ValueEventListener products1 = new ValueEventListener() {
             @Override

         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 productsValues.clear();
                 productsList.clear();
                 shopValues.clear();
                 for (DataSnapshot data : dataSnapshot.getChildren()) {
                     Family family = data.getValue(Family.class);
                     shopValues = family.getLists();
                     while (!shopValues.isEmpty()) {
                         NewList list = shopValues.remove(0);
                         if (list.getListName().equals(lName)) {
                             date=list.getDatetime();
                             title.setText(lName + " " + date);
                             productsValues = list.getProducts();
                             while (!productsValues.isEmpty()) {
                                 Product product = productsValues.remove(0);
                                 if (!product.getNproduct().equals("")) {
                                     productsList.add(product.getNproduct() + " * " + product.getCamut() + " , order : " + product.getNorder());
                                 }
                             }
                         }
                     }
                 }
                 ArrayAdapter<String> adp = new ArrayAdapter<String>(ChosenList.this, R.layout.support_simple_spinner_dropdown_item, productsList);
                 products.setAdapter(adp);
             }

      @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
      };
       query.addValueEventListener(products1);
        products.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        products.setOnItemLongClickListener(this);
        products.setOnItemClickListener(this);
}

    /**
     * start "add new product" alert dialog.
     * @param view
     */
    public void newProduct(View view) {
        productDialog = (LinearLayout) getLayoutInflater().inflate(R.layout.add_product, null);
        productName = (EditText) productDialog.findViewById(R.id.productName);
        productAmount = (EditText) productDialog.findViewById(R.id.productAmount);
        orderName = (EditText) productDialog.findViewById(R.id.orderName);
        addProduct1 = new AlertDialog.Builder(this);
        addProduct1.setView(productDialog);
        addProduct1.setTitle("add new product");
        addProduct1.setPositiveButton("OK", OKclick1);
        addProduct1.show();
    }

    /**
     * add new product to the list.
     */
    DialogInterface.OnClickListener OKclick1 = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (i == DialogInterface.BUTTON_POSITIVE) {
                final String Pname = productName.getText().toString();
                final String Pamount =productAmount.getText().toString();
                final String orderName1=orderName.getText().toString();
                Query query = refFamily.orderByChild("familyUname").equalTo(familyName);
                ValueEventListener addProduct = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        shopValues.clear();
                        productsValues.clear();
                        listsHelper.clear();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Family family = data.getValue(Family.class);
                            shopValues=family.getLists();
                            while (!shopValues.isEmpty()){
                                NewList list=shopValues.remove(0);
                                if(list.getListName().equals(lName)){
                                   Product product=new Product(Pname,Pamount,orderName1);
                                   list.addProduct(product);
                                }
                                listsHelper.add(list);
                            }
                            family.setLists(listsHelper);
                            refFamily.child(familyName).setValue(family);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                query.addListenerForSingleValueEvent(addProduct);
            }
        }
    };

    /**
     * delete product from the list.
     * @param adapterView
     * @param view
     * @param i
     * @param l
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, final long l) {
         Query query = refFamily.orderByChild("familyUname").equalTo(familyName);
         ValueEventListener deleteProduct = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shopValues.clear();
                productsValues.clear();
                listsHelper.clear();
                productsHelper.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Family family = data.getValue(Family.class);
                    shopValues=family.getLists();
                    while (!shopValues.isEmpty()){
                        NewList list=shopValues.remove(0);
                        if(list.getListName().equals(lName)) {
                            while (!list.getProducts().isEmpty()) {
                                Product product = list.getProducts().remove(0);
                                String str = product.getNproduct() + " * " + product.getCamut() + " , order : " + product.getNorder();
                                if (!str.equals(products.getItemAtPosition(i))) {
                                    productsHelper.add(product);
                                }
                            }
                            list.setProducts(productsHelper);
                        }
                        listsHelper.add(list);
                    }
                    family.setLists(listsHelper);
                    refFamily.child(familyName).setValue(family);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        query.addListenerForSingleValueEvent(deleteProduct);
        return true;
    }

    /**
     * start "chnge amount" alert dialog and change the amount"
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        position=i;
        changeAmountDialog=(LinearLayout) getLayoutInflater().inflate(R.layout.new_amount,null);
        newAmount = (EditText) changeAmountDialog.findViewById(R.id.newAmount);
        changeAmount = new AlertDialog.Builder(this);
        changeAmount.setView(changeAmountDialog);
        changeAmount.setTitle("change amount");
        changeAmount.setPositiveButton("OK", OKclick2);
        changeAmount.show();
    }
    DialogInterface.OnClickListener OKclick2 = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, final int i) {
            if (i == DialogInterface.BUTTON_POSITIVE) {
                final String newAmount1 = newAmount.getText().toString();
                Query query = refFamily.orderByChild("familyUname").equalTo(familyName);
                ValueEventListener Namount=new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        shopValues.clear();
                        productsValues.clear();
                        listsHelper.clear();
                        productsHelper.clear();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Family family = data.getValue(Family.class);
                            shopValues=family.getLists();
                            while (!shopValues.isEmpty()){
                                NewList list=shopValues.remove(0);
                                if(list.getListName().equals(lName)) {
                                    while (!list.getProducts().isEmpty()) {
                                        Product product = list.getProducts().remove(0);
                                        String str = product.getNproduct() + " * " + product.getCamut() + " , order : " + product.getNorder();
                                        if (!str.equals(products.getItemAtPosition(position))) {
                                            productsHelper.add(product);
                                        }
                                        else {
                                            product.setCamut(newAmount1);
                                            productsHelper.add(product);
                                        }
                                    }
                                    list.setProducts(productsHelper);
                                }
                                listsHelper.add(list);
                            }
                            family.setLists(listsHelper);
                            refFamily.child(familyName).setValue(family);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                };
                query.addListenerForSingleValueEvent(Namount);
            }
        }
    };

    /**
     * start "remove list" alert dialog.
     * @param view
     */
    public void removeProducts(View view){
        editListDialog = (LinearLayout) getLayoutInflater().inflate(R.layout.edit_list, null);
        editListName = (EditText) editListDialog.findViewById(R.id.editName);
        addProduct1 = new AlertDialog.Builder(this);
        addProduct1.setView(editListDialog);
        addProduct1.setTitle("remove list");
        addProduct1.setPositiveButton("OK", OKclick3);
        addProduct1.show();
    }

    /**
     * remove the list's products to another list.
     */
    DialogInterface.OnClickListener OKclick3 = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (i == DialogInterface.BUTTON_POSITIVE) {
                final String NLname = editListName.getText().toString();
                Query query = refFamily.orderByChild("familyUname").equalTo(familyName);
                ValueEventListener removeList = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        shopValues.clear();
                        productsValues.clear();
                        listsHelper.clear();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Family family = data.getValue(Family.class);
                            shopValues = family.getLists();
                            ArrayList<NewList> listsHelper2 = new ArrayList<>();
                            while (!shopValues.isEmpty()) {
                                NewList list = shopValues.remove(0);
                                if (list.getListName().equals(lName)) {
                                    Product product = list.getProducts().remove(0);
                                    while (!list.getProducts().isEmpty()) {
                                        productsValues.add(list.getProducts().remove(0));
                                    }
                                    list.addProduct(product);
                                }
                                listsHelper2.add(list);
                            }
                            while (!listsHelper2.isEmpty()) {
                                NewList list1 = listsHelper2.remove(0);
                                if (list1.getListName().equals(NLname)) {
                                    flag = true;
                                    while (!productsValues.isEmpty()) {
                                        list1.getProducts().add(productsValues.remove(0));
                                    }
                                }
                                listsHelper.add(list1);
                            }
                            if (flag==true) {
                                family.setLists(listsHelper);
                                refFamily.child(familyName).setValue(family);
                            }
                            if(flag==false) {
                                Toast.makeText(ChosenList.this, "list don't exist", Toast.LENGTH_SHORT).show();
                            }
                            flag=false;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                query.addListenerForSingleValueEvent(removeList);
            }
        }
    };
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.yourFamilies).setVisible(false);
        menu.findItem(R.id.leaveFamily).setVisible(false);
        menu.findItem(R.id.logOut).setVisible(false);
        menu.findItem(R.id.requests).setVisible(false);
        return true;
    }

    /**
     * start menu.
     * @param Item
     * @return
     */
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
        return true;
    }
}
