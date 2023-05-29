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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.skt.tmap.engine.navigation.SDKManager;
import com.skt.tmap.overlay.TMapLayer;
import com.skt.tmap.overlay.TMapMarkerItem;
import com.skt.tmap.poi.TMapPOIItem;
import com.tmapmobility.tmap.tmapsdk.ui.util.TmapUISDK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.IntStream;

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
    double loc_latitude;//터치위치 gps(위,경도)
    double loc_longitude;
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

        tMapView = new TMapView(mapview.this);
        tmaplayout = findViewById(R.id.tmap_layout);
        tmaplayout.addView(tMapView);
        tMapView.setSKTMapApiKey(API_KEY);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavi);


        // tmap 위치 관리자(LocationManager) 초기화
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


        // '내 위치' 버튼, 클릭시 사용자의 위치 정보를 받아와서 해당 위치로 지도의 중심지 이동하기 (몇번 실행했으나 될 때도 있고, 안될 때도 있음. 수정 필요)
        ImageView location_bttn = findViewById(R.id.locationImage);
        location_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num = 0;
                tMapView.setCenterPoint(latitude, longitude);
            }
        });

        initnav();

        handler = new Handler(Looper.getMainLooper());

        // 사용자가 지도의 특정 위치 클릭시 해당 위치의 정보(poi data) 가져와서 저장하고,
        // 해당 정보를 Bottom_LocationInform으로 보내서 ottom_LocationInform에서 해당 정보를 보여줄 수 있도록 하는 코드
        // 'click'이 아닌, 'pressdown & up'으로 구성되어있기에 지도 이동, 확대 등의 제스쳐와 차이점을 주기 위해서 타이머 사용. 화면에 손가락이 닿았다가 때었을 때 0.1초 이내일 경우에만 위치 정보가 반응하도록 설정.
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

                    // 터치한 화면의 장소(tMapPoint)를 위도, 경도를 변환하기
                    final double[] latitude_loc = {tMapPoint.getLatitude()};
                    final double[] longitude_loc = {tMapPoint.getLongitude()};

                    TMapData tMapData = new TMapData();

                    // 위도, 경도로 주소 찾기
                    tMapData.convertGpsToAddress(latitude_loc[0], longitude_loc[0], new TMapData.OnConvertGPSToAddressListener() {
                        @Override
                        public void onConverGPSToAddress(String s) {
                            String address = s;
                            Log.d(TAG, "주소 : "+s);

                            tMapData.findAllPOI(s, new TMapData.OnFindAllPOIListener() {
                                @Override
                                public void onFindAllPOI(ArrayList<TMapPOIItem> poiItems) {
                                    if(poiItems!=null) {

                                        // 찾은 주소 및 장소 정보 저장하기
                                        TMapPOIItem tMapPOIItem = poiItems.get(0);

                                        poiName_loc = tMapPOIItem.getPOIName();
                                        poiAddress_loc = s;
                                        loc_latitude = tMapPOIItem.getPOIPoint().getLatitude();
                                        loc_longitude = tMapPOIItem.getPOIPoint().getLongitude();

                                        Log.d(TAG, "장소명: "+poiName_loc+" /장소: "+poiAddress_loc+" /위도: "+loc_latitude+" /경도: "+loc_longitude);

                                        HashMap <Object, Object> poiList = new HashMap<>();
                                        poiList.put("loc_name", poiName_loc);
                                        poiList.put("loc_addr", poiAddress_loc);
                                        poiList.put("loc_lat", loc_latitude);
                                        poiList.put("loc_lon", loc_longitude);
                                    } else {
                                        findViewById(R.id.loc_layout).setVisibility(View.GONE);
                                        bottomNavigationView.setVisibility(View.VISIBLE);
                                        findViewById(R.id.main_content).setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        }
                    });


                    // 위치 정보 bottom_sheet(Bottom_LocationInform) 관련 코드 (위의 홈 fragment 코드와 유사)
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
                    bundle.putDouble("loc_lat", loc_latitude);
                    bundle.putDouble("loc_lon", loc_longitude);

                    Bottom_LocationInform bottom_locationInform = new Bottom_LocationInform();
                    bottom_locationInform.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.loc_layout, bottom_locationInform).commit();

                    bottomNavigationView.setVisibility(View.GONE);
                    findViewById(R.id.main_content).setVisibility(View.GONE);


                } else {
                    findViewById(R.id.loc_layout).setVisibility(View.GONE);
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    findViewById(R.id.main_content).setVisibility(View.VISIBLE);
                }
            }
        });
    }

    // 홈 화면 fragment 관련 코드(이전 MainActivity와 동일)
    private void initnav(){
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

    }

    // 타이머 함수
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

    // 사용자 위치 받아올 때 사용되는 함수들
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

        if(num==0) {
            // 현재 위치로 지도 중심 설정
            tMapView.setCenterPoint(latitude, longitude);
            tMapView.setZoomLevel(15);
            num++;
        }
        // 핑(마커) 추가
        if(tMapView.getMarkerItemFromId("현재위치") != null){
            tMapView.removeTMapMarkerItem("현재위치");
        }

        TMapMarkerItem markerItem = new TMapMarkerItem();
        TMapPoint point = new TMapPoint(latitude, longitude);
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.main_gps);
        markerItem.setTMapPoint(point);
        markerItem.setId("현재위치");
        markerItem.setIcon(icon);

        tMapView.addTMapMarkerItem(markerItem);

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
    public void onClick1(View v){
        findpoi("주유소");
    }
    public void onClick2(View v){
        findpoi("주차장");
    }
    public void onClick3(View v){
        findpoi("카페");
    }

    public void onClick4(View v){
        Intent intent = new Intent(mapview.this,Test.class);
        startActivity(intent);
    }

    private void deletepoint(TMapMarkerItem markerItem){
        tMapView.removeTMapMarkerItem(markerItem.getId());
    }

    private TMapPoint getCurrentLocation() {
        return new TMapPoint(latitude, longitude);
    }
    private void addMapMarker(TMapPoint point, String title, Bitmap icon) {
        TMapMarkerItem markerItem = new TMapMarkerItem();
        markerItem.setTMapPoint(point);
        markerItem.setId(title);
        markerItem.setIcon(icon);
        if (tMapView.getMarkerItemFromId(markerItem.getId()) != null) {
            deletepoint(markerItem);
        }else{
            tMapView.addTMapMarkerItem(markerItem);
        }

    }

    private void findpoi(String data){
        // 현재 위치를 가져오는 메서드를 호출하여 현재 위치를 얻습니다.
        TMapPoint currentLocation = getCurrentLocation();
        // TMapData 객체를 생성합니다.
        TMapData tMapData = new TMapData();

        tMapData.findAroundNamePOI(currentLocation,data, 3, 50, new TMapData.OnFindAroundNamePOIListener() {
            @Override
            public void onFindAroundNamePOI(ArrayList<TMapPOIItem> poiItems) {
                for (TMapPOIItem poiItem : poiItems) {
                    TMapPoint point = poiItem.getPOIPoint();
                    String title = poiItem.getPOIName();
                    Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.poi);
                    addMapMarker(point, title, icon);
                }
            }
        });
    }

    // 검색창 함수
    public void searching(View view) {
        Intent intent_searching = new Intent(mapview.this, Search.class);
        startActivity(intent_searching);
    }




}