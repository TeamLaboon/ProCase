package com.flipbox.skyline.procase.Activity;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.flipbox.skyline.procase.Database.DataBaseHandler;
import com.flipbox.skyline.procase.Database.Project;
import com.flipbox.skyline.procase.Database.User;
import com.flipbox.skyline.procase.R;

import java.util.ArrayList;
import java.util.List;


public class ProjectList extends ListActivity {
    private DataBaseHandler database;
    private String clientID;
   // LinearLayout layout;
   // ListView listView;
   // List<String> stringValues;
    public final static String EXTRA_MESSAGE_NAME = "com.flipbox.skyline.procase.MESSAGENAME";
    public final static String EXTRA_MESSAGE_DESC = "com.flipbox.skyline.procase.MESSAGEDESC";
    customAdapter myAdapter;
    public class dataProject{
        String nama;
        String type;
        String description;
        String urlLogo;
        String projectID;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poject_list);
/*        Intent intent = getIntent();
        clientID = intent.getStringExtra(SignInActivity.EXTRA_MESSAGE_CID);
        database = new DataBaseHandler(this);
        User clientData = database.getUser(Integer.valueOf(clientID));
        String clientNameText = clientData.getName();
        TextView clientName = (TextView)findViewById(R.id.textView3);
        clientName.setText(clientNameText);
      //  layout = (LinearLayout) findViewById(R.id.progressbar_view);
        myAdapter = new customAdapter();
        setListAdapter(myAdapter);
*/    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        dataProject project = myAdapter.getDataValue(position);
        Intent projectDesc = new Intent(ProjectList.this, ProjectDescription.class);
        String messageName = project.nama;
        String messageDesc = project.description;
        projectDesc.putExtra(EXTRA_MESSAGE_NAME,messageName);
        projectDesc.putExtra(EXTRA_MESSAGE_DESC,messageDesc);
        startActivity(projectDesc);
    }
    public class customAdapter extends BaseAdapter{
       List<dataProject> dataProjectList = dataProjectListView();
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
                LayoutInflater inflater = (LayoutInflater)ProjectList.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                arg1 = inflater.inflate(R.layout.listitem,arg2,false);
            }

            TextView projectName = (TextView)arg1.findViewById(R.id.textView4);
            TextView projectDesc = (TextView)arg1.findViewById(R.id.textView5);
            TextView projectType = (TextView)arg1.findViewById(R.id.textView8);

            dataProject project= dataProjectList.get(arg0);

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
   /*     ArrayList<Project> ProjectList = database.getAllProjectsByUserID(Integer.parseInt(clientID));

       //for(int i = 1;i<=ProjectList.size();i++){
          //for(int i=1;i<10;i++){
        for(Project value : ProjectList){
            //value = new Project();
            dataProject project = new dataProject();
            project.nama =value.getName();
            project.description = value.getDescription();
            project.type =value.getType();
            dataProjectList.add(project);
        }

     */   return dataProjectList;
    }

/*
    class Task extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            layout.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            layout.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
            super.onPostExecute(result);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            stringValues.add("String 1");
            stringValues.add("String 2");
            stringValues.add("String 3");
            stringValues.add("String 4");
            stringValues.add("String 5");

            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    } progress bar*/

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_poject_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
