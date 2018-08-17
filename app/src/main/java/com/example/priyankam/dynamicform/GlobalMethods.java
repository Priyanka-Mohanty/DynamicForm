package com.example.priyankam.dynamicform;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.priyanka.dynamicformlibrary.utils.Utility;

import java.io.File;
import java.util.ArrayList;

class GlobalMethods {
    public static void image_file_delete(Context mContext, String imageName) {
        String path = Configuration.IMAGE_FILE_PATH + imageName;
        File file = new File(path);
        if (file.exists()) {
            boolean deleted = file.delete();
        }
        Log.i("Image Upload Success", "Image is delete from File" + path);
    }

    public static ArrayList<String> fileFilter(String filePath, String fileHeader) {
        ArrayList<String> allImagePath = new ArrayList<>();
        try {
            File path = new File(Utility.FILE_PATH);
            if (path.exists()) {
                String[] fileNames = path.list();
                try {
                    if (fileNames != null) {
                        for (int j = 0; j < fileNames.length; j++) {

                            if (fileNames[j].startsWith(fileHeader)) {
                                File file = new File(filePath + fileNames[j]);
                                allImagePath.add(file.getAbsolutePath());
                                System.out.println("All image" + file.getAbsolutePath());
                            }
                        }
                    } else {
                        Log.e("fileNames is null", "fileNames is null");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return allImagePath;
    }

    public static void hideKeyboard(View view) {
        try {
            InputMethodManager inputManager = (InputMethodManager) view
                    .getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("hideKeyboard", "hideKeyboard " + e.getMessage());
        }
    }
}
