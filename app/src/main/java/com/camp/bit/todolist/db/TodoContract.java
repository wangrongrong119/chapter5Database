package com.camp.bit.todolist.db;

import android.provider.BaseColumns;

import com.camp.bit.todolist.beans.State;

import java.util.Date;

/**
 * Created on 2019/1/22.
 *
 * @author xuyingyi@bytedance.com (Yingyi Xu)
 */
public final class TodoContract {

    // TODO 定义表结构和 SQL 语句常量
    public static final class FeedReaderContract{
        private FeedReaderContract(){}
        // 1.   内部类一般只为其外部类使用；
        //
        //   2.   内部类提供了某种进入外部类的窗户；
        //
        //   3.   也是最吸引人的原因，每个内部类都能独立地继承一个接口，??
    }
    public static class FeedEntry implements BaseColumns{

        public  static final String TABLE_NAME="notebook";
        public  static final String DATE="date";
        public  static final String STATE="state";
        public  static final String CONTENT="content";



    }
    public   static  final String SQL_CREATE_ENTRIES=
            "CREATE TABLE "+FeedEntry.TABLE_NAME+"("
                    +FeedEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"
                    +FeedEntry.DATE+" DATE ,"
                    +FeedEntry.STATE+" INT ,"
                    +FeedEntry.CONTENT+" STRING )";

    private  static final String SQL_DELETE_ENTRIES=
            "DROP TABLE IF EXITS"+FeedEntry.TABLE_NAME;



    private TodoContract() {
    }

}
