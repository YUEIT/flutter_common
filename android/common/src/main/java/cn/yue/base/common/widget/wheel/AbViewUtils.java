package cn.yue.base.common.widget.wheel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

public final class AbViewUtils {

	/**
	 * 描述：重置AbsListView的高度.
	 * item 的最外层布局要用 RelativeLayout,如果计算的不准，就为RelativeLayout指定一个高度
	 * @param absListView the abs list view
	 * @param lineNumber 每行几个  ListView一行一个item
	 * @param verticalSpace the vertical space
	 */
	public static void setAbsListViewHeight(AbsListView absListView, int lineNumber, int verticalSpace) {
		
		int totalHeight = getAbsListViewHeight(absListView,lineNumber,verticalSpace);
		ViewGroup.LayoutParams params = absListView.getLayoutParams();
		params.height = totalHeight;
		((ViewGroup.MarginLayoutParams) params).setMargins(0, 0, 0, 0);
		absListView.setLayoutParams(params);
	}
	
	/**
	 * 描述：获取AbsListView的高度.
	 * @param absListView the abs list view
	 * @param lineNumber 每行几个  ListView一行一个item
	 * @param verticalSpace the vertical space
	 */
	public static int getAbsListViewHeight(AbsListView absListView, int lineNumber, int verticalSpace) {
		int totalHeight = 0;
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
	    int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
	    absListView.measure(w, h);
	    ListAdapter mListAdapter = absListView.getAdapter();
		if (mListAdapter == null) {
			return totalHeight;
		}
		
		int count = mListAdapter.getCount();
		if(absListView instanceof ListView){
			for (int i = 0; i < count; i++) {
				View listItem = mListAdapter.getView(i, null, absListView);
				listItem.measure(w, h);
				totalHeight += listItem.getMeasuredHeight();
			}
			if (count == 0) {
				totalHeight = verticalSpace;
			} else {
				totalHeight = totalHeight + (((ListView)absListView).getDividerHeight() * (count - 1));
			}
			
		}else if(absListView instanceof GridView){
			int remain = count % lineNumber;
			if(remain>0){
				remain = 1;
			}
			if(mListAdapter.getCount()==0){
				totalHeight = verticalSpace;
			}else{
				View listItem = mListAdapter.getView(0, null, absListView);
				listItem.measure(w, h);
				int line = count/lineNumber + remain;
				totalHeight = line*listItem.getMeasuredHeight()+(line-1)*verticalSpace;
			}
			
		}
		return totalHeight;

	}
	
	/**
	 * 测量这个view，最后通过getMeasuredWidth()获取宽度和高度.
	 *
	 * @param v 要测量的view
	 * @return 测量过的view
	 */
	public static void measureView(View v){
		if(v == null){
			return;
		}
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
	    int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
	    v.measure(w, h);
	}
	
	/**
	 * 描述：根据分辨率获得字体大小.
	 *
	 * @param screenWidth the screen width
	 * @param screenHeight the screen height
	 * @param textSize the text size
	 * @return the int
	 */
	public static int resizeTextSize(int screenWidth,int screenHeight,int textSize){
		float ratio =  1;
		try {
			float ratioWidth = (float)screenWidth / 480; 
			float ratioHeight = (float)screenHeight / 800; 
			ratio = Math.min(ratioWidth, ratioHeight);
		} catch (Exception e) {
		}
		return Math.round(textSize * ratio);
	}
	
	/**
	 * 
	 * 描述：dip转换为px
	 * @param context
	 * @param dipValue
	 * @return
	 * @throws 
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 
	 * 描述：px转换为dip
	 * @param context
	 * @param pxValue
	 * @return
	 * @throws 
	 */
	public static int px2dip(Context context, float pxValue) {
		if (null == context) return (int) pxValue;
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int sp2px(Context context, float spValue) {
		if (null == context) return (int) spValue;
		final float fontScale = context.getResources().getDisplayMetrics().density;
		return (int) (spValue * fontScale + 0.5f);
	}
	
}