package com.anjoyo.musicplayer.define;

import com.anjoyo.musicplayer.R;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public abstract class AbstractListActivity extends AbstractActivity {

	protected ImageButton topSearchImageButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTopView();
	}
	
	private void setTopView() {
		((ImageButton)findViewById(R.id.ibtn_activity_top_back)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		topSearchImageButton = (ImageButton)findViewById(R.id.ibtn_activity_top_search);
	}
	
}
