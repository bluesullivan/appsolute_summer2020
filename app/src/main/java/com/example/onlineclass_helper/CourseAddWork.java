package com.example.onlineclass_helper;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Description
 */

// ListAddTask 클래스 : 과목별 할일 list 추가 페이지
public class CourseAddWork extends AppCompatActivity {
    //변수,객체 생성

    private DBManager db; //db
    private ImageView IconBack; //뒤로가기 아이콘
    private Button btnClear; //입력한 내용 초기화 버튼
    private Button btnDone; //할일 등록 버튼
    // private Button btnTest;

    private String workName; //할일 이름
    private String workDes; //할일 설명,내용
    private int workIdOfCourse; //과목별 번호

    EditText tname,tdes; //할일이름,설명 입력받을 edittext
    Spinner lists; //과목 목록 선택하는 spinner

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_add_task);

        db = new DBManager(this);

        tname = (EditText)findViewById(R.id.listaddtask_name);
        tname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tname.getText().clear();
            }
        });

        tdes = (EditText)findViewById(R.id.listaddtask_description);
        tdes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tdes.getText().clear();
            }
        });

        btnClear = (Button)findViewById(R.id.listaddtask_clear);
        btnDone = (Button)findViewById(R.id.listaddtask_Done);
        IconBack = (ImageView)findViewById(R.id.listaddtask_toolbar_back);
        // btnTest = (Button)findViewById(R.id.listaddtask_test);

        viewClear();
        comeBack();
        addTask();

        lists = (Spinner)findViewById(R.id.listaddtask_List_select);
        ArrayList<String> listdownlist = new ArrayList<>();
        final ArrayList<Integer> listdownlist_ID = new ArrayList<>();
        Cursor getMyList = db.getLists();
        while(getMyList.moveToNext()){
            listdownlist_ID.add(getMyList.getInt(0));
            listdownlist.add(getMyList.getInt(0)+ " -- "+getMyList.getString(1));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,listdownlist){
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                View view = super.getDropDownView(position, convertView,parent);
                TextView tv = (TextView) view;
                tv.setBackgroundColor(Color.parseColor("#ff7400"));
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lists.setAdapter(adapter);

        lists.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                workIdOfCourse = listdownlist_ID.get(position);
                Toast.makeText(MyApplication.getAppContext(), "TEST" + workIdOfCourse, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    //tname, tdes (할일 이름, 설명) 입력 받는 메소드
    public void viewClear(){
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tname.getText().clear();
                tdes.getText().clear();
            }
        });
    }

    //입력 끝낸 후 done 버튼 클릭시 처리하는 메소드
    public void addTask(){
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //입력 받기
                workName = tname.getText().toString();
                workDes = tdes.getText().toString();

                Toast.makeText(CourseAddWork.this, workName + workDes + workIdOfCourse, Toast.LENGTH_LONG).show();

                //task를 database로 추가
                if(workName.length() != 0){ //valid input

                    boolean insertTaskList = db.addTaskInList(workName, workDes, workIdOfCourse);
                    if(insertTaskList){
                        finish();
                        Toast.makeText(CourseAddWork.this, "Task Created!", Toast.LENGTH_LONG).show();
                    }else{
                        //show input error
                        Toast.makeText(CourseAddWork.this, "Invalid Input, please check again!"+ workName + workDes + workIdOfCourse, Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(CourseAddWork.this, "Name can't be null !", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // back 버튼 클릭시 돌아가는 메소드
    public void comeBack(){
        IconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}