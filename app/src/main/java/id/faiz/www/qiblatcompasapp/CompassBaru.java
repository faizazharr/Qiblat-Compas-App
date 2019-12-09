package id.faiz.www.qiblatcompasapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import static android.view.View.INVISIBLE;

public class CompassBaru extends AppCompatActivity implements SensorEventListener {
    ImageView ic_compass;
    private static SensorManager sensorManager;
    private static Sensor sensor;
    private float currentDegree;
    private float currentDegreeNeedle = 0f;

    Button tombol_Back;
    SharedPreferences prefs;
    private ImageView arrowViewQiblat;
    Location userLoc = new Location("service Provider");


    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass_baru);
        ic_compass = findViewById(R.id.ic_compass);
        arrowViewQiblat = findViewById(R.id.jarum_kiblat);
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        prefs = getSharedPreferences("", MODE_PRIVATE);


        if (sensor != null){
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        }else {
            Toast.makeText(getApplicationContext(), "Not Support", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int degree = Math.round(sensorEvent.values[0]);
        float head = Math.round(sensorEvent.values[0]);
        float bearTo;

        Location destinationLoc = new Location("service Provider");

        destinationLoc.setLatitude(21.422487); //kaaba latitude setting
        destinationLoc.setLongitude(39.826206); //kaaba longitude setting
        bearTo = userLoc.bearingTo(destinationLoc);
        //bearTo = The angle from true north to the destination location from the point we're your currently standing.(asal image k N se destination taak angle )
        //head = The angle that you've rotated your phone from true north. (jaise image lagi hai wo true north per hai ab phone jitne rotate yani jitna image ka n change hai us ka angle hai ye)
        GeomagneticField geoField = new GeomagneticField(Double.valueOf(userLoc.getLatitude()).floatValue(), Double
                .valueOf(userLoc.getLongitude()).floatValue(),
                Double.valueOf(userLoc.getAltitude()).floatValue(),
                System.currentTimeMillis());
        head -= geoField.getDeclination(); // converts magnetic north into true north

        if (bearTo < 0) {
            bearTo = bearTo + 360;
            //bearTo = -100 + 360  = 260;
        }

        float direction = bearTo + head;

        // If the direction is smaller than 0, add 360 to get the rotatigon clockwise.
        if (direction < 0) {
            direction = direction + 360;
        }


        RotateAnimation raQibla = new RotateAnimation(currentDegreeNeedle, direction,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        raQibla.setDuration(500);
        raQibla.setFillAfter(true);

        currentDegreeNeedle = -direction;

        arrowViewQiblat.startAnimation(raQibla);


        RotateAnimation animation = new RotateAnimation(currentDegree, -degree,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setDuration(500);
        animation.setFillAfter(true);
        ic_compass.setAnimation(animation);
        currentDegree=-degree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
//    public void adjustArrowQiblat(float azimuth) {
//        Animation an = new RotateAnimation(-(currentAzimuth)+kiblat_derajat, -azimuth,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//                0.5f);
//        currentAzimuth = (azimuth);
//        an.setDuration(500);
//        an.setRepeatCount(0);
//        an.setFillAfter(true);
//        arrowViewQiblat.startAnimation(an);
//
//    }
//
//    public void arrowkiblat(){
//        Location kaabaLocation = new Location("");
//        kaabaLocation.setLatitude(21.4225);
//        kaabaLocation.setLongitude(39.8262);
//        float kaabaBearing = userLocation.bearingTo(kaabaLocation);
//    }

}
