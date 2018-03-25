package com.example.cube.tutorialgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by cube on 3/25/2018.
 */

public class Animation {
    private Bitmap[] frames;
    private int frameIndex;

    private boolean isPlaying = false;

    private float frameTime;

    private long lastFrame;

    public boolean isPlaying(){
        return isPlaying;
    }

    public void play() {
        isPlaying = true;
        frameIndex = 0;
        lastFrame = System.currentTimeMillis();
    }

    public void stop() {
        isPlaying = false;
    }

    public Animation(Bitmap[] frames, float animTime){
        this.frames = frames;
        frameIndex = 0;

        frameTime = animTime/frames.length;

        lastFrame = System.currentTimeMillis();

    }

    public void draw(Canvas canvas, Rect destination){
        if(!isPlaying)
            return;

        scaleRect(destination);

        canvas.drawBitmap(frames[frameIndex],null,destination,new Paint());
    }

    private void scaleRect(Rect rect) {
        float whRatio = (float)(frames[frameIndex].getWidth())/frames[frameIndex].getHeight();
        if (rect.width()>rect.height())
            rect.left =(int) (rect.right - (rect.height() * whRatio));
        else
            rect.top = (int) (rect.bottom - (rect.width() * 1/whRatio));
    }

    public void update() {
        if(System.currentTimeMillis() - lastFrame > frameIndex* 1000){
            frameIndex++;

            frameIndex = frameIndex >= frames.length ? 0 : frameIndex;

            lastFrame = System.currentTimeMillis();
        }
    }
}