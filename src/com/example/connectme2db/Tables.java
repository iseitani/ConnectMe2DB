package com.example.connectme2db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Tables extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tables);
		Bundle extras = getIntent().getExtras();
		if(extras != null){	
			AccessDBTask adbt=new AccessDBTask();
			adbt.execute(new String[]{extras.getString("url"),extras.getString("username"),extras.getString("password"),extras.getString("classKey")});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tables, menu);
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
	private class AccessDBTask extends AsyncTask<String, Void, String> {

		//MyTask m;
		ArrayList<String> s=new ArrayList<String>();
		String username;
		String password;
		String url;
		int classKey;
		@Override
		protected String doInBackground(String... params) {

			String driverClassName="org.postgresql.Driver"; 
			username=params[1];
			password=params[2];
			url=params[0];
			classKey=Integer.parseInt(params[3]);
			Connection dbConnect;
			Statement statement;
			//ResultSet rs;
			//String []display;
			//code for jdbc
			try{
				Class.forName (driverClassName);
				dbConnect = DriverManager.getConnection (url, username, password);
				statement = dbConnect.createStatement();

				DatabaseMetaData databaseMetaData = dbConnect.getMetaData();
				String[] types  = { "TABLE" };
				ResultSet result = databaseMetaData.getTables(
						"%",username, "%", types );
				while(result.next()) {
					String tableName = result.getString(3);
					s.add(tableName);
				}

				statement.close();
				dbConnect.close();
			}catch(Exception ex){

			}
			return null;
		}
		@Override
		protected void onPostExecute(String result){

			//Toast.makeText(Tables.this,url+username+password, Toast.LENGTH_SHORT).show();
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(Tables.this,R.layout.activity_listview, s);

			final ListView listView = (ListView) findViewById(R.id.country_list);
			listView.setOnItemClickListener(new OnItemClickListener(){

				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					String result=(listView.getItemAtPosition(position)).toString();
					if(classKey==0){
						Intent show=new Intent(Tables.this,ViewInfo.class);
						show.putExtra("table", result);
						show.putExtra("username", username);
						show.putExtra("password", password);
						show.putExtra("url", url);
						startActivity(show);
					}
					else{
						Intent show=new Intent(Tables.this,QueryEditor.class);
						show.putExtra("table", result);
						show.putExtra("username", username);
						show.putExtra("password", password);
						show.putExtra("url", url);
						startActivity(show);
					}
				}

			});
			listView.setAdapter(adapter);
		}


	}//class AccessDBTask

}
