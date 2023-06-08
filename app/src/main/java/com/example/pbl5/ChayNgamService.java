package com.example.pbl5;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChayNgamService extends Service {
    FirebaseDatabase database ;
    DatabaseReference rootRef;
    public final String channelId = "fuck";

    public  int notificationId ;
    public static int serviceId;
    public static int dephanhuy;
    public static int  khophanhuy;
    public static int nguyhiem;
    public boolean first_time;
    public boolean first_time2;
    public boolean first_time3;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        database= FirebaseDatabase.getInstance();
        rootRef= database.getReference("Status");
        serviceId=startId;
        first_time=true;
        first_time2=true;
        first_time3=true;
        DoBackgroundTask doBackgroundTask = new DoBackgroundTask();
        doBackgroundTask.doInBackground(3);
        String channel_idd = createNotificationChannel(ChayNgamService.this);
        RemoteViews remoteViews = new RemoteViews(ChayNgamService.this.getPackageName(), R.layout.custom_notif);
        Notification notification = new NotificationCompat.Builder(this, channel_idd)
                .setContentTitle("")
                .setContentText("")
                .setSmallIcon(R.drawable.smarttrash)
                .setContent(remoteViews)
                .build();

        startForeground(1, notification);
        return START_STICKY;
    }

    private String createNotificationChannel(Context context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Smart Bin";
            String channelDescription = "";
            int channelImportance = NotificationManager.IMPORTANCE_LOW;
            boolean channelEnableVibrate = true;
            String channelId1= "ThongBaoChayNgam";
            NotificationChannel notificationChannel = new NotificationChannel(channelId1, channelName, channelImportance);
            notificationChannel.setDescription(channelDescription);
            //notificationChannel.enableVibration(false);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
            return channelId1;
        } else {
            // Returns null for pre-O (26) devices.
            return null;
        }
    }


    @Override
    public void onDestroy() {
        Log.d("test", "onDestroy ChayNgamServiceccccccccccc ");
        super.onDestroy();
    }

    private class DoBackgroundTask extends AsyncTask<Integer, Integer, Integer>  {

        public void onProgressUpdate(Integer... progress) {

        }
        public  String createNotificationChannel(Context context) {
            // NotificationChannels are required for Notifications on O (API 26) and above.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence channelName = "Smart Bin";
                String channelDescription = "Trạng thái thùng rác";
                int channelImportance = NotificationManager.IMPORTANCE_HIGH;
                boolean channelEnableVibrate = true;
                NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, channelImportance);
                notificationChannel.setDescription(channelDescription);
                notificationChannel.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, null);
                notificationChannel.enableVibration(channelEnableVibrate);
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                assert notificationManager != null;
                notificationManager.createNotificationChannel(notificationChannel);

                return channelId;
            } else {
                // Returns null for pre-O (26) devices.
                return null;
            }
        }
        public void notifyy(String binname)
        {
            //Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.bin);

            Intent intent = new Intent(ChayNgamService.this, MainActivity.class);
            intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(ChayNgamService.this, 0, intent,  PendingIntent.FLAG_MUTABLE);
            String channel_id = createNotificationChannel(ChayNgamService.this);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(ChayNgamService.this, channel_id)
                    //.setLargeIcon(bitmap) // Set icon cho thông báo
                    .setContentTitle("Smart Bin") // Set tiêu đề cho thông báo
                    .setContentText("Thùng rác "+binname+ " đã đầy !") // Set nội dung cho thông báo
                    .setPriority(NotificationCompat.PRIORITY_MAX) // Set mức độ ưu tiên của thông báo
                    .setSmallIcon(R.drawable.smarttrash)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL);
            Bitmap bitmapp = null;
            switch(binname)
            {
                case "dễ phân hủy":
                    bitmapp = BitmapFactory.decodeResource(getResources(),R.drawable.vegetable);
                    break;
                case "khó phân hủy":
                    bitmapp = BitmapFactory.decodeResource(getResources(),R.drawable.bottle);
                    break;
                case "nguy hiểm":
                    bitmapp = BitmapFactory.decodeResource(getResources(),R.drawable.vaccine);
                    break;
            }
            builder.setLargeIcon(bitmapp);
            Notification notification = builder.build();

            NotificationManager notificationManager =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(binname.hashCode(), notification);
        }
        void sendMessage(Bin bin)
        {
            Intent intent = new Intent();
            intent.putExtra("bin",bin);

            intent.setAction("test.Broadcast");
            Log.d("test", "sendMessage : "+ bin.name);
            sendBroadcast(intent);
        }
        @Override
        public Integer doInBackground(Integer ... intergers) {
            Log.d("test", "doInBackground for ChayNgamService: ");


            rootRef.child("Bin01").limitToLast(1).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    int current = snapshot.getValue(Integer.class);
                    dephanhuy = current;
                    if(!first_time && current ==100&&!Bin.isActivityActive)
                    {
                        notifyy("dễ phân hủy");
                    }
                    else
                    {
                        first_time=false;
                    }
                    sendMessage(new Bin("Bin01", current));


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            rootRef.child("Bin02").limitToLast(1).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    int current =  snapshot.getValue(Integer.class);
                    khophanhuy=current;
                    if(!first_time2 && current ==100 && !Bin.isActivityActive)
                    {
                        notifyy("khó phân hủy");
                    }
                    else
                    {
                        first_time2=false;
                    }
                    sendMessage(new Bin("Bin02", current));

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            rootRef.child("Bin03").limitToLast(1).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    int current =  snapshot.getValue(Integer.class);
                    nguyhiem=current;
                    if(!first_time3 && current ==100&&!Bin.isActivityActive)
                    {
                        notifyy("nguy hiểm");
                    }
                    else
                    {
                        first_time3=false;
                    }
                    sendMessage(new Bin("Bin03", current));

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return 3;

        }
    }

}