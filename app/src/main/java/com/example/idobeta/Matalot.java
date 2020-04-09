package com.example.idobeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.idobeta.FBref.refTasks;

public class Matalot extends AppCompatActivity {
    AlertDialog.Builder addTask;
    LinearLayout TaskDialog;
    EditText Ntask;
    EditText startDate;
    EditText startTime;
    EditText endDate;
    EditText endTime;
    EditText Norder;
    Button NewTask;
    ListView tasks;
    ArrayList<String> TaskList=new ArrayList<>();
    ArrayList<Task> TaskValues=new ArrayList<Task>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matalot);
        NewTask=(Button)findViewById(R.id.newTask);
        tasks=(ListView) findViewById(R.id.tasks);
        ValueEventListener TasksListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                TaskValues.clear();
                TaskList.clear();
                for (DataSnapshot data : ds.getChildren()) {
                    String Tdes = (String) data.getKey();
                    Task task = data.getValue(Task.class);
                    TaskValues.add(task);
                    String Torder=task.getOrder();
                    TaskList.add(Tdes+" "+Torder);
                }

                ArrayAdapter<String> adp = new ArrayAdapter<String>(Matalot.this, R.layout.support_simple_spinner_dropdown_item, TaskList);
                tasks.setAdapter(adp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Matalot", "Failed to read value.", databaseError.toException());
            }
        };
        refTasks.addValueEventListener(TasksListener);
        //tasks.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //tasks.setOnItemClickListener(this);
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
                String Tdes = Ntask.getText().toString();
                String TSdatime = startDate.getText().toString() + " " + startTime.getText().toString();
                String TEdatime=endDate.getText().toString()+" "+endTime.getText().toString();
                String TOname=Norder.getText().toString();
                Task task= new Task(Tdes,TSdatime ,TEdatime,TOname, true);
                refTasks.child(Ntask.getText().toString()).setValue(task);
            }
        }
    };
}
