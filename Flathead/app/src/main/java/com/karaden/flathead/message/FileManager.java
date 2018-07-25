package com.karaden.flathead.message;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by kate on 21/3/18.
 * Handle the reading/writing of the messages file
 */
public class FileManager {
    public static void writeMessageFile(File externalFilesDir, String messageFileName, AssetManager assetManager, String assetFileName) {

        File outFile = new File(externalFilesDir, messageFileName);

        //if it doesn't exist
        if (!outFile.isFile() && !outFile.isDirectory()) {

            InputStream in = null;
            OutputStream out = null;

            try {

                in = assetManager.open(assetFileName);
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

    /**
     * @param externalFilesDir location of file
     * @param messageFileName  filename to open
     * @return array of strings from the file (newline separated)
     */
    public static ArrayList<String> readArrayFromFile(File externalFilesDir, String messageFileName)
            throws IOException {
        ArrayList<String> res = new ArrayList<>();
        FileInputStream in;
        BufferedReader reader;

        File file = new File(externalFilesDir, messageFileName);

        //make sure it exists
        if (file.isFile() && !file.isDirectory()) {


            in = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(in));

            String line = reader.readLine();
            while (line != null) {
                //The following line is NOT redundant! It is necessary to force the correct parsing of new line characters
                //noinspection RedundantStringFormatCall
                line = String.format(line);
                res.add(line);
                line = reader.readLine();
            }

        } else {
            throw new IOException("File not found: " + messageFileName);
        }

        return res;
    }

    public static int readTimerFile(File externalFilesDir, String fileName)
        throws IOException {

        int timeOut;
        FileInputStream in;
        BufferedReader reader;

        File file = new File(externalFilesDir, fileName);

        //make sure it exists
        if (file.isFile() && !file.isDirectory()) {


            in = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(in));

            timeOut = Integer.parseInt(reader.readLine());

        } else {
            throw new IOException("File not found: " + fileName);
        }

        return timeOut;
    }

}
