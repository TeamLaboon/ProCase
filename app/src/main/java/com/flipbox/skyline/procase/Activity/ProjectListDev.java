package com.flipbox.skyline.procase.Activity;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.flipbox.skyline.procase.Database.Company;
import com.flipbox.skyline.procase.Database.DataBaseHandler;
import com.flipbox.skyline.procase.Database.Project;
import com.flipbox.skyline.procase.R;
import com.flipbox.skyline.procase.app.AppController;

import java.util.ArrayList;
import java.util.List;
import android.support.v7.widget.SearchView;

    public class ProjectListDev extends ActionBarActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {
        public static final String DEFAULT = "N/A";
        private SharedPreferences sharedPreferences;
        private SharedPreferences.Editor editor;
        private String query="";
        private ListView myListView;
        private DataBaseHandler database;
        ImageLoader imageLoader = AppController.getInstance().getmImageLoader();
        private Toolbar mToolbar;
        TextView textView;
        private int company_id;
        private String token;
        public final static String EXTRA_MESSAGE_NAME = "com.flipbox.skyline.procase.MESSAGENAME";
        public final static String EXTRA_MESSAGE_DESC = "com.flipbox.skyline.procase.MESSAGEDESC";
        public final static String EXTRA_MESSAGE_PROTOTYPE = "com.flipbox.skyline.procase.MESSAGEPROTOTYPE";

        customAdapter myAdapter;

       public class dataProject {
            String nama;
            String type;
            String description;
            String prototype;
            String urlLogo;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_project_list_dev);
            myListView = (ListView) findViewById(R.id.list);
            textView = (TextView) findViewById(R.id.notification);

            sharedPreferences = getApplicationContext().getSharedPreferences("DataClient", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            token = sharedPreferences.getString("token", DEFAULT);

            database = new DataBaseHandler(this);
            company_id = database.getCompanyIdByToken(token);

            TextView companyName = (TextView)findViewById(R.id.companyName);
            NetworkImageView companyLogo = (NetworkImageView) findViewById(R.id.companyLogo);
            TextView companyAddress = (TextView)findViewById(R.id.companyAddress);
            Company company = database.getCompanyById(company_id);
            companyName.setText(company.getName());
            companyAddress.setText(company.getAddress());
            if (imageLoader == null) {
                imageLoader = AppController.getInstance().getmImageLoader();
            }
            companyLogo.setImageUrl(company.getLogo(), imageLoader);

/*
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
*/
            if (database.isProject(company_id)) {
                displayList();
            } else {
                textView.setText("There're no prototypes.");
            }
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_project_list_dev, menu);

            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            MenuItem searchViewa = menu.findItem(R.id.action_search);
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewa);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getCallingActivity()));
            searchView.setIconifiedByDefault(false);
            searchView.setOnQueryTextListener(this);
            searchView.setOnCloseListener(this);
            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_sign_out :
                    new AlertDialog.Builder(this)
                            .setTitle("Sign Out")
                            .setMessage("Are you sure you want to sign out ?")
                            .setPositiveButton(R.string.alert_yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    editor.clear();
                                    editor.commit();

                                    // After logout redirect user to Loing Activity
                                    Intent i = new Intent(ProjectListDev.this, SignInActivity.class);

                                    // Add new Flag to start new Activity
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                    // Staring Login Activity
                                    getApplicationContext().startActivity(i);
                                    finish();
                                }
                            })
                            .setNegativeButton(R.string.alert_no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(R.drawable.ic_dialog_alert)
                            .show();
                    return true;

            }
            return super.onOptionsItemSelected(item);
        }

        public  void displayList(){
                myAdapter = new customAdapter();
                myListView.setAdapter(myAdapter);
                myListView.setTextFilterEnabled(true);

            if(myListView.getCount()<=0)
                textView.setText("There're no result for '"+query+"'.");
            else
                textView.setText("");

                myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dataProject project = myAdapter.getDataValue(position);
                        Intent projectDesc = new Intent(ProjectListDev.this, ProjectDescription.class);
                        String messageName = project.nama;
                        String messageDesc = project.description;
                        String messagePrototype = project.prototype;
                        projectDesc.putExtra(EXTRA_MESSAGE_NAME, messageName);
                        projectDesc.putExtra(EXTRA_MESSAGE_DESC, messageDesc);
                        projectDesc.putExtra(EXTRA_MESSAGE_PROTOTYPE, messagePrototype);
                        startActivity(projectDesc);
                    }
                });

        }

        @Override
        public boolean onClose() {
            filterData("");
            return false;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            filterData(query);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText){
            filterData(newText);
            return  false;
        }

        public class customAdapter extends BaseAdapter {
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
                if (arg1 == null) {
                    LayoutInflater inflater = (LayoutInflater) ProjectListDev.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    arg1 = inflater.inflate(R.layout.listitem, arg2, false);
                }

                //hasby nambah
                if (imageLoader == null) {
                    imageLoader = AppController.getInstance().getmImageLoader();
                }

                NetworkImageView projectLogo = (NetworkImageView) arg1.findViewById(R.id.project_logo);
                TextView projectName = (TextView) arg1.findViewById(R.id.textView4);
                TextView projectDesc = (TextView) arg1.findViewById(R.id.textView5);
                TextView projectType = (TextView) arg1.findViewById(R.id.textView8);

                final dataProject project = dataProjectList.get(arg0);

                projectLogo.setImageUrl(project.urlLogo, imageLoader);
                projectName.setText(project.nama);
                projectDesc.setText(project.description);
                projectType.setText(project.type);

                ImageView shareImage = (ImageView)arg1.findViewById(R.id.shareImage);
                shareImage.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String text = project.nama
                                + "\n\n" + project.description
                                + "\n" + project.prototype;

                        Intent textShareIntent = new Intent(Intent.ACTION_SEND);
                        textShareIntent.putExtra(Intent.EXTRA_TEXT, text);
                        textShareIntent.setType("text/plain");

                        startActivity(Intent.createChooser(textShareIntent, "Share to..."));
                    }
                });

                return arg1;
            }

            public dataProject getDataValue(int position) {

                return dataProjectList.get(position);
            }
        }

        public List<dataProject> dataProjectListView() {

            List<dataProject> dataProjectList = new ArrayList<dataProject>();
            ArrayList<Project> ProjectList = database.getAllProjectsByCompanyId(company_id);

            for (Project value : ProjectList) {
                if (query.equals("")) {
                    dataProject project = new dataProject();
                    project.nama = value.getName();
                    project.description = value.getDescription();
                    project.type = value.getType();
                    project.prototype = value.getPrototype();
                    project.urlLogo = value.getLogo();
                    dataProjectList.add(project);
                }
                else {
                    if(value.getName().toLowerCase().contains(query) || value.getDescription().toLowerCase().contains(query)) {
                        dataProject project = new dataProject();
                        project.nama = value.getName();
                        project.description = value.getDescription();
                        project.type = value.getType();
                        project.prototype = value.getPrototype();
                        project.urlLogo = value.getLogo();
                        dataProjectList.add(project);
                    }
                }

            }
            return dataProjectList;
        }

        public void filterData(String query){

            query = query.toLowerCase();
            this.query = query;

            if(query.isEmpty()) {
                displayList();
            }
            else{
                displayList();
            }

        }
    }
