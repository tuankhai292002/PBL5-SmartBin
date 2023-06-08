package com.example.pbl5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.pbl5.databinding.ActivityMainBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static ActivityMainBinding binding;
    public static int loz = 1;
    FirebaseDatabase database ;
    DatabaseReference rootRef;
    myReciever br;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d("test", "onCreate: ");
        super.onCreate(savedInstanceState);
        Bin.isActivityActive=true;
        setContentView(R.layout.activity_main);
        startService(new Intent(MainActivity.this, ChayNgamService.class));

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        IntentFilter filter = new IntentFilter("test.Broadcast");
         br = new myReciever();
        registerReceiver( br, filter);



        setContentView(binding.getRoot());
    }


class myReciever extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent) {
        MainActivity activity = (MainActivity) context;
        Log.d("test", "Activity address: " + activity.toString());
        Bin bin = (Bin) intent.getSerializableExtra("bin");
        Log.d("test","onReceive: "+bin.state+"");
        switch (bin.name)
        {
            case "Bin01":
                binding.barDephanhuy.setProgress(bin.state);
                binding.tvDephanhuy.setText(bin.state+"");
                Log.d("test", "đã nhận dữ liệu từ bên servcie và đã set lên giao diện ");
                break;
            case "Bin02" :
                binding.barKhophanhuy.setProgress(bin.state);
                binding.tvKhophanhuy.setText(bin.state+"");
                break;
            default :
                binding.barNguyhiem.setProgress(bin.state);
                binding.tvNguyhiem.setText(bin.state+"");
                break;

        }
    }
    }

    @Override
    public void onResume() {
        super.onResume();
        Bin.isActivityActive = true;
    }
    @Override
    public void onPause() {
        Log.d("test", "onPauseeeee: ");
        Bin.isActivityActive=false;
        super.onPause();
    }
    public void onDestroy()
    {
        unregisterReceiver( br);
        Log.d("test", "onDestroyyyyyy: ");
        Bin.isActivityActive=false;
        super.onDestroy();
    }



}