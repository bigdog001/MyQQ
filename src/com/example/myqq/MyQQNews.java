package com.example.myqq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.download.httpDownloader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MyQQNews extends Activity{
	private static ListView listView;
	private Button button;
	List<Map<String, String>> moreList;
	private PopupWindow pwMyPopWindow;// popupwindow
	private ListView lvPopupList;// popupwindow中的ListView
	private int NUM_OF_VISIBLE_LIST_ROWS = 3;// 指定popupwindow中Item的数量
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_item_news);
		listView=(ListView) findViewById(R.id.listview);
		button=(Button) findViewById(R.id.button);
		iniData();
		iniPopupWindow();

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pwMyPopWindow.isShowing()) {

					pwMyPopWindow.dismiss();// 关闭
				} else {
					pwMyPopWindow.showAsDropDown(button);// 显示
				}

			}
		});


		httpDownloader httpDownloader=new httpDownloader(this, listView);
		httpDownloader.execute();

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent=new Intent();
				intent.setClass(MyQQNews.this, MyQQNewsMessge.class);
				startActivity(intent);
			}
		});
	}
	//数据
	private void iniData() {

		moreList = new ArrayList<Map<String, String>>();
		Map<String, String> map;
		map = new HashMap<String, String>();
		map.put("share_key", "复制");
		moreList.add(map);
		map = new HashMap<String, String>();
		map.put("share_key", "删除");
		moreList.add(map);
		map = new HashMap<String, String>();
		map.put("share_key", "修改");
		moreList.add(map);
	}

	private void iniPopupWindow() {

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.task_detail_popupwindow, null);
		lvPopupList = (ListView) layout.findViewById(R.id.lv_popup_list);
		pwMyPopWindow = new PopupWindow(layout);
		pwMyPopWindow.setFocusable(true);// 加上这个popupwindow中的ListView才可以接收点击事件

		lvPopupList.setAdapter(new SimpleAdapter(MyQQNews.this, moreList,
				R.layout.list_item_popupwindow, new String[] { "share_key" },
				new int[] { R.id.tv_list_item }));
		lvPopupList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Toast.makeText(MyQQNews.this,
						moreList.get(position).get("share_key"),
						Toast.LENGTH_LONG).show();
			}
		});

		// 控制popupwindow的宽度和高度自适应
		lvPopupList.measure(View.MeasureSpec.UNSPECIFIED,
				View.MeasureSpec.UNSPECIFIED);
		pwMyPopWindow.setWidth(lvPopupList.getMeasuredWidth());
		pwMyPopWindow.setHeight((lvPopupList.getMeasuredHeight() + 20)
				* NUM_OF_VISIBLE_LIST_ROWS);

		// 控制popupwindow点击屏幕其他地方消失
		pwMyPopWindow.setBackgroundDrawable(this.getResources().getDrawable(
				R.drawable.bg_popupwindow));// 设置背景图片，不能在布局中设置，要通过代码来设置
		pwMyPopWindow.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上
	}

}
