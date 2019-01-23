package com.camp.bit.todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.AccessControlContext;

//import static com.camp.bit.todolist.db.TodoContract.SQL_CREATE_ENTRIES;

/**
 * Created on 2019/1/22.
 *
 * @author xuyingyi@bytedance.com (Yingyi Xu)
 */
public class TodoDbHelper extends SQLiteOpenHelper {

    // TODO 定义数据库名、版本；创建数据库
    public static  final  int DATABASE_VERSION =1;
    public  static  final  String  DATABASE_NAME="FeedReader.db";


    public TodoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TodoContract.SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TodoContract.SQL_CREATE_ENTRIES);
        onCreate(db);

    }
    public  void onDowngrade(SQLiteDatabase db,int oldVersion ,int newVersion){
        onUpgrade(db,oldVersion,newVersion);
    }

}
