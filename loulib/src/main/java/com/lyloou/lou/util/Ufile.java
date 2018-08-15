package com.lyloou.lou.util;

import java.io.File;

public class Ufile {

    // https://stackoverflow.com/questions/14727226/android-how-to-remove-files-from-external-storage
    public static boolean deleteDir(File dir) {
        if (dir == null) {
            return true;
        }

        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }
}
