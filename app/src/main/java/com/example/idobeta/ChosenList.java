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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;
import static com.example.idobeta.FBref.refShopLists;

public class ChosenList extends AppCompatActivity implements AdapterView.OnItemLongClickListener {
    AlertDialog.Builder addProduct1;
    LinearLayout productDialog;
    Button addProduct;
    ListView products;
    TextView title;
    String lName;
    Intent getList1;
    ArrayList<Product> productsValues = new ArrayList<>();
    ArrayList<String> productsList = new ArrayList<>();
    EditText productName;
    EditText productAmount;
    EditText orderName;
    NewList list;
    int count=0;
    Product product;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_list);
        addProduct = (Button) findViewById(R.id.addProduct);
        products = (ListView) findViewById(R.id.products);
        title = (TextView) findViewById(R.id.title);
        getList1 = getIntent();
        lName = getList1.getExtras().getString("a");
        title.setText(lName);
         Query query = refShopLists.orderByChild("listName").equalTo(lName);
         ValueEventListener products1 = new ValueEventListener() {
             @Override

         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            productsValues.clear();
            productsList.clear();
               for (DataSnapshot data : dataSnapshot.getChildren()) {
                   list = data.getValue(NewList.class);
                   productsValues=list.getProducts();
               }
         while(!productsValues.isEmpty()){
             Product product=productsValues.remove(0);
             if(product.getNorder().equals("")) {
                 productsList.add("");
             }
             else{
                 productsList.add(product.getNproduct()+" * "+product.getCamut()+" , order: "+product.getNorder());
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
}
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
    DialogInterface.OnClickListener OKclick1 = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (i == DialogInterface.BUTTON_POSITIVE) {
                final String Pname = productName.getText().toString();
                final String Pamount =productAmount.getText().toString();
                Query query = refShopLists.orderByChild("listName").equalTo(lName);
                ValueEventListener addProduct = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                           NewList list1=data.getValue(NewList.class);
                           Product product1=new Product(Pname,Pamount,orderName.getText().toString());
                           list1.addProduct(product1);
                           refShopLists.child(list1.getListName()).setValue(list1);
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

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, final long l) {
        final Query query = refShopLists.orderByChild("listName").equalTo(lName);
        final ValueEventListener deleteProduct = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productsValues.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    NewList list1=data.getValue(NewList.class);
                    productsValues=list1.getProducts();
                    ArrayList productsValues2=list1.getProducts();
                    while (!productsValues.isEmpty()){
                        product=productsValues.remove(0);
                        String str=product.getNproduct()+" * "+product.getCamut()+" , : "+product.getNorder();
                        if(str.equals(products.getItemAtPosition(i))){
                            list1.getProducts().remove(count);
                        }
                        count++;
                    }
                    refShopLists.child(list1.getListName()).setValue(list1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        query.addListenerForSingleValueEvent(deleteProduct);
        return true;
    }
}
