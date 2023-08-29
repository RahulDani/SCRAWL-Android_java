package com.rad.notes_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class Notes_deatils extends AppCompatActivity {
    EditText titleEditText,contentEditText;
    ImageButton saveNoteBtn;

    TextView pageTitletextView;
    String title,content,docId;

    boolean isEditMode=false;

    TextView deletebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_deatils);

        titleEditText=findViewById(R.id.note_title);
        contentEditText=findViewById(R.id.note_content);
        saveNoteBtn=findViewById(R.id.saveNotebtn);
        pageTitletextView=findViewById(R.id.page_title);
        deletebtn=findViewById(R.id.deleteBtnid);

        title=getIntent().getStringExtra("title");
        content=getIntent().getStringExtra("content");
        docId=getIntent().getStringExtra("docId");

        if(docId!=null && !docId.isEmpty()){
            isEditMode=true;
        }

        titleEditText.setText(title);
        contentEditText.setText(content);
        if(isEditMode){
            pageTitletextView.setText("EDIT YOUR NOTE");
            deletebtn.setVisibility(View.VISIBLE);
        }

        saveNoteBtn.setOnClickListener(view ->saveNote() );
        deletebtn.setOnClickListener(view -> deleteNoteFromFireBase());
    }

    void saveNote(){
        String noteTitle=titleEditText.getText().toString();
        String noteContent=contentEditText.getText().toString();

        if(noteTitle.isEmpty()){
            titleEditText.setError("Please provide Title");
            return;
        }

        Note note=new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        saveNoteToFirebase(note);



    }

    void saveNoteToFirebase(Note note){
        DocumentReference documentReference;

        if(isEditMode){
            documentReference = Utility.getCollectionRefForNotes().document(docId);
        }
        else{
            documentReference = Utility.getCollectionRefForNotes().document();
        }


        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Notes_deatils.this, "Notes are added successfully", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Notes_deatils.this,MainActivity.class));
                }
                else {
                    Toast.makeText(Notes_deatils.this, "Failed to add Notes", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    void deleteNoteFromFireBase(){

        DocumentReference documentReference;
        documentReference = Utility.getCollectionRefForNotes().document(docId);
        


        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Notes_deatils.this, "Notes are deleted successfully", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Notes_deatils.this,MainActivity.class));
                }
                else {
                    Toast.makeText(Notes_deatils.this, "Failed to delete Notes", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




}
