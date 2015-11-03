package devloop0.cs61a.org.cs61a;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by nathr on 10/31/2015.
 */

// http://stackoverflow.com/questions/25299473/can-i-draw-a-simple-paint-pie-chart-like-this
public class AssignmentCountdown extends View {
    private RectF rect = new RectF();
    private Paint paint = new Paint();
    private int percentage;

    public int doneColor = Color.parseColor("#FFE6FF");
    public int leftColor = Color.parseColor("#E7FFE6");

    public AssignmentCountdown(Context context) {
        this(context, null);
    }

    public AssignmentCountdown(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
        invalidate();
    }

    public int getPercentage() {
        return percentage;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > h) {
            rect.set(w / 2 - h / 2, 0, w / 2 + h / 2, h);
        }
        else {
            rect.set(0, h / 2 - w / 2, w, h / 2 + w / 2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(doneColor);
        canvas.drawArc(rect, 270, 360 * percentage / 100, true, paint);
        paint.setColor(leftColor);
        canvas.drawArc(rect, 270 + 360 * percentage / 100, 360 - 360 * percentage / 100, true, paint);
    }
}
