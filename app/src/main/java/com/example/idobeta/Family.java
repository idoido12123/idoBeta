package com.example.idobeta;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

    public class Family {
        String familyUname;
        String lastName;
        ArrayList<User> users;

        public Family(){}
        public Family(String lastName,String FamilyUname){
            this.lastName=lastName;
            this.familyUname=FamilyUname;
            this.users=new ArrayList<>();
        }
        public String getLastName(){
            return this.lastName;
        }

        public String getFamilyUname(){
            return this.familyUname;
        }
        public ArrayList getUsers(){
            return this.users;
        }

        void addUser(User user){
            this.users.add(user);
        }
        public void setLastName(String lastName){
            this.lastName=lastName;
        }
       // public void setDate(String date){
           // this.date=date;
       // }
    }
