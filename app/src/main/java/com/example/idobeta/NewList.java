package com.example.idobeta;

import java.util.ArrayList;

public class NewList {
    String listName;
    String datetime;
    boolean active;
    ArrayList<Product> products;

    public NewList(){}
    public NewList(String listName, String datime,boolean active){
        this.listName=listName;
        this.datetime=datime;
        this.active=active;
        this.products=new ArrayList<Product>();
    }
    public String getListName(){
        return this.listName;
    }

    public String getDatetime(){
        return this.datetime;
    }
    public boolean getActive(){
        return this.active;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product){
        products.add(product);
    }
    public void setListName(String listName){
        this.listName=listName;
    }
    public void setDatetime(String date,String time){
        this.datetime=date+" "+time;
    }
    public void setActive(boolean active){
        this.active=active;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
