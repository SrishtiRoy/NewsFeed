package com.and.newsfeed.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.and.newsfeed.R;


public class RoundedImageView extends ImageView {

    private static final int PRESSED_ALPHA = 75;
    private static final int ANIMATION_TIME_ID = android.R.integer.config_shortAnimTime;

    private static final int DEFAULT_FILL_COLOR = Color.TRANSPARENT;
    private static final int DEFAULT_BORDER_COLOR = Color.WHITE;
    private static final int DEFAULT_FOCUS_COLOR = Color.argb(PRESSED_ALPHA,
            255, 255, 255);
    private static final float DEFAULT_BORDER_WIDTH = 1.0f;
    private static final float DEFAULT_PRESSED_RING_WIDTH = 5.0f;

    private BitmapShader shader;

    private float centerY;
    private float centerX;
    private float outerRadius;

    private Paint canvasPaint;
    private Paint fillPaint;
    private Paint borderPaint;
    private Paint focusPaint;

    private int fillColor;
    private int borderColor;
    private int focusColor;
    private float borderWidth;
    private float pressedRingWidth;

    private Boolean clickable = false;

    private boolean showAnimation;
    private float animationProgress;
    private ObjectAnimator pressedAnimator;
    private AnimatorSet loaderAnimatorSet;

    public RoundedImageView(Context context) {
        super(context);
        init(context, null);
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @SuppressLint("NewApi")
    private void init(Context context, AttributeSet attrs) {

        if (Build.VERSION.SDK_INT >= 11) {
            showAnimation = true;
        }

        this.setScaleType(ScaleType.CENTER_INSIDE);

        if (attrs != null) {

            final TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.RoundedImageView);

            try {
                fillColor = a.getColor(R.styleable.RoundedImageView_fill_color,
                        DEFAULT_FILL_COLOR);
                borderColor = a.getColor(
                        R.styleable.RoundedImageView_border_color,
                        DEFAULT_BORDER_COLOR);
                focusColor = a.getColor(
                        R.styleable.RoundedImageView_focus_color,
                        DEFAULT_FOCUS_COLOR);
                borderWidth = a.getDimension(
                        R.styleable.RoundedImageView_border_width,
                        DEFAULT_BORDER_WIDTH);
                clickable = a.getBoolean(
                        R.styleable.RoundedImageView_clickable, false);
                if (clickable) {
                    pressedRingWidth = a.getDimension(
                            R.styleable.RoundedImageView_pressed_ring_width,
                            DEFAULT_PRESSED_RING_WIDTH);
                } else {
                    pressedRingWidth = 0.0f;
                }

            } catch (Exception e) {

                a.recycle();
            }
        }

        canvasPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvasPaint.setStyle(Paint.Style.FILL);

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(fillColor);

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(borderWidth);

        focusPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        focusPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        focusPaint.setColor(Color.TRANSPARENT);
        focusPaint.setStrokeWidth(0.0f);

        setClickable(clickable);

        this.invalidate();

        if (showAnimation) {
            final int pressedAnimationTime = getResources().getInteger(
                    ANIMATION_TIME_ID);
            pressedAnimator = ObjectAnimator.ofFloat(this, "animationProgress",
                    0f, 0f);
            pressedAnimator.setDuration(pressedAnimationTime);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        Bitmap image = drawableToBitmap(this.getDrawable());
        if (image != null) {
            shader = new BitmapShader(image, Shader.TileMode.CLAMP,
                    Shader.TileMode.CLAMP);
            canvasPaint.setShader(shader);
        } else {
            canvasPaint.setColor(Color.TRANSPARENT);
        }
        canvas.drawCircle(centerX, centerY, outerRadius - pressedRingWidth
                - borderWidth / 2, fillPaint);
        canvas.drawCircle(centerX, centerY, outerRadius - pressedRingWidth
                - borderWidth / 2, canvasPaint);
        canvas.drawCircle(centerX, centerY, outerRadius - pressedRingWidth
                - borderWidth / 2, borderPaint);
        canvas.drawCircle(centerX, centerY, outerRadius - pressedRingWidth / 2,
                focusPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = ((float) w) / 2;
        centerY = ((float) h) / 2;
        outerRadius = ((float) Math.min(w, h)) / 2;
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);

        if (clickable) {

            if (pressed) {
                focusPaint.setColor(focusColor);
                showPressedRing();
            } else {
                hidePressedRing();
            }
        }
    }

    public Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        } else if (drawable instanceof BitmapDrawable && getWidth() > 0
                && getHeight() > 0) {
            Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                    Bitmap.Config.ARGB_8888);
            Bitmap image = ((BitmapDrawable) drawable).getBitmap();
            try {
                float scaleWidth = (outerRadius * 2 - getPaddingLeft() - getPaddingRight())
                        / (float) image.getWidth();
                float scaleHeight = (outerRadius * 2 - getPaddingTop() - getPaddingBottom())
                        / (float) image.getHeight();
                float scale = scaleWidth < scaleHeight ? scaleWidth : scaleHeight;
                Bitmap scaledImage = Bitmap.createScaledBitmap(image,
                        (int) (scale * image.getWidth()),
                        (int) (scale * image.getHeight()), false);
                if (scaledImage != null) {
                    Canvas canvas = new Canvas(bitmap);
                    Matrix matrix = new Matrix();
                    matrix.setTranslate((getWidth() - scaledImage.getWidth()) / 2,
                            (getHeight() - scaledImage.getHeight()) / 2);

                    canvas.drawBitmap(scaledImage, matrix, null);
                    return bitmap;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                Bitmap.Config.ARGB_8888);
        return bitmap;
    }

    public float getAnimationProgress() {
        return animationProgress;
    }

    public void setAnimationProgress(float animationProgress) {
        this.animationProgress = animationProgress;
        focusPaint.setStrokeWidth(animationProgress);
        this.invalidate();
    }

    @SuppressLint("NewApi")
    private void hidePressedRing() {
        if (showAnimation) {
            pressedAnimator.removeAllListeners();
            pressedAnimator.setFloatValues(animationProgress, 0f);
            pressedAnimator.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    focusPaint.setColor(Color.TRANSPARENT);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }
            });
            pressedAnimator.start();
        }
    }

    @SuppressLint("NewApi")
    private void showPressedRing() {
        if (showAnimation) {
            pressedAnimator.removeAllListeners();
            pressedAnimator.setFloatValues(animationProgress, pressedRingWidth);
            pressedAnimator.start();
        }
    }

    public void setFillColor(int color) {
        fillPaint.setColor(color);
        this.invalidate();
    }

    public void setBorderColor(int color) {
        borderPaint.setColor(color);
        this.invalidate();
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
        this.invalidate();
    }

    @SuppressLint("NewApi")
    public void startLoaderAnimation() {
        if (showAnimation) {
            ObjectAnimator fadeOutAnimator = ObjectAnimator.ofFloat(this,
                    "alpha", 0f);
            fadeOutAnimator.setDuration(1000);
            ObjectAnimator fadeInAnimator = ObjectAnimator.ofFloat(this,
                    "alpha", 0f, 1f);
            fadeInAnimator.setDuration(1000);
            loaderAnimatorSet = new AnimatorSet();
            loaderAnimatorSet.play(fadeInAnimator).after(fadeOutAnimator);
            loaderAnimatorSet.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    loaderAnimatorSet.start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }
            });
            loaderAnimatorSet.start();
        }
    }

    @SuppressLint("NewApi")
    public void endLoaderAnimation() {
        if (showAnimation && loaderAnimatorSet != null) {
            loaderAnimatorSet.removeAllListeners();
        }
    }
}