package com.anjoyo.musicplayer.define;

import java.util.List;

import com.anjoyo.musicplayer.bean.LyricBean;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.widget.TextView;

public class LyricView extends TextView {

	/** 控件的高 */
	private float mHeight;
	/** 控件的宽 */
	private float mWidth;
	private Paint mPaint;
	private Paint prePaint;
	private Paint nextPaint;
	
	/** 当前歌曲的歌词 */
	private List<LyricBean> lyricBeanList;
	/** 歌词进度，List中的位置 */
	private int index;
	
	private float heightSign;
	private boolean start;
	
	private Shader inShader = new LinearGradient(0, 0, 0, LYRIC_ONE_HEIGHT, 
			Color.argb(0xcc, 0xcc, 0xcc, 0xcc), 
			Color.argb(0xff, 0xff, 0xff, 0xff), TileMode.MIRROR);
	private Shader outShader = new LinearGradient(0, LYRIC_ONE_HEIGHT, 0, 0, 
			Color.argb(0xcc, 0xcc, 0xcc, 0xcc), 
			Color.argb(0xff, 0xff, 0xff, 0xff), TileMode.MIRROR);
	
	/** 歌词显示的字体大小 */
	private static final int LYRIC_TEXT_SIZE = 36;
	/** 每行歌词的高度 */
	private static final int LYRIC_ONE_HEIGHT = 60;
	
	public LyricView(Context context) {
		super(context);
		initView();
	}

	public LyricView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public LyricView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
		
		new UpdateThread().start();
	}
	
	private void initView() {
		setFocusable(true);
		 
		mPaint = new Paint();
		mPaint.setAntiAlias(true);// 设置画笔的锯齿效果
		mPaint.setTextAlign(Align.CENTER);
		mPaint.setTextSize(LYRIC_TEXT_SIZE);
		mPaint.setTypeface(Typeface.SERIF);
		
		prePaint = new Paint(mPaint);
		prePaint.setShader(outShader);
		nextPaint = new Paint(mPaint);
		nextPaint.setShader(inShader);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (canvas == null) {
			return;
		}
		mPaint.setColor(Color.argb(0xff, 0xff, 0xff, 0xff));//当前歌词词句颜色
		if (lyricBeanList == null || lyricBeanList.size() < 1 || index > lyricBeanList.size() - 1) {
			canvas.drawText("好音质，MusicPlayer！", mWidth / 2, mHeight / 2, mPaint);
			return;
		}
		canvas.drawText(lyricBeanList.get(index).getLyric(), mWidth / 2, heightSign, mPaint);
		
		mPaint.setColor(Color.argb(0xcc, 0xcc, 0xcc, 0xcc));//非当前歌词词句颜色
		
		float currentY = heightSign;
		//画出本句之前的歌词
		for (int i = index - 1; i >= 0; i--) {
			currentY -= LYRIC_ONE_HEIGHT;
			if (i == index - 1) {
				canvas.drawText(lyricBeanList.get(i).getLyric(), mWidth / 2,
						currentY, prePaint);
			} else {
				canvas.drawText(lyricBeanList.get(i).getLyric(), mWidth / 2,
						currentY, mPaint);
			}
		}
		currentY = heightSign;
		//画出本句之后的歌词
		for (int i = index + 1; i < lyricBeanList.size(); i++) {
			currentY += LYRIC_ONE_HEIGHT;
			if (i == index + 1) {
				canvas.drawText(lyricBeanList.get(i).getLyric(), mWidth / 2,
						currentY, nextPaint);	
			} else {
				canvas.drawText(lyricBeanList.get(i).getLyric(), mWidth / 2,
						currentY, mPaint);		
			}
		}
	}
	
	public void setLyricList(List<LyricBean> lyricBeanList) {
		this.lyricBeanList = lyricBeanList;
	}
	
	public void setStart(boolean start) {
		this.start = start;
	}
	
	public boolean getStart() {
		return this.start;
	}
	
	public void setIndex(int index) {
		if (this.index != index) {
			this.index = index;
			heightSign = mHeight / 2 + LYRIC_ONE_HEIGHT / 2;
		}
	}
	
	private class UpdateThread extends Thread {
		@Override
		public void run() {
			while (true) {
				if (start && lyricBeanList.size() > index + 1) {
					if (heightSign > mHeight / 2f - LYRIC_ONE_HEIGHT / 2f) {
						heightSign -= LYRIC_ONE_HEIGHT / 
								(float)(lyricBeanList.get(index + 1).getLyricTime() 
										- lyricBeanList.get(index).getLyricTime()) * 50;
					}
					postInvalidate();
				}
				try {
					sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mWidth = w;
		mHeight = h;
		heightSign = h / 2f + LYRIC_ONE_HEIGHT / 2f;
	}

	
}
