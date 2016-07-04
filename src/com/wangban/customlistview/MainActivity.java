package com.wangban.customlistview;

import java.util.ArrayList;

import com.example.customlistview.R;
import com.wangban.customlistview.CustomListView.OnRefreshListener;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {
	ArrayList<String> list = new ArrayList();
	MyAdatper myAdatper;
	// ��һ������ˢ�¿��
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
		MyOnRefreshListener myOnRefreshListener = new MyOnRefreshListener();
		customListView.setOnRefreshListener(myOnRefreshListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// 2.5 дʵ����
	class MyOnRefreshListener implements OnRefreshListener {

		@Override
		public void onRefresh(final CustomListView customListView) {
			new Thread() {
				public void run() {
					try {
						this.sleep(2000);
						String data = "����ȡ��������";
						list.add(data);
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								myAdatper.notifyDataSetChanged();
								customListView.refreshComplete();
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
					}
				};
			}.start();
		}

	}

}
