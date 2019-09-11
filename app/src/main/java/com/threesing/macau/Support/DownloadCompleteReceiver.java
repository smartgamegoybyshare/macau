package com.threesing.macau.Support;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.DOWNLOAD_SERVICE;

public class DownloadCompleteReceiver extends BroadcastReceiver {

    private String TAG = "DownloadCompleteReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
                if(Value.language_flag == 0){
                    Toast.makeText(context, "dowload finish",
                            //To notify the Client that the file is being downloaded
                            Toast.LENGTH_SHORT).show();
                }else if(Value.language_flag == 1) {
                    Toast.makeText(context, "下載完成",
                            //To notify the Client that the file is being downloaded
                            Toast.LENGTH_SHORT).show();
                }else if(Value.language_flag == 2){
                    Toast.makeText(context, "下载完成",
                            //To notify the Client that the file is being downloaded
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
