package com.example.onlineclass_helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

// CalendarAdapter 클래스 : CalenderFragment 아래에서 calender에 대한 View 관리

public class MonthAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    // * inflater : xml로 정의된 view (또는 menu 등)를 실제 객체화 시키는 용도

    ArrayList<String> task_items;
    ArrayList<String> task_descriptions;
    ArrayList<Integer>task_id;
    ArrayList<Integer>task_status;

    // db 선언
    private DBManager db;

    // 새로운 날짜가 추가되면 생성자 업데이트
    public MonthAdapter(Context c, ArrayList<Integer> id, ArrayList<String> i, ArrayList<String> d, ArrayList<Integer> status){
        task_id = id;
        task_items = i;
        task_descriptions = d;
        task_status = status;
        mInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // 할일의 수를 가져오는 메소드
    @Override
    public int getCount() {
        return task_items.size();
    }

    // 할일을 가져오는 메소드
    @Override
    public Object getItem(int position) {
        return task_items.get(position);
    }

    // position(int 타입)을 받아서 position(long 타입)으로 변환해 리턴하는 메소드
    // 그냥 position 하면 되는거 아닌가 이 부분 관련해서 수정할 수 있을 듯
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 뷰
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // view
        View v = mInflater.inflate(R.layout.item_task, null);
        // db
        db = new DBManager(MyApplication.getAppContext());

        // task 이름, 메모, 체크박스에 관한 텍스트뷰
        TextView nameTextView = (TextView)v.findViewById(R.id.taskName_TextView);
        TextView decriptionTextView = (TextView) v.findViewById(R.id.taskDes_TextView);
        final CheckBox taskCheckBox = (CheckBox) v.findViewById(R.id.taskCheckBox);

        final int index = position;

        // task 이름, 메모, 상태에 관한 변수
        String name = task_items.get(position);
        String desc = task_descriptions.get(position);
        int status = task_status.get(position);

        // Task 이름, 메모 텍스트뷰
        nameTextView.setText(name);
        decriptionTextView.setText(desc);

        // Task 완료상태
        if (status == 1) { // status : 1 -> 완료상태
            taskCheckBox.setChecked(true);
        }
        else { // status : 0 -> 미완상태
            taskCheckBox.setChecked(false);
        }


        // task(할일) 체크박스 클릭시
        // 완료여부(status) 업데이트
        taskCheckBox.setOnClickListener(new View.OnClickListener() {

            int id = task_id.get(index); // 해당하는 task의 id를 가져옴

            @Override
            public void onClick(View v) {
                // 완료상태
                if(taskCheckBox.isChecked()){
                    db.setTaskComplete(id);
                    Toast.makeText(MyApplication.getAppContext(),"set Complete "+id,Toast.LENGTH_LONG).show();

                }
                // 미완상태
                else{
                    db.setTaskIncomplete(id);
                    Toast.makeText(MyApplication.getAppContext(),"set unComplete "+id,Toast.LENGTH_LONG).show();

                }
            }
        });
        return v;
    }

}