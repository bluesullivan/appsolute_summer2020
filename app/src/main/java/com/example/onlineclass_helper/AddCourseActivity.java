package com.example.onlineclass_helper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

// AddListActivity 클래스 : List 추가하는 클래스


public class AddCourseActivity extends AppCompatActivity {
        private Button btnClear;
    private Button btnDone;
    private Button btnTest;
    private String workName;
    private String workDes;
    EditText lname, ldes;

    private DBManager db;
    private ImageView IconBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        db = new DBManager(this);

        lname = (EditText)findViewById(R.id.addlist_name);
        lname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lname.getText().clear();
            }
        });

        ldes = (EditText)findViewById(R.id.addlist_description);
        ldes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ldes.getText().clear();
            }
        });

        btnClear = (Button)findViewById(R.id.addlist_clear);
        btnDone = (Button)findViewById(R.id.addlist_Done);
        IconBack = (ImageView)findViewById(R.id.addlist_toolbar_back);

        addList();
        viewClear();
        comeBack();
    }

    // 뒤로가기
    public void comeBack(){
        IconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // 뷰 Clear
    public void viewClear(){
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lname.getText().clear();
                ldes.getText().clear();
            }
        });
    }

    // 리스트 추가
    public void addList(){
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //input 값들 가져오기(list name, 메모)
                workName = lname.getText().toString();
                workDes = ldes.getText().toString();

                // Database에 list insert
                if(workName.length() != 0){ // input이 있을때
                    boolean insertList = db.addListData(workName, workDes);
                    if(insertList){ //생성 완료
                        finish();
                        Toast.makeText(AddCourseActivity.this, "List created!", Toast.LENGTH_LONG).show();
                    }else{ // input에러 다시 확인하십시오
                        Toast.makeText(AddCourseActivity.this, "Invalid Input, please check again!"+ workName + workDes, Toast.LENGTH_LONG).show();
                    }
                }else{ // Name을 입력하지 않았을 때! 에러메세지
                    Toast.makeText(AddCourseActivity.this, "Name can't be null !", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}