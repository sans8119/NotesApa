package com.pp.asn.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.pp.asn.PhoneApplication;
import com.pp.asn.db.DatabaseHelper;
import com.pp.asn.db.DbUtils;
import com.pp.asn.model.NoteData;
import com.pp.asn.phone2.R;
import com.pp.asn.utils.Constants;

import java.util.Calendar;

public class MessageActivity extends AppCompatActivity {
    private final String TAG = "MessageActivity";
    private TextView saveButton;
    private TextView editButton;
    private TextView dateTV;
    private EditText msgTV;
    private EditText titleTV;
    private NoteData noteData;
    private String noteTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity);
        init();
        String title = "";
        if (getIntent().hasExtra(DatabaseHelper.NotesTable.TITLE)) {
            noteTitle = getIntent().getStringExtra(DatabaseHelper.NotesTable.TITLE);
            title = getIntent().getStringExtra(DatabaseHelper.NotesTable.TITLE);
            titleTV.setText(title);
            ((PhoneApplication) getApplication()).getDbUtils().getNoteFromDb(new DbUtils.DbCallBack() {
                @Override
                public void sendData(Object object) {
                    Log.d(TAG, "-----onCreate----" + object);
                    Calendar calendarCurrent = Calendar.getInstance();
                    int dayOfYear = calendarCurrent.get(Calendar.DAY_OF_YEAR);
                    int year = calendarCurrent.get(Calendar.YEAR);
                    int month = calendarCurrent.get(Calendar.MONTH);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(((NoteData) object).getTime());
                    noteData.setId(((NoteData) object).getId());
                    StringBuffer dateStrBuffer = new StringBuffer(100).append(getString(R.string.last_updated)).append(":");
                    if (calendar.get(Calendar.DAY_OF_YEAR) == dayOfYear && (calendar.get(Calendar.YEAR) == year) && (calendar.get(Calendar.MONTH)
                            == month)) {
                        dateStrBuffer.append(getString(R.string.today)).append(" ").append(calendarCurrent.get(Calendar.HOUR_OF_DAY)).
                                append(":").append(calendarCurrent.get(Calendar.MINUTE));
                    } else {
                        dateStrBuffer.append(calendarCurrent.get(Calendar.DAY_OF_MONTH) + "-" +
                                calendarCurrent.get(Calendar.MONTH) + "-" + calendarCurrent.get(Calendar.YEAR)).append(" ").append(calendarCurrent.get(Calendar.HOUR_OF_DAY)).
                                append(":").append(calendarCurrent.get(Calendar.MINUTE));
                    }
                    dateTV.setText(dateStrBuffer);
                    msgTV.setText(((NoteData) object).getText());
                    noteData = (NoteData) object;
                /*else if(calendar.get(Calendar.DAY_OF_MONTH) == (dayOfMonth) && (calendar.get(Calendar.YEAR) == year) && (calendar.get(Calendar.MONTH)
                        == month)){
                    dateStrBuffer.append(getString(R.string.dau_after_tomorrow));
                }else if(calendar.get(Calendar.DAY_OF_MONTH) == (dayOfMonth) && (calendar.get(Calendar.YEAR) == year) && (calendar.get(Calendar.MONTH)
                        == month)){
                    dateStrBuffer.append(getString(R.string.day_before_yesterday));
                }else if(){
                    dateStrBuffer.append(getString(R.string.tomorrow));
                }else if(){
                    dateStrBuffer.append(getString(R.string.yesterday));
                }*/
                }
            }, this, title);

        } else {
            Log.d(TAG, "-----onCreate----");
            saveButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.INVISIBLE);
            msgTV.setEnabled(true);
            titleTV.setEnabled(true);
        }
    }

    private void init() {
        saveButton = (TextView) findViewById(R.id.save_tv);
        editButton = (TextView) findViewById(R.id.edit_tv);
        dateTV = (TextView) findViewById(R.id.title_tv);
        msgTV = (EditText) findViewById(R.id.message_tv);
        msgTV.setEnabled(false);
        titleTV = (EditText) findViewById(R.id.title_tv);
        titleTV.setEnabled(false);
        dateTV = (TextView) findViewById(R.id.date_tv);
        noteData = new NoteData();
    }

    public void onBackButtonClick(View v) {
        handleBackButton();
    }

    @Override
    public void onBackPressed() {
        handleBackButton();
    }

    private void handleBackButton() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constants.ID, noteData.getId());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void onEditButtonClick(View v) {
        saveButton.setVisibility(View.VISIBLE);
        editButton.setVisibility(View.INVISIBLE);
        msgTV.setEnabled(true);
        titleTV.setEnabled(true);
    }

    public void onSaveButtonClick(View v) {
        saveButton.setVisibility(View.INVISIBLE);
        editButton.setVisibility(View.VISIBLE);
        msgTV.setEnabled(false);
        titleTV.setEnabled(false);
        noteData.setTitle(titleTV.getText().toString());
        noteData.setText(msgTV.getText().toString());
        Calendar calendar = Calendar.getInstance();
        noteData.setTime(calendar.getTimeInMillis());
        ((PhoneApplication) getApplication()).getDbUtils().storeNotesInDb(noteData, this, noteTitle);
    }

}
