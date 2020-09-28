package com.example.onlineclass_helper;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

public class ItemDetail extends AppCompatActivity {

    private DBManager db;
    private Integer workId;
    private ImageView IconBack;

    EditText taskName;
    EditText taskDes;
    TextView taskDate;

    Button btnSave;
    Button btnDelete;

    private DatePickerDialog.OnDateSetListener myDateSetListener;  //시간을 입력받을수 있는 DatePickerDialog
    //OnDateSetListener를 구현하고 그 안에 onDateSet() 함수를 오버 라이딩이 필요함

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        db = new DBManager(this);

        taskName = (EditText) findViewById(R.id.taskDetail_name);
        taskDes = (EditText) findViewById(R.id.taskDetail_description);
        taskDate = (TextView) findViewById(R.id.taskDetail_date);

        btnSave = (Button) findViewById(R.id.taskDetail_Edit);
        btnDelete = (Button) findViewById(R.id.taskDetail_Delete);

        IconBack = (ImageView)findViewById(R.id.taskdetail_toolbar_back);
        TextView bar_name = (TextView) findViewById(R.id.taskdetail_toolbar_barname);

         /*======================================================================================
         * get the intent 's content from task_fragment ListView
         * by "TASK_ID"
       ======================================================================================*/
        Intent intent = getIntent();
        workId = intent.getIntExtra("TASK_ID",-1);
        //activity끼리 데이터전달을 위해
        //intent.getIntExtra(“key”, default_value) defaultvalue가 필요


        /*======================================================================================
         * SQL query: get the task by given its ID;
         * And show the data into the front end
        ======================================================================================*/
        Cursor task_detail = db.getTaskbyId(workId);
        //Cursor는 안드로이드 db를 처리하기 위한인터페이스

        if(task_detail.getCount() == 0){
            display("Error","NO Data Found.");
            return;
        }
        // getCount() 커서가 참조 할 수 있는 해당 테이블의 행(Row)의 갯수반환, 0이면 뭔가가 없는것이니

        while(task_detail.moveToNext()){
            // Cursor를 다음 행(Row)으로 이동
            String one = task_detail.getString(1);
            taskName.setText(one);
            bar_name.setText(task_detail.getString(1));
            taskDes.setText(task_detail.getString(2));
            String temp_date = task_detail.getString(3);
            String m = temp_date.substring(0,2);
            String d = temp_date.substring(2,4);
            String y = temp_date.substring(4);
            String new_temp = m + "/" + d + "/"+ y;
            taskDate.setText(temp_date);
        }
        //???????
        addDate();//?

          /*======================================================================================
         * click Icon back, back to previous page
       ======================================================================================*/
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
        //삭제버튼

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskname = taskName.getText().toString();
                String taskdes = taskDes.getText().toString();
                String taskdate = taskDate.getText().toString();
                db.updateTaskByIdNoList(workId, taskname, taskdes, taskdate);
                finish();
            }
        });
        //업데이트 버튼
    }

    public void display(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this); //알림창, 여기서 this는 Activity의 this
        builder.setCancelable(true);
        builder.setTitle(title);   // 제목 설정
        builder.setMessage(message);
        builder.show(); // 알림창 띄우기
    }

    public void addDate(){
        taskDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){ //show the dialog for user to select date
                //setonCickListener은 버튼클릭 리스너

                Calendar cal = Calendar.getInstance(); //캘린더객체 이용
                int year = cal.get(Calendar.YEAR); //cal.get으로 값을 얻어옴
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        ItemDetail.this, android.R.style.Theme_Material_Light,
                        myDateSetListener, year, month, day);
                //팝업 형태의 DatePickerDialog은 DatePickerDialog 클래스를 따로 제공
                //OnDateSetListener가 구현되어있어야함
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //
                dialog.show();//화면에 보여줌
            }
        });
        myDateSetListener = new DatePickerDialog.OnDateSetListener() {
            //OnDateSetListener구현, onDateSet도 구현
            String m,d,y,dateview;
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(year,month,dayOfMonth);
                m = (String) DateFormat.format("MM",cal);
                d = (String) DateFormat.format("dd",cal);
                y = (String) DateFormat.format("yyyy",cal);
                String temp_date_1 = m + d + y; // generate the task_date for backend
                //dateview = m + "/" + d + "/"+ y;
                taskDate.setText(temp_date_1);
            }
        };
    }


}
