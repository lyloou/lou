package com.lyloou.lou.util;

import java.io.File;
import java.io.IOException;

/**
 * Created by admin on 2016/5/3.
 */
public class Ustring {

    public static boolean isValidPath(String path){
        File f = new File(path);
        try{
            f.getCanonicalPath();
            return true;
        } catch (IOException e){
            return false;
        }
    }
}
