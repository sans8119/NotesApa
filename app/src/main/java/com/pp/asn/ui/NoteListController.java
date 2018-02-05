package com.pp.asn.ui;

import com.pp.asn.adapter.NotesListAdapter;
import com.pp.asn.model.NoteData;

import java.util.LinkedHashMap;

public class NoteListController {

    private final String TAG = NoteListController.class.getSimpleName();

    public void applyFilter(NotesListAdapter notesListAdapter, int[] state) {
        LinkedHashMap<String, NoteData> notes = new LinkedHashMap<String, NoteData>();
        notes.putAll(notesListAdapter.getNotesListMapMaster());
        final int hearted = 0;
        final int stared = 1;
        final int poem = 2;
        final int story = 3;
        Object[] notesObject = notes.keySet().toArray();
        for (int i = 0; i < notesObject.length; i++) {
            String key = notesObject[i].toString();
            NoteData noteData = notes.get(key);
            if (state[hearted] == 1 && noteData.getHearted() == 1 ||
                    (state[stared] == 1 && noteData.getStarred() == 1) ||
                    (state[poem] == 1 && noteData.getPoem() == 1) ||
                    (state[story] == 1 && noteData.getStory() == 1)) {
            } else {
                notes.remove(key);
            }
        }
        //Log.d(TAG, "1---applyFilter----" + notes);
        notesListAdapter.getNotesListMap().clear();
        notesListAdapter.getNotesListMap().putAll(notes);
        notesListAdapter.notifyDataSetChanged();
    }

}
