package com.example.idobeta;

public class Product {
    String Nproduct;
    String camut;;
    String Norder;
    public Product(){}
    /**
     * class Product builder.
     * <p>
     * @param Nproduct,camut,Norder
     */
    public Product(String Nproduct, String camut,String Norder){
        this.Nproduct=Nproduct;
        this.camut=camut;
        this.Norder=Norder;
    }
    /**
     * getNproduct.
     * return product's name
     * <p>
     * @return Nproduct.
     */
    public String getNproduct(){
        return this.Nproduct;
    }
    /**
     * getNorder.
     * return product's order
     * <p>
     * @return Norder.
     */
    public String getNorder(){
        return this.Norder;
    }
    /**
     * getCamut.
     * return product's last name
     * <p>
     * @return camut.
     */
    public String getCamut() {
        return this.camut;
    }
    /**
     * setNproduct.
     * change product's name.
     * <p>
     * @param Nproduct
     */
    public void setNproduct(String Nproduct){
        this.Nproduct=Nproduct;
    }
    /**
     * setCamut.
     * change product's amount.
     * <p>
     * @param camut
     */
    public void setCamut(String camut){
        this.camut=camut;
    }
    /**
     * setNorder.
     * change product's order.
     * <p>
     * @param Norder
     */
    public void setNorder(String Norder) {
        this.Norder = Norder;
    }
}
