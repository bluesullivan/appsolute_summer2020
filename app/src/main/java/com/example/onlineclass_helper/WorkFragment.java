package com.example.onlineclass_helper;


import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

public class WorkFragment extends Fragment {

    ListView myToDoTasksList;   // 해야할 일 List
    ListView myComTasksList;    // 완료한 List

    ImageView tasktoolBarSetting;   //툴바 아이콘 셋팅
    ImageView tasktoolbarAdd;   //Task 아이콘 셋팅

    DBManager myDB;    // database

    ItemAdapterTest taskItemAdapter;    //TaskList item Adapter

    // 빈생성자
    public WorkFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //초기화
        Resources res =getResources();
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        myDB = new DBManager(getActivity());

        //리스트뷰 (해야할일 + 완료한 일)
        myToDoTasksList = (ListView)view.findViewById(R.id.tasklist_ListView);
        myComTasksList = (ListView)view.findViewById(R.id.completelist_ListView);

        //툴바 이미지뷰 (셋팅 + 추가) : 셋팅은 삭제 가능
        tasktoolBarSetting = (ImageView)view.findViewById(R.id.listbar_setting);
        tasktoolbarAdd = (ImageView)view.findViewById(R.id.listbar_addNewList);

        listViewCreate(myToDoTasksList,0);
        listViewCreate(myComTasksList,1);


        // Task 추가
        // 사용자가 추가(+)아이콘 클릭시, AddTaskActivity 로 넘어감
        tasktoolbarAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Addtask_Intent = new Intent(tasktoolbarAdd.getContext(), AddWorkActivity.class);
                startActivity(Addtask_Intent);
            }
        });

        // Setting 아이콘
        // Setting 아이콘 클릭시 메뉴를 보여줌
        // 메뉴에서 item 클릭시, 해당하는 페이지로 이동
        // 여기 삭제 가능한 부분!!

        return view;
    }

    // 리스트뷰 업데이트
    @Override
    public void onResume(){
        super.onResume();
        listViewCreate(myToDoTasksList,0);
        listViewCreate(myComTasksList,1);
        taskItemAdapter.notifyDataSetChanged();

    }


    // List뷰를 변동가능한 사이즈로 만들어줌
    // 두개의 List뷰를 하나의 페이지에 나타날 수 있도록!

    public static void setDynamicHeight(ListView listView) {
        ItemAdapterTest adapter = (ItemAdapterTest)listView.getAdapter();
        // adapter가 널값인지 체크
        if (adapter == null) {
            return;
        }
        int height = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            height += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        layoutParams.height = height + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(layoutParams);
        listView.requestLayout();
    }

    // Task에 대한 리스트뷰 생성
    public void listViewCreate(ListView temp, int status){
        ArrayList<String> taskItems = new ArrayList<>();
        ArrayList<String> taskDes = new ArrayList<>();
        ArrayList<Integer> taskStatus = new ArrayList<>();
        final ArrayList<Integer> taskId = new ArrayList<>();

        Calendar c = Calendar.getInstance();
        String d = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        String m = Integer.toString(c.get(Calendar.MONTH)+1);
        String y = Integer.toString(c.get(Calendar.YEAR));

        m = (String) DateFormat.format("MM",c);
        d = (String) DateFormat.format("dd",c);
        y = (String) DateFormat.format("yyyy",c);

        String task_date = m+d+y;

        // 데이터베이스에서 해당하는 날짜의 Task 를 Select 하여 가져옴
        Cursor todo_task_data = myDB.getTaskbyDate(task_date,status);
        while(todo_task_data.moveToNext()) {
            taskId.add(todo_task_data.getInt(0));
            taskItems.add(todo_task_data.getString(1));
            taskDes.add(todo_task_data.getString(2));
            taskStatus.add(todo_task_data.getInt(4));

        }
        taskItemAdapter = new ItemAdapterTest(MyApplication.getAppContext(), taskId,taskItems, taskDes, taskStatus);
        temp.setAdapter(taskItemAdapter);
        setDynamicHeight(temp); //set dynmic height for listview

        // Task 클릭시
        temp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int taskid = taskId.get(position);
                Intent item_detail = new Intent(view.getContext(), ItemDetail.class);
                item_detail.putExtra("TASK_ID",taskid);
                startActivity(item_detail);
            }
        });

    }

}