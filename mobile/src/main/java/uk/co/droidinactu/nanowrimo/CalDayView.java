package uk.co.droidinactu.nanowrimo;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import uk.co.droidinactu.nanowrimo.db.DataManager;
import uk.co.droidinactu.nanowrimo.db.DayWordCount;

/**
 * Display the number of words written on a specified date
 * <p>
 * Created by aspela on 12/07/17.
 */
public class CalDayView extends LinearLayout implements View.OnClickListener {
    private static final String LOG_TAG = CalDayView.class.getSimpleName() + ":";

    private TextView mDayNumber;
    private TextView mDailyQuota;
    private TextView mDailyQuotaRevised;
    private TextView mDailyActual;
    private TextView mCumulative;
    private TextView mWordsLeft;
    private DayWordCount mDaysWrdCnt;

    public CalDayView(final Context context,
                      int pDayQuotaRevised,
                      final int pCumulative,
                      final DayWordCount pDaysWrdCnt) {
        super(context);

        SharedPreferences sp = context.getSharedPreferences(DataManager.PREFS_NAME, Context.MODE_PRIVATE);
        final int wordcountTrgt = sp.getInt("pref_wordcount_target", 50000);

        this.mDaysWrdCnt = pDaysWrdCnt;
        this.setOrientation(VERTICAL);
        this.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, (float) 1.0));
        this.setBackgroundResource(R.drawable.cal_cell);

        this.setOnClickListener(this);

        mDayNumber = new TextView(context);
        mDayNumber.setPadding(25, 3, 5, 3);
        mDayNumber.setText("" + mDaysWrdCnt.getDayNumber());
        mDayNumber.setTextColor(Color.GREEN);
        mDayNumber.setBackgroundResource(R.drawable.cal_cell_date);
        LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp1.gravity = Gravity.LEFT;
        addView(mDayNumber, lp1);

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.END;

        mDailyQuota = new TextView(context);
        mDailyQuota.setPadding(1, 1, 5, 0);
        int tmpWrds1 = Math.round((float) wordcountTrgt / (float) 30);
        mDailyQuota.setText("" + tmpWrds1);
        mDailyQuota.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        mDailyQuota.setTypeface(null, Typeface.ITALIC);
        mDailyQuota.setTextColor(Color.BLUE);
        mDailyQuota.setGravity(Gravity.RIGHT);
        addView(mDailyQuota, lp);

        mDailyQuotaRevised = new TextView(context);
        mDailyQuotaRevised.setPadding(1, 1, 5, 0);
        if (mDaysWrdCnt.getDayNumber() == 1) {
            mDailyQuotaRevised.setText("");
        } else {
            mDailyQuotaRevised.setText("" + pDayQuotaRevised);
        }
        mDailyQuotaRevised.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        mDailyQuotaRevised.setTypeface(null, Typeface.ITALIC);
        mDailyQuotaRevised.setTextColor(Color.BLUE);
        mDailyQuotaRevised.setGravity(Gravity.RIGHT);
        addView(mDailyQuotaRevised, lp);

        mDailyActual = new TextView(context);
        mDailyActual.setPadding(1, 1, 5, 0);
        mDailyActual.setText("" + mDaysWrdCnt.getWordcount());
        mDailyActual.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
        mDailyActual.setTypeface(null, Typeface.BOLD);
        if (mDaysWrdCnt.getWordcount() >= pDayQuotaRevised) {
            mDailyActual.setTextColor(Color.GREEN);
        } else {
            mDailyActual.setTextColor(Color.RED);
        }
        mDailyActual.setGravity(Gravity.RIGHT);
        addView(mDailyActual, lp);

        mCumulative = new TextView(context);
        mCumulative.setPadding(1, 1, 5, 0);
        if (mDaysWrdCnt.getDayNumber() == 1) {
            mCumulative.setText("");
        } else {
            mCumulative.setText("" + pCumulative);
        }
        mCumulative.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        mCumulative.setGravity(Gravity.RIGHT);
        addView(mCumulative, lp);

        mWordsLeft = new TextView(context);
        mWordsLeft.setPadding(1, 1, 5, 0);
        mWordsLeft.setText("" + (wordcountTrgt - pCumulative));
        mWordsLeft.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        mWordsLeft.setGravity(Gravity.RIGHT);
        addView(mWordsLeft, lp);
    }

    @Override
    public void onClick(View view) {
        NaNoApplication.d(LOG_TAG + "onClick() pressed");

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_enter_word_count);
        dialog.setTitle("Enter Wordcount for " + mDaysWrdCnt.getDayNumber());

        TextView text = (TextView) dialog.findViewById(R.id.dialog_wrdcnt_desc);
        text.setText("Android custom dialog example!");
        ImageView image = (ImageView) dialog.findViewById(R.id.dialog_wrdcnt_icon);
        image.setImageResource(R.mipmap.ic_launcher);

        EditText txtWrdcnt = (EditText) dialog.findViewById(R.id.dialog_wrdcnt_words);
        txtWrdcnt.setText("" + mDaysWrdCnt.getWordcount());
        txtWrdcnt.setSelection(txtWrdcnt.getText().length());

        Button dialogButton = (Button) dialog.findViewById(R.id.dialog_wrdcnt_ButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtWrdcnt = (EditText) dialog.findViewById(R.id.dialog_wrdcnt_words);
                String wrdcntStr = txtWrdcnt.getText().toString().trim();
                try {
                    int wordCount = Integer.parseInt(wrdcntStr);
                    mDailyActual.setText(wrdcntStr);
                    NaNoApplication.d(LOG_TAG + "onClick() number of words was [" + wordCount + "]");
                    mDaysWrdCnt.setDaysWordCount(wordCount);
                    NaNoApplication.getInstance().getDataManager().saveWordCount(mDaysWrdCnt);
                } catch (NumberFormatException nfe) {
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
