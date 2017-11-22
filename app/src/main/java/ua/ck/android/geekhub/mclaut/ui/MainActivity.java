package ua.ck.android.geekhub.mclaut.ui;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import ua.ck.android.geekhub.mclaut.R;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mainDrawerLayout;
    private ActionBarDrawerToggle mainDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_main);
        mainDrawerToggle = new ActionBarDrawerToggle(this, mainDrawerLayout,
                R.string.drawer_main_open, R.string.drawer_main_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(getTitle());
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
                getActionBar().setTitle(getTitle());
                invalidateOptionsMenu();
            }
        };
        mainDrawerLayout.setDrawerListener(mainDrawerToggle);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
}
