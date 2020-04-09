package com.example.idobeta;

public class Product {
    String Nproduct;
    String camut;;
    String Norder;

    public Product(){}
    public Product(String Nproduct, String camut,String Norder){
        this.Nproduct=Nproduct;
        this.camut=camut;
        this.Norder=Norder;
    }
    public String getNproduct(){
        return this.Nproduct;
    }

    public String getNorder(){
        return this.Norder;
    }
    public String getCamut() {
        return this.camut;
    }
    public void setNproduct(String Nproduct){
        this.Nproduct=Nproduct;
    }
    public void setCamut(String camut){
        this.camut=camut;
    }

    public void setNorder(String Norder) {
        this.Norder = Norder;
    }
}
