package cz.inovett.cheating;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.builders.Actions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static cz.inovett.cheating.R.layout.blog_row;

public class MainActivity extends AppCompatActivity {
    private RecyclerView blogList;
    private DatabaseReference databaseReference;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseUsers;
    private CardView card_invisible;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,InsertActivity.class);
                startActivity(i);
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Blog");

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);
        card_invisible = (CardView) findViewById(R.id.card_invisible);

        auth = FirebaseAuth.getInstance();


        blogList = (RecyclerView) findViewById(R.id.blog_list);
        blogList.setHasFixedSize(true);
        blogList.setLayoutManager( new LinearLayoutManager(this));

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth == null){
                    Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            }
        };


    }

    @Override
    protected void onStart() {
        super.onStart();
        /*checkUserExist();*/

        auth.addAuthStateListener(mAuthListener);




        final FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class,
                blog_row,
                BlogViewHolder.class,
                databaseReference

        ) {
            @Override
            protected void populateViewHolder(final BlogViewHolder viewHolder, final Blog model, int position) {
                final String post_key = getRef(position).getKey();

                databaseReference.child(post_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {

                        String post_uid = (String) dataSnapshot.child("uid").getValue();
                        if (auth.getCurrentUser().getUid().equals(post_uid)) {
                            viewHolder.setTitle(model.getTitle());
                            viewHolder.setFullText(model.getText());
                        }else{
                            viewHolder.itemView.setVisibility(View.GONE);
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent singleBlogInetnt = new Intent(MainActivity.this, BlogSingleActivity.class);
                        singleBlogInetnt.putExtra("blog_id", post_key);
                        startActivity(singleBlogInetnt);
                    }
                });

            }
        };

        blogList.setAdapter(firebaseRecyclerAdapter);

        super.onStart();




        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        FirebaseUserActions.getInstance().start(getIndexApiAction());
        }





    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        return Actions.newView("MainMenu", "http://[ENTER-YOUR-URL-HERE]");
    }

    @Override
    public void onStop() {

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        FirebaseUserActions.getInstance().end(getIndexApiAction());
        super.onStop();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    public  static  class BlogViewHolder extends RecyclerView.ViewHolder{

        View view;
        FirebaseAuth mAuth;
        public BlogViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            mAuth = FirebaseAuth.getInstance();
        }



        public void setTitle(String title) {
            TextView post_title = (TextView) view.findViewById(R.id.post_Name);
                post_title.setText(title);

        }
        public void setFullText(String Text) {
            TextView full_text = (TextView) view.findViewById(R.id.post_full_text);
                full_text.setText(Text);


        }
    }
}
