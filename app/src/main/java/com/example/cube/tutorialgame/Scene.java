package com.example.cube.tutorialgame;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by cube on 3/25/2018.
 */

public interface Scene {
    public void update();
    public void draw(Canvas canvas);
    public void terminate();
    public void recieveTouch (MotionEvent event);
}
