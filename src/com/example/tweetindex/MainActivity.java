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

public class MainActivity extends Activity implements OnClickListener {
	private TextView titleText, advancedText;
	private EditText searchVar;
	private Button btnSearch, btnAll;
	
	private AlertDialog.Builder noSearch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Typeface tf = Typeface.createFromAsset(getAssets(), "berlin.ttf");
		titleText = (TextView) findViewById(R.id.title_text);
		titleText.setTypeface(tf);
		advancedText = (TextView) findViewById(R.id.bottom_text);
		advancedText.setTypeface(tf);
		searchVar = (EditText) findViewById(R.id.search_var);
		
		
		btnSearch = (Button) findViewById(R.id.btn_search);
		btnAll = (Button) findViewById(R.id.btn_all);
		
		btnSearch.setOnClickListener(this);
		btnAll.setOnClickListener(this);
		advancedText.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
			case R.id.btn_search:
				String search = searchVar.getText().toString().trim();
				if (search.equals("")) {
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
					noSearch.setMessage("Please enter a stock code to query");
					AlertDialog dialogBox = noSearch.create();
					dialogBox.show();
				} else {
					intent = new Intent(getBaseContext(), Search1Activity.class);
					intent.putExtra("search", search);
					startActivity(intent);
				}
				break;
			case R.id.btn_all:
				intent = new Intent(getBaseContext(), Search1Activity.class);
				intent.putExtra("search", "__all__");
				startActivity(intent);
				break;
			case R.id.bottom_text:
				intent = new Intent(getBaseContext(), AdvancedActivity.class);
				startActivity(intent);
				break;
		}
	}
}