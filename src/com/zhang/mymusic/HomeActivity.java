package com.zhang.mymusic;

import java.io.StringReader;
import java.util.ArrayList;
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

import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.zhang.mymusic.domain.Mp3Info;
import com.zhang.mymusic.downloder.HttpDownloder;
import com.zhang.mymusic.xml.Mp3ListContentHandler;

public class HomeActivity extends Activity {

	private static final int UPDATE = 1;//菜单控制常量
	private static final int ABOUT = 2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
	}

	/**
	 * 添加自定义菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.add(0,UPDATE, 1, R.string.mp3list_update);
		menu.add(0,ABOUT, 2, R.string.mp3list_about);
		return true;
	}

	/**
	 * 响应菜单点击事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId()== UPDATE){
			//用户点击更新
			Toast.makeText(HomeActivity.this, "gengxin", 1).show();
			String xml =downloaderXML("http://192.168.1.109:8080/mp3/resources.xml");
			parse( xml);
			System.out.println("->>"+xml);
		}else if(item.getItemId()== ABOUT){
			//用户点击关于
			Toast.makeText(HomeActivity.this, "guanyu", 1).show();
		}
		return super.onOptionsItemSelected(item);
	}
	
	private String downloaderXML(String url){
		HttpDownloder downloder = new HttpDownloder();
		String result = downloder.download(url);	
		return result;
	}
	
	private List<Mp3Info> parse(String xml){
		List<Mp3Info> infolist = new ArrayList<Mp3Info>();
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		try {
			XMLReader reader = saxParserFactory.newSAXParser().getXMLReader();
			
			Mp3ListContentHandler contentHandler = new Mp3ListContentHandler(infolist);
			reader.setContentHandler(contentHandler);
			reader.parse(new InputSource(new StringReader(xml)));
			for (Iterator iterator = infolist.iterator(); iterator.hasNext();) {
				Mp3Info mp3Info = (Mp3Info) iterator.next();
				System.out.println(mp3Info);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return infolist;
		
	}
}

