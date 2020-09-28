package com.example.onlineclass_helper;


import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;


// calenderFragment 클래스 : 월별 페이지 관리

public class MonthFragment extends Fragment {

    CalendarView calendarView;
    TextView myDate;
    ListView myList;

    ImageView mysetting;
    ImageView addNewTask;

    DBManager myDB;
    MonthAdapter calendarItemAdapter;

    // 빈 생성자
    public MonthFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 캘린더 Fragement에 관해 레이아웃 inflate
        Resources res = getResources();
        View view = inflater.inflate(R.layout.fragment_calender, container, false);

        // 캘린더, 날짜, 리스트, setting
        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        myDate = (TextView) view.findViewById(R.id.calendarbar_text);
        myList = (ListView) view.findViewById(R.id.task_ListView);
        mysetting = (ImageView) view.findViewById(R.id.calendarbar_setting);


        // Task 추가
        addNewTask = (ImageView) view.findViewById(R.id.calendarbar_addNewList);
        addNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Addtask_Intent = new Intent(addNewTask.getContext(), AddWorkActivity.class);
                startActivity(Addtask_Intent);
            }
        });

        myDB = new DBManager(getActivity());
        viewSetting();

        // item, description, id, status 담을 ArrayList
        final ArrayList<String> taskItems = new ArrayList<>();
        final ArrayList<String> taskDes = new ArrayList<>();
        final ArrayList<Integer> taskId = new ArrayList<>();
        final ArrayList<Integer> taskStatus = new ArrayList<>();

        // 캘린더에서 날짜 선택시
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                Calendar cal_2 = Calendar.getInstance();
                cal_2.set(year,month,day);
                String m = (String) DateFormat.format("MM",cal_2);
                String d = (String) DateFormat.format("dd",cal_2);
                String y = (String) DateFormat.format("yyyy",cal_2);

                String task_date_2 = m + d + y; // generate the task_date for backend
                String date = (month + 1) + "/" + day + "/" + year;
                myDate.setText(date);

                taskId.clear();
                taskItems.clear();
                taskDes.clear();
                taskStatus.clear();

                // 해당 날짜의 task 가져오기
                Cursor todo_task_data = myDB.getTaskbyDate(task_date_2,0);
                while(todo_task_data.moveToNext()) {
                    taskId.add(todo_task_data.getInt(0));
                    taskItems.add(todo_task_data.getString(1));
                    taskDes.add(todo_task_data.getString(2));
                    taskStatus.add(todo_task_data.getInt(4));
                }

                MonthAdapter calendarItemAdapter= new MonthAdapter(getContext(), taskId, taskItems, taskDes,taskStatus);
                myList.setAdapter(calendarItemAdapter);
            }


        });

        // 아이템 클릭시 해당 Task의 Detail 보여주기
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int taskid = taskId.get(position);
                Intent item_detail = new Intent(view.getContext(), ItemDetail.class);
                item_detail.putExtra("TASK_ID",taskid);
                startActivity(item_detail);
                //Toast.makeText(MyApplication.getAppContext(),"Item clicked "+taskid,Toast.LENGTH_LONG).show();
            }
        });



        return view;
    }

    @Override
    public void onResume(){

        super.onResume();
        viewSetting();
        calendarItemAdapter.notifyDataSetChanged();

    }


    // view 보여주는 메소드
    public void viewSetting(){
        // 캘린더 가져오기
        Calendar cal = Calendar.getInstance();

        // 날짜 가져오기
        final String mm = (String) DateFormat.format("MM",cal);
        final String dd = (String) DateFormat.format("dd",cal);
        final String yy = (String) DateFormat.format("yyyy",cal);
        String task_date = mm+dd+yy;
        myDate.setText(task_date);

        // task item, 메모, id, 완료상태 담을 ArrayList
        final ArrayList<String> taskItems = new ArrayList<>();
        final ArrayList<String> taskDes = new ArrayList<>();
        final ArrayList<Integer> taskId = new ArrayList<>();
        final ArrayList<Integer> taskStatus = new ArrayList<>();

        // 해당 날짜에 대한 Task 가져오기
        Cursor todo_task_data = myDB.getTaskbyDate(task_date,0);
        while(todo_task_data.moveToNext()) {
            taskId.add(todo_task_data.getInt(0));
            taskItems.add(todo_task_data.getString(1));
            taskDes.add(todo_task_data.getString(2));
            taskStatus.add(todo_task_data.getInt(4));
        }

        // CalendarItemAdapter 가져오기
        calendarItemAdapter= new MonthAdapter(getContext(), taskId, taskItems, taskDes,taskStatus);
        myList.setAdapter(calendarItemAdapter);

        // item 클릭시 itemDetail
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int taskid = taskId.get(position);
                Intent item_detail = new Intent(view.getContext(), ItemDetail.class);
                item_detail.putExtra("TASK_ID",taskid);
                startActivity(item_detail);
                //Toast.makeText(MyApplication.getAppContext(),"Item clicked "+taskid,Toast.LENGTH_LONG).show();
            }
        });
    }
}