package com.bluekeroro.android.sunset;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by BlueKeroro on 2018/4/11.
 */
public class SunsetFragment extends Fragment {
    private View mSceneView;
    private View mSunView;
    private View mSkyView;
    private int mBlueSkyColor;
    private int mSunsetSkyColor;
    private int mNightSkyColor;
    private boolean isNight;
    private AnimatorSet animatorSet;
    public static SunsetFragment newInstance(){
        return new SunsetFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isNight=false;
        View view=inflater.inflate(R.layout.fragment_sunset,container,false);
        mSceneView=view;
        mSunView=view.findViewById(R.id.sun);
        mSkyView=view.findViewById(R.id.sky);
        Resources resource=getResources();
        mBlueSkyColor=resource.getColor(R.color.blue_sky);
        mSunsetSkyColor=resource.getColor(R.color.sunset_sky);
        mNightSkyColor=resource.getColor(R.color.night_sky);
        mSceneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(animatorSet!=null){
                    animatorSet.cancel();
                }
                if(isNight){

                    startAnimationReturn();
                }else{
                    startAnimation();
                }
                isNight=!isNight;
            }
        });
        sunChange();
        return view;
    }
    private void startAnimation(){
        float sunYStart=mSunView.getTop();
        float sunYEnd=mSkyView.getHeight();
        ColorDrawable currentColor=(ColorDrawable)mSkyView.getBackground();
        ObjectAnimator heightAnimator=ObjectAnimator.ofFloat(mSunView,"y",sunYStart+mSunView.getTranslationY(),sunYEnd)
                .setDuration(3000);
        heightAnimator.setInterpolator(new AccelerateInterpolator());
        ObjectAnimator sunsetSkyAnimator=ObjectAnimator.ofInt(mSkyView,"backgroundColor",currentColor.getColor(),mSunsetSkyColor)
                .setDuration(3000);
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());
        ObjectAnimator nightSkyAnimator=ObjectAnimator.ofInt(mSkyView,"backgroundColor",mSunsetSkyColor,mNightSkyColor)
                .setDuration(1500);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());
        animatorSet=new AnimatorSet();
        if(sunYStart+mSunView.getTranslationY()==sunYEnd&&currentColor.getColor()!=mNightSkyColor){
            nightSkyAnimator=ObjectAnimator.ofInt(mSkyView,"backgroundColor",currentColor.getColor(),mNightSkyColor)
                    .setDuration(1500);
            nightSkyAnimator.setEvaluator(new ArgbEvaluator());
            animatorSet.play(nightSkyAnimator);
        }else{
            animatorSet.play(heightAnimator)
                    .with(sunsetSkyAnimator)
                    .before(nightSkyAnimator);
        }
        animatorSet.start();
    }
    private void startAnimationReturn(){
        float sunYStart=mSkyView.getHeight();
        float sunYEnd=mSunView.getTop();
        ColorDrawable currentColor=(ColorDrawable)mSkyView.getBackground();
        ObjectAnimator heightAnimator=ObjectAnimator.ofFloat(mSunView,"y",sunYEnd+mSunView.getTranslationY(),sunYEnd)
                .setDuration(3000);
        heightAnimator.setInterpolator(new AccelerateInterpolator());
        ObjectAnimator sunriseSkyAnimator=ObjectAnimator.ofInt(mSkyView,"backgroundColor",currentColor.getColor(),mBlueSkyColor)
                .setDuration(3000);
        sunriseSkyAnimator.setEvaluator(new ArgbEvaluator());
        //currentColor=(ColorDrawable)mSkyView.getBackground();
        ObjectAnimator nightSkyAnimator=ObjectAnimator.ofInt(mSkyView,"backgroundColor",currentColor.getColor(),mSunsetSkyColor)
                .setDuration(1500);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());
        animatorSet=new AnimatorSet();

        if(mSunView.getTop()+mSunView.getTranslationY()<mSkyView.getHeight()){
            animatorSet.play(heightAnimator)
                    .with(sunriseSkyAnimator);
        }else if(currentColor.getColor()!=mNightSkyColor){
            sunriseSkyAnimator=ObjectAnimator.ofInt(mSkyView,"backgroundColor",mSunsetSkyColor,mBlueSkyColor)
                    .setDuration(3000);
            sunriseSkyAnimator.setEvaluator(new ArgbEvaluator());
            nightSkyAnimator=ObjectAnimator.ofInt(mSkyView,"backgroundColor",currentColor.getColor(),mSunsetSkyColor)
                    .setDuration(1500);
            nightSkyAnimator.setEvaluator(new ArgbEvaluator());
            animatorSet.play(heightAnimator)
                    .with(sunriseSkyAnimator)
                    .after(nightSkyAnimator);
        }else{
            sunriseSkyAnimator=ObjectAnimator.ofInt(mSkyView,"backgroundColor",mSunsetSkyColor,mBlueSkyColor)
                    .setDuration(3000);
            sunriseSkyAnimator.setEvaluator(new ArgbEvaluator());
            nightSkyAnimator=ObjectAnimator.ofInt(mSkyView,"backgroundColor",mNightSkyColor,mSunsetSkyColor)
                    .setDuration(1500);
            nightSkyAnimator.setEvaluator(new ArgbEvaluator());
            animatorSet.play(heightAnimator)
                    .with(sunriseSkyAnimator)
                    .after(nightSkyAnimator);
        }
        animatorSet.start();
    }
    void sunChange(){
        float changeScale=0.8f;
        ObjectAnimator sunChangeXAnimator=ObjectAnimator.ofFloat(mSunView,"scaleX",1,changeScale)
                .setDuration(1500);
        sunChangeXAnimator.setRepeatCount(ValueAnimator.INFINITE);
        sunChangeXAnimator.setRepeatMode(ValueAnimator.REVERSE);
        ObjectAnimator sunChangeYAnimator=ObjectAnimator.ofFloat(mSunView,"scaleY",1,changeScale)
                .setDuration(1500);
        sunChangeYAnimator.setRepeatCount(ValueAnimator.INFINITE);
        sunChangeYAnimator.setRepeatMode(ValueAnimator.REVERSE);
        sunChangeXAnimator.start();
        sunChangeYAnimator.start();
    }
}
