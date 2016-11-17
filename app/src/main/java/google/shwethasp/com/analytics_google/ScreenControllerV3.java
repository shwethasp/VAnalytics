package google.shwethasp.com.analytics_google;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import google.shwethasp.com.analytics_google.activity.BaseLoginActivity;
import google.shwethasp.com.analytics_google.activity.LoginActivity;
//import google.shwethasp.com.analytics_google.interfaces.D2EComponent;
//import google.shwethasp.com.analytics_google.interfaces.IAccountDao;
//import google.shwethasp.com.analytics_google.interfaces.IProfileDao;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;
import retrofit.RetrofitError;
import retrofit.client.Response;

import android.support.multidex.MultiDexApplication;
public class ScreenControllerV3 extends MultiDexApplication {
    public static final int ACCOUNT_REQUEST_CODE = 14;
    public static final int AUTH_TOKEN_REQUEST_CODE = 12;
    private static final boolean DEVELOPER_MODE = false;
    public static final int DISPATCHER_REQUEST_CODE = 15;
    public static final int LOGIN_REQUEST_CODE = 11;
    public static final int MENU_REQUEST_CODE = 13;
    public static final int NEED_LOGIN = 101;
    public static final int PROFILE_REQUEST_CODE = 10;
    public static final int REFRESH_PROFILE_REQUEST_CODE = 16;
    private static final String TAG = "ScreenController";
//    private D2EComponent component;
//    private ErrorHandler errorHandler;
    @Inject
//    protected IAccountDao mAccountDao;
    private Handler mHandler = new Handler();
//    private JobManager mJobManager;
//    @Inject
//    protected IProfileDao mProfileDao;
//    private Profile selectedProfile;
//    private TokenService tokenService;

    public interface AuthenticateListener {
        void authResult(boolean z);
    }

    /*public static final class DaggerComponentInitializer {
        public static D2EComponent init(ScreenControllerV3 app) {
            return DaggerD2EComponent.builder().analyticsModule(new AnalyticsModule(app)).build();

        }
    }*/

    public class LogoutEvent {
    }

    public void onCreate() {
        Log.i(TAG, "OnCreate");
        super.onCreate();
        enableDeveloperMode();
        Fabric.with(this, new Kit[]{new Crashlytics()});
//        buildComponentAndInject();
//         this.component.injectApplication(this);
//        this.tokenService = new TokenService(this);
       // this.errorHandler = new GAErrorHandler(this);
//        loadSelectedAccount();
//        configureJobManager();
        Crashlytics.getInstance().core.setUserIdentifier(getUserDeviceId());
    }

    private void enableDeveloperMode() {
    }
/*
    private void configureJobManager() {
        this.mJobManager = new JobManager((Context) this, new Builder(this).minConsumerCount(1).maxConsumerCount(3).loadFactor(3).consumerKeepAlive(120).build());
    }*/

/*    public JobManager getJobManager() {
        return this.mJobManager;
    }*/

    /*public void closeApp() {
    }

    public boolean hasValidTimeZone() {
        return TimeZone.getTimeZone(getProfile().getTimeZone()) != null ? true : DEVELOPER_MODE;
    }

    public Date getTimeZoneDate() {
        return AnalyticsDates.getTimeZoneDate(getProfile().getTimeZone());
    }

    public Date getFromDate() {
        int weeks = AnalyticsPreferences.getPeriod(this);
        Profile profile = getProfile();
        if (profile == null) {
            return null;
        }
        if (weeks != -1) {
            return AnalyticsDates.getFromDate(weeks, profile.getTimeZone(), AnalyticsPreferences.isIncludedToday(this));
        }
        return AnalyticsPreferences.getCustomFromDate(this).getTime();
    }

    public Date getToDate() {
        int weeks = AnalyticsPreferences.getPeriod(this);
        Profile profile = getProfile();
        if (profile == null) {
            return null;
        }
        if (weeks != -1) {
            return AnalyticsDates.getToDate(weeks, profile.getTimeZone(), AnalyticsPreferences.isIncludedToday(this));
        }
        return AnalyticsPreferences.getCustomToDate(this).getTime();
    }

    public boolean isSameDate(Date date) {
        Calendar compCal = Calendar.getInstance();
        compCal.setTime(date);
        Calendar todayCal = Calendar.getInstance();
        todayCal.setTime(AnalyticsDates.getTimeZoneDate(getProfile().getTimeZone()));
        return (compCal.get(1) == todayCal.get(1) && compCal.get(6) == todayCal.get(6)) ? true : DEVELOPER_MODE;
    }

    public static boolean isScreenOn(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Constants.SCREEN_ON, true);
    }

    public static void turnScreenOnOff(Activity activity) {
        if (isScreenOn(activity)) {
            activity.getWindow().addFlags(128);
        } else {
            activity.getWindow().clearFlags(128);
        }
    }*/

