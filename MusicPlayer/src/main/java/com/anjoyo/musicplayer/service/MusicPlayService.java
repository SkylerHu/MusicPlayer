package com.anjoyo.musicplayer.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.anjoyo.musicplayer.R;
import com.anjoyo.musicplayer.application.MusicApplication;
import com.anjoyo.musicplayer.bean.LyricBean;
import com.anjoyo.musicplayer.bean.MusicBean;
import com.anjoyo.musicplayer.bean.MusicState;
import com.anjoyo.musicplayer.define.Constant;
import com.anjoyo.musicplayer.util.MusicManagerUtil;
import com.anjoyo.musicplayer.util.PlayMusicUtil;
import com.anjoyo.musicplayer.util.ResourceUtil;
import com.anjoyo.musicplayer.util.SharePreferenceUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MusicPlayService extends Service {

	/** 所有歌曲 */
	private List<MusicBean> musicList;
	/** 歌曲所在文件夹集合，文件夹对应之下的歌曲List */
	private Map<String, List<MusicBean>> musicInfoMap;
	/** 音乐信息提取的文件夹List */
	private List<String> folderList;
	/** 当前文件夹在folderList中的index */
	private int currentFolderIndex;
	/** 正在播放的歌曲所在文件夹 */
	private String currentPlayFolder;
	/** 当前播放列表 */
	private List<MusicBean> currentMusicList;
	/** 播放的歌曲在当前文件夹List中的index */
	private int currentMusicIndex;
	/** 正在播放的歌曲 */
	private MusicBean currentMusicBean;
	/** 当前播放歌手的图片 */
	private Bitmap currentBitmap;
	/** 当前播放歌曲的歌词 */
	private List<LyricBean> currentLyricList;
	/** 当前歌曲播放暂停记录的进度 */
	private int currentPausePosition;
	
	private List<MusicBean> favoriteMusicList;
	/** 播放器设置信息，仅在程序开始时读取/退出时更改SharePreference中的value */
	private MusicState musicState;
	/** 最爱音乐列表 */
	private NotificationManager notificationManager;
	
	private MusicApplication mApplication;
	private MusicBinder mMusicBinder;
	private MediaPlayer mMediaPlayer;
	
	public class MusicBinder extends Binder {
		/**
		 * @return 获得MusicPlayService服务对象，以便调用此类中的方法
		 */
		public MusicPlayService getService() {
			return MusicPlayService.this;
		}
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mMusicBinder = new MusicBinder();
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				PlayMusicUtil.changeMusic(MusicPlayService.this, 
						MusicPlayService.this, Constant.MUSIC_OPERATE_NEXT);
			}
		});
		mApplication = (MusicApplication) getApplication();
		notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		initData();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return mMusicBinder;
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		notificationManager.cancel(Constant.NOTIFICATION_ID);//取消通知
		if (mMediaPlayer.isPlaying()) {
			mMediaPlayer.stop();
		}
		mMediaPlayer.release();
		mMediaPlayer = null;
		return super.onUnbind(intent);
	}

	/** 
	 * 程序刚启动必须初始化的数据
	 */
	private void initData() {
		musicList = mApplication.getMusicList();
		musicInfoMap = mApplication.getMusicInfoMap();
		
		favoriteMusicList = MusicManagerUtil.getFavMusicList(musicList);
		
		musicState = SharePreferenceUtil.getMusicState(mApplication);
//		musicState.setCurrentMusicId(0);
		//设置当前播放音乐的标志
		for (MusicBean musicBean : musicList) {
			if (musicBean.getId() == musicState.getCurrentMusicId()) {
				musicBean.setPlayState(2);
			} else {
				musicBean.setPlayState(0);
			}
		}
		
		folderList = new ArrayList<String>(musicInfoMap.keySet());
		
		if (musicState.getCurrentMusicId() != 0 && musicState.getWhatMusicFolder() == 0) {
			currentPlayFolder = Constant.MUSIC_PAGER_GV_LOCAL;
			currentMusicList = musicList;
			for (int i = 0; i < musicList.size(); i++) {
				MusicBean musicBean = musicList.get(i);
				if (musicBean.getId() == musicState.getCurrentMusicId()) {
					currentMusicIndex = i;
					currentMusicBean = musicBean;
					break;
				}
			}
		} else if (musicState.getCurrentMusicId() != 0 && musicState.getWhatMusicFolder() == 1) {
			currentPlayFolder = Constant.MUSIC_PAGER_GV_FAV;
			currentMusicList = favoriteMusicList;
			for (int i = 0; i < favoriteMusicList.size(); i++) {
				MusicBean musicBean = favoriteMusicList.get(i);
				if (musicBean.getId() == musicState.getCurrentMusicId()) {
					currentMusicIndex = i;
					currentMusicBean = musicBean;
					break;
				}
			}
		} else if(musicState.getCurrentMusicId() != 0) {
			for (int j = 0; j < folderList.size(); j++) {
				String path = folderList.get(j);
				if (musicState.getCurrentMusicId() != 0) {
					List<MusicBean> musicBeans = musicInfoMap.get(path);
					boolean flag = false;
					for (int i = 0; i < musicBeans.size(); i++) {
						MusicBean musicBean = musicBeans.get(i);
						if (musicBean.getId() == musicState.getCurrentMusicId()) {
							currentPlayFolder = path;
							currentMusicIndex = i;
							currentMusicBean = musicBean;
							currentFolderIndex = j;
							flag = true;
							break;
						}
					}
					if (flag) {
						currentMusicList = musicBeans;
						break;
					}
				}
			}
		}
		if (currentMusicBean == null) {
			if (folderList.size() > 1 && currentMusicList.size() > 1) {
				currentPlayFolder = folderList.get(0);
				currentFolderIndex = 0;
				currentMusicList = musicInfoMap.get(currentPlayFolder);
				currentMusicBean = currentMusicList.get(0);
				currentMusicIndex = 0;

				musicState.setWhatMusicFolder(2);
			}
		}

		if (currentMusicBean != null) {
			musicState.setCurrentMusicId(currentMusicBean.getId());

			currentBitmap = ResourceUtil.getImage(currentMusicBean.getArtist());
			currentLyricList = ResourceUtil.getLyric(currentMusicBean);
		}
	}

	/** 
	 * 播放音乐
	 */
	public void play() {
		mMediaPlayer.reset();

		if (currentMusicBean == null) {
			Toast.makeText(this, R.string.no_music, Toast.LENGTH_LONG);
			return;
		}
		
		try {
			mMediaPlayer.setDataSource(currentMusicBean.getPath());
			mMediaPlayer.prepare();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (currentPausePosition > 0) {
			mMediaPlayer.seekTo(currentPausePosition);
		}
		mMediaPlayer.start();
		
		currentPausePosition = 0;
	}
	
	/** 
	 * 暂停播放
	 */
	public void pause() {
		currentPausePosition = mMediaPlayer.getCurrentPosition();
		mMediaPlayer.pause();
	}
	
	public MediaPlayer getMediaPlayer() {
		return mMediaPlayer;
	}

	/** 
	 * @return 判断播放器的是否正在播放并返回结果
	 */
	public boolean isMediaPlaying() {
		return mMediaPlayer.isPlaying();
	}
	/** 
	 * @return 返回歌曲播放的当前进度
	 */
	public int getCurrentPosition() {
		return mMediaPlayer.getCurrentPosition();
	}
	
	/** 
	 * @return 返回歌曲时长
	 */
	public int getCurrentMusicDuration() {
		return mMediaPlayer.getDuration();
	}
	/**
	 * 
	 * @param isLyricBg 是否为歌词LyricActivity的背景
	 * @return
	 */
	public Bitmap getCurrentBitmap(boolean isLyricBg) {
		if (isLyricBg) {
			if (currentBitmap == null) {
				return BitmapFactory.decodeResource(getResources(), R.drawable.lyric_background);
			}
		} else {
			if (currentBitmap == null) {
				return BitmapFactory.decodeResource(getResources(), R.drawable.icon_artist_default);
			}
		}
		return currentBitmap;
	}

	public void setCurrentBitmap(Bitmap currentBitmap) {
		this.currentBitmap = currentBitmap;
	}


	public Map<String, List<MusicBean>> getMusicInfoMap() {
		return musicInfoMap;
	}

	public void setMusicInfoMap(Map<String, List<MusicBean>> musicInfoMap) {
		this.musicInfoMap = musicInfoMap;
	}

	public MusicBean getCurrentMusicBean() {
		return currentMusicBean;
	}

	public void setCurrentMusicBean(MusicBean currentMusicBean) {
		this.currentMusicBean = currentMusicBean;
	}

	public int getCurrentPausePosition() {
		return currentPausePosition;
	}

	public void setCurrentPausePosition(int currentPosition) {
		this.currentPausePosition = currentPosition;
	}

	public String getCurrentPlayFolder() {
		return currentPlayFolder;
	}

	public void setCurrentPlayFolder(String currentPlayFolder) {
		this.currentPlayFolder = currentPlayFolder;
	}

	public List<MusicBean> getCurrentMusicList() {
		return currentMusicList;
	}

	public void setCurrentMusicList(List<MusicBean> currentMusicList) {
		this.currentMusicList = currentMusicList;
	}

	public int getCurrentIndex() {
		return currentMusicIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentMusicIndex = currentIndex;
	}

	public MusicState getMusicState() {
		return musicState;
	}

	public void setMusicState(MusicState musicState) {
		this.musicState = musicState;
	}

	public MusicApplication getmApplication() {
		return mApplication;
	}

	public void setmApplication(MusicApplication mApplication) {
		this.mApplication = mApplication;
	}

	public NotificationManager getNotificationManager() {
		return notificationManager;
	}

	public void setNotificationManager(NotificationManager notificationManager) {
		this.notificationManager = notificationManager;
	}

	public MusicBinder getmMusicBinder() {
		return mMusicBinder;
	}

	public void setmMusicBinder(MusicBinder mMusicBinder) {
		this.mMusicBinder = mMusicBinder;
	}

	public MediaPlayer getmMediaPlayer() {
		return mMediaPlayer;
	}

	public void setmMediaPlayer(MediaPlayer mMediaPlayer) {
		this.mMediaPlayer = mMediaPlayer;
	}

	public List<String> getFloderList() {
		return folderList;
	}

	public void setFloderList(List<String> floderList) {
		this.folderList = floderList;
	}

	public List<MusicBean> getMusicList() {
		return musicList;
	}

	public void setMusicList(List<MusicBean> musicList) {
		this.musicList = musicList;
	}

	public List<String> getFolderList() {
		return folderList;
	}

	public void setFolderList(List<String> folderList) {
		this.folderList = folderList;
	}

	public int getCurrentFolderIndex() {
		return currentFolderIndex;
	}

	public void setCurrentFolderIndex(int currentFolderIndex) {
		this.currentFolderIndex = currentFolderIndex;
	}

	public int getCurrentMusicIndex() {
		return currentMusicIndex;
	}

	public void setCurrentMusicIndex(int currentMusicIndex) {
		this.currentMusicIndex = currentMusicIndex;
	}

	public List<MusicBean> getFavoriteMusicList() {
		return favoriteMusicList;
	}

	public void setFavoriteMusicList(List<MusicBean> favoriteMusicList) {
		this.favoriteMusicList = favoriteMusicList;
	}

	public List<LyricBean> getCurrentLyricList() {
		return currentLyricList;
	}

	public void setCurrentLyricList(List<LyricBean> currentLyricList) {
		this.currentLyricList = currentLyricList;
	}

	
	
}
