package com.example.connectme2db;

import java.lang.reflect.Field;
import java.sql.*;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewInfo extends Activity {

	Button data;
	TextView txt;
	String table,username,password,url;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_info);
		txt=(TextView)findViewById(R.id.myInfo);
		data=(Button)findViewById(R.id.viewTableCon);
		Bundle extras = getIntent().getExtras();
		if(extras != null){	
			table=extras.getString("table");
			username=extras.getString("username");
			password=extras.getString("password");
			url=extras.getString("url");

		}
		AccessDBTask1 adbt=new AccessDBTask1();
		adbt.execute(new String[]{url,username,password,table});
		data.setText(getString(R.string.viewMyTableData)+" : "+table);
		data.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent show=new Intent(ViewInfo.this,TableContent.class);
				show.putExtra("table", table);
				show.putExtra("username", username);
				show.putExtra("password", password);
				show.putExtra("url", url);
				startActivity(show);

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_info, menu);
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
	private class AccessDBTask1 extends AsyncTask<String, Void, String> {

		String str="";
		String driverClassName="org.postgresql.Driver";  
		Connection dbConnect=null;
		Statement statement=null;
		//ResultSet rs=null;
		@Override
		protected String doInBackground(String... params) {
			try{
				Class.forName(driverClassName);
				dbConnect = DriverManager.getConnection(params[0],params[1],params[2]);
				statement = dbConnect.createStatement();
				DatabaseMetaData databaseMetaData = dbConnect.getMetaData();
				/*String[] types  = { "TABLE" };
				//onoma pinaka
				ResultSet result = databaseMetaData.getTables(
				    "%", "db040", "%", types );

				   //sthles me typo dedomenwn 
				while(result.next()) {
				    String tableName = result.getString(3);
				 */
				ResultSet result1 = databaseMetaData.getColumns(
						"%", params[1],  params[3], "%");
				str+=(params[3]+"\n");
				while(result1.next()){
					String columnName = result1.getString(4);
					int    columnType = result1.getInt(5);
					str+=(columnName+"  :  "+DataType(columnType)+"\n");


				}

				str+=("---------------------------\n");

				//kyria kleidia
				result1=databaseMetaData.getPrimaryKeys("%", params[1], params[3]);
				String columnName="";
				while(result1.next()){
					columnName += result1.getString(4)+"  ";
				}
				str+=(columnName+"\n");
				System.out.println("---------------------------\n");
				//xena kleidia me sysxetiseis
				ResultSet foreignKeys = databaseMetaData.getImportedKeys("%", params[1], params[3]);


				while (foreignKeys.next()) {
					String fkTableName = foreignKeys.getString("FKTABLE_NAME");
					String fkColumnName = foreignKeys.getString("FKCOLUMN_NAME");
					String pkTableName = foreignKeys.getString("PKTABLE_NAME");
					String pkColumnName = foreignKeys.getString("PKCOLUMN_NAME");
					str+=(fkTableName + "." + fkColumnName + " -> " + pkTableName + "." + pkColumnName+"\n");
				}
				str+=("---------------------------\n");






				statement.close();
				dbConnect.close();
			}
			catch(Exception ex){

			}
			return null;
		}
		@Override
		protected void onPostExecute(String result){
			txt=(TextView)findViewById(R.id.myInfo);
			txt.setText(str);

		}
		public String DataType(int key){
			String type="";
			// Get all field in java.sql.Types
			Field[] fields = java.sql.Types.class.getFields();
			for (int i = 0; i < fields.length; i++) {
				try {
					String name = fields[i].getName();
					Integer value = (Integer) fields[i].get(null);
					if(key==value){
						type=name;
					}
				} catch (IllegalAccessException e) {
				}
			}

			return type;
		}

	}//class AccessDBTask

}
