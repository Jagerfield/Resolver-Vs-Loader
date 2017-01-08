package jagerfield.app;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import jagerfield.ContentResolverVsCursorLoader.R;
import jagerfield.app.ListFragment.ContactListFragment;
import jagerfield.app.Utilities.C;
import jagerfield.utilities.lib.AppUtilities;
import jagerfield.utilities.lib.PermissionsUtil.GuiDialog.PermissionsManager;
import jagerfield.utilities.lib.PermissionsUtil.PermissionsUtil;
import jagerfield.utilities.lib.PermissionsUtil.Results.IGetPermissionResult;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int READ_CONTACT_PERMISSION_REQUEST_CODE = 76;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        checkPermissions();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }

    private void launchViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addTab(C.CURSOR_CONTENT_RESOLVER, ContactListFragment.newInstance(C.CURSOR_CONTENT_RESOLVER));
        viewPagerAdapter.addTab(C.CURSOR_LOADER, ContactListFragment.newInstance(C.CURSOR_LOADER));
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    public void checkPermissions()
    {
        PermissionsUtil permissionsUtil = AppUtilities.getPermissionUtil(this);
        IGetPermissionResult result = permissionsUtil.getPermissionResults(C.REQUIRED_PERMISSION);

        if (result.isGranted())
        {
            launchViewPager();
        }
        else if (!result.isGranted() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            //There are missing permissions ask for them
            permissionsUtil.requestPermissions(C.REQUIRED_PERMISSION);
        }
        else if (!result.isGranted() && Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            //For SDK less than M, there are permissions missing in the manifest
            String missingPermissions = TextUtils.join(", ", result.getMissingInManifest_ForSdkBelowM()).trim();
            Toast.makeText(this, "Following permissions are missing : " + missingPermissions, Toast.LENGTH_LONG).show();
            Log.e(C.TAG_LIB, "Following permissions are missing : " + missingPermissions);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        PermissionsUtil permissionsUtil = AppUtilities.getPermissionUtil(this);

        if (requestCode == permissionsUtil.getPermissionsReqCodeId())
        {
            IGetPermissionResult result = null;
            result = permissionsUtil.getPermissionResults(permissions);

            if (result.isGranted())
            {
                launchViewPager();
                return;
            }

            final AppCompatActivity activity = this;

            PermissionsManager.getNewInstance(activity, result, permissions, new PermissionsManager.PermissionsManagerCallback()
            {
                @Override
                public void onPermissionsGranted(IGetPermissionResult result) {

                    /**
                     * User accepted all requested permissions
                     */

                    launchViewPager();
                }

                @Override
                public void onPermissionsMissing(IGetPermissionResult result)
                {
                    //Write your code here
                    Toast.makeText(MainActivity.this, "User didn't accept all permissions", Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    public class ViewPagerAdapter extends FragmentPagerAdapter {

        private final ArrayList<FragModel> fragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position).fragment;
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentList.get(position).title;
        }

        public void addTab(String title, Fragment fragment) {
            fragmentList.add(new FragModel(title, fragment));
        }

        class FragModel {
            Fragment fragment;
            String title;

            public FragModel(String title, Fragment fragment) {
                this.fragment = fragment;
                this.title = title;
            }
        }
    }
}
