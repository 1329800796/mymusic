package com.zhang.mymusic;

import java.io.File;

import com.zhang.mymusic.domain.Mp3Info;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 播放音乐
 * 
 * @author Administrator
 * 
 */
public class Mp3PlayActivity extends Activity implements OnClickListener {
	private Button begin;
	private Button pause;
	private Button stop;
	private MediaPlayer mediaPlayer = null;
	private Mp3Info mp3Info = null;
	private boolean isplaying = false;
	private boolean ispause = false;
	private boolean isrelease = false;// 释放的状态，停止

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playermp3);
		Intent intent = getIntent();
		mp3Info = (Mp3Info) intent.getSerializableExtra("mp3info");
		begin = (Button) findViewById(R.id.button1);
		pause = (Button) findViewById(R.id.button2);
		stop = (Button) findViewById(R.id.button3);
		begin.setOnClickListener(this);
		pause.setOnClickListener(this);
		stop.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			if (!isplaying) {
				String path = getMp3Path(mp3Info);
				mediaPlayer = MediaPlayer.create(Mp3PlayActivity.this,
						Uri.parse("file://" + path));
				mediaPlayer.setLooping(false);
				mediaPlayer.start();
				isplaying = true;
				isrelease = false;
			}
			break;
		case R.id.button2:
			if (mediaPlayer != null) {
				if (!isrelease) {
					if (!ispause) {
						mediaPlayer.pause();
						ispause = true;
						isplaying = true;
					} else {
						mediaPlayer.start();
						ispause = false;
					}
				}
			}

			break;
		case R.id.button3:
			if (mediaPlayer != null) {
				if (isplaying) {
					if (!isrelease) {
						mediaPlayer.stop();
						mediaPlayer.release();
						isrelease = true;

					} else {
						isplaying = false;
					}
				}
			}
			break;
		}
	}

	/**
	 * 获取MP3文件path
	 * 
	 * @param mp3Info
	 * @return
	 */
	private String getMp3Path(Mp3Info mp3Info) {
		String SDCard = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		String path = SDCard + File.separator + "mp3" + File.separator
				+ mp3Info.getMp3Name();
		return path;
	}
}
