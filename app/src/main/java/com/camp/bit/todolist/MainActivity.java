package com.camp.bit.todolist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.camp.bit.todolist.beans.Note;
import com.camp.bit.todolist.beans.State;
import com.camp.bit.todolist.db.TodoContract;
import com.camp.bit.todolist.db.TodoDbHelper;
import com.camp.bit.todolist.debug.DebugActivity;
import com.camp.bit.todolist.ui.NoteListAdapter;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD = 1002;

    private RecyclerView recyclerView;
    private NoteListAdapter notesAdapter;
    TodoDbHelper mDbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

         mDbHelper= new TodoDbHelper(this);
         db=mDbHelper.getWritableDatabase();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        new Intent(MainActivity.this, NoteActivity.class),
                        REQUEST_CODE_ADD);
            }
        });

        recyclerView = findViewById(R.id.list_todo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        notesAdapter = new NoteListAdapter(new NoteOperator() {
            @Override
            public void deleteNote(Note note) {
                MainActivity.this.deleteNote(note);
            }

            @Override
            public void updateNote(Note note) {
                MainActivity.this.updateNode(note);
            }
        });
        recyclerView.setAdapter(notesAdapter);

        notesAdapter.refresh(loadNotesFromDatabase());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_debug:
                startActivity(new Intent(this, DebugActivity.class));
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD
                && resultCode == Activity.RESULT_OK) {
            notesAdapter.refresh(loadNotesFromDatabase());
        }
    }

    private List<Note> loadNotesFromDatabase() {
        // TODO 从数据库中查询数据，并转换成 JavaBeans

        if (db==null){return Collections.emptyList();}
        List<Note> result=new LinkedList<>();
        Cursor cursor=null;
        try {


             cursor=db.query(
                    TodoContract.FeedEntry.TABLE_NAME,
                    new String[]{TodoContract.FeedEntry._ID,TodoContract.FeedEntry.DATE,TodoContract.FeedEntry.STATE,TodoContract.FeedEntry.CONTENT},
                    null,
                    null,
                    null,
                    null,
                     TodoContract.FeedEntry.DATE+" DESC"

            );
             while (cursor.moveToNext()){
                 String content =cursor.getString(cursor.getColumnIndex(TodoContract.FeedEntry.CONTENT));
                 long date=cursor.getLong(cursor.getColumnIndex(TodoContract.FeedEntry.DATE));
                 long state=cursor.getLong(cursor.getColumnIndex(TodoContract.FeedEntry.STATE));
                 long id=cursor.getLong(cursor.getColumnIndex(TodoContract.FeedEntry._ID));
                 Log.d("gfsgcvsjkgja:",String.valueOf(id));
                 Note note=new Note();
                 note.setId(id);

                 note.setContent(content);
                 note.setState(State.from((int)state));
                 note.setDate(new Date(date));
                 result.add(note);
             }


        }finally {
            if(cursor!=null){
                cursor.close();
            }

        }

//SQLiteDatabase db=mDbHelper.getWritableDatabase();
//String[] projection={
//
//        TodoContract.FeedReaderContract.FeedEntry.AUTOID,
//        TodoContract.FeedReaderContract.FeedEntry.CONTENT,
//        TodoContract.FeedReaderContract.FeedEntry.DATE,
//        TodoContract.FeedReaderContract.FeedEntry.STATE
//};
//String selection=TodoContract.FeedReaderContract.FeedEntry.TABLE_NAME+"=?";
//String [] selectionArgs={"notebook"};
//
//String sortOrder=TodoContract.FeedReaderContract.FeedEntry.DATE+"DESC";
//




        return result;
    }

    private void deleteNote(Note note) {
        // TODO 删除数据
        Cursor cursor=null;

        Log.d("删除数据:",String.valueOf(note.getId()));
        String selection = TodoContract.FeedEntry._ID + " LIKE ?";
        String[] selectionArgs={Long.toString(note.id)};
        int deleteRows=db.delete(TodoContract.FeedEntry.TABLE_NAME,selection,selectionArgs);
        notesAdapter.refresh(loadNotesFromDatabase());





    }

    private void updateNode(Note note) {
        // TODO 更新数据??

        Log.d("更新状态:",String.valueOf(note.getId()));
        Log.d("更新状态前state:",String.valueOf(note.getState()));
        SQLiteDatabase db=mDbHelper.getWritableDatabase();
        State state1=note.getState();


        ContentValues values=new ContentValues();
        values.put(TodoContract.FeedEntry.STATE, 1);



        String selection = TodoContract.FeedEntry._ID + " LIKE ?";
        String[] selectionArgs={Long.toString(note.id)};

        int count=db.update(TodoContract.FeedEntry.TABLE_NAME,values,selection,selectionArgs);

        notesAdapter.refresh(loadNotesFromDatabase());
        Log.d("更新状态后state:",String.valueOf(note.getState()));


    }

}
