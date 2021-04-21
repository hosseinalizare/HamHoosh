package com.example.koohestantest1.downloadmanager;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.koohestantest1.BuildConfig;
import com.example.koohestantest1.classDirectory.BaseCodeClass;

import java.io.File;

public class DownloadReceiver extends BroadcastReceiver {
    private long downloadId;
    private File file;

    public DownloadReceiver(File file) {
        this.file = file;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        long receivedId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        if (BaseCodeClass.appId == receivedId) {
            /*File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), BaseCodeClass.appName);
            file.setReadable(true, false);
            if (*//*file.isFile()*//*true) {
                Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                Toast.makeText(context, "نسخه جدید با موفقیت نصب شد", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "دانلود موفقیت آمیز بود", Toast.LENGTH_SHORT).show();
            }*/

        }else{
            Toast.makeText(context, "فامتور با موفقیت دانلود شد", Toast.LENGTH_SHORT).show();
        }

    }

    public void setDownloadId(long downloadId) {
        this.downloadId = downloadId;
    }
}
