package com.zhang.mymusic.xml;

import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.zhang.mymusic.domain.Mp3Info;

public class Mp3ListContentHandler extends DefaultHandler {
	
	private List<Mp3Info> infolist = null;
	private Mp3Info info = null;
	private String tagname = null;

	
	
	public Mp3ListContentHandler(List<Mp3Info> infolist) {
		super();
		this.infolist = infolist;
	}

	public List<Mp3Info> getInfolist() {
		return infolist;
	}

	public void setInfolist(List<Mp3Info> infolist) {
		this.infolist = infolist;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		String temp = new String (ch,start,length);
		if (tagname.equals("id")) {
			info.setId(temp);
		}else if(tagname.equals("mp3.name")){
			info.setMp3Name(temp);
			
		}else if(tagname.equals("mp3.size")){
			info.setMp3Size(temp);
			
		}else if(tagname.equals("lrc.name")){
			info.setLrcName(temp);
			
		}else if(tagname.equals("lrc.size")){
			info.setLrcSize(temp);
			
		}
		
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		this.tagname = localName;
		if(tagname.equals("resource")){
			info =new Mp3Info();
		}
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		if(qName.equals("resource")){
			infolist.add(info);
		}
		tagname ="";
	}
}
