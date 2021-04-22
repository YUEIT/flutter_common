package cn.yue.base.common.widget.wheel;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.ColorInt;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.util.LinkedList;
import java.util.List;


// TODO: Auto-generated Javadoc

/**
 * Numeric wheel view.
 */
public class WheelView extends View {

	/** The tag. */
	private static String TAG = "WheelView";

	/** The Constant D. */

	/** The m context. */
	private Context mContext = null;

	/** Scrolling duration. */
	private static final int SCROLLING_DURATION = 400;

	/** Minimum delta for scrolling. */
	private static final int MIN_DELTA_FOR_SCROLLING = 1;

	/** Items text color. */
	private static final int ITEMS_TEXT_COLOR = 0xFF999999;

	private int itemTextColor;

	/** Top and bottom shadows colors. */
	// private static int[] SHADOWS_COLORS = new int[] { 0xFF111111, 0x00AAAAAA,
	// 0x00AAAAAA };
	private static int[] SHADOWS_COLORS = new int[] { 0x00000000, 0x00000000, 0x00000000 };

	/** Left and right padding value. */
	private static final int PADDING = 5;

	/** Default count of visible items. */
	private static final int DEF_VISIBLE_ITEMS = 5;

	// Wheel Values
	/** The adapter. */
	private AbWheelAdapter adapter = null;

	/** The current item. */
	private int currentItem = 0;

	// Widths
	/** The items width. */
	private int itemsWidth = 0;

	// Count of visible items
	/** The visible items. */
	private int visibleItems = DEF_VISIBLE_ITEMS;

	// Item height
	/** The item height. */
	private int itemHeight = 0;

	// Text paints
	/** The items paint. */
	private TextPaint itemsPaint;

	/** The value paint. */
	private TextPaint valuePaint;

	// Layouts
	/** The items layout. */
	private StaticLayout itemsLayout;


	/** The value layout. */
	private StaticLayout valueLayout;


	// Scrolling
	/** The is scrolling performed. */
	private boolean isScrollingPerformed;

	/** The scrolling offset. */
	private int scrollingOffset;

	// Scrolling animation
	/** The gesture detector. */
	private GestureDetector gestureDetector;

	/** The scroller. */
	private Scroller scroller;

	/** The last scroll y. */
	private int lastScrollY;

	// Cyclic
	/** The is cyclic. */
	boolean isCyclic = false;

	// Listeners
	/** The changing listeners. */
	private List<AbOnWheelChangedListener> changingListeners = new LinkedList<AbOnWheelChangedListener>();

	/** The scrolling listeners. */
	private List<AbOnWheelScrollListener> scrollingListeners = new LinkedList<AbOnWheelScrollListener>();


	/** 中间覆盖条的颜色，如果没有设置centerDrawable时才生效. */
	private int[] centerSelectGradientColors = new int[] { 0x10a1e0, 0x10a1e0, 0x10a1e0 };

	/** The center select stroke width. */
	private int centerSelectStrokeWidth = 1;

	/** The center select stroke color. */
	private int centerSelectStrokeColor = 0xFFEFEFEF;

	/** Shadows drawables. */
	private GradientDrawable topShadow;

	/** The bottom shadow. */
	private GradientDrawable bottomShadow;

	/** Current value. */
	private int valueTextColor = 0xF0000000;


	// 轮子的背景 底部的颜色
	/** The bottom gradient colors. */
	private int[] bottomGradientColors = new int[] { 0x00000000, 0x00000000, 0x00000000 };
	// private int[] bottomGradientColors = null;
	// 轮子的背景 顶部的颜色
	/** The top gradient colors. */
	private int[] topGradientColors = new int[] { 0x00000000, 0x00000000, 0x00000000 };
	// private int[] topGradientColors = null;

	/** The top stroke width. */
	private int topStrokeWidth = 1;

	/** The top stroke color. */
	private int topStrokeColor = 0x00000000;

	/** 值的文字大小. */
	private int valueTextSize = 20;


