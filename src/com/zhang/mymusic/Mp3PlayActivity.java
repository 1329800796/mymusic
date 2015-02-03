package com.zhang.mymusic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Queue;
import com.zhang.mymusic.domain.Mp3Info;
import com.zhang.mymusic.lrc.LrcProcessor;
import com.zhang.mymusic.service.PlayService;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Mp3PlayActivity extends Activity {
	Button beginButton = null;
	Button pauseButton = null;
	Button stopButton = null;
	MediaPlayer mediaPlayer = null;

	private ArrayList<Queue> queues = null;
	private TextView lrcTextView = null;
	private Mp3Info mp3Info = null;
	private Handler handler = new Handler();
	private UpdateTimeCallback updateTimeCallback = null;
	private long begin = 0;
	private long nextTimeMill = 0;
	private long currentTimeMill = 0;
	private String message = null;
	private long pauseTimeMills = 0;
	private boolean isPlaying = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playermp3);
		Intent intent = getIntent();
		mp3Info = (Mp3Info) intent.getSerializableExtra("mp3Info");
		beginButton = (Button) findViewById(R.id.begin);
		pauseButton = (Button) findViewById(R.id.pause);
		stopButton = (Button) findViewById(R.id.stop);
		beginButton.setOnClickListener(new BeginButtonListener());
		pauseButton.setOnClickListener(new PauseButtonListener());
		stopButton.setOnClickListener(new StopButtonListener());
		lrcTextView = (TextView)findViewById(R.id.lrcText);
	}

	/**
	 * 根据歌词文件的名字，来读取歌词文件当中的信息
	 * @param lrcName
	 */
	private void prepareLrc(String lrcName){
		try {
			InputStream inputStream = new FileInputStream(Environment.getExternalStorageDirectory().getAbsoluteFile() +File.separator + "mp3/" + mp3Info.getLrcName());
			LrcProcessor lrcProcessor = new LrcProcessor();
			queues = lrcProcessor.process(inputStream);
			//创建一个UpdateTimeCallback对象
			updateTimeCallback = new UpdateTimeCallback(queues);
			begin = 0 ;
			currentTimeMill = 0 ;
			nextTimeMill = 0 ;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	class BeginButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			//创建一个Intent对象，用于同时Service开始播放MP3
			Intent intent = new Intent();
			intent.setClass(Mp3PlayActivity.this, PlayService.class);
			intent.putExtra("mp3Info", mp3Info);
			intent.putExtra("MSG", AppConstant.PlayMsg.PLAY_MSG);
			//读取LRC文件
			prepareLrc(mp3Info.getLrcName());
			//启动Service
			startService(intent);
			//将begin的值置为当前毫秒数
			begin = System.currentTimeMillis();
			//执行updateTimeCallback
			handler.postDelayed(updateTimeCallback, 5);
			isPlaying = true;
		}

	}

	class PauseButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			//通知Service暂停播放MP3
			Intent intent = new Intent();
			intent.setClass(Mp3PlayActivity.this, PlayService.class);
			intent.putExtra("MSG", AppConstant.PlayMsg.PASU_MSG);
			startService(intent);
			//
			if(isPlaying){
				handler.removeCallbacks(updateTimeCallback);
				pauseTimeMills = System.currentTimeMillis();
			}
			else{
				handler.postDelayed(updateTimeCallback, 5);
				begin = System.currentTimeMillis() - pauseTimeMills + begin;
			}
			isPlaying = isPlaying ? false : true;
		}

	}

	class StopButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			//通知Service停止播放MP3文件
			Intent intent = new Intent();
			intent.setClass(Mp3PlayActivity.this, PlayService.class);
			intent.putExtra("MSG", AppConstant.PlayMsg.STOP_MSG);
			startService(intent);
			//从Handler当中移除updateTimeCallback
			handler.removeCallbacks(updateTimeCallback);
		}

	}
	
	class UpdateTimeCallback implements Runnable{
		Queue times = null;
		Queue messages = null;
		public UpdateTimeCallback(ArrayList<Queue> queues) {
			//从ArrayList当中取出相应的对象对象
			times = queues.get(0);
			messages = queues.get(1);
		}
		@Override
		public void run() {
			//计算偏移量，也就是说从开始播放MP3到现在为止，共消耗了多少时间，以毫秒为单位
			long offset = System.currentTimeMillis() - begin;
			if(currentTimeMill == 0){
				nextTimeMill = (Long)times.poll();
				message = (String)messages.poll();
			}
			if(offset >= nextTimeMill){
				lrcTextView.setText(message);
				message = (String)messages.poll();
				nextTimeMill = (Long)times.poll();
			}
			currentTimeMill = currentTimeMill + 10;
			handler.postDelayed(updateTimeCallback, 10);
		}
		
	}
}
