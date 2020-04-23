package com.example.idobeta;

import java.util.ArrayList;

public class Family {
    String familyUname;
        String lastName;
        ArrayList<User> users;
        ArrayList<Task> tasks;
        ArrayList<NewList> lists;
        ArrayList<User> requests;
        int notificition;

        public Family(){}
    /**
     * class Family builder.
     * <p>
     * @param lastName,FamilyUname
     */
    public Family(String lastName,String FamilyUname,int notificition){
        this.lastName=lastName;
        this.familyUname=FamilyUname;
        this.users=new ArrayList<>();
        this.tasks=new ArrayList<>();
        this.lists=new ArrayList<>();
        this.requests=new ArrayList<>();
        this.notificition=notificition;
    }
    /**
     * getLastName.
     * return family's last name
     * <p>
     * @return lastName.
     */
        public String getLastName(){
            return this.lastName;
        }
    /**
     * getFamilyUname.
     * return family's user name.
     * <p>
     * @return familyUname.
     */
    public String getFamilyUname(){
            return this.familyUname;
        }
    /**
     * getUsers.
     * return family's members
     * <p>
     * @return users.
     */
    public ArrayList getUsers(){
            return this.users;
        }
    /**
     * getTasks.
     *return family's tasks
     * <p>
     * @return tasks.
     */
    public ArrayList<Task> getTasks() {
            return this.tasks;
        }
    /**
     * getLists.
     * return family's lists
     * <p>
     * @return lists.
     */
    public ArrayList<NewList> getLists() {
            return lists;
        }
    /**
     * getRequests.
     * return family's requests.
     * <p>
     * @return requests.
     */
    public ArrayList<User> getRequests() {
        return requests;
    }
    /**
     * addUser.
     * add user to users.
     * <p>
     * @param user
     */
    public void addUser(User user){
            this.users.add(user);
        }
    /**
     * setLastName.
     * change family's last name.
     * <p>
     * @param lastName
     */
    public void setLastName(String lastName){
            this.lastName=lastName;
        }
    /**
     * addTask.
     * add task to tasks.
     * <p>
     * @param task
     */
    public void addTask(Task task){
            this.tasks.add(task);
        }
    /**
     * addList.
     * add list to lists.
     * <p>
     * @param list
     */
    public void addList(NewList list) {
            this.lists.add(list);
        }
    /**
     * setTasks.
     * change family's tasks.
     * <p>
     * @param tasks
     */
    public void setTasks(ArrayList<Task> tasks) {
            this.tasks = tasks;
        }
    /**
     * setLists.
     *change family's lists.
     * <p>
     * @param lists
     */
    public void setLists(ArrayList<NewList> lists) {
        this.lists = lists;
    }
    /**
     * setUsers.
     * change family's users.
     * <p>
     * @param users
     */
    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
    /**
     * setRequests.
     * change family's requests.
     * <p>
     * @param requests
     */
    public void setRequests(ArrayList<User> requests) {
        this.requests = requests;
    }
    /**
     * addRequests.
     * add request to requests.
     * <p>
     * @param user
     */
    public void addRequest(User user){
            requests.add(user);
    }

    /**
     * get the flag number of the notification.
     * @return
     */
    public int getNotificition() {
        return notificition;
    }

    /**
     * change the notification number.
     * @param notificition
     */
    public void setNotificition(int notificition) {
        this.notificition = notificition;
    }
}
