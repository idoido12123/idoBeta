package com.example.idobeta;

import java.util.ArrayList;

public class NewList {
    String listName;
    String datetime;
    ArrayList<Product> products;

    public NewList(){}
    /**
     * class NewList builder.
     * <p>
     * @param listName,datime
     */
    public NewList(String listName, String datime){
        this.listName=listName;
        this.datetime=datime;
        this.products=new ArrayList<Product>();
    }
    /**
     * getNproduct.
     * return list's name.
     * <p>
     * @return Nproduct.
     */
    public String getListName(){
        return this.listName;
    }
    /**
     * getNproduct.
     * return list's usage date and time.
     * <p>
     * @return Nproduct.
     */
    public String getDatetime(){
        return this.datetime;
    }
    /**
     * getNproduct.
     * return list's products.
     * <p>
     * @return Nproduct.
     */
    public ArrayList<Product> getProducts() {
        return products;
    }
    /**
     * addProduct.
     * add product to list.
     * <p>
     * @param product
     */
    public void addProduct(Product product){
        products.add(product);
    }
    /**
     * setListName.
     * change list's name.
     * <p>
     * @param listName
     */
    public void setListName(String listName){
        this.listName=listName;
    }
    /**
     * setDateTime.
     * change list's usage date and time.
     * <p>
     * @param date,time
     */
    public void setDatetime(String date,String time){
        this.datetime=date+" "+time;
    }
    /**
     * setProducts.
     * change list's products.
     * <p>
     * @param products
     */
    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
