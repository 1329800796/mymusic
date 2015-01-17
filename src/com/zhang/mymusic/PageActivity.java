package com.zhang.mymusic;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * 以分页的形式把远程和本地的界面 放在这个界面中。
 * 
 * @author Administrator
 * 
 */
public class PageActivity extends TabActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page);
		// 得到host对象。
		TabHost tabHost = getTabHost();
		// 生成意图，指向一个活动
		Intent homeit = new Intent();
		homeit.setClass(this, HomeActivity.class);
		// 代表一页
		TabHost.TabSpec homeSpec = tabHost.newTabSpec("Home");
		Resources res = getResources();
		homeSpec.setIndicator("Home",
				res.getDrawable(android.R.drawable.stat_sys_download));
		homeSpec.setContent(homeit);
		tabHost.addTab(homeSpec);

		// 生成意图，指向一个活动
		Intent localit = new Intent();
		localit.setClass(this, LocalHostActivity.class);
		// 代表一页
		TabHost.TabSpec localitSpec = tabHost.newTabSpec("Local");
		localitSpec.setIndicator("Local",
				res.getDrawable(android.R.drawable.stat_sys_upload));
		localitSpec.setContent(localit);
		tabHost.addTab(localitSpec);

	}

}
