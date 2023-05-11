package com.example.bigmap;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;


import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.bigmap.bottom.BoardFragment;
import com.example.bigmap.bottom.Bottom_Favorite;
import com.example.bigmap.bottom.Bottom_Home;
import com.example.bigmap.bottom.MypageFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.skt.tmap.TMapData;
import com.skt.tmap.TMapTapi;
import com.skt.tmap.engine.navigation.network.RequestConstant;
import com.skt.tmap.engine.navigation.network.ndds.NddsDataType;
import com.skt.tmap.engine.navigation.route.RoutePlanType;
import com.skt.tmap.engine.navigation.route.data.MapPoint;
import com.skt.tmap.engine.navigation.route.data.WayPoint;
import com.skt.tmap.poi.TMapPOIItem;
import com.tmapmobility.tmap.tmapsdk.ui.fragment.NavigationFragment;
import com.tmapmobility.tmap.tmapsdk.ui.util.TmapUISDK;
import com.skt.tmap.engine.navigation.SDKManager;
import com.skt.tmap.engine.navigation.network.ndds.CarOilType;
import com.skt.tmap.engine.navigation.network.ndds.TollCarType;
import com.skt.tmap.engine.navigation.network.ndds.dto.request.TruckType;
import com.skt.tmap.engine.navigation.route.RoutePlanType;
import com.skt.tmap.vsm.coordinates.VSMCoordinates;
import com.skt.tmap.vsm.data.VSMMapPoint;
import com.skt.tmap.vsm.map.MapEngine;
import com.skt.tmap.vsm.map.marker.MarkerImage;
import com.skt.tmap.vsm.map.marker.VSMMarkerBase;
import com.skt.tmap.vsm.map.marker.VSMMarkerManager;
import com.skt.tmap.vsm.map.marker.VSMMarkerPoint;
import com.tmapmobility.tmap.tmapsdk.ui.data.CarOption;
import com.tmapmobility.tmap.tmapsdk.ui.data.TruckInfoKey;
import com.tmapmobility.tmap.tmapsdk.ui.fragment.NavigationFragment;
import com.tmapmobility.tmap.tmapsdk.ui.util.TmapUISDK;
import com.tmapmobility.tmap.tmapsdk.ui.view.MapConstant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
public class MainActivity extends AppCompatActivity {



    private NavigationFragment navigationFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private static final String TAG = "Big_Map";
    private final static String CLIENT_ID = "";
    private final static String API_KEY = BuildConfig.Api_key;
    private final static String USER_KEY = "";
    boolean isEDC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 제거 코드 입니다.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        initnav();
    }
    private void initnav(){
        FrameLayout bottomSheet = findViewById(R.id.main_content);
        FrameLayout mainLayout = findViewById(R.id.tmapUILayout);
        int screenHeight = getResources().getDisplayMetrics().heightPixels;


        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(getResources().getDimensionPixelSize(R.dimen.main_layout_height));
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                int bottomSheetHeight = 400;
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetHeight = (int) (bottomSheet.getHeight())-150;
                }
                int mainLayoutHeight = screenHeight - bottomSheetHeight;
                mainLayout.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mainLayoutHeight));
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                int bottomSheetHeight = (int) (bottomSheet.getHeight() * slideOffset);
                int mainLayoutHeight = screenHeight - bottomSheetHeight;
                mainLayout.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mainLayoutHeight));
            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavi);
        getSupportFragmentManager().beginTransaction().add(R.id.main_content,new Bottom_Home()).commit();
        //바텀 네비게이션뷰 안의 아이템 설정
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    //item을 클릭시 id값을 가져와 FrameLayout에 fragment.xml띄우기
                    case R.id.item_home:
                        findViewById(R.id.main_frame).setVisibility(View.INVISIBLE);
                        findViewById(R.id.main_content).setVisibility(View.VISIBLE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new Bottom_Home()).commit();

                        break;
                    case R.id.item_star:
                        findViewById(R.id.main_frame).setVisibility(View.INVISIBLE);
                        findViewById(R.id.main_content).setVisibility(View.VISIBLE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new Bottom_Favorite()).commit();

                        break;
                    case R.id.item_viewList:
                        findViewById(R.id.main_frame).setVisibility(View.VISIBLE);
                        findViewById(R.id.main_content).setVisibility(View.INVISIBLE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new BoardFragment()).commit();
                        break;
                    case R.id.item_mine:
                        findViewById(R.id.main_frame).setVisibility(View.VISIBLE);
                        findViewById(R.id.main_content).setVisibility(View.INVISIBLE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new MypageFragment()).commit();
                        break;
                }
                return true;
            }

        });

    }


    private void checkPermission() {

        if (checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            initUI();
            initUISDK();
        } else {
            String[] permissionArr = {android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION};
            requestPermissions(permissionArr, 100);
        }
    }
    private void initUISDK() {

        //tmap-sdk-1.2.arr인증부분
        TMapTapi tmaptapi = new TMapTapi(this);
        tmaptapi.setSKTmapAuthentication(API_KEY);
        //tamp-navigation인증부분
        TmapUISDK.Companion.initialize(this, CLIENT_ID, API_KEY, USER_KEY,new TmapUISDK.InitializeListener() {

            @Override
            public void onSuccess() {
                Log.e(TAG, "success initialize");
            }

            @Override
            public void onFail(int i, @Nullable String s) {
                Log.e(TAG, "fail initialize : " + s);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            initUI();
            initUISDK();
        } else {
            Toast.makeText(this, "위치 권한이 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initUI() {


        fragmentManager = getSupportFragmentManager();

        navigationFragment = TmapUISDK.Companion.getFragment();

        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.tmapUILayout, navigationFragment);
        transaction.commitAllowingStateLoss();

        isEDC = false;
        navigationFragment.setDrivingStatusCallback(new TmapUISDK.DrivingStatusCallback() {
            @Override
            public void onStartNavigation() {

            }

            @Override
            public void onStopNavigation() {

            }

            @Override
            public void onPermissionDenied(int i, @Nullable String s) {

            }

            @Override
            public void onArrivedDestination(@NonNull String s, int i, int i1) {

            }

            @Override
            public void onBreakawayFromRouteEvent() {

            }

            @Override
            public void onApproachingAlternativeRoute() {

            }

            @Override
            public void onPassedAlternativeRouteJunction() {

            }

            @Override
            public void onPeriodicReroute() {

            }

            @Override
            public void onRouteChanged(int i) {

            }

            @Override
            public void onForceReroute(@NonNull NddsDataType.DestSearchFlag destSearchFlag) {

            }

            @Override
            public void onNoLocationSignal(boolean b) {

            }

            @Override
            public void onApproachingViaPoint() {

            }

            @Override
            public void onPassedViaPoint() {

            }

            @Override
            public void onChangeRouteOptionComplete(@NonNull RoutePlanType routePlanType) {

            }

            @Override
            public void onBreakAwayRequestComplete() {

            }

            @Override
            public void onPeriodicRerouteComplete() {

            }

            @Override
            public void onUserRerouteComplete() {

            }

            @Override
            public void onDestinationDirResearchComplete() {

            }

            @Override
            public void onDoNotRerouteToDestinationComplete() {

            }

            @Override
            public void onFailRouteRequest(@NonNull String s, @NonNull String s1) {

            }

            @Override
            public void onPassedTollgate(int i) {

            }

            @Override
            public void onLocationChanged() {

            }
        });

    }
    public void searching(View view) {
        Intent intent_searching = new Intent(this, Search.class);
        startActivity(intent_searching);
    }

}