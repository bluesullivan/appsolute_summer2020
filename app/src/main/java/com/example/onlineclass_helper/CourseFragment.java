package com.example.onlineclass_helper;


import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 *  Description: ListFragment class
 *  Todo: 1.listview need update right after(haven't solve yet)
 *        2.task click action
 *        3.selected list changes color
 */

// ListFragment 클래스
public class CourseFragment extends Fragment {

    DBManager myDB;
    int courseID;

    CourseAdapter courseItemAdapter;

    ListView CourseBar;
    ListView workOfcourse_todo;
    ListView getWorkOfCourse_complete;

    ImageView setting;
    ImageView addWork;
    ImageView addCourse;

    TextView coursebar;

    public CourseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //initialize and match layout
        myDB = new DBManager(getActivity());

        Resources res =getResources();
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        coursebar = (TextView)view.findViewById(R.id.listbar_title);

        //Tool바
        setting = (ImageView)view.findViewById(R.id.listbar_setting);


        addWork = (ImageView)view.findViewById(R.id.listbar_addNewList);
        addListTask();  //addtask icon clickListener

        addCourse =(ImageView)view.findViewById(R.id.addlist_imageview);
        addlist();  //addlist icon clickListener

        //right task bar
        workOfcourse_todo = (ListView)view.findViewById(R.id.taskBartodo_ListView);
        getWorkOfCourse_complete = (ListView)view.findViewById(R.id.taskBarcomplete_ListView);

        //left list Bar
        CourseBar = (ListView)view.findViewById(R.id.listNavBar);
        listbarCreated(CourseBar); //the list of lists

        //listviewClicksetting(tasksOfTheList_todo,list_ID,0);
        //listviewClicksetting(getTasksOfTheList_complete,list_ID,1);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume(){

        super.onResume();
        listbarCreated(CourseBar);
        courseItemAdapter.notifyDataSetChanged();

    }

    //
    public static void setDynamicHeight(ListView listView) {
        ItemAdapterTest adapter = (ItemAdapterTest)listView.getAdapter();
        //check adapter if null
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

    // 강의추가 버튼 클릭시 실행
    public void addlist(){
        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Addlist_Intent = new Intent(addCourse.getContext(), AddCourseActivity.class);
                startActivity(Addlist_Intent);
            }
        });
    }

    // 오른쪽상단 할일 추가 버튼 누르면 ListAddTask 클래스
    public void addListTask(){
        addWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Addtask_Intent = new Intent(addWork.getContext(), CourseAddWork.class);
                startActivity(Addtask_Intent);
            }
        });
    }



    // 과목list
    public void listbarCreated(ListView listItems){

        final ArrayList<Integer> myListId = new ArrayList<>();
        final ArrayList<String> list_Name = new ArrayList<>();

        Cursor getMyList = myDB.getLists();
        while (getMyList.moveToNext()) {

            myListId.add(getMyList.getInt(0));
            list_Name.add(getMyList.getString(1));

        }

        courseItemAdapter = new CourseAdapter(MyApplication.getAppContext(),myListId,list_Name);
        listItems.setAdapter(courseItemAdapter);

        // 강의 길게 클릭시 listener
        listItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //get the list_id
                courseID = myListId.get(position);

                Intent list_detail = new  Intent(view.getContext(), CourseDetail.class);
                list_detail.putExtra("LIST_ID", courseID);
                startActivity(list_detail);
                return true;
            }
        });

        // 강의 클릭시 listener
        // 강의 클릭하면 해당 강의별 할일 보여줌
        listItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                courseID = myListId.get(position);
                coursebar.setText(list_Name.get(position));
                //Toast.makeText(MyApplication.getAppContext(), "List" + list_id + "Clicked", Toast.LENGTH_LONG).show();

                final ArrayList<Integer> todoTasksId = new ArrayList<>();
                ArrayList<String> todoTasks = new ArrayList<>();
                ArrayList<String> todoTasksDes = new ArrayList<>();
                ArrayList<Integer> taskStatus = new ArrayList<>();

                //데이터베이스 쿼리: list_id 로 해당 할일 가져옴
                Cursor list_task = myDB.getTaskByList(courseID,0);

                while(list_task.moveToNext()) {
                    todoTasksId.add(list_task.getInt(0));
                    todoTasks.add(list_task.getString(1));
                    todoTasksDes.add(list_task.getString(2));
                    taskStatus.add(list_task.getInt(4));
                }
                ItemAdapterTest taskItemAdapter = new ItemAdapterTest(MyApplication.getAppContext(),todoTasksId,todoTasks,todoTasksDes,taskStatus);
                workOfcourse_todo.setAdapter(taskItemAdapter);
                setDynamicHeight(workOfcourse_todo); //set dynmic height for listview

                //listviewClicksetting(tasksOfTheList_todo,list_ID,0); //click to-do task action

                final ArrayList<Integer> ComTasksId = new ArrayList<>();
                ArrayList<String> completeTask = new ArrayList<>();
                ArrayList<String> completeTaskDes = new ArrayList<>();
                ArrayList<Integer> CompletetaskStatus = new ArrayList<>();

                Cursor list_task_completed = myDB.getTaskByList(courseID,1);

                while(list_task_completed.moveToNext()) {
                    ComTasksId.add(list_task_completed.getInt(0));
                    completeTask.add(list_task_completed.getString(1));
                    completeTaskDes.add(list_task_completed.getString(2));
                    CompletetaskStatus.add(list_task_completed.getInt(4));
                }
                ItemAdapterTest taskItemAdapter2 = new ItemAdapterTest(MyApplication.getAppContext(),ComTasksId,completeTask,completeTaskDes,CompletetaskStatus);
                getWorkOfCourse_complete.setAdapter(taskItemAdapter2);
                setDynamicHeight(getWorkOfCourse_complete); //set dynmic height for listview

                //listviewClicksetting(getTasksOfTheList_complete,list_ID,1); //click complete task action

                /*
                getTasksOfTheList_complete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int task_detail_2 = ComTasksId.get(position);
                        Intent task = new Intent(view.getContext(), ItemDetail_List.class);
                        task.putExtra("LIST_TASK_ID",task_detail_2);
                        startActivity(task);
                    }
                });
                */

            }
        });

    }

    //click the task, jump to the task detail page
    public void listviewClicksetting(ListView temp, final int listID, final int status){

        temp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final ArrayList<Integer> TasksId = new ArrayList<>();

                //database query: get corresponding task by list_id
                Cursor list_task = myDB.getTaskByList(listID,status);
                while(list_task.moveToNext()) {
                    TasksId.add(list_task.getInt(0));
                }

                int taskid = TasksId.get(position);

                //Toast.makeText(MyApplication.getAppContext(), "task" + taskid + "Clicked", Toast.LENGTH_LONG).show();
                Intent task_detail = new Intent(view.getContext(), ItemDetail_Course.class);
                task_detail.putExtra("LIST_TASK_ID",taskid);
                startActivity(task_detail);
            }
        });


    }
}