package com.example.idobeta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.idobeta.App.CHANNEL_ID;
import static com.example.idobeta.FBref.refFamily;
import static com.example.idobeta.FBref.refTasks;

public class Matalot extends AppCompatActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {
    final String CHANNEL_ID = "new notification";
    final int NOTIFICATION_ID = 001;
    AlertDialog.Builder addTask;
    AlertDialog.Builder showTask;
    LinearLayout TaskDialog;
    LinearLayout showTaskDialog;
    EditText Ntask;
    EditText startDate;
    EditText startTime;
    EditText endDate;
    EditText endTime;
    EditText Norder;
    Switch active;
    Button NewTask;
    ListView tasks;
    String taskHelp;
    ArrayList<String> TaskList = new ArrayList<>();
    ArrayList<Task> TaskValues = new ArrayList<Task>();
    TextView startT;
    TextView endT;
    TextView taskO;
    String familyUname2;
    Family family1;
    Family family2;
    String input;
    String taskDes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
        setContentView(R.layout.activity_matalot);
        NewTask = (Button) findViewById(R.id.newTask);
        tasks = (ListView) findViewById(R.id.tasks);
        familyUname2 = settings.getString("currentFamily","");
        Query query = refFamily.orderByChild("familyUname").equalTo(familyUname2);
        ValueEventListener TasksListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                TaskValues.clear();
                TaskList.clear();
                for (DataSnapshot data : ds.getChildren()) {
                    Family family = data.getValue(Family.class);
                    TaskValues = family.getTasks();
                    while (!TaskValues.isEmpty()) {
                        Task task1 = TaskValues.remove(0);
                        if (!task1.getTeur().equals("")) {
                            TaskList.add(task1.getTeur());
                        }
                    }
                }

