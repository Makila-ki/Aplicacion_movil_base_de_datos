package com.example.proyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;

import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


import com.example.proyecto.adapter.Adaptador;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;


public class Home extends AppCompatActivity {

    ViewPager2 viewPager2;
    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    BottomNavigationView bottomNavigationView;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        viewPager2=findViewById(R.id.Vistata);
        bottomNavigationView = findViewById(R.id.navi);

        fragmentArrayList.add(new HomeFragment());
        fragmentArrayList.add(new CarFragment());
        fragmentArrayList.add(new UsuFragment());
        Adaptador adaptador = new Adaptador(this, fragmentArrayList);
        viewPager2.setAdapter(adaptador);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.firstFragment);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.secondFragment);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.thirdFragment);
                        break;
                }

                super.onPageSelected(position);
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.firstFragment){
                    viewPager2.setCurrentItem(0);
                } else if (item.getItemId()==R.id.secondFragment) {
                    viewPager2.setCurrentItem(1);
                } else if (item.getItemId()==R.id.thirdFragment) {
                    viewPager2.setCurrentItem(2);
                }
                return true;
            }
        });


    }



}