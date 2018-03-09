package android.bignerdranch.com.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ybr on 09.03.2018.
 */

public class BoxDrawingView extends View {
    private static final String TAG = "BoxDrawingView";
    private static final String ARGUMENT_BOXES = "ArgumentBoxes";
    private static final String ARGUMENT_SUPER = "ArgumentSuper";

    private Box mCurrentBox;
    private ArrayList<Box> mBoxen = new ArrayList<>();
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;

    public BoxDrawingView(Context context) {
        this(context, null);
    }

    public BoxDrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARGUMENT_BOXES, mBoxen);
        args.putParcelable(ARGUMENT_SUPER, super.onSaveInstanceState());
        return args;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state != null){
            Bundle args = (Bundle)state;
            if(args.containsKey(ARGUMENT_SUPER)){
                super.onRestoreInstanceState(args.getParcelable(ARGUMENT_SUPER));
            }
            if(args.containsKey(ARGUMENT_BOXES)){
                mBoxen = args.getParcelableArrayList(ARGUMENT_BOXES);
                invalidate();
            }

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);

        for(Box box : mBoxen){
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);
            canvas.drawRect(left, top, right, bottom, mBoxPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF current = new PointF(event.getX(), event.getY());
        String action = "";
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";
                mCurrentBox = new Box(current);
                mBoxen.add(mCurrentBox);
                break;
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";
                if(mCurrentBox != null){
                    mCurrentBox.setCurrent(current);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                mCurrentBox = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                mCurrentBox = null;
                break;
        }
        Log.i(TAG, action + "at x=" + current.x + ", y=" + current.y);

        return true;
    }
}
