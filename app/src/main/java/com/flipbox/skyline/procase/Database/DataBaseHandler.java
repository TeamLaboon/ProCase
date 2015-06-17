package com.flipbox.skyline.procase.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Hasby on 10/06/2015.
 */
public class DataBaseHandler extends SQLiteOpenHelper {
    // database version, name, table, colums
    private static final int DATABASE_VERSION = 6;
    private static final String DATABASE_NAME = "protocase";
    private static final String TABLE_USER = "user";
    private static final String TABLE_PROJECT = "project";
    private static final String USER_ID = "id";
    private static final String USER_NAME = "name";
    private static final String USER_LOGO = "logo";
    private static final String USER_TYPE = "type";
    private static final String PROJECT_ID = "id";
    private static final String PROJECT_USER_ID = "user_id";
    private static final String PROJECT_NAME = "name";
    private static final String PROJECT_TYPE = "type";
    private static final String PROJECT_LOGO = "logo";
    private static final String PROJECT_DESCRIPTION = "description";
    private static final String PROJECT_PROTOTYPE = "prototype";

    public DataBaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // buat tabel
    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_TABLE_USER = "CREATE TABLE "+ TABLE_USER +" ("
                + USER_ID +" VARCHAR(50), "
                + USER_NAME +" VARCHAR(100), "
                + USER_TYPE +" VARCHAR(10), "
                + USER_LOGO +" VARCHAR(50) );";
        db.execSQL(CREATE_TABLE_USER);

        String CREATE_TABLE_PROJECT = "CREATE TABLE "+ TABLE_PROJECT +" ("
                + PROJECT_ID +" VARCHAR(50), "
                + PROJECT_USER_ID +" VARCHAR(50),"
                + PROJECT_NAME +" VARCHAR(100), "
                + PROJECT_TYPE +" VARCHAR(50),"
                + PROJECT_LOGO +" VARCHAR(50),"
                + PROJECT_DESCRIPTION +" VARCHAR(50),"
                + PROJECT_PROTOTYPE + " VARCHAR(50) );";
        db.execSQL(CREATE_TABLE_PROJECT);
    }

    // perbarui databse
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop tabel lama jika sudah ada
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECT);
        // Create tables again
        onCreate(db);
    }

    // Menambahkan data USER
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_ID, user.getID()); // CLient ID
        values.put(USER_NAME, user.getName()); // CLient Name
        values.put(USER_TYPE, user.getType()); // CLient Name
        values.put(USER_LOGO, user.getLogo()); // CLient Logo

        // memasukkan data
        db.insert(TABLE_USER, null, values);
        db.close(); // Menutup koneksi database
    }
    // Menambahkan data PROJECT
    public void addProject(Project project) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PROJECT_ID, project.getID()); // Project ID
        values.put(PROJECT_USER_ID, project.getUserID()); // Project user id
        values.put(PROJECT_NAME, project.getName()); // Project name
        values.put(PROJECT_TYPE, project.getType()); // Project type
        values.put(PROJECT_LOGO, project.getLogo()); // Project logo
        values.put(PROJECT_DESCRIPTION, project.getDescription()); // Project description
        values.put(PROJECT_PROTOTYPE, project.getPrototype()); // Project prototype

        // memasukkan data
        db.insert(TABLE_PROJECT, null, values);
        db.close(); // Menutup koneksi database
    }

    // Mengambil satu data user
    public User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select * from user where id = '"+ id +"';", null);//db.query(TABLE_USER, new String[] { USER_ID,USER_NAME, USER_LOGO }, USER_ID + " = " +id,
            //new String[] { String.valueOf(id) }, null, null, null, null);
            if (cursor != null)
            cursor.moveToFirst();

            User user = new User(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                // return user
            return user;
        }
        catch (Exception e) {
            return new User(Integer.parseInt("0"), "", "", "");
        }
    }

    // Mengambil satu Project
    public Project getProject(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from project where id = '" + id +"';", null);
        cursor.moveToFirst();
        if (cursor != null)
            cursor.moveToFirst();

        Project project = new Project(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
        // return project
        return project;
    }

    // cek ketersediaan user
    public boolean isUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select * from user where id = '" + id +"';", null);
            cursor.moveToFirst();
            if(id == Integer.valueOf(cursor.getString(0)))return true;
            else return false;
        }
        catch (Exception e) {
            return false;
        }
    }

    // mengambil semua project terhadap satu user
    public ArrayList<Project> getAllProjectsByUserID(int id) {
        ArrayList<Project> listProject = new ArrayList<Project>();
        // Select semua data
        String selectQuery = "SELECT  * FROM "+ TABLE_PROJECT + " where user_id = '"+ id +"';";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Perulangan semua data untuk dimasukkan kedalam list
        if (cursor.moveToFirst()) {
            do {
                Project project = new Project();
                project.setID(Integer.parseInt(cursor.getString(0)));
                project.setUserID(Integer.parseInt(cursor.getString(1)));
                project.setName(cursor.getString(2));
                project.setType(cursor.getString(3));
                project.setLogo(cursor.getString(4));
                project.setDescription(cursor.getString(5));
                project.setPrototype(cursor.getString(6));
                // Menambahkan data Project ke dalam list
                listProject.add(project);
            } while (cursor.moveToNext());
        }

        // return listProject
        return listProject;
    }

    public ArrayList<Project> getAllProjects() {
        ArrayList<Project> listProject = new ArrayList<Project>();
        // Select semua data
        String selectQuery = "SELECT  * FROM "+ TABLE_PROJECT + ";";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Perulangan semua data untuk dimasukkan kedalam list
        if (cursor.moveToFirst()) {
            do {
                Project project = new Project();
                project.setID(Integer.parseInt(cursor.getString(0)));
                project.setUserID(Integer.parseInt(cursor.getString(1)));
                project.setName(cursor.getString(2));
                project.setType(cursor.getString(3));
                project.setLogo(cursor.getString(4));
                project.setDescription(cursor.getString(5));
                project.setPrototype(cursor.getString(6));
                // Menambahkan data Project ke dalam list
                listProject.add(project);
            } while (cursor.moveToNext());
        }

        // return listProject
        return listProject;
    }

    // mengambil jumlah data buku
    public int getUserCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }


    // Mengubah data buku
    public int updateUser(User buku) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_NAME, buku.getName());
        values.put(USER_LOGO, buku.getLogo());

        // mengubah data
        return db.update(TABLE_USER, values, USER_ID + " = ?",
                new String[]{String.valueOf(buku.getID())});
    }

    // Menghapus data buku
    public void deleteContact(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, USER_ID + " = ?",
                new String[]{String.valueOf(user.getID())});
        db.close();
    }
}