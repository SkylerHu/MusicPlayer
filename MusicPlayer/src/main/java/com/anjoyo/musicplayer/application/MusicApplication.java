package com.anjoyo.musicplayer.application;

import java.util.List;
import java.util.Map;

import com.anjoyo.musicplayer.bean.MusicBean;
import com.anjoyo.musicplayer.service.MusicPlayService;
import com.anjoyo.musicplayer.service.MusicPlayService.MusicBinder;

import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class MusicApplication extends Application {
	
	/** 程序退出标志 */
	private boolean programExit = false;
	/** 程序是否保存设置标志 */
	private boolean saveState = false;
	/** 所有歌曲 */
	private List<MusicBean> musicList;
	/** 歌曲所在文件夹集合，文件夹对应之下的歌曲List */
	private Map<String, List<MusicBean>> musicInfoMap;
	/** Service对象，用于之间的通信 */
	private MusicPlayService musicPlayService;
	
	private ServiceConnection serviceConnection;
	
//	/** 有通话前的播放状态 */
//	private boolean callFlag;
	
	@Override
	public void onCreate() {
		super.onCreate();
		serviceConnection = new ServiceConnection() {
			@Override
			public void onServiceDisconnected(ComponentName name) {}
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				musicPlayService = ((MusicBinder)service).getService();
			}
		};
		
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.PHONE_STATE");
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				TelephonyManager telephonyManager 
					= (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
				telephonyManager.listen(new PhoneStateListener(){
					@Override
					public void onCallStateChanged(int state, String incomingNumber) {
						super.onCallStateChanged(state, incomingNumber);
						if (musicPlayService != null && musicPlayService.getMediaPlayer() != null
								&& musicPlayService.getCurrentMusicBean().getPlayState() == 1) {
							if (state != TelephonyManager.CALL_STATE_IDLE) {
								musicPlayService.pause();
							} else {
								musicPlayService.play();
							}
						}
					}
				}, PhoneStateListener.LISTEN_CALL_STATE);
			}
		}, filter);
	}
	
	
	
	public boolean isProgramExit() {
		return programExit;
	}

	public void setProgramExit(boolean programExit) {
		this.programExit = programExit;
	}

	public boolean isSaveState() {
		return saveState;
	}

	public void setSaveState(boolean saveState) {
		this.saveState = saveState;
	}
	
	public List<MusicBean> getMusicList() {
		return musicList;
	}

	public void setMusicList(List<MusicBean> musicList) {
		this.musicList = musicList;
	}

	public Map<String, List<MusicBean>> getMusicInfoMap() {
		return musicInfoMap;
	}

	public void setMusicInfoMap(Map<String, List<MusicBean>> musicInfoMap) {
		this.musicInfoMap = musicInfoMap;
	}

	public MusicPlayService getMusicPlayService() {
		return musicPlayService;
	}

	public void setMusicPlayService(MusicPlayService musicPlayService) {
		this.musicPlayService = musicPlayService;
	}

	public ServiceConnection getServiceConnection() {
		return serviceConnection;
	}

	public void setServiceConnection(ServiceConnection serviceConnection) {
		this.serviceConnection = serviceConnection;
	}

}
