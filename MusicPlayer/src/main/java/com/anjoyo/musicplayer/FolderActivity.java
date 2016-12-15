package com.anjoyo.musicplayer;

import java.util.List;
import java.util.Map;

import com.anjoyo.musicplayer.adapter.FolderListViewAdapter;
import com.anjoyo.musicplayer.define.AbstractListActivity;
import com.anjoyo.musicplayer.define.Constant;
import com.anjoyo.musicplayer.util.AdapterListUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class FolderActivity extends AbstractListActivity {

	private TextView mTopTextView;
	
	private ListView mFolderListView;
	private List<Map<String, Object>> mFolderList;
	private FolderListViewAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void loadLayout() {
		setContentView(R.layout.activity_folder);
	}

	@Override
	protected void initView() {
		mTopTextView = (TextView)findViewById(R.id.tv_activity_top_title);
		mFolderListView = (ListView)findViewById(R.id.lv_activity_folder);
		mFolderList = AdapterListUtil.initFolderListViewList(musicPlayService);
	}

	@Override
	protected void setView() {
		mTopTextView.setText("文件夹");
		mAdapter = new FolderListViewAdapter(FolderActivity.this, mFolderList, musicPlayService);
		mFolderListView.setAdapter(mAdapter);
		
		mFolderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(FolderActivity.this, ShowListActivity.class);
				intent.putExtra(Constant.INTENT_MUSIC_FOLDER_NAME, 
						(String)mFolderList.get(position).get(Constant.KEY_FOLDER_LV_NAME));
				startActivity(intent);
				
			}
		});
	}
	
	@Override
	public void onMusicRefresh() {
		List<Map<String, Object>> list = AdapterListUtil.initFolderListViewList(musicPlayService);
		mFolderList.clear();
		mFolderList.addAll(list);
		mAdapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		onMusicRefresh();
	}

}
