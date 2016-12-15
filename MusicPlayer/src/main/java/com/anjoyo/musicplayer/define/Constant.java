package com.anjoyo.musicplayer.define;

public abstract class Constant {

//	/** 数据库名 */
//	public static final String DB_NAME = "music_player";
//	/** “我的最爱”存储的表名 */
//	public static final String TABLE_FAVORITE = "music_is_favorite";
//	/** 字段：{@link TABLE_FAVORITE}的_id */
//	public static final String FIELD_ID = "_id";
//	/** 字段：是否为“我的最爱” */
//	public static final String FIELD_FAVORITE = "is_favorite";
//	/** 字段：音乐Id */
//	public static final String MUSIC_ID = android.provider.MediaStore.Audio.Media._ID;
//	/** 字段：歌曲名称 */
//	public static final String MUSIC_TITLE = android.provider.MediaStore.Audio.Media.TITLE;
//	/** 字段：歌手id */
//	public static final String MUSIC_ARTIST_ID = android.provider.MediaStore.Audio.Media.ARTIST_ID;
//	/** 字段：歌手名称 */
//	public static final String MUSIC_ARTIST = android.provider.MediaStore.Audio.Artists.ARTIST;
//	/** 字段：专辑id */
//	public static final String MUSIC_ALBUM_ID = android.provider.MediaStore.Audio.Media.ALBUM_ID;
//	/** 字段：专辑名称 */
//	public static final String MUSIC_ALBUM = android.provider.MediaStore.Audio.Albums.ALBUM;
//	/** 字段：歌手-歌曲名 */
//	public static final String MUSIC_DISPLAY_NAME = android.provider.MediaStore.Audio.Media.DISPLAY_NAME;
//	/** 字段：音乐播放时长 */
//	public static final String MUSIC_DURATION = android.provider.MediaStore.Audio.Media.DURATION;
//	/** 字段：音乐文件大小 */
//	public static final String MUSIC_SIZE = android.provider.MediaStore.Audio.Media.SIZE;
//	/** 字段：音乐路径 */
//	public static final String MUSIC_PATH = android.provider.MediaStore.Audio.Media.DATA;
	
	/** “我的最爱” 存储 的 文件名  */
	public static final String FILE_XML_FAVORITE_MUSIC = "favorite_music.xml";
	/** “我的最爱” 文件存储的路径 */
	public static final String FILE_PATH = "/mnt/sdcard/Android/data/com.anjoyo.musicplayer";
	/** “我的最爱” 文件 根节点 */
	public static final String FILE_XML_ROOT = "FavoriteMusic";
	/** “我的最爱” 文件 子节点 */
	public static final String FILE_XML_ELEMENT = "MusicId";
	
	
	/** 获取数据成功,Application中的数据已赋值 */
	public static final int MAIN_WHAT_GET_DATA_SUCCESS = 1001;
	/** Application中的数据都不为空，进入操作界面 */
	public static final int MAIN_WHAT_START_ACTIVITY = 1002;
	
	
	/** Notification的id */
	public static final int NOTIFICATION_ID = 1234;
	
	/** 保存设置信息的文件名 */
	public static final String SHARED_FILE_NAME = "music_state";
	/** 记录播放的当前歌曲id */
	public static final String SHARED_CURRENT_MUSIC_ID = "current_music_play";
	/** 记录播放模式 */
	public static final String SHARED_MUSIC_PLAY_MODE = "music_play_mode";
	/** 记录是否显示桌面歌词 */
	public static final String SHARED_SHOW_DESKTOP_LYRIC = "show_desktop_lyric";
	/** 记录是否播放全部音乐 */
	public static final String SHARED_IS_ALL_MUSIC_FOLDER = "is_all_music_folder";
	
	/** 顺序播放 */
	public static final int PLAY_MODE_INT_ORDER = 0;
	public static final String PLAY_MODE_ORDER = "顺序播放";
	/** 单曲循环 */
	public static final int PLAY_MODE_INT_REPEAT = 1;
	public static final String PLAY_MODE_REPEAT = "单曲循环";
	/** 循环播放 */
	public static final int PLAY_MODE_INT_LOOP = 2;
	public static final String PLAY_MODE_LOOP = "循环播放";
	/** 随机播放 */
	public static final int PLAY_MODE_INT_RANDOM = 3;
	public static final String PLAY_MODE_RANDOM = "随机播放";
	
	/** SD卡中歌手图片目录 */
//	public static final String PATH_ARTIST_IMAGE = "/mnt/sdcard/MusicPlayer/artist";
//	/** SD卡中歌词目录 */
//	public static final String PATH_MUSIC_LYRIC = "/mnt/sdcard/MusicPlayer/lyric";
	//天天动听手机上的目录
	public static final String PATH_ARTIST_IMAGE = "/mnt/sdcard/ttpod/art/artist";
	public static final String PATH_MUSIC_LYRIC = "/mnt/sdcard/ttpod/lyric";
	
	/** LyricActivity显示的歌词文件存储格式 */
	public static final String LYRIC_FILE_TYPE_TRC = ".trc";
	public static final String LYRIC_FILE_TYPE_LRC = ".lrc";
	
	/** MusicActivity的GridView中item图片 */
	public static final String KEY_GV_ITEM_IMAGE = "gv_item_image";
	/** MusicActivity的GridView中item名称 */
	public static final String KEY_GV_ITEM_NAME = "gv_item_name";
	public static final String MUSIC_PAGER_GV_LOCAL = "我的音乐";
	public static final String MUSIC_PAGER_GV_FAV = "我的最爱";
	public static final String MUSIC_PAGER_GV_FOLDER = "文件夹";
	public static final String MUSIC_PAGER_GV_DOWNLOAD = "下载管理";
	public static final String MUSIC_PAGER_GV_ADD_OPERATE = "定制首页";
	/** MusicActivity的GridView中item数据 */
	public static final String KEY_GV_ITEM_NUM = "gv_item_num";
	/** MusicActivity的GridView中item:是全部音乐，还是文件夹音乐 */
	public static final String KEY_GV_ITEM_ISPLAYING = "gv_item_isplaying";
	
	/** FolderActivity中ListView初始化数据源所需要的key：文件夹名 */
	public static final String KEY_FOLDER_LV_NAME = "folder_item_name";
	/** FolderActivity中ListView初始化数据源所需要的key：记录播放器的播放状态，正确显示图标 */
	public static final String KEY_FOLDER_LV_ISPLAYING = "folder_item_isplaying";
	
	/** 显示歌曲列表时，传递数据：歌曲所在文件夹名称 */
	public static final String INTENT_MUSIC_FOLDER_NAME = "music_folder_name";
	
	
	/** AbstractActivity中Hander标志：更新播放进度 */
	public static final int HANDLER_REFRESH_VIEW = 2100;
	/** LyricActivity中Hander标志：更新播放进度 */
	public static final int HANDLER_REFRESH_LRIC = 2233;
	/** LyricActivity中Hander标志：更新背景图片显示 */
	public static final int HANDLER_REFRESH_BG = 2244;
	
	//PlayMusicUtil
	/** 播放上一首歌曲 */
	public static final int MUSIC_OPERATE_PRE = -1;
	/** 对当前歌曲操作 */
	public static final int MUSIC_OPERATE_CURRENT = 0;
	/** 播放下一首歌曲 */
	public static final int MUSIC_OPERATE_NEXT = 1;
	
	
	
}
