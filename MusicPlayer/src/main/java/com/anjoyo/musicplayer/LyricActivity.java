package com.anjoyo.musicplayer;

import java.util.List;

import com.anjoyo.musicplayer.bean.LyricBean;
import com.anjoyo.musicplayer.bean.MusicBean;
import com.anjoyo.musicplayer.define.BaseActivity;
import com.anjoyo.musicplayer.define.Constant;
import com.anjoyo.musicplayer.define.LyricView;
import com.anjoyo.musicplayer.util.PlayMusicUtil;
import com.anjoyo.musicplayer.util.TimeUtil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class LyricActivity extends BaseActivity {

	private View mContentView;
	
	private ImageButton mBackImageButton;
	private TextView mDisNameTextView;
	private TextView mArtistTextView;
	private LyricView mLyricView;
	private SeekBar mSeekBar;
	private TextView mCurrentTimeTextView;
	private TextView mDurationTextView;
	private ImageButton mMusicListImageButton;
	private ImageButton mPreImageButton;
	private ImageButton mCurrentImageButton;
	private ImageButton mNextImageButton;
	private ImageButton mFavImageButton;
	
	/** 判断是否是手动改 */
	private boolean seekBarFlag;
	/** 更新线程启动与结束标识 */
	private boolean flag;
	/** 记录index是否有改变 */
	private int indexFlag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setViewListener();
	}

	@Override
	protected void newHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case Constant.HANDLER_REFRESH_LRIC:
					int duration = musicPlayService.getCurrentMusicDuration();
					int progress = musicPlayService.getCurrentPosition();
					if (progress < 1000 || progress >= duration - 50) {
//						PlayMusicUtil.changeMusic(LyricActivity.this, 
//								musicPlayService, Constant.MUSIC_OPERATE_NEXT);
						setView();
					}
					if (mLyricView.getStart() == false) {
						mLyricView.setStart(true);
					}
					if (seekBarFlag) {
						int index = getIndex(progress, duration);
						if (index != indexFlag) {
							mLyricView.setIndex(index);
							indexFlag = index;
						}
						mSeekBar.setProgress(progress);
						mCurrentTimeTextView.setText(TimeUtil.getTime(musicPlayService.getCurrentPosition()));
					}
					break;

				default:
					break;
				}
			}
		};
	}

	@Override
	protected void loadLayout() {
		mContentView = View.inflate(LyricActivity.this, R.layout.activity_lyric, null);
		mContentView.setBackgroundDrawable(new BitmapDrawable(musicPlayService.getCurrentBitmap(true)));
//		view.getBackground().setAlpha(0x44);
		setContentView(mContentView);
		
	}

	@Override
	protected void initView() {
		mBackImageButton = (ImageButton)findViewById(R.id.ibtn_lyric_back);
		mDisNameTextView = (TextView)findViewById(R.id.tv_lyric_display_name);
		mArtistTextView = (TextView)findViewById(R.id.tv_lyric_artist);
		mLyricView = (LyricView)findViewById(R.id.tv_lyric_content);
		mSeekBar = (SeekBar)findViewById(R.id.sb_lyric);
		seekBarFlag = true;
		
		mCurrentTimeTextView = (TextView)findViewById(R.id.tv_lyric_current_time);
		mDurationTextView = (TextView)findViewById(R.id.tv_lyric_duration);
		mMusicListImageButton = (ImageButton)findViewById(R.id.ibtn_lyric_list);
		mPreImageButton = (ImageButton)findViewById(R.id.ibtn_lyric_pre);
		mCurrentImageButton = (ImageButton)findViewById(R.id.ibtn_lyric_current);
		mNextImageButton = (ImageButton)findViewById(R.id.ibtn_lyric_next);
		mFavImageButton = (ImageButton)findViewById(R.id.ibtn_lyric_fav);
	}

	@Override
	protected void setView() {
		mContentView.setBackgroundDrawable(new BitmapDrawable(musicPlayService.getCurrentBitmap(true)));
		mDisNameTextView.setText(musicPlayService.getCurrentMusicBean().getDisplayName());
		mArtistTextView.setText(musicPlayService.getCurrentMusicBean().getArtist());
		mLyricView.setLyricList(musicPlayService.getCurrentLyricList());
		mSeekBar.setMax(musicPlayService.getCurrentMusicDuration());
		mDurationTextView.setText(TimeUtil.getTime(musicPlayService.getCurrentMusicDuration()));
		if (musicPlayService.isMediaPlaying()) {
			mCurrentImageButton.setImageResource(R.drawable.ibtn_pause_selector);
		} else {
			mCurrentImageButton.setImageResource(R.drawable.ibtn_play_selector);
		}
		if (musicPlayService.getCurrentMusicBean().isFavorite()) {
			mFavImageButton.setImageResource(R.drawable.icon_music_favorite);
		} else {
			mFavImageButton.setImageResource(R.drawable.icon_music_normal);
		}
	}
	
	private void setViewListener() {
		AllButtonClickListener listener = new AllButtonClickListener();
		mBackImageButton.setOnClickListener(listener);
		mMusicListImageButton.setOnClickListener(listener);
		mPreImageButton.setOnClickListener(listener);
		mCurrentImageButton.setOnClickListener(listener);
		mNextImageButton.setOnClickListener(listener);
		mFavImageButton.setOnClickListener(listener);
		
		mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				seekBarFlag = true;
				mLyricView.setStart(false);
				if (musicPlayService.isMediaPlaying()) {
					musicPlayService.play();
				}
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				seekBarFlag = false;
				mLyricView.setStart(true);
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser) {
					int index = getIndex(progress, musicPlayService.getCurrentMusicDuration());
					if (index != indexFlag) {
						mLyricView.setIndex(index);
						indexFlag = index;
					}
					mSeekBar.setProgress(progress);
					musicPlayService.setCurrentPausePosition(progress);
					mCurrentTimeTextView.setText(TimeUtil.getTime(progress));
				}
			}
		});
		
	}
	
	private class AllButtonClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ibtn_lyric_back:
				finish();
				overridePendingTransition(R.anim.in_keep_no_anim, R.anim.lyric_out_bottom_to_top);
				break;
			case R.id.ibtn_lyric_list:
				
				break;
			case R.id.ibtn_lyric_pre:
				PlayMusicUtil.changeMusic(LyricActivity.this, 
						musicPlayService, Constant.MUSIC_OPERATE_PRE);
				break;
			case R.id.ibtn_lyric_current:
				if (musicPlayService.isMediaPlaying()) {
					musicPlayService.pause();
					mLyricView.setStart(false);
					mCurrentImageButton.setImageResource(R.drawable.ibtn_play_selector);
					musicPlayService.getCurrentMusicBean().setPlayState(2);
				} else {
					musicPlayService.play();
					mLyricView.setStart(true);
					mCurrentImageButton.setImageResource(R.drawable.ibtn_pause_selector);
					musicPlayService.getCurrentMusicBean().setPlayState(1);
				}
				break;
			case R.id.ibtn_lyric_next:
				PlayMusicUtil.changeMusic(LyricActivity.this, 
						musicPlayService, Constant.MUSIC_OPERATE_NEXT);
				break;
			case R.id.ibtn_lyric_fav:
				final MusicBean musicBean = musicPlayService.getCurrentMusicBean();
				if (musicBean.isFavorite()) {
					new AlertDialog.Builder(LyricActivity.this).setTitle("收藏歌曲提示")
					.setMessage("确定要取消收藏吗？")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							musicPlayService.getFavoriteMusicList().remove(musicBean);
							musicBean.setFavorite(false);
							mFavImageButton.setImageResource(R.drawable.icon_music_normal);
						}
					})
					.setNegativeButton("取消", null)
					.create().show();
				} else {
					musicBean.setFavorite(true);
					mFavImageButton.setImageResource(R.drawable.icon_music_favorite);
					musicPlayService.getFavoriteMusicList().add(musicBean);
				}
				break;

			default:
				break;
			}
		}
	}
	
	private class ShowLyricThread extends Thread {
		@Override
		public void run() {
			while (flag) {
				if (musicPlayService.isMediaPlaying()) {
					handler.sendEmptyMessage(Constant.HANDLER_REFRESH_LRIC);
				}
				try {
					sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (musicPlayService.isMediaPlaying()) {
			mCurrentImageButton.setImageResource(R.drawable.ibtn_pause_selector);
		} else {
			mCurrentImageButton.setImageResource(R.drawable.ibtn_play_selector);
		}
		flag = true;
		new ShowLyricThread().start();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		flag = false;
		mLyricView.setStart(false);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.in_keep_no_anim, R.anim.lyric_out_bottom_to_top);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
	private int getIndex(int progress, int duration) {
		int index = 0;
		List<LyricBean> lyricBeans = musicPlayService.getCurrentLyricList();
		if (lyricBeans == null || lyricBeans.size() == 0) {
			return index;
		}
		if (progress <= duration) {
			for (int i = 1; i < lyricBeans.size(); i++) {
				if (progress <= lyricBeans.get(i).getLyricTime()) {
					index = i - 1;
					break;
				}
			}
		} else {
			index = lyricBeans.size() - 1;
		}
		return index;
	}

}
