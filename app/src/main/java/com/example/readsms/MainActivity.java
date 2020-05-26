package com.example.readsms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

public class MainActivity extends AppCompatActivity {
    ListView list;
    ArrayList list1;
    public EditText et ;
    public Button btn_Save;
    Button eml;
    public String FILE_NAME = "nyy2.txt";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list);
        et =findViewById(R.id.textView);
        btn_Save = findViewById(R.id.save);
        eml = findViewById(R.id.email);
        et.setText("");

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


        String[] permissions = new String[]{
                Manifest.permission.READ_SMS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        ActivityCompat.requestPermissions(this, permissions, 1);
        read_sms();
        // share_email();

        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chk = et.getText().toString();

                if (chk.matches(""))
                {
                    Toast.makeText(MainActivity.this, "Please Enter the file name to continue.. ", Toast.LENGTH_LONG).show();

                }
                else
                    write_external();

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
                /*File root = Environment.getExternalStorageDirectory();
                String pathToMyAttachedFile = "temp/attachement.xml";
                File file = new File(root, pathToMyAttachedFile);
                if (!file.exists() || !file.canRead()) {
                    return;
                }
                Uri uri = Uri.fromFile(file);
                emailIntent.putExtra(Intent.EXTRA_STREAM, uri);*/
                startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));


            }
        });
    }

    private void write_internal() {

        //////////////////////////////// /// Writing to intenral memory Only cannot be accessed from other apps or Rooted
        FileOutputStream fos = null;
        try {

            fos = openFileOutput(FILE_NAME, MODE_APPEND);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(list1.toString().getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Saved to " + getFilesDir() + "/" + FILE_NAME,
                Toast.LENGTH_LONG).show();

    }

    private void read_sms() {

        Uri uri = Uri.parse("content://sms");
        Cursor c = getContentResolver().query(uri, null, null, null, null);
        list1 = new ArrayList();
        StringBuilder sb = new StringBuilder();
        while (c.moveToNext()) {
            String sent = c.getString(0);
            String no = c.getString(2);
            String message = c.getString(12); //12 for Moto G and 13 for emul
            list1.add("Message No    : " + (c.getPosition() + 1) + ";\n"
                    + "Sent at     :" + sent + ";\n"
                    + "From       :" + no + ";\n"
                    + "Body       : " + message + ";\n");


            //sb.append(no + message + sent).append("\n");
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

    public void write_external() {

                File root = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM);
                File dir = new File(root.getPath());
                File file = new File(dir, et.getText().toString()+".txt");

                try {
                    //FileOutputStream fos=new FileOutputStream(Environment.getExternalStorageDirectory()+"/abc.txt");
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(list1.toString().getBytes());
                    fileOutputStream.close();
                    Toast.makeText(MainActivity.this, "File Saved on External SD card ", Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        //OLD CODE
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
}