                ArrayAdapter<String> adp = new ArrayAdapter<String>(Matalot.this, R.layout.support_simple_spinner_dropdown_item, TaskList);
                tasks.setAdapter(adp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Matalot", "Failed to read value.", databaseError.toException());
            }
        };
        query.addValueEventListener(TasksListener);
        tasks.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        tasks.setOnItemClickListener(this);
        tasks.setOnItemLongClickListener(this);
    }

    public void newTask(View view) {
        TaskDialog = (LinearLayout) getLayoutInflater().inflate(R.layout.new_task, null);
        Ntask = (EditText) TaskDialog.findViewById(R.id.taskDes);
        startDate = (EditText) TaskDialog.findViewById(R.id.startDate);
        startTime = (EditText) TaskDialog.findViewById(R.id.startTime);
        endDate = (EditText) TaskDialog.findViewById(R.id.endDate);
        endTime = (EditText) TaskDialog.findViewById(R.id.endTime);
        Norder = (EditText) TaskDialog.findViewById(R.id.orderName);
        addTask = new AlertDialog.Builder(this);
        addTask.setView(TaskDialog);
        addTask.setTitle("Create new task");
        addTask.setPositiveButton("OK", OKclick);
        addTask.show();
    }
        DialogInterface.OnClickListener OKclick = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == DialogInterface.BUTTON_POSITIVE) {
                    final String Tdes = Ntask.getText().toString();
                    input = Ntask.getText().toString();
                    final String TSdatime = startDate.getText().toString() + " " + startTime.getText().toString();
                    final String TEdatime = endDate.getText().toString() + " " + endTime.getText().toString();
                    final String TOname = Norder.getText().toString();
                    Query query = refFamily.orderByChild("familyUname").equalTo(familyUname2);
                    ValueEventListener addTask = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                Family family = data.getValue(Family.class);
                                Task task = new Task(Tdes, TSdatime, TEdatime, TOname, true,false);
                                family.addTask(task);
                                family.setNotificition(1);
                                moveToService(1);
                                refFamily.child(familyUname2).setValue(family);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    query.addListenerForSingleValueEvent(addTask);

                }
            }
        };

    public void moveToService(int i) {
        Intent serviceIntent = new Intent(this, ExmpleService.class);
        serviceIntent.putExtra("a",i);
        serviceIntent.putExtra("b",taskDes);
        startService(serviceIntent);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
        showTaskDialog = (LinearLayout) getLayoutInflater().inflate(R.layout.show_task, null);
        startT = (TextView) showTaskDialog.findViewById(R.id.openDatime);
        endT = (TextView) showTaskDialog.findViewById((R.id.endDatime));
        taskO = (TextView) showTaskDialog.findViewById(R.id.taskOrder);
        active = (Switch) showTaskDialog.findViewById(R.id.active);
        showTask = new AlertDialog.Builder(this);
        Query query = refFamily.orderByChild("familyUname").equalTo(familyUname2);
        ValueEventListener showTaskL = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    family1 = data.getValue(Family.class);
                }
                ArrayList<Task> taskValue = family1.getTasks();
                Task removeTask = (taskValue.remove(0));
                while (!taskValue.isEmpty()) {
                    Task task2 = taskValue.remove(0);
                    if (task2.getTeur().equals((String) tasks.getItemAtPosition(i))) {
                        showTask.setView(showTaskDialog);
                        showTask.setTitle((String) tasks.getItemAtPosition(i));
                        startT.setText("start date and time : " + task2.getOpenDatime());
                        endT.setText("end date and time : " + task2.getEndDatime());
                        taskO.setText("task order : " + task2.getOrder());
                        if (task2.isActive()) {
                            active.setChecked(true);
                            active.setText("active");
                            active.setTextColor(Color.GREEN);
                        } else {
                            active.setChecked(false);
                            active.setText(" not active");
                            active.setTextColor(Color.RED);
                        }
                        showTask.show();
                        taskHelp = task2.getTeur();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        query.addListenerForSingleValueEvent(showTaskL);
    }

    public void active1(View view) {
        if (active.isChecked()) {
            final SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
            final SharedPreferences.Editor editor =settings.edit();
            active.setText("active");
            active.setTextColor(Color.GREEN);
            Query query = refFamily.orderByChild("familyUname").equalTo(familyUname2);
            ValueEventListener changeActive = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        family2 = data.getValue(Family.class);
                    }
                    ArrayList<Task> taskValue2 = family2.getTasks();
                    ArrayList<Task> tasksHelper = new ArrayList<>();
                    tasksHelper.add(taskValue2.remove(0));
                    while (!taskValue2.isEmpty()) {
                        Task task = taskValue2.remove(0);
                        if (task.getTeur().equals(taskHelp)) {
                            task.setCurrentActive(true);
                            task.setActive(true);
                        }
                        tasksHelper.add(task);
                    }
                    family2.setTasks(tasksHelper);
                    family2.setNotificition(2);
                    refFamily.child(familyUname2).setValue(family2);
                    moveToService(2);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            query.addListenerForSingleValueEvent(changeActive);
        } else {
            active.setText("not active");
            active.setTextColor(Color.RED);
            Query query = refFamily.orderByChild("familyUname").equalTo(familyUname2);
            ValueEventListener changeActive = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        family2 = data.getValue(Family.class);
                    }
                    ArrayList<Task> taskValue2 = family2.getTasks();
                    ArrayList<Task> tasksHelper = new ArrayList<>();
                    tasksHelper.add(taskValue2.remove(0));
                    while (!taskValue2.isEmpty()) {
                        Task task = taskValue2.remove(0);
                        if (task.getTeur().equals(taskHelp)) {
                            task.setActive(false);
                        }
                        tasksHelper.add(task);
                    }
                    family2.setTasks(tasksHelper);
                    family2.setNotificition(0);
                    refFamily.child(familyUname2).setValue(family2);
                    moveToService(0);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            query.addListenerForSingleValueEvent(changeActive);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        final String taskHelper2 = (String) tasks.getItemAtPosition(i);
        Query query = refFamily.orderByChild("familyUname").equalTo(familyUname2);
        ValueEventListener removeValue = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Family family = data.getValue(Family.class);
                    ArrayList<Task> taskValue2 = family.getTasks();
                    ArrayList<Task> tasksHelper = new ArrayList<>();
                    tasksHelper.add(taskValue2.remove(0));
                    while (!taskValue2.isEmpty()) {
                        Task task = taskValue2.remove(0);
                        if (!task.getTeur().equals(taskHelper2)) {
                            tasksHelper.add(task);
                        }
                    }
                    family.setTasks(tasksHelper);
                    refFamily.child(familyUname2).setValue(family);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        query.addListenerForSingleValueEvent(removeValue);
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem Item) {
        final SharedPreferences settings = getSharedPreferences("PREFS_NAME", MODE_PRIVATE);
        String st = Item.getTitle().toString();
        if (st.equals("family tasks")) {
            Intent go = new Intent(this, Matalot.class);
            go.putExtra("a", settings.getString("currentFamily", ""));
            startActivity(go);
        }
        if (st.equals("family lists")) {
            Intent go = new Intent(this, Reshimot.class);
            go.putExtra("a", settings.getString("currentFamily", ""));
            startActivity(go);
        }
        if (st.equals("log out")) {
            Intent serviceIntent = new Intent(this, ExmpleService.class);
            stopService(serviceIntent);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("haveFamily", false);
            editor.putBoolean("stayConnect", false);
            Intent go = new Intent(this, Connect.class);
            startActivity(go);
        }
        if (st.equals("leave family")) {
            Intent serviceIntent = new Intent(this, ExmpleService.class);
            stopService(serviceIntent);
            Query query = refFamily.orderByChild("familyUname").equalTo(settings.getString("currentFamily", ""));
            ValueEventListener leaveFamily = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Family family = data.getValue(Family.class);
                        ArrayList<User> UserValue2 = family.getUsers();
                        ArrayList<User> usersHelper = new ArrayList<>();
                        User user = null;
                        while (!UserValue2.isEmpty()) {
                            user = UserValue2.remove(0);
                            if (!user.getUid().equals(settings.getString("currentUser", ""))) {
                                usersHelper.add(user);
                            }
                        }
                        family.setUsers(usersHelper);
                        if (family.getUsers().isEmpty()) {
                            refFamily.child(familyUname2).removeValue();
                        } else {
                            refFamily.child(familyUname2).setValue(family);
                        }
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean("haveFamily", false);
                        editor.putString("currentFamily", "");
                        editor.commit();
                        returnToFamily(user);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            query.addListenerForSingleValueEvent(leaveFamily);
        }
        if (st.equals("change family")) {
            Query query = refFamily.orderByChild("familyUname").equalTo(settings.getString("currentFamily", ""));
            ValueEventListener leaveFamily = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Family family = data.getValue(Family.class);
                        User user = null;
                        while (!family.getUsers().isEmpty()) {
                            user = (User) family.getUsers().remove(0);
                            if (user.getUid().equals(settings.getString("currentUser", ""))) {
                                returnToFamily(user);
                            }
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            query.addListenerForSingleValueEvent(leaveFamily);
        }
        if (st.equals("new requests")) {
            Intent go = new Intent(this, Requests.class);
            go.putExtra("a", familyUname2);
            startActivity(go);
        }
        return true;
    }

    public void returnToFamily(User user1) {
        Intent getUser = new Intent(this, YourFamily.class);
        getUser.putExtra("a", user1.getUid());
        getUser.putExtra("b", user1.getName());
        getUser.putExtra("c", user1.getPassword());
        getUser.putExtra("d", user1.getEmail());
        startActivity(getUser);
    }
}