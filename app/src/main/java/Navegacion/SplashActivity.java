package Navegacion;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestioninventario.R;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        //Animacion de la pantalla
        Animation animacion1= AnimationUtils.loadAnimation(this, R.anim.desplazamiento_arriba);
        Animation animacion2= AnimationUtils.loadAnimation(this, R.anim.desplazamiento_abajo);


        TextView gestionInventarioTextView=findViewById(R.id.gestionInventarioTextView);
        ImageView pantallaSplashImageView= findViewById(R.id.pantallaSplashImageView);


        gestionInventarioTextView.setAnimation(animacion2);
        pantallaSplashImageView.setAnimation(animacion1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }

        },2000);


    }
}
