package com.example.readsms;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CallLog;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public String[] permissions;
    ListView list;
    ArrayList list1;
    Button load;
    public EditText et;
    public Button btn_Save;
    public TextView file_loc;
    Button eml;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list);
        et = findViewById(R.id.textView);
        btn_Save = findViewById(R.id.save);
        file_loc = findViewById(R.id.file_loc);
        load = findViewById(R.id.Load);
        et.setText("");
        perm();
        /* if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)
                {  ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS},0);
                    Toast.makeText(MainActivity.this, "Please Allow the 'Read-SMS' Permission to continue", Toast.LENGTH_LONG).show();
                }
                else if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                    Toast.makeText(MainActivity.this, "Please Allow the 'Write-external' Permission to continue", Toast.LENGTH_LONG).show();
                }

                 else if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                 {
                     ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                     Toast.makeText(MainActivity.this, "Please Allow the 'Read-external' Permission to continue", Toast.LENGTH_LONG).show();
                 }

                else
                {
                   // read_sms();
                  //  write_internal();

                }*/

//===================================
      /*  permissions = new String[]{
                Manifest.permission.READ_SMS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_CALL_LOG
        };
      ActivityCompat.requestPermissions(this, permissions, 1);*/
//=======================================
       /* PackageManager pm = this.getPackageManager();
        int hasPerm = pm.checkPermission(Manifest.permission.READ_SMS,this.getPackageName());

        if (hasPerm != PackageManager.PERMISSION_GRANTED)

        {
            // do stuf
            Toast.makeText(this, "Permission is required in order for working", Toast.LENGTH_SHORT).show();
        }
        else {

            read_sms();

        }*/

