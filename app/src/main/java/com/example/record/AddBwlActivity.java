package com.example.record;


import static android.content.ContentValues.TAG;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;
import com.google.android.material.bottomsheet.BottomSheetDialog;

/**
 * 添加内容到数据库活动
 */
public class AddBwlActivity extends Activity {
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HHmmss", Locale.CHINA);//用以生成当前时间的文件，避免文件名重复
    private EditText etDate = null,editAuthor=null,etTime=null,etTitle=null,etContent=null;
    private Button btnSave = null;
    private ImageView imageView;
    static final int DATE_DIALOG_ID = 0;  //日期
    static final int TIME_DIALOG_ID = 1;  //时间
    private int REQUEST_CAMERA=110;      //选择相机时的值
    private int REQUEST_PICKER=111;      //选择相册时的值
    private DbHelper dbhelper;
    private  File file=null;
    private  File file1=new File("");
    Bundle bundle;     //打包数据
    private Uri uri;   //图片的uri

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bwl);
        init();//初始化相机，相册
        dbhelper = new DbHelper(this, "DB2", null, 2);
        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        editAuthor=findViewById(R.id.author);
        editAuthor.setText(SPDataUtils.getAuthorInfo(this).getAuthor());
        etDate = findViewById(R.id.etDate);
        etDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //并会调用 onCreateDialog(int)回调函数来请求一个Dialog
                showDialog(DATE_DIALOG_ID);

            }
        });

        etTime = (EditText)findViewById(R.id.etTime);
        etTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //并会调用 onCreateDialog(int)回调函数来请求一个Dialog
                showDialog(TIME_DIALOG_ID);

            }
        });


        btnSave = (Button)findViewById(R.id.btnSave);//保存按键
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                ContentValues value = new ContentValues(); //类似bundle，存基本类型的值，也类似与MAP<k,v>;
                String title = etTitle.getText().toString();
                String author =editAuthor.getText().toString();
                String content = etContent.getText().toString();
                String noticeDate = etDate.getText().toString();
                String noticeTime = etTime.getText().toString();
                //输入值不能为空！
                if(title.equals("")){
                    Toast.makeText(AddBwlActivity.this,"标题不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(author.equals("")){
                    Toast.makeText(AddBwlActivity.this,"作者不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(content.equals("")){
                    Toast.makeText(AddBwlActivity.this,"内容不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(noticeDate.equals("")){
                    Toast.makeText(AddBwlActivity.this,"日期不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(noticeTime.equals("")){
                    Toast.makeText(AddBwlActivity.this,"时间不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                //保证这一份日期的数据
                value.put("title", title);
                value.put("author",author);
                value.put("content", content);
                value.put("uri",file1.toString().trim());
                value.put("noticeDate", noticeDate);
                value.put("noticeTime", noticeTime);


                SQLiteDatabase db = dbhelper.getWritableDatabase();

                long id = 0;

                long status = 0;
                if(bundle!=null){//说明为修改内容，获得id修改哪一个
                    id = bundle.getLong("id");
                    status = db.update("diary", value, "id=?", new String[]{bundle.getLong("id")+""});
                }else{
                    status = db.insert("diary", null, value);
                    id = status;
                }
                 //不为-1说明插入成功
                if(status!=-1){
                    //setAlarm(id);
                    Toast.makeText(AddBwlActivity.this, "保存成功", Toast.LENGTH_LONG).show();
                    Intent intent =new Intent();
                    intent.setClass(AddBwlActivity.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(AddBwlActivity.this, "保存失败", Toast.LENGTH_LONG).show();
                }
            }
        });

        //获取上一个activity的传值
        bundle = this.getIntent().getExtras();
        if(bundle!=null){
            etDate.setText(bundle.getString("noticeDate"));
            etTime.setText(bundle.getString("noticeTime"));
            etTitle.setText(bundle.getString("title"));
            editAuthor.setText(bundle.getString("author"));
            etContent.setText(bundle.getString("content"));
            if(!bundle.getString("uri").equals("")){ //如果该uri不为空，则调用Glide加载图片
                Glide.with(this).load(bundle.getString("uri")).into(imageView);
            }
        }
    }


    /**
     * 获取日期
     */
    private OnDateSetListener dateSetListener = new OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {

            StringBuilder dateStr = new StringBuilder();
            dateStr.append(year).append("-")
                    .append(month+1).append("-")
                    .append(day);

            etDate.setText(dateStr.toString());
        }
    };

    /**
     * 获取时间
     */
    private OnTimeSetListener timeSetListener = new OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {


            StringBuilder timeStr = new StringBuilder();
            timeStr.append(hour).append(":")
                    .append(minute);

            etTime.setText(timeStr.toString());
        }
    };

    /**
     * 当Activity调用showDialog函数时会触发该函数的调用
     * 引用网络
     */
    protected Dialog onCreateDialog(int id){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        switch(id){
            case DATE_DIALOG_ID:
                DatePickerDialog dpd = new DatePickerDialog(this,dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                dpd.setCancelable(true);
                dpd.setTitle("选择日期");
                dpd.show();
                break;
            case TIME_DIALOG_ID:
                TimePickerDialog tpd = new TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
                tpd.setCancelable(true);
                tpd.setTitle("选择时间");
                tpd.show();
                break;
            default:
                break;
        }
        return null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_CAMERA){
                Glide.with(this).load(file).into(imageView);
                Bitmap bitmap=getBitmapFromUri(uri);
                getFile(bitmap);
            }else if(requestCode==REQUEST_PICKER){
                try {
                   Uri data1= data.getData();
                   Bitmap bitmap=null;
                   ContentResolver contentResolver= getContentResolver();
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data1);
                    getFile(bitmap);
                    String[] column ={MediaStore.Images.ImageColumns.DATA};
                   Cursor query= contentResolver.query(data1,column,null,null,null);
                   query.moveToNext();
                   int columnIndex =query.getColumnIndex(column[0]);
                   String string =query.getString(columnIndex);

                   Glide.with(this).load(string).into(imageView);

                }catch (NullPointerException e){
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void init() {
        imageView =findViewById(R.id.photo);
        String[] mode = {"打开相机","相册","取消"};
        ListView listView = new ListView(this);
        listView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));//list布局，ViewGroup是View容器类
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,mode));
        BottomSheetDialog dialog =new BottomSheetDialog(this);//从底层向上拉出显示
        dialog.setContentView(listView);
        //当点击图片，出现更改选项
        imageView.setOnClickListener(v -> {
            dialog.show();
        });
        //开始头像模块
        listView.setOnItemClickListener((parent, view, position, id) -> {
            switch (position){
                case 0:
                    Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    file= new File(getExternalCacheDir(),simpleDateFormat.format(new Date())+".jpg");
                    uri = FileProvider.getUriForFile(this,"com.example.record.fileProvider",file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);//k+v
                    startActivityForResult(intent,REQUEST_CAMERA); //第一个为请求的活动，第二个为请求码前面定义110
                    break;
                case 1:
                    Intent intent1=new Intent(Intent.ACTION_PICK);
                    intent1.setType("image/*");
                    startActivityForResult(intent1,REQUEST_PICKER);
                    break;
                case 2:
                    dialog.dismiss();
                default:
                    break;
            }
        });
    }
    private Bitmap getBitmapFromUri(Uri uri)
   {
           try
           {
                // 读取uri所在的图片
               Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                return bitmap;
               }
           catch (Exception e)
           {
               Log.e("[Android]", e.getMessage());
               Log.e("[Android]", "目录为：" + uri);
               e.printStackTrace();
               return null;
              }
          }
    public File getFile(Bitmap bitmap) {

        file1 = new File(getFilesDir(), simpleDateFormat.format(new Date())+".jpg");
        System.out.println(file1.toString().trim());

        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file1));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//压缩图片
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

}