   /* public void logout() {
        this.selectedProfile = null;
    }

    public boolean isLogin() {
        return this.selectedProfile != null ? true : DEVELOPER_MODE;
    }*/

    public void onLowMemory() {
        Log.i(TAG, "------------ OnLowMemory ------------");
        super.onLowMemory();
    }

    public String getUserDeviceId() {
        return Secure.getString(getContentResolver(), "android_id");
    }

   /* public String getCoin() {
        Profile p = getProfile();
        if (p == null || p.getCurrency() == null) {
            return "$";
        }
        return p.getCurrency();
    }

    private void loadSelectedAccount() {
        String profileId = AnalyticsPreferences.getProfileId(this);
        if (profileId != null) {
            Profile profile = this.mProfileDao.getProfile(profileId);
            if (profile != null) {
                setSelectedProfile(profile);
            }
        }
    }*/

   /* public void refreshToken(Account account) throws Exception {
        this.tokenService.refresh(account, this.mHandler);
    }

    public Profile getProfile() {
        return this.selectedProfile;
    }

    public List<Profile> getProfiles() {
        return getSelectedAccount().getProfiles();
    }

    private void setSelectedProfile(Profile profile) {
        this.selectedProfile = profile;
       // Formatter.getInstance().setCoin(getCoin());
    }

    public Profile getSelectedProfile() {
        return this.selectedProfile;
    }

    public String getProfileName() {
        Profile profile = getSelectedProfile();
        return profile != null ? profile.getName() : BuildConfig.FLAVOR;
    }

    public void setSelectedAccount(Profile p) {
        AnalyticsPreferences.saveSelectedProfile(getBaseContext(), p.getId());
        loadSelectedAccount();
        p.getAccount().setLastProfileId(p.getId());
        EventBus.getDefault().post(p);
    }

    public int getSelectedProfilePosition() {
        List<Profile> profiles = getProfiles();
        Profile p = getSelectedProfile();
        if (profiles == null || p == null) {
            return -1;
        }
        for (int i = 0; i < profiles.size(); i++) {
            if (((Profile) profiles.get(i)).getId().equals(p.getId())) {
                return i;
            }
        }
        return -1;
    }
*/


    public boolean hasAds() {
        return true;
    }

//    public ErrorHandler getErrorHandler() {
//        return this.errorHandler;
//    }

   /* public Account getSelectedAccount() {
        Account account = null;
        if (AnalyticsPreferences.isFavouriteSelected(this)) {
            account = this.mAccountDao.getFavouriteAccount();
        }
        if (account == null && this.selectedProfile != null) {
            account = this.selectedProfile.getAccount();
        }
        if (account != null) {
            return account;
        }
        //TODO: Commented as it is showing error.
        //account = new Account();
        EventBus.getDefault().post(new LogoutEvent());
        return account;
    }*/

    public void tagDataInitialized() {
    }

    public static void handleError(Activity activity, Exception exception) {
//        ErrorHandler errorHandler = ((E6bappsApplication) activity.getApplication()).getErrorHandler();
        boolean handled = DEVELOPER_MODE;
        Response r = null;
        if (exception.getCause() instanceof RetrofitError) {
            r = ((RetrofitError) exception.getCause()).getResponse();
        } else if (exception.getCause() != null && (exception.getCause().getCause() instanceof RetrofitError)) {
            r = ((RetrofitError) exception.getCause().getCause()).getResponse();
        }
        if (r != null) {
            int statusCode = r.getStatus();
            if (statusCode == 401 || statusCode == 403) {
                handled = true;
                if (activity instanceof BaseLoginActivity) {
                    showProfileError(activity);
                } else {
                    startLoginActivity(activity);
                }
            }
        }
     /*   if (!handled) {
            errorHandler.handle(exception);
        }*/
    }

    public static void showProfileError(Context context) {
        AlertDialog profileErrorDialog = new AlertDialog.Builder(context).create();
        profileErrorDialog.setTitle(context.getResources().getString(R.string.Login));
        profileErrorDialog.setMessage(context.getResources().getString(R.string.ProfileError));
        profileErrorDialog.setButton(-1, "Ok", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
       // profileErrorDialog.setIcon(17301543);
        profileErrorDialog.show();
    }

    public static void startLoginActivity(Activity activity) {
//        ((ScreenControllerV3) activity.getApplication()).logout();
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.setFlags(32768);
        activity.startActivity(intent);
        activity.finish();
    }

 /*   public void buildComponentAndInject() {
        this.component = DaggerComponentInitializer.init(this);
    }

    public static D2EComponent component(Context context) {
        return ((ScreenControllerV3) context.getApplicationContext()).component;
    }*/
}
