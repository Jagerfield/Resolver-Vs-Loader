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
        checkPermission();
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


    private void checkPermission()
    {
        PermissionsUtil permissionsUtil = AppUtilities.getPermissionUtil(MainActivity.this);
        IGetPermissionResult result = permissionsUtil.getPermissionResults(C.REQUIRED_PERMISSION);

        if (result.isGranted())
        {
            launchViewPager();
        }
        else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !result.isGranted())
        {
            /**
             * For SDK >= M, make a permission request
             */
            permissionsUtil.requestPermissions(C.REQUIRED_PERMISSION);
        }
        else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M && !result.isGranted())
        {
            /**
             * For SDK < M, there are permissions missing in the manifest
             */
            String missingPermissions = TextUtils.join(", ", result.getMissingInManifest_ForSdkBelowM()).trim();
            Toast.makeText(this, "Following permissions are missing : " + missingPermissions, Toast.LENGTH_LONG).show();
            Log.e(C.TAG_LIB, "Following permissions are missing : " + missingPermissions);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        PermissionsUtil permissionsUtil = AppUtilities.getPermissionUtil(MainActivity.this);

        if (requestCode == permissionsUtil.getPermissionsReqCodeId())
        {
            IGetPermissionResult result = null;
            result = permissionsUtil.getPermissionResults(C.REQUIRED_PERMISSION);
            if (result == null)
            {
                return;
            }

            if (result.isGranted())
            {
                launchViewPager();
            }
            else
            {
                /**
                 * For SDK >= M, there are permissions missing
                 */
                String deniedPermissions = TextUtils.join(", ", result.getUserDeniedPermissionsList()).trim();
                String neverAskAgainPermissions = TextUtils.join(", ", result.getNeverAskAgainPermissionsList()).trim();

                String missingPermissions = "";

                if (!deniedPermissions.isEmpty())
                {
                    if (!neverAskAgainPermissions.isEmpty())
                    {
                        neverAskAgainPermissions = ", " + neverAskAgainPermissions;
                    }

                    missingPermissions = deniedPermissions + neverAskAgainPermissions;
                }
                else
                {
                    missingPermissions = neverAskAgainPermissions;
                }

                Toast.makeText(this, "Following permissions are missing : " + missingPermissions, Toast.LENGTH_LONG).show();
                Log.e(C.TAG_LIB, "Following permissions are missing : " + missingPermissions);
            }
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
