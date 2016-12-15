package com.anjoyo.musicplayer.util;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.anjoyo.musicplayer.MusicActivity;
import com.anjoyo.musicplayer.R;
import com.anjoyo.musicplayer.bean.MusicBean;
import com.anjoyo.musicplayer.define.Constant;
import com.anjoyo.musicplayer.service.MusicPlayService;

public class NotificationUtil {

	/** 
	 * 发Notification通知
	 */
	public static void showNotification(Context context, MusicPlayService musicPlayService) {
//		manager.cancel(Constant.NOTIFICATION_ID);
		MusicBean currentMusicBean = musicPlayService.getCurrentMusicBean();
		if (currentMusicBean == null) {
			return;
		}
		
		Notification notification = new Notification();
		//设置标志，通知放置在正在
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		notification.icon = R.drawable.ic_launcher;
		notification.tickerText = currentMusicBean.getDisplayName();
		notification.when = System.currentTimeMillis();
		
		RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_content_view);
		contentView.setImageViewBitmap(R.id.iv_notification, musicPlayService.getCurrentBitmap(false));
		contentView.setTextViewText(R.id.tv_notification_artist, currentMusicBean.getArtist());
		contentView.setTextViewText(R.id.tv_notification_displayname, currentMusicBean.getDisplayName());
		notification.contentView = contentView;
		
		Intent intent = null;
		if (context instanceof Activity) {
			intent = new Intent(context, context.getClass());
		} else {
			intent = new Intent();
			ComponentName component = ContextManagerUtil.getTopActivity(context);
			if (component != null) {
				intent.setComponent(component);
			} else {
				intent = new Intent(context, MusicActivity.class);
			}
		}
		PendingIntent pendingIntent = PendingIntent
				.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.contentIntent = pendingIntent;
//		notification.setLatestEventInfo(this, currentMusicBean.getTitle(), 
//				currentMusicBean.getArtist(), pendingIntent);
		musicPlayService.getNotificationManager().notify(Constant.NOTIFICATION_ID, notification);
	}
	
}
