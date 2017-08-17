package noocsharp.mobiuschess;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nihal on 7/19/2017.
 */

public class ChessboardView extends View {
    private int width;
    private int height;

    Drawable drawable;
    Map<String, Drawable> pieceImageMap;

    Paint chessboardPaint;


    public ChessboardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;
        invalidate();
    }

    private void init() {

        chessboardPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        chessboardPaint.setColor(0xFF00FFFF);

        drawable = getResources().getDrawable(R.drawable.chess_board);

        pieceImageMap = new HashMap<>();

        pieceImageMap.put("black_bishop", getResources().getDrawable(R.drawable.ic_chess_bdt45));

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        Drawable localBishop = pieceImageMap.get("black_bishop");
        localBishop.setBounds(0, 0, canvas.getWidth()/8, canvas.getHeight()/8);
        localBishop.draw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(l);
    }
}
