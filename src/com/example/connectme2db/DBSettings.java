package com.example.connectme2db;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DBSettings extends Activity {

	private Button connectme;
	private EditText username,password,host,port,dbname;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dbsettings);
		username=(EditText)findViewById(R.id.usernameDBS);
		password=(EditText)findViewById(R.id.passwordDBS);
		port=(EditText)findViewById(R.id.port);
		dbname=(EditText)findViewById(R.id.dbname);
		host=(EditText)findViewById(R.id.url);
		connectme=(Button)findViewById(R.id.connectAtDB);
		connectme.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				//String url="jdbc:postgresql://"+host.getText()+":"+port.getText()+"/"+dbname.getText();
				//String user=username.getText().toString();
				Intent returnIntent = new Intent();
				returnIntent.putExtra("url","jdbc:postgresql://"+host.getText()+":"+port.getText()+"/"+dbname.getText());
				returnIntent.putExtra("username",username.getText().toString());
				returnIntent.putExtra("password",password.getText().toString());
				setResult(RESULT_OK,returnIntent);     
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dbsettings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
