package com.nwu.foodshake;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

// shake detection inspired from
// http://jasonmcreynolds.com/?p=388
public class ShakeDetector implements SensorEventListener {
    //----------------------------------------------------------------------------------------------
    public interface OnShakeListener {
        void onShake(int count);
    }

    //----------------------------------------------------------------------------------------------
    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
    private static final int SHAKE_SLOP_TIME_MS = 500;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;

    private final OnShakeListener mListener;
    private long mLastShake;
    private int mShakeCount;

    //----------------------------------------------------------------------------------------------
    public ShakeDetector(OnShakeListener listener) {
        mListener = listener;
    }

    //----------------------------------------------------------------------------------------------
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // ignore
    }

    //----------------------------------------------------------------------------------------------
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (mListener == null) {
            return;
        }

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        float gX = x / SensorManager.GRAVITY_EARTH;
        float gY = y / SensorManager.GRAVITY_EARTH;
        float gZ = z / SensorManager.GRAVITY_EARTH;

        // gForce will be close to 1 when there is no movement.
        float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

        if (gForce <= SHAKE_THRESHOLD_GRAVITY) {
            return;
        }

        final long now = System.currentTimeMillis();
        // ignore shake events too close to each other (500ms)
        if (mLastShake + SHAKE_SLOP_TIME_MS > now) {
            return;
        }

        // reset the shake count after 3 seconds of no shakes
        if (mLastShake + SHAKE_COUNT_RESET_TIME_MS < now) {
            mShakeCount = 0;
        }

        mLastShake = now;
        mShakeCount++;
        mListener.onShake(mShakeCount);
    }

    //----------------------------------------------------------------------------------------------
}