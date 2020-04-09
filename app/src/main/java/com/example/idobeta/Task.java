package com.example.idobeta;

public class Task {
    String teur;
    String openDatime;
    String endDatime;
    String order;
    boolean taskDone;
    public Task(){}
    public Task(String teur,String openDatime,String endDatime,String order,boolean taskDone){
        this.teur=teur;
        this.openDatime=openDatime;
        this.endDatime=endDatime;
        this.order=order;
        this.taskDone=taskDone;
    }

    public String getTeur() {
        return teur;
    }

    public String getEndDatime() {
        return endDatime;
    }

    public String getOpenDatime() {
        return openDatime;
    }

    public String getOrder() {
        return order;
    }

    public boolean isTaskDone() {
        return taskDone;
    }

    public void setEndDatime(String endDatime) {
        this.endDatime = endDatime;
    }

    public void setOpenDatime(String openDatime) {
        this.openDatime = openDatime;
    }

    public void setTeur(String teur) {
        this.teur = teur;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setTaskDone(boolean taskDone) {
        this.taskDone = taskDone;
    }
}
