package com.anjoyo.musicplayer.bean;

public class MusicBean {

	/** 音乐Id */
	private int id;
	/** 歌曲名称 */
	private String title;
	/** 歌手id */
	private int artistId;
	/** 歌手名称 */
	private String artist;
	/** 专辑id */
	private int albumId;
	/** 专辑名称 */
	private String album;
	/** 歌手-歌曲名 , 原数据以".mp3"结束，已去除*/
	private String displayName;
	/** 音乐播放时长 */
	private int duration;
	/** 音乐文件大小 */
	private int size;
	/** 音乐路径 */
	private String path;
	/** 是否喜欢 */
	private boolean favorite;
	/** 是否放置到播放器中 
	 * 0已放置，1处于播放状态， 2处于暂停状态*/
	private int playState;
	
	public MusicBean(int id, String title, int artistId, String artist,
			int albumId, String album, String displayName, int duration,
			int size, String path) {
		super();
		this.id = id;
		this.title = title;
		this.artistId = artistId;
		this.artist = artist;
		this.albumId = albumId;
		this.album = album;
		this.displayName = displayName;
		this.duration = duration;
		this.size = size;
		this.path = path;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getArtistId() {
		return artistId;
	}

	public void setArtistId(int artistId) {
		this.artistId = artistId;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public int getAlbumId() {
		return albumId;
	}

	public void setAlbumId(int albumId) {
		this.albumId = albumId;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}
	
	public int getPlayState() {
		return playState;
	}

	public void setPlayState(int playState) {
		this.playState = playState;
	}

	@Override
	public String toString() {
		return "MusicBean [id=" + id + ", title=" + title + ", artistId="
				+ artistId + ", artist=" + artist + ", albumId=" + albumId
				+ ", album=" + album + ", displayName=" + displayName
				+ ", duration=" + duration + ", size=" + size + ", path="
				+ path + "]";
	}

}
