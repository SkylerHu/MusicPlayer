package com.anjoyo.musicplayer.adapter;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.anjoyo.musicplayer.R;
import com.anjoyo.musicplayer.bean.MusicBean;
import com.anjoyo.musicplayer.service.MusicPlayService;

public class ShowListViewAdapter extends AbstractAdapter<MusicBean> {

	public ShowListViewAdapter(Context context, List<MusicBean> dataList, MusicPlayService musicPlayService) {
		super(context, dataList, musicPlayService);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.music_list_item, null);
			holder = new ViewHolder();
			holder.favImageButton = (ImageButton)convertView.findViewById(R.id.ibtn_music_list_fav);
			holder.isPlayingImageView = (ImageView)convertView.findViewById(R.id.iv_music_list_isplaying);
			holder.displayNameTextView = (TextView)convertView.findViewById(R.id.tv_music_list_displayname);
			holder.artistTextView = (TextView)convertView.findViewById(R.id.tv_music_list_artist);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final MusicBean musicBean = dataList.get(position);
		if (musicBean.isFavorite()) {
			holder.favImageButton.setImageResource(R.drawable.icon_music_favorite);
		} else {
			holder.favImageButton.setImageResource(R.drawable.icon_music_normal);
		}
		
		holder.favImageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				if (musicBean.isFavorite()) {
					new AlertDialog.Builder(context).setTitle("收藏歌曲提示")
						.setMessage("确定要取消收藏吗？")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								((ImageView)v).setImageResource(R.drawable.icon_music_normal);
								musicPlayService.getFavoriteMusicList().remove(musicBean);
								musicBean.setFavorite(false);
							}
						})
						.setNegativeButton("取消", null)
						.create().show();
				} else {
					((ImageView)v).setImageResource(R.drawable.icon_music_favorite);
					musicBean.setFavorite(true);
					musicPlayService.getFavoriteMusicList().add(musicBean);
				}
			}
		});
		
		holder.displayNameTextView.setText(position+1 + ". " + musicBean.getDisplayName());
		holder.artistTextView.setText(musicBean.getArtist());
		if (musicBean.getPlayState() == 0) {
			holder.isPlayingImageView.setVisibility(View.GONE);
		} else {
			if (musicBean.getPlayState() == 1) {
				holder.isPlayingImageView.setImageResource(R.drawable.current_song_play);
			} else if (musicBean.getPlayState() == 2) {
				holder.isPlayingImageView.setImageResource(R.drawable.current_song_pause);
			}
			holder.isPlayingImageView.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	private static class ViewHolder {
		ImageButton favImageButton;
		ImageView isPlayingImageView;
		TextView displayNameTextView;
		TextView artistTextView;
	}
	
}
