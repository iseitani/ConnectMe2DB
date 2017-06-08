package com.example.connectme2db;

//import java.lang.reflect.Field;
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
import android.webkit.WebSettings;
import android.webkit.WebView;

public class TableContent extends Activity {

	String table;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_table_content);
		Bundle extras = getIntent().getExtras();
		if(extras != null){	
			AccessDBTask2 adbt=new AccessDBTask2();
			adbt.execute(new String[]{extras.getString("url"),extras.getString("username"),extras.getString("password"),extras.getString("table")});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.table_content, menu);
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

	private class AccessDBTask2 extends AsyncTask<String, Void, String> {

		//TableLayout table = new TableLayout(TableContent.this);
		String htmlCode="<html><head><style>table,th,td{border:4px solid black;}</style></head><body><table style=\"width:300px\"><tr>";
		String driverClassName="org.postgresql.Driver";  
		Connection dbConnect=null;
		Statement statement=null;
		ResultSet rs=null;
		@Override
		protected String doInBackground(String... params) {
			try{
				Class.forName(driverClassName);
				dbConnect = DriverManager.getConnection(params[0],params[1],params[2]);
				statement = dbConnect.createStatement();
				DatabaseMetaData databaseMetaData = dbConnect.getMetaData();
				ResultSet result1 = databaseMetaData.getColumns(
						"%", params[1],  params[3], "%");
				//	TableRow tb=new TableRow(TableContent.this);
				String query="SELECT * FROM "+params[3];
				rs = statement.executeQuery(query);
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnsNumber = rsmd.getColumnCount();
				String[] cols=new String[columnsNumber];
				int i=0;
				while(result1.next()){
					String columnName = result1.getString(4);
					cols[i++]=columnName;
					htmlCode+="<th>"+columnName+"</th>";
					// TextView txt=new TextView(TableContent.this);
					// txt.setText(columnName+"     ");
					//tb.addView(txt);	  
				}
				// table.addView(tb);
				htmlCode+="</tr>";
				while(rs.next()){
					//TableRow tb1=new TableRow(TableContent.this);
					htmlCode+="<tr>";
					for (int j=0;j<cols.length;j++){
						//TextView txt1=new TextView(TableContent.this);
						//txt1.setText(rs.getString(cols[j])+"     ");
						//tb1.addView(txt1);
						htmlCode+="<td>"+rs.getString(cols[j])+"</td>";
					}
					// table.addView(tb1);
					htmlCode+="</tr>";
				}
				htmlCode+="</table></body></html>";
				statement.close();
				dbConnect.close();
			}
			catch(Exception ex){

			}
			return null;
		}
		@Override
		protected void onPostExecute(String result){

			// HorizontalScrollView l=(HorizontalScrollView)findViewById(R.id.myTableLayout);
			// l.addView(table);
			WebView w=(WebView)findViewById(R.id.myTableContent);
			WebSettings webSettings = w.getSettings();
			webSettings.setBuiltInZoomControls(true);
			w.loadData(htmlCode, "text/html", "UTF-8");
		}


	}//class AccessDBTask

}
