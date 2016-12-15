package com.anjoyo.musicplayer.bean;

import com.anjoyo.musicplayer.define.Constant;

public class MusicState {

	private int currentMusicId;
	private int playMode;
	private boolean showDesktopLyric;
	/** 
	 * 0我的音乐 or 1我的最爱 or 2文件夹
	 */
	private int whatMusicFolder;

	public MusicState(int currentMusicId, int playMode,
			boolean showDesktopLyric, int whatMusicFolder) {
		super();
		this.currentMusicId = currentMusicId;
		this.playMode = playMode;
		this.showDesktopLyric = showDesktopLyric;
		this.whatMusicFolder = whatMusicFolder;
	}

	public int getCurrentMusicId() {
		return currentMusicId;
	}

	public void setCurrentMusicId(int currentMusicId) {
		this.currentMusicId = currentMusicId;
	}

	public int getPlayMode() {
		return playMode;
	}

	public void setPlayMode(int playMode) {
		this.playMode = playMode % 4;
	}

	public boolean isShowDesktopLyric() {
		return showDesktopLyric;
	}

	public void setShowDesktopLyric(boolean showDesktopLyric) {
		this.showDesktopLyric = showDesktopLyric;
	}

	public int getWhatMusicFolder() {
		return whatMusicFolder;
	}

	public void setWhatMusicFolder(int whatMusicFolder) {
		this.whatMusicFolder = whatMusicFolder;
	}

	public void setWhatMusicFolder(String folderName) {
		if (Constant.MUSIC_PAGER_GV_LOCAL.equals(folderName)) {
			this.whatMusicFolder =  0;
		} else if (Constant.MUSIC_PAGER_GV_FAV.equals(folderName)) {
			this.whatMusicFolder = 1;
		} else {
			this.whatMusicFolder = 2;
		}
	}
}
