package com.example.record;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 继承自带的SQLiteOpenHelper.并创建diray表
 */
public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(Context context, String name, CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder();
        sql.append("create table if not exists diary(")
                .append("id integer primary key autoincrement,")
                .append("title varchar(50),")
                .append("author varchar(10),")
                .append("content varchar(200),")
                .append("uri varchar(200),")
                .append("createDate varchar(10),")
                .append("noticeDate varchar(10),")
                .append("noticeTime varchar(5) )");
        db.execSQL(sql.toString());

    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }


}
