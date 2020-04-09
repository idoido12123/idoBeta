package com.example.idobeta;

import java.util.ArrayList;

public class NewList {
    String listName;
    String datetime;
    boolean activ;
    ArrayList<Product> products;
    String listFamily;

    public NewList(){}
    public NewList(String listName, String datime,boolean activ,String listFamily){
        this.listName=listName;
        this.datetime=datime;
        this.activ=activ;
        this.products=new ArrayList<Product>();
        this.listFamily=listFamily;
    }
    public String getListName(){
        return this.listName;
    }

    public String getDatetime(){
        return this.datetime;
    }
    public boolean getActiv(){
        return this.activ;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public String getListFamily() {
        return this.listFamily;
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
    public void setActiv(boolean activ){
        this.activ=activ;
    }

    public void setListFamily(String listFamily) {
        this.listFamily = listFamily;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
