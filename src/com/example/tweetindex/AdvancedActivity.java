package com.example.tweetindex;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AdvancedActivity extends Activity implements OnClickListener {
	private TextView titleText, viewName, viewLoc, viewBefore, viewAfter;
	private EditText screenName, location, before, after;
	private Button btnAdvanced;
	
	private AlertDialog.Builder noSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advanced);
		
		Typeface tf = Typeface.createFromAsset(getAssets(), "berlin.ttf");
		titleText = (TextView) findViewById(R.id.title_text);
		titleText.setTypeface(tf);
		viewName = (TextView) findViewById(R.id.view_name);
		viewName.setTypeface(tf);
		viewLoc = (TextView) findViewById(R.id.view_loc);
		viewLoc.setTypeface(tf);
		viewBefore = (TextView) findViewById(R.id.view_before);
		viewBefore.setTypeface(tf);
		viewAfter = (TextView) findViewById(R.id.view_after);
		viewAfter.setTypeface(tf);
		
		screenName = (EditText) findViewById(R.id.screen_name);
		location = (EditText) findViewById(R.id.location);
		before = (EditText) findViewById(R.id.before);
		after = (EditText) findViewById(R.id.after);
		
		btnAdvanced = (Button) findViewById(R.id.btn_advanced);
		btnAdvanced.setOnClickListener(this);
		titleText.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.advanced, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
			case R.id.btn_advanced:
				String strName = screenName.getText().toString().trim();
				String strLocation = location.getText().toString().trim();
				String strBefore = before.getText().toString().trim();
				String strAfter = after.getText().toString().trim();
				if (strName.equals("") && strLocation.equals("") && strBefore.equals("") && strAfter.equals("")) {
					noSearch = new AlertDialog.Builder(this);
					noSearch.setCancelable(false);
					noSearch.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
						@Override
						public void onClick(DialogInterface dialog, int id) {
							// End this dialog upon clicking "OK"
							dialog.dismiss();
						}
					});
					noSearch.setTitle("No Search Item");
					noSearch.setMessage("Please enter atleast one value to query");
					AlertDialog dialogBox = noSearch.create();
					dialogBox.show();
				} else {
					intent = new Intent(getBaseContext(), Search2Activity.class);
					intent.putExtra("name", strName);
					intent.putExtra("loc", strLocation);
					intent.putExtra("before", strBefore);
					intent.putExtra("after", strAfter);
					startActivity(intent);
				}
				break;
			case R.id.title_text:
				intent = new Intent(getBaseContext(), MainActivity.class);
				startActivity(intent);
				break;
		}
	}
}
