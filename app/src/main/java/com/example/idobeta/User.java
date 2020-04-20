package com.example.idobeta;


public class User {
    String uid;
    String name;
    String email;
    String password;
    public User(){}
    /**
     * class User builder.
     * <p>
     * @param	uid,name,password,email.
     */
    public User(String uid,String name,String password,String email){
        this.uid=uid;
        this.name=name;
        this.password=password;
        this.email=email;
    }
    /**
     * getUid.
     * <p>
     * @return uid.
     */

    public String getUid(){
        return  this.uid;
    }
    /**
     * getName.
     * <p>
     * @return name.
     */
    public String getName(){
        return this.name;
    }
    /**
     * getEmail.
     * <p>
     * @return email.
     */
    public String getEmail() {
        return email;
    }
    /**
     * getPassword.
     * <p>
     * @return password.
     */
    public String getPassword() {
        return password;
    }
    /**
     * setEmail.
     * change user's email
     * <p>
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * setPassword.
     * change user's password.
     * <p>
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * setUid.
     * change user's Uid.
     * <p>
     * @param uid
     */
    public void setUid(String uid){
        this.uid=uid;
    }
    /**
     * setName.
     * change user's name.
     * <p>
     * @param name
     */
    public void setName(String name){
        this.name=name;
    }
}
