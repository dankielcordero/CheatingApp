package cz.inovett.cheating;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InsertActivity extends AppCompatActivity {
    private EditText name, fullText;
    private Button mSubmitBtn;
    private DatabaseReference databaseReference;
    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        name = (EditText) findViewById(R.id.nameField);
        fullText = (EditText) findViewById(R.id.editTextFullText);
        mSubmitBtn = (Button) findViewById(R.id.buttonSubmit);
        mProgress = new ProgressDialog(this);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Blog");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());


        mSubmitBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                StartPosting();
            }
        });
    }
    private void StartPosting() {
        final String strName = name.getText().toString().trim();
        final String strFullText = fullText.getText().toString().trim();

        if (!TextUtils.isEmpty(strName)
                &&!TextUtils.isEmpty(strFullText)){
            mProgress.setMessage("Posting ....");
            mProgress.show();
            final DatabaseReference newPost = databaseReference.push();


            mDatabaseUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    newPost.child("title").setValue(strName);
                    newPost.child("Text").setValue(strFullText);
                    newPost.child("uid").setValue(mCurrentUser.getUid());
                    Intent i = new Intent(InsertActivity.this, MainActivity.class);
                    Toast.makeText(InsertActivity.this, "Successfully added", Toast.LENGTH_SHORT).show();
                    startActivity(i);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            mProgress.dismiss();

        }

    }
}
