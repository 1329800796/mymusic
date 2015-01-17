package com.zhang.mymusic;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.zhang.mymusic.domain.Mp3Info;
import com.zhang.mymusic.downloder.HttpDownloder;
import com.zhang.mymusic.xml.Mp3ListContentHandler;

public class HomeActivity extends Activity {

	private static final int UPDATE = 1;// 菜单控制常量
	private static final int ABOUT = 2;
	private ListView listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		listview = (ListView) findViewById(R.id.list);
		updateListView();
	}

	/**
	 * 添加自定义菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.add(0, UPDATE, 1, R.string.mp3list_update);
		menu.add(0, ABOUT, 2, R.string.mp3list_about);
		return true;
	}

	/**
	 * 响应菜单点击事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == UPDATE) {
			// 用户点击更新
			updateListView();
		} else if (item.getItemId() == ABOUT) {
			// 用户点击关于
			Toast.makeText(HomeActivity.this, "guanyu", 1).show();
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 更新
	 */
	private void updateListView() {
		// 下载基本信息xml
		String xml = downloaderXML("http://192.168.1.100:8080/mp3/resources.xml");
		// 解析xml，并且封装成MP3list
		List<Mp3Info> mp3infolist = parse(xml);
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		for (Iterator iterator = mp3infolist.iterator(); iterator.hasNext();) {
			Mp3Info mp3info = (Mp3Info) iterator.next();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("mp3_name", mp3info.getMp3Name());
			map.put("mp3_size", mp3info.getMp3Size());
			list.add(map);
		}

		SimpleAdapter simpleAdapter = new SimpleAdapter(this, list,
				R.layout.mp3info_item, new String[] { "mp3_name", "mp3_size" },
				new int[] { R.id.mp3_name, R.id.mp3_size });
		listview.setAdapter(simpleAdapter);
	}

	/**
	 * 下载xml
	 * 
	 * @param url
	 * @return
	 */
	private String downloaderXML(String url) {
		HttpDownloder downloder = new HttpDownloder();
		String result = downloder.download(url);
		return result;
	}

	/**
	 * 解析封装
	 * 
	 * @param xml
	 * @return
	 */
	private List<Mp3Info> parse(String xml) {
		List<Mp3Info> infolist = new ArrayList<Mp3Info>();
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		try {
			XMLReader reader = saxParserFactory.newSAXParser().getXMLReader();

			Mp3ListContentHandler contentHandler = new Mp3ListContentHandler(
					infolist);
			reader.setContentHandler(contentHandler);
			reader.parse(new InputSource(new StringReader(xml)));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return infolist;

	}
}
