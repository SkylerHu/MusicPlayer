package com.anjoyo.musicplayer.adapter;

import java.util.List;
import java.util.Map;

import com.anjoyo.musicplayer.R;
import com.anjoyo.musicplayer.define.Constant;
import com.anjoyo.musicplayer.service.MusicPlayService;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicGridViewAdapter extends AbstractAdapter<Map<String, Object>> {

	public MusicGridViewAdapter(Context context, List<Map<String, Object>> dataList, 
			MusicPlayService musicPlayService) {
		super(context, dataList, musicPlayService);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(context).inflate(R.layout.music_pager_mine_gv_item, null);
		Map<String, Object> data = dataList.get(position);
		((ImageView)convertView.findViewById(R.id.iv_music_mine_gv))
			.setImageDrawable((Drawable)data.get(Constant.KEY_GV_ITEM_IMAGE));
		String name = (String)data.get(Constant.KEY_GV_ITEM_NAME);
		((TextView)convertView.findViewById(R.id.tv_music_mine_gv))
			.setText(name);
		((TextView)convertView.findViewById(R.id.tv_music_mine_gv_num))
			.setText((String)data.get(Constant.KEY_GV_ITEM_NUM));
		ImageView imageView = (ImageView)convertView.findViewById(R.id.iv_music_mine_gv_state);
		boolean isPlayMusicFolder = (Boolean) data.get(Constant.KEY_GV_ITEM_ISPLAYING);
		if (isPlayMusicFolder) {
			imageView.setVisibility(View.VISIBLE);
		} else {
			imageView.setVisibility(View.INVISIBLE);
		}
		if (musicPlayService.isMediaPlaying()) {
			imageView.setImageResource(R.drawable.current_song_play);
		} else {
			imageView.setImageResource(R.drawable.current_song_pause);
		}
		return convertView;
	}

}
