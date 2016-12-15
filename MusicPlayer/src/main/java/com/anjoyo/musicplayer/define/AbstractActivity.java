package com.anjoyo.musicplayer.define;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anjoyo.musicplayer.LyricActivity;
import com.anjoyo.musicplayer.R;
import com.anjoyo.musicplayer.util.PlayMusicUtil;
import com.anjoyo.musicplayer.util.TimeUtil;

public abstract class AbstractActivity extends BaseActivity {

	protected ProgressBar bottomProgressBar;
	protected ImageView bottomArtistImageView;
	protected TextView bottomArtistTextView;
	protected TextView bottomNameTextView;
	protected TextView bottomTimeTextView;
	
	protected ImageButton bottomPlayPreImageButton;
	protected ImageButton bottomPlayCurrentImageButton;
	protected ImageButton bottomPlayNextImageButton;
	
	protected RelativeLayout bottomRelativeLayout;
	
	/** 更新界面线程的运行标志 */
	private boolean flag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initBottomView();
		setBottomListener();
//		View view = getWindow().getDecorView();
//		view.getBackground().setAlpha(0x33);
	}
	
	@Override
	protected void newHandler() {
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case Constant.HANDLER_REFRESH_VIEW:
					int duration = bottomProgressBar.getMax();
					int position = musicPlayService.getCurrentPosition();
					if (position < 1000 || position >= duration - 50) {
//						PlayMusicUtil.changeMusic(AbstractActivity.this, 
//								musicPlayService, Constant.MUSIC_OPERATE_NEXT);
						setBottomView();
						onMusicRefresh();
					} else {
						bottomProgressBar.setProgress(position);
						String time = TimeUtil.getTime(position) + " - " + TimeUtil.getTime(duration);
						bottomTimeTextView.setText(time);
					}
					break;
				default:
					break;
				}
			}
		};
	}
	
//	/** 检测是否更改了文件夹操作 */
//	public void onFolderChange(){}
	
	/** 
	 * 播放状态改变，刷新界面
	 */
	public abstract void onMusicRefresh();
	
	private void initBottomView() {
		bottomProgressBar = (ProgressBar)findViewById(R.id.pb_bottom_progress);
		bottomArtistImageView = (ImageView)findViewById(R.id.iv_bottom_artist);
		bottomArtistTextView = (TextView)findViewById(R.id.tv_bottom_artist);
		bottomNameTextView = (TextView)findViewById(R.id.tv_bottom_display_name);
		bottomTimeTextView = (TextView)findViewById(R.id.tv_bottom_time);
		bottomPlayPreImageButton = (ImageButton)findViewById(R.id.ibtn_bottom_play_pre);
		bottomPlayCurrentImageButton = (ImageButton)findViewById(R.id.ibtn_bottom_play_current);
		bottomPlayNextImageButton = (ImageButton)findViewById(R.id.ibtn_bottom_play_next);
		
		bottomRelativeLayout = (RelativeLayout)findViewById(R.id.rl_bottom);
		
	}
	
	public void setBottomView() {
		if (musicPlayService.getCurrentMusicBean() == null) {
			return;
		}
		bottomProgressBar.setMax(musicPlayService.getCurrentMusicBean().getDuration());
		bottomArtistImageView.setImageBitmap(musicPlayService.getCurrentBitmap(false));
		bottomArtistTextView.setText(musicPlayService.getCurrentMusicBean().getArtist());
		bottomNameTextView.setText(musicPlayService.getCurrentMusicBean().getDisplayName());
		if (musicPlayService.isMediaPlaying()) {
			bottomPlayCurrentImageButton.setImageResource(R.drawable.ibtn_pause_selector);
		} else {
			bottomPlayCurrentImageButton.setImageResource(R.drawable.ibtn_play_selector);
		}
	}
	
	private void setBottomListener() {
		BottomImageButtonListener listener = new BottomImageButtonListener();
		bottomPlayPreImageButton.setOnClickListener(listener);
		bottomPlayCurrentImageButton.setOnClickListener(listener);
		bottomPlayNextImageButton.setOnClickListener(listener);
		
		bottomRelativeLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AbstractActivity.this, LyricActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.lyric_in_bottom_to_top, R.anim.in_keep_no_anim);
			}
		});
	}
	
	private class BottomImageButtonListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
//			onFolderChange();//在别的文件夹下加此操作，可更改文件夹进行播放
			//或者调用PlayMusicUtil.onFolderChange(Context context, MusicPlayService musicPlayService, 
//				List<MusicBean> musicBeans, String folderName)
			switch (v.getId()) {
			case R.id.ibtn_bottom_play_pre:
				PlayMusicUtil.changeMusic(AbstractActivity.this, 
						musicPlayService, Constant.MUSIC_OPERATE_PRE);
				setBottomView();
				break;
			case R.id.ibtn_bottom_play_current:
				if (musicPlayService.getCurrentMusicBean() == null) {
					Toast.makeText(AbstractActivity.this, R.string.no_music, Toast.LENGTH_LONG);
					return;
				}
				if (musicPlayService.isMediaPlaying()) {
					musicPlayService.pause();
					bottomPlayCurrentImageButton.setImageResource(R.drawable.ibtn_play_selector);
					musicPlayService.getCurrentMusicBean().setPlayState(2);
				} else {
					musicPlayService.play();
					bottomPlayCurrentImageButton.setImageResource(R.drawable.ibtn_pause_selector);
					musicPlayService.getCurrentMusicBean().setPlayState(1);
				}
				break;
			case R.id.ibtn_bottom_play_next:
				PlayMusicUtil.changeMusic(AbstractActivity.this, 
						musicPlayService, Constant.MUSIC_OPERATE_NEXT);
				setBottomView();
				break;
			default:
				break;
			}
			onMusicRefresh();
		}
	}
	
	private class RefreshInfoThread extends Thread {
		@Override
		public void run() {
			while (flag) {
				if (musicPlayService.isMediaPlaying()) {
					handler.sendEmptyMessage(Constant.HANDLER_REFRESH_VIEW);
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
		flag = true;//线程可以更新界面
		setBottomView();
		new RefreshInfoThread().start();
		onMusicRefresh();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		flag = false;//结束更新线程
	}

}
