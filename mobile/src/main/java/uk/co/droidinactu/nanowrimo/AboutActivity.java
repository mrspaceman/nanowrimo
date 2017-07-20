package uk.co.droidinactu.nanowrimo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    private TextView txtDescOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtDescOne = (TextView) findViewById(R.id.about_description_one);
        txtDescOne.setText(Html.fromHtml(getResources().getString(R.string.about_description_one)));
    }

}
