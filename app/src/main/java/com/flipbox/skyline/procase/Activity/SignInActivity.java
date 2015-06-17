package com.flipbox.skyline.procase.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.flipbox.skyline.procase.Database.Client;
import com.flipbox.skyline.procase.Database.User;
import com.flipbox.skyline.procase.Database.DataBaseHandler;
import com.flipbox.skyline.procase.Database.Project;
import com.flipbox.skyline.procase.R;


public class SignInActivity extends Activity {
    private DataBaseHandler database;
    public final static String EXTRA_MESSAGE_CID = "com.flipbox.skyline.procase.MESSAGCID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        database = new DataBaseHandler(this);

        database.addUser(new User(1, "client 1", "Client", "hahahah"));
        database.addUser(new User(2, "Hasby", "Developer", "Wadud forever"));
        database.addUser(new User(3, "client 2", "Client", "hahahah"));
        database.addProject(new Project(1,3,"dika 3", "hasby", "logo", "ini desktipsi1", "prototype"));
        database.addProject(new Project(2,3, "memang 3", "dak", "logo", "description 2", "prototype"));
        database.addProject(new Project(3,3, "tampan 3", "setuju", "logo", "description 3", "prototype"));
        database.addProject(new Project(4,3, "hahaha 3", "hehehe", "logo", "description 4", "prototype"));
        database.addProject(new Project(5,1,"dika 1", "hasby", "logo", "ini desktipsi1", "prototype"));
        database.addProject(new Project(6,1,"memang 1","dak","logo","description 2","prototype"));
        database.addProject(new Project(7,1,"tampan 1","setuju","logo","description 3","prototype"));
        database.addProject(new Project(8,1,"hahaha 1","hehehe","logo","description 4","prototype"));

        //listView = (ListView) findViewById(R.id.listView);
       // layout = (LinearLayout) findViewById(R.id.progressbar_view);

    }

    public void sendMessage(View v){

        Intent projectList = new Intent(this,ProjectList.class);
        Intent projectListDev = new Intent(this,ProjectListDev.class);
        EditText clientid = (EditText)findViewById(R.id.editText);
        String textClientid = clientid.getText().toString();
            if (textClientid.equals("")) {
                Toast.makeText(SignInActivity.this, "Isi User ID ! BODOH !!", Toast.LENGTH_LONG).show();
            }
            else{
                User validation = database.getUser(Integer.parseInt(textClientid));
                if (validation.getType().equals("Developer")) {
                    startActivity(projectListDev);
                } else if (validation.getType().equals("Client")) {
                    projectList.putExtra(EXTRA_MESSAGE_CID, textClientid);
                    startActivity(projectList);
                } else {
                    Toast.makeText(SignInActivity.this, "User ID salah !! BODOH !!", Toast.LENGTH_LONG).show();
                }
            }
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
