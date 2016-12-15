package com.anjoyo.musicplayer.adapter;

import java.util.List;
import java.util.Map;

import com.anjoyo.musicplayer.R;
import com.anjoyo.musicplayer.define.Constant;
import com.anjoyo.musicplayer.service.MusicPlayService;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FolderListViewAdapter extends AbstractAdapter<Map<String, Object>> {

	public FolderListViewAdapter(Context context, List<Map<String, Object>> dataList,
			MusicPlayService musicPlayService) {
		super(context, dataList, musicPlayService);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(context).inflate(R.layout.activity_folder_list_item, null);
		Map<String, Object> data = dataList.get(position);
		String path = (String)data.get(Constant.KEY_FOLDER_LV_NAME);
		String[] pathInfo = getPathInfo(path);
		((TextView)convertView.findViewById(R.id.tv_folder_name)).setText(pathInfo[1]);
		((TextView)convertView.findViewById(R.id.tv_folder_filepath)).setText(pathInfo[0]);
		((TextView)convertView.findViewById(R.id.tv_folder_count))
			.setText("(" + musicPlayService.getMusicInfoMap().get(path).size() + ")");
		ImageView imageView = (ImageView)convertView.findViewById(R.id.iv_folder_isplaying);
		boolean isPlayingFolder = (Boolean) data.get(Constant.KEY_FOLDER_LV_ISPLAYING);
		if (isPlayingFolder) {
			if (musicPlayService.isMediaPlaying()) {
				imageView.setImageResource(R.drawable.current_song_play);
			} else {
				imageView.setImageResource(R.drawable.current_song_pause);
			}
			imageView.setVisibility(View.VISIBLE);
		} else {
			imageView.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}

	
	private String[] getPathInfo(String path) {
		String[] pathInfo = new String[2];
		int i = path.length() - 2;
		for (; i >= 0; i--) {
			if (path.charAt(i) == '/') {
				break;
			}
		}
		pathInfo[0] = path.substring(0, i);
		pathInfo[1] = path.substring(i + 1, path.length() - 1);
		return pathInfo;
	}

}
