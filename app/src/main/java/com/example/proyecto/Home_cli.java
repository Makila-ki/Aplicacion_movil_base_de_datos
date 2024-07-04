package com.example.proyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.proyecto.adapter.Adaptador;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class Home_cli extends AppCompatActivity {
    ViewPager2 viewPager3;
    ArrayList<Fragment> fragmentArrayListt = new ArrayList<>();
    BottomNavigationView bottomNavigationVieww;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cli);
        viewPager3 = findViewById(R.id.Vistata_cli);
        bottomNavigationVieww = findViewById(R.id.navi_cli);

        fragmentArrayListt.add(new Fragment_home_cli());
        fragmentArrayListt.add(new Fragment_cart_cli());
        fragmentArrayListt.add(new Fragment_cliente());
        Adaptador adaptador = new Adaptador(this, fragmentArrayListt);
        viewPager3.setAdapter(adaptador);

        viewPager3.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationVieww.setSelectedItemId(R.id.firstFragmentt);
                        break;
                    case 1:
                        bottomNavigationVieww.setSelectedItemId(R.id.secondFragmentt);
                        break;
                    case 2:
                        bottomNavigationVieww.setSelectedItemId(R.id.thirdFragmentt);
                        break;
                }
                super.onPageSelected(position);
            }
        });

        bottomNavigationVieww.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.firstFragmentt){
                    viewPager3.setCurrentItem(0);
                } else if (item.getItemId()==R.id.secondFragmentt) {
                    viewPager3.setCurrentItem(1);
                } else if (item.getItemId()==R.id.thirdFragmentt) {
                    viewPager3.setCurrentItem(2);
                }
                return true;
            }
        });
    }
}