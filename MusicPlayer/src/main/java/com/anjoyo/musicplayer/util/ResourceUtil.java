package com.anjoyo.musicplayer.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.anjoyo.musicplayer.bean.LyricBean;
import com.anjoyo.musicplayer.bean.MusicBean;
import com.anjoyo.musicplayer.define.Constant;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ResourceUtil {

	/** 歌词文件中前4句和后四句都比较特殊 */
	private static final int NUM = 4;
	
	/** 
	 * @param context
	 * @return	获得图片资源
	 */
	public static Bitmap getImage(String artistName) {
		Bitmap bitmap = null;
		File path = new File(Constant.PATH_ARTIST_IMAGE + File.separator + artistName);
		if (path.exists() && path.isFile()) {
			InputStream is = null;
			try {
				is = new FileInputStream(path);
				bitmap = BitmapFactory.decodeStream(is);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} 
		return bitmap;
	}
	
	/** 
	 * 获得歌词并封装成对象
	 * @param displayName
	 * @return
	 */
	public static List<LyricBean> getLyric(MusicBean musicBean) {
		List<LyricBean> lyricBeans = new ArrayList<LyricBean>();
		File file = new File(Constant.PATH_MUSIC_LYRIC + File.separator 
				+ musicBean.getDisplayName() + Constant.LYRIC_FILE_TYPE_TRC);
		if (!file.exists() || !file.isFile()) {
			file = new File(Constant.PATH_MUSIC_LYRIC + File.separator 
				+ musicBean.getTitle() + Constant.LYRIC_FILE_TYPE_TRC);
			if (!file.exists() || !file.isFile()) {
				file = new File(Constant.PATH_MUSIC_LYRIC);
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (files[i].getName().contains(musicBean.getTitle())) {
						file = files[i];
						break;
					}
				}
			}
		}
		if (file.exists() && file.isFile()) {
			try {
				parserLyricByTrc(file, lyricBeans);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		lyricBeans.add(new LyricBean("MusicPlayer", musicBean.getDuration() + 1000000, null));
		return lyricBeans;
	}

	/**
	 * 解析格式为trc（天天动听）的歌词
	 * @param file
	 * @param lyricBeans
	 * @throws IOException
	 */
	private static void parserLyricByTrc(File file, List<LyricBean> lyricBeans) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		String lyric = null;
		for (int i = 0; i < NUM; i++) {
			lyric = br.readLine();
			if (lyric != null) {
				String[] lyrics = lyric.split(":");
				if (lyrics.length > 1) {
					lyric = lyrics[1];
					if (lyric.length() > 1) {
						lyric = lyric.substring(0, lyric.length() - 1);
					}
				} else {
					lyric = "";
				}
			} else {
				lyric = "";
			}
			LyricBean lyricBean = new LyricBean(lyric, 0, null);
			lyricBeans.add(lyricBean);
		}
		while ((lyric = br.readLine()) != null) {
			lyric = lyric.substring(1);
			String[] data = lyric.split("]");
			if (data.length > 1) {
				int time = getTime(data[0]);
				if (data[1].charAt(0) == '<') {
					lyric = data[1].replaceAll("<\\d+>", "");
					LyricBean lyricBean = new LyricBean(lyric, time, getWordTimeList(data[1]));
					lyricBeans.add(lyricBean);
				} else {
					break;
				}
			}
		}
		br.close();
		isr.close();
		
		if (lyricBeans.size() > NUM) {
			int firstTime = lyricBeans.get(NUM).getLyricTime() / 4;
			for (int i = 0; i < NUM; i++) {
				lyricBeans.get(i).setLyricTime(firstTime * i);
			}
		}
		
		
	}
	
	private static List<Integer> getWordTimeList(String word) {
		List<Integer> wordTimeList = new ArrayList<Integer>();
		boolean tag = true;
		int start = 0;
		for (int i = 0; i < word.length(); i++) {
			char ch = word.charAt(i);
			if (ch == '<') {
				tag = true;
				start = i + 1;
			} else if (ch == '>') {
				if (tag) {
					wordTimeList.add(Integer.parseInt(word.substring(start, i)));
					tag = false;
				}
			} else if (ch < '0' && ch > '9') { //标志 '<' 中间出现文字不能算作时间 '>'
				tag = false;
			}
		}
		return wordTimeList;
	}
	
	private static int getTime(String timeStr) {
		String[] mas = timeStr.split(":");
		int time = 0;
		try {
			time = Integer.parseInt(mas[0]) * 60 * 1000 + (int)(Float.parseFloat(mas[1]) * 1000);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return time;
	}
	
}
