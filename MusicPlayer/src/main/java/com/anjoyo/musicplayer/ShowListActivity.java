package com.anjoyo.musicplayer;

import java.util.List;

import com.anjoyo.musicplayer.adapter.ShowListViewAdapter;
import com.anjoyo.musicplayer.bean.MusicBean;
import com.anjoyo.musicplayer.define.AbstractListActivity;
import com.anjoyo.musicplayer.define.Constant;
import com.anjoyo.musicplayer.util.AdapterListUtil;
import com.anjoyo.musicplayer.util.MusicManagerUtil;
import com.anjoyo.musicplayer.util.PlayMusicUtil;
import com.anjoyo.musicplayer.util.TimeUtil;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowListActivity extends AbstractListActivity {

	private TextView mTopTextView;
	
	private ListView mListView;
	private List<MusicBean> mMusicList;
	private ShowListViewAdapter mShowAdapter;
	
	private String mFolderName;
	
	/** 删除菜单 */
	private static final int MENU_DELETE = Menu.FIRST + 10000;
	/** 查看详细信息菜单 */
	private static final int MENU_DETAIL = Menu.FIRST + 10001;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void loadLayout() {
		setContentView(R.layout.activity_show_list);
	}

	@Override
	protected void initView() {
		mTopTextView = (TextView)findViewById(R.id.tv_activity_top_title);
		mListView = (ListView)findViewById(R.id.lv_show_list);
		
		mFolderName = getIntent().getStringExtra(Constant.INTENT_MUSIC_FOLDER_NAME);
		mMusicList = AdapterListUtil.initShowListViewList(application, mFolderName);
	}

	@Override
	protected void setView() {
		mTopTextView.setText(mFolderName);
		
		mShowAdapter = new ShowListViewAdapter(ShowListActivity.this, mMusicList, musicPlayService);
		mListView.setAdapter(mShowAdapter);
		
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onFolderChange();
				ImageView isPlay = (ImageView) view.findViewById(R.id.iv_music_list_isplaying);
				if (musicPlayService.isMediaPlaying() && 
						musicPlayService.getCurrentMusicBean().getId() == mMusicList.get(position).getId()) {
//						musicPlayService.getCurrentPlayFolder().equals(mFolderName) &&
//						musicPlayService.getCurrentIndex() == position) {
					musicPlayService.pause();
					mMusicList.get(position).setPlayState(2);
					isPlay.setImageResource(R.drawable.current_song_pause);
				} else {
					if (musicPlayService.getCurrentIndex() != position) {
						PlayMusicUtil.updateCurrentInfo(ShowListActivity.this, 
								musicPlayService, mMusicList, position, 
								musicPlayService.isMediaPlaying()?1:2);
					}
					musicPlayService.play();
					mMusicList.get(position).setPlayState(1);
					isPlay.setVisibility(View.VISIBLE);
					isPlay.setImageResource(R.drawable.current_song_play);
				}
				
				setBottomView();
				onMusicRefresh();
			}
		});
		
		registerForContextMenu(mListView);
	}

	@Override
	public void onMusicRefresh() {
		mShowAdapter.notifyDataSetChanged();
//		if (musicPlayService.getCurrentPlayFolder().equals(mFolderName)) {
//			for (int i = 0; i < mListView.getChildCount(); i++) {
//				ImageView isPlay = (ImageView) mListView.getChildAt(i)
//						.findViewById(R.id.iv_music_list_isplaying);
//				if (i != musicPlayService.getCurrentIndex()) {
//					isPlay.setVisibility(View.GONE);
//				} else {
//					isPlay.setVisibility(View.VISIBLE);
//					if (musicPlayService.isMediaPlaying()) {
//						isPlay.setImageResource(R.drawable.current_song_play);
//					} else {
//						isPlay.setImageResource(R.drawable.current_song_pause);
//					}
//				}
//			}
//		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (mFolderName.equals(musicPlayService.getCurrentPlayFolder())) {
			mListView.setSelection(musicPlayService.getCurrentIndex());
		}
	}
	
