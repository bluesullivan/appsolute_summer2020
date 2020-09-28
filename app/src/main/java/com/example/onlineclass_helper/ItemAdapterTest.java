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

/**
 * Author: Jiali
 * Date: Nov. 2018
 * Description: ItemAdapter for tasks' listview
 */
public class ItemAdapterTest extends BaseAdapter {

    LayoutInflater mInflater;
    ArrayList<String> task_items;
    ArrayList<String> task_descriptions;
    ArrayList<Integer> task_id;
    ArrayList<Integer> task_status;

    private DBManager db;

    public ItemAdapterTest(Context c, ArrayList<Integer> id, ArrayList<String> i, ArrayList<String> d, ArrayList<Integer> status) {
        task_id = id;
        task_items = i;
        task_descriptions = d;
        task_status = status;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return task_items.size();
    }
    // Adapter가 관리할 Data의 개수를 설정

    @Override
    public Object getItem(int position) {
        return task_items.get(position);
    }
    // Adapter가 관리하는 Data의 Item 의 Position을 <객체> 형태

    @Override
    public long getItemId(int position) {
        return position;
    }
    // Adapter가 관리하는 Data의 Item 의 position 값의 ID

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // ListView의 뿌려질 한줄의 Row를 설정
        View v = mInflater.inflate(R.layout.item_task, null);
        //view를 객체화 R.layout.item_task에 해당하는 뷰를
        db = new DBManager(MyApplication.getAppContext());

        TextView nameTextView = (TextView) v.findViewById(R.id.taskName_TextView);
        TextView decriptionTextView = (TextView) v.findViewById(R.id.taskDes_TextView);
        final CheckBox taskCheckBox = (CheckBox) v.findViewById(R.id.taskCheckBox);

        final int index = position;

        String name = task_items.get(position);
        String desc = task_descriptions.get(position);
        int status = task_status.get(position);

        nameTextView.setText(name);
        decriptionTextView.setText(desc);
        //testView에 텍스트를 넣어줌

        if (status == 1) {
            taskCheckBox.setChecked(true);
            //체크박스 체크를했으면
        } else {
            taskCheckBox.setChecked(false);
        }

        taskCheckBox.setOnClickListener(new View.OnClickListener() {
            //체크박스리스너
            int id = task_id.get(index);

            @Override
            public void onClick(View v) {
                if(taskCheckBox.isChecked()){
                    db.setTaskComplete(id);
                    Toast.makeText(MyApplication.getAppContext(),"set Complete "+id,Toast.LENGTH_LONG).show();

                }
                else{
                    db.setTaskIncomplete(id);
                    Toast.makeText(MyApplication.getAppContext(),"set unComplete "+id,Toast.LENGTH_LONG).show();

                }
            }
        });

        return v;
    }

}