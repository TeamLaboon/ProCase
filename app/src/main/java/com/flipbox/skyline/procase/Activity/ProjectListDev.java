package com.flipbox.skyline.procase.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.SearchView.OnQueryTextListener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.flipbox.skyline.procase.Database.DataBaseHandler;
import com.flipbox.skyline.procase.Database.Project;
import com.flipbox.skyline.procase.R;
import com.flipbox.skyline.procase.app.AppController;
import com.flipbox.skyline.procase.app.JSONParser;

import java.util.ArrayList;
import java.util.List;


    //public class ProjectListDev extends ActionBarActivity implements OnQueryTextListener {
    public class ProjectListDev extends ActionBarActivity {
        private ListView myListView;
        private DataBaseHandler database;
        JSONParser jsonParser;
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
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            Intent intent = getIntent();
            token = intent.getStringExtra(SignInActivity.EXTRA_MESSAGE_CID);
            myListView = (ListView) findViewById(R.id.list);

          //  handleIntent(getIntent());
            if (token.equals("hasbyGanteng")) {
                TextView textView = (TextView) findViewById(R.id.notification);
                textView.setText("There's no prototypes.");
            } else {
                database = new DataBaseHandler(this);
                myAdapter = new customAdapter();
                myListView.setAdapter(myAdapter);
                myListView.setTextFilterEnabled(true);
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
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_project_list_dev, menu);
          /*  SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
            searchView.setSubmitButtonEnabled(true);
            searchView.setOnQueryTextListener(this);
        /*

        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    myListView.clearTextFilter();
                } else {
                    myListView.setFilterText(newText.toString());
                }

                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        };
        searchView.setOnQueryTextListener(textChangeListener);*/
            return true;

        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_search) {
                Intent search = new Intent(this,SearchResultActivty.class);
                startActivity(search);
                return true;
            }
            if (id == R.id.action_settings) {
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        // public class customAdapter extends BaseAdapter implements Filterable {
        public class customAdapter extends BaseAdapter {
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

                dataProject project = dataProjectList.get(arg0);

                projectLogo.setImageUrl(project.urlLogo, imageLoader);
                projectName.setText(project.nama);
                projectDesc.setText(project.description);
                projectType.setText(project.type);
                return arg1;
            }

            public dataProject getDataValue(int position) {

                return dataProjectList.get(position);
            }
        }

        public List<dataProject> dataProjectListView() {

            List<dataProject> dataProjectList = new ArrayList<dataProject>();
            company_id = database.getCompanyIdByToken(token);
            ArrayList<Project> ProjectList = database.getAllProjectsByCompanyId(company_id);

            for (Project value : ProjectList) {
                //value = new Project();
                dataProject project = new dataProject();
                project.nama = value.getName();
                project.description = value.getDescription();
                project.type = value.getType();
                project.prototype = value.getPrototype();
                project.urlLogo = value.getLogo();
                dataProjectList.add(project);
            }
            return dataProjectList;
        }

     /*   @Override
        protected void onNewIntent(Intent intent) {
            handleIntent(intent);
        }

        private void handleIntent(Intent intent) {

            if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
                String query = intent.getStringExtra(SearchManager.QUERY);
                //use the query to search your data somehow
            }
        }*/
    }
