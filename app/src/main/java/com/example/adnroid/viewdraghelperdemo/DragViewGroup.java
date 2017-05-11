package com.example.adnroid.viewdraghelperdemo;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by adnroid on 17/5/11.
 */

public class DragViewGroup extends FrameLayout {
    private ViewDragHelper mViewDragHelper;
    private View mMenuView;
    private View mMainView;
    private int mWidth;
    private static  final String TAG = "DragViewGroup";


    public DragViewGroup(@NonNull Context context) {
        super(context);
        initView();
    }

    public DragViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();

    }

    private void initView() {
        mViewDragHelper = ViewDragHelper.create(this,callBack);
    }

    public DragViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 当完成inflateview时调用
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMenuView = getChildAt(0);
        mMainView = getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = mMenuView.getMeasuredWidth();
    }

    /**
     * 将事件拦截交给ViewDragHelper来处理
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    /**
     * 将触摸事件交给ViewDragHelper来处理
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }


    ViewDragHelper.Callback callBack = new ViewDragHelper.Callback() {
        /**
         * 何时开始检测触摸事件
         * @param child
         * @param pointerId
         * @return
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            //当触摸的view是Mainview 时开始检测触摸事件
            return child == mMainView;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            Log.i(TAG,"left: "+left);
            if(left<0)//当滑动小于左侧时
                left = 0;
            if (left>mWidth)//当滑动大于滑动过右侧时
                left = mWidth;
            return left;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return 0;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (mMainView.getLeft()<300){//关闭彩打
                mViewDragHelper.smoothSlideViewTo(mMainView,0,0);

            }else {//打开菜单
                mViewDragHelper.smoothSlideViewTo(mMainView,mWidth,0);
            }
            ViewCompat.postInvalidateOnAnimation(DragViewGroup.this);
        }
    };

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mViewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
}
