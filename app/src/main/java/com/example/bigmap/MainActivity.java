package com.example.bigmap;

import com.example.bigmap.BuildConfig;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tmapmobility.tmap.tmapsdk.ui.fragment.NavigationFragment;
import com.tmapmobility.tmap.tmapsdk.ui.util.TmapUISDK;

public class MainActivity extends AppCompatActivity {



    private NavigationFragment navigationFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    private final static String apikey = BuildConfig.Api_key;

    private static final String TAG = "Big_Map";
    private final static String CLIENT_ID = "";
    private final static String API_KEY = apikey;
    private final static String USER_KEY = "";
    boolean isEDC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 제거 코드 입니다.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        initnav_var();

    }
     public void onClick1(View view){
         navigationFragment.startSafeDrive();

     }


        private void initnav_var(){
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


//    tmap Navi SDK 함수 모음 (이전에는 가상 디바이스에서 구현이 되다가 갑자기 다운되서 안되네요.. 그래서 일단 비활성화 해놨습니다.)

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

        //isEDC = false;


        //네비게이션 상태 변경 시 callback
//        navigationFragment.setDrivingStatusCallback(new TmapUISDK.DrivingStatusCallback() {
//
//            @Override
//            public void onUserRerouteComplete() {
//                // 사용자 재탐색 동작 완료 시 호출
//            }
//
//            @Override
//            public void onStopNavigation() {
//                // 네비게이션 종료 시 호출
////                buttonLayout.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onStartNavigation() {
//                Log.e(TAG, "onStartNavigation");
//                // 네비게이션 시작 시 호출
//            }
//
//            @Override
//            public void onRouteChanged(int i) {
//                // 경로 변경 완료 시 호출
//            }
//
//            @Override
//            public void onPermissionDenied(int i, @Nullable String s) {
//                // 권한 에러 발생 시 호출
//            }
//
//            @Override
//            public void onPeriodicRerouteComplete() {
//                // 정주기 재탐색 동작 완료 시 호출
//            }
//
//            @Override
//            public void onPeriodicReroute() {
//                // 정주기 재탐색 발생 시점에 호출
//            }
//
//            @Override
//            public void onPassedViaPoint() {
//                // 경유지 통과 시 호출
//            }
//
//            @Override
//            public void onPassedTollgate(int i) {
//                // 톨게이트 통과 시 호출
//                // i 요금
//            }
//
//            @Override
//            public void onPassedAlternativeRouteJunction() {
//                // 대안 경로 통과 시 호출
//            }
//
//            @Override
//            public void onNoLocationSignal(boolean b) {
//                // GPS 상태 변화 시점에 호출
//
//            }
//
//            @Override
//            public void onLocationChanged() {
//                //위치 갱신 때 마다 호출
//            }
//
//            @Override
//            public void onFailRouteRequest(@NonNull String errorCode, @NonNull String errorMsg) {
//                //경로 탐색 실패 시 호출
//            }
//
//            @Override
//            public void onDoNotRerouteToDestinationComplete() {
//                //미리 종료 안내 동작 탐색 완료 시점에 호출
//            }
//
//            @Override
//            public void onDestinationDirResearchComplete() {
//                // 건너편 안내 동작 탐색 완료 시점에 호출
//            }
//
//            @Override
//            public void onChangeRouteOptionComplete(@NonNull RoutePlanType routePlanType) {
//                // 경로 옵션 변경 완료 시 호출
//            }
//
//            @Override
//            public void onBreakawayFromRouteEvent() {
//                // 경로 이탈 재탐색 발생 시점에 호출
//                Log.e(TAG, "onBreakawayFromRouteEvent");
//            }
//
//            @Override
//            public void onBreakAwayRequestComplete() {
//                //경로 이탈 재탐색 동작 완료 시점에 호출
//                Log.e(TAG, "onBreakAwayRequestComplete");
//            }
//
//            @Override
//            public void onArrivedDestination(@NonNull String dest, int drivingTime, int drivingDistance) {
//                // 목적지 도착 시 호출
//                // dest 목적지 명
//                // drivingTime 운전시간
//                // drivingDistance 운전거리
//            }
//
//            @Override
//            public void onApproachingViaPoint() {
//                // 경유지 접근 시점에 호출 (1km 이내)
//            }
//
//            @Override
//            public void onApproachingAlternativeRoute() {
//                // 대안 경로 접근 시 호출
//            }
//
//            @Override
//            public void onForceReroute(@NonNull com.skt.tmap.engine.navigation.network.ndds.NddsDataType.DestSearchFlag destSearchFlag)  {
//                // 경로 재탐색 발생 시점에 호출
//            }
//        });


    }
}