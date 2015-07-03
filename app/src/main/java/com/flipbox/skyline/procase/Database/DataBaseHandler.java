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
    private static final int DATABASE_VERSION = 8;
    private static final String DATABASE_NAME = "protocase";
    private static final String TABLE_COMPANY = "company";
    private static final String TABLE_PROJECT = "project";
    private static final String COMPANY_ID = "id";
    private static final String COMPANY_NAME = "name";
    private static final String COMPANY_TOKEN = "token";
    private static final String COMPANY_ADDRESS = "address";
    private static final String COMPANY_LOGO = "logo";
    private static final String PROJECT_ID = "id";
    private static final String PROJECT_COMPANY_ID = "company_id";
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
        String CREATE_TABLE_COMPANY = "CREATE TABLE "+ TABLE_COMPANY +" ("
                + COMPANY_ID +" VARCHAR(50), "
                + COMPANY_NAME +" VARCHAR(100), "
                + COMPANY_ADDRESS +" VARCHAR(10), "
                + COMPANY_TOKEN +" VARCHAR(10), "
                + COMPANY_LOGO +" VARCHAR(50) );";
        db.execSQL(CREATE_TABLE_COMPANY);

        String CREATE_TABLE_PROJECT = "CREATE TABLE "+ TABLE_PROJECT +" ("
                + PROJECT_ID +" VARCHAR(50), "
                + PROJECT_COMPANY_ID +" VARCHAR(50),"
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPANY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECT);
        // Create tables again
        onCreate(db);
    }

    // Menambahkan data USER
    public void addCompany(Company company) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COMPANY_ID, company.getID()); // CLient ID
        values.put(COMPANY_NAME, company.getName()); // CLient Name
        values.put(COMPANY_ADDRESS, company.getAddress()); // CLient Name
        values.put(COMPANY_TOKEN, company.getToken()); // CLient Logo
        values.put(COMPANY_LOGO, company.getLogo()); // CLient Logo

        // memasukkan data
        db.insert(TABLE_COMPANY, null, values);
        db.close(); // Menutup koneksi database
    }
    // Menambahkan data PROJECT
    public void addProject(Project project) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PROJECT_ID, project.getID()); // Project ID
        values.put(PROJECT_COMPANY_ID, project.getCompanyID()); // Project company id
        values.put(PROJECT_NAME, project.getName()); // Project name
        values.put(PROJECT_TYPE, project.getType()); // Project type
        values.put(PROJECT_LOGO, project.getLogo()); // Project logo
        values.put(PROJECT_DESCRIPTION, project.getDescription()); // Project description
        values.put(PROJECT_PROTOTYPE, project.getPrototype()); // Project prototype

        // memasukkan data
        db.insert(TABLE_PROJECT, null, values);
        db.close(); // Menutup koneksi database
    }

    // Mengambil satu data company
    public Company getCompanyById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select * from "+ TABLE_COMPANY +" where "+ COMPANY_ID +" = '"+ id +"';", null);//db.query(TABLE_COMPANY, new String[] { COMPANY_ID,COMPANY_NAME, COMPANY_TOKEN }, COMPANY_ID + " = " +id,
            //new String[] { String.valueOf(id) }, null, null, null, null);
            if (cursor != null)
            cursor.moveToFirst();

            Company company = new Company(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                // return company
            return company;
        }
        catch (Exception e) {
            return new Company(Integer.parseInt("0"), "", "", "", "");
        }
    }

    // Mengambil satu Project
    public Project getProjectById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("select * from " + TABLE_PROJECT + " where " + PROJECT_ID + " = '" + id + "';", null);
            cursor.moveToFirst();
            if (cursor != null)
                cursor.moveToFirst();

            Project project = new Project(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
            // return project
            return project;
        }
        catch (Exception e) {
            return new Project(0, 0, "", "", "", "", "");
        }
    }

    // cek ketersediaan company
    public boolean isToken(String token) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select * from "+ TABLE_COMPANY +" where "+ COMPANY_TOKEN +" = '" + token +"';", null);
            cursor.moveToFirst();
            if (token.equals(cursor.getString(cursor.getColumnIndex(COMPANY_TOKEN))))return true;
            else return false;
        }
        catch (Exception e) {
            return false;
        }
    }

    // cek ketersediaan company
    public boolean isProject(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select * from "+ TABLE_PROJECT +" where "+ PROJECT_COMPANY_ID +" = '" + id +"';", null);
            cursor.moveToFirst();
            if (id == cursor.getInt(cursor.getColumnIndex(PROJECT_COMPANY_ID)))return true;
            else return false;
        }
        catch (Exception e) {
            return false;
        }
    }

    public int getCompanyIdByToken(String token) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from "+ TABLE_COMPANY +" where "+ COMPANY_TOKEN +" = '" + token +"';", null);
        cursor.moveToFirst();
        if (cursor != null)
            cursor.moveToFirst();

        // return id
        return Integer.parseInt(cursor.getString(0));
    }

    // mengambil semua project terhadap satu company
    public ArrayList<Project> getAllProjectsByCompanyId(int id) {
        ArrayList<Project> listProject = new ArrayList<Project>();
        // Select semua data
        String selectQuery = "SELECT  * FROM "+ TABLE_PROJECT + " where company_id = '"+ id +"';";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Perulangan semua data untuk dimasukkan kedalam list
        if (cursor.moveToFirst()) {
            do {
                Project project = new Project();
                project.setID(Integer.parseInt(cursor.getString(0)));
                project.setCompanyID(Integer.parseInt(cursor.getString(1)));
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
        String selectQuery = "SELECT  * FROM "+ TABLE_PROJECT + " where ;";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Perulangan semua data untuk dimasukkan kedalam list
        if (cursor.moveToFirst()) {
            do {
                Project project = new Project();
                project.setID(Integer.parseInt(cursor.getString(0)));
                project.setCompanyID(Integer.parseInt(cursor.getString(1)));
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

    // Mengubah data Company
    public int updateCompany(Company company) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COMPANY_ID, company.getID()); // CLient ID
        values.put(COMPANY_NAME, company.getName()); // CLient Name
        values.put(COMPANY_ADDRESS, company.getAddress()); // CLient Name
        values.put(COMPANY_TOKEN, company.getToken()); // CLient Logo
        values.put(COMPANY_LOGO, company.getLogo()); // CLient Logo

        // mengubah data
        return db.update(TABLE_COMPANY, values, COMPANY_ID + " = ?",
                new String[] { String.valueOf(company.getID()) });
    }

    // Mengubah data Project
    public int updateProject(Project project) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PROJECT_ID, project.getID()); // Project ID
        values.put(PROJECT_COMPANY_ID, project.getCompanyID()); // Project company id
        values.put(PROJECT_NAME, project.getName()); // Project name
        values.put(PROJECT_TYPE, project.getType()); // Project type
        values.put(PROJECT_LOGO, project.getLogo()); // Project logo
        values.put(PROJECT_DESCRIPTION, project.getDescription()); // Project description
        values.put(PROJECT_PROTOTYPE, project.getPrototype()); // Project prototype

        // mengubah data
        return db.update(TABLE_PROJECT, values, PROJECT_ID + " = ?",
                new String[] { String.valueOf(project.getID()) });
    }

    // Menghapus data company
    public void deleteCompany(Company company) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COMPANY, COMPANY_ID + " = ?",
                new String[]{String.valueOf(company.getID())});
        db.close();
    }

    // Menghapus data project
    public void deleteProject(Project project) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PROJECT, PROJECT_ID + " = ?",
                new String[]{String.valueOf(project.getID())});
        db.close();
    }

    public void deleteAll(){

        String selectQuery = "DELETE FROM "+ TABLE_PROJECT + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(selectQuery);
        selectQuery = "DELETE FROM "+ TABLE_COMPANY + ";";
        db.execSQL(selectQuery);

    }
}