package com.flipbox.skyline.procase.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.flipbox.skyline.procase.Database.DataBaseHandler;
import com.flipbox.skyline.procase.Database.Project;
import com.flipbox.skyline.procase.R;
import com.flipbox.skyline.procase.app.AppController;
import com.flipbox.skyline.procase.app.JSONParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchResultActivty extends ActionBarActivity {
    private ListView myListView;
    private DataBaseHandler database;
    JSONParser jsonParser;
    private int company_id;
    private String token;
    private ArrayList<Project> arrayList = new ArrayList<Project>();
    public final static String EXTRA_MESSAGE_NAME = "com.flipbox.skyline.procase.MESSAGENAME";
    public final static String EXTRA_MESSAGE_DESC = "com.flipbox.skyline.procase.MESSAGEDESC";
    public final static String EXTRA_MESSAGE_PROTOTYPE = "com.flipbox.skyline.procase.MESSAGEPROTOTYPE";

    customAdapter myAdapter;
    searchAdapter sAdapter;

    public class dataProject {
        String nama;
        String type;
        String description;
        String prototype;
        String urlLogo;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_activty);
        handleIntent(getIntent());
        myListView = (ListView)findViewById(R.id.listView1);
        database = new DataBaseHandler(this);
        myAdapter = new customAdapter();
        sAdapter = new searchAdapter(this,arrayList);
        myListView.setAdapter(sAdapter);
     //   myListView.setAdapter(myAdapter);
        myListView.setTextFilterEnabled(true);
        /* myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dataProject project = sAdapter.getDataValue(position);
                Intent projectDesc = new Intent(SearchResultActivty.this, ProjectDescription.class);
                String messageName = project.nama;
                String messageDesc = project.description;
                String messagePrototype = project.prototype;
                projectDesc.putExtra(EXTRA_MESSAGE_NAME, messageName);
                projectDesc.putExtra(EXTRA_MESSAGE_DESC, messageDesc);
                projectDesc.putExtra(EXTRA_MESSAGE_PROTOTYPE, messagePrototype);
                startActivity(projectDesc);
            }
        });*/
    }

    public void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_project_list_dev, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);

        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    myListView.clearTextFilter();
                } else {
                   // myAdapter.getFilter().filter(newText.toString());
                    myListView.setFilterText(newText.toString());
                }

                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            /*
           @Override
           public boolean onQueryTextChange(String newText)
           {
               // this is your adapter that will be filtered
               myAdapter.getFilter().filter(newText);
               System.out.println("on text chnge text: "+newText);
               return true;
           }
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                // this is your adapter that will be filtered
                myAdapter.getFilter().filter(query);
                System.out.println("on query submit: "+query);
                return true;
            }*/
        };
        searchView.setOnQueryTextListener(textChangeListener);

        final EditText editText = (EditText) menu.findItem(R.id.action_search).getActionView();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
              /*  if (myAdapter != null) {
                    myAdapter.getFilter().filter(charSequence);
                }*/
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editText.getText().toString().toLowerCase(Locale.getDefault());
                sAdapter.filter(text);

            }
        });

        return true;
    }
   /* public void onListItemClick(ListView l,
                                View v, int position, long id) {
        // call detail activity for clicked entry
    }*/

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query =
                    intent.getStringExtra(SearchManager.QUERY);
          //  doSearch(query);
        }
    }

   /* private void doSearch(String queryStr) {
        // get a Cursor, prepare the ListAdapter
        // and set it
    }*/
    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_activty);
        handleIntent(getIntent());
    }

    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_result_activty, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
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
    }



    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
        }
    }
    */

    public class customAdapter extends BaseAdapter implements Filterable {
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
                LayoutInflater inflater = (LayoutInflater) SearchResultActivty.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

      /*  public void filter(String charText) {
            List<dataProject> dataProjectList = new ArrayList<dataProject>();
           // company_id = database.getCompanyIdByToken(token);
           // ArrayList<Project> ProjectList = database.getAllProjectsByCompanyId(company_id);
            charText = charText.toLowerCase();
             dataProjectList.clear();
             if (charText.length() == 0) {
                 dataProjectList.addAll(ProjectList);
                 } else {
                 for (Project data : ProjectList) {
                    //dataProject project = new dataProject();
                     if (data.getName().contains(charText)) {
                         dataProjectList.add(data);
                         }
                     }
                 }
             notifyDataSetChanged();
            }*/
      @Override
      public Filter getFilter() {
          // TODO Auto-generated method stub
          return new Filter(){
              // ContactSetGet is your gettersetter class
              List<Project> i = new ArrayList<Project>();
              ArrayList<Project> ProjectList = database.getAllProjects();
              @Override
              protected FilterResults performFiltering(CharSequence prefix) {
                  // TODO Auto-generated method stub
                  FilterResults results = new FilterResults();


                  if (prefix!= null && prefix.toString().length() > 0) {

                      for (int index = 0; index < dataProjectList.size(); index++) {
                          Project si = ProjectList.get(index);
                          Log.i("----------------si.getFirstName()---------","."+si.getName());
                          Log.i("----------------prefix---------","."+prefix.toString());
                          //String number
                          if(si.getName().startsWith(prefix.toString())){
                              i.add(si);
                          }
                      }
                      results.values = i;
                      results.count = i.size();
                  }
                  else{
                      synchronized (dataProjectList){
                          results.values = i;
                          results.count = i.size();
                      }
                  }
                  return results;
              }

              @SuppressWarnings("unchecked")
              @Override
              protected void publishResults(CharSequence constraint,
                                            FilterResults results) {
                  // TODO Auto-generated method stub
                  ProjectList = (ArrayList<Project>) results.values;
                  customAdapter.this.notifyDataSetChanged();
              }
          };
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


    public class searchAdapter extends BaseAdapter{

        Context mContext;
        LayoutInflater inflater;
        private List<Project> Worldpopulationlist=null;
        private ArrayList<Project> arraylist;
        ImageLoader imageLoader = AppController.getInstance().getmImageLoader();

        public searchAdapter(Context context, List<Project>Worldpopulationlist){
            mContext = context;
            this.Worldpopulationlist = Worldpopulationlist;
            inflater = LayoutInflater.from(mContext);
            this.arraylist = new ArrayList<Project>();
            this.arraylist.addAll(Worldpopulationlist);
        }

      /*  public class ViewHolder{
            TextView rank;
            TextView country;
            TextView population;
        } */

        @Override
        public int getCount() {
            return Worldpopulationlist.size();
        }

        @Override
        public Project getItem(int position) {
            return Worldpopulationlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class ViewHolder {
            TextView namavh;
            TextView descvh;
            TextView tipevh;
        }


        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            if(view == null){
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.listitem, null);
                holder.namavh = (TextView) view.findViewById(R.id.textView4);
                holder.descvh = (TextView) view.findViewById(R.id.textView5);
                holder.tipevh = (TextView) view.findViewById(R.id.textView8);
                view.setTag(holder);
            }else {
                holder = (ViewHolder) view.getTag();
            }

            holder.namavh.setText(Worldpopulationlist.get(position).getName());
            holder.descvh.setText(Worldpopulationlist.get(position).getDescription());
            holder.tipevh.setText(Worldpopulationlist.get(position).getPrototype());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(mContext, ProjectDescription.class);
                    intent.putExtra(EXTRA_MESSAGE_NAME, (Worldpopulationlist.get(position).getName()));
                    intent.putExtra(EXTRA_MESSAGE_DESC, (Worldpopulationlist.get(position).getDescription()));
                    intent.putExtra(EXTRA_MESSAGE_PROTOTYPE, (Worldpopulationlist.get(position).getPrototype()));
                    mContext.startActivity(intent);
                }
            });

            return view;
            /*
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) SearchResultActivty.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.listitem, parent, false);
            }

            //hasby nambah
            if (imageLoader == null) {
                imageLoader = AppController.getInstance().getmImageLoader();
            }

            NetworkImageView projectLogo = (NetworkImageView) view.findViewById(R.id.project_logo);
            TextView projectName = (TextView) view.findViewById(R.id.textView4);
            TextView projectDesc = (TextView) view.findViewById(R.id.textView5);
            TextView projectType = (TextView) view.findViewById(R.id.textView8);

            Project project = Worldpopulationlist.get(position);

            projectLogo.setImageUrl(project.urlLogo, imageLoader);
            projectName.setText(project.nama);
            projectDesc.setText(project.description);
            projectType.setText(project.type);
            return view;*/
        }

        public void filter(String charText){
            charText = charText.toLowerCase(Locale.getDefault());
            Worldpopulationlist.clear();
            if(charText.length()== 0){
                Worldpopulationlist.addAll(arraylist);
            }else{
                for(Project wp :arraylist){
                    if(wp.getName().toLowerCase(Locale.getDefault()).contains(charText)){
                        Worldpopulationlist.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }

        public Project getDataValue(int position) {

            return Worldpopulationlist.get(position);
        }
    }
}
