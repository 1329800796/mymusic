package com.zhang.mymusic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.zhang.mymusic.domain.Mp3Info;
import com.zhang.mymusic.utils.FileUtils;

/**
 * 显示本地sd卡数据的界面
 * 
 * @author Administrator
 * 
 */
public class LocalHostActivity extends Activity {

	private ListView listView;
	private List<Mp3Info> mp3Infos = null;
	private Mp3Info mp3Info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local_activity);
		listView = (ListView) findViewById(R.id.list);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterview, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (mp3Infos != null) {
				    mp3Info = mp3Infos.get(position);
					Intent it = new Intent();
					it.putExtra("mp3Info", mp3Info);
					
					it.setClass(LocalHostActivity.this, Mp3PlayActivity.class);
					startActivity(it);
				}

			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		FileUtils fileUtils = new FileUtils();
		mp3Infos = fileUtils.getMp3Files("mp3/");
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for (Iterator iterator = mp3Infos.iterator(); iterator.hasNext();) {
		    mp3Info = (Mp3Info) iterator.next();
		    
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("mp3_name", mp3Info.getMp3Name());
			map.put("mp3_size", mp3Info.getMp3Size());
			list.add(map);
		}
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, list,
				R.layout.mp3info_item, new String[] { "mp3_name", "mp3_size" },
				new int[] { R.id.mp3_name, R.id.mp3_size });

		listView.setAdapter(simpleAdapter);
	}
}
