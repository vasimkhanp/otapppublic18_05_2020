package com.otapp.net.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SaveTicket {

    private Context TheThis;
    private String NameOfFolder = "/Otapp";
    public static String NameOfFile = null;

    public boolean saveMovieTicket(Context context, Bitmap ImageToSave) {
        boolean isDone = false;
        if (ImageToSave != null) {
            TheThis = context;
            String file_path = context.getExternalCacheDir().getAbsolutePath() + NameOfFolder;
            String CurrentDateAndTime = getCurrentDateAndTime();
            File dir = new File(file_path);
            NameOfFile = "Otapp" + CurrentDateAndTime + ".jpeg";

            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(dir, NameOfFile);
            Log.e("Storage::", "File::" + file.getAbsolutePath());
            if (file.exists()) file.delete();
            try {
                FileOutputStream fOut = new FileOutputStream(file);
                ImageToSave.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.flush();
                fOut.close();
                MakeSureFileWasCreatedThenMakeAvabile(file);
                isDone = true;
            } catch (Exception e) {
                Log.d("Errer : ", e.getMessage());
            }
        }
        return isDone;

    }

    private void MakeSureFileWasCreatedThenMakeAvabile(File file) {
        MediaScannerConnection.scanFile(TheThis,
                new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });

    }

    @SuppressLint("SimpleDateFormat")
    private String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

}
