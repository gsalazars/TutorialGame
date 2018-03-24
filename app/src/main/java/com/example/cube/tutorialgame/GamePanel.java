package com.example.cube.tutorialgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by cube on 3/17/2018.
 */

class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;

    private RectPlayer player;
    private Point playerPoint;
    private ObstacleManager obstacleManager;

    private Rect gameOverRect = new Rect();

    private boolean movingPlayer =false;

    private boolean gameOver = false;
    private long gameOverTime;

    public GamePanel(Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(),this);

        player = new RectPlayer(new Rect(100,100,200,200), Color.rgb(255,0,0));
        playerPoint = new Point(new Point(Constants.SCREEN_WIDTH/2,3*Constants.SCREEN_HEIGHT/4));
        player.update(playerPoint);

        obstacleManager = new ObstacleManager(200,350,75,Color.GREEN);

        setFocusable(true);
    }

    public void reset(){
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, (3*Constants.SCREEN_HEIGHT)/4);
        player.update(playerPoint);
        obstacleManager = new ObstacleManager(200,350,75,Color.BLACK);
        movingPlayer = false;
    }
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {


    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        thread = new MainThread(getHolder(), this);

        thread.setRunning(true);
        thread.start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        while(true){
            try {
                thread.setRunning(false);
                thread.join();
            }catch(Exception e){
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(!gameOver && player.getRectangle().contains((int) event.getX(),(int) event.getY()))
                    movingPlayer = true;
                if(gameOver && System.currentTimeMillis() - gameOverTime >= 2000){
                    reset();
                    gameOver= false;
                }
            case MotionEvent.ACTION_MOVE:
                if(movingPlayer && !gameOver)
                    playerPoint.set((int)event.getX(),(int)event.getY());
                break;

            case MotionEvent.ACTION_UP:
                movingPlayer = false;
                break;

        }

        return true;
    }

    public void update(){
        if(!gameOver) {
            player.update(playerPoint);
            obstacleManager.update();
            if(obstacleManager.playerCollide(player)){
                gameOver = true;
                gameOverTime = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

        canvas.drawColor(Color.WHITE);
        player.draw(canvas);
        obstacleManager.draw(canvas);

        if(gameOver){
            Paint paint = new Paint();
            paint.setTextSize(100);
            paint.setColor(Color.MAGENTA);
            drawGameOver(canvas,paint,"Game Over");

        }
    }

    private void drawGameOver(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(gameOverRect);
        int cHeight = gameOverRect.height();
        int cWidth = gameOverRect.width();
        paint.getTextBounds(text,0,text.length(),gameOverRect);
        float x = cWidth / 2f -gameOverRect.width()/2f- gameOverRect.left;
        float y = cHeight / 2f + gameOverRect.height() /2f - gameOverRect.bottom;
        canvas.drawText(text,x,y,paint);

    }
}
