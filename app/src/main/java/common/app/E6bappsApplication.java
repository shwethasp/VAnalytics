package common.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings.Secure;
import android.util.Log;

import java.io.InputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Timer;
import java.util.TimerTask;


import common.util.E6bappsPreferences;


public abstract class E6bappsApplication extends Application {
    public static final String AD_INITIALIZED = "ad_initialized";
    private static final long GRACE_PERIOD = 1500;
    private static final String TAG = E6bappsApplication.class.getCanonicalName();
  //  private GaManager gaManager;
    private int mActivityCount;
//    private IAppRate mAppRate;
 //   protected TagData tagData;

    public class E6bappsExceptionHandler implements UncaughtExceptionHandler {
        private UncaughtExceptionHandler oldHandler;

        public E6bappsExceptionHandler(UncaughtExceptionHandler oldHandler) {
            this.oldHandler = oldHandler;
        }

        public void uncaughtException(Thread thread, Throwable throwable) {
            this.oldHandler.uncaughtException(thread, throwable);
         ///   E6bappsApplication.this.mAppRate.appCrash();
        }
    }

//    public abstract ErrorHandler getErrorHandler();

    public abstract boolean hasAds();

    public void onCreate() {
        super.onCreate();
        //this.gaManager = new GaManager(this);
      //  this.mAppRate = new AppRateImpl(this);
      /*  if (hasAds()) {
            PendingResult<ContainerHolder> pending = TagManager.getInstance(this).loadContainerPreferNonDefault(getString(R.raw.e6bapps_gtm_id), R.raw.gtm_default_container);
            this.tagData = (TagData) new Gson().fromJson(loadJSONFromAsset(R.raw.gtm_default_container), TagData.class);
            pending.setResultCallback(new ResultCallback<ContainerHolder>() {
                public void onResult(ContainerHolder containerHolder) {
                    TagData newTagData = E6bappsApplication.this.parseContainer(containerHolder.getContainer());
                    if (newTagData != null) {
                        E6bappsApplication.this.tagData = newTagData;
                        E6bappsApplication.this.tagDataInitialized();
                    }
                }
            }, 2, TimeUnit.SECONDS);
        }*/
      //  this.mAppRate.appLaunch();
    }

   /* public GaManager getGaManager() {
        return this.gaManager;
    }*/

  /*  private TagData parseContainer(Container container) {
        String tagDataJson = container.getString("tagDataJson");
        Log.d(TAG, "parseContainer: " + tagDataJson);
        TagData tagData = null;
        try {
            return (TagData) new Gson().fromJson(tagDataJson, TagData.class);
        } catch (Exception e) {
            Log.e(TAG, "parseContainer");
            return tagData;
        }
    }*/

    private void tagDataInitialized() {
        Intent intent = new Intent();
        intent.setAction(AD_INITIALIZED);
        sendBroadcast(intent);
    }

  /*  public TagData getTagData() {
        return this.tagData;
    }

    public TagAdData getTagAdData() {
        if (this.tagData != null) {
            return this.tagData.getTagAdData();
        }
        return null;
    }*/

    public String loadJSONFromAsset(int jsonRaw) {
        try {
            InputStream is = getResources().openRawResource(jsonRaw);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            return json;
        } catch (Exception ex) {
            Log.e(TAG, "loadJSONFromAsset", ex);
            return null;
        }
    }

    public void pauseActivity() {
        new Timer().schedule(new TimerTask() {
            public void run() {
                E6bappsApplication.this.mActivityCount = E6bappsApplication.this.mActivityCount - 1;
                if (E6bappsApplication.this.mActivityCount < 0) {
                    E6bappsApplication.this.mActivityCount = 0;
                }
                Log.d(E6bappsApplication.TAG, "Activity count decreased to: " + E6bappsApplication.this.mActivityCount);
                if (E6bappsApplication.this.mActivityCount == 0) {
                    E6bappsPreferences.setLaunch(E6bappsApplication.this, true);
                }
            }
        }, GRACE_PERIOD);
    }

    public void resumeActivity() {
        this.mActivityCount++;
        Log.d(TAG, "Activity count increase to: " + this.mActivityCount);
        if (E6bappsPreferences.isLaunch(this)) {
            E6bappsPreferences.setLaunch(this, false);
            E6bappsPreferences.resetOnboardingCount(this);
        }
    }

    public static final String getUserDeviceId(Context context) {
        return Secure.getString(context.getContentResolver(), "android_id");
    }
}
