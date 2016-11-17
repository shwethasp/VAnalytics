package google.shwethasp.com.analytics_google.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import google.shwethasp.com.analytics_google.R;


public class SplashActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
    /*    if (((ScreenControllerV3) getApplication()).isLogin()) {
            startActivity(new Intent(this, MainFragmentActivity.class));
            finish();
            return;
        }*/
        new Handler().postDelayed(new Runnable() {
            public void run() {
                SplashActivity.this.startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.activity_slide_left_in,
                        R.anim.activity_slide_left_out);
                SplashActivity.this.finish();
            }
        }, 2000);
    }
}
