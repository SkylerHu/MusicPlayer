package com.anjoyo.musicplayer.define;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

public class MarqueeTextView extends TextView {

	public MarqueeTextView(Context context) {
		super(context);
//		android:ellipsize="marquee" 
//      android:focusable="true" 
//      android:marqueeRepeatLimit="marquee_forever" 
//      android:focusableInTouchMode="true" 
//      android:scrollHorizontally="true"
////      android:singleLine="true"
//		setSingleLine(true);
//		setEllipsize(TruncateAt.MARQUEE);
//		setFocusable(true);
//		setMarqueeRepeatLimit(Integer.MAX_VALUE);
//		setFocusableInTouchMode(true);
	}
	
	public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MarqueeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		if (focused) {
			super.onFocusChanged(focused, direction, previouslyFocusedRect);
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		if (hasWindowFocus) {
			super.onWindowFocusChanged(hasWindowFocus);
		}
	}
	
	@Override
	public boolean isFocused() {
		return true;
	}
	
}
