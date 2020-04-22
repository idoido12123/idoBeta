package com.example.idobeta;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.idobeta.App.CHANNEL_ID;
import static com.example.idobeta.FBref.refFamily;

public class ExmpleService extends Service {
    String input;
    boolean changeTask;
    Family family;
    ArrayList<Task>taskValue=new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        SharedPreferences settings = getSharedPreferences("PREFS_NAME", MODE_PRIVATE);
            Query query = refFamily.orderByChild("familyUname").equalTo(settings.getString("currentFamily",""));
            final ValueEventListener taskAdd = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        family = data.getValue(Family.class);
                        int flag=family.getNotificition();
                        if ( flag == 0  ) {
                            input = "you dont have new task (:";
                            startService1(0);
                        }
                        if(flag==2){
                            taskValue.clear();
                            taskValue.add(family.getTasks().remove(0));
                            while (!family.getTasks().isEmpty()){
                                Task task=family.getTasks().remove(0);
                                    if(task.isCurrentActive()){
                                        input="the task "+task.getTeur()+" is active!!";
                                    }
                                }
                            startService1(2);
                        }
                        if (flag == 1) {
                            taskValue.clear();
                            taskValue.add(family.getTasks().remove(0));
                            while (!family.getTasks().isEmpty()) {
                                Task task = family.getTasks().remove(0);
                                if (family.getTasks().isEmpty()) {
                                    input = "you have new task!! " + task.getTeur();
                                }
                                taskValue.add(task);
                            }
                            startService1(1);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            if(changeTask==false){
                query.removeEventListener(taskAdd);
            }
            query.addValueEventListener(taskAdd);
        return START_NOT_STICKY;
    }
    public void startService1(int i) {
        SharedPreferences settings = getSharedPreferences("PREFS_NAME", MODE_PRIVATE);
        family.setNotificition(4);
        if (i==1||i==2) {
            family.setTasks(taskValue);
        }
        refFamily.child(settings.getString("currentFamily","")).setValue(family);
        if (i == 0 || i == 1 || i == 2) {
            Intent notificationIntent = new Intent(this, Matalot.class);
            notificationIntent.putExtra("a", settings.getString("currentFamily", "a"));
            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0, notificationIntent, 0);
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("family tasks").setContentText(input).setSmallIcon(R.drawable.ic_message)
                    .setContentIntent(pendingIntent).build();
            startForeground(1, notification);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
