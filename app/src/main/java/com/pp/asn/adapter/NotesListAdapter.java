package com.pp.asn.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pp.asn.db.DatabaseHelper;
import com.pp.asn.model.NoteData;
import com.pp.asn.phone2.R;
import com.pp.asn.ui.MessageActivity;
import com.pp.asn.utils.Constants;

import java.util.Calendar;
import java.util.LinkedHashMap;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.ViewHolder> {
    private final LinkedHashMap<String, NoteData> notesListMap;
    private final LinkedHashMap<String, NoteData> notesListMapMaster;
    int counter = 0;
    private ViewGroup parent;
    private String TAG = "NotesListAdapter";

    public NotesListAdapter() {
        notesListMap = new LinkedHashMap<String, NoteData>();
        notesListMapMaster = new LinkedHashMap<String, NoteData>();
    }

    public LinkedHashMap<String, NoteData> getNotesListMapMaster() {
        return notesListMapMaster;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        Log.d(TAG, "----NotesListAdapter=-onBindViewHolder--->" + position + " " + notesListMap.keySet().toArray()[position] + ", " + notesListMap);
        String key = (String) notesListMap.keySet().toArray()[position];
        Log.d(TAG, "----NotesListAdapter=-onBindViewHolder--->" + notesListMap.get(key).getHearted() + ", starred:" + notesListMap.get(key).getStarred());

        viewHolder.titleTV.setText(notesListMap.get(key).getTitle());
        viewHolder.starredIV.setTag(position);
        viewHolder.heartedIV.setTag(position);
        viewHolder.deleteIV.setTag(position);
        viewHolder.poemTV.setTag(position);
        viewHolder.storyTV.setTag(position);
        viewHolder.deleteIV.setTag(position);
        if (notesListMap.get(key).getHearted() == 0) {
            Log.d(TAG, "--~~~>hearted 0");
            viewHolder.heartedIV.setImageResource(R.drawable.heart_empty);
        } else {
            Log.d(TAG, "--~~~>hearted 1");
            viewHolder.heartedIV.setImageResource(R.drawable.heart_icon);
        }
        if (notesListMap.get(key).getStarred() == 0) {
            Log.d(TAG, "--~~~>starred 0");
            viewHolder.starredIV.setImageResource(android.R.drawable.star_big_off);
        } else {
            Log.d(TAG, "--~~~>starred 1");
            viewHolder.starredIV.setImageResource(android.R.drawable.star_big_on);
        }
        if (notesListMap.get(key).getPoem() == 0) {
            Log.d(TAG, "--~~~>hearted 0");
            viewHolder.poemTV.setTextColor(Color.BLACK);
        } else {
            Log.d(TAG, "--~~~>hearted 1");
            viewHolder.poemTV.setTextColor(Color.BLUE);
        }
        if (notesListMap.get(key).getStory() == 0) {
            Log.d(TAG, "--~~~>starred 0");
            viewHolder.poemTV.setTextColor(Color.BLACK);
        } else {
            Log.d(TAG, "--~~~>starred 1");
            viewHolder.poemTV.setTextColor(Color.BLUE);
        }
        if (notesListMap.get(key).getText().length() > 10)
            viewHolder.shortDescTV.setText(notesListMap.get(key).getText().substring(0, 10));
        else
            viewHolder.shortDescTV.setText(notesListMap.get(key).getText());
        Calendar calendarCurrent = Calendar.getInstance();
        int dayOfYear = calendarCurrent.get(Calendar.DAY_OF_YEAR);
        int year = calendarCurrent.get(Calendar.YEAR);
        int month = calendarCurrent.get(Calendar.MONTH);
        Calendar calendar = Calendar.getInstance();
        NoteData noteData = notesListMap.get(key);
        calendar.setTimeInMillis(noteData.getTime());
        //noteData.setId(((NoteData) object).getId());
        Context context = viewHolder.titleTV.getContext();
        StringBuffer dateStrBuffer = new StringBuffer(100).append(context.getString(R.string.last_updated)).append(":");
        if (calendar.get(Calendar.DAY_OF_YEAR) == dayOfYear && (calendar.get(Calendar.YEAR) == year) && (calendar.get(Calendar.MONTH)
                == month)) {
            dateStrBuffer.append(context.getString(R.string.today)).append(" ").append(calendar.get(Calendar.HOUR_OF_DAY)).
                    append(":").append(calendar.get(Calendar.MINUTE));
        } else {
            dateStrBuffer.append(calendar.get(Calendar.DAY_OF_MONTH) + "-" +
                    calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.YEAR)).append(" ").append(calendar.get(Calendar.HOUR_OF_DAY)).
                    append(":").append(calendar.get(Calendar.MINUTE));
        }
        viewHolder.dateTV.setText(dateStrBuffer);
    }

    @Override
    public int getItemCount() {
        return notesListMap.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        this.parent = parent;
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_header, parent, false));
        //viewHolder.titleTV.setText(notesListMap.get("title:" + counter).getTitle());
        ++counter;
        Log.d(TAG, "----onCreateViewHolder=---->" + notesListMap);
        return viewHolder;
    }

    private NotesListAdapter.ViewHolder getViewHolder(int position) {
        if (parent == null)
            return null;
        else
            return ((NotesListAdapter.ViewHolder) ((RecyclerView) parent).findViewHolderForAdapterPosition(position));
    }

    public LinkedHashMap<String, NoteData> getNotesListMap() {
        return notesListMap;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTV;
        private final TextView shortDescTV;
        private final TextView dateTV;
        private final ImageView starredIV;
        private final ImageView heartedIV;
        private final TextView poemTV;
        private final TextView storyTV;
        private final ImageView deleteIV;
        private View view;
        private LinearLayout mParentLayout;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            titleTV = (TextView) view.findViewById(R.id.title_tv);
            shortDescTV = (TextView) view.findViewById(R.id.desc_tv);
            poemTV = (TextView) view.findViewById(R.id.poem_tv);
            storyTV = (TextView) view.findViewById(R.id.story_tv);
            dateTV = (TextView) view.findViewById(R.id.date_tv);
            starredIV = (ImageView) view.findViewById(R.id.starred_iv);
            heartedIV = (ImageView) view.findViewById(R.id.hearted_iv);
            deleteIV = (ImageView) view.findViewById(R.id.delete_iv);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "----onClick----:>" + view);
                    Intent intent = new Intent(view.getContext(), MessageActivity.class);
                    intent.putExtra(DatabaseHelper.NotesTable.TITLE, titleTV.getText());
                    ((AppCompatActivity) view.getContext()).startActivityForResult(intent, Constants.MESSAGE_EDITED);
                }
            });
        }

        public View getView() {
            return view;
        }
    }

}
