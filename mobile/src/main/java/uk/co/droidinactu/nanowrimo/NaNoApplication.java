package uk.co.droidinactu.nanowrimo;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.util.Patterns;

import org.apache.commons.io.FileUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

import uk.co.droidinactu.nanowrimo.db.DataManager;


/**
 * Created by aspela on 17/05/16.
 */
public class NaNoApplication extends Application {
    public static final String LOG_TAG = NaNoApplication.class.getSimpleName() + ":";

    public final static boolean IS_DEBUGGING = true;

    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final DateTimeFormatter sdf = DateTimeFormat.forPattern("yyyy-MM-dd");
    public static final DateTimeFormatter sdf_file = DateTimeFormat.forPattern("yyyy-MM-dd");
    public static final DateTimeFormatter logDataFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
    public static final String simpleDateFmtStrView = "dd-MMM-yyyy";
    public static final String simpleDateFmtStrDb = "yyyyMMdd";

    public static final DecimalFormat minsFmt = new DecimalFormat("#0");
    public static final DecimalFormat kmFmt = new DecimalFormat("#0.0");
    public static final DecimalFormat decFmt = new DecimalFormat("#0.00");
    public static final DecimalFormat gbp = new DecimalFormat("£#0.00");

    public static final int ONE_SECOND = 1000;
    public static final int ONE_MINUTE = 60000;
    public static final long BLE_DEVICE_SCAN_PERIOD = 10000;

    private static NaNoApplication instance;
    private DataManager dataMgr;
    private PowerManager.WakeLock wakeLock;

    public static void d(final String msg) {
        logMessage(Log.DEBUG, msg);
    }

    public static void logMessage(final int lvl, final String msg) {
        switch (lvl) {
            case Log.VERBOSE:
                Log.v(LOG_TAG, msg);
                break;
            case Log.DEBUG:
                Log.d(LOG_TAG, msg);
                break;
            case Log.INFO:
                Log.i(LOG_TAG, msg);
                break;
            case Log.WARN:
                Log.w(LOG_TAG, msg);
                break;
            case Log.ERROR:
                Log.e(LOG_TAG, msg);
                break;
            case Log.ASSERT:
                Log.wtf(LOG_TAG, msg);
                break;
        }
    }

    public static void e(final String msg) {
        logMessage(Log.ERROR, msg);
    }

    public static void e(final String msg, final Throwable thr) {
        logMessage(Log.ERROR, msg, thr);
    }

    public static void i(final String msg) {
        logMessage(Log.INFO, msg);
    }

    public static void logMessage(final int lvl, final String msg, final Throwable thr) {
        Log.e(LOG_TAG, msg, thr);
    }

    public static NaNoApplication getInstance() {
        return NaNoApplication.instance;
    }

    public static void v(final String msg) {
        logMessage(Log.VERBOSE, msg);
    }

    public static void w(final String msg) {
        logMessage(Log.WARN, msg);
    }

    public static void wtf(final String msg) {
        logMessage(Log.ASSERT, msg);
    }

    public static boolean is_external_storage_available() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    private static void copyFileUsingApacheCommonsIO(String source, String dest) throws IOException {
        copyFileUsingApacheCommonsIO(new File(source), new File(dest));
    }

    private static void copyFileUsingApacheCommonsIO(File source, File dest) throws IOException {
        FileUtils.copyFile(source, dest);
    }

    private static void copyFileUsingChannel(String source, String dest) throws IOException {
        copyFileUsingChannel(new File(source), new File(dest));
    }

    private static void copyFileUsingChannel(File source, File dest) throws IOException {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destChannel = new FileOutputStream(dest).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } finally {
            sourceChannel.close();
            destChannel.close();
        }
    }

    private static void copyFileUsingStream(String source, String dest) throws IOException {
        copyFileUsingStream(new File(source), new File(dest));
    }

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    public DataManager getDataManager() {
        return dataMgr;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Application#onCreate()
     */
    @Override
    public void onCreate() {
        super.onCreate();
        NaNoApplication.logMessage(Log.DEBUG, "onCreate(); application being created.");
        NaNoApplication.instance = this;
        dataMgr = new DataManager(getApplicationContext());
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Application#onTerminate()
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public PowerManager.WakeLock getWakeLock() {
        if (wakeLock == null) {
            // lazy loading: first call, create wakeLock via PowerManager.
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "wakeup");
        }
        return wakeLock;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    protected Drawable getAppImage(final String packageName) {
        try {
            return getPackageManager().getApplicationIcon(packageName);
        } catch (final NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Bitmap getApplicationImage() {
        return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    }

    public String getAppSdCardPathDir() {
        final File extDir = Environment.getExternalStorageDirectory();
        return extDir.getPath() + File.separator + getApplicationName() + File.separator;
    }

    public String getApplicationName() {
        return this.getString(R.string.app_name);
    }

    public boolean isActivityRunning() {
        boolean isActivityFound = false;
//        ActivityManager activityManager = (ActivityManager) Monitor.this.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> activitys = activityManager.getRunningTasks(Integer.MAX_VALUE);
//        for (int i = 0; i < activitys.size(); i++) {
//            if (activitys.get(i).topActivity.toString().equalsIgnoreCase("ComponentInfo{com.example.testapp/com.example.testapp.Your_Activity_Name}")) {
//                isActivityFound = true;
//            }
//        }
        return isActivityFound;
    }


    protected int getAppVersionNbr(final String packageName) {
        try {
            final PackageInfo pInfo = getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
            return pInfo.versionCode;
        } catch (final NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    protected String getAppVersionName(final String packageName) {
        try {
            final PackageInfo pInfo = getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
            return pInfo.versionName;
        } catch (final NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getEmailAddr() {
        final Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        final Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
        for (final Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                final String possibleEmail = account.name;
                if (possibleEmail.endsWith("googlemail.com") || possibleEmail.endsWith("gmail.com")) {
                    return possibleEmail;
                }
            }
        }
        return null;
    }

}
