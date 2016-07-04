package com.example.customlistview;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {
	ArrayList<String> list = new ArrayList();
	MyAdatper myAdatper;
	CustomListView customListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		for (int i = 0; i < 10; i++) {
			list.add("data" + i);
		}
		myAdatper = new MyAdatper(this, list);
		customListView = (CustomListView) findViewById(R.id.coustomListView);
		customListView.setAdapter(myAdatper);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
