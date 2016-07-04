package com.example.customlistview;

import javax.crypto.spec.IvParameterSpec;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CustomListView extends ListView {
	View view;
	int height;
	private final static int STATE_DONE = 1;
	private final static int STATE_PULL = 2;
	private final static int STATE_RELEASE = 3;
	private final static int STATE_REFRESHING = 4;

	int currentState;
	TextView tvState;
	ProgressBar progressBar;
	int downY;
	private ImageView ivArrow;

	public CustomListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		currentState = STATE_DONE;
		view = View.inflate(getContext(), R.layout.listview_header, null);
		tvState = (TextView) view.findViewById(R.id.tv_state);
		ivArrow = (ImageView) view.findViewById(R.id.iv_arrow);

		// 测量后的大小mesure(),宽度，高度测量
		view.measure(0, 0);

		height = view.getMeasuredHeight();
		view.setPadding(0, -height, 0, 0);
		this.addHeaderView(view);

	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		try {

			int action = ev.getAction();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				// 状态为pull
				if (currentState == STATE_DONE) {
					currentState = STATE_PULL;
					downY = (int) ev.getY();
				}
				break;
			case MotionEvent.ACTION_MOVE:
				// 向下拉header
				if (currentState == STATE_PULL) {
					int moveY = (int) ev.getY();
					int top = moveY - height;
					// int left=(int) ev.getX();
					Log.i("test_top", "top: " + top);
					view.setPadding(0, top, 0, 0);
					currentState = STATE_RELEASE;
				}
				// 把狀態改為release

				break;
			case MotionEvent.ACTION_UP:
				// 把狀態改成refreshing
				if (currentState == STATE_RELEASE) {
					currentState = STATE_RELEASE;
					tvState.setText("刷新中");
					ivArrow.setVisibility(View.GONE);
					progressBar.setVisibility(View.VISIBLE);
					currentState = STATE_DONE;
				}
				break;

			default:
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return super.onTouchEvent(ev);
	}

}
