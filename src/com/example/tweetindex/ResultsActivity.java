package com.example.tweetindex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ResultsActivity extends Activity implements OnClickListener {
	private String url;
	private SimpleAdapter adapter;
	private ListView tweetList;
	private TextView titleText, searchText, netText;
	
	private Pattern json_re, screen_name_re, location_re, tweet_re1, tweet_re2, sentiment_re, created_at_re, net_re;

	private AlertDialog.Builder crashDialog;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);
		
		Typeface tf = Typeface.createFromAsset(getAssets(), "berlin.ttf");
		titleText = (TextView) findViewById(R.id.title_text);
		titleText.setTypeface(tf);
		searchText = (TextView) findViewById(R.id.search_text);
		searchText.setTypeface(tf);
		netText = (TextView) findViewById(R.id.net_text);
		netText.setTypeface(tf);

		tweetList = (ListView) findViewById(R.id.tweet_list);
		
		titleText.setOnClickListener(this);
		url = "http://ec2-54-201-141-84.us-west-2.compute.amazonaws.com/";
		
		defineRegex();
	}
	
	protected void setURL(String query) {
		url = url.concat(query);
	}
	
	protected void setSearchText(String text) {
		searchText.setText(text);
	}

	private void defineRegex() {
		json_re = Pattern.compile("\\{([^\\}]+)");
		screen_name_re = Pattern.compile("screen_name\': u\'([^\']+)");
		location_re = Pattern.compile("location\': u\'([^\']*)");
		tweet_re1 = Pattern.compile("tweet\': u\'([^\']+)");
		tweet_re2 = Pattern.compile("tweet\': u\"([^\"]+)");
		sentiment_re = Pattern.compile("buy_sentiment\': (\\w*)");
		created_at_re = Pattern.compile("datetime\\.datetime\\((\\d{4}), (\\d{1,2}), (\\d{1,2}), (\\d{1,2}), (\\d{1,2}), (\\d{1,2})\\)");
		net_re = Pattern.compile("\\}(-*\\d+)");
	}	
	
	protected void makeRequest() {
		HttpClient Client = new DefaultHttpClient();
		List<Map<String, String>> tweets = new ArrayList<Map<String, String>>();
		
		try {
			String SetServerString = "";
			HttpGet httpget = new HttpGet(url);
//			HttpResponse response = Client.execute(httpget);
//			SetServerString = response.getEntity().toString();
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			SetServerString = Client.execute(httpget, responseHandler);
			
			Matcher json_ma = json_re.matcher(SetServerString);
			Matcher net_ma = net_re.matcher(SetServerString);
			if (net_ma.find()) {
				String tempNet = net_ma.group(1);
				if (tempNet.charAt(0) != '-') {
					tempNet = "+".concat(tempNet);
				}
				netText.setText("Net sentiment is ".concat(tempNet));
			}
			
			while (json_ma.find()) {
				Map<String, String> map = new HashMap<String, String>();
				String object = json_ma.group(1);
				String name = "", loc = "", tweet = "", sentiment = "", timestamp = "";
				
				Matcher screen_name_ma = screen_name_re.matcher(object);
				if (screen_name_ma.find()) {
					name = screen_name_ma.group(1);
				}
				Matcher location_ma = location_re.matcher(object);
				if (location_ma.find()) {
					loc = location_ma.group(1);
				}
				Matcher tweet_ma = tweet_re1.matcher(object);
				if (tweet_ma.find()) {
					tweet = tweet_ma.group(1);
				} else {
					tweet_ma = tweet_re2.matcher(object);
					if (tweet_ma.find()) {
						tweet = tweet_ma.group(1);
					}
				}
				Matcher sentiment_ma = sentiment_re.matcher(object);
				if (sentiment_ma.find(1)) {
					sentiment = sentiment_ma.group(1);
					if (sentiment.equals("True")) {
						sentiment = "BUY";
					} else {
						sentiment = "SELL";
					}
				}
				Matcher created_at_ma = created_at_re.matcher(object);
				if (created_at_ma.find()) {
					String month = created_at_ma.group(2);
					if (month.length() == 1) {
						month = "0".concat(month);
					}
					String day = created_at_ma.group(3);
					if (day.length() == 1) {
						day = "0".concat(day);
					}
					String hour = created_at_ma.group(4);
					if (hour.length() == 1) {
						hour = "0".concat(hour);
					}
					String minute = created_at_ma.group(5);
					if (minute.length() == 1) {
						minute = "0".concat(minute);
					}
					String second = created_at_ma.group(6);
					if (second.length() == 1) {
						second = "0".concat(second);
					}
					timestamp = created_at_ma.group(1) + "-" + month + "-" +
							    day + " " + hour + ":" + minute + ":" + second;
				}
				
				map.put("txt_name", name);
				map.put("txt_loc", loc);
				map.put("txt_tweet", tweet);
				map.put("txt_sentiment", sentiment);
				map.put("txt_timestamp", timestamp);
				tweets.add(map);
			}
		} catch (Exception ex) {
			crashDialog = new AlertDialog.Builder(this);
			crashDialog.setCancelable(false);
			crashDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
				@Override
				public void onClick(DialogInterface dialog, int id) {
					// End this dialog upon clicking "OK"
					ResultsActivity.this.finish();
				}
			});
			crashDialog.setTitle("The server is not responding");
			crashDialog.setMessage("Please perform the query again");
			AlertDialog dialogBox = crashDialog.create();
			dialogBox.show();
		}

		adapter = new SimpleAdapter(this, tweets, R.layout.list_items,
				  new String[] {"txt_name", "txt_loc", "txt_tweet", "txt_sentiment", "txt_timestamp"},
				  new int[] {R.id.txt_name, R.id.txt_loc, R.id.txt_tweet, R.id.txt_sentiment, R.id.txt_timestamp});
		tweetList.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.results, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
			case R.id.title_text:
				intent = new Intent(getBaseContext(), MainActivity.class);
				startActivity(intent);
				break;
		}
	}
}
