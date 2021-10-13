package com.example.parstagram.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.parstagram.LoginActivity;
import com.example.parstagram.Post;
import com.example.parstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends PostsFragment{
    private static final String TAG = "ProfileFragment";

    private TextView tvLoggedInUser;
    private Button btnLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvLoggedInUser = view.findViewById(R.id.tvLoggedInUser);
        btnLogout = view.findViewById(R.id.btnLogout);

        tvLoggedInUser.setText("@" + ParseUser.getCurrentUser().getUsername());
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Logging out current user");
                ParseUser.logOut();
                Log.i(TAG, "Current user logged out");

                Log.i(TAG, "Going back to login activity");
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void queryPosts() {
        // Specifying model to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // Including foreign key in query
        query.include(Post.KEY_USER);
        // Selecting signed in user's posts
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        // Limiting query to first 20 posts
        query.setLimit(20);
        // Ordering posts by created date
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        // Choosing object to query
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    // Unsuccessful query
                    Log.e(TAG, "Failed query for posts", e);

                }
                // Successful query
                for (Post post : posts) {
                    // Iterating through retrieved post objects
                    Log.i(TAG, "Post: " + post.getDescription());
                }
                adapter.clear();
                adapter.addAll(posts);
                swipeContainer.setRefreshing(false);
            }
        });
    }
}
