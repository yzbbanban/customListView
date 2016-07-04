package com.wangban.customlistview;

import com.example.customlistview.R;

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
	private final static int STATE_DONE = 1;// ctrl+shift+X
	private final static int STATE_PULL = 2;
	private final static int STATE_RELEASE = 3;
	private final static int STATE_REFRESHING = 4;
	int currentState;
	TextView tvState;
	ImageView ivArrow;
	ProgressBar progressBar;
	int downY;

	public CustomListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 设置初始状态
		currentState = STATE_DONE;
		view = View.inflate(getContext(), R.layout.listview_header, null);

		tvState = (TextView) view.findViewById(R.id.tv_state);
		ivArrow = (ImageView) view.findViewById(R.id.iv_arrow);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		// view.getHeight,getWidth要求view已经显示出来了

		// measure:测量
		// 0:宽度如何测
		// 0:高度如何测
		view.measure(0, 0);
		// MeasuredHeight:得测量后的大小
		height = view.getMeasuredHeight();
		view.setPadding(0, -height, 0, 0);
		this.addHeaderView(view);

	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		try {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:// 按下
				// 把状态改成pull
				if (currentState == STATE_DONE) {
					currentState = STATE_PULL;
					downY = (int) ev.getY();
				}
				break;
			case MotionEvent.ACTION_MOVE:// 移动
				// 向下拉header
				if (currentState == STATE_PULL) {
					int moveY = (int) ev.getY();
					int top = moveY - downY - height;
					//Log.i("testtop", "downY=" + downY + "," + moveY + "," + top);
					view.setPadding(0, top, 0, 0);
					// 把状态改成release
					if (top > height) {
						currentState = STATE_RELEASE;
						tvState.setText("松开刷新");
					}
				}

				break;
			case MotionEvent.ACTION_UP:// 松开
				// 把状态改成refreshing
				if (currentState == STATE_RELEASE) {
					currentState = STATE_REFRESHING;
					tvState.setText("刷新中");
					ivArrow.setVisibility(View.GONE);
					progressBar.setVisibility(View.VISIBLE);
					// 调用接口指向的实现类对象
					if (onRefreshListener != null) {
						// this,框架传数据给用框架的人
						onRefreshListener.onRefresh(this);
					}

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

	// 2.3 让接口指向实现类,给用框架的人调
	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		this.onRefreshListener = onRefreshListener;
	}

	// 2.2 申明
	OnRefreshListener onRefreshListener;

	// 2.1 定义接口
	interface OnRefreshListener {
		// 抽象方法，用框架的人实现
		// 框架调，框架传数据给用框架的人
		public void onRefresh(CustomListView customListView);
	}
}
