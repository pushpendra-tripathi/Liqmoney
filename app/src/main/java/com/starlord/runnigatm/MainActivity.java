package com.starlord.runnigatm;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    ImageView profileImage;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        MaterialToolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        profileImage = findViewById(R.id.profile_image);
        drawerLayout = findViewById(R.id.activity_main);
//        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
//                R.string.open, R.string.close);
//
//        drawerLayout.addDrawerListener(actionBarDrawerToggle);
//        actionBarDrawerToggle.syncState();


        navigationView = findViewById(R.id.nv);

        profileImage.setOnClickListener(v -> openDrawer());

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent;
            switch(id)
            {
                case R.id.action_profile:
                    intent = new Intent(MainActivity.this, ProfileActivity.class);
                    break;
                case R.id.action_chats:
                    intent = new Intent(MainActivity.this, ChatActivity.class);
                    break;
                case R.id.action_settings:
                    intent = new Intent(MainActivity.this, SettingsActivity.class);
                    break;
                case R.id.action_transaction:
                    intent = new Intent(this, TransactionsActivity.class);
                    break;
                case R.id.action_help:
                    intent = new Intent(this, HelpActivity.class);
                    break;
                default:
                    return true;
            }

            closeDrawer();
            startActivity(intent);

            return true;

        });
    }

    public void closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void openDrawer() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }
}