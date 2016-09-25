package com.example.tweetindex;

import android.os.Bundle;

public class Search2Activity extends ResultsActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String strName = this.getIntent().getStringExtra("name");
		String strLocation = this.getIntent().getStringExtra("loc");
		String strBefore = this.getIntent().getStringExtra("before");
		String strAfter = this.getIntent().getStringExtra("after");
		
		String query = "?";
		if (!strName.equals("")) {
			query = query.concat("name=" + strName + "&");
		}
		if (!strLocation.equals("")) {
			query = query.concat("loc=" + strLocation + "&");
		}
		if (!strBefore.equals("")) {
			query = query.concat("before=" + strBefore + "&");
		}
		if (!strAfter.equals("")) {
			query = query.concat("after=" + strAfter + "&");
		}
		
		setURL(query);
		setSearchText("Custom Search");
		makeRequest();
	}
}
