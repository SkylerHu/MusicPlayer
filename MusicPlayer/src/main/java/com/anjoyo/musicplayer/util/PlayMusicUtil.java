package com.anjoyo.musicplayer.util;

import java.util.List;
import java.util.Random;

import android.content.Context;

import com.anjoyo.musicplayer.bean.MusicBean;
import com.anjoyo.musicplayer.define.Constant;
import com.anjoyo.musicplayer.service.MusicPlayService;

public class PlayMusicUtil {

	/**
	 * 切换歌曲
	 * @param context
	 * @param musicPlayService
	 * @param tag 标志是切上/下一首，见{@link Constant}，
	 * 取值可{@link MUSIC_OPERATE_PRE} or {@link MUSIC_OPERATE_NEXT}
	 */
	public static void changeMusic(Context context, MusicPlayService musicPlayService, int tag) {
		List<MusicBean> musicList = musicPlayService.getCurrentMusicList();
		musicPlayService.setCurrentPausePosition(0);
		int position = musicPlayService.getCurrentIndex();
		int oldState = musicList.get(position).getPlayState();
		switch (musicPlayService.getMusicState().getPlayMode()) {
		case Constant.PLAY_MODE_INT_ORDER:
			position += tag;
			if (position < 0) {
				position = musicList.size() - 1;
				updateCurrentInfo(context, musicPlayService, musicList, position, oldState);
				musicPlayService.play();
				musicPlayService.pause();
				musicList.get(position).setPlayState(2);
			} else if (position == musicList.size()) {
				position = 0;
				updateCurrentInfo(context, musicPlayService, musicList, position, oldState);
				musicPlayService.play();
				musicPlayService.pause();
				musicList.get(position).setPlayState(2);
			} else {
				updateCurrentInfo(context, musicPlayService, musicList, position, oldState);
				musicPlayService.play();
			}
			break;
		case Constant.PLAY_MODE_INT_REPEAT:
//			musicPlayService.getmMediaPlayer().setLooping(true);
			musicPlayService.play();
			break;
		case Constant.PLAY_MODE_INT_LOOP:
			position += tag;
			if (position < 0) {
				position = musicList.size() - 1;
			} else if (position == musicList.size()) {
				position = 0;
			}
			updateCurrentInfo(context, musicPlayService, musicList, position, oldState);
			musicPlayService.play();
			break;
		case Constant.PLAY_MODE_INT_RANDOM:
			int index = position;
			Random random = new Random();
			do {
				position = random.nextInt(musicList.size());
			} while (index != position);
			updateCurrentInfo(context, musicPlayService, musicList, position, oldState);
			musicPlayService.play();
			break;
		default:
			break;
		}
		if (!musicPlayService.isMediaPlaying()) {
			musicPlayService.pause();
			musicList.get(position).setPlayState(2);
		}

		if (! (musicPlayService.getCurrentMusicBean().getPlayState() == 1)) {
			musicPlayService.pause();
			musicList.get(position).setPlayState(2);
		}
	}
	
//	/**
//	 * 在别的文件夹下操作 ，将播放的文件夹转变成当前文件夹，并改变播放列表记录的当前index
//	 * @param musicPlayService
//	 * @param musicBeans
//	 * @param folderName
//	 */
//	public static void onFolderChange(Context context, MusicPlayService musicPlayService, 
//			List<MusicBean> musicBeans, String folderName) {
//		if (! musicPlayService.getCurrentPlayFolder().equals(folderName)) {
//			musicPlayService.setCurrentPlayFolder(folderName);
//			musicPlayService.setCurrentMusicList(musicBeans);
//			musicPlayService.getMusicState().setWhatMusicFolder(folderName);
//			boolean flag = true;
//			for (int i = 0; i < musicBeans.size(); i++) {
//				if (musicPlayService.getCurrentMusicBean().getId() == musicBeans.get(i).getId()) {
//					musicPlayService.setCurrentMusicIndex(i);
//					flag = false;
//					break;
//				}
//			}
//			if (flag) {
//				musicPlayService.setCurrentMusicBean(musicBeans.get(0));
//				updateCurrentInfo(context, musicPlayService, musicBeans, 0);
//			}
//		}
//	}

	/** 
	 * 换曲，更新当前播放记录信息
	 * @param context
	 * @param musicPlayService
	 * @param musicList
	 * @param position
	 */
	public static void updateCurrentInfo(Context context, MusicPlayService musicPlayService, 
			List<MusicBean> musicList, int position, int oldState) {
		musicPlayService.setCurrentMusicIndex(position);
		musicPlayService.setCurrentMusicBean(musicList.get(position));
		musicPlayService.setCurrentBitmap(ResourceUtil.getImage(musicList.get(position).getArtist()));
		musicPlayService.setCurrentLyricList(ResourceUtil.getLyric(musicList.get(position)));
		NotificationUtil.showNotification(context, musicPlayService);
		musicPlayService.getMusicState().setCurrentMusicId(musicList.get(position).getId());
		
		updatePlayState(musicPlayService, musicList.get(position).getId(), oldState);
		
	}
	
	/** 
	 * 设置当前播放音乐的标志
	 * @param musicPlayService
	 * @param musicId
	 * @param isPlaying
	 */
	public static void updatePlayState(MusicPlayService musicPlayService, int musicId, int playState) {
		for (MusicBean musicBean : musicPlayService.getCurrentMusicList()) {
			if (musicBean.getId() == musicId) {
				musicBean.setPlayState(playState);
			} else {
				musicBean.setPlayState(0);
			}
		}
	}
	
	/** 
	 * @param playMode 播放模式
	 * @return 将int类型表示的模式转换成字符串并返回
	 */
	public static String getMusicPlayModeString(int playMode) {
		switch (playMode) {
		case Constant.PLAY_MODE_INT_ORDER:
			return Constant.PLAY_MODE_ORDER;
		case Constant.PLAY_MODE_INT_REPEAT:
			return Constant.PLAY_MODE_REPEAT;
		case Constant.PLAY_MODE_INT_LOOP:
			return Constant.PLAY_MODE_LOOP;
		case Constant.PLAY_MODE_INT_RANDOM:
			return Constant.PLAY_MODE_RANDOM;
		default:
			break;
		}
		return "";
	}
}
