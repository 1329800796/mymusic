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
	 * ���ݸ���ļ������֣�����ȡ����ļ����е���Ϣ
	 * @param lrcName
	 */
	private void prepareLrc(String lrcName){
		try {
			InputStream inputStream = new FileInputStream(Environment.getExternalStorageDirectory().getAbsoluteFile() +File.separator + "mp3/" + mp3Info.getLrcName());
			LrcProcessor lrcProcessor = new LrcProcessor();
			queues = lrcProcessor.process(inputStream);
			//����һ��UpdateTimeCallback����
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
			//����һ��Intent��������ͬʱService��ʼ����MP3
			Intent intent = new Intent();
			intent.setClass(Mp3PlayActivity.this, PlayService.class);
			intent.putExtra("mp3Info", mp3Info);
			intent.putExtra("MSG", AppConstant.PlayMsg.PLAY_MSG);
			//��ȡLRC�ļ�
			prepareLrc(mp3Info.getLrcName());
			//����Service
			startService(intent);
			//��begin��ֵ��Ϊ��ǰ������
			begin = System.currentTimeMillis();
			//ִ��updateTimeCallback
			handler.postDelayed(updateTimeCallback, 5);
			isPlaying = true;
		}

	}

	class PauseButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			//֪ͨService��ͣ����MP3
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
			//֪ͨServiceֹͣ����MP3�ļ�
			Intent intent = new Intent();
			intent.setClass(Mp3PlayActivity.this, PlayService.class);
			intent.putExtra("MSG", AppConstant.PlayMsg.STOP_MSG);
			startService(intent);
			//��Handler�����Ƴ�updateTimeCallback
			handler.removeCallbacks(updateTimeCallback);
		}

	}
	
	class UpdateTimeCallback implements Runnable{
		Queue times = null;
		Queue messages = null;
		public UpdateTimeCallback(ArrayList<Queue> queues) {
			//��ArrayList����ȡ����Ӧ�Ķ������
			times = queues.get(0);
			messages = queues.get(1);
		}
		@Override
		public void run() {
			//����ƫ������Ҳ����˵�ӿ�ʼ����MP3������Ϊֹ���������˶���ʱ�䣬�Ժ���Ϊ��λ
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