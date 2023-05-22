package com.example.bigmap;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bigmap.bottom.BoardFragment;
import com.example.bigmap.bottom.Bottom_Favorite;
import com.example.bigmap.bottom.Bottom_Home;
import com.example.bigmap.bottom.Bottom_LocationInform;
import com.example.bigmap.bottom.MypageFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.skt.tmap.TMapData;
import com.skt.tmap.TMapGpsManager;
import com.skt.tmap.TMapPoint;
import com.skt.tmap.TMapTapi;
import com.skt.tmap.TMapView;
import com.skt.tmap.overlay.TMapMarkerItem;
import com.skt.tmap.poi.TMapPOIItem;
import com.tmapmobility.tmap.tmapsdk.ui.util.TmapUISDK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class mapview extends AppCompatActivity
        implements LocationListener {

    private static final String TAG = "Big_Map";
    private final static String CLIENT_ID = "";
    private final static String API_KEY = BuildConfig.Api_key;
    private final static String USER_KEY = "";
    boolean isEDC;
    private LocationManager locationManager;
    TMapView tMapView;
    double latitude;
    double longitude;
    private Timer timer;
    private Handler handler;
    private long startTime;
    FrameLayout tmaplayout;
    String poiName_loc;
    String poiAddress_loc;
    int num = 0;
    int num_loc_layout = 0;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 제거 코드 입니다.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapview);

//        checkPermission();

        tMapView = new TMapView(mapview.this);
        tmaplayout = findViewById(R.id.tmap_layout);
        tmaplayout.addView(tMapView);
        tMapView.setSKTMapApiKey(API_KEY);



        // 위치 관리자(LocationManager) 초기화
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        // 위치 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없으면 권한 요청
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {
            // 권한이 있으면 위치 정보 요청
            requestLocationUpdates();
        }

        ImageView location_bttn = findViewById(R.id.locationImage);
        location_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num = 0;
                requestLocationUpdates();
            }
        });

        /*tmapgps.setProvider(tmapgps.PROVIDER_GPS); //gps로 현 위치를 잡습니다.
        tmapgps.openGps();

          화면중심을 단말의 현재위치로 이동 */
