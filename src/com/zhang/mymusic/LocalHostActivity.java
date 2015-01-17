package com.zhang.mymusic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.zhang.mymusic.domain.Mp3Info;
import com.zhang.mymusic.utils.FileUtils;
/**
 * 显示本地sd卡数据的界面
 * @author Administrator
 *
 */
public class LocalHostActivity extends Activity {
 private ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local_activity);
		listView = (ListView) findViewById(R.id.list);
		
	}
	
	@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			FileUtils  fileUtils = new FileUtils();
			List<Mp3Info> mp3Infos = fileUtils.getmp3files("mp3/");
			
			List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
			for (Iterator iterator = mp3Infos.iterator(); iterator.hasNext();) {
				Mp3Info mp3info =  (Mp3Info) iterator
						.next();
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("mp3_name", mp3info.getMp3Name());
				map.put("mp3_size", mp3info.getMp3Size());
				list.add(map);
			}
			SimpleAdapter simpleAdapter = new SimpleAdapter(this, list, R.layout.mp3info_item, new String[]{"mp3_name","mp3_size"},new int[]{R.id.mp3_name,R.id.mp3_size});
//			simpleAdapter.notifyDataSetChanged();
			listView.setAdapter(simpleAdapter);
		}
}
