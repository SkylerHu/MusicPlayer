package com.anjoyo.musicplayer.adapter;

import java.util.List;

import com.anjoyo.musicplayer.service.MusicPlayService;

import android.content.Context;
import android.widget.BaseAdapter;

public abstract class AbstractAdapter<T> extends BaseAdapter {

	protected Context context;
	protected List<T> dataList;
	protected MusicPlayService musicPlayService;
	
	public AbstractAdapter(Context context, List<T> dataList,
			MusicPlayService musicPlayService) {
		super();
		this.context = context;
		this.dataList = dataList;
		this.musicPlayService = musicPlayService;
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
