package com.example.onlineclass_helper;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {


    private CourseFragment listFrag;
    private WorkFragment taskFrag;
    private MonthFragment calenderFrag;
    private BottomNavigationView mainNav;
    private FrameLayout mainFrame;

    DBManager myDB;    //SQLite database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskFrag = new WorkFragment();
        listFrag = new CourseFragment();
        calenderFrag = new MonthFragment();

        //match the component in layout files
        mainNav =(BottomNavigationView) findViewById(R.id.main_nav);
        mainFrame = (FrameLayout) findViewById(R.id.main_frame);

        myDB = new DBManager(this);    //check DatabaseHelper.java

        setFrag(taskFrag);

        //Buttom navigation Selected Listerner
        //set the tabs corresponding to the fragment
        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch(menuItem.getItemId()){

                    case R.id.nav_list:
                        setFrag(listFrag);
                        return true;

                    case R.id.nav_calender:
                        setFrag(calenderFrag);
                        return true;

                    case R.id.nav_task:
                        setFrag(taskFrag);
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    ////set current fragment
    private void setFrag(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();

    }


}
