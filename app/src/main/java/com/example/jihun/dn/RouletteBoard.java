package com.example.jihun.dn;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RouletteBoard extends ImageView {
    private static final String TAG = RouletteBoard.class.getSimpleName();

    /**
     * 그려지는 텍스트 없을경우 default
     */
    private static final String DEFAULT_TEXT = "N/A";
    private String mText = "";

    /**
     * 페인트 : 색 등을 지정하는
     */
    private Paint mPaint = new Paint();

    private Context mContext = null;

    public RouletteBoard(Context context) {
        super(context);
        mContext = context;
    }

    public RouletteBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    public RouletteBoard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(20);
        mPaint.setStyle(Paint.Style.FILL);
        TypedArray a = mContext.getTheme().obtainStyledAttributes(attrs,R.styleable.RouletteBoard,
                0, 0);

        try {
            mText = a.getString(R.styleable.RouletteBoard_text);
            mText = (mText == null)? DEFAULT_TEXT : mText; // 체크하기
        } finally {
            a.recycle();
        }
        // TODO 색깔 등도 attrs 에 지정 해주면 좋죠.
    }

    public void setText(String text) {
        mText = text;
    }

    public String getText() {
        return mText;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = this.getWidth()/2;
        int height = this.getHeight()/2;
        width -= mPaint.measureText(mText)/2;
        canvas.drawText(mText, width, height, mPaint);
    }



}
