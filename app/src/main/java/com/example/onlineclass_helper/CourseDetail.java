package com.example.onlineclass_helper;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Description: list detail class
 */

// ListDetail 클래스 : 강의 리스트 정보 수정 페이지
public class CourseDetail extends AppCompatActivity {
    //변수,객체 선언

    EditText listName; //강의명
    EditText listDes; //강의 설명

    Button btnSave; //강의 저장버튼
    Button btnDelete; //강의 삭제버튼

    private DBManager db;
    private Integer Courseid;
    private ImageView IconBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail);

        db = new DBManager(this);

        listName = (EditText) findViewById(R.id.listdetail_name);
        listDes = (EditText) findViewById(R.id.listdetail_des);

        btnSave = (Button) findViewById(R.id.listdetail_save);
        btnDelete = (Button) findViewById(R.id.listdetail_delete);

        IconBack = (ImageView)findViewById(R.id.listdetail_toolbar_back);
        TextView bar_name = (TextView) findViewById(R.id.listdetail_toolbar_barname);

        Intent intent = getIntent();
        Courseid = intent.getIntExtra("LIST_ID", -1);


        Cursor list_detail = db.getListById(Courseid);
        if(list_detail.getCount() == 0){
            display("Error","NO Data Found.");
            return;
        }

        while(list_detail.moveToNext()){
            String one = list_detail.getString(1);
            listName.setText(one);
            bar_name.setText(list_detail.getString(1));
            listDes.setText(list_detail.getString(2));
        }

        IconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteListbyId(Courseid);
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String listname = listName.getText().toString();
                String listdes = listDes.getText().toString();
                db.updateListById(Courseid, listname, listdes);
                finish();
            }
        });

    }

    // 강의 정보 수정 페이지 실행
    public void display(String title, String message) {
        //알림창
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //알림창 속성 설정
        builder.setCancelable(true);  // 뒤로버튼 클릭시 취소 가능 설정
        builder.setTitle(title); //제목 설정
        builder.setMessage(message); //메세지 설정
        builder.show(); //알림창 띄우기
    }
}