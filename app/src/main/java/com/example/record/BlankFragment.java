package com.example.record;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static String mParam1;
    // TODO: Rename and change types of parameters
    View listView;
    private DbHelper dbhelper;
    private SQLiteDatabase db;
    SimpleCursorAdapter adapter;
    String uri;
    public BlankFragment() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *此方法在本实验中其实不需要使用
     * @param param1 Parameter 1.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1) {
        mParam1=param1;
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(listView == null){ //防止重复的搭建listView
        listView =  inflater.inflate(R.layout.view_one, container, false);
    }
        initView(); //初始化碎片内容
        return listView;
    }

    /**
     * 初始化listView，数据库，adapter，以及listView的点击处理事件
     */
    public void initView() {

        ListView listView1 = listView.findViewById(R.id.lv_bwlList);
        dbhelper = new DbHelper(getActivity(), "DB2", null, 2);
        db = dbhelper.getReadableDatabase();
        Cursor cursor = db.query( "diary", new String[]{"id as _id","title","author","content","uri","noticeDate","noticeTime"}, null, null, null, null, null);
         adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.list_item_bwl,
                cursor,
                new String[]{"title", "noticeDate", "noticeTime","author", "content"},
                new int[]{R.id.title, R.id.noticeDate, R.id.noticeTime,R.id.author, R.id.content,},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        );
        listView1.setAdapter(adapter);
        this.registerForContextMenu(listView1);  //启动长按选择菜单
        //listView点击事件
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String title = ((TextView) view.findViewById(R.id.title)).getText().toString();
                String content = ((TextView) view.findViewById(R.id.content)).getText().toString();
                String author =((TextView) view.findViewById(R.id.author)).getText().toString();
                Cursor cursor = db.query("diary", new String[]{"uri"}, "id=?", new String[]{""+id}, null, null, null,"1");
                cursor.moveToFirst();
                String uri =cursor.getString(0);
                System.out.println(uri);
                String noticeDate = ((TextView) view.findViewById(R.id.noticeDate)).getText().toString();
                String noticeTime = ((TextView) view.findViewById(R.id.noticeTime)).getText().toString();
                Intent intent = new Intent();
                intent.setClass(getActivity(), AddBwlActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("id", id);
                bundle.putString("title", title);
                bundle.putString("author",author);
                bundle.putString("content", content);
                bundle.putString("uri",uri);
                bundle.putString("noticeDate", noticeDate);
                bundle.putString("noticeTime", noticeTime);

                intent.putExtras(bundle);

                startActivity(intent);
            }

        });

    }



    //长按的上下文菜单
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderIcon(R.drawable.alarm);
        menu.add(0, 3, 0, "修改");
        menu.add(0, 4, 0, "删除");
    }
    //菜单具体内容，3为修改，4为删除
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case 3:
                String title = ((TextView) menuInfo.targetView.findViewById(R.id.title)).getText().toString();
                String content = ((TextView) menuInfo.targetView.findViewById(R.id.content)).getText().toString();
                String author =((TextView) menuInfo.targetView.findViewById(R.id.author)).getText().toString();
                Cursor cursor1 = db.query("diary", new String[]{"uri"}, "id=?", new String[]{""+menuInfo.id}, null, null, null,"1");
                cursor1.moveToFirst();
                String uri =cursor1.getString(0);
                String noticeDate = ((TextView) menuInfo.targetView.findViewById(R.id.noticeDate)).getText().toString();
                String noticeTime = ((TextView) menuInfo.targetView.findViewById(R.id.noticeTime)).getText().toString();
                Intent intent = new Intent();
                intent.setClass(getActivity(), AddBwlActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("id", menuInfo.id);
                bundle.putString("title", title);
                bundle.putString("author",author);
                bundle.putString("content", content);
                bundle.putString("uri",uri);
                bundle.putString("noticeDate", noticeDate);
                bundle.putString("noticeTime", noticeTime);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case 4:
                int status = db.delete( "diary", "id=?", new String[]{"" + menuInfo.id});

                if (status != -1) {
                    //删除后更新listview
                    Cursor cursor = db.query( "diary", new String[]{"id as _id", "title", "author","content","uri", "noticeDate", "noticeTime"}, null, null, null, null, null);
                    adapter.changeCursor(cursor);
				    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_LONG).show();
                }

                break;

        }


        return true;
    }
    //重写onResume，被暂停后恢复可交互；
    public void onResume() {
        super.onResume();
        Cursor cursor = db.query( "diary", new String[]{"id as _id", "title","author", "content","uri", "noticeDate", "noticeTime"}, null, null, null, null, null);
        adapter.changeCursor(cursor);
    }

}