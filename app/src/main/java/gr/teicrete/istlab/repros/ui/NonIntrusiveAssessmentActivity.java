package gr.teicrete.istlab.repros.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import gr.teicrete.istlab.repros.R;
import gr.teicrete.istlab.repros.model.db.DBHandler;

public class NonIntrusiveAssessmentActivity extends AppCompatActivity {

    private Button btnEndSession;

    private String roomId;


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_intrusive_assessment);

        roomId = getIntent().getStringExtra("EXTRA_ROOM_ID");
//        recommendationsArray = getIntent().getStringArrayExtra("EXTRA_RECOMMENDATIONS");
//        timestamps = getIntent().getLongArrayExtra("EXTRA_TIMESTAMP");
//        motion = getIntent().getDoubleArrayExtra("EXTRA_MOTION");
//        audio = getIntent().getDoubleArrayExtra("EXTRA_AUDIO");
//        light = getIntent().getDoubleArrayExtra("EXTRA_LIGHT");

        btnEndSession = (Button) findViewById(R.id.btn_end_session);
        btnEndSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DBHandler dbHandler = new DBHandler(roomId, false);
                dbHandler.removeTemporaryReadingsSnapshots();

                Intent intent = new Intent(NonIntrusiveAssessmentActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        setupTabIcons();
    }

    private void setupTabIcons() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_assessment_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_info_white_24dp);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            if (position == 0) {
//                return NonIntrusiveResultsFragment.newInstance(timestamps, motion, audio, light);
                return NonIntrusiveResultsFragment.newInstance(roomId);
            } else if (position == 1) {
//                return RecommendationsFragment.newInstance(recommendationsArray);
                return RecommendationsFragment.newInstance(roomId, false);
            }

            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Results";
                case 1:
                    return "Recommendations";
            }
            return null;
        }
    }
}
