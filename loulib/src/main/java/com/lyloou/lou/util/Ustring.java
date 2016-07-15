package com.lyloou.lou.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by admin on 2016/5/3.
 */
public class Ustring {

    public static boolean isValidFilePath(String path) {
        File f = new File(path);
        try {
            f.getCanonicalPath();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static String getJsonStrByMap(Map<String, String> map) {
        String jsonStr = "{}";
        try {
            JSONObject json = new JSONObject();
            for (String str : map.keySet()) {
                json.put(str, map.get(str));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

}
