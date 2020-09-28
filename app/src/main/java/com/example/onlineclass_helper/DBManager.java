package com.example.onlineclass_helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


// DatabaseHelper 클래스 : sqlLite를 이용하여, db 관리
// 1.Task 2.List 3.Person 테이블에 대한 create, insert, delete, select 하는 메소드 포함

public class DBManager extends SQLiteOpenHelper{

    // DB 생성
    private static final String DATABASE_NAME = "todo.db";


    ///////////////////////////////////////////////////////////////////////////////////////////////
    // 1. Task 테이블
    // id, name, des(메모), date(날짜), status(완료여부), list(과목(수업)으로 묶이는!!)
    private static final String TASK_TABLE_NAME = "task_table";
    private static final String TASK_COL0_ID= "ID";
    private static final String TASK_COL1_NAME= "NAME";
    private static final String TASK_COL2_DES= "DES";
    private static final String TASK_COL3_DATE= "DATE";
    private static final String TASK_COL4_STATUS = "STATUS";
    private static final String TASK_COL5_LIST= "LIST";

    // Task Table 생성
    private String create_task_table_statement =
            "CREATE TABLE "+ TASK_TABLE_NAME +" ( "+
                    TASK_COL0_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+ //프라이머리키
                    TASK_COL1_NAME+ " TEXT NOT NULL, "+ // 이름 반드시 입력
                    TASK_COL2_DES+" TEXT, "+
                    TASK_COL3_DATE+" TEXT, "+
                    TASK_COL4_STATUS+" INTEGER NOT NULL, "+ // 완료여부 필수
                    TASK_COL5_LIST+" INTEGER );";


    ///////////////////////////////////////////////////////////////////////////////////////////////
    // 2. 리스트 Table ( 수강 과목!!!)
    // id, name, des
    private static final String LIST_TABLE_NAME = "list_table";
    private static final String L_ID = "L_ID";
    private static final String L_Name = "L_NAME";
    private static final String L_Desc = "L_DES";

    // 리스트 Table 생성
    private String create_list_table_statement =
            "CREATE TABLE "+ LIST_TABLE_NAME +" ( "+
                    L_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    L_Name+ " TEXT NOT NULL, "+
                    L_Desc+" TEXT );";

    ///////////////////////////////////////////////////////////////////////////////////////////////

    // 3. Person 테이블 ( 유저 정보에 관한 테이블) -> 유저정보 안 담을거면 필요 없긴 함!!

    // id, username, nickname, email, instruction
    private static final String PERSON_TABLE_NAME = "person_table";
    private static final String PERSON_INFO_ID ="ID";
    private static final String PERSON_INFO_USER = "USERNAME";
    private static final String PERSON_INFO_NAME = "NICKNAME";
    private static final String PERSON_INFO_EMAIL = "EMAIL";
    private static final String PERSON_INFO_INSTRUCTION = "INSTRUCTION";

    // Person 테이블 생성
    private String create_person_table_statement =
            "CREATE TABLE IF NOT EXISTS "+ PERSON_TABLE_NAME +" ( "+
                    PERSON_INFO_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    PERSON_INFO_USER + " TEXT NOT NULL, "+
                    PERSON_INFO_NAME+ " TEXT NOT NULL, "+
                    PERSON_INFO_EMAIL+" TEXT, "+
                    PERSON_INFO_INSTRUCTION + " TEXT );";


    ////////////////////////////////////////////////////////////////////////////////////////////////

