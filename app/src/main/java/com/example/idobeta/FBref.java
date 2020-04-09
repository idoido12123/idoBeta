package com.example.idobeta;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FBref {
    public static FirebaseDatabase FBDB=FirebaseDatabase.getInstance();
    public static DatabaseReference refUsers=FBDB.getReference("Users");
    public static DatabaseReference refFamily=FBDB.getReference("Family");
    public static DatabaseReference refShopLists=FBDB.getReference("ShopList");
    public static DatabaseReference refTasks=FBDB.getReference("Tasks");
    public static DatabaseReference refProducts=FBDB.getReference("products");

}
