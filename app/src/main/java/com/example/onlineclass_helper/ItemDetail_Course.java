package com.example.onlineclass_helper;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
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
 * Author: Jiali
 * Date: Nov. 2018
 * Description: task detail class for list fragment
 * not be used yet
 */
public class ItemDetail_Course extends AppCompatActivity {

    private DBManager db;
    private Integer workId;
    private Integer workofCoureId;
    private ImageView IconBack;

    EditText taskName;
    EditText taskDes;
    Spinner taskList;

    Button btnSave;
    Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail__list);

        db = new DBManager(this);

        taskName = (EditText) findViewById(R.id.listtask_detail_name);
        taskDes = (EditText) findViewById(R.id.listtask_detail_description);
        taskList = (Spinner) findViewById(R.id.listtask_detail_List_select);

        btnSave = (Button) findViewById(R.id.listtask_detail_save);
        btnDelete = (Button) findViewById(R.id.listtask_detail_delete);

        IconBack = (ImageView)findViewById(R.id.listtaskdetail_toolbar_back);
        TextView bar_name = (TextView) findViewById(R.id.listtaskdetail_toolbar_barname);

        Intent intent = getIntent();
        workId = intent.getIntExtra("LIST_TASK_ID",-1);

        Cursor task_detail = db.getTaskbyId(workId);
        //Cursor는 안드로이드 db를 처리하기 위한인터페이스
        if(task_detail.getCount() == 0){
            display("Error","NO Data Found.");
            return;
        }
        // getCount() 커서가 참조 할 수 있는 해당 테이블의 행(Row)의 갯수반환

        while(task_detail.moveToNext()){
            // Cursor를 다음 행(Row)으로 이동
            String one = task_detail.getString(1);
            taskName.setText(one);
            bar_name.setText(task_detail.getString(1));
            taskDes.setText(task_detail.getString(2));

        }

        spinnerSet(taskList);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskname = taskName.getText().toString();
                String taskdes = taskDes.getText().toString();
                db.updateTaskByIdWithList(workId, taskname, taskdes, workofCoureId);
                finish();
            }
        });

        IconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteTaskbyId(workId);
                finish();
            }
        });
        //삭제, 업데이트 버튼

    }

    public void spinnerSet(Spinner taskList){

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
        taskList.setAdapter(adapter);

        //Spinner 객체에서 setOnItemSelectedListener() 리스너를 세팅할때는
        //OnItemSelectedListener 리스터 객체 두개 메소드를 구현필요
        // onItemSelected() onNothingSelected()

        taskList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                workofCoureId = listdownlist_ID.get(position);
                Toast.makeText(MyApplication.getAppContext(), "TEST" + workofCoureId, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void display(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
