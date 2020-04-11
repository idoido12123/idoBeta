package com.example.idobeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

import static com.example.idobeta.FBref.refFamily;
import static com.example.idobeta.FBref.refTasks;

public class Matalot extends AppCompatActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matalot);
        NewTask = (Button) findViewById(R.id.newTask);
        tasks = (ListView) findViewById(R.id.tasks);
        Intent getName = getIntent();
        familyUname2 = getName.getExtras().getString("a");
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
                        TaskList.add(task1.getTeur());
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
                final String TSdatime = startDate.getText().toString() + " " + startTime.getText().toString();
                final String TEdatime = endDate.getText().toString() + " " + endTime.getText().toString();
                final String TOname = Norder.getText().toString();
                Query query = refFamily.orderByChild("familyUname").equalTo(familyUname2);
                ValueEventListener addTask = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Family family = data.getValue(Family.class);
                            Task task = new Task(Tdes, TSdatime, TEdatime, TOname, true);
                            family.addTask(task);
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
                            task.setActive(true);
                        }
                        tasksHelper.add(task);
                    }
                    family2.setTasks(tasksHelper);
                    refFamily.child(familyUname2).setValue(family2);
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
                    refFamily.child(familyUname2).setValue(family2);
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
        final String taskHelper2=(String) tasks.getItemAtPosition(i);
        Query query=refFamily.orderByChild("familyUname").equalTo(familyUname2);
        ValueEventListener removeValue=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for (DataSnapshot data:dataSnapshot.getChildren()){
                   Family family=data.getValue(Family.class);
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
}
