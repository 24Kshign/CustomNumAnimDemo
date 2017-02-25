package com.jack.candyhh.customnumanimdemo.listener;

import android.animation.Animator;

/**
 * Created by jack on 17/2/25
 * 为了只让外部实现onAnimationRepeat方法
 */
public abstract class CustomAnimListener implements Animator.AnimatorListener {

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }
}