//        tMapView.setTrackingMode(true);
//        tMapView.setSightVisible(true);
        // 핸들러 초기화


        // 클릭 이벤트 설정
        /*tMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 클릭한 위치의 위도, 경도 가져오기
                TMapPoint tMapPoint = tMapView.getLocationPoint();
                double latitude = tMapPoint.getLatitude();
                double longitude = tMapPoint.getLongitude();

                // 위도, 경도 출력
                Toast.makeText(mapview.this, "위도: " + latitude + ", 경도: " + longitude, Toast.LENGTH_SHORT).show();
            }
        });*/

        FrameLayout bottomSheet = findViewById(R.id.main_content);
        int screenHeight = getResources().getDisplayMetrics().heightPixels;


        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(getResources().getDimensionPixelSize(R.dimen.main_layout_height));
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                int bottomSheetHeight = 400;
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetHeight = (int) (bottomSheet.getHeight())-180;
                }
                int mainLayoutHeight = screenHeight - bottomSheetHeight;
                tmaplayout.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mainLayoutHeight));
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                int bottomSheetHeight = (int) (bottomSheet.getHeight() * slideOffset);
                int mainLayoutHeight = screenHeight - bottomSheetHeight;
                tmaplayout.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mainLayoutHeight));
            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavi);
        getSupportFragmentManager().beginTransaction().add(R.id.main_content, new Bottom_Home()).commit();
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



        handler = new Handler(Looper.getMainLooper());



        tMapView.setOnClickListenerCallback(new TMapView.OnClickListenerCallback() {
            @Override
            public void onPressDown(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {


                // 터치 시작 시 시간 기록
                startTime = System.currentTimeMillis();

                // 타이머 시작
                startTimer();

            }

            @Override
            public void onPressUp(ArrayList<TMapMarkerItem> arrayList3, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {

                // 타이머 종료
                stopTimer();

                // 시간 측정
                long elapsedTime = System.currentTimeMillis() - startTime;

                if (elapsedTime < 100) {
                    final double[] latitude_loc = {tMapPoint.getLatitude()};
                    final double[] longitude_loc = {tMapPoint.getLongitude()};


//                    TMapPoint tMapPoint_location = new TMapPoint(latitude, longitude);
//                    Toast.makeText(mapview.this, "위도: " + latitude_loc + ", 경도: " + longitude_loc, Toast.LENGTH_SHORT).show();

                    TMapData tMapData = new TMapData();
                    // TMapData에서 해당 위치와 가장 가까운 장소 검색

                    tMapData.convertGpsToAddress(latitude_loc[0], longitude_loc[0], new TMapData.OnConvertGPSToAddressListener() {
                        @Override
                        public void onConverGPSToAddress(String s) {
                            String address = s;
                            Log.d(TAG, "주소 : "+s);

//                            Toast.makeText(mapview.this, "주소 : "+s, Toast.LENGTH_SHORT).show();

                            tMapData.findAllPOI(s, new TMapData.OnFindAllPOIListener() {
                                @Override
                                public void onFindAllPOI(ArrayList<TMapPOIItem> poiItems) {
                                    if(poiItems!=null) {
                                        TMapPOIItem tMapPOIItem = poiItems.get(0);

                                        poiName_loc = tMapPOIItem.getPOIName();
                                        poiAddress_loc = s;
                                        latitude = tMapPOIItem.getPOIPoint().getLatitude();
                                        longitude = tMapPOIItem.getPOIPoint().getLongitude();



                                        Log.d(TAG, "장소명: "+poiName_loc+" /장소: "+poiAddress_loc+" /위도: "+latitude+" /경도: "+longitude);

                                        HashMap <Object, Object> poiList = new HashMap<>();
                                        poiList.put("loc_name", poiName_loc);
                                        poiList.put("loc_addr", poiAddress_loc);
                                        poiList.put("loc_lat", latitude);
                                        poiList.put("loc_lon", longitude);
                                    } else {
                                        findViewById(R.id.loc_layout).setVisibility(View.GONE);
                                        bottomNavigationView.setVisibility(View.VISIBLE);
                                        findViewById(R.id.main_content).setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        }
                    });


                    FrameLayout loc_layout = findViewById(R.id.loc_layout);
                    findViewById(R.id.loc_layout).setVisibility(View.VISIBLE);
                    int screenHeight = getResources().getDisplayMetrics().heightPixels;

                    BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(loc_layout);
                    bottomSheetBehavior.setPeekHeight(getResources().getDimensionPixelSize(R.dimen.main_layout_height));
                    bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                        @Override
                        public void onStateChanged(@NonNull View bottomSheet, int newState) {

                            int bottomSheetHeight = 400;
                            if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                                bottomSheetHeight = (int) (bottomSheet.getHeight()) - 150;
                            }
                            int mainLayoutHeight = screenHeight - bottomSheetHeight;
                            tmaplayout.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mainLayoutHeight));
                        }

                        @Override
                        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                            int bottomSheetHeight = (int) (bottomSheet.getHeight() * slideOffset);
                            int mainLayoutHeight = screenHeight - bottomSheetHeight;
                            tmaplayout.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mainLayoutHeight));
                        }
                    });
                    findViewById(R.id.loc_layout).setVisibility(View.VISIBLE);

                    bundle = new Bundle();
                    bundle.putString("loc_name", poiName_loc);
                    bundle.putString("loc_addr", poiAddress_loc);
                    bundle.putDouble("loc_lat", latitude);
                    bundle.putDouble("loc_lon", longitude);

                    Bottom_LocationInform bottom_locationInform = new Bottom_LocationInform();
                    bottom_locationInform.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.loc_layout, bottom_locationInform).commit();

                    bottomNavigationView.setVisibility(View.GONE);
                    findViewById(R.id.main_content).setVisibility(View.GONE);





                    // 위도, 경도 출력
//                            Toast.makeText(mapview.this, "위도: " + latitude + ", 경도: " + longitude, Toast.LENGTH_SHORT).show();
                } else {
                    findViewById(R.id.loc_layout).setVisibility(View.GONE);
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    findViewById(R.id.main_content).setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void startTimer() {
        if (timer == null) {
            timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    // 타이머 동작
                }
            };
            timer.schedule(timerTask, 0, 100);
        }
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void requestLocationUpdates() {
        // 위치 정보 업데이트 요청
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    0, 0, this);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                0, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

//        Toast.makeText(this, "위치: "+latitude + ", " + longitude, Toast.LENGTH_SHORT).show();

        // 현재 위치로 지도 중심 설정

        if(num==0) {
            tMapView.setCenterPoint(latitude, longitude);
            tMapView.setZoomLevel(15);
            num++;
        }


    }

    @Override
    public void onProviderDisabled(String provider) {
        // 위치 공급자가 비활성화된 경우
        Toast.makeText(this, "위치 공급자가 비활성화되었습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        // 위치 공급자가 활성화된 경우
        Toast.makeText(this, "위치 공급자가 활성화되었습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // 위치 공급자의 상태가 변경된 경우
    }

    // 권한 요청 결과 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 위치 권한이 허용된 경우
                requestLocationUpdates();
            } else {
                // 위치 권한이 거부된 경우
                Toast.makeText(this, "위치 권한이 거부되어 앱을 사용할 수 없습니다.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 위치 업데이트 중지
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 위치 업데이트 요청
        requestLocationUpdates();
    }

    private TMapGpsManager.OnLocationChangedListener locationListener = new TMapGpsManager.OnLocationChangedListener() {
        @Override
        public void onLocationChange(Location location) {
            if (location != null) {
                tMapView.setLocationPoint(location.getLatitude(), location.getLongitude());
            }
        }
    };

    private class FindAroundNamePOIListenerCallback {
    }

    public void searching(View view) {
        Intent intent_searching = new Intent(mapview.this, Search.class);
        startActivity(intent_searching);
    }


    /*@Override
    public void onLocationChange(Location location) {
        if (m_bTrackingMode) {
            tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        }
    }*/
}