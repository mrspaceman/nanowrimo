package uk.co.droidinactu.nanowrimo;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import uk.co.droidinactu.nanowrimo.db.DataManager;
import uk.co.droidinactu.nanowrimo.db.DayWordCount;

public class Dashboard extends AppCompatActivity implements
        View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String LOG_TAG = Dashboard.class.getSimpleName() + ":";

    private static final int REQUEST_INVITE = 1;
    private static final int REQUEST_IMAGE = 2;
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 150;
    private static final String MESSAGE_SENT_EVENT = "message_sent";
    private static final String MESSAGE_URL = "http://bikerbreakfastclub.firebase.google.com/message/";
    private static final String LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif";

    public static final String INSTANCE_ID_TOKEN_RETRIEVED = "iid_token_retrieved";
    public static final String BIKERCHAT_MSG_LENGTH = "bikerChat_msg_length";
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 8193;

    private ProgressBar mProgressBar;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private GoogleApiClient mGoogleApiClient;

    private TextView dash_username;
    private TextView dash_month_name;
    private TableLayout dash_month_table;
    private SharedPreferences mSharedPreferences;
    private int mMonthNbr = 11;

    private FusedLocationProviderClient mFusedLocationClient;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private ChildEventListener wordcountDataListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
            Log.d(LOG_TAG, "onChildAdded [DayWordCount]:" + dataSnapshot.getKey());
            DayWordCount obj = dataSnapshot.getValue(DayWordCount.class);
            NaNoApplication.getInstance().getDataManager().addWordCountToList(obj);
            updateCalendarDays();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            Log.d(LOG_TAG, "onChildChanged [DayWordCount]:" + dataSnapshot.getKey());
            DayWordCount obj = dataSnapshot.getValue(DayWordCount.class);
            NaNoApplication.getInstance().getDataManager().addWordCountToList(obj);
            updateCalendarDays();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Log.d(LOG_TAG, "onChildRemoved [DayWordCount]:" + dataSnapshot.getKey());
            String objKey = dataSnapshot.getKey();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            Log.d(LOG_TAG, "onChildMoved [DayWordCount]:" + dataSnapshot.getKey());
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e(LOG_TAG, "The read failed [DayWordCount]: " + databaseError.getCode());
        }
    };

    public int checkPermission() {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {//Can add more as per requirement

            requestPermissions(
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }
        return checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });
        fab.setVisibility(View.GONE);

        int permissionCheck = checkPermission();

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                requestPermissions(
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    // ...
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                TextView navHead1 = (TextView) findViewById(R.id.nav_header_text1);
                navHead1.setText(NaNoApplication.getInstance().getDataManager().getFirebaseUser().getDisplayName());

                TextView navHead2 = (TextView) findViewById(R.id.nav_header_text2);
                navHead2.setText(getMonthName(mMonthNbr - 1) + " " + Calendar.getInstance().get(Calendar.YEAR));
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mMonthNbr = mSharedPreferences.getInt("MONTH_NAME", 11);

        NaNoApplication.getInstance().getDataManager().initialise();
        if (NaNoApplication.getInstance().getDataManager().getFirebaseUser() == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }
        NaNoApplication.getInstance().getDataManager().initialiseListeners(wordcountDataListener);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        dash_username = (TextView) findViewById(R.id.dash_username);
        dash_username.setText(NaNoApplication.getInstance().getDataManager().getFirebaseUser().getDisplayName());
        dash_username.setPadding(25, 10, 25, 10);
        dash_username.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);


        dash_month_name = (TextView) findViewById(R.id.dash_month_name);
        dash_month_name.setText(getMonthName(mMonthNbr - 1) + " " + Calendar.getInstance().get(Calendar.YEAR));
        dash_month_name.setPadding(25, 10, 25, 10);
        dash_month_name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);

        dash_month_table = (TableLayout) findViewById(R.id.dash_month_table);
        dash_month_table.setBackgroundDrawable(getResources().getDrawable(R.drawable.cal_table));

        updateCalendar();
    }

    private String getMonthName(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }

    private void updateCalendar() {
        updateCalendarHeader();
        updateCalendarDays();
    }

    private void updateCalendarHeader() {
        String[] headers = {"Sun", "Mon", "Tues", "Wed", "Thurs", "Fri", "Sat"};
        TableRow tblRwHeader = (TableRow) findViewById(R.id.dash_month_table_rowHead);
        for (int col = 0; col < 7; col++) {
            TextView tv = new TextView(this);
            tv.setText(headers[col]);
            tv.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, (float) 1.0));
            tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.cal_cell));
            tblRwHeader.addView(tv);
        }
    }

    private void updateCalendarDays() {
        int yearNbr = Calendar.getInstance().get(Calendar.YEAR);
        String input_date = "01/" + mMonthNbr + "/" + yearNbr;
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
        Date dt1 = new Date();
        try {
            dt1 = format1.parse(input_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar c = Calendar.getInstance();
        c.setTime(dt1);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK); // Sunday is 1
        int monthDayNbr = 1;
        int cumWordCount = 0;

        SharedPreferences sp = getSharedPreferences(DataManager.PREFS_NAME, Context.MODE_PRIVATE);
        final int wordcountTrgt = sp.getInt("pref_wordcount_target", 50000);

        final Map<String, DayWordCount> wrdCnts = NaNoApplication.getInstance().getDataManager().getWordCounts(yearNbr, mMonthNbr);
        dash_month_table.removeViews(1, dash_month_table.getChildCount() - 1);

        TableRow tblRw = new TableRow(this);
        tblRw.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        for (int col = 1; col < 8; col++) {
            if (col >= dayOfWeek) {
                DayWordCount dayWrdCount = wrdCnts.get(monthDayNbr + "/" + mMonthNbr + "/" + yearNbr);
                if (dayWrdCount == null) {
                    dayWrdCount = new DayWordCount(monthDayNbr + "/" + mMonthNbr + "/" + yearNbr, 0);
                }
                int revisedQuota = Math.round((float) (wordcountTrgt - cumWordCount) / (float) (31 - monthDayNbr));
                cumWordCount += dayWrdCount.getWordcount();
                tblRw.addView(new CalDayView(this,
                        revisedQuota,
                        cumWordCount,
                        dayWrdCount));
                monthDayNbr++;
            } else {
                tblRw.addView(new TextView(this));
            }
        }
        dash_month_table.addView(tblRw);
        for (int row = 2; row <= 5; row++) {
            tblRw = new TableRow(this);
            for (int col = 1; col <= 7; col++) {
                if (monthDayNbr <= 30) {
                    DayWordCount dayWrdCount = wrdCnts.get(monthDayNbr + "/" + mMonthNbr + "/" + yearNbr);
                    if (dayWrdCount == null) {
                        dayWrdCount = new DayWordCount(monthDayNbr + "/" + mMonthNbr + "/" + yearNbr, 0);
                    }
                    int revisedQuota = Math.round((float) (wordcountTrgt - cumWordCount) / (float) (31 - monthDayNbr));
                    cumWordCount += dayWrdCount.getWordcount();
                    tblRw.addView(new CalDayView(this,
                            revisedQuota,
                            cumWordCount,
                            dayWrdCount));
                    monthDayNbr++;
                } else {
                    tblRw.addView(new TextView(this));
                }
            }
            dash_month_table.addView(tblRw);
        }
        updateAppWidget();
    }


    private void updateAppWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
        int widgetIDs[] = appWidgetManager.getAppWidgetIds(new ComponentName(getApplication(), NaNoMonthWidgetProvider.class));

        for (int mAppWidgetId : widgetIDs) {
            NaNoMonthWidgetProvider.updateAppWidget(this.getApplicationContext(),appWidgetManager, mAppWidgetId);
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(LOG_TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_nanowrimo) {
            Uri uri = Uri.parse("http://www.nanowrimo.org/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

        } else if (id == R.id.nav_reset) {
            // clear out database

        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));

        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, AboutActivity.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
