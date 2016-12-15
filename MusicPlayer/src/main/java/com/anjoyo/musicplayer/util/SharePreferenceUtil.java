package com.anjoyo.musicplayer.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.anjoyo.musicplayer.bean.MusicState;
import com.anjoyo.musicplayer.define.Constant;

public class SharePreferenceUtil {

	/** 
	 * 保存播放器的设置信息
	 * @param musicState
	 */
	public static void saveMusicState(Context context, MusicState musicState) {
		SharedPreferences preferences = context.getSharedPreferences(Constant.SHARED_FILE_NAME, 
				Context.MODE_WORLD_WRITEABLE);
		Editor editor = preferences.edit();
		editor.putInt(Constant.SHARED_CURRENT_MUSIC_ID, musicState.getCurrentMusicId());
		editor.putInt(Constant.SHARED_MUSIC_PLAY_MODE, musicState.getPlayMode());
		editor.putBoolean(Constant.SHARED_SHOW_DESKTOP_LYRIC, musicState.isShowDesktopLyric());
		editor.putInt(Constant.SHARED_IS_ALL_MUSIC_FOLDER, musicState.getWhatMusicFolder());
		editor.commit();
	}
	
	/** 
	 * @return 读取播放器的设置信息并返回
	 */
	public static MusicState getMusicState(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(Constant.SHARED_FILE_NAME, 
				Context.MODE_WORLD_READABLE);
		int currentMusicId = preferences.getInt(Constant.SHARED_CURRENT_MUSIC_ID, 0);
		int playMode = preferences.getInt(Constant.SHARED_MUSIC_PLAY_MODE, Constant.PLAY_MODE_INT_ORDER);
		boolean showDesktopLyric = preferences.getBoolean(Constant.SHARED_SHOW_DESKTOP_LYRIC, false);
		int whatMusicFolder = preferences.getInt(Constant.SHARED_IS_ALL_MUSIC_FOLDER, 2);
		MusicState musicState = new MusicState(currentMusicId, playMode, showDesktopLyric, whatMusicFolder);
		return musicState;
	}
	
}
