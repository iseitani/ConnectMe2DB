package com.example.connectme2db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class QueryEditor extends Activity {

	Button queryButton;
	//String url,username,password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_editor);
		queryButton=(Button)findViewById(R.id.queryStringExe);
		queryButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle extras = getIntent().getExtras();
				if(extras != null){	
					AccessDBTask3 adbt=new AccessDBTask3();
					adbt.execute(new String[]{extras.getString("url"),extras.getString("username"),extras.getString("password"),extras.getString("table")});
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.query_editor, menu);
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
	private class AccessDBTask3 extends AsyncTask<String, Void, String> {

		//TableLayout table = new TableLayout(TableContent.this);
		String msg;
		String driverClassName="org.postgresql.Driver";  
		Connection dbConnect=null;
		Statement statement=null;
		ResultSet rs=null;
		boolean flag;
		EditText queryT;
		@Override
		protected String doInBackground(String... params) {
			try{
				 queryT=(EditText)findViewById(R.id.queryString);
				String query=queryT.getText().toString();
				Class.forName(driverClassName);
				dbConnect = DriverManager.getConnection(params[0],params[1],params[2]);
				statement = dbConnect.createStatement();
				
				if(query.substring(0,5).toString().equalsIgnoreCase("SELECT")){
					flag=true;
	               rs=statement.executeQuery(query);
	               msg="<html><head><style>table,th,td{border:4px solid black;}</style></head><body><table style=\"width:300px\"><tr>";
	               DatabaseMetaData databaseMetaData = dbConnect.getMetaData();
					ResultSet result1 = databaseMetaData.getColumns("%", params[1],  params[3], "%");
					//	TableRow tb=new TableRow(TableContent.this);
					rs = statement.executeQuery(query);
					ResultSetMetaData rsmd = rs.getMetaData();
					int columnsNumber = rsmd.getColumnCount();
					String[] cols=new String[columnsNumber];
					int i=0;
					while(result1.next()){
						String columnName = result1.getString(4);
						cols[i++]=columnName;
						msg+="<th>"+columnName+"</th>";
						// TextView txt=new TextView(TableContent.this);
						// txt.setText(columnName+"     ");
						//tb.addView(txt);	  
					}
					// table.addView(tb);
					msg+="</tr>";
					while(rs.next()){
						//TableRow tb1=new TableRow(TableContent.this);
						msg+="<tr>";
						for (int j=0;j<cols.length;j++){
							//TextView txt1=new TextView(TableContent.this);
							//txt1.setText(rs.getString(cols[j])+"     ");
							//tb1.addView(txt1);
							msg+="<td>"+rs.getString(cols[j])+"</td>";
						}
						// table.addView(tb1);
						msg+="</tr>";
					}
					msg+="</table></body></html>";
				}
				else {
					flag=false;
					statement.executeUpdate(query);
					msg=getString(R.string.QueryExecuted);
			}
				
				statement.close();
				dbConnect.close();
			}
			catch(Exception ex){

			}
			return null;
		}
		@Override
		protected void onPostExecute(String result){
			TextView txt;
            if(flag){
            	setContentView(R.layout.queryexecutor);
            	txt=(TextView)findViewById(R.id.updateExecute1);
            	txt.setText(queryT.getText().toString());
            	WebView w=(WebView)findViewById(R.id.webviewQuery);
            	w.loadData(msg, "text/html", "UTF-8");
            	Button b=(Button)findViewById(R.id.backToExe);
            	b.setOnClickListener(new View.OnClickListener() {
					
					public void onClick(View v) {
						// TODO Auto-generated method stub
						setContentView(R.layout.activity_query_editor);
					}
				});
            }
			
		}


	}//class AccessDBTask
}
