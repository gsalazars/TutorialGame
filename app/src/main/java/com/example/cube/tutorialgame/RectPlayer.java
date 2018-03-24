package com.example.cube.tutorialgame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import com.example.cube.tutorialgame.GameObject;

/**
 * Created by cube on 3/21/2018.
 */

public class RectPlayer implements GameObject {

    private Rect rectangle;
    private int color;

    public Rect getRectangle(){
        return rectangle;
    }

    public RectPlayer(Rect rectangle, int color){
        this.rectangle = rectangle;
        this.color = color;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle, paint);
    }

    @Override
    public void update() {

    }

    public void update(Point point){
        //left,top,right,bot
        rectangle.set(point.x-rectangle.width()/2,point.y-rectangle.height()/2,point.x + rectangle.width()/2, point.y + rectangle.height()/2);

    }
}
