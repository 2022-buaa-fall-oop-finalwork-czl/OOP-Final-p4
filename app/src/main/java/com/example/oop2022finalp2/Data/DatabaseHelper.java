package com.example.oop2022finalp2.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

/**
 * @author Zhilong Cao
 * @description:
 * @date :2022/11/17 18:46
 **/
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_USERLIST = "create table Users("
            + "_id integer primary key autoincrement,"
            + "userid text,"
            + "userpassword text)";
    private DatabaseHelper databaseHelper = null;

    private Context mContext;

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERLIST);
        Toast.makeText(mContext, "Create succeeded",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
