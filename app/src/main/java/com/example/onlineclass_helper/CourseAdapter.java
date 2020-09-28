package com.example.onlineclass_helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Description: List Adapter for lists listview
 */

// ListAdapter 클래스
public class CourseAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    ArrayList<String> list_name;
    ArrayList<Integer> list_id;

    private DBManager db;

    public CourseAdapter(Context c, ArrayList<Integer> id, ArrayList<String> name) {
        list_id = id;
        list_name = name;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //ListAdapter 메서드
    @Override
    public int getCount() {
        return list_name.size();
    }

    @Override
    public Object getItem(int position) {
        return list_name.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    //과목 추가한 view를 추가함?, 데이터베이스와 연결하여
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.item_list, null);
        db = new DBManager(MyApplication.getAppContext());

        TextView nameTextView = (TextView) v.findViewById(R.id.listName_textView);

        final int index = position;

        String name = list_name.get(position);
        nameTextView.setText(name);

        return v;
    }

}