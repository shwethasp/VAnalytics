package common.ads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/*import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;*/

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import common.app.E6bappsApplication;

import common.util.Constants;
import common.util.E6bappsPreferences;
import google.vmokshagroup.com.analytics_google.R;


public class AdManager implements OnClickListener {
    private static final long AD_ANIM_DURATION = 1000;
    private static final int MIN_ONBOARDING_COUNT = 3;
    private static final int[][] MOVES = new int[][]{new int[]{-1, 0}, new int[]{0, -1}};
    private static final String TAG = AdManager.class.getCanonicalName();
    private Activity activity;
    private ViewGroup adHolder;
    private ImageView adInHouse;
    private AdListener adListener = new AdListener() {
        public void onAdLoaded() {
            Log.i(AdManager.TAG, "onReceiveAd::ad simple");
            if (AdManager.this.adHolder != null) {
               /* AdManager.this.application.getGaManager().trackEvent(Constants.ADS, Constants.AD_REQUEST, null);
                AdManager.this.adInHouse.setVisibility(8);
                E6bappsPreferences.simpleAdCountPerDay(AdManager.this.activity);*/
            }
        }

        public void onAdLeftApplication() {
            Log.i(AdManager.TAG, "AdManager: onLeaveApplication");
           // AdManager.this.application.getGaManager().trackEvent(Constants.ADS, Constants.AD_CLIC, null);
           /* E6bappsPreferences.adClic(AdManager.this.activity, AdManager.this.tagData.getSimpleAd().getHideTime());
            if (AdManager.this.tagData.isHideAfterClic()) {
                AdManager.this.adHolder.setVisibility(8);
            }*/
        }

        public void onAdFailedToLoad(int errorCode) {
            Log.i(AdManager.TAG, "AdManager:  onFailedToReceiveAd");
          //  AdManager.this.application.getGaManager().trackEvent(Constants.ADS, Constants.AD_FAIL, null);
        }
    };
    private AdManagerListener adManagerListener;
    private AdRequest adRequest;
    private E6bappsApplication application;
    private InterstitialAd interstitial;
    private AdListener interstitialAdListener = new AdListener() {
        public void onAdLoaded() {
            Log.i(AdManager.TAG, "onReceiveAd::ad = interstitial");
          //  AdManager.this.application.getGaManager().trackEvent(Constants.ADS, Constants.AD_FULL_SCREEN_RECEIVED, null);
        }

        public void onAdOpened() {
          //  E6bappsPreferences.fullScreenAdCountPerDay(AdManager.this.activity);
          //  AdManager.this.application.getGaManager().trackEvent(Constants.ADS, Constants.AD_FULL_SCREEN_SHOWED, null);
        }

        public void onAdLeftApplication() {
            Log.i(AdManager.TAG, "AdManager: onLeaveApplication");
         //   AdManager.this.application.getGaManager().trackEvent(Constants.ADS, Constants.AD_FULL_SCREEN_CLIC, null);
         //   E6bappsPreferences.adFullScreenClic(AdManager.this.activity, AdManager.this.tagData.getFullScreenAd().getHideTime());
         //   if (AdManager.this.adManagerListener != null) {
         //       AdManager.this.adManagerListener.adFullScreenDismiss();
          //  }
        }

        public void onAdFailedToLoad(int errorCode) {
            Log.i(AdManager.TAG, "AdManager:  onFailedToReceiveAd");
          //  AdManager.this.application.getGaManager().trackEvent(Constants.ADS, Constants.AD_FULL_SCREEN_FAIL, null);
        }

        public void onAdClosed() {
            Log.i(AdManager.TAG, "AdManager:  onDismissScreen");
          //  AdManager.this.application.getGaManager().trackEvent(Constants.ADS, Constants.AD_FULL_SCREEN_CLOSED, null);
          //  E6bappsPreferences.adFullScreenClosed(AdManager.this.activity);
          //  if (AdManager.this.adManagerListener != null) {
          //      AdManager.this.adManagerListener.adFullScreenDismiss();
          //  }
        }
    };
    private AdView lastAdView;


    public interface AdManagerListener {
        void adFullScreenDismiss();
    }

   /* public AdManager(Activity activity, TagAdData tagAdData) {
        this.application = (E6bappsApplication) activity.getApplication();
        this.activity = activity;
        this.tagData = tagAdData;
    }*/

    public void initSimpleAd(ViewGroup adHolder) {
       /* Log.i(TAG, "initSimpleAd");
        this.adHolder = adHolder;
        this.adInHouse = (ImageView) adHolder.findViewById(R.id.ad_in_house);
        if (hideSimpleAd()) {
            adHolder.setVisibility(8);
            Log.i(TAG, "initSimpleAd::hideAd");
            return;
        }
        this.adInHouse.setOnClickListener(this);
        if (E6bappsPreferences.getSimpleAdCountPerDay(this.activity) < this.tagData.getSimpleAd().getTimesPerDay()) {
            adHolder.setDrawingCacheEnabled(true);
            initAds();
            requestAds();
            Log.i(TAG, "initSimpleAd::requestAd");
        }*/
    }

    private void requestAds() {
     /*   this.lastAdView = (AdView) this.adHolder.findViewById(R.id.adView);
        if (this.lastAdView == null) {
            this.lastAdView = getAdView();
            this.adHolder.addView(this.lastAdView, 0);
        }
        this.lastAdView.loadAd(this.adRequest);
        this.lastAdView.setAdListener(this.adListener)*/;
    }

//    private void initAds() {
//        this.adRequest = new Builder().build();
//    }
//
   /* private AdView getAdView() {
        String adId = this.activity.getString(R.string.e6bapps_ad_id);
        Log.i(TAG, "Init ad id: " + adId);
        AdView adView = new AdView(this.activity);
        adView.setAdUnitId(adId);
        adView.setAdSize(getAdSize());
        return adView;
    }*/

