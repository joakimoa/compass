package com.ojojoj.compass;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DecimalFormat;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {
    TextView text1; // orientation
    TextView text2; //
    TextView text3; //

    int mAzimuth;

    float[] rMat = new float[9];
    float[] orientation = new float[3];

    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;

    private SensorManager mSensorManager;
    private Sensor mRotationV, mAccelerometer, mMagnetometer;
    boolean haveSensor = false, haveSensor2 = false;

    float lastX, lastY, lastZ;
    float currentX = 0;
    float currentY = 0;
    float currentZ = 0;

    boolean music = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);

        startSensors();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rMat, event.values);
            mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;

        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(rMat, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(rMat, orientation);
            mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
        }

        //text1.setText(mAzimuth + "° ");
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//            lastX = currentX;
//            lastY = currentY;
//            lastZ = currentZ;
            currentX = event.values[0];
            currentY = event.values[1];
            currentZ = event.values[2];
//            float deltaX = lastX - currentX;
//            float deltaY = lastY - currentY;
//            float deltaZ = lastZ - currentZ;

            DecimalFormat df = new DecimalFormat("#.##");
            String formatted = df.format(currentX);
            String sx = "X: " + formatted;
            text1.setText(sx);
            formatted = df.format(currentY);
            String sy = "Y: " + formatted;
            text2.setText(sy);
            formatted = df.format(currentZ);
            String sz = "Z: " + formatted;
            text3.setText(sz);


            if (currentY >= 9 && !music) {
                System.out.println("start music");
                Intent musicServiceIntent = new Intent(getApplicationContext(), MusicService.class);
                startService(musicServiceIntent);
                music = true;
            } else if (currentY < 9 && music) {
                System.out.println("stop music");
                stopService(new Intent(SensorActivity.this, MusicService.class));
                music = false;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void startSensors() {
//        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null) {
//            if ((mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) || (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null)) {
//                //noSensorsAlert();
//                System.out.println("No sensors");
//            } else {
//                mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//                mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//                haveSensor = mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
//                haveSensor2 = mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
//            }
//        } else {
//            mRotationV = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
//            haveSensor = mSensorManager.registerListener(this, mRotationV, SensorManager.SENSOR_DELAY_UI);
//        }

        //mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // success! we have an accelerometer
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            // fai! we dont have an accelerometer!
        }
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(SensorActivity.this, MusicService.class));
        super.onDestroy();
    }
}
