package com.flipbox.skyline.procase.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.flipbox.skyline.procase.Database.Company;
import com.flipbox.skyline.procase.Database.DataBaseHandler;
import com.flipbox.skyline.procase.Database.Project;
import com.flipbox.skyline.procase.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;


public class JSONParser extends Activity {
    private String urlJsonCompanies = "http://protocase.sakadigital.id/api/companies";              // json object response url
    private String urlJsonPrototypes = "http://protocase.sakadigital.id/api/company_prototypes";    // json array response url
    private static String TAG = JSONParser.class.getSimpleName();
    private ProgressDialog pDialog;             // Progress dialog
    private String jsonResponse;                // temporary string to show the parsed response
    private DataBaseHandler databse;

    public JSONParser(ProgressDialog _pDialog, DataBaseHandler database){
        pDialog = _pDialog;
        databse = database;
    }

    /**
     *  Method to make json array request where json response start with "["
     */
/*    private void checkToken(final String token){
        showpDialog();
        String url = urlJsonCompanies +"?token="+ token;
        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response){
                //check = true;
                hidepDialog();
                Toast.makeText(JSONParser.this, "User ID benar !!", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //check = false;
                hidepDialog();
                Toast.makeText(JSONParser.this, "User ID salah !!", Toast.LENGTH_LONG).show();
            }
        });
        //adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }
*/
    public void insertIntoDatabseByToken(String token){
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
                        String token = company.getString("token");

                        databse.addCompany(new Company(Integer.parseInt(id), name, address, token));
                        insertPrototypeIntoDatabseByCompanyId(Integer.parseInt(id));
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

    public void insertPrototypeIntoDatabseByCompanyId(int company_id){
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
                        String name = prototypes.getString("name");
                        String type = prototypes.getString("type");
                        String logo = prototypes.getString("logo");
                        String description = prototypes.getString("description");
                        String prototype = prototypes.getString("prototype");

                        databse.addProject(new Project(Integer.parseInt(id), Integer.parseInt(company_id), name, type, logo, description, prototype));
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


}
