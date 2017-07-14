package com.lyloou.lou.util;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
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
            return json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

    // 这个只是一层的，更多层的请参考：https://stackoverflow.com/questions/21720759/convert-a-json-string-to-a-hashmap
    public static Map<String, String> jsonToMap(JSONObject json) throws JSONException {

        Map<String, String> map = new HashMap<>();

        Iterator<String> keysItr = json.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            String value = String.valueOf(json.get(key));
            map.put(key, value);
        }
        return map;
    }

    public static boolean isMobilePhoneNumber(String number) {
        /*
		移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		联通：130、131、132、152、155、156、185、186
		电信：133、153、180、189、（1349卫通）
		总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		*/
        number = number.trim();
        final String TEL_REGEX = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return !TextUtils.isEmpty(number) && number.matches(TEL_REGEX);
    }

}