//	@Override
	public void onFolderChange() {
		if (! musicPlayService.getCurrentPlayFolder().equals(mFolderName)) {
			musicPlayService.setCurrentPlayFolder(mFolderName);
			musicPlayService.setCurrentMusicList(mMusicList);
			musicPlayService.getMusicState().setWhatMusicFolder(mFolderName);
			boolean flag = true;
			for (int i = 0; i < mMusicList.size(); i++) {
				if (musicPlayService.getCurrentMusicBean().getId() == mMusicList.get(i).getId()) {
					musicPlayService.setCurrentMusicIndex(i);
					flag = false;
					break;
				}
			}
			if (flag) {
				musicPlayService.setCurrentMusicIndex(0);
				musicPlayService.setCurrentMusicBean(mMusicList.get(0));
			}
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		menu.add(mMusicList.get(info.position).getDisplayName());
		menu.add(Menu.NONE, MENU_DELETE, 1, "删除");
		menu.add(Menu.NONE, MENU_DETAIL, 2, "歌曲信息");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final int position = ((AdapterContextMenuInfo) item.getMenuInfo()).position;
		final MusicBean musicBean = mMusicList.get(position);
		switch (item.getItemId()) {
		case MENU_DELETE:
			View view  = View.inflate(this, R.layout.dialog_music_delete, null);
			
			final Dialog deleteDialog = createDialog(view);
			
			((TextView)view.findViewById(R.id.tv_delete_sure)).setText(musicBean.getDisplayName());
			final CheckBox delFileCheckBox = (CheckBox)view.findViewById(R.id.cb_delete_file);
			((Button)view.findViewById(R.id.btn_delete_sure)).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					boolean isDeleteFile = delFileCheckBox.isChecked();
					MusicManagerUtil.deleteMusic(ShowListActivity.this, musicBean, isDeleteFile);
					mMusicList.remove(position);
					mShowAdapter.notifyDataSetChanged();
					removeMusic(musicBean);
					deleteDialog.cancel();
				}
			});
			((Button)view.findViewById(R.id.btn_delete_cancel)).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					deleteDialog.cancel();
				}
			});
			break;
		case MENU_DETAIL:
			musicInfoDetail(musicBean);
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	/** 
	 * 删除内存中的数据
	 * @param musicBean
	 */
	private void removeMusic(final MusicBean musicBean) {
		List<MusicBean> list = musicPlayService.getMusicList();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getId() == musicBean.getId()) {
				list.remove(i);
				break;
			}
		}
		list = musicPlayService.getMusicInfoMap().get(musicPlayService.getCurrentPlayFolder());
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getId() == musicBean.getId()) {
				list.remove(i);
				break;
			}
		}
		list = musicPlayService.getFavoriteMusicList();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getId() == musicBean.getId()) {
				list.remove(i);
				break;
			}
		}
	}
	
	/**
	 * 查看music的详细信息
	 * @param musicBean
	 */
	private void musicInfoDetail(final MusicBean musicBean) {
		View view = View.inflate(this, R.layout.dialog_music_detail, null);

		final Dialog detailDialog = createDialog(view);
		
		final EditText nameEditText = (EditText)view.findViewById(R.id.et_detail_name);
		final EditText artistEditText = (EditText)view.findViewById(R.id.et_detail_artist);
		final EditText albumEditText = (EditText)view.findViewById(R.id.et_detail_album);
		nameEditText.setText(musicBean.getDisplayName());
		artistEditText.setText(musicBean.getArtist());
		albumEditText.setText(musicBean.getAlbum());
		((TextView)view.findViewById(R.id.tv_detail_duration))
			.setText(TimeUtil.getTime(musicBean.getDuration()));
		int size = musicBean.getSize() / 1024;
		((TextView)view.findViewById(R.id.tv_detail_size))
			.setText(size/1024 + "." + size % 1024 / 10 + "M");
		((TextView)view.findViewById(R.id.tv_detail_path)).setText(musicBean.getPath());
		((Button)view.findViewById(R.id.btn_detial_save)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String OldDisplayName = musicBean.getDisplayName();
				musicBean.setDisplayName(nameEditText.getText().toString().trim());
				musicBean.setArtist(artistEditText.getText().toString().trim());
				musicBean.setAlbum(albumEditText.getText().toString().trim());
				mShowAdapter.notifyDataSetChanged();
				setBottomView();
				MusicManagerUtil.updateMusicInfo(ShowListActivity.this, musicBean, OldDisplayName);
				Toast.makeText(ShowListActivity.this, "保存成功", Toast.LENGTH_LONG).show();
				detailDialog.cancel();
			}
		});
		((Button)view.findViewById(R.id.btn_detial_cancel)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				detailDialog.cancel();
			}
		});
	}

	/** 
	 * 自定义对话框
	 * @param view
	 * @return
	 */
	private Dialog createDialog(View view) {
//		final AlertDialog dialog = new AlertDialog.Builder(this).create();
		Dialog dialog = new Dialog(this, R.style.CustomDialogStyle);
		dialog.show();
		Window window = dialog.getWindow();
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		window.setContentView(view);
		WindowManager.LayoutParams  lp= window.getAttributes();  
		lp.width=screenWidth - 50;//定义宽度  
		lp.height=LayoutParams.WRAP_CONTENT;//定义高度  
		window.setAttributes(lp);
		return dialog;
	}
	
}
