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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import noocsharp.Chessboard;
import noocsharp.piece.Bishop;
import noocsharp.piece.King;
import noocsharp.piece.Knight;
import noocsharp.piece.Nwap;
import noocsharp.piece.Pawn;
import noocsharp.piece.Piece;
import noocsharp.piece.Queen;
import noocsharp.piece.Rook;
import noocsharp.utilities.Color;
import noocsharp.utilities.Tuple;
import noocsharp.utilities.Utilities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by nihal on 7/19/2017.
 */

public class ChessboardView extends View {

    private static final String TAG = "ChessboardView";
    private int width;
    private int height;

    Drawable drawable;
    Drawable row1;
    Drawable row2;
    Map<String, Drawable> pieceImageMap;

    Paint chessboardPaint;

    Chessboard board;

    int cellWidth;
    int cellHeight;

    int selectionX;
    int selectionY;

    int destinationX;
    int destinationY;


    public ChessboardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        board = new Chessboard(8, 14);
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

        // Adds all piece images to the map
        pieceImageMap.put("black_bishop", getResources().getDrawable(R.drawable.chess_bdt45));
        pieceImageMap.put("black_rook",   getResources().getDrawable(R.drawable.chess_rdt45));
        pieceImageMap.put("black_knight", getResources().getDrawable(R.drawable.chess_ndt45));
        pieceImageMap.put("black_king",   getResources().getDrawable(R.drawable.chess_kdt45));
        pieceImageMap.put("black_queen",  getResources().getDrawable(R.drawable.chess_qdt45));
        pieceImageMap.put("black_pawn",   getResources().getDrawable(R.drawable.chess_pdt45));
        pieceImageMap.put("black_nwap",   getResources().getDrawable(R.drawable.chess_hdt45));

        pieceImageMap.put("white_bishop", getResources().getDrawable(R.drawable.chess_blt45));
        pieceImageMap.put("white_rook",   getResources().getDrawable(R.drawable.chess_rlt45));
        pieceImageMap.put("white_knight", getResources().getDrawable(R.drawable.chess_nlt45));
        pieceImageMap.put("white_king",   getResources().getDrawable(R.drawable.chess_klt45));
        pieceImageMap.put("white_queen",  getResources().getDrawable(R.drawable.chess_qlt45));
        pieceImageMap.put("white_pawn",  getResources().getDrawable(R.drawable.chess_plt45));
        pieceImageMap.put("white_nwap",   getResources().getDrawable(R.drawable.chess_hlt45));

        row1 = getResources().getDrawable(R.drawable.chess_board_row1);
        row2 = getResources().getDrawable(R.drawable.chess_board_row2);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();

        int gridX = (int) (eventX/cellWidth);
        int gridY = (int) (eventY/cellHeight);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.i(TAG, String.format("position: %d, %d", gridX, gridY));

            selectionX = gridX;
            selectionY = gridY;

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            Log.i(TAG, String.format("position: %d, %d", gridX, gridY));

            destinationX = gridX;
            destinationY = gridY;

            // Checks if another piece in in the destination square
            Piece p = Utilities.searchForPos(board.getPiecesArray(), new Tuple<>(selectionX, selectionY));
            Piece des = Utilities.searchForPos(board.getPiecesArray(), new Tuple<>(destinationX, destinationY));

            if (p != des && p != null) {
                board.makeMove(new Tuple<>(selectionX, selectionY), new Tuple<>(destinationX, destinationY));
                invalidate();
                /*
                if (p != null) {
                    board.getPiecesArray().remove(des);
                    p.setPos(new Tuple<>(destinationX, destinationY));
                    invalidate();
                }
                */
            }

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {

        }

        //return super.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /*
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        */

        int numVertical = 14;
        int numHorizontal = 8;

        cellWidth = canvas.getWidth() / numHorizontal;
        cellHeight = canvas.getHeight() / numVertical;

        for (int i = 0; i < numVertical; i++) {
            Log.i(TAG, String.format("row iteration : %d", i));
            if (i % 2 == 0) {
                row1.setBounds(0, i * cellHeight, cellWidth * 8, (i + 1) * cellHeight);
                row1.draw(canvas);
            } else {
                row2.setBounds(0, i * cellHeight, cellWidth * 8, (i + 1) * cellHeight);
                row2.draw(canvas);
            }
        }


        for (Piece p : board.getPiecesArray()) {
            Drawable d = getConfiguredDrawableFromPiece(p);
            if (d != null) {
                d.draw(canvas);
            }

        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public Drawable getConfiguredDrawableFromPiece(Piece p) {
        Drawable d = pieceImageMap.get("white_pawn");
        if (p instanceof Pawn) {
            if (p.color == Color.WHITE) {
                d = pieceImageMap.get("white_pawn");
            } else if (p.color == Color.BLACK) {
                d = pieceImageMap.get("black_pawn");
            }
        } else if (p instanceof Rook) {
            if (p.color == Color.WHITE) {
                d = pieceImageMap.get("white_rook");
            } else if (p.color == Color.BLACK) {
                d = pieceImageMap.get("black_rook");
            }
        } else if (p instanceof Bishop) {
            if (p.color == Color.WHITE) {
                d = pieceImageMap.get("white_bishop");
            } else if (p.color == Color.BLACK) {
                d = pieceImageMap.get("black_bishop");
            }

        } else if (p instanceof Knight) {
            if (p.color == Color.WHITE) {
                d = pieceImageMap.get("white_knight");
            } else if (p.color == Color.BLACK) {
                d = pieceImageMap.get("black_knight");
            }

        } else if (p instanceof King) {
            if (p.color == Color.WHITE) {
                d = pieceImageMap.get("white_king");
            } else if (p.color == Color.BLACK) {
                d = pieceImageMap.get("black_king");
            }

        } else if (p instanceof Queen) {
            if (p.color == Color.WHITE) {
                d = pieceImageMap.get("white_queen");
            } else if (p.color == Color.BLACK) {
                d = pieceImageMap.get("black_queen");
            }

        } else if (p instanceof Nwap) {
            if (p.color == Color.WHITE) {
                d = pieceImageMap.get("white_nwap");
            } else if (p.color == Color.BLACK) {
                d = pieceImageMap.get("black_nwap");
            }
        }

        d.setBounds(p.pos.x * cellWidth, p.pos.y * cellHeight, (p.pos.x + 1) * cellWidth, (p.pos.y + 1) * cellHeight);
        return d;
    }
}