//==================================================
        /*int GET_MY_PERMISSION = 1;
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_SMS)){
                *//* do nothing*//*

            }

            else {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_SMS},GET_MY_PERMISSION);

            }

        }*/

        //============================================================
        ////CALLING THE MAIN FUNCTIONS

        //   read_sms();
        // read_call();
        //   share_email();

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (perm())
                    read_sms();
                else {
                    Toast.makeText(getApplicationContext(), "PLease provide the SMS read Permission", Toast.LENGTH_SHORT).show();
                    file_loc.setText("Permission not provided please press the button again to provide the permission");
                }
            }
        });

        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perm();

                if (storage_perm())
                    write_external();
                else {
                    Toast.makeText(getApplicationContext(), "Storage permission required ", Toast.LENGTH_SHORT).show();
                    file_loc.setText("Storage permission required to save the file to SD card");
                }
            }
        });
    }
    private void share_email() {
        eml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "email", Toast.LENGTH_LONG).show();
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"email@example.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject here");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "body text");
                File root = Environment.getExternalStorageDirectory();
                String pathToMyAttachedFile = "/test.txt";
                File file2 = new File(root, pathToMyAttachedFile);
                if (!file2.exists() || !file2.canRead()) {
                    return;
                }
                Uri uri = Uri.fromFile(file2);
                emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
                Toast.makeText(MainActivity.this, "URI", Toast.LENGTH_LONG).show();
                startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
                Toast.makeText(MainActivity.this, "BRoken", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void write_internal() {

        //////////////////////////////// /// Writing to intenral memory Only cannot be accessed from other apps or Rooted
        FileOutputStream fos = null;
        try {

            fos = openFileOutput(et.getText().toString() + ".txt", MODE_APPEND);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(list1.toString().getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Saved to " + getFilesDir() + "/" + et.getText().toString() + ".txt",
                Toast.LENGTH_LONG).show();

    }
    private void read_sms() {

        Uri uri = Uri.parse("content://sms");
        Cursor c = getContentResolver().query(uri, null, null, null, null);
        list1 = new ArrayList();
        StringBuilder sb = new StringBuilder();
        list1.add("Sent" + "," + "no" + "," + "message");
        while (c.moveToNext()) {
            String msg_no = c.getString(0);
            String sent_to = c.getString(2);

            String date_sms = millisToDate((c.getString(4)));
            String date_sent = millisToDate((c.getString(5)));


            String message = c.getString(12); //12 for Moto G and 13 for emul
            list1.add("\n"
                    + "Message No. :" + msg_no + "\n"
                    + "Sent to:" + sent_to + "\n"
                    + "Date Sent:" + date_sent + "\n"
                    + "Message:" + message + "\n");

            /*list1.add("\n"
                    + "Message No " + (c.getPosition() + 1) + ";"
                    + "Sent at " + sent + ";"
                    + "From " + no + ";"
                    + "Body " + message );*/
            // sb.append(no + message + sent).append("\n");
        }
        ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, list1);
        list.setAdapter(adapter);
    }

    public void read_call() {
        Uri allCalls = Uri.parse("content://call_log/calls");
        Cursor call = getContentResolver().query(allCalls, null, null, null, null);
        list1 = new ArrayList();
        StringBuilder sb = new StringBuilder();
        list1.add("Sent" + "," + "no" + "," + "message");
        while (call.moveToNext()) {
            String num = call.getString(call.getColumnIndex(CallLog.Calls.NUMBER));// for  number
            String name = call.getString(call.getColumnIndex(CallLog.Calls.CACHED_NAME));// for name
            String duration = call.getString(call.getColumnIndex(CallLog.Calls.DURATION));// for duration
            int type = Integer.parseInt(call.getString(call.getColumnIndex(CallLog.Calls.TYPE)));//
            list1.add(num + "," + name + "," + duration + "," + type);
            /*list1.add("\n"
                    + "Message No " + (c.getPosition() + 1) + ";"
                    + "Sent at " + sent + ";"
                    + "From " + no + ";"
                    + "Body " + message );*/
            // sb.append(no + message + sent).append("\n");
        }
        ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, list1);
        list.setAdapter(adapter);
    }

    public void displaySmsLog() {
        Uri allMessages = Uri.parse("content://sms/sent");
        //Cursor cursor = managedQuery(allMessages, null, null, null, null); Both are same
        Cursor cursor = this.getContentResolver().query(allMessages, null,
                null, null, null);
        while (cursor.moveToNext()) {
            for ( int i = 0; i < cursor.getColumnCount(); i++ ) {
                Log.d("nyy", cursor.getString(i) + "--------" + cursor.getColumnName(i));
            }
            //Log.d("nyy","******************");
        }
    }

    public static String millisToDate(String currentTime) {
        String finalDate;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(currentTime));
        Date date = calendar.getTime();
        finalDate = date.toString();
        return finalDate;
    }

    public void write_external() {
        storage_perm();
        if ((list1 == null)) {
            Toast.makeText(this, "Please press the Load SMS Button First", Toast.LENGTH_SHORT).show();

        } else {


            {
                String chk = et.getText().toString();
                if (chk.matches("")) {
                    Toast.makeText(MainActivity.this, "Please Enter the file name to continue.. ", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, " ", Toast.LENGTH_LONG).show();

                    File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                    File dir = new File(root.getPath());
                    File file = new File(dir, et.getText().toString() + ".txt");
                    try {
                        //FileOutputStream fos=new FileOutputStream(Environment.getExternalStorageDirectory()+"/abc.txt");
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        fileOutputStream.write(list1.toString().getBytes());
                        fileOutputStream.close();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    file_loc.setText(String.format("File Saved on External SD card%s%s", dir, file));
                }
                //OLD CODE
            }
        }

       /* //  File path = Environment.getExternalStoragePublicDirectory(
        //         Environment.DIRECTORY_DOWNLOADS);

        // File file = new File(path, "papu.txt");
        //  String fileName = "pappu.txt";
        File myFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "Pappu.txt");
        String abc = "Abc";

        try {

            FileOutputStream fos = new FileOutputStream(myFile);
            fos.write(list1.toString().getBytes());
            Toast.makeText(MainActivity.this, "NOT", Toast.LENGTH_LONG).show();
            fos.close();
        }  catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "NOT", Toast.LENGTH_LONG).show();

        }*/

    }

    public boolean perm() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS},
                    1);
            Toast.makeText(this, "PLease provide the SMS read Permission", Toast.LENGTH_SHORT).show();
            file_loc.setText("Permission not provided please press the button again to provide the permission");
            return false;
        }
    }//perm()

    public boolean storage_perm() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
            Toast.makeText(this, "External Storage write Permission required ", Toast.LENGTH_SHORT).show();
            file_loc.setText("External Storage write Permission required");
            return false;
        }
    }//perm()

}//Mainactivity