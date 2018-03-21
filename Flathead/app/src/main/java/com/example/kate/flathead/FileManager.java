package com.example.kate.flathead;

/*
  Handle the reading/writing of the messages file
 */

import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class FileManager {
    static void checkAndLoadMessages(File externalFilesDir, String messageFileName, AssetManager assetManager) {

        File outFile = new File(externalFilesDir, messageFileName);

        //if it doesn't exist
        if (!outFile.isFile() && !outFile.isDirectory()) {

            InputStream in = null;
            OutputStream out = null;

            try {

                in = assetManager.open("arrays.xml");
                out = new FileOutputStream(outFile);
                copyFile(in, out);

            } catch (IOException e) {
                Log.e("tag", "Failed to copy asset file: " + messageFileName, e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
            }
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

}
