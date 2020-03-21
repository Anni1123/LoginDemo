package com.example.logindemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends AppCompatActivity {

    private DatabaseReference mUserDatabase;
    private Toolbar mtoolbar;
    FirebaseRecyclerAdapter<Users, UsersviewHolder> firebaseRecyclerAdapter;
    private RecyclerView mRecycle;
    @Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
       setSupportActionBar(mtoolbar);
        mtoolbar=(Toolbar)findViewById(R.id.user_layout);
        setSupportActionBar(mtoolbar);
       getSupportActionBar().setTitle("Accounts Update");

    mRecycle=(RecyclerView)findViewById(R.id.recycle);
    mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
    mRecycle.setHasFixedSize(true);
    mRecycle.setLayoutManager(new LinearLayoutManager(this));
}

    @Override
    protected void onStart() {
        super.onStart();
        Query query=mUserDatabase.limitToLast(50).orderByPriority();
        FirebaseRecyclerOptions<Users> options =new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(query,Users.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersviewHolder>(options) {
            @NonNull
            @Override
            public UsersviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_single_layout, parent, false);
                return new UsersviewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull UsersviewHolder holder, int position, @NonNull Users model) {

                holder.setName(model.getName());

                holder.setStatus(model.getStatus());
                holder.setImage(model.getImage(),getApplicationContext());
                final String user_id=getRef(position).getKey();
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent profileIntent=new Intent(UserActivity.this,ProfileActivity.class);
                        profileIntent.putExtra("user_id",user_id);
                        startActivity(profileIntent);

                    }
                });
            }
        };
        firebaseRecyclerAdapter.startListening();
        mRecycle.setAdapter(firebaseRecyclerAdapter);
    }
    public class UsersviewHolder extends RecyclerView.ViewHolder{

        View mView;
        public UsersviewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setName(String name) {
            TextView usersname = (TextView) mView.findViewById(R.id.display_name);
            usersname.setText(name);
        }
        public void setStatus(String status){
            TextView userstatus=(TextView)mView.findViewById(R.id.default_status);
            userstatus.setText(status);
        }

        public void setImage(String image, Context ctx){
            CircleImageView imageuser=(CircleImageView)mView.findViewById(R.id.profile_image);
            Picasso.with(UserActivity.this).load(image).placeholder(R.mipmap.ic_launcher).into(imageuser);
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();


    }
}