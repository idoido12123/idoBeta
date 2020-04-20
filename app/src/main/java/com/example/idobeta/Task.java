package com.example.idobeta;

public class Task {
    String teur;
    String openDatime;
    String endDatime;
    String order;
    boolean active;
    public Task(){}
    /**
     * class Task builder.
     * <p>
     * @param teur,openDatime,endDatime,order,active
     */
    public Task(String teur,String openDatime,String endDatime,String order,boolean active){
        this.teur=teur;
        this.openDatime=openDatime;
        this.endDatime=endDatime;
        this.order=order;
        this.active=active;
    }
    /**
     * getTeur.
     * return task's description
     * <p>
     * @return teur.
     */
    public String getTeur() {
        return teur;
    }
    /**
     * getEndDatime.
     * return task's end date and time.
     * <p>
     * @return endDatime.
     */
    public String getEndDatime() {
        return endDatime;
    }
    /**
     * getOpenDatime.
     * return task's open date and time.
     * <p>
     * @return openDatime.
     */
    public String getOpenDatime() {
        return openDatime;
    }
    /**
     * getOrder.
     * return task's order
     * <p>
     * @return order.
     */
    public String getOrder() {
        return order;
    }
    /**
     * isActive.
     * return if task is active or not.
     * <p>
     * @return active.
     */
    public boolean isActive() {
        return active;
    }
    /**
     * setEndDatime.
     * change user's email
     * <p>
     * @param endDatime
     */
    public void setEndDatime(String endDatime) {
        this.endDatime = endDatime;
    }
    /**
     * setOpenDatime.
     * change task's open date and time.
     * <p>
     * @param openDatime
     */
    public void setOpenDatime(String openDatime) {
        this.openDatime = openDatime;
    }
    /**
     * setTeur.
     * change task's description.
     * <p>
     * @param teur
     */
    public void setTeur(String teur) {
        this.teur = teur;
    }
    /**
     * setOrder.
     * change user's email
     * <p>
     * @param order
     */
    public void setOrder(String order) {
        this.order = order;
    }
    /**
     * setActive.
     * change user's email
     * <p>
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }
}
