package com.anjoyo.musicplayer.bean;

import java.util.List;

/**
 * 一句歌词
 * 
 * @author HLP
 */
public class LyricBean {

	/** 一句歌词 */
	private String lyric;
	/** 本句歌词开始的时间 */
	private int lyricTime;
	/** 本句歌词每个字的持续时间 ，构成数组 */
	private List<Integer> wordsTime;

	public LyricBean(String lyric, int lyricTime, List<Integer> wordsTime) {
		super();
		this.lyric = lyric;
		this.lyricTime = lyricTime;
		this.wordsTime = wordsTime;
	}

	public String getLyric() {
		return lyric;
	}

	public void setLyric(String lyric) {
		this.lyric = lyric;
	}

	public int getLyricTime() {
		return lyricTime;
	}

	public void setLyricTime(int lyricTime) {
		this.lyricTime = lyricTime;
	}

	public List<Integer> getWordsTime() {
		return wordsTime;
	}

	public void setWordsTime(List<Integer> wordsTime) {
		this.wordsTime = wordsTime;
	}

}
