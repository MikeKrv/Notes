package com.example.mike.notes.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mike.notes.R;
import com.example.mike.notes.adapters.NotesAdapter;
import com.example.mike.notes.model.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private Realm realm;
    private List<Note> notesList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onResume() {
        super.onResume();
        notesList.clear();
        adapter.refresh();
        retrieveDataFromDb();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new SlideInRightAnimator());
        adapter = new NotesAdapter();
        recyclerView.setAdapter(adapter);
        retrieveDataFromDb();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Note");

        LayoutInflater li = LayoutInflater.from(this);
        View dialogView = li.inflate(R.layout.dialog_view, null);
        final TextInputLayout tilTitle = dialogView.findViewById(R.id.tilDialogTitle);
        final EditText etTitle = tilTitle.getEditText();
        final TextInputLayout tilText = dialogView.findViewById(R.id.tilDialogText);
        final EditText etText = tilText.getEditText();


        builder.setView(dialogView);
        final Note note = new Note();
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                long time = System.currentTimeMillis();
                note.setId(time);
                note.setTime(time);
                note.setTitle(etTitle.getText().toString());
                note.setText(etText.getText().toString());

                addNote(note);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button positiveButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                if (etTitle.getText().toString().isEmpty()){
                    positiveButton.setEnabled(false);
                    tilTitle.setError("Empty Title");
                }
                etTitle.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 0){
                            positiveButton.setEnabled(false);
                            tilTitle.setError("Empty Title");
                        } else {
                            positiveButton.setEnabled(true);
                            tilTitle.setErrorEnabled(false);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (etTitle.isFocused() && etTitle.getText().length() > 34) {
                            Toast.makeText(MainActivity.this, "Reached the maximum number of characters!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        dialog.show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == NotesAdapter.NEW_NOTE_ACTIVTIY_REQUEST_CODE && resultCode == RESULT_OK){
            Note note = new Note();
            note.setId(data.getLongExtra("ID", 1L));
            note.setTitle(data.getStringExtra("TITLE"));
            note.setText(data.getStringExtra("TEXT"));
            note.setTime(data.getLongExtra("TIME", 1L));
            adapter.updateItem();
            addToDb(note);
        }
    }

    private void addToDb(final Note note) {
        realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(note);
            }
        });
    }

    private void addNote(final Note note) {
        //notesList.add(note);
        adapter.addItem(note);
        recyclerView.getItemAnimator().setAddDuration(1000);
        recyclerView.getAdapter().notifyItemInserted(0);
        recyclerView.smoothScrollToPosition(0);
        addToDb(note);
    }

    private void retrieveDataFromDb() {
        Observable<List<Note>> observer = Observable.fromCallable(getNotesFromRealmCallable());
        observer.subscribe(new Consumer<List<Note>>() {
            @Override
            public void accept(List<Note> notes) throws Exception {
                notesList.addAll(notes);
                adapter.addItems(notesList);
                adapter.notifyDataSetChanged();
            }
        });


    }

    public Callable<List<Note>> getNotesFromRealmCallable(){
        return new Callable<List<Note>>() {
            @Override
            public List<Note> call() throws Exception {
                realm = Realm.getDefaultInstance();
                RealmResults<Note> realmResults = realm.where(Note.class)
                        .distinctValues("id").sort("time", Sort.DESCENDING).findAll();
                return realm.copyToRealm(realmResults);
            }
        };
    }
}
