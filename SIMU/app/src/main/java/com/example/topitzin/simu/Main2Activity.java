package com.example.topitzin.simu;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;

import com.example.topitzin.simu.objetos.SecctionPagerAdapter;

public class Main2Activity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,
        NanoStationFragment.OnFragmentInteractionListener, RadioFragment.OnFragmentInteractionListener ,
        Users.OnFragmentInteractionListener, UsuarioFragment.OnFragmentInteractionListener{


    SecctionPagerAdapter mPageAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mPageAdapter = new SecctionPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        setUpViewPager(mViewPager);

        TabLayout tabLayout  = (TabLayout)findViewById(R.id.toolbarup);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.home);
        tabLayout.getTabAt(1).setIcon(R.drawable.wifi_signal4);
        tabLayout.getTabAt(2).setIcon(R.drawable.settings6);
        //tabLayout.getTabAt(2).setIcon(R.drawable.calendar1);
        //tabLayout.getTabAt(3).setIcon(R.drawable.rss);
    }

    private void setUpViewPager(ViewPager mViewPager) {
        SecctionPagerAdapter adapter = new SecctionPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new NanoStationFragment());
        adapter.addFragment(new Users());

        mViewPager.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
