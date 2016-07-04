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
		// ���ó�ʼ״̬
		currentState = STATE_DONE;
		view = View.inflate(getContext(), R.layout.listview_header, null);

		tvState = (TextView) view.findViewById(R.id.tv_state);
		ivArrow = (ImageView) view.findViewById(R.id.iv_arrow);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		// view.getHeight,getWidthҪ��view�Ѿ���ʾ������

		// measure:����
		// 0:�����β�
		// 0:�߶���β�
		view.measure(0, 0);
		// MeasuredHeight:�ò�����Ĵ�С
		height = view.getMeasuredHeight();
		view.setPadding(0, -height, 0, 0);
		this.addHeaderView(view);

	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		try {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:// ����
				// ��״̬�ĳ�pull
				if (currentState == STATE_DONE) {
					currentState = STATE_PULL;
					downY = (int) ev.getY();
				}
				break;
			case MotionEvent.ACTION_MOVE:// �ƶ�
				// ������header
				if (currentState == STATE_PULL) {
					int moveY = (int) ev.getY();
					int top = moveY - downY - height;
					//Log.i("testtop", "downY=" + downY + "," + moveY + "," + top);
					view.setPadding(0, top, 0, 0);
					// ��״̬�ĳ�release
					if (top > height) {
						currentState = STATE_RELEASE;
						tvState.setText("�ɿ�ˢ��");
					}
				}

				break;
			case MotionEvent.ACTION_UP:// �ɿ�
				// ��״̬�ĳ�refreshing
				if (currentState == STATE_RELEASE) {
					currentState = STATE_REFRESHING;
					tvState.setText("ˢ����");
					ivArrow.setVisibility(View.GONE);
					progressBar.setVisibility(View.VISIBLE);
					// ���ýӿ�ָ���ʵ�������
					if (onRefreshListener != null) {
						// this,��ܴ����ݸ��ÿ�ܵ���
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

	// 2.3 �ýӿ�ָ��ʵ����,���ÿ�ܵ��˵�
	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		this.onRefreshListener = onRefreshListener;
	}

	// 2.2 ����
	OnRefreshListener onRefreshListener;

	// 2.1 ����ӿ�
	interface OnRefreshListener {
		// ���󷽷����ÿ�ܵ���ʵ��
		// ��ܵ�����ܴ����ݸ��ÿ�ܵ���
		public void onRefresh(CustomListView customListView);
	}
}
