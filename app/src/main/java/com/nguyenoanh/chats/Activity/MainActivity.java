package com.nguyenoanh.chats.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenoanh.chats.Fragments.ChatFragment;
import com.nguyenoanh.chats.Fragments.ProfileFragment;
import com.nguyenoanh.chats.Fragments.UserFragment;
import com.nguyenoanh.chats.Model.User;
import com.nguyenoanh.chats.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    CircleImageView profileImage;
    TextView username;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar;
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar ().setDisplayHomeAsUpEnabled (true);

        profileImage = findViewById(R.id.profileImage);
        username =(TextView) findViewById(R.id.tvUserName);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUserName());

                if (user.getImageURL ().equals ("default")){
                    profileImage.setImageResource(R.drawable.anh);
                }else{
                    Glide.with(MainActivity.this).load(user.getImageURL())
                            .into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // create 2 fragment chats and users in activity main
        TabLayout tabLayout = (TabLayout) findViewById (R.id.tab_layout);
        ViewPager viewPager = (ViewPager) findViewById (R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter (getSupportFragmentManager ());
        viewPagerAdapter.addFragment (new ChatFragment (), "Chats");
        viewPagerAdapter.addFragment (new UserFragment (), "Users");
        viewPagerAdapter.addFragment (new ProfileFragment (), "Profile");

        viewPager.setAdapter (viewPagerAdapter);

        tabLayout.setupWithViewPager (viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, StartActivity.class));
                return true;
        }
        return false;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter{
        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
            this.fragments = new ArrayList<> ();
            this.titles = new ArrayList<> ();
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get (i);
        }

        @Override
        public int getCount() {
            return fragments.size ();
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add (fragment);
            titles.add (title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int i) {
            return titles.get (i);
        }
    }
}
