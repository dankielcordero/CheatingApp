package cz.inovett.cheating;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class BlogSingleActivity extends AppCompatActivity {
    private String mPost_key;
    private DatabaseReference mDatabase;

    private Button mSingleRomeBtn;

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog_single);
        getSupportActionBar().hide();


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");

        mAuth = FirebaseAuth.getInstance();

        mPost_key = getIntent().getExtras().getString("blog_id");

        mSingleRomeBtn = (Button) findViewById(R.id.buttonRemove);



        //Toast.makeText(BlogSingleActivity.this, post_key, Toast.LENGTH_SHORT).show();

       /* mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                String post_uid = (String) dataSnapshot.child("uid").getValue();
                if (mAuth.getCurrentUser().getUid().equals(post_uid)) {
                    mSingleRomeBtn.setVisibility(View.VISIBLE);
                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
       mSingleRomeBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mDatabase.child(mPost_key).removeValue();
               Intent mainIntent = new Intent(BlogSingleActivity.this, MainActivity.class);
               startActivity(mainIntent);
           }
       });
    }



}
