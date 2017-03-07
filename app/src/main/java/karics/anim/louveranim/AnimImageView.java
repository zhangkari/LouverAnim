package karics.anim.louveranim;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 *
 */
public class AnimImageView extends ImageView {
    private Paint mPaint;

    private float mWidthFactor;
    private float mHeightFactor;

    private Bitmap mBitmap;

    private Rect mSrcRect;
    private Rect mDstRect;

    public AnimImageView(Context context) {
        super(context);
        init();
    }

    public AnimImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        mWidthFactor = -1;
        mHeightFactor = -1;
        mPaint = new Paint();
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mBitmap == null) {
            return;
        }

        if (mHeightFactor >= 0) {
            mSrcRect.bottom = mSrcRect.top + (int) (mBitmap.getHeight() * mHeightFactor);
            mDstRect.bottom = mSrcRect.bottom;
        }

        if (mWidthFactor >= 0) {
            mSrcRect.right = mSrcRect.left + (int) (mBitmap.getWidth() * mWidthFactor);
            mDstRect.right = mSrcRect.right;
        }

        canvas.drawBitmap(mBitmap, mSrcRect, mDstRect, mPaint);
    }

    public void setAnimHeight(float factor) {
        if (Math.abs(mHeightFactor - factor) >= 0.001f) {
            mHeightFactor = factor;
            invalidate();
        }
    }

    public void setAnimWidth(float factor) {
        if (Math.abs(mWidthFactor - factor) >= 0.001f) {
            mWidthFactor = factor;
            invalidate();
        }
    }

    @Override
    public void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
        mBitmap = bitmap;

        if (null != bitmap) {
            if (null == mSrcRect) {
                mSrcRect = new Rect();
            }
            mSrcRect.top = 0;
            mSrcRect.left = 0;
            mSrcRect.right = bitmap.getWidth();
            mSrcRect.bottom = bitmap.getHeight();

            mDstRect = new Rect(mSrcRect);
        }
    }
}
