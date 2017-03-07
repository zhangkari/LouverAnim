package karics.anim.louveranim;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Louver Animator
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LouverAnimator extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
    final static String TAG = "LouverAnimator";

    private View mTarget;
    private FrameLayout mContainer;
    private AnimImageView mSubstitute;

    private Direction mDirection;
    private Listener mListener;

    public LouverAnimator() {
        mDirection = Direction.VERTICAL;
        removeAllListeners();
        removeAllUpdateListeners();
        addListener(this);
        addUpdateListener(this);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public LouverAnimator setTarget(View view) {
        if (null == view) {
            throw new NullPointerException("view must not be null");
        }
        mTarget = view;
        return this;
    }

    public void show() {
        show(null);
    }

    public void show(final Listener listener) {
        if (mTarget == null) {
            throw new IllegalStateException("call setTarget() first!");
        }

        if (isRunning()) {
            return;
        }

        setFloatValues(0.0f, 1.0f);
        setListener(new Listener() {
            @Override
            public void onStart() {
                if (null != listener) {
                    listener.onStart();
                }
            }

            @Override
            public void onEnd() {
                mTarget.setVisibility(View.VISIBLE);
                if (null != listener) {
                    listener.onEnd();
                }
            }
        });

        startAnim();
    }

    public void dismiss() {
        dismiss(null);
    }

    public void dismiss(final Listener listener) {
        if (mTarget == null) {
            throw new IllegalStateException("call setTarget() first!");
        }

        if (isRunning()) {
            return;
        }

        setFloatValues(1.0f, 0.0f);
        setListener(new Listener() {
            @Override
            public void onStart() {
                if (null != listener) {
                    listener.onStart();
                }
            }

            @Override
            public void onEnd() {
                mTarget.setVisibility(View.GONE);
                if (null != listener) {
                    listener.onEnd();
                }
            }
        });

        startAnim();
    }

    protected final void startAnim() {
        if (mTarget == null) {
            throw new IllegalStateException("call setTarget() first!");
        }

        if (isRunning()) {
            return;
        }

        if (mContainer == null) {
            mContainer = new FrameLayout(mTarget.getContext());
        } else {
            mContainer.removeAllViews();
        }

        ViewGroup parent = (ViewGroup) mTarget.getParent();
        if (parent.indexOfChild(mContainer) < 0) {
            parent.addView(mContainer, mTarget.getLayoutParams());
        }

        if (mSubstitute == null) {
            mSubstitute = new AnimImageView(mTarget.getContext());
        }
        mContainer.addView(mSubstitute);
        mTarget.setVisibility(View.GONE);

        Bitmap bitmap = convertViewToBitmap(mTarget);
        mSubstitute.setImageBitmap(bitmap);

        start();
    }

    @Override
    public void onAnimationStart(Animator animation) {
        mTarget.setVisibility(View.GONE);
        if (mListener != null) {
            mListener.onStart();
        }
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (mListener != null) {
            mListener.onEnd();
            mListener = null;
        }

        ViewGroup parent = (ViewGroup) mTarget.getParent();
        parent.removeView(mContainer);
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float value = (Float) animation.getAnimatedValue();
        if (mDirection == Direction.VERTICAL) {
            mSubstitute.setAnimHeight(value);
        } else if (mDirection == Direction.HORIZONTAL) {
            mSubstitute.setAnimWidth(value);
        }
    }

    public boolean setDirection(Direction direction) {
        if (isRunning()) {
            return false;
        }

        Float alpha = (Float) this.getAnimatedValue();
        if (alpha != null && alpha == 0.0f) {
            return false;
        }
        mDirection = direction;
        return true;
    }

    static Bitmap convertViewToBitmap(View view) {
        view.clearFocus();
        Canvas canvas = new Canvas();
        Bitmap bitmap = createBitmapSafely(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_4444, 2);
        if (bitmap != null) {
            canvas.setBitmap(bitmap);
            canvas.translate(-view.getScrollX(), -view.getScrollY());
            view.draw(canvas);
            canvas.setBitmap(null);
        }
        return bitmap;
    }

    static Bitmap createBitmapSafely(int width, int height, Bitmap.Config config, int retryCount) {
        while (retryCount-- > 0) {
            try {
                return Bitmap.createBitmap(width, height, config);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                System.gc();
            }
        }
        return null;
    }

    public enum Direction {
        VERTICAL,
        HORIZONTAL
    }

    public interface Listener {
        void onStart();

        void onEnd();
    }
}
