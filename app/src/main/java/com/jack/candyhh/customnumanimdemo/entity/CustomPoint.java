package com.jack.candyhh.customnumanimdemo.entity;

/**
 * Created by jack on 17/2/24
 * 把每一个数字当成一个点来处理
 */
public class CustomPoint {
    private float x;   //点的x坐标
    private float y;  //点的y坐标

    public CustomPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}