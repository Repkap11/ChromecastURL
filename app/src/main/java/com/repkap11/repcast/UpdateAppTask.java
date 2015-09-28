package com.repkap11.repcast;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by paul on 9/20/15.
 */
public class UpdateAppTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = UpdateAppTask.class.getSimpleName();
    private Context mContext;
    private static final String REMOTE_URL = "https://repkam09.com/dl/repcast/repcast.apk";

    public UpdateAppTask(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    protected String doInBackground(Void... arg0) {
        try {
            URL url = new URL(REMOTE_URL);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            String username = "guest";
            String password = "guest";
            String userPassword = username + ":" + password;
            String encoding = Base64.encodeToString(userPassword.getBytes(), Base64.DEFAULT);
            c.setRequestProperty("Authorization", "Basic " + encoding);
            c.setUseCaches(false);
            c.connect();
            File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "RepCast.apk");
            outputFile.getParentFile().mkdirs();
            if (outputFile.exists()) {
                outputFile.delete();
            }
            outputFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(outputFile);

            InputStream is = c.getInputStream();

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }
            fos.close();
            is.close();

            PackageInfo newInfo = mContext.getPackageManager().getPackageArchiveInfo(outputFile.getAbsolutePath(), 0);
            PackageInfo oldPackageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            if (newInfo.versionCode > oldPackageInfo.versionCode) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(outputFile), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
                mContext.startActivity(intent);
            } else {
                Log.e(TAG,"Not installing because apk is older or the same");
                return "No update avaliable from Repkam09.com";

            }


        } catch (Exception e) {
            e.printStackTrace();
            return "RepCast Error Updating:" + e.getMessage();

        }
        return null;
    }

    @Override
    protected void onPostExecute(String message) {
        super.onPostExecute(message);
        if (message != null) {
            Log.e(TAG, message);
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }
}

