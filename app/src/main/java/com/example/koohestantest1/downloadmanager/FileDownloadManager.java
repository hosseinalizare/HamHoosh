package com.example.koohestantest1.downloadmanager;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;

public class FileDownloadManager {

    public static final String JPG_FORMAT = ".jpg";
    public static final String APK_FORMAT = ".apk";
    private Context context;

    public FileDownloadManager(Context context) {
        this.context = context;
    }

    /*public long downloadFromUrl(String url, String fileName, String format) {
        String fullFileName = fileName + format;
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fullFileName)// Uri of the destination file
                .setTitle(fullFileName)// Title of the Download Notification
                .setDescription("Downloading...");// Description of the Download Notification

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        long id = downloadManager.enqueue(request);
        if (format.equals(APK_FORMAT)) {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fullFileName);
            if(file.exists()){
                DownloadReceiver downloadReceiver = new DownloadReceiver(file);
                Intent intent = new Intent();
                downloadReceiver.onReceive(context,intent);
                context.registerReceiver(downloadReceiver,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                Uri uri = FileProvider.getUriForFile(context,context.getApplicationContext().getPackageName()+ ".provider",file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
        return id;
    }*/

    public long downloadFromUrl(String url, String fileName, String format) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName + format)// Uri of the destination file
                .setTitle(fileName+format)// Title of the Download Notification
                .setDescription("Downloading");// Description of the Download Notification

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        return downloadManager.enqueue(request);
    }
}
