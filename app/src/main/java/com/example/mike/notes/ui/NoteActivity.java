package com.example.mike.notes.ui;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mike.notes.R;
import com.example.mike.notes.model.Note;
import com.example.mike.notes.utils.Utils;

import io.realm.Realm;

public class NoteActivity extends AppCompatActivity {

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        setTitle("Edit");

        final TextInputLayout tilTitle = findViewById(R.id.tilNewNoteTitle);
        final EditText etTitle = tilTitle.getEditText();
        final TextInputLayout tilText = findViewById(R.id.tilNewNoteText);
        final EditText etText = tilText.getEditText();
        final TextView tv_time = findViewById(R.id.tvTime);
        final Button btnSave = findViewById(R.id.btnSaveNote);
        etTitle.setText(getIntent().getStringExtra("TITLE"));
        etText.setText(getIntent().getStringExtra("TEXT"));
        tv_time.setText("Последнее изменение " + Utils.parseDate(getIntent().getLongExtra("TIME", 1L), this));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Note note = new Note();
                note.setId(NoteActivity.this.getIntent().getLongExtra("ID", 1L));
                note.setTime(System.currentTimeMillis());
                note.setTitle(etTitle.getText().toString());
                note.setText(etText.getText().toString());
                int positon = getIntent().getIntExtra("POSITION", 0);

                Intent replyIntent = new Intent();

                replyIntent.putExtra("TITLE", note.getTitle());
                replyIntent.putExtra("TEXT", note.getText());
                replyIntent.putExtra("TIME", note.getTime());
                replyIntent.putExtra("ID", note.getId());
                replyIntent.putExtra("POSITION", positon);
                setResult(RESULT_OK, replyIntent);
                Toast.makeText(NoteActivity.this, "Note has been changed", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
