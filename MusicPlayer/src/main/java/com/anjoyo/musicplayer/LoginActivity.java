package com.anjoyo.musicplayer;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.anjoyo.musicplayer.application.MusicApplication;
import com.anjoyo.musicplayer.bean.MusicBean;
import com.anjoyo.musicplayer.define.Constant;
import com.anjoyo.musicplayer.service.MusicPlayService;
import com.anjoyo.musicplayer.util.MusicManagerUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.Window;

public class LoginActivity extends Activity {

	private MusicApplication application;
	
	private Handler mMainHandler;
	private MusicApplication mApplication;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		mApplication = (MusicApplication) getApplication();
		mApplication.setProgramExit(false);
		mApplication.setSaveState(false);
		mMainHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case Constant.MAIN_WHAT_GET_DATA_SUCCESS:
					Intent service = new Intent(mApplication, MusicPlayService.class);
					ServiceConnection conn = mApplication.getServiceConnection();
					mApplication.bindService(service, conn, BIND_AUTO_CREATE);
					new IsStartActivityThread().start();
					break;
				case Constant.MAIN_WHAT_START_ACTIVITY:
					startActivity(new Intent(LoginActivity.this, MusicActivity.class));
					finish();
					break;
				default:
					break;
				}
			}
		};
		
		File artistFile = new File(Constant.PATH_ARTIST_IMAGE);
		if (!artistFile.exists()) {
			artistFile.mkdirs();
		}
		File lyricFile = new File(Constant.PATH_MUSIC_LYRIC);
		if (!lyricFile.exists()) {
			lyricFile.mkdirs();
		}
		
		application = (MusicApplication)getApplication();
		new InitDataThread().start();
		
	}
	
	/** 
	 * 初始化音乐列表
	 * @author HLP
	 */
	private class InitDataThread extends Thread {
		@Override
		public void run() {
			List<MusicBean> musicList = MusicManagerUtil.getMusicList(LoginActivity.this);
			application.setMusicList(musicList);
			Map<String, List<MusicBean>> musicMap = MusicManagerUtil.getMusicMap(musicList);
			application.setMusicInfoMap(musicMap);
			mMainHandler.sendEmptyMessage(Constant.MAIN_WHAT_GET_DATA_SUCCESS);
		}
	}
	
	private class IsStartActivityThread extends Thread {
		@Override
		public void run() {
			while (true) {
				if (application.getMusicList() != null && application.getMusicInfoMap() != null
						&& application.getMusicPlayService() != null) {
					mMainHandler.sendEmptyMessage(Constant.MAIN_WHAT_START_ACTIVITY);
					break;
				}
				try {
					sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
