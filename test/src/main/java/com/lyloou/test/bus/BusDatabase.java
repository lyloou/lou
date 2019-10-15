package com.lyloou.test.bus;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.List;

public class BusDatabase {
    private static String DATA_FILE_NAME = "data_bus.db";


    private final File dataFile;

    public BusDatabase(Context context) {
        dataFile = new File(context.getFilesDir(), DATA_FILE_NAME);
    }

    public List<BusParam> readData() {
        try {
            return new Gson().fromJson(new FileReader(dataFile), getType());
        } catch (Exception e) {
        }
        return null;
    }

    private Type getType() {
        return new TypeToken<List<BusParam>>() {
        }.getType();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void writeData(List<BusParam> params) {
        String json = new Gson().toJson(params, getType());
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

    public void delete() {
        dataFile.delete();
    }
}
