package com.example.record;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/*
 *保存作者信息
 */
public class InfoActivity extends AppCompatActivity implements View.OnClickListener{
 private Button btn_save;
 private EditText et_name,et_year;
 public  static String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        initView(); //初始化视图
        initData();//初始化数据

    }

    /**
     * 初始化数据；
     */
    private void initData() {
        AuthorInfo authorInfo=SPDataUtils.getAuthorInfo(this);
        if(authorInfo != null && !TextUtils.isEmpty(authorInfo.getAuthor()) ){
            et_name.setText(authorInfo.getAuthor());
            et_year.setText(authorInfo.getYear());
            name =authorInfo.getAuthor();
        }else{
            et_name.setText("派大星");
            et_year.setText("18");
            name = "派大星";
        }
    }

    /*
    初始化
     */
    private void initView() {
       btn_save = findViewById(R.id.button1);
       et_name = findViewById(R.id.et_author);
       et_year = findViewById(R.id.et_year);
       btn_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button1:
                chkAmdSaveData();
                break;
        }
    }

    /**
     * 检查登陆数据并保存
     */
    private void chkAmdSaveData() {
        String author =et_name.getText().toString().trim();
        String year =et_year.getText().toString().trim();
        if(TextUtils.isEmpty(author)){
            showMsg("请输入作者信息");
        }else if(TextUtils.isEmpty(year)){
            showMsg("请输入年龄");
        }else {
            boolean flag= SPDataUtils.saveUserInfo(this,author,year);
            if(flag){
                showMsg("保存数据成功");
                Intent intent =new Intent();
                intent.setClass(this,MainActivity.class);
                startActivity(intent);
            }else{
                showMsg("保存数据失败");
            }
        }
    }

    /**
     * toast显示提示信息
     * @param msg 要显示的内容
     */
    private void showMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}