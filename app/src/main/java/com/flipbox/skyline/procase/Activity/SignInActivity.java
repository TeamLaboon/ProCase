package com.flipbox.skyline.procase.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.flipbox.skyline.procase.Database.Client;
import com.flipbox.skyline.procase.Database.Company;
import com.flipbox.skyline.procase.Database.User;
import com.flipbox.skyline.procase.Database.DataBaseHandler;
import com.flipbox.skyline.procase.Database.Project;
import com.flipbox.skyline.procase.R;
import com.flipbox.skyline.procase.app.AppController;
import com.flipbox.skyline.procase.app.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SignInActivity extends Activity {
    private String urlJsonCompanies = "http://protocase.sakadigital.id/api/companies";              // json object response url
    private String urlJsonPrototypes = "http://protocase.sakadigital.id/api/company_prototypes";    // json array response url
    private static String TAG = JSONParser.class.getSimpleName();
    private ProgressDialog pDialog;             // Progress dialog
    private DataBaseHandler database;
    private Intent projectListDev;
    public final static String EXTRA_MESSAGE_CID = "com.flipbox.skyline.procase.Activity.MESSAGCID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        database = new DataBaseHandler(this);
        projectListDev = new Intent(this,ProjectListDev.class);

        Button btnMakeObjectRequest = (Button)findViewById(R.id.button);
        btnMakeObjectRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText clientid = (EditText) findViewById(R.id.editText);
                String textClientid = clientid.getText().toString();
                if (textClientid.equals("")) {
                    Toast.makeText(SignInActivity.this, "Isi User ID !  !!", Toast.LENGTH_LONG).show();
                } else {
                    checkToken(textClientid);
                }
            }
        });

    }

    private void checkToken(final String token){
        pDialog = new ProgressDialog(SignInActivity.this);
        pDialog.setMessage("Connecting to server...");
        pDialog.show();

        String url = "http://protocase.sakadigital.id/api/companies" +"?token="+ token;
        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response){
                pDialog.dismiss();
                insertCompanyIntoDatabaseByToken(token);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(SignInActivity.this, "User ID salah !!", Toast.LENGTH_LONG).show();
            }
        });
        //adding request to request queue

        AppController.getInstance().addToRequestQueue(req);
    }

    public void insertCompanyIntoDatabaseByToken(final String token){
        showpDialog("Collecting Data Company...");
        String url = urlJsonCompanies +"?token="+ token;
        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response){

                try{
                    for (int i=0; i<response.length(); i++){
                        JSONObject company = (JSONObject)response.get(i);

                        String id = company.getString("id");
                        String name = company.getString("name");
                        String address = company.getString("address");
                        String _token = company.getString("token");

                        Company cek_company = database.getCompanyById(Integer.parseInt(id));
                        if(cek_company.getID()==0){
                            database.addCompany(new Company(Integer.parseInt(id), name, address, _token));
                        }
                        else{
                            database.updateCompany(new Company(Integer.parseInt(id), name, address, _token));
                        }
                        insertPrototypeIntoDatabseByCompanyId(Integer.parseInt(id), token);
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                hidepDialog();
            }
        });
        //adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

    public void insertPrototypeIntoDatabseByCompanyId(int company_id, final String token){
        showpDialog("Collecting Data Prototypes...");
        String url = urlJsonPrototypes +"?company_id="+ company_id;
        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response){

                try{
                    for (int i=0; i<response.length(); i++){
                        JSONObject prototypes = (JSONObject)response.get(i);

                        String id = prototypes.getString("id");
                        String company_id = prototypes.getString("company_id");
                        String name = prototypes.getString("prototype_title");
                        String type = prototypes.getString("created_at");
                        String logo = prototypes.getString("prototype_thumbnail");
                        String description = prototypes.getString("prototype_information");
                        String prototype = prototypes.getString("prototype");

                        Project cek_project = database.getProjectById(Integer.parseInt(id));
                        if(cek_project.getID()==0){
                            database.addProject(new Project(Integer.parseInt(id), Integer.parseInt(company_id), name, type, logo, description, prototype));
                        }
                        else{
                            database.updateProject(new Project(Integer.parseInt(id), Integer.parseInt(company_id), name, type, logo, description, prototype));
                        }
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                hidepDialog();
                projectListDev.putExtra(EXTRA_MESSAGE_CID, token);
                startActivity(projectListDev);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
                projectListDev.putExtra(EXTRA_MESSAGE_CID, "hasbyGanteng");
                startActivity(projectListDev);
                finish();
            }
        });
        //adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

    private void showpDialog(String message){
        if(!pDialog.isShowing()) {
            pDialog.setMessage(message);
            pDialog.show();
        }
    }
    private void hidepDialog(){
        if(pDialog.isShowing())
            pDialog.dismiss();
    }




    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
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
