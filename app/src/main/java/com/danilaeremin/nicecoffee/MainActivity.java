package com.danilaeremin.nicecoffee;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.danilaeremin.nicecoffee.Fragments.CategoryFragment;
import com.danilaeremin.nicecoffee.Fragments.MainFragment;
import com.danilaeremin.nicecoffee.Fragments.NavigationDrawerFragment;
import com.danilaeremin.nicecoffee.core.NiceCoffeeAuthServer;
import com.danilaeremin.nicecoffee.core.Utils;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    final String LOG_TAG = ActionBarActivity.class.getSimpleName();

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getDbTime() == 0 &&
            getSharedPreferences(getString(R.string.content_authority),MODE_PRIVATE).
                    getBoolean("FIRST_RUN_DIALOG", true)) {
            getSharedPreferences(getString(R.string.content_authority),MODE_PRIVATE).
                    edit().putBoolean("FIRST_RUN_DIALOG", false).commit();

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(getString(R.string.first_run_head))
                    .setMessage(getString(R.string.first_run_text))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.first_run_now), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                            downloadDb();

                            getSharedPreferences(getString(R.string.content_authority),MODE_PRIVATE).
                                    edit().putBoolean("FIRST_RUN_DIALOG", true).commit();
                        }
                    })
                    .setCancelable(true)
                    .setNegativeButton(getString(R.string.first_run_later),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                    getSharedPreferences(getString(R.string.content_authority),MODE_PRIVATE).
                                            edit().putBoolean("FIRST_RUN_DIALOG", true).commit();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void downloadDb () {
        Intent intent = new Intent(this, NiceCoffeeAuthServer.class);
        startService(intent.putExtra(NiceCoffeeAuthServer.TASK_TYPE, NiceCoffeeAuthServer.FORCE_UPDATE_DB));
    }

    public long getDbTime () {
        return getSharedPreferences(getString(R.string.content_authority),MODE_PRIVATE).getLong(NiceCoffeeAuthServer.DB_TIME, 0);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        switch (position) {
            case -1: {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, MainFragment.newInstance())
                        .commit();

                break;
            }
            case 0: {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, MainFragment.newInstance())
                        .commit();

                break;
            }
            case 1: {
                break;
            }
            case 2: {
                break;
            }
            case 3: {
                makeCall(getString(R.string.drawer_main_3));

                break;
            }
            case 4: {

                Intent mIntent = new Intent(this, AboutUsActivity.class);
                startActivity(mIntent);

                break;
            }
            case 5: {
                Log.e(LOG_TAG, "NO SUCH BUTTON");
            }

            case 15: {
                break;
            }
            case 16: {
                break;
            }
            case 17: {
                break;
            }
            case 18: {
                break;
            }

            default: {
                Integer category_id = Utils.getBasicCategory(position-6);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, CategoryFragment.newInstance(category_id))
                        .addToBackStack( getString(R.string.back_stack_tag) )
                        .commit();
            }
        }
    }

    public void onSectionAttached(String title) {
        mTitle = title;
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    private void makeCall (String phone) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phone));
            startActivity(callIntent);
        } catch (ActivityNotFoundException activityException) {
            Log.e("Calling a Phone Number", "Call failed", activityException);
        }
    }
}