    // DatabaseHelper 생성자 정의
    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    // task, list, person 테이블 생성 명령
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_task_table_statement);
        db.execSQL(create_list_table_statement);
        db.execSQL(create_person_table_statement);
    }

    // 업그레이드(데이터베이스 update)하는 메소드
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LIST_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PERSON_TABLE_NAME);
        onCreate(db);
    }

    // Task 테이블에 이름, 메모, 날짜, 완료상태 insert 하는 메소드
    public boolean addTaskData( String name, String des, String date){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_COL1_NAME,name);
        contentValues.put(TASK_COL2_DES,des);
        contentValues.put(TASK_COL3_DATE,date);
        contentValues.put(TASK_COL4_STATUS,0);

        long result = db.insert(TASK_TABLE_NAME,null,contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    // 할일을 과목에 넣는 메소드
    // 과목 id, name, des, status insert
    public boolean addTaskInList( String name, String des, int list_id){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_COL1_NAME,name);
        contentValues.put(TASK_COL2_DES,des);
        contentValues.put(TASK_COL5_LIST,list_id);
        contentValues.put(TASK_COL4_STATUS,0);

        long result = db.insert(TASK_TABLE_NAME,null,contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    // 날짜와 완료상태로 부터 Task를 가져오는 메소드
    public Cursor getTaskbyDate(String date, int status){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM "+ TASK_TABLE_NAME +
                        " WHERE  "+ TASK_COL3_DATE+ "= ?" +
                        " AND "+ TASK_COL4_STATUS+" = ?",
                new String[]{date, String.valueOf(status)});
    }

    // id로 부터 Task를 가져오는 메소드
    public Cursor getTaskbyId(int index){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+
                TASK_TABLE_NAME+
                " WHERE "+ TASK_COL0_ID + " = ?", new String[]{String.valueOf(index)});

        return cursor;
    }

    // id로 부터 Task 삭제하는 메소드
    public void deleteTaskbyId(int index){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "
                        + TASK_TABLE_NAME
                        + " WHERE " + TASK_COL0_ID + " = ?",
                new String[] {String.valueOf(index)});
    }

    // list 없이 Task 업데이트하는 메소드
    public void updateTaskByIdNoList(int index, String name, String des, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE "
                        + TASK_TABLE_NAME + " SET "
                        + TASK_COL1_NAME + " = ?, "
                        + TASK_COL2_DES + " = ?, "
                        + TASK_COL3_DATE + " = ? "
                        + " WHERE " + TASK_COL0_ID + " = ?",
                new String[] {name, des, date, String.valueOf(index)});
    }

    // list 포함해서 Task 업데이트하는 메소드
    public void updateTaskByIdWithList(int index, String name, String des, int list){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE "
                        + TASK_TABLE_NAME + " SET "
                        + TASK_COL1_NAME + " = ?, "
                        + TASK_COL2_DES + " = ?, "
                        + TASK_COL5_LIST + " = ? "
                        + " WHERE " + TASK_COL0_ID + " = ?",
                new String[] {name, des, String.valueOf(list), String.valueOf(index)});
    }

    // List 추가 하는 메소드
    public boolean addListData( String name, String des ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(L_Name, name);
        contentValues.put(L_Desc, des);

        long result = db.insert(LIST_TABLE_NAME,null, contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    // 리스트 가져오는 메소드
    public Cursor getLists(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+LIST_TABLE_NAME, null);
        return cursor;
    }

    // ID로 부터 리스트 가져오는 메소드
    public Cursor getListById(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+
                LIST_TABLE_NAME+
                " WHERE "+ L_ID + " = ?", new String[]{String.valueOf(id)});

        return cursor;

    }

    // ID를 통해 리스트 삭제하는 메소드
    public void deleteListbyId(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "
                        + LIST_TABLE_NAME
                        + " WHERE " + L_ID + " = ?",
                new String[] {String.valueOf(id)});
    }

    // ID로 리스트 업데이트 하는 메소드
    public void updateListById(int id, String name, String des){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE "
                        + LIST_TABLE_NAME + " SET "
                        + L_Name + " = ?, "
                        + L_Desc + " = ? "
                        + " WHERE " + L_ID + " = ?",
                new String[] {name, des, String.valueOf(id)});
    }


    // 리스트의 ID와 status로 부터 Task(할일)을 가져오는 메소드
    public  Cursor getTaskByList(int listid, int status){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM "+ TASK_TABLE_NAME +
                        " WHERE "+ TASK_COL5_LIST + " = ?" +
                        " AND "+ TASK_COL4_STATUS+" = ?",
                new String[]{String.valueOf(listid), String.valueOf(status)});
    }

    // 할일을 보여주는 메소드
    public Cursor showTaskData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor task_data = db.rawQuery("SELECT * FROM "+TASK_TABLE_NAME, null);
        return task_data;
    }


    // Task(할일)의 status(완료상태)를 1로 바꿔주는 메소드(1: 완료, 0: 미완료)
    public void setTaskComplete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TASK_TABLE_NAME
                        + " SET " + TASK_COL4_STATUS + " = 1"
                        + " WHERE " + TASK_COL0_ID + " = ?",
                new String[] {String.valueOf(id)});
    }

    //Task(할일)의 status(완료상태)를 0으로 바꿔주는 메소드(1: 완료, 0: 미완료)
    public void setTaskIncomplete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TASK_TABLE_NAME
                        + " SET " + TASK_COL4_STATUS + " = 0"
                        + " WHERE " + TASK_COL0_ID + " = ?",
                new String[] {String.valueOf(id)});
    }

    // Person(유저정보) 추가하는 메소드
    public boolean addPersonData(String userName, String name, String email, String instruction){
        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(PERSON_INFO_USER, userName);
        contentValue.put(PERSON_INFO_NAME, name);
        contentValue.put(PERSON_INFO_EMAIL,email);
        contentValue.put(PERSON_INFO_INSTRUCTION,instruction);
        long result = db1.insert(PERSON_TABLE_NAME,null,contentValue);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    // Person info(유저정보) 가져오는 메소드
    public Cursor showUserData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor user_data = db.rawQuery("SELECT * FROM "+PERSON_TABLE_NAME, null);
        return user_data;
    }

    // Person info(유저정보) 업데이트 하는 메소드
    public void updateUserInfo(int index, String userName, String nickName, String email, String instruction){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE "
                        + PERSON_TABLE_NAME + " SET " + PERSON_INFO_USER + " = ?, "
                        + PERSON_INFO_NAME + " = ?, " + PERSON_INFO_EMAIL + " = ?, "
                        + PERSON_INFO_INSTRUCTION + " = ? " + " WHERE "
                        + PERSON_INFO_ID + " = ?",
                new String[]{userName, nickName, email, instruction, String.valueOf(index)});
    }
}