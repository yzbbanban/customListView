package com.wangban.customlistview;

import javax.crypto.spec.IvParameterSpec;

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
	private final static int STATE_DONE = 1;
	private final static int STATE_PULL = 2;
	private final static int STATE_RELEASE = 3;
	private final static int STATE_REFRESHING = 4;

	int currentState;
	TextView tvState;
	ProgressBar progressBar;
	int downY;
	private ImageView ivArrow;
	/**
	 * ����ˢ�¿��
	 * @param context
	 * @param attrs
	 */
	public CustomListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		currentState = STATE_DONE;
		view = View.inflate(getContext(), R.layout.listview_header, null);
		tvState = (TextView) view.findViewById(R.id.tv_state);
		ivArrow = (ImageView) view.findViewById(R.id.iv_arrow);

		// ������Ĵ�Сmesure(),��ȣ��߶Ȳ���
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
				// ״̬Ϊpull
				if (currentState == STATE_DONE) {
					currentState = STATE_PULL;
					downY = (int) ev.getY();
				}
				break;
			case MotionEvent.ACTION_MOVE:
				// ������header
				if (currentState == STATE_PULL) {
					int moveY = (int) ev.getY();
					int top = moveY - height;
					// int left=(int) ev.getX();
					Log.i("test_top", "top: " + top);
					view.setPadding(0, top, 0, 0);
					currentState = STATE_RELEASE;
				}
				// �Ѡ�B�Ğ�release

				break;
			case MotionEvent.ACTION_UP:
				// �Ѡ�B�ĳ�refreshing
				if (currentState == STATE_RELEASE) {
					currentState = STATE_RELEASE;
					tvState.setText("ˢ����");
					ivArrow.setVisibility(View.GONE);
					progressBar.setVisibility(View.VISIBLE);
					// ���ýӿ�ָ���ʵ�������
					if (onRefreshListener != null) {
						// this,��ܴ����ݸ���Ҫ�õ���
						onRefreshListener.onRefresh(this);
					}
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

	// 2.3 �ýӿ�ʵ����
	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {

	}

	// 2.2����
	OnRefreshListener onRefreshListener;

	// 2.1����ӿ�
	interface OnRefreshListener {
		// ���󷽷����ÿ�ܵ�ʵ��
		// ��ܵ�����ܴ����ݸ��ÿ�ܵ���
		public void onRefresh(CustomListView customListView);
	}

}
