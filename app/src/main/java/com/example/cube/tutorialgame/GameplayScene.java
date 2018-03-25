package com.example.cube.tutorialgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

/**
 * Created by cube on 3/25/2018.
 */

public class GameplayScene implements Scene  {
    private RectPlayer player;
    private Point playerPoint;
    private ObstacleManager obstacleManager;

    private Rect gameOverRect = new Rect();

    private boolean movingPlayer =false;

    private boolean gameOver = false;
    private long gameOverTime;


    public GameplayScene(){
        player = new RectPlayer(new Rect(100,100,200,200), Color.rgb(255,0,0));
        playerPoint = new Point(new Point(Constants.SCREEN_WIDTH/2,3*Constants.SCREEN_HEIGHT/4));
        player.update(playerPoint);

        obstacleManager = new ObstacleManager(200,350,75,Color.GREEN);
    }

    public void reset(){
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, (3*Constants.SCREEN_HEIGHT)/4);
        player.update(playerPoint);
        obstacleManager = new ObstacleManager(200,350,75,Color.BLACK);
        movingPlayer = false;
    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE =0;
    }

    @Override
    public void recieveTouch(MotionEvent event) {
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

    }

    @Override
    public void draw(Canvas canvas) {
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

    public void update() {
        if(!gameOver) {
            player.update(playerPoint);
            obstacleManager.update();
            if(obstacleManager.playerCollide(player)){
                gameOver = true;
                gameOverTime = System.currentTimeMillis();
            }
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
