package com.zhang.mymusic.service;

import com.zhang.mymusic.AppConstant;
import com.zhang.mymusic.HomeActivity;
import com.zhang.mymusic.R;
import com.zhang.mymusic.domain.Mp3Info;
import com.zhang.mymusic.downloder.HttpDownloder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DownloadService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		// 获取对象
		Mp3Info mp3 = (Mp3Info) intent.getSerializableExtra("mp3Info");

		// 下载线程
		DownloadThead downloadThead = new DownloadThead(mp3);
		Thread thread = new Thread(downloadThead);
		thread.start();

		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 
	 * @author Administrator
	 * 
	 */
	class DownloadThead implements Runnable {
		private Mp3Info mp3 = null;

		public DownloadThead(Mp3Info mp3) {
			this.mp3 = mp3;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// 下载地址http://192.168.1.100:8080/mp3/a1.mp3
			String mp3url = AppConstant.URL.BASE_URL + mp3.getMp3Name();
			String lrcurl = AppConstant.URL.BASE_URL + mp3.getLrcName();
			HttpDownloder downloder = new HttpDownloder();

			// url + sd 卡下载位置+MP3 名字
			int result = downloder.downFile(mp3url, "mp3/", mp3.getMp3Name());
			int lrcresult = downloder
					.downFile(lrcurl, "mp3/", mp3.getLrcName());

			String resultmessage = null;
			if (result == -1) {
				resultmessage = "下载失败";
			} else if (result == 0) {
				resultmessage = "文件下载成功";
			} else if (result == 1) {
				resultmessage = "文件已经存在";
			}
			// 使用Notification 提示客户下载结果
			CreateInform(resultmessage);

		}

	}

	public void CreateInform(String resultmessage) {
		// 定义一个PendingIntent，当用户点击通知时，跳转到某个Activity(也可以发送广播等)
		Intent intent = new Intent(this, HomeActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, 0);

		// 创建一个通知
		Notification notification = new Notification(R.drawable.ic_launcher,
				resultmessage, System.currentTimeMillis());
		notification
				.setLatestEventInfo(this, "点击查看", "点击查看详细内容", pendingIntent);

		// 用NotificationManager的notify方法通知用户生成标题栏消息通知
		NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nManager.notify(100, notification);// id是应用中通知的唯一标识
		// 如果拥有相同id的通知已经被提交而且没有被移除，该方法会用更新的信息来替换之前的通知。
	}

}