	/** Top and bottom items offset. */
	private int itemOffset = valueTextSize / 5;

	/** 行间距. */
	private int additionalItemHeight = 80;

	/** 屏幕宽度. */
	private int screenWidth = 0;

	/** 屏幕高度. */
	private int screenHeight = 0;

	/**
	 * Constructor.
	 *
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 * @param defStyle
	 *            the def style
	 */
	public WheelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initData(context);
	}

	/**
	 * Constructor.
	 *
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 */
	public WheelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData(context);
	}

	/**
	 * Constructor.
	 *
	 * @param context
	 *            the context
	 */
	public WheelView(Context context) {
		super(context);
		initData(context);
	}

	/**
	 * Initializes class data.
	 *
	 * @param context
	 *            the context
	 */
	private void initData(Context context) {
		mContext = context;
		gestureDetector = new GestureDetector(context, gestureListener);
		gestureDetector.setIsLongpressEnabled(false);
		scroller = new Scroller(context);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		screenWidth = displayMetrics.widthPixels;
		screenHeight = displayMetrics.heightPixels;
	}

	/**
	 * Gets wheel adapter.
	 *
	 * @return the adapter
	 */
	public AbWheelAdapter getAdapter() {
		return adapter;
	}

	/**
	 * Sets wheel adapter.
	 *
	 * @param adapter
	 *            the new wheel adapter
	 */
	public void setAdapter(AbWheelAdapter adapter) {
		this.adapter = adapter;
		invalidateLayouts();
		invalidate();// 重绘
	}

	public void setData(List list) {
		if(adapter != null) {
			adapter.setData(list);
			invalidateLayouts();
			invalidate();
		}
	}

	public List getData() {
		if(adapter != null) {
			return adapter.getData();
		}
		return null;
	}

	/**
	 * Set the the specified scrolling interpolator.
	 *
	 * @param interpolator
	 *            the interpolator
	 */
	public void setInterpolator(Interpolator interpolator) {
		scroller.forceFinished(true);
		scroller = new Scroller(getContext(), interpolator);
	}

	/**
	 * Gets count of visible items.
	 *
	 * @return the count of visible items
	 */
	public int getVisibleItems() {
		return visibleItems;
	}

	/**
	 * Sets count of visible items.
	 *
	 * @param count
	 *            the new count
	 */
	public void setVisibleItems(int count) {
		visibleItems = count;
		invalidate();
	}



	/**
	 * Adds wheel changing listener.
	 *
	 * @param listener
	 *            the listener
	 */
	public void addChangingListener(AbOnWheelChangedListener listener) {
		changingListeners.add(listener);
	}

	/**
	 * Removes wheel changing listener.
	 *
	 * @param listener
	 *            the listener
	 */
	public void removeChangingListener(AbOnWheelChangedListener listener) {
		changingListeners.remove(listener);
	}

	/**
	 * Notifies changing listeners.
	 *
	 * @param oldValue
	 *            the old wheel value
	 * @param newValue
	 *            the new wheel value
	 */
	protected void notifyChangingListeners(int oldValue, int newValue) {
		for (AbOnWheelChangedListener listener : changingListeners) {
			listener.onChanged(this, oldValue, newValue);
		}
	}

	/**
	 * Adds wheel scrolling listener.
	 *
	 * @param listener
	 *            the listener
	 */
	public void addScrollingListener(AbOnWheelScrollListener listener) {
		scrollingListeners.add(listener);
	}

	/**
	 * Removes wheel scrolling listener.
	 *
	 * @param listener
	 *            the listener
	 */
	public void removeScrollingListener(AbOnWheelScrollListener listener) {
		scrollingListeners.remove(listener);
	}

	/**
	 * Notifies listeners about starting scrolling.
	 */
	protected void notifyScrollingListenersAboutStart() {
		for (AbOnWheelScrollListener listener : scrollingListeners) {
			listener.onScrollingStarted(this);
		}
	}

	/**
	 * Notifies listeners about ending scrolling.
	 */
	protected void notifyScrollingListenersAboutEnd() {
		for (AbOnWheelScrollListener listener : scrollingListeners) {
			listener.onScrollingFinished(this);
		}
	}

	/**
	 * Gets current value.
	 *
	 * @return the current value
	 */
	public int getCurrentItem() {
		return currentItem;
	}

	/**
	 * Sets the current item. Does nothing when index is wrong.
	 *
	 * @param index
	 *            the item index
	 * @param animated
	 *            the animation flag
	 */
	public void setCurrentItem(int index, boolean animated) {
		if (adapter == null || adapter.getItemsCount() == 0) {
			return; // throw?
		}
		if (index < 0 || index >= adapter.getItemsCount()) {
			if (isCyclic) {
				while (index < 0) {
					index += adapter.getItemsCount();
				}
				index %= adapter.getItemsCount();
			} else {
				return; // throw?
			}
		}
		if (index != currentItem) {
			if (animated) {
				scroll(index - currentItem, SCROLLING_DURATION);
			} else {
				invalidateLayouts();

				int old = currentItem;
				currentItem = index;

				notifyChangingListeners(old, currentItem);

				invalidate();
			}
		}
	}

	/**
	 * Sets the current item w/o animation. Does nothing when index is wrong.
	 *
	 * @param index
	 *            the item index
	 */
	public void setCurrentItem(int index) {
		setCurrentItem(index, false);
	}

	/**
	 * Tests if wheel is cyclic. That means before the 1st item there is shown
	 * the last one
	 *
	 * @return true if wheel is cyclic
	 */
	public boolean isCyclic() {
		return isCyclic;
	}

	/**
	 * Set wheel cyclic flag.
	 *
	 * @param isCyclic
	 *            the flag to set
	 */
	public void setCyclic(boolean isCyclic) {
		this.isCyclic = isCyclic;

		invalidate();
		invalidateLayouts();
	}

	/**
	 * Invalidates layouts.
	 */
	private void invalidateLayouts() {
		itemsLayout = null;
		valueLayout = null;
		scrollingOffset = 0;
	}

	private int itemTextSize = 35;

	public int getItemTextSize() {
		return itemTextSize;
	}

	public void setItemTextSize(int itemTextSize) {
		this.itemTextSize = AbViewUtils.resizeTextSize(screenWidth, screenHeight, itemTextSize);
	}

	public int getItemsTextColor() {
		if(itemTextColor != 0){
			return itemTextColor;
		}
		return ITEMS_TEXT_COLOR;
	}

	public void setItemTextColor(@ColorInt int color) {
		this.itemTextColor = color;
	}
	/**
	 * Initializes resources.
	 */
	private void initResourcesIfNecessary() {
		if (itemsPaint == null) {
			itemsPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
			itemsPaint.setTextSize(valueTextSize);
		}

		if (valuePaint == null) {
			valuePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
			valuePaint.setTextSize(valueTextSize);
		}

		/*
		 * Android中提供了Shader类专门用来渲染图像以及一些几何图形， Shader下面包括几个直接子类，分别是BitmapShader、
		 * ComposeShader、LinearGradient、 RadialGradient、SweepGradient。
		 * BitmapShader主要用来渲染图像， LinearGradient 用来进行梯度渲染，RadialGradient
		 * 用来进行环形渲染， SweepGradient 用来进行梯度渲染，ComposeShader则是一个
		 * 混合渲染，可以和其它几个子类组合起来使用。
		 */

		// 上边界渐变层
		if (topShadow == null) {
			topShadow = new GradientDrawable(Orientation.TOP_BOTTOM, SHADOWS_COLORS);
		}
		// 下边界渐变层
		if (bottomShadow == null) {
			bottomShadow = new GradientDrawable(Orientation.BOTTOM_TOP, SHADOWS_COLORS);
		}

		if (this.getBackground() == null) {
			// 原来用颜色渐变实现setBackgroundDrawable(layerDrawable);
			// 底部的颜色
			GradientDrawable mGradientDrawable1 = new GradientDrawable(Orientation.TOP_BOTTOM, topGradientColors);
			GradientDrawable mGradientDrawable2 = new GradientDrawable(Orientation.BOTTOM_TOP, bottomGradientColors);

			mGradientDrawable1.setStroke(topStrokeWidth, topStrokeColor);
			mGradientDrawable1.setShape(GradientDrawable.RECTANGLE);
			mGradientDrawable2.setShape(GradientDrawable.RECTANGLE);
			mGradientDrawable1.setGradientType(GradientDrawable.LINEAR_GRADIENT);
			mGradientDrawable2.setGradientType(GradientDrawable.LINEAR_GRADIENT);

			GradientDrawable[] mDrawables = new GradientDrawable[2];
			mDrawables[0] = mGradientDrawable1;
			mDrawables[1] = mGradientDrawable2;

			LayerDrawable layerDrawable = new LayerDrawable(mDrawables);
			layerDrawable.setLayerInset(0, 0, 0, 0, 0); // 第一个参数0代表数组的第1个元素
			layerDrawable.setLayerInset(1, 4, 1, 4, 1); // 第一个参数1代表数组的第2个元素
			setBackgroundDrawable(layerDrawable);
		}

	}

	/**
	 * Calculates desired height for layout.
	 *
	 * @param layout
	 *            the source layout
	 * @return the desired layout height
	 */
	private int getDesiredHeight(Layout layout) {
		if (layout == null) {
			return 0;
		}

		int desired = getItemHeight() * visibleItems;

		// Check against our minimum height
		desired = Math.max(desired, getSuggestedMinimumHeight());

		return desired;
	}

	/**
	 * Returns text item by index.
	 *
	 * @param index
	 *            the item index
	 * @return the item or null
	 */
	private String getTextItem(int index) {
		if (adapter == null || adapter.getItemsCount() == 0) {
			return null;
		}
		int count = adapter.getItemsCount();
		if ((index < 0 || index >= count) && !isCyclic) {
			return null;
		} else {
			while (index < 0) {
				index = count + index;
			}
		}

		index %= count;
		return calculateShowStr(adapter.getItem(index));
	}

	private String calculateShowStr(String s) {
		if (AbGraphical.getStringWidth(s, valuePaint) < getWidth()) {
			return s;
		}
		float enWidth = (int) AbGraphical.getStringWidth("a", valuePaint);
		float cnWidth = (int) AbGraphical.getStringWidth("哈", valuePaint);
		float countWith = 0;
		int num = s.length();
		for (int i = 0; i < s.length(); i++) {
			String temp = s.substring(i, i + 1);
			String chinese = "[\u0391-\uFFE5]";
			if (temp.matches(chinese)) {
				countWith += cnWidth;
			} else {
				countWith += enWidth;
			}
			if (countWith >= getWidth()) {
				num = i;
				break;
			}
		}
		return s.substring(0, num);
	}

	/**
	 * Builds text depending on current value.
	 *
	 * @param useCurrentValue
	 *            the use current value
	 * @return the text
	 */
	private String buildText(boolean useCurrentValue) {
		StringBuilder itemsText = new StringBuilder();
		int addItems = visibleItems / 2 + 1;

		for (int i = currentItem - addItems; i <= currentItem + addItems; i++) {
			if (useCurrentValue || i != currentItem) {
				String text = getTextItem(i);
				if (text != null) {
					itemsText.append(text);
				}
			}
			if (i < currentItem + addItems) {
				itemsText.append("\n");
			}
		}

		return itemsText.toString();
	}

	/**
	 * Returns the max item length that can be present.
	 *
	 * @return the max length
	 */
	private int getMaxTextLength() {
		AbWheelAdapter adapter = getAdapter();
		if (adapter == null) {
			return 0;
		}

		int adapterLength = adapter.getMaximumLength();
		if (adapterLength > 0) {
			return adapterLength;
		} else {
			return 0;
		}
	}

	/**
	 * Returns height of wheel item.
	 *
	 * @return the item height
	 */
	private int getItemHeight() {
		if (itemHeight != 0) {
			return itemHeight;
		} else if (itemsLayout != null && itemsLayout.getLineCount() > 2) {
			itemHeight = itemsLayout.getLineTop(2) - itemsLayout.getLineTop(1);
			return itemHeight;
		}

		return getHeight() / visibleItems;
	}

	/**
	 * Calculates control width and creates text layouts.
	 *
	 * @param widthSize
	 *            the input layout width
	 * @param mode
	 *            the layout mode
	 * @return the calculated control width
	 */
	private int calculateLayoutWidth(int widthSize, int mode) {
		initResourcesIfNecessary();

		int width = widthSize;

		int maxLength = getMaxTextLength();
		if (maxLength > 0) {
			// 一个字符宽度
			float textWidth = (int) AbGraphical.getStringWidth("0", valuePaint);
			itemsWidth = (int) (maxLength * textWidth);
		} else {
			itemsWidth = 0;
		}

		boolean recalculate = false;
		if (mode == MeasureSpec.EXACTLY) {
			width = widthSize;
			recalculate = true;
		} else {
			width = itemsWidth + 2 * PADDING;

			// Check against our minimum width
			width = Math.max(width, getSuggestedMinimumWidth());

			if (mode == MeasureSpec.AT_MOST && widthSize < width) {
				width = widthSize;
				recalculate = true;
			}
		}

		if (recalculate) {
			// recalculate width
			int pureWidth = width - 2 * PADDING;
			if (pureWidth <= 0) {
				itemsWidth = 0;
			}
			itemsWidth = pureWidth;
		}

		if (itemsWidth > 0) {
			createLayouts(itemsWidth);
		}

		return width;
	}

	/**
	 * Creates layouts.
	 *
	 * @param widthItems
	 *            width of items layout
	 *            width of label layout
	 */
	private void createLayouts(int widthItems) {

		//选中Item
		if (!isScrollingPerformed && (valueLayout == null || valueLayout.getWidth() > widthItems)) {
			String text = getAdapter() != null ? calculateShowStr(getAdapter().getItem(currentItem)) : null;
			valueLayout = new StaticLayout(text != null ? text : "",
					valuePaint,
					widthItems,
					Layout.Alignment.ALIGN_CENTER,
					1.0f,
					additionalItemHeight,
					false);
		} else if (isScrollingPerformed) {
			valueLayout = null;
		} else {
			valueLayout.increaseWidthTo(widthItems);
		}

		//Item
		if (itemsLayout == null || itemsLayout.getWidth() > widthItems) {
			String text = getAdapter() != null ? getAdapter().getItem(currentItem) : null;
			itemsLayout = new StaticLayout(buildText(isScrollingPerformed),
					itemsPaint,
					widthItems,
					Layout.Alignment.ALIGN_CENTER,
					1.0f,
					additionalItemHeight,
					false);
		} else {
			itemsLayout.increaseWidthTo(widthItems);
		}
	}

	/**
	 * 描述：TODO.
	 *
	 * @param widthMeasureSpec
	 *            the width measure spec
	 * @param heightMeasureSpec
	 *            the height measure spec
	 * @see View#onMeasure(int, int)
	 * @author: zhaoqp
	 * @date：2013-6-17 上午9:04:47
	 * @version v1.0
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int width = calculateLayoutWidth(widthSize, widthMode);

		int height;
		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else {
			height = getDesiredHeight(itemsLayout);

			if (heightMode == MeasureSpec.AT_MOST) {
				height = Math.min(height, heightSize);
			}
		}
		setMeasuredDimension(width, height);
	}

	/**
	 * 描述：TODO.
	 *
	 * @param canvas
	 *            the canvas
	 * @see View#onDraw(Canvas)
	 * @author: zhaoqp
	 * @date：2013-6-17 上午9:04:47
	 * @version v1.0
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (itemsLayout == null) {
			if (itemsWidth == 0) {
				calculateLayoutWidth(getWidth(), MeasureSpec.EXACTLY);
			} else {
				createLayouts(itemsWidth);
			}
		}

		if (itemsWidth > 0) {
			canvas.save();
			// Skip padding space and hide a part of top and bottom items
			canvas.translate(PADDING, -itemOffset);
			drawItems(canvas);
			drawValue(canvas);
			canvas.restore();
		}
		if (isDrawCenterRect) {
			drawCenterRect(canvas);
		}
		if (isDrawShadows) {
			drawShadows(canvas);
		}

	}

	/**
	 * Draws shadows on top and bottom of control.
	 *
	 * @param canvas
	 *            the canvas for drawing
	 */
	private void drawShadows(Canvas canvas) {
		if (topShadow != null) {
			topShadow.setBounds(0, 0, getWidth(), getHeight() / visibleItems);
			topShadow.draw(canvas);
		}

		if (bottomShadow != null) {
			bottomShadow.setBounds(0, getHeight() - getHeight() / visibleItems, getWidth(), getHeight());
			bottomShadow.draw(canvas);
		}

	}

	private boolean isDrawShadows = false;// 是否绘制上下阴影

	public boolean isDrawShadows() {
		return isDrawShadows;
	}

	public void setDrawShadows(boolean isDrawShadows) {
		this.isDrawShadows = isDrawShadows;
	}

	private boolean isDrawCenterRect = true;// 是否绘制中间举行

	public boolean isDrawCenterRect() {
		return isDrawCenterRect;
	}

	public void setDrawCenterRect(boolean isDrawCenterRect) {
		this.isDrawCenterRect = isDrawCenterRect;
	}

	/**
	 * Draws value and label layout.
	 *
	 * @param canvas
	 *            the canvas for drawing
	 */
	private void drawValue(Canvas canvas) {
		valuePaint.setColor(valueTextColor);
		valuePaint.drawableState = getDrawableState();
		Rect bounds = new Rect();
		itemsLayout.getLineBounds(visibleItems / 2, bounds);
		// draw current value
		if (valueLayout != null) {
			canvas.save();
			canvas.translate(0, bounds.top + scrollingOffset + 40);
			valueLayout.draw(canvas);
			canvas.restore();
		}
	}
	private void drawItems(Canvas canvas) {
		canvas.save();
		int top = itemsLayout.getLineTop(1) - 40;
		canvas.translate(0, -top + scrollingOffset);
		itemsPaint.setColor(getItemsTextColor());
		itemsPaint.drawableState = getDrawableState();
		itemsLayout.draw(canvas);
		canvas.restore();
	}

	/**
	 * Draws rect for current value.
	 *
	 * @param canvas
	 *            the canvas for drawing
	 */
	private void drawCenterRect(Canvas canvas) {
		final int itemHeight = getItemHeight();
		final int itemWidth = getWidth();
		//设置divider
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(centerSelectStrokeColor);
		for (int i = 1; i < DEF_VISIBLE_ITEMS; i++){
			Rect rect = new Rect();
			rect.bottom = itemHeight * i;
			canvas.drawLine(0, itemHeight * i, itemWidth, itemHeight * i, paint);
		}
	}

	private boolean touchable = true;
	public void setTouchable(boolean touchable) {
		this.touchable = touchable;
	}

	/**
	 * 描述：TODO.
	 *
	 * @param event
	 *            the event
	 * @return true, if successful
	 * @see View#onTouchEvent(MotionEvent)
	 * @author: zhaoqp
	 * @date：2013-6-17 上午9:04:47
	 * @version v1.0
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		AbWheelAdapter adapter = getAdapter();
		if (adapter == null) {
			return true;
		}

		if (touchable && !gestureDetector.onTouchEvent(event) && event.getAction() == MotionEvent.ACTION_UP) {
			justify();
		}
		return true;
	}

	/**
	 * Scrolls the wheel.
	 *
	 * @param delta
	 *            the scrolling value
	 */
	private void doScroll(int delta) {
		scrollingOffset += delta;

		int count = scrollingOffset / getItemHeight();
		int pos = currentItem - count;
		if (isCyclic && adapter.getItemsCount() > 0) {
			// fix position by rotating
			while (pos < 0) {
				pos += adapter.getItemsCount();
			}
			pos %= adapter.getItemsCount();
		} else if (isScrollingPerformed) {
			//
			if (pos < 0) {
				count = currentItem;
				pos = 0;
			} else if (pos >= adapter.getItemsCount()) {
				count = currentItem - adapter.getItemsCount() + 1;
				pos = adapter.getItemsCount() - 1;
			}
		} else {
			// fix position
			pos = Math.max(pos, 0);
			pos = Math.min(pos, adapter.getItemsCount() - 1);
		}

		int offset = scrollingOffset;
		if (pos != currentItem) {
			setCurrentItem(pos, false);
		} else {
			invalidate();
		}

		// update offset
		scrollingOffset = offset - count * getItemHeight();
		if (scrollingOffset > getHeight()) {
			scrollingOffset = scrollingOffset % getHeight() + getHeight();
		}
	}

	// gesture listener
	/** The gesture listener. */
	private SimpleOnGestureListener gestureListener = new SimpleOnGestureListener() {

		public boolean onDown(MotionEvent e) {
			if (isScrollingPerformed) {
				scroller.forceFinished(true);
				clearMessages();
				return true;
			}
			return false;
		}

		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			startScrolling();
			doScroll((int) -distanceY);
			return true;
		}

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			lastScrollY = currentItem * getItemHeight() + scrollingOffset;
			int maxY = isCyclic ? 0x7FFFFFFF : adapter.getItemsCount() * getItemHeight();
			int minY = isCyclic ? -maxY : 0;
			scroller.fling(0, lastScrollY, 0, (int) -velocityY / 2, 0, 0, minY, maxY);
			setNextMessage(MESSAGE_SCROLL);
			return true;
		}
	};

	// Messages
	/** The message scroll. */
	private final int MESSAGE_SCROLL = 0;

	/** The message justify. */
	private final int MESSAGE_JUSTIFY = 1;

	/**
	 * Set next message to queue. Clears queue before.
	 *
	 * @param message
	 *            the message to set
	 */
	private void setNextMessage(int message) {
		clearMessages();
		animationHandler.sendEmptyMessage(message);
	}

	/**
	 * Clears messages from queue.
	 */
	private void clearMessages() {
		animationHandler.removeMessages(MESSAGE_SCROLL);
		animationHandler.removeMessages(MESSAGE_JUSTIFY);
	}

	// animation handler
	/** The animation handler. */
	private Handler animationHandler = new Handler() {

		public void handleMessage(Message msg) {
			scroller.computeScrollOffset();
			int currY = scroller.getCurrY();
			int delta = lastScrollY - currY;
			lastScrollY = currY;
			if (delta != 0) {
				doScroll(delta);
			}

			// scrolling is not finished when it comes to final Y
			// so, finish it manually
			if (Math.abs(currY - scroller.getFinalY()) < MIN_DELTA_FOR_SCROLLING) {
				currY = scroller.getFinalY();
				scroller.forceFinished(true);
			}
			if (!scroller.isFinished()) {
				animationHandler.sendEmptyMessage(msg.what);
			} else if (msg.what == MESSAGE_SCROLL) {
				justify();
			} else {
				finishScrolling();
			}
		}
	};

	/**
	 * Justifies wheel.
	 */
	private void justify() {
		if (adapter == null) {
			return;
		}

		lastScrollY = 0;
		int offset = scrollingOffset;
		int itemHeight = getItemHeight();
		boolean needToIncrease = offset > 0 ? currentItem < adapter.getItemsCount() : currentItem > 0;
		if ((isCyclic || needToIncrease) && Math.abs((float) offset) > (float) itemHeight / 2) {
			if (offset < 0)
				offset += itemHeight + MIN_DELTA_FOR_SCROLLING;
			else
				offset -= itemHeight + MIN_DELTA_FOR_SCROLLING;
		}
		if (Math.abs(offset) > MIN_DELTA_FOR_SCROLLING) {
			scroller.startScroll(0, 0, 0, offset, SCROLLING_DURATION);
			setNextMessage(MESSAGE_JUSTIFY);
		} else {
			finishScrolling();
		}
	}

	/**
	 * Starts scrolling.
	 */
	private void startScrolling() {
		if (!isScrollingPerformed) {
			isScrollingPerformed = true;
			notifyScrollingListenersAboutStart();
		}
	}

	/**
	 * Finishes scrolling.
	 */
	void finishScrolling() {
		if (isScrollingPerformed) {
			notifyScrollingListenersAboutEnd();
			isScrollingPerformed = false;
		}
		invalidateLayouts();
		invalidate();
	}

	/**
	 * Scroll the wheel.
	 *
	 * @param itemsToScroll
	 *            the items to scroll
	 * @param time
	 *            scrolling duration
	 */
	public void scroll(int itemsToScroll, int time) {
		scroller.forceFinished(true);
		lastScrollY = scrollingOffset;
		int offset = itemsToScroll * getItemHeight();
		scroller.startScroll(0, lastScrollY, 0, offset - lastScrollY, time);
		setNextMessage(MESSAGE_SCROLL);
		startScrolling();
	}

	/**
	 * Sets the value text size.
	 *
	 * @param textSize
	 *            the new value text size
	 */
	public void setValueTextSize(int textSize) {
		this.valueTextSize = AbViewUtils.resizeTextSize(screenWidth, screenHeight, textSize);
		this.itemOffset = valueTextSize / 5;
	}

	/**
	 * Gets the center select gradient colors.
	 *
	 * @return the center select gradient colors
	 */
	public int[] getCenterSelectGradientColors() {
		return centerSelectGradientColors;
	}

	/**
	 * Sets the center select gradient colors.
	 *
	 * @param centerSelectGradientColors
	 *            the new center select gradient colors
	 */
	public void setCenterSelectGradientColors(int[] centerSelectGradientColors) {
		this.centerSelectGradientColors = centerSelectGradientColors;
	}

	/**
	 * Gets the center select stroke width.
	 *
	 * @return the center select stroke width
	 */
	public int getCenterSelectStrokeWidth() {
		return centerSelectStrokeWidth;
	}

	/**
	 * Sets the center select stroke width.
	 *
	 * @param centerSelectStrokeWidth
	 *            the new center select stroke width
	 */
	public void setCenterSelectStrokeWidth(int centerSelectStrokeWidth) {
		this.centerSelectStrokeWidth = centerSelectStrokeWidth;
	}

	/**
	 * Gets the center select stroke color.
	 *
	 * @return the center select stroke color
	 */
	public int getCenterSelectStrokeColor() {
		return centerSelectStrokeColor;
	}

	/**
	 * Sets the center select stroke color.
	 *
	 * @param centerSelectStrokeColor
	 *            the new center select stroke color
	 */
	public void setCenterSelectStrokeColor(int centerSelectStrokeColor) {
		this.centerSelectStrokeColor = centerSelectStrokeColor;
	}


	/**
	 * Sets the value text color.
	 *
	 * @param valueTextColor
	 *            the new value text color
	 */
	public void setValueTextColor(int valueTextColor) {
		this.valueTextColor = valueTextColor;
	}


	/**
	 * Sets the additional item height.
	 *
	 * @param additionalItemHeight
	 *            the new additional item height
	 */
	public void setAdditionalItemHeight(int additionalItemHeight) {
		this.additionalItemHeight = additionalItemHeight;
	}

}
