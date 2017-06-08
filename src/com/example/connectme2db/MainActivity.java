package com.example.connectme2db;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	private Button abort,goNext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		abort=(Button)findViewById(R.id.abort);
		goNext=(Button)findViewById(R.id.connect);
		goNext.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent con=new Intent(MainActivity.this,DBSettings.class);
				startActivityForResult(con, 0);

			}
		});
		abort.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 0) {
			if(resultCode == RESULT_OK){ 
				goNext.setText(getString(R.string.connectTables1));
				final String url=data.getStringExtra("url");
				final String username=data.getStringExtra("username");
				final String password=data.getStringExtra("password");
				goNext.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent con=new Intent(MainActivity.this,Tables.class);
						con.putExtra("url", url);
						con.putExtra("username", username);
						con.putExtra("password", password);
						con.putExtra("classKey","0");
						startActivity(con);
					}
				});
				abort.setText(getString(R.string.queryEditor1));
				abort.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent con=new Intent(MainActivity.this,Tables.class);
						con.putExtra("url", url);
						con.putExtra("username", username);
						con.putExtra("password", password);
						con.putExtra("classKey","1");
						startActivity(con);
					}
				});
			}
		}
	}
}
