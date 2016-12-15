package com.anjoyo.musicplayer.util;

import java.util.List;

import com.anjoyo.musicplayer.bean.MusicBean;

public class MusicSortUtil {
	
	
	public static void quitSort(List<MusicBean> musicBeans, int low, int high) {
		if (low < high) {
			int provtIndex = partition(musicBeans, low, high);
			quitSort(musicBeans, low, provtIndex - 1);
			quitSort(musicBeans, provtIndex + 1, high);
		}
	}
	
	private static int partition(List<MusicBean> musicBeans, int low, int high) {
		MusicBean provtKeyBean = musicBeans.get(low);
		while (low < high) {
			while (low < high && PingYinUtil.getPingYin(musicBeans.get(high).getTitle())
					.compareTo(PingYinUtil.getPingYin(provtKeyBean.getTitle())) >= 0) {
				high--;
			}
			musicBeans.set(low, musicBeans.get(high));
			while (low < high && PingYinUtil.getPingYin(musicBeans.get(high).getTitle())
					.compareTo(PingYinUtil.getPingYin(provtKeyBean.getTitle())) <= 0) {
				low++;
			}
			musicBeans.set(high, musicBeans.get(low));
		}
		musicBeans.set(low, provtKeyBean);
		return low;
	}
	
}
