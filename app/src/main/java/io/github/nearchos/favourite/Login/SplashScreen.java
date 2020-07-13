package io.github.nearchos.favourite.Login;

import androidx.appcompat.app.AppCompatActivity;
import io.github.nearchos.favourite.R;
import pl.droidsonroids.gif.GifImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private TextView load;
    private GifImageView gif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        load = findViewById(R.id.loading);
        gif = findViewById(R.id.gifImage);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        load.startAnimation(animation);
        gif.startAnimation(animation);

        final Intent i = new Intent(getApplicationContext(),Opening.class);
        Thread timer = new Thread(){
            public void run()
            {
                try{
                    sleep(5000);
                }catch(InterruptedException e)
                {
                   e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();

    }
}
