package com.lyloou.lou.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 类描述：
 * 创建人： Lou
 * 创建时间： 2016/7/13 14:22
 * 修改人： Lou
 * 修改时间：2016/7/13 14:22
 * 修改备注：
 * <p/>
 * 用法：
 * {@code
 * <p/>
 * <p/>
 * // 初始化
 * LouSQLite.init(mContext, MyCallBack.getInstance());
 * <p/>
 * Phrase phrase = new Phrase("青青子衿，悠悠我心");
 * <p/>
 * <p/>
 * // 从数据库中删除
 * LouSQLite.delete(MyCallBack.TABLE_PHRASE, MyCallBack.KEY_PHRASE_ID + "=?", new String[]{phrase.getId()});
 * <p/>
 * <p/>
 * // 插入一个数据到数据库
 * LouSQLite.insert(MyCallBack.TABLE_PHRASE, phrase);
 * <p/>
 * <p/>
 * // 插入一组数据
 * List<Phrase> lists =  Arrays.asList(
 * new Phrase("窈窕淑女，君子好逑"),
 * new Phrase("海上生明月，天涯共此时"),
 * new Phrase("青青子衿，悠悠我心"),
 * new Phrase("人生若只如初见")
 * );
 * LouSQLite.insert(MyCallBack.TABLE_PHRASE, lists);
 * <p/>
 * <p/>
 * // 更新到数据库
 * LouSQLite.update(MyCallBack.TABLE_PHRASE, phrase, MyCallBack.KEY_PHRASE_ID + "=?", new String[]{phrase.getId()});
 * <p/>
 * <p/>
 * //  查找
 * List<Phrase> lists = LouSQLite.query(MyCallBack.TABLE_PHRASE, "select * from " + MyCallBack.TABLE_PHRASE, null);
 * }
 */
public class MyCallBack implements LouSQLite.ICallBack {

    private MyCallBack() {
    }

    private static LouSQLite.ICallBack INSTANCE;

    public static LouSQLite.ICallBack getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MyCallBack();
        }
        return INSTANCE;
    }

    ///////////////////////////////////////////////////////////////////////////
    // db config
    ///////////////////////////////////////////////////////////////////////////
    public static final String DB_NAME = "ledfan.db";
    public static final int DB_VERSION = 1;


    ///////////////////////////////////////////////////////////////////////////
    // table phrase
    ///////////////////////////////////////////////////////////////////////////
    public static final String TABLE_PHRASE = "phrase";
    public static final String KEY_PHRASE_ID = "PHRASE_ID";
    private static final String KEY_PHRASE_CONTENT = "PHRASE_CONTENT";
    private static final String KEY_PHRASE_FAVORITE = "PHRASE_FAVORITE";
    private static final String TABLE_PHRASE_SQL = "create table " + TABLE_PHRASE + " (" +
            "id integer primary key autoincrement, " +
            KEY_PHRASE_ID + " text, " +
            KEY_PHRASE_CONTENT + " text, " +
            KEY_PHRASE_FAVORITE + " integer" +
            ")";


    ///////////////////////////////////////////////////////////////////////////
    // table favorite
    ///////////////////////////////////////////////////////////////////////////
    public static final String TABLE_FAVORITE = "favorite";
    public static final String KEY_FAVORITE_ID = "FAVORITE_ID";
    private static final String KEY_FAVORITE_CONTENT = "FAVORITE_CONTENT";
    private static final String KEY_FAVORITE_FAVORITE = "FAVORITE_FAVORITE";
    private static final String TABLE_FAVORITE_SQL = "create table " + TABLE_FAVORITE + " (" +
            "id integer primary key autoincrement, " +
            KEY_FAVORITE_ID + " text, " +
            KEY_FAVORITE_CONTENT + " text," +
            KEY_FAVORITE_FAVORITE + " integer" +
            ")";


    ///////////////////////////////////////////////////////////////////////////
    // overrite
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public List<String> createTablesSQL() {
        return Arrays.asList(
                TABLE_PHRASE_SQL,
                TABLE_FAVORITE_SQL
        );
    }

    @Override
    public String getName() {
        return DB_NAME;
    }

    @Override
    public int getVersion() {
        return DB_VERSION;
    }

    @Override
    public void doUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        switch (oldVersion) {
            case 0:
                db.execSQL(TABLE_FAVORITE_SQL); // 升级操作；
            case 1:
                break;
            default:
                break;
        }
    }


    @Override
    public <T> void assignValuesByInstance(String tableName, T t, ContentValues values) {

        switch (tableName) {
            case TABLE_PHRASE:
                if (t instanceof Phrase) {
                    Phrase phrase = (Phrase) t;
                    values.put(KEY_PHRASE_ID, phrase.getId());
                    values.put(KEY_PHRASE_CONTENT, phrase.getContent());
                    values.put(KEY_PHRASE_FAVORITE, phrase.getFavorite());
                }
                break;
            case TABLE_FAVORITE:
                if (t instanceof Phrase) {
                    Phrase phrase = (Phrase) t;
                    values.put(KEY_FAVORITE_ID, phrase.getId());
                    values.put(KEY_FAVORITE_CONTENT, phrase.getContent());
                    values.put(KEY_FAVORITE_FAVORITE, phrase.getFavorite());
                }
                break;
        }
    }

    @Override
    public Object newInstanceByCursor(String tableName, Cursor cursor) {
        switch (tableName) {
            case TABLE_PHRASE:
                return new Phrase(
                        cursor.getString(cursor.getColumnIndex(KEY_PHRASE_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_PHRASE_CONTENT)),
                        cursor.getInt(cursor.getColumnIndex(KEY_PHRASE_FAVORITE))
                );
            case TABLE_FAVORITE:
                return new Phrase(
                        cursor.getString(cursor.getColumnIndex(KEY_FAVORITE_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_FAVORITE_CONTENT)),
                        cursor.getInt(cursor.getColumnIndex(KEY_FAVORITE_FAVORITE))
                );
        }

        return null;
    }

    public static class Phrase {
        private String mId; // 短语的唯一标识
        private String mContent; // 短语的内容
        private int mFavorite; // 是否是收藏状态：0表示未收藏，1表示已收藏；

        public Phrase(String content) {
            this(UUID.randomUUID().toString(), content, 0);
        }

        public Phrase(String content, int favorite) {
            this(UUID.randomUUID().toString(), content, favorite);
        }

        public Phrase(String id, String content, int favorite) {
            mId = id;
            mContent = content;
            mFavorite = favorite;
        }

        public String getId() {
            return mId;
        }

        public void setId(String id) {
            mId = id;
        }

        public String getContent() {
            return mContent;
        }

        public void setContent(String content) {
            mContent = content;
        }

        public int getFavorite() {
            return mFavorite;
        }

        public void setFavorite(int favorite) {
            mFavorite = favorite;
        }
    }
}
