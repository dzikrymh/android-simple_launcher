package id.mdsolusindo.partriplauncher.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;

import id.mdsolusindo.partriplauncher.R;
import id.mdsolusindo.partriplauncher.adapters.AppAdapter;
import id.mdsolusindo.partriplauncher.adapters.ViewPagerAdapter;
import id.mdsolusindo.partriplauncher.databinding.ActivityMainBinding;
import id.mdsolusindo.partriplauncher.models.AppObject;
import id.mdsolusindo.partriplauncher.models.PagerObject;
import id.mdsolusindo.partriplauncher.viewmodels.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;

    private ViewPager mViewPager;
    private int cellHeight;

    private int NUMBER_OF_ROWS = 5;
    private int DRAWER_PEEK_HEIGHT = 100;
    private String PREFS_NAME = "PartripLauncherPrefs";

    private int numRow = 0;
    private int numColumn = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        setContentView(binding.getRoot());

        getPermissions();
        getData();

        binding.topDrawerLayout.post(() -> {
            DRAWER_PEEK_HEIGHT = binding.topDrawerLayout.getHeight();
            initializeHome();
            initializeDrawer();
        });

        binding.settings.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SettingActivity.class)));
    }

    private ViewPagerAdapter mViewPagerAdapter;
    private void initializeHome() {
        ArrayList<PagerObject> pagerAppList = new ArrayList<>();

        ArrayList<AppObject> appList1 = new ArrayList<>();
        ArrayList<AppObject> appList2 = new ArrayList<>();
        ArrayList<AppObject> appList3 = new ArrayList<>();

        installedAppList = getInstalledAppList();
        for (AppObject app : installedAppList) {
            if (app.getPackageName().equals("id.mdsolusindo.partripots")) {
                appList1.add(new AppObject(app.getPackageName(), app.getName(), app.getImage(), app.getAppInDrawer()));
            }
        }

        for (int i = 1; i < numColumn*numRow ;i++)
            appList1.add(new AppObject("", "", getResources().getDrawable(R.drawable.ic_launcher_foreground), false));
        for (int i = 0; i < numColumn*numRow ;i++)
            appList2.add(new AppObject("", "", getResources().getDrawable(R.drawable.ic_launcher_foreground), false));
        for (int i = 0; i < numColumn*numRow ;i++)
            appList3.add(new AppObject("", "", getResources().getDrawable(R.drawable.ic_launcher_foreground), false));

        pagerAppList.add(new PagerObject(appList1));
        pagerAppList.add(new PagerObject(appList2));
        pagerAppList.add(new PagerObject(appList3));

        cellHeight = (getDisplayContentHeight() - DRAWER_PEEK_HEIGHT) / numRow ;

        mViewPager = findViewById(R.id.viewPager);
        mViewPagerAdapter = new ViewPagerAdapter(this, pagerAppList, cellHeight, numColumn);
        mViewPager.setAdapter(mViewPagerAdapter);
    }

    private List<AppObject> installedAppList = new ArrayList<>();
    private BottomSheetBehavior mBottomSheetBehavior;
    private void initializeDrawer() {
        mBottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet);
        mBottomSheetBehavior.setHideable(false);
        mBottomSheetBehavior.setPeekHeight(DRAWER_PEEK_HEIGHT);

        installedAppList = getInstalledAppList();

        binding.drawerGrid.setAdapter(new AppAdapter(this, installedAppList, cellHeight));

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(mAppDrag != null)
                    return;

                if(newState == BottomSheetBehavior.STATE_COLLAPSED && binding.drawerGrid.getChildAt(0).getY() != 0)
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if(newState == BottomSheetBehavior.STATE_DRAGGING && binding.drawerGrid.getChildAt(0).getY() != 0)
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    public AppObject mAppDrag = null;
    public void itemPress(AppObject app){
        if(mAppDrag != null && !app.getName().equals("")){
            Toast.makeText(this,"Cell Already Occupied", Toast.LENGTH_SHORT).show();
            return;
        }
        if(mAppDrag != null && !app.getAppInDrawer()){

            app.setPackageName(mAppDrag.getPackageName());
            app.setName(mAppDrag.getName());
            app.setImage(mAppDrag.getImage());
            app.setAppInDrawer(false);

            if(!mAppDrag.getAppInDrawer()){
                mAppDrag.setPackageName("");
                mAppDrag.setName("");
                mAppDrag.setImage(getResources().getDrawable(R.drawable.ic_launcher_foreground));
                mAppDrag.setAppInDrawer(false);
            }
            mAppDrag = null;
            mViewPagerAdapter.notifyGridChanged();
            return;
        }else{
            System.out.println("app::"+app.getPackageName());
            Intent launchAppIntent = getApplicationContext().getPackageManager().getLaunchIntentForPackage(app.getPackageName());
            if (launchAppIntent != null)
                getApplicationContext().startActivity(launchAppIntent);
        }
    }

    public void itemLongPress(AppObject app){
        collapseDrawer();
        mAppDrag = app;
    }

    private void collapseDrawer() {
        binding.drawerGrid.setY(DRAWER_PEEK_HEIGHT);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private List<AppObject> getInstalledAppList() {
        List<AppObject> list = new ArrayList<>();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> untreatedAppList = getApplicationContext().getPackageManager().queryIntentActivities(intent, 0);

        for(ResolveInfo untreatedApp : untreatedAppList){
            String appName = untreatedApp.activityInfo.loadLabel(getPackageManager()).toString();
            String appPackageName = untreatedApp.activityInfo.packageName;
            Drawable appImage = untreatedApp.activityInfo.loadIcon(getPackageManager());

            AppObject app = new AppObject(appPackageName, appName, appImage, true);
            if (!list.contains(app))
                list.add(app);
        }
        return list;
    }


    private int getDisplayContentHeight() {
        final WindowManager windowManager = getWindowManager();
        final Point size = new Point();
        int screenHeight = 0, actionBarHeight = 0, statusBarHeight = 0;
        if(getActionBar()!=null){
            actionBarHeight = getActionBar().getHeight();
        }

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if(resourceId > 0){
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        int contentTop = (findViewById(android.R.id.content)).getTop();
        windowManager.getDefaultDisplay().getSize(size);
        screenHeight = size.y;
        return screenHeight - contentTop - actionBarHeight - statusBarHeight;
    }


    private void getData(){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String imageUri = sharedPreferences.getString("imageUri", null);
        int numRow = sharedPreferences.getInt("numRow", 7);
        int numColumn = sharedPreferences.getInt("numColumn", 5);

        if(this.numRow != numRow || this.numColumn != numColumn){
            this.numRow = numRow;
            this.numColumn = numColumn;
            initializeHome();
        }

        if(imageUri != null)
            binding.homeScreenImage.setImageURI(Uri.parse(imageUri));

    }
    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
}