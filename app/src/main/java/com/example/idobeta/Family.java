package com.example.idobeta;

import java.util.ArrayList;

public class Family {
    String familyUname;
        String lastName;
        ArrayList<User> users;
        ArrayList<Task> tasks;
        ArrayList<NewList> lists;

        public Family(){}
        public Family(String lastName,String FamilyUname){
            this.lastName=lastName;
            this.familyUname=FamilyUname;
            this.users=new ArrayList<>();
            this.tasks=new ArrayList<>();
            this.lists=new ArrayList<>();
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

        public ArrayList<NewList> getLists() {
            return lists;
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

        public void addList(NewList list) {
            this.lists.add(list);
        }

        public void setTasks(ArrayList<Task> tasks) {
            this.tasks = tasks;
        }

    public void setLists(ArrayList<NewList> lists) {
        this.lists = lists;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
