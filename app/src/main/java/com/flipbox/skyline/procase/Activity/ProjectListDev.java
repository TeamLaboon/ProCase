package com.flipbox.skyline.procase.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.flipbox.skyline.procase.Database.Client;
import com.flipbox.skyline.procase.Database.DataBaseHandler;
import com.flipbox.skyline.procase.Database.Project;
import com.flipbox.skyline.procase.R;
import com.flipbox.skyline.procase.app.AppController;
import com.flipbox.skyline.procase.app.JSONParser;

import java.util.ArrayList;
import java.util.List;


public class ProjectListDev extends ListActivity {
    private DataBaseHandler database;
    JSONParser jsonParser;
    private int company_id;
    private String token;
    private String clientID;
    // LinearLayout layout;
    // ListView listView;
    // List<String> stringValues;
    public final static String EXTRA_MESSAGE_NAME = "com.flipbox.skyline.procase.MESSAGENAME";
    public final static String EXTRA_MESSAGE_DESC = "com.flipbox.skyline.procase.MESSAGEDESC";
    public final static String EXTRA_MESSAGE_PROTOTYPE = "com.flipbox.skyline.procase.MESSAGEPROTOTYPE";
    customAdapter myAdapter;
    public class dataProject{
        String nama;
        String type;
        String description;
        String prototype;
        String urlLogo;
        String projectID;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list_dev);

        Intent intent = getIntent();
        token = intent.getStringExtra(SignInActivity.EXTRA_MESSAGE_CID);
        database = new DataBaseHandler(this);


        //  layout = (LinearLayout) findViewById(R.id.progressbar_view);
        myAdapter = new customAdapter();
        setListAdapter(myAdapter);


    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        dataProject project = myAdapter.getDataValue(position);
        Intent projectDesc = new Intent(ProjectListDev.this, ProjectDescription.class);
        String messageName = project.nama;
        String messageDesc = project.description;
        String messagePrototype = project.prototype;
        projectDesc.putExtra(EXTRA_MESSAGE_NAME,messageName);
        projectDesc.putExtra(EXTRA_MESSAGE_DESC,messageDesc);
        projectDesc.putExtra(EXTRA_MESSAGE_PROTOTYPE,messagePrototype);
        startActivity(projectDesc);
    }
    public class customAdapter extends BaseAdapter{
        List<dataProject> dataProjectList = dataProjectListView();
        ImageLoader imageLoader = AppController.getInstance().getmImageLoader();
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return dataProjectList.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return dataProjectList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            // TODO Auto-generated method stub
            if(arg1 == null){
                LayoutInflater inflater = (LayoutInflater)ProjectListDev.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                arg1 = inflater.inflate(R.layout.listitem,arg2,false);
            }

            //hasby nambah
            if(imageLoader == null){
                imageLoader = AppController.getInstance().getmImageLoader();
            }

            NetworkImageView projectLogo = (NetworkImageView)arg1.findViewById(R.id.project_logo);
            TextView projectName = (TextView)arg1.findViewById(R.id.textView4);
            TextView projectDesc = (TextView)arg1.findViewById(R.id.textView5);
            TextView projectType = (TextView)arg1.findViewById(R.id.textView8);

            dataProject project= dataProjectList.get(arg0);

            projectLogo.setImageUrl(project.urlLogo,imageLoader);
            projectName.setText(project.nama);
            projectDesc.setText(project.description);
            projectType.setText(project.type);
            return arg1;
        }

        public dataProject getDataValue(int position){

            return dataProjectList.get(position);
        }


    }

    public List<dataProject> dataProjectListView(){

        List<dataProject> dataProjectList = new ArrayList<dataProject>();
        company_id = database.getCompanyIdByToken(token);
        ArrayList<Project> ProjectList = database.getAllProjectsByCompanyId(company_id);

        for(Project value : ProjectList){
            //value = new Project();
            dataProject project = new dataProject();
            project.nama =value.getName();
            project.description = value.getDescription();
            project.type =value.getType();
            project.prototype=value.getPrototype();
            project.urlLogo=value.getLogo();
            dataProjectList.add(project);
        }

        return dataProjectList;
    }

}
