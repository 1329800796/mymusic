package com.zhang.mymusic.service;

import java.io.File;

import com.zhang.mymusic.AppConstant;
import com.zhang.mymusic.domain.Mp3Info;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;

public class PlayService extends Service {

	private MediaPlayer mediaPlayer = null;
	private boolean isplaying = false;
	private boolean ispause = false;
	private boolean isrelease = false;// 释放的状态，停止

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Mp3Info mp3Info = (Mp3Info) intent.getSerializableExtra("mp3Info");
		
		int MSG = intent.getIntExtra("MSG", 0);
		if (mp3Info != null) {
			if (MSG == AppConstant.PlayMsg.PLAY_MSG) {
				
				play(mp3Info);
				
			}
		} else {
			if (MSG == AppConstant.PlayMsg.PASU_MSG) {
				pause();
			} else if (MSG == AppConstant.PlayMsg.STOP_MSG) {
				stop();
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private void play(Mp3Info mp3Info) {
		String path = getMp3Path(mp3Info);
		mediaPlayer = MediaPlayer.create(this, Uri.parse("file://" + path));
		mediaPlayer.setLooping(false);
		mediaPlayer.start();
		isplaying = true;
		isrelease = false;
	}

	private void pause() {
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
	}
	
	private void stop(){
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
