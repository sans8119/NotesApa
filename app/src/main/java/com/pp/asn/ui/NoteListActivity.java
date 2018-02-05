package com.pp.asn.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.pp.asn.PhoneApplication;
import com.pp.asn.adapter.NotesListAdapter;
import com.pp.asn.db.DbUtils;
import com.pp.asn.model.NoteData;
import com.pp.asn.phone2.R;
import com.pp.asn.utils.Constants;

import java.util.LinkedHashMap;

public class NoteListActivity extends AppCompatActivity {
    public static int navItemIndex = 0;
    private final String TAG = NoteListActivity.class.getSimpleName();
    private RecyclerView notesListView;
    private NotesListAdapter notesListAdapter;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private NoteListController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        controller = new NoteListController();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        notesListView = (RecyclerView) findViewById(R.id.recyclerView_notes_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        notesListView.setLayoutManager(mLayoutManager);
        notesListView.setItemAnimator(new DefaultItemAnimator());
        notesListAdapter = new NotesListAdapter();
        ((PhoneApplication) getApplication()).getDbUtils().getAllNotesFromDb(new DbUtils.DbCallBack() {
            @Override
            public void sendData(Object object) {
                notesListAdapter.getNotesListMap().putAll((LinkedHashMap<String, NoteData>) object);
                notesListAdapter.getNotesListMapMaster().putAll((LinkedHashMap<String, NoteData>) object);
            }
        }, this);
        notesListView.setAdapter(notesListAdapter);
        notesListAdapter.notifyDataSetChanged();
        setUpNavigationView();

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(notesListView.getContext(),
                DividerItemDecoration.VERTICAL);
        notesListView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == Constants.MESSAGE_EDITED) {
            if (resultCode == Activity.RESULT_OK) {
                ((PhoneApplication) getApplication()).getDbUtils().getAllNotesFromDb(new DbUtils.DbCallBack() {
                    @Override
                    public void sendData(final Object object) {
                        ((PhoneApplication) getApplication()).getDbUtils().getAllNotesFromDb(new DbUtils.DbCallBack() {
                            @Override
                            public void sendData(Object object) {
                                notesListAdapter.getNotesListMap().putAll((LinkedHashMap<String, NoteData>) object);
                                notesListAdapter.getNotesListMapMaster().putAll((LinkedHashMap<String, NoteData>) object);
                                notesListAdapter.getNotesListMap().clear();
                                notesListAdapter.getNotesListMap().putAll((LinkedHashMap<String, NoteData>) object);
                                NoteListActivity.this.runOnUiThread(new Thread() {
                                    public void run() {
                                        notesListAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }, NoteListActivity.this);
                    }
                }, this);
            } else if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.hearted:
                        navItemIndex = 0;
                        break;
                    case R.id.favorite:
                        navItemIndex = 1;
                        break;
                    case R.id.poem:
                        navItemIndex = 2;
                        break;
                    case R.id.story:
                        navItemIndex = 3;
                        break;
                }
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);
                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, null, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }


    public void onAddNoteButtonClick(View view) {
        Intent intent = new Intent(view.getContext(), MessageActivity.class);
        startActivityForResult(intent, Constants.MESSAGE_EDITED);
    }

    public void onStarredButtonClick(View view) {
        setStarredOrHearted(R.string.starred, view);
    }

    public void onHeartedButtonClick(View view) {
        setStarredOrHearted(R.string.hearted, view);
    }

    public void onPoemButtonClick(View view) {
        setStarredOrHearted(R.string.poem, view);
    }

    public void onStoryButtonClick(View view) {
        setStarredOrHearted(R.string.story, view);
    }

    public void onFilterCloseButtonClick(View view) {
        drawer.closeDrawers();
    }

    public void onDeleteButtonClick(View view) {
        String key = notesListAdapter.getNotesListMap().keySet().toArray()[Integer.parseInt(view.getTag().toString())].toString();
        final NoteData noteData = notesListAdapter.getNotesListMap().get(key);
        ((PhoneApplication) getApplication()).getDbUtils().deleteNote(new DbUtils.DbCallBack() {
            @Override
            public void sendData(Object object) {
                if ((Boolean) object) {
                    NoteListActivity.this.runOnUiThread(new Thread() {
                        public void run() {
                            notesListAdapter.getNotesListMap().remove(noteData.getTitle());
                            notesListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        }, this, noteData);
    }

    public void onShowAllButtonClicked(View view) {
        notesListAdapter.getNotesListMap().clear();
        notesListAdapter.getNotesListMap().putAll(notesListAdapter.getNotesListMapMaster());
        notesListAdapter.notifyDataSetChanged();
        drawer.closeDrawers();
    }

    public void onApplyButtonClicked(View view) {
        int[] state = new int[4];
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            if (((CheckBox) menu.getItem(i).getActionView()).isChecked()) {
                state[i] = 1;
            } else {
                state[i] = 0;
            }
        }
        controller.applyFilter(notesListAdapter, state);
        drawer.closeDrawers();
    }

    private void setStarredOrHearted(int starHeartPoemStory, View view) {
        int position = Integer.parseInt(view.getTag().toString());
        String key = notesListAdapter.getNotesListMap().keySet().toArray()[position].toString();
        NoteData noteData = notesListAdapter.getNotesListMap().get(key);
        if (starHeartPoemStory == R.string.starred) {
            int state = noteData.getStarred() ^ 1;
            if (state == 0) {
                ((ImageView) view).setImageResource(android.R.drawable.star_big_off);
            } else {
                ((ImageView) view).setImageResource(android.R.drawable.star_big_on);
            }
            noteData.setStarred(state);
        } else if (starHeartPoemStory == R.string.hearted) {
            int state = noteData.getHearted() ^ 1;
            if (state == 0) {
                ((ImageView) view).setImageResource(R.drawable.heart_empty);
            } else {
                ((ImageView) view).setImageResource(R.drawable.heart_icon);
            }
            noteData.setHearted(state);
        } else if (starHeartPoemStory == R.string.poem) {
            int state = noteData.getPoem() ^ 1;
            if (state == 0) {
                ((TextView) view).setTextColor(Color.BLACK);
            } else {
                ((TextView) view).setTextColor(Color.BLUE);
            }
            noteData.setPoem(state);
        } else if (starHeartPoemStory == R.string.story) {
            int state = noteData.getStory() ^ 1;
            if (state == 0) {
                ((TextView) view).setTextColor(Color.BLACK);
            } else {
                ((TextView) view).setTextColor(Color.BLUE);
            }
            noteData.setStory(state);
        }
        noteData.setId(position);
        ((PhoneApplication) getApplication()).getDbUtils().storeNotesInDb(noteData, this, noteData.getTitle());
    }

    public void onFilterSelected(View Veiw) {
    }

}