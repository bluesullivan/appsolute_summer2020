package com.example.onlineclass_helper;

import android.app.DatePickerDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.text.format.DateFormat;
import java.util.Calendar;

// AddTaskActivity : myDB에 Task 추가하는 클래스

public class AddWorkActivity extends AppCompatActivity {
    private Button btnClear;
    private Button btnDone;

    EditText tname, tdes;

    private String workName;
    private String workDes;
    private String workDate;

    private DBManager db;
    private ImageView IconBack;
    private TextView date;
    private DatePickerDialog.OnDateSetListener myDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // 저장할 DB 불러오기
        db = new DBManager(this);

        // Task 이름
        tname = (EditText)findViewById(R.id.addtask_name);
        tname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tname.getText().clear();
            }
        });

        // Task 메모
        tdes = (EditText)findViewById(R.id.addtask_description);
        tdes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tdes.getText().clear();
            }
        });

        btnClear = (Button)findViewById(R.id.addtask_clear);
        btnDone = (Button)findViewById(R.id.addtask_Done);
        IconBack = (ImageView)findViewById(R.id.addtask_toolbar_back);

        date = (TextView) findViewById(R.id.addtask_Date_select);

        addTask();
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

    // Task 추가 메소드
    public void addTask(){
        addDate();
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // input 값 가져오기
                workName = tname.getText().toString();
                workDes = tdes.getText().toString();
                workDate = date.getText().toString();

                // 데이터베이스에 task insert
                if(workName.length() != 0){ //valid input

                    boolean insertTask = db.addTaskData(workName, workDes, workDate);
                    // insert 성공시 : 완료메세지
                    if(insertTask){
                        finish();
                        Toast.makeText(AddWorkActivity.this, "Task created!", Toast.LENGTH_LONG).show();
                    }else{ // insert 실패시 : 에러메세지
                        Toast.makeText(AddWorkActivity.this, "Invalid Input, please check again!"+ workName + workDes + workDate, Toast.LENGTH_LONG).show();
                    }
                }else{ // Name을 입력하지 않았을때 : 에러메세지
                    Toast.makeText(AddWorkActivity.this, "Name can't be null !", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    // Task에 날짜 추가하는 함수
    public void addDate(){
        date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){ //show the dialog for user to select date
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        AddWorkActivity.this, android.R.style.Theme_Material_Light,
                        myDateSetListener, year, month, day);
                dialog.show();
            }
        });
        myDateSetListener = new DatePickerDialog.OnDateSetListener() {
            String m,d,y,dateview;
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(year,month,dayOfMonth);
                m = (String) DateFormat.format("MM",cal);
                d = (String) DateFormat.format("dd",cal);
                y = (String) DateFormat.format("yyyy",cal);
                workDate = m + d + y; // generate the task_date for backend
                date.setText(workDate); // show the date on frontend
            }
        };
    }

    // 뷰 Clear 하는 함수
    public void viewClear(){
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tname.getText().clear();
                tdes.getText().clear();
            }
        });
    }

    // display 하는 함수
    public void display(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

}