package com.example.idobeta;

public class User {
    String uid;
    String name;
    String email;
    String password;
    public User(){}
    public User(String uid,String name,String password,String email){
        this.uid=uid;
        this.name=name;
        this.password=password;
        this.email=email;
    }
    public String getUid(){
        return  this.uid;
    }
    public String getName(){
        return this.name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUid(String uid){
        this.uid=uid;
    }
    public void setName(String name){
        this.name=name;
    }
}
