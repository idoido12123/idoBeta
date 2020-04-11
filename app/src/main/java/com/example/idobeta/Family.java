package com.example.idobeta;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

    public class Family {
        String familyUname;
        String lastName;
        ArrayList<User> users;
        ArrayList<Task> tasks;

        public Family(){}
        public Family(String lastName,String FamilyUname){
            this.lastName=lastName;
            this.familyUname=FamilyUname;
            this.users=new ArrayList<>();
            this.tasks=new ArrayList<>();
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

        public ArrayList<Task> getTasks() {
            return this.tasks;
        }

        void addUser(User user){
            this.users.add(user);
        }
        public void setLastName(String lastName){
            this.lastName=lastName;
        }
        public void addTask(Task task){
            this.tasks.add(task);
        }

        public void setTasks(ArrayList<Task> tasks) {
            this.tasks = tasks;
        }
    }
