package com.anjoyo.musicplayer.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.anjoyo.musicplayer.bean.MusicBean;

public class MusicManagerUtil {

	/** 
	 * 获取“我的最爱”列表
	 * @param musicBeans
	 * @return
	 */
	public static List<MusicBean> getFavMusicList(List<MusicBean> musicBeans) {
		List<MusicBean> favMusicList = new ArrayList<MusicBean>();
		for (MusicBean musicBean : musicBeans) {
			if (musicBean.isFavorite()) {
				favMusicList.add(musicBean);
			}
		}
		return favMusicList;
	}
	
	/** 
	 * 歌曲所在文件夹集合，key为文件夹、value为对应之下的歌曲List
	 * @param context
	 * @return
	 */
	public static Map<String, List<MusicBean>> getMusicMap(Context context) {
		Map<String, List<MusicBean>> musicMap = new HashMap<String, List<MusicBean>>();
		List<MusicBean> musicList = getMusicList(context);
		for (MusicBean musicBean : musicList) {
			String keyPath = musicBean.getPath().split(musicBean.getDisplayName())[0];
			if (musicMap.containsKey(keyPath)) {
				musicMap.get(keyPath).add(musicBean);
			} else {
				List<MusicBean> musicBeans = new ArrayList<MusicBean>();
				musicBeans.add(musicBean);
				musicMap.put(keyPath, musicBeans);
			}
		}
		return musicMap;
	}
	
	/** 
	 * 把获得的List数据分文件夹
	 * @param musicList
	 * @return
	 */
	public static Map<String, List<MusicBean>> getMusicMap(List<MusicBean> musicList) {
		Map<String, List<MusicBean>> musicMap = new HashMap<String, List<MusicBean>>();
		for (MusicBean musicBean : musicList) {
			String keyPath = getKeyPath(musicBean.getPath());
			if (musicMap.containsKey(keyPath)) {
				musicMap.get(keyPath).add(musicBean);
			} else {
				List<MusicBean> musicBeans = new ArrayList<MusicBean>();
				musicBeans.add(musicBean);
				musicMap.put(keyPath, musicBeans);
			}
		}
		return musicMap;
	}
	
	private static String getKeyPath(String path) {
		String str = "";
		for (int i = path.length() - 1; i >= 0; i--) {
			if (path.charAt(i) == '/') {
				str = path.substring(0, i+1);
				break;
			}
		}
		return str;
	}
	
	/** 
	 * 更改歌曲信息
	 * @param context
	 * @param musicBean
	 */
	public static void updateMusicInfo(Context context, MusicBean musicBean, String OldDisplayName) {
		ContentResolver resolver = context.getContentResolver();
		ContentValues values = new ContentValues();
		String displayName = musicBean.getDisplayName();
		String path = musicBean.getPath().split(OldDisplayName)[0] + displayName + ".mp3";
		
		File oldFile = new File(musicBean.getPath());
		File newFile = new File(path);
		oldFile.renameTo(newFile);
		musicBean.setPath(path);
		
		values.put(MediaStore.Audio.Media.DATA, path);
		values.put(MediaStore.Audio.Media.DISPLAY_NAME, displayName + ".mp3");
		values.put(MediaStore.Audio.Media.ARTIST, musicBean.getArtist());
		values.put(MediaStore.Audio.Media.ALBUM, musicBean.getAlbum());
		resolver.update(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, 
				values, MediaStore.Audio.Media._ID + "=" + musicBean.getId(), null);
		
//		values.clear();
//		values.put(MediaStore.Audio.Artists.ARTIST, musicBean.getArtist());
//		resolver.update(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, 
//				values, MediaStore.Audio.Artists._ID + "=" + musicBean.getArtistId(), null);
//		
//		values.clear();
//		values.put(MediaStore.Audio.Albums.ARTIST, musicBean.getArtist());
//		resolver.update(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, 
//				values, MediaStore.Audio.Albums._ID + "=" + musicBean.getAlbumId(), null);
	}
	
	/** 
	 * 删除数据库中的信息
	 * @param context
	 * @param musicBean
	 * @param isDeleteFile 是否删除本地文件
	 */
	public static void deleteMusic(Context context, MusicBean musicBean, boolean isDeleteFile) {
		context.getContentResolver().delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, 
				MediaStore.Audio.Media._ID + "=" + musicBean.getId(), null);
		if (isDeleteFile) {
			File file = new File(musicBean.getPath());
			if (file.exists()) {
				file.delete();
			}
		}
	}
	
	/** 
	 * 扫描获得手机里的音乐信息，并返回一个List集合
	 * @param context
	 * @return 
	 */
	public static List<MusicBean> getMusicList(Context context) {
		
		List<Integer> favoriteMusicList = XmlDom4jUtil.getFavoriteMusic();
		
		List<MusicBean> musicList = new ArrayList<MusicBean>();
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
				MediaStore.Audio.Media.IS_MUSIC + "=1", null,
				MediaStore.Audio.Media.TITLE);
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
			String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
			int artistId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
			String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
//			Cursor artistCursor = context.getContentResolver().query(
//					MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, null, 
//					MediaStore.Audio.Artists._ID + "=" + artistId, null, null);
//			String artist = "";
//			if (artistCursor.moveToNext()) {
//				artist = artistCursor.getString(artistCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
//			}
			int albumId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
			String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
//			Cursor albumCursor = context.getContentResolver().query(
//					MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, null, 
//					MediaStore.Audio.Albums._ID + "=" + albumId, null, null);
//			String album = "";
//			if (albumCursor.moveToNext()) {
//				album = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
//			}
			String display_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
			String displayName = display_name.substring(0, display_name.length() - 4);
			int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
			int size = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
			String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

			MusicBean musicBean = new MusicBean(id, title, artistId, artist, 
					albumId, album, displayName, duration, size, path);
			
			for (Integer musicId : favoriteMusicList) {
				if (musicBean.getId() == musicId) {
					musicBean.setFavorite(true);
					break;
				}
			}
			
			musicList.add(musicBean);
		}
		return musicList;
	}

}
