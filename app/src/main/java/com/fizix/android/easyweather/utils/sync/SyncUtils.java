package com.fizix.android.easyweather.utils.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.fizix.android.easyweather.accounts.GenericAccountService;
import com.fizix.android.easyweather.data.Contract;

public class SyncUtils {

    // The frequency we want to sync at.
    private static final long SYNC_FREQUENCY = 3 * 60 * 60;

    // The preference we use to store the setup state.
    private static final String PREF_SETUP_COMPLETE = "setup_complete";

    // Create the new dummy account for the sync adapter.
    public static void createSyncAccount(Context context) {
        boolean newAccount = false;
        boolean setupComplete = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
                PREF_SETUP_COMPLETE, false);

        // Create account, if it's missing.  Either it's the first run or the user has deleted the
        // account.
        Account account = GenericAccountService.getAccount(context);
        AccountManager accountManager = (AccountManager) context.getSystemService(
                Context.ACCOUNT_SERVICE);
        if (accountManager.addAccountExplicitly(account, null, null)) {
            // Inform the system that this account supports sync.
            ContentResolver.setIsSyncable(account, Contract.CONTENT_AUTHORITY, 1);

            // Inform the system that this account is eligible for auto sync when the network is up.
            ContentResolver.setSyncAutomatically(account, Contract.CONTENT_AUTHORITY, true);

            // Recommend a schedule for automatic synchronization.  The system may modify this based
            // on other scheduled syncs and network utilization.
            ContentResolver.addPeriodicSync(
                    account, Contract.CONTENT_AUTHORITY, new Bundle(), SYNC_FREQUENCY);

            newAccount = true;
        }

        // Schedule an initial sync if we detect problems with either our account of our local data
        // has been deleted.  (Not that it's possible to clear app data WITHOUT affecting the
        // account list, so we need to check both.
        if (newAccount || !setupComplete) {
            syncAllNow(context);
            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putBoolean(PREF_SETUP_COMPLETE, true)
                    .commit();
        }
    }

    private static void syncInternal(Context context, Bundle bundle) {
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        ContentResolver.requestSync(
                GenericAccountService.getAccount(context), Contract.CONTENT_AUTHORITY, bundle);
    }

    // Helper method to trigger an immediate sync of all locations.
    public static void syncAllNow(Context context) {
        Bundle bundle = new Bundle();
        syncInternal(context, bundle);
    }

    // Helper method to trigger an immediate sync of the specified location.
    public static void syncLocationNow(Context context, long locationId) {
        Bundle bundle = new Bundle();
        bundle.putLong(SyncAdapter.KEY_LOCATION_ID, locationId);
        syncInternal(context, bundle);
    }

}
