package com.example.tweetindex;

import android.os.Bundle;

public class Search1Activity extends ResultsActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String search = this.getIntent().getStringExtra("search");
		
		if (search.equals("__all__")) {
			search = "ALL STOCKS";
		} else {
			search = "$".concat(search).toUpperCase();
		}
		
		String query = "?search=".concat(search.replace(" ", "_"));
		setURL(query);
		setSearchText(search);
		makeRequest();
	}

}
