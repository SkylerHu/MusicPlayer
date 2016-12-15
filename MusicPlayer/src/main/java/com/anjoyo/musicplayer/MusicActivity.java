package com.anjoyo.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.anjoyo.musicplayer.adapter.MusicGridViewAdapter;
import com.anjoyo.musicplayer.adapter.ViewPagerAdapter;
import com.anjoyo.musicplayer.define.AbstractActivity;
import com.anjoyo.musicplayer.define.Constant;
import com.anjoyo.musicplayer.util.AdapterListUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MusicActivity extends AbstractActivity {

	private RadioGroup mRadioGroup;
	/** ViewPager上面的滑动条 */
	private View mPositionView;
	private ViewPager mMusicViewPager;
	private List<View> mViewList;
	private ViewPagerAdapter mPagerAdapter;
	/** 滑动条mPositionView的位置 */
	private int mPositionViewCurrent;
	/** ViewPager当前选中页面 */
	private int mPagerSelected;

	private GridView mGridView;
	private List<Map<String, Object>> mDataList;
	private MusicGridViewAdapter mGridViewAdapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void loadLayout() {
		setContentView(R.layout.activity_music);
	}
	
	@Override
	protected void initView() {
		mRadioGroup = (RadioGroup) findViewById(R.id.rg_music_head);
		mPositionView = findViewById(R.id.view_music_position);
		//设置宽度为屏幕的1/3
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(screenWidth / 3, dip2px(3));
		params.topMargin = dip2px(57);
		mPositionView.setLayoutParams(params);
		
		mMusicViewPager = (ViewPager) findViewById(R.id.vp_music);
		mViewList = new ArrayList<View>();
		View mineView = View.inflate(this, R.layout.music_pager_mine, null);
		initMineView(mineView);
		mViewList.add(mineView);
		View lookView = View.inflate(this, R.layout.music_pager_look, null);
		initLookView(lookView);
		mViewList.add(lookView);
		View searchView = View.inflate(this, R.layout.music_pager_search, null);
		initSearchView(searchView);
		mViewList.add(searchView);
		
	}

	private int dip2px(int dpValue) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
	}
	
	//ViewPager的第一个页面
	private void initMineView(View v) {
		mGridView = (GridView)v.findViewById(R.id.gv_music_mine);
		mDataList = AdapterListUtil.initMusicGridViewList(this, musicPlayService);
		mGridViewAdapter = new MusicGridViewAdapter(this, mDataList, musicPlayService);
		mGridView.setAdapter(mGridViewAdapter);
		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String itemName = (String) mDataList.get(position).get(Constant.KEY_GV_ITEM_NAME);
				if (Constant.MUSIC_PAGER_GV_LOCAL.equals(itemName)) {
					Intent intent = new Intent(MusicActivity.this, ShowListActivity.class);
					intent.putExtra(Constant.INTENT_MUSIC_FOLDER_NAME, Constant.MUSIC_PAGER_GV_LOCAL);
					startActivity(intent);
				} else if (Constant.MUSIC_PAGER_GV_FAV.equals(itemName)) {
					Intent intent = new Intent(MusicActivity.this, ShowListActivity.class);
					intent.putExtra(Constant.INTENT_MUSIC_FOLDER_NAME, Constant.MUSIC_PAGER_GV_FAV);
					startActivity(intent);
				} else if (Constant.MUSIC_PAGER_GV_FOLDER.equals(itemName)) {
					Intent intent = new Intent(MusicActivity.this, FolderActivity.class);
					startActivity(intent);
				}
			}
		});
	}
	
	//ViewPager的第二个页面
	private void initLookView(View v) {
		
	}
	
	//ViewPager的第三个页面
	private void initSearchView(View v) {
		
	}
	
	@Override
	protected void setView() {
		
		setMusicViewPager();
		
		mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rbn_music_mine:
					mMusicViewPager.setCurrentItem(0, false);
					break;
				case R.id.rbn_music_look:
					mMusicViewPager.setCurrentItem(1, false);
					break;
				case R.id.rbn_music_search:
					mMusicViewPager.setCurrentItem(2, false);
					break;
				default:
					break;
				}
				int toXValue = mPagerSelected * screenWidth / 3;
				startAnimation(toXValue);
			}
		});
	}

	private void startAnimation(int toXValue) {
		mPositionView.clearAnimation();
		TranslateAnimation animation = 
				new TranslateAnimation(mPositionViewCurrent, toXValue, 0, 0);
		animation.setFillAfter(true);
		mPositionViewCurrent = toXValue;
		mPositionView.setAnimation(animation);
	}
	
	private void setMusicViewPager() {
		mPagerAdapter = new ViewPagerAdapter(mViewList);
		mMusicViewPager.setAdapter(mPagerAdapter);
		mMusicViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			private int sign;
			@Override
			public void onPageSelected(int arg0) {
				mPagerSelected = arg0;
//				Log.v(TAG, "mPagerSelected = " + mPagerSelected);
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// arg0百分比参照的页面,arg1滑动百分比,arg2滑动像素
//				Log.v(TAG, "arg2 = " + arg2);
				if (sign == 0) {
					int toXValue = mPagerSelected * screenWidth / 3;
					startAnimation(toXValue);
				} else {
					int toXValue = mPagerSelected * screenWidth / 3 + arg2 / 3;
					if (toXValue - mPositionViewCurrent > screenWidth / 3 - 50) {
						toXValue -= screenWidth / 3;
					}
					startAnimation(toXValue);
				}
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
				//1按下，2松手，0无动作
				sign = arg0;
			}
		});
	}
	
	@Override
	public void onMusicRefresh() {
		List<Map<String, Object>> list = AdapterListUtil.initMusicGridViewList(this, musicPlayService);
		mDataList.clear();
		mDataList.addAll(list);
		mGridViewAdapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		onMusicRefresh();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent=new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

}
