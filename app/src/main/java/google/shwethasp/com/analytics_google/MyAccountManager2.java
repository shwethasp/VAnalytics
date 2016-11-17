package google.shwethasp.com.analytics_google;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import google.shwethasp.com.analytics_google.activity.BaseLoginActivity;

public class MyAccountManager2 {
    private static final List<String> AUTH_SCOPES = new ArrayList(Arrays.asList(new String[]{"https://www.googleapis.com/auth/analytics.readonly"}));
    public static final String AUTH_TOKEN_TYPE;
    private static final String REALTIME_TYPE = "Analytics Realtime";
    private static final String TAG = MyAccountManager2.class.getCanonicalName();
    private AccountManager manager;
    public static String restoken;

    public interface MyAccountManagerListener {
        void authToken(String str);
    }

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("oauth2:");
        for (String scope : AUTH_SCOPES) {
            sb.append(scope);
            sb.append(" ");
        }
        AUTH_TOKEN_TYPE = sb.toString().trim();
    }

    public MyAccountManager2(Context context) {
        this.manager = AccountManager.get(context);
    }

    private void getToken(Account account, final MyAccountManagerListener listener) {
        if (account != null) {
            this.manager.getAuthToken(account, AUTH_TOKEN_TYPE, null, true, new AccountManagerCallback<Bundle>() {
                public void run(AccountManagerFuture<Bundle> accountManagerFuture) {
                    String token = null;
                    try {
                        Bundle bundle = (Bundle) accountManagerFuture.getResult();
                        if (bundle.containsKey("authtoken")) {
                            token = (String) bundle.get("authtoken");
                        }
                    } catch (Exception e) {
                        Log.e(MyAccountManager2.TAG, "getAuthToken", e);
                        BaseLoginActivity.dismissDialog();
                        Crashlytics.logException(e);
                    }
                    if (token != null) {
                        Log.d(MyAccountManager2.TAG, "Token getToken listener: " + token);
                        listener.authToken(token);
                    } else {
                        BaseLoginActivity.dismissDialog();
                    }

                }
            }, new Handler());
        }
    }

    public void getAsyncToken(String accountName, MyAccountManagerListener listener) {
        getToken(getAccount(accountName), listener);
    }

    private String getToken(Account account) {
        String token = null;
        if (account != null) {
            try {
                token = this.manager.blockingGetAuthToken(account, AUTH_TOKEN_TYPE, false);
            } catch (OperationCanceledException e) {
                Log.d(TAG, "OperationCanceledException", e);

                Crashlytics.logException(e);
            } catch (AuthenticatorException e2) {
                Log.d(TAG, "AuthenticatorException", e2);
                Crashlytics.logException(e2);

            } catch (IOException e3) {
                Log.d(TAG, "IOException", e3);
                Crashlytics.logException(e3);

            }
        }
        Log.d(TAG, "Token getToken: " + token);
        return token;

    }

    public String invalidateToken(String accountName, Handler handler) throws OperationCanceledException, AuthenticatorException, IOException {
        String validToken = null;
        Account account = getAccount(accountName);
        String invalidToken = getToken(account);
        if (invalidToken != null) {
            validToken = invalidateToken(account, invalidToken, handler);
        }
        Log.d(TAG, "Token invalidateToken: " + invalidToken + " validToken: " + validToken);
        return validToken;
    }

    private String invalidateToken(Account account, String invalidToken, Handler handler) throws OperationCanceledException, AuthenticatorException, IOException {
        this.manager.invalidateAuthToken("com.google", invalidToken);
        Bundle bundle = (Bundle) this.manager.getAuthToken(account, AUTH_TOKEN_TYPE, null, true, null, handler).getResult();
        if (bundle.containsKey("authtoken")) {
            return bundle.getString("authtoken");
        }
        return null;
    }

    public List<String> getAccountNames() {
        List<String> names = new ArrayList();
        for (Account account : this.manager.getAccountsByType("com.google")) {
            names.add(account.name);
        }
        return names;
    }

    private Account getAccount(String name) {
        if (name != null) {
            //TODO:Size int is not found in Usage
           /* if (size > 0) {*/
            for (Account account : this.manager.getAccountsByType("com.google")) {
                if (name.equals(account.name)) {
                    return account;
                }
            }
            /*} else {
                Log.e(TAG, "getAccount return 0 accounts");
                Crashlytics.log("getAccount return 0 accounts");
            }*/
        }
        return null;
    }

  /*  public static AccountInfo getProfile(Activity activity, String email) {
        if (email == null) {
            return null;
        }
        AccountInfo p = new AccountInfo(email);
        if (email == null || email.length() <= 0) {
            return p;
        }
        int index = email.indexOf("@");
        if (index < 0 || index >= email.length()) {
            return p;
        }
        p.displayName = email.substring(0, index);
        return p;
    }*/

    public void addAccount(Activity context) {
        this.manager.addAccount("com.google", "my_auth_token", null, null, context, null, null);
    }

    public boolean startAuthActivity(final Activity context, int requestCode, String accountName, final MyAccountManagerListener listener) {
        Account account = getAccount(accountName);
        if (account == null) {
            return false;
        }
        this.manager.getAuthToken(account, AUTH_TOKEN_TYPE, null, context, new AccountManagerCallback<Bundle>() {
            public void run(AccountManagerFuture<Bundle> future) {
                String authToken = null;
                try {
                    authToken = ((Bundle) future.getResult()).getString("authtoken");
                    Log.i(MyAccountManager2.TAG, "Got auth token");
                } catch (OperationCanceledException e) {
                    Log.e(MyAccountManager2.TAG, "Auth token operation Canceled", e);
                    BaseLoginActivity.dismissDialog();
                    BaseLoginActivity.accountsListView.setSelection(0);
                    Snackbar.make(BaseLoginActivity.coordinatorLayout,"Check your Internet connection",Snackbar.LENGTH_LONG).show();
                } catch (IOException e2) {
                    Log.e(MyAccountManager2.TAG, "Auth token IO exception", e2);
                    BaseLoginActivity.dismissDialog();
                    BaseLoginActivity.accountsListView.setSelection(0);
                    Snackbar.make(BaseLoginActivity.coordinatorLayout,"Check your Internet connection",Snackbar.LENGTH_LONG).show();
                 //   Toast.makeText(context,"Check your Internet connection",Toast.LENGTH_SHORT).show();
                } catch (AuthenticatorException e3) {
                    Log.e(MyAccountManager2.TAG, "Authentication Failed", e3);
                    BaseLoginActivity.dismissDialog();
                    BaseLoginActivity.accountsListView.setSelection(0);
                    Snackbar.make(BaseLoginActivity.coordinatorLayout,"Check your Internet connection",Snackbar.LENGTH_LONG).show();
                }
                if (authToken != null) {
                Log.d(MyAccountManager2.TAG, "Token startAuthActivity token: " + authToken);
                restoken = authToken;
                listener.authToken(authToken);
                } else {
                    BaseLoginActivity.dismissDialog();

                }
            }
        }, new Handler());
        return true;
    }

    public AccountManagerFuture<Bundle> getRealTimeToken(Activity context, String accountName, AccountManagerCallback<Bundle> callback) {
        Account account = getAccount(accountName);
        if (account != null) {
            return this.manager.getAuthToken(account, REALTIME_TYPE, null, context, callback, null);
        }
        return null;
    }
}
