package com.anjoyo.musicplayer.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;

import com.anjoyo.musicplayer.R;
import com.anjoyo.musicplayer.application.MusicApplication;
import com.anjoyo.musicplayer.bean.MusicBean;
import com.anjoyo.musicplayer.define.Constant;
import com.anjoyo.musicplayer.service.MusicPlayService;

public class AdapterListUtil {

	/**
	 * 初始化ShowListActivity中ListView的数据源
	 * @param application
	 * @param folderName
	 * @return
	 */
	public static List<MusicBean> initShowListViewList(MusicApplication application, String folderName) {
		List<MusicBean> musicBeans = new ArrayList<MusicBean>();
		if (Constant.MUSIC_PAGER_GV_LOCAL.equals(folderName)) {
			musicBeans = application.getMusicList();
		} else if (Constant.MUSIC_PAGER_GV_FAV.equals(folderName)) {
			musicBeans = application.getMusicPlayService().getFavoriteMusicList();
		} else {
			musicBeans = application.getMusicInfoMap().get(folderName);
		}
		return musicBeans;
	}
	
	/**
	 * 初始化FolderActivity中ListView的数据源
	 * @param musicPlayService
	 * @return
	 */
	public static List<Map<String, Object>> initFolderListViewList(MusicPlayService musicPlayService) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		List<String> folderList = musicPlayService.getFloderList();
		for (String string : folderList) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put(Constant.KEY_FOLDER_LV_NAME, string);
			data.put(Constant.KEY_FOLDER_LV_ISPLAYING, 
					string.equals(musicPlayService.getCurrentPlayFolder()));
			dataList.add(data);
		}
		return dataList;
	}
	
	/** 
	 * 初始化MusicActivity中GridView的数据源
	 * @param context
	 * @param application
	 * @return
	 */
	public static List<Map<String, Object>> initMusicGridViewList(Context context, 
			MusicPlayService musicPlayService) {
		int state = musicPlayService.getMusicState().getWhatMusicFolder();
		
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		Resources resources = context.getResources();
		Map<String, Object> data0 = new HashMap<String, Object>();
		data0.put(Constant.KEY_GV_ITEM_IMAGE, resources.getDrawable(R.drawable.icon_local_music));
		data0.put(Constant.KEY_GV_ITEM_NAME, Constant.MUSIC_PAGER_GV_LOCAL);
		data0.put(Constant.KEY_GV_ITEM_NUM, musicPlayService.getMusicList().size()+"");
		data0.put(Constant.KEY_GV_ITEM_ISPLAYING, state==0?true:false);
		dataList.add(data0);
		Map<String, Object> data1 = new HashMap<String, Object>();
		data1.put(Constant.KEY_GV_ITEM_IMAGE, resources.getDrawable(R.drawable.icon_favorites));
		data1.put(Constant.KEY_GV_ITEM_NAME, Constant.MUSIC_PAGER_GV_FAV);
		data1.put(Constant.KEY_GV_ITEM_NUM, musicPlayService.getFavoriteMusicList().size()+"");
		data1.put(Constant.KEY_GV_ITEM_ISPLAYING, state==1?true:false);
		dataList.add(data1);
		Map<String, Object> data2 = new HashMap<String, Object>();
		data2.put(Constant.KEY_GV_ITEM_IMAGE, resources.getDrawable(R.drawable.icon_folder_plus));
		data2.put(Constant.KEY_GV_ITEM_NAME, Constant.MUSIC_PAGER_GV_FOLDER);
		data2.put(Constant.KEY_GV_ITEM_NUM, musicPlayService.getMusicInfoMap().keySet().size()+"");
		data2.put(Constant.KEY_GV_ITEM_ISPLAYING, state==2?true:false);
		dataList.add(data2);
		Map<String, Object> data3 = new HashMap<String, Object>();
		data3.put(Constant.KEY_GV_ITEM_IMAGE, resources.getDrawable(R.drawable.icon_download));
		data3.put(Constant.KEY_GV_ITEM_NAME, Constant.MUSIC_PAGER_GV_DOWNLOAD);
		data3.put(Constant.KEY_GV_ITEM_NUM, 0+"");
		data3.put(Constant.KEY_GV_ITEM_ISPLAYING, false);
		dataList.add(data3);
		Map<String, Object> data4 = new HashMap<String, Object>();
		data4.put(Constant.KEY_GV_ITEM_IMAGE, resources.getDrawable(R.drawable.icon_add_operate));
		data4.put(Constant.KEY_GV_ITEM_NAME, Constant.MUSIC_PAGER_GV_ADD_OPERATE);
		data4.put(Constant.KEY_GV_ITEM_NUM, "");
		data4.put(Constant.KEY_GV_ITEM_ISPLAYING, false);
		dataList.add(data4);
		return dataList;
	}
	
}
