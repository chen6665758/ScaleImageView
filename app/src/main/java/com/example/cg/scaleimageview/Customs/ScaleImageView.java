package com.example.cg.scaleimageview.Customs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.cg.scaleimageview.R;

/**
 * 自定义控件，根据传入的图片组，对图片进行从小到大的显示
 * 作者：cg
 * 时间：2017/4/24 0024 上午 10:30
 */
public class ScaleImageView extends ImageView {

    private Drawable mDrawable;
    private Handler mHandler;

    private int[] imgSrc = new int[]{R.mipmap.mm1};

    private boolean isEnlarge = true;
    private float scaleNum = 1.0f;                              //图片初始放大的倍数
    private float scaleMaxNum = 1.2f;                           //图片最大会被放大多少，默认是1.2倍
    private float scaleValue = 0.002f;                          //图片每次放大缩小的倍数
    private int num = 1;                                        //当前显示到第几个图

    public ScaleImageView(Context context) {
        this(context,null);
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs,R.styleable.ScaleImageView,defStyleAttr,0);
        int n = array.getIndexCount();
        for(int i=0;i<n;i++)
        {
            int arr = array.getIndex(i);
            switch (arr)
            {
                case R.styleable.ScaleImageView_isEnlarge:
                    isEnlarge = array.getBoolean(arr,true);
                    break;
                case R.styleable.ScaleImageView_scaleMaxNum:
                    scaleMaxNum = array.getFloat(arr,1.2f);
                    break;
                case R.styleable.ScaleImageView_scaleValue:
                    scaleValue = array.getFloat(arr,0.002f);
                    break;
            }
        }
        array.recycle();

        if(!isEnlarge)
        {
            scaleNum = scaleMaxNum;
        }

        //初始化第一个图
        mDrawable=getDrawable();
        mHandler=new ScaleImageView.MoveHandler();
        mHandler.sendEmptyMessageDelayed(1, 220L);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.scale(scaleNum,scaleNum,getMeasuredWidth()/2,getMeasuredHeight()/2);

        mDrawable.draw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if(isEnlarge) {
            mDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
        }else{
            int mCanvasWBgSize = Math.round(getMeasuredWidth() * scaleMaxNum);
            int mCanvashBgSize = Math.round(getMeasuredHeight() * scaleMaxNum);

            int leftRight = (mCanvasWBgSize - getMeasuredWidth())/2;
            int topBottom = (mCanvashBgSize - getMeasuredHeight())/2;

            mDrawable.setBounds(-leftRight,-topBottom,getMeasuredWidth() + leftRight,getMeasuredHeight() + topBottom);
        }

    }

    private class MoveHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            /**
             * 此处进行判断，如果小于最大倍数，就不断放大，如果大于了最大或等于最大倍数，就进行换图
             */
            if(isEnlarge) {
                if (scaleNum < scaleMaxNum) {
                    scaleNum += scaleValue;
                    invalidate();
                    mHandler.sendEmptyMessageDelayed(1, 22);
                } else {
                    scaleNum = 1.0f;
                    setImageResource(imgSrc[num % 3]);
                    mDrawable = getDrawable();
                    num++;
                    invalidate();
                    mHandler.sendEmptyMessageDelayed(1, 22);
                }
            }else{

                if(scaleNum >= 0.9) {
                    scaleNum -= 0.002;
                    invalidate();
                    mHandler.sendEmptyMessageDelayed(1, 22);
                }else {
                    scaleNum = scaleMaxNum;

                    //如果图片只有一个，哪就循环此图片，不用更新
                    if(imgSrc.length!=1) {
                        setImageResource(imgSrc[num % 3]);
                        mDrawable = getDrawable();
                        num++;
                    }

                    invalidate();
                    mHandler.sendEmptyMessageDelayed(1, 22);
                }

            }
        }
    }

    /**
     * 加载图片列表
     * @param images   图片列表
     */
    public void setImages(int[] images)
    {
        this.imgSrc = images;
        invalidate();
    }
}
