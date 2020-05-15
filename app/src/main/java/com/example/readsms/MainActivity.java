package com.example.readsms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.AndroidException;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    ListView list ;
    ArrayList list1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list =(ListView) findViewById(R.id.list);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.READ_SMS},0);
            Toast.makeText(MainActivity.this, "Please Allow the 'Read-SMS' Permission to continue", Toast.LENGTH_LONG).show();
        }
        else

        {

            Uri uri= Uri.parse("content://sms/sent");
            Cursor c = getContentResolver().query(uri,null,null,null,null);
            list1 = new ArrayList();
            while (c.moveToNext()){
                String no=c.getString(2);
                String message = c.getString(12); //12 for Moto G and 13 for emul
                list1.add("From: " + no + "\n"  + "Message: " + message);

            }

            ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,list1);
            list.setAdapter(adapter);

        }

    }
}
