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

    private OrientationData orientationData;
    private long frameTime;

    public GameplayScene(){
        player = new RectPlayer(new Rect(100,100,200,200), Color.rgb(255,0,0));
        playerPoint = new Point(new Point(Constants.SCREEN_WIDTH/2,3*Constants.SCREEN_HEIGHT/4));
        player.update(playerPoint);

        obstacleManager = new ObstacleManager(200,350,75,Color.GREEN);

        orientationData = new OrientationData();
        orientationData.register();
        frameTime = System.currentTimeMillis();
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
                    orientationData.newGame();
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
            if (frameTime < Constants.INIT_TIME)
                frameTime = Constants.INIT_TIME;
            int elapsedTime = (int) (System.currentTimeMillis() - frameTime);
            frameTime = System.currentTimeMillis();
            if (orientationData.getOrientation() != null && orientationData.getStartOrientation() != null){
                float pitch = orientationData.getOrientation()[1] - orientationData.getStartOrientation()[1];
                float roll = orientationData.getOrientation()[2] - orientationData.getStartOrientation()[2];

                float xSpeed = 2 * roll * Constants.SCREEN_WIDTH/500f;
                float ySpeed = pitch * Constants.SCREEN_HEIGHT/1000f;

                playerPoint.x += Math.abs(xSpeed*elapsedTime) > 5 ? xSpeed*elapsedTime : 5;
                playerPoint.y -= Math.abs(ySpeed*elapsedTime) > 5 ? ySpeed*elapsedTime : 0;

                //if u go to far u spown on the other side
                if(playerPoint.x < 0)
                    playerPoint.x = Constants.SCREEN_WIDTH;
                else if(playerPoint.x > Constants.SCREEN_WIDTH)
                    playerPoint.x = 0;

                if(playerPoint.y < 0)
                    playerPoint.y = 0;
                else if(playerPoint.y > Constants.SCREEN_HEIGHT)
                    playerPoint.y = Constants.SCREEN_HEIGHT;

            }

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
