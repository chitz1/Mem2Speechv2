package gonzalvo.dpsm.cas.upm.edu.ph.mem2speech.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class DrawingView extends View {
    private Path path;
    private Paint paint;
    private float x, y;
    private final float STROKE_WIDTH = 10f;
    private float strokeWidth;
    private int selectedColor = Color.BLACK;

    private Color_WidthDTO color_widthDTO;
    private ArrayList<Path> paths;

    public DrawingView(final Context context, final AttributeSet attributes) {
        super(context, attributes);

        this.strokeWidth = STROKE_WIDTH;

        this.color_widthDTO = new Color_WidthDTO();
        this.paths = new ArrayList<>();

        this.path = new Path();
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setColor(Color.BLACK);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeJoin(Paint.Join.ROUND);
        this.paint.setStrokeWidth(5f);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        final Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        new Canvas(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(final Path path : paths){
            this.paint.setColor(color_widthDTO.getColor(path));
            this.paint.setStrokeWidth(color_widthDTO.getStrokeWidth(path));
            canvas.drawPath(path, this.paint);
        }
        this.paint.setColor(this.selectedColor);
        this.paint.setStrokeWidth(this.strokeWidth);
        canvas.drawPath(this.path, this.paint);
    }

    private void startTouch(final float x, final float y) {
        this.path.moveTo(x, y);
        this.x = x;
        this.y = y;
    }

    private void moveTouch(final float x, final float y) {
        final float dx = Math.abs(x - this.x);
        final float dy = Math.abs(y - this.y);

        float TOLERANCE = 2;
        if(dx >= TOLERANCE || dy >= TOLERANCE) {
            this.path.quadTo(this.x, this.y, (x + this.x)/2, (y + this.y)/2);
            this.x = x;
            this.y = y;
        }
    }

    private void upTouch() {
        savePath();

        this.path.setLastPoint(this.x, this.y);
        this.x+=5;
        savePath();
        this.x-=5;

        this.path.setLastPoint(this.x, this.y);
        this.y--;
        savePath();
        this.path = new Path();
        this.y++;

        this.x-=5;
        this.path.setLastPoint(this.x, this.y);
        this.x+=5;
        savePath();

        this.path.reset();
    }

    private void savePath() {
        this.path.lineTo(this.x, this.y);
        this.paths.add(this.path);
        this.color_widthDTO.putPenProperty(this.path, this.selectedColor, this.strokeWidth);
        this.path = new Path();
    }

    public void clearCanvas() {
        this.color_widthDTO.clear();
        this.paths.clear();
        this.path.reset();
        invalidate();
    }

    public void switchTool() {
        if(penIsActive()) {
            switchToEraser();
        } else {
            switchToPen();
        }
    }

    private boolean penIsActive() {
        return this.selectedColor == Color.BLACK;
    }

    private void switchToPen() {
        this.selectedColor = Color.BLACK;
        this.strokeWidth = STROKE_WIDTH;
    }

    private void switchToEraser() {
        this.selectedColor = Color.WHITE;
        this.strokeWidth = 25f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouch(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x, y);
                break;
            case MotionEvent.ACTION_UP:
                upTouch();
                break;
        }
        invalidate();
        return true;
    }
}
