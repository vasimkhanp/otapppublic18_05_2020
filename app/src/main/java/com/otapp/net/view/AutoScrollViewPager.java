package com.otapp.net.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

public class AutoScrollViewPager extends ViewPager {

    public static final int DEFAULT_INTERVAL = 1500;

    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    public static final int SLIDE_BORDER_MODE_NONE = 0;
    public static final int SLIDE_BORDER_MODE_CYCLE = 1;
    public static final int SLIDE_BORDER_MODE_TO_PARENT = 2;

    private long interval = DEFAULT_INTERVAL;
    private int direction = RIGHT;
    private boolean isCycle = true;
    private boolean stopScrollWhenTouch = true;
    private int slideBorderMode = SLIDE_BORDER_MODE_NONE;
    private boolean isBorderAnimation = true;
    private double autoScrollFactor = 1.0;
    private double swipeScrollFactor = 1.0;

    private Handler handler;
    private boolean isAutoScroll = false;
    private boolean isStopByTouch = false;
    private float touchX = 0f, downX = 0f;
    private float touchY = 0f;

    private CustomDurationScroller scroller = null;

    public static final int SCROLL_WHAT = 0;

    public AutoScrollViewPager(Context paramContext) {
        super(paramContext);
        init();
    }

    public AutoScrollViewPager(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    private void init() {
        handler = new MyHandler(this);
        setViewPagerScroller();
    }

    /**
     * start auto scroll, first scroll delay time is {@link #getInterval()}
     */
    public void startAutoScroll() {
        isAutoScroll = true;
        sendScrollMessage((long) (interval + scroller.getDuration() / autoScrollFactor * swipeScrollFactor));
    }

    /**
     * start auto scroll
     *
     * @param delayTimeInMills first scroll delay time
     */
    public void startAutoScroll(int delayTimeInMills) {
        isAutoScroll = true;
        sendScrollMessage(delayTimeInMills);
    }

    /**
     * stop auto scroll
     */
    public void stopAutoScroll() {
        isAutoScroll = false;
        handler.removeMessages(SCROLL_WHAT);
    }

    /**
     * set the factor by which the duration of sliding animation will change while swiping
     */
    public void setSwipeScrollDurationFactor(double scrollFactor) {
        swipeScrollFactor = scrollFactor;
    }

    /**
     * set the factor by which the duration of sliding animation will change while auto scrolling
     */
    public void setAutoScrollDurationFactor(double scrollFactor) {
        autoScrollFactor = scrollFactor;
    }

    private void sendScrollMessage(long delayTimeInMills) {
        /** remove messages before, keeps one message is running at most **/
        handler.removeMessages(SCROLL_WHAT);
        handler.sendEmptyMessageDelayed(SCROLL_WHAT, delayTimeInMills);
    }

    /**
     * set ViewPager scroller to change animation duration when sliding
     */
    private void setViewPagerScroller() {
        try {
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            Field interpolatorField = ViewPager.class.getDeclaredField("sInterpolator");
            interpolatorField.setAccessible(true);

            scroller = new CustomDurationScroller(getContext(), (Interpolator) interpolatorField.get(null));
            scrollerField.set(this, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * scroll only once
     */
    public void scrollOnce() {
        PagerAdapter adapter = getAdapter();
        int currentItem = getCurrentItem();
        int totalCount;
        if (adapter == null || (totalCount = adapter.getCount()) <= 1) {
            return;
        }

        int nextItem = (direction == LEFT) ? --currentItem : ++currentItem;
        if (nextItem < 0) {
            if (isCycle) {
                setCurrentItem(totalCount - 1, isBorderAnimation);
            }
        } else if (nextItem == totalCount) {
            if (isCycle) {
                setCurrentItem(0, isBorderAnimation);
            }
        } else {
            setCurrentItem(nextItem, true);
        }
    }

    /**
     * <ul>
     * if stopScrollWhenTouch is true
     * <li>if event is down, stop auto scroll.</li>
     * <li>if event is up, start auto scroll again.</li>
     * </ul>
     */

    boolean consumeTouch = false;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);

        if (stopScrollWhenTouch) {
            if ((action == MotionEvent.ACTION_DOWN) && isAutoScroll) {
                isStopByTouch = true;
                stopAutoScroll();
            } else if (ev.getAction() == MotionEvent.ACTION_UP && isStopByTouch) {
                startAutoScroll();
            }
        }

        if (slideBorderMode == SLIDE_BORDER_MODE_TO_PARENT || slideBorderMode == SLIDE_BORDER_MODE_CYCLE) {
            touchX = ev.getX();
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                downX = touchX;
                touchY = ev.getY();
            } else if (action == MotionEvent.ACTION_UP) {
                consumeTouch = Math.abs(touchY - ev.getY()) > 0;
            }

            int currentItem = getCurrentItem();
            PagerAdapter adapter = getAdapter();
            int pageCount = adapter == null ? 0 : adapter.getCount();
            /**
             * current index is first one and slide to right or current index is last one and slide to left.<br/>
             * if slide border mode is to parent, then requestDisallowInterceptTouchEvent false.<br/>
             * else scroll to last one when current item is first one, scroll to first one when current item is last
             * one.
             */
            if ((currentItem == 0 && downX <= touchX) || (currentItem == pageCount - 1 && downX >= touchX)) {
                if (slideBorderMode == SLIDE_BORDER_MODE_TO_PARENT) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    if (pageCount > 1) {
                        setCurrentItem(pageCount - currentItem - 1, isBorderAnimation);
                    }
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                return super.dispatchTouchEvent(ev);
            }
        }
        if (consumeTouch) {
            getParent().requestDisallowInterceptTouchEvent(true);
        } else {
            getParent().requestDisallowInterceptTouchEvent(false);
            if (stopScrollWhenTouch)
                startAutoScroll();
        }

        return super.dispatchTouchEvent(ev);
    }

    private static class MyHandler extends Handler {

        private final WeakReference<AutoScrollViewPager> autoScrollViewPager;

        public MyHandler(AutoScrollViewPager autoScrollViewPager) {
            this.autoScrollViewPager = new WeakReference<AutoScrollViewPager>(autoScrollViewPager);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case SCROLL_WHAT:
                    AutoScrollViewPager pager = this.autoScrollViewPager.get();
                    if (pager != null) {
                        pager.scroller.setScrollDurationFactor(pager.autoScrollFactor);
                        pager.scrollOnce();
                        pager.scroller.setScrollDurationFactor(pager.swipeScrollFactor);
                        pager.sendScrollMessage(pager.interval + pager.scroller.getDuration());
                    }
                default:
                    break;
            }
        }
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public int getDirection() {
        return (direction == LEFT) ? LEFT : RIGHT;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean isCycle() {
        return isCycle;
    }

    public void setCycle(boolean isCycle) {
        this.isCycle = isCycle;
    }

    public boolean isStopScrollWhenTouch() {
        return stopScrollWhenTouch;
    }

    public void setStopScrollWhenTouch(boolean stopScrollWhenTouch) {
        this.stopScrollWhenTouch = stopScrollWhenTouch;
    }

    public int getSlideBorderMode() {
        return slideBorderMode;
    }

    public void setSlideBorderMode(int slideBorderMode) {
        this.slideBorderMode = slideBorderMode;
    }

    public boolean isBorderAnimation() {
        return isBorderAnimation;
    }

    public void setBorderAnimation(boolean isBorderAnimation) {
        this.isBorderAnimation = isBorderAnimation;
    }

    public class CustomDurationScroller extends Scroller {

        private double scrollFactor = 1;

        public CustomDurationScroller(Context context) {
            super(context);
        }

        public CustomDurationScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        // @SuppressLint("NewApi")
        // public CustomDurationScroller(Context context, Interpolator interpolator, boolean flywheel){
        // super(context, interpolator, flywheel);
        // }

        public void setScrollDurationFactor(double scrollFactor) {
            this.scrollFactor = scrollFactor;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, (int) (duration * scrollFactor));
        }
    }

}
