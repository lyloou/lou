package com.lyloou.test.man;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.lyloou.test.util.Ugson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

class DataRepositoryHelper {
    private static final List<Data> DATA = new ArrayList<>();

    static {
        DATA.add(new Data().setTitle("木子楼").setUrl("http://lyloou.com/"));
        DATA.add(new Data().setTitle("阮一峰").setUrl("http://ruanyifeng.com/blog/"));
        DATA.add(new Data().setTitle("阮一峰 - ES6").setUrl("http://es6.ruanyifeng.com/"));
        DATA.add(new Data().setTitle("阮一峰 - 幸存者").setUrl("http://survivor.ruanyifeng.com/index.html"));
        DATA.add(new Data().setTitle("阮一峰 - 前方的路").setUrl("http://road.ruanyifeng.com/index.html"));
        DATA.add(new Data().setTitle("陈 皓").setUrl("https://coolshell.cn/"));
        DATA.add(new Data().setTitle("刘未鹏").setUrl("http://mindhacks.cn/"));
        DATA.add(new Data().setTitle("廖雪峰").setUrl("https://www.liaoxuefeng.com/"));
        DATA.add(new Data().setTitle("王 垠").setUrl("http://www.yinwang.org/"));
    }

    private static final String DATA_FILE_NAME = "data.db";

    private final File dataFile;

    private DataRepositoryHelper(Context context) {
        dataFile = new File(context.getFilesDir(), DATA_FILE_NAME);
    }

    public static DataRepositoryHelper newInstance(Context context) {
        return new DataRepositoryHelper(context);
    }

    public List<Data> readData() {
        try {
            return Ugson.getGson().fromJson(new FileReader(dataFile), getDataListType());
        } catch (Exception e) {
            // 文件不存在，或者文件内容不符合规范；匀可忽略
        }
        return DATA;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void writeData(List<Data> data) {
        String json = Ugson.getGson().toJson(data, getDataListType());
        Writer writer = null;
        try {
            if (!dataFile.exists()) {
                dataFile.createNewFile();
            }
            writer = new FileWriter(dataFile);
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Type getDataListType() {
        return new TypeToken<List<Data>>() {
        }.getType();
    }

    public void delete() {
        dataFile.delete();
    }
}
