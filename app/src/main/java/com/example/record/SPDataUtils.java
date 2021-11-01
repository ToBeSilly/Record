package com.example.record;

import android.content.Context;
import android.content.SharedPreferences;

/*

 * 使用SharedPreferences实现数据存取工具类
 */
public class SPDataUtils {
    private static final String myFileName = "mydata";

    /*
     * 保存用户信息
     * @param context 上下文
     * @param autho 作者
     * @param year 年龄
     * @return boolean 结果
     */
    public static boolean saveUserInfo(Context context, String author, String year) {
        boolean flag = false;
        SharedPreferences sp = context.getSharedPreferences(myFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("author", author);
        editor.putString("year", year);
        editor.commit();
        flag = true;
        return flag;
    }

    /**
     * 获取用户信息的方法
     *
     * @param context 上下文
     * @return 作者实例
     */
    public static AuthorInfo getAuthorInfo(Context context) {
        AuthorInfo authorInfo = new AuthorInfo();
        SharedPreferences sp = context.getSharedPreferences(myFileName, Context.MODE_PRIVATE);
        String author = sp.getString("author", null);
        String year = sp.getString("year", null);
        authorInfo.setAuthor(author);
        authorInfo.setYear(year);
        return  authorInfo;
    }
}