  /*  @SuppressLint({"NewApi"})
    private AdSize getAdSize() {
        AdSize adSize = AdSize.SMART_BANNER;
        if (VERSION.SDK_INT < 13) {
            return adSize;
        }
        Configuration config = this.activity.getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= PaperSize.EXECUTIVE_HEIGHT) {
            return AdSize.LEADERBOARD;
        }
        if (config.smallestScreenWidthDp >= 600) {
            return AdSize.FULL_BANNER;
        }
        return adSize;
    }
*/
    /*public void initFullScreenAdView(AdManagerListener listener) {
        initFullScreenAdView(listener, this.activity.getString(R.string.e6bapps_ad_interstitial_id));
    }
*/
  /*  public void initFullScreenAdView(AdManagerListener listener, String adUnit) {
        Log.i(TAG, "initFullScreenAdView");
        if (canShowInterstitial()) {
            this.adManagerListener = listener;
            this.interstitial = new InterstitialAd(this.activity);
            this.interstitial.setAdUnitId(adUnit);
            AdRequest adRequest = new Builder().build();
            this.interstitial.setAdListener(this.interstitialAdListener);
            this.interstitial.loadAd(adRequest);
            Log.i(TAG, "initFullScreenAdView::requestAd");
        }
    }*/

 /*   private boolean hideSimpleAd() {
        boolean z;
        boolean show = this.tagData.getSimpleAd().isShow();
        if (show && this.tagData.isHideAfterClic()) {
            show = E6bappsPreferences.getAdLastHideTime(this.activity) - System.currentTimeMillis() < 0;
        }
        String str = TAG;
        StringBuilder append = new StringBuilder().append("hideSimpleAd::hide = ");
        if (show) {
            z = false;
        } else {
            z = true;
        }
        Log.i(str, append.append(z).toString());
        if (show) {
            return false;
        }
        return true;
    }

    public boolean hideFullScreenAd() {
        boolean show;
        boolean z;
        if (!this.tagData.getFullScreenAd().isShow() || System.currentTimeMillis() - E6bappsPreferences.getAdFullScreenShowed(this.activity) <= this.tagData.getFullScreenAd().getDelayTime()) {
            show = false;
        } else {
            show = true;
        }
        if (show && this.tagData.isHideAfterClic()) {
            if (E6bappsPreferences.getAdLastHideTime(this.activity) - System.currentTimeMillis() < 0) {
                show = true;
            } else {
                show = false;
            }
        }
        String str = TAG;
        StringBuilder append = new StringBuilder().append("hideFullScreenAd::hide = ");
        if (show) {
            z = false;
        } else {
            z = true;
        }
        Log.i(str, append.append(z).toString());
        if (show) {
            return false;
        }
        return true;
    }
*/
    public boolean hasFullScreenAd() {
        return this.interstitial != null && this.interstitial.isLoaded();
    }

    public void showFullScreenAd() {
        if (this.interstitial != null) {
            this.interstitial.show();
            E6bappsPreferences.adFullScreenShowed(this.activity);
        }
    }

  /*  public boolean showFullScreenAd(Context context) {
        if (!hasFullScreenAd() || !canStartShowingInterstitial(context) || !canShowInterstitial()) {
            return false;
        }
        this.interstitial.show();
        E6bappsPreferences.adFullScreenShowed(this.activity);
        return true;
    }*/

    public void onClick(View v) {
        buyProVersion(this.activity);
    }

    public static void buyProVersion(Context context) {
        Intent i = new Intent("android.intent.action.VIEW", Uri.parse(context.getResources().getString(R.string.e6bapps_ad_package)));
        i.setFlags(268435456);
        context.startActivity(i);
    }

    private void animateAds(final View oldAd, final View newAd) {
        int[] move = MOVES[(int) Math.round(Math.random())];
        TranslateAnimation animOldAd = new TranslateAnimation(0.0f, (float) (move[0] * this.adHolder.getWidth()), 0.0f, (float) (move[1] * this.adHolder.getHeight()));
        animOldAd.setDuration(AD_ANIM_DURATION);
        animOldAd.setFillAfter(false);
        animOldAd.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
                oldAd.setVisibility(0);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                oldAd.setVisibility(8);
            }
        });
        for (int i = 0; i < move.length; i++) {
            move[i] = move[i] * -1;
        }
        TranslateAnimation animNewAd = new TranslateAnimation((float) (move[0] * this.adHolder.getWidth()), 0.0f, (float) (move[1] * this.adHolder.getHeight()), 0.0f);
        animNewAd.setDuration(AD_ANIM_DURATION);
        animNewAd.setFillAfter(false);
        animNewAd.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
                newAd.setVisibility(0);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
            }
        });
        oldAd.startAnimation(animOldAd);
        newAd.startAnimation(animNewAd);
    }
/*
    public boolean canStartShowingInterstitial(Context context) {
        int count = E6bappsPreferences.getOnboardingCount(context);
        Log.d(TAG, "canStartShowingInterstitial count: " + count);
        return count >= this.tagData.getFullScreenAd().getMinOnBoardingPerSession();
    }

    public boolean canShowInterstitial() {
        return !hideFullScreenAd() && E6bappsPreferences.getFullScreenAdCountPerDay(this.activity) < this.tagData.getFullScreenAd().getTimesPerDay();
    }*/
}
