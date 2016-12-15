package com.anjoyo.musicplayer.define;

import com.anjoyo.musicplayer.R;
import com.anjoyo.musicplayer.application.MusicApplication;
import com.anjoyo.musicplayer.bean.MusicState;
import com.anjoyo.musicplayer.service.MusicPlayService;
import com.anjoyo.musicplayer.util.ContextManagerUtil;
import com.anjoyo.musicplayer.util.NotificationUtil;
import com.anjoyo.musicplayer.util.SharePreferenceUtil;
import com.anjoyo.musicplayer.util.XmlDom4jUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

public abstract class BaseActivity extends Activity {
	
	protected MusicApplication application;
	protected MusicPlayService musicPlayService;
	protected Handler handler;
	
	protected int screenWidth;
	protected int screenHeight;
	
	private PopupWindow mPopupWindow;
	private boolean popupFlag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		application = (MusicApplication) getApplication();
		musicPlayService = application.getMusicPlayService();
		
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		screenWidth = outMetrics.widthPixels;
		screenHeight = outMetrics.heightPixels;
		
		init();
	}

	private void init() {
		newHandler();
		loadLayout();
		initView();
		setView();
		initPopupMenu();
	}
	
	protected abstract void newHandler();
	protected abstract void loadLayout();
	protected abstract void initView();
	protected abstract void setView();
	
	@Override
	protected void onResume() {
		super.onResume();
		if (application.isProgramExit()) {
			finish();
		} else {
			NotificationUtil.showNotification(this, musicPlayService);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (application.isProgramExit() && !application.isSaveState()) {
			MusicState musicState = musicPlayService.getMusicState();
			SharePreferenceUtil.saveMusicState(this, musicState);
			
			XmlDom4jUtil.saveFavoriteMusic(musicPlayService.getFavoriteMusicList());
			
			application.setSaveState(true);
			if (ContextManagerUtil.isServiceRunning(this, MusicPlayService.class)) {
				application.unbindService(application.getServiceConnection());
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			new AlertDialog.Builder(BaseActivity.this).setTitle("提示")
				.setMessage("确定退出？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						application.setProgramExit(true);
						finish();
					}
				})
				.setNegativeButton("取消", null)
				.create().show();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
	/** 使用PopupWindow建立菜单 */
	private void initPopupMenu() {
		View poupuView = LayoutInflater.from(this).inflate(R.layout.menu_popup_window, null);
		mPopupWindow = new PopupWindow(poupuView, 120, 200);
	}
	
}
