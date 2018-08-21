package com.wordpress.gistschool.raji.farmchat;

import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FarmerActivity extends AppCompatActivity implements HomeFarmerFragment.OnFragmentInteractionListener,
DashBoardFarmFragment.OnFragmentInteractionListener,
whoToChatUpFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_farmer );
        TabLayout tab = (TabLayout) findViewById( R.id.tl_student );
        tab.addTab( tab.newTab( ).setIcon( R.drawable.ic_home_black_24dp ) );
        tab.addTab( tab.newTab( ).setIcon( R.drawable.ic_mail_outline_black_24dp ) );
        tab.addTab( tab.newTab( ).setIcon( R.drawable.ic_dashboard_black_24dp ) );

        //set Tab gravity to fill and color to light blue

        tab.setTabGravity( TabLayout.GRAVITY_FILL );
        tab.setSelectedTabIndicatorColor( Color.rgb( 255,255,255 ) );
        tab.setPadding( 4,4,4,4 );
        // create ViewPager, initialize it and set an adapter to it
        final ViewPager pager = (ViewPager) findViewById( R.id.vp_swap );
        final PagerAdapter adapter = new PagerAdapter( getSupportFragmentManager( ), tab.getTabCount( ) );
        pager.setAdapter( adapter );
        pager.setOnPageChangeListener( new TabLayout.TabLayoutOnPageChangeListener( tab ) );
        tab.setOnTabSelectedListener( new TabLayout.OnTabSelectedListener( ) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem( tab.getPosition( ) );
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        } );
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}

