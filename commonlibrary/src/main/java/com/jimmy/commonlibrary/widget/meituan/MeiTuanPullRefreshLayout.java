package com.jimmy.commonlibrary.widget.meituan;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.jimmy.commonlibrary.R;


public class MeiTuanPullRefreshLayout extends ViewGroup{

	private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;
	private static final int DRAG_MAX_DISTANCE =64;
	private static final int INVALID_POINTER = -1;
	private static final float DRAG_RATE = .5f;

	public static final int STYLE_MATERIAL = 0;
	public static final int STYLE_CIRCLES = 1;
	public static final int STYLE_WATER_DROP = 2;
	public static final int STYLE_RING = 3;
	public static final int MODE_TOP = 0;
	public static final int MODE_BOTTOM = 1;

	private View mTarget;
	private Interpolator mDecelerateInterpolator;
	private int mTouchSlop;
	private int mMediumAnimationDuration;
	private int mSpinnerFinalOffset;
	private int mTotalDragDistance;
	private int mCurrentOffsetTop;
	private boolean mRefreshing;
	private int mActivePointerId;
	private boolean mIsBeingDragged;
	private float mInitialMotionY;
	private int mFrom;
	private boolean mNotify;
	private OnRefreshListener mListener;
	private int[] mColorSchemeColors;
	private int mMode;

	private LinearLayout headerView;
	private int headerHeight;
	private int mHeaderViewIndex;
	private boolean mUsingCustomStart;
	private int mOriginalOffsetTop = 0;

	/**
	 * 下拉状态
	 */
	public static final int STATUS_PULL_TO_REFRESH = 0;

	/**
	 * 释放立即刷新状态
	 */
	public static final int STATUS_RELEASE_TO_REFRESH = 1;

	/**
	 * 正在刷新状态
	 */
	public static final int STATUS_REFRESHING = 2;

	/**
	 * 刷新完成或未刷新状态
	 */
	public static final int STATUS_REFRESH_FINISHED = 3;

	private int currentStatus = STATUS_REFRESH_FINISHED;
	private MeiTuanRefreshFirstStepView mFirstView;
	private MeiTuanRefreshSecondStepView mSecondView;
	private AnimationDrawable secondAnim;
	private MeiTuanRefreshThirdStepView mThirdView;
	private AnimationDrawable thirdAnim;

	public MeiTuanPullRefreshLayout(Context context) {
		this(context, null);
	}

	public MeiTuanPullRefreshLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PullRefreshLayout);
		final int type = a.getInteger(R.styleable.PullRefreshLayout_type, STYLE_MATERIAL);
		a.recycle();

		mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		mMediumAnimationDuration = getResources().getInteger(android.R.integer.config_mediumAnimTime);
		mSpinnerFinalOffset = mTotalDragDistance = dp2px(DRAG_MAX_DISTANCE);
		headerHeight = dp2px(60);
		headerView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.meituan_item, null, false);
		mFirstView = (MeiTuanRefreshFirstStepView) headerView.findViewById(R.id.first_view);
		mSecondView = (MeiTuanRefreshSecondStepView) headerView.findViewById(R.id.second_view);
		mSecondView.setBackgroundResource(R.drawable.pull_to_refresh_second_anim);
		secondAnim = (AnimationDrawable) mSecondView.getBackground();
		mThirdView = (MeiTuanRefreshThirdStepView) headerView.findViewById(R.id.third_view);
		mThirdView.setBackgroundResource(R.drawable.pull_to_refresh_third_anim);
		thirdAnim = (AnimationDrawable) mThirdView.getBackground();
		// mRefreshDrawable.setColorSchemeColors(new int[]{Color.rgb(0xC9, 0x34, 0x37),
		// Color.rgb(0x37, 0x5B, 0xF1), Color.rgb(0xF7, 0xD2, 0x3E), Color.rgb(0x34, 0xA3, 0x50)});
		headerView.setVisibility(View.GONE);
		addView(headerView);

		setWillNotDraw(false);
		setChildrenDrawingOrderEnabled(true);
	}

	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		// TODO Auto-generated method stub
		if (mHeaderViewIndex < 0) {
			return i;
		} else if (i == childCount - 1) {
			// Draw the selected child last
			return mHeaderViewIndex;
		} else if (i >= mHeaderViewIndex) {
			// Move the children after the selected child earlier one
			return i + 1;
		} else {
			// Keep the children before the selected child the same
			return i;
		}
	}

	public int getFinalOffset() {
		return mSpinnerFinalOffset;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		ensureTarget();
		if (mTarget == null)
			return;

		widthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingRight() - getPaddingLeft(), MeasureSpec.EXACTLY);
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY);
		mTarget.measure(widthMeasureSpec, heightMeasureSpec);
		headerView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(headerHeight, MeasureSpec.EXACTLY));

		// mRefreshView.measure(MeasureSpec.makeMeasureSpec(mRefreshViewWidth, MeasureSpec.EXACTLY),
		// MeasureSpec.makeMeasureSpec(mRefreshViewHeight, MeasureSpec.EXACTLY));

		// if (!mUsingCustomStart) {
		// mOriginalOffsetTop = - mHeaderLayout.getMeasuredHeight();
		// }
		mHeaderViewIndex = -1;
		for (int index = 0; index < getChildCount(); index++) {
			if (getChildAt(index) == headerView) {
				mHeaderViewIndex = index;
				break;
			}
		}

	}

	private void ensureTarget() {
		if (mTarget != null)
			return;
		if (getChildCount() > 0) {
			for (int i = 0; i < getChildCount(); i++) {
				View child = getChildAt(i);
				if (child != headerView)
					mTarget = child;
			}
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		if (!isEnabled() || canChildScrollUp() || mRefreshing) {
			return false;
		}

		final int action = MotionEventCompat.getActionMasked(ev);

		switch (action) {
			case MotionEvent.ACTION_DOWN:
				setTargetOffsetTop(0, true);
				mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
				mIsBeingDragged = false;
				final float initialMotionY = getMotionEventY(ev, mActivePointerId);
				if (initialMotionY == -1) {
					return false;
				}
				mInitialMotionY = initialMotionY;
				break;
			case MotionEvent.ACTION_MOVE:
				if (mActivePointerId == INVALID_POINTER) {
					return false;
				}
				final float y = getMotionEventY(ev, mActivePointerId);
				if (y == -1) {
					return false;
				}
				final float yDiff = y - mInitialMotionY;
				// if (Math.abs(yDiff) > mTouchSlop && !mIsBeingDragged) {
				// mIsBeingDragged = true;
				// mMode = yDiff > 0 ? MODE_TOP : MODE_BOTTOM;
				// }
				if (yDiff > mTouchSlop && !mIsBeingDragged) {
					mIsBeingDragged = true;
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				mIsBeingDragged = false;
				mActivePointerId = INVALID_POINTER;
				break;
			case MotionEventCompat.ACTION_POINTER_UP:
				onSecondaryPointerUp(ev);
				break;
		}

		return mIsBeingDragged;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		if (!mIsBeingDragged) {
			return super.onTouchEvent(ev);
		}

		final int action = MotionEventCompat.getActionMasked(ev);

		switch (action) {
			case MotionEvent.ACTION_MOVE: {
				final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
				if (pointerIndex < 0) {
					return false;
				}

				final float y = MotionEventCompat.getY(ev, pointerIndex);
				// final float yDiff = Math.abs(y - mInitialMotionY);
				final float yDiff = y - mInitialMotionY;
				final float scrollTop = yDiff * DRAG_RATE;
				float originalDragPercent = scrollTop / mTotalDragDistance;
				if (originalDragPercent < 0) {
					return false;
				}
				float dragPercent = Math.min(1f, Math.abs(originalDragPercent));
				// float adjustedPercent = (float) Math.max(dragPercent - .4, 0) * 5 / 3;
				// float adjustedPercent = dragPercent;
				float extraOS = Math.abs(scrollTop) - mTotalDragDistance;
				float slingshotDist = mSpinnerFinalOffset;
				float tensionSlingshotPercent = Math.max(0, Math.min(extraOS, slingshotDist * 2) / slingshotDist);
				float tensionPercent = (float) ((tensionSlingshotPercent / 4) - Math.pow((tensionSlingshotPercent / 4), 2)) * 2f;
				float extraMove = (slingshotDist) * tensionPercent * 2;
				int targetY = (int) ((slingshotDist * dragPercent) + extraMove);
				if (headerView.getVisibility() != View.VISIBLE) {
					headerView.setVisibility(View.VISIBLE);
				}
				//给第一个状态的View设置当前进度值
				mFirstView.setCurrentProgress(dragPercent);
				//重画
				mFirstView.postInvalidate();
				if (scrollTop > mTotalDragDistance) {
					updateHeaderView(STATUS_RELEASE_TO_REFRESH);
					changeHeaderByState(STATUS_RELEASE_TO_REFRESH);
					
				} else {
					updateHeaderView(STATUS_PULL_TO_REFRESH);
					changeHeaderByState(STATUS_PULL_TO_REFRESH);
				}
				
			
				setTargetOffsetTop(targetY - mCurrentOffsetTop, true);
				break;
			}
			case MotionEventCompat.ACTION_POINTER_DOWN:
				final int index = MotionEventCompat.getActionIndex(ev);
				mActivePointerId = MotionEventCompat.getPointerId(ev, index);
				break;
			case MotionEventCompat.ACTION_POINTER_UP:
				onSecondaryPointerUp(ev);
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL: {
				if (mActivePointerId == INVALID_POINTER) {
					return false;
				}
				final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
				final float y = MotionEventCompat.getY(ev, pointerIndex);
				final float overscrollTop = (y - mInitialMotionY) * DRAG_RATE;
				mIsBeingDragged = false;
				if (overscrollTop > mTotalDragDistance) {
					setRefreshing(true, true);
					updateHeaderView(STATUS_REFRESHING);
					changeHeaderByState(STATUS_REFRESHING);
				} else {
					mRefreshing = false;
					animateOffsetToStartPosition();
					updateHeaderView(STATUS_PULL_TO_REFRESH);
					changeHeaderByState(STATUS_PULL_TO_REFRESH);
				}
				mActivePointerId = INVALID_POINTER;
				return false;
			}
		}

		return true;
	}


	private void animateOffsetToStartPosition() {
		mFrom = mCurrentOffsetTop;
		mAnimateToStartPosition.reset();
		mAnimateToStartPosition.setDuration(mMediumAnimationDuration);
		mAnimateToStartPosition.setInterpolator(mDecelerateInterpolator);
		mAnimateToStartPosition.setAnimationListener(mToStartListener);
		headerView.clearAnimation();
		headerView.startAnimation(mAnimateToStartPosition);
	}

	private void animateOffsetToCorrectPosition() {
		mFrom = mCurrentOffsetTop;
		mAnimateToCorrectPosition.reset();
		mAnimateToCorrectPosition.setDuration(mMediumAnimationDuration);
		mAnimateToCorrectPosition.setInterpolator(mDecelerateInterpolator);
		mAnimateToCorrectPosition.setAnimationListener(mRefreshListener);
		headerView.clearAnimation();
		headerView.startAnimation(mAnimateToCorrectPosition);
	}

	private final Animation mAnimateToStartPosition = new Animation() {
		@Override
		public void applyTransformation(float interpolatedTime, Transformation t) {
			moveToStart(interpolatedTime);
		}
	};

	private final Animation mAnimateToCorrectPosition = new Animation() {
		@Override
		public void applyTransformation(float interpolatedTime, Transformation t) {
			int targetTop = 0;
			int endTarget = mSpinnerFinalOffset;
			targetTop = (mFrom + (int) ((endTarget - mFrom) * interpolatedTime));
			int offset = targetTop - mTarget.getTop();
			setTargetOffsetTop(offset, false /* requires update */);
		}
	};

	private void moveToStart(float interpolatedTime) {
		int targetTop = mFrom - (int) (mFrom * interpolatedTime);
		int offset = targetTop - mTarget.getTop();
		setTargetOffsetTop(offset, false);
	}

	public void setRefreshing(boolean refreshing) {
		if (mRefreshing != refreshing) {
			setRefreshing(refreshing, false /* notify */);
		}
	}

	private void setRefreshing(boolean refreshing, final boolean notify) {
		if (mRefreshing != refreshing) {
			mNotify = notify;
			ensureTarget();
			mRefreshing = refreshing;
			if (mRefreshing) {
				animateOffsetToCorrectPosition();
			} else {
				animateOffsetToStartPosition();
			}
		}
	}

	private Animation.AnimationListener mRefreshListener = new Animation.AnimationListener() {
		@Override
		public void onAnimationStart(Animation animation) {
			headerView.setVisibility(View.VISIBLE);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {}

		@Override
		public void onAnimationEnd(Animation animation) {
			if (mRefreshing) {
				if (mNotify) {
					if (mListener != null) {
						mListener.onRefresh();
					}
				}
			} else {
				headerView.setVisibility(View.GONE);
				animateOffsetToStartPosition();
			}
			mCurrentOffsetTop = mTarget.getTop();
		}
	};

	private Animation.AnimationListener mToStartListener = new Animation.AnimationListener() {
		@Override
		public void onAnimationStart(Animation animation) {}

		@Override
		public void onAnimationRepeat(Animation animation) {}

		@Override
		public void onAnimationEnd(Animation animation) {
			headerView.setVisibility(View.GONE);
			mCurrentOffsetTop = mTarget.getTop();
		}
	};

	private void onSecondaryPointerUp(MotionEvent ev) {
		final int pointerIndex = MotionEventCompat.getActionIndex(ev);
		final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
		if (pointerId == mActivePointerId) {
			final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
			mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
		}
	}

	private float getMotionEventY(MotionEvent ev, int activePointerId) {
		final int index = MotionEventCompat.findPointerIndex(ev, activePointerId);
		if (index < 0) {
			return -1;
		}
		return MotionEventCompat.getY(ev, index);
	}

	private void setTargetOffsetTop(int offset, boolean requiresUpdate) {
		//Log.i("setTargetOffsetTop:", "offset:" + offset);
		// mRefreshView.offsetTopAndBottom(offset);
		mTarget.offsetTopAndBottom(offset);
		headerView.bringToFront();
		headerView.offsetTopAndBottom(offset);
		mCurrentOffsetTop = mTarget.getTop();
		if (requiresUpdate && android.os.Build.VERSION.SDK_INT < 11) {
			invalidate();
		}
	}

	private boolean canChildScrollUp() {
		if (android.os.Build.VERSION.SDK_INT < 14) {
			if (mTarget instanceof AbsListView) {
				final AbsListView absListView = (AbsListView) mTarget;
				return absListView.getChildCount() > 0
						&& (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
			} else {
				return mTarget.getScrollY() > 0;
			}
		} else {
			return ViewCompat.canScrollVertically(mTarget, -1);
		}
	}

	private boolean canChildScrollDown() {
		if (android.os.Build.VERSION.SDK_INT < 14) {
			if (mTarget instanceof AbsListView) {
				final AbsListView absListView = (AbsListView) mTarget;
				return absListView.getChildCount() > 0
						&& (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
			} else {
				return mTarget.getScrollY() > 0;
			}
		} else {
			return ViewCompat.canScrollVertically(mTarget, -1);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		ensureTarget();
		if (mTarget == null)
			return;

		int height = getMeasuredHeight();
		int width = getMeasuredWidth();
		int left = getPaddingLeft();
		int top = getPaddingTop();
		int right = getPaddingRight();
		int bottom = getPaddingBottom();

		mTarget.layout(left, top + mCurrentOffsetTop, left + width - right, top + height - bottom + mCurrentOffsetTop);
		// mRefreshView.layout(width / 2 - mRefreshViewWidth / 2, -mRefreshViewHeight +
		// mCurrentOffsetTop, width / 2 + mRefreshViewHeight / 2, mCurrentOffsetTop);
		// mRefreshView.layout(left, top, left + width - right, top + height - bottom);

		// mHeaderLayout.layout(left, mCurrentOffsetTop, left + width - right, headerHeight +
		// mCurrentOffsetTop);
		headerView.layout(left, -headerHeight + mOriginalOffsetTop + mCurrentOffsetTop, left + width - right, mOriginalOffsetTop + mCurrentOffsetTop);
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
	}

	public void setOnRefreshListener(OnRefreshListener listener) {
		mListener = listener;
	}

	public interface OnRefreshListener {
		void onRefresh();
	}

	public void setProgressViewOffset(int start) {
		headerView.setVisibility(View.GONE);
		mOriginalOffsetTop = start;
		mUsingCustomStart = true;
		headerView.invalidate();
	}

	public void updateHeaderView(int status) {
		/*if (currentStatus != status) {
			if (status == STATUS_PULL_TO_REFRESH) {
				mDescriptionText.setText(R.string.pull_to_refresh);
				mArrowView.setVisibility(View.VISIBLE);
				mLoadingView.setVisibility(View.GONE);
				mLoadingView.clearAnimation();
				rotateArrow(status);
			} else if (status == STATUS_RELEASE_TO_REFRESH) {
				mDescriptionText.setText(R.string.release_to_refresh);
				mArrowView.setVisibility(View.VISIBLE);
				mLoadingView.setVisibility(View.GONE);
				mLoadingView.clearAnimation();
				rotateArrow(status);
			} else if (status == STATUS_REFRESHING) {
				mDescriptionText.setText(R.string.refreshing);
				mArrowView.setVisibility(View.GONE);
				mArrowView.clearAnimation();
				mLoadingView.setVisibility(View.VISIBLE);
				startAnimaltion(mLoadingView);
			}
			currentStatus = status;
		}*/
	}


	/***
	 *  是否是顶部在刷新
	 * @return
	 */
	public boolean isRefreshing() {
		return mRefreshing;
	}

	/**
	 * 根据状态改变headerView的动画和文字显示
	 * @param state
	 */
	private void changeHeaderByState(int state){
		switch (state) {
		case STATUS_REFRESH_FINISHED://如果的隐藏的状态
			//设置headerView的padding为隐藏
			headerView.setVisibility(View.GONE);
			//第一状态的view显示出来
			mFirstView.setVisibility(View.VISIBLE);
			//第二状态的view隐藏起来
			mSecondView.setVisibility(View.GONE);
			//停止第二状态的动画
			secondAnim.stop();
			//第三状态的view隐藏起来
			mThirdView.setVisibility(View.GONE);
			//停止第三状态的动画
			thirdAnim.stop();
			break;
		case STATUS_RELEASE_TO_REFRESH://当前状态为放开刷新
			//文字显示为放开刷新
			//第一状态view隐藏起来
			mFirstView.setVisibility(View.GONE);
			//第二状态view显示出来
			mSecondView.setVisibility(View.VISIBLE);
			//播放第二状态的动画
			secondAnim.start();
			//第三状态view隐藏起来
			mThirdView.setVisibility(View.GONE);
			//停止第三状态的动画
			thirdAnim.stop();
			break;
		case STATUS_PULL_TO_REFRESH://当前状态为下拉刷新
			//设置文字为下拉刷新
			//第一状态view显示出来
			mFirstView.setVisibility(View.VISIBLE);
			//第二状态view隐藏起来
			mSecondView.setVisibility(View.GONE);
			//第二状态动画停止
			secondAnim.stop();
			//第三状态view隐藏起来
			mThirdView.setVisibility(View.GONE);
			//第三状态动画停止
			thirdAnim.stop();
			break;
		case STATUS_REFRESHING://当前状态为正在刷新
			//文字设置为正在刷新
			//第一状态view隐藏起来
			mFirstView.setVisibility(View.GONE);
			//第三状态view显示出来
			mThirdView.setVisibility(View.VISIBLE);
			//第二状态view隐藏起来
			mSecondView.setVisibility(View.GONE);
			//停止第二状态动画
			secondAnim.stop();
			//启动第三状态view
			thirdAnim.start();
			break;
		default:
			break;
		}
	}
	
}
