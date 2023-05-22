package com.example.bigmap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.bigmap.bottom.Bottom_LocationInform;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.skt.tmap.TMapData;
import com.skt.tmap.TMapGpsManager;
import com.skt.tmap.TMapPoint;
import com.skt.tmap.TMapView;
import com.skt.tmap.overlay.TMapMarkerItem;
import com.skt.tmap.poi.TMapPOIItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class mapview_clone extends AppCompatActivity{

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
    public static Context context;

    int num = 0;
    int num_loc_layout = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapview_clone);
        FrameLayout loc_layout = findViewById(R.id.loc_layout);
//        findViewById(R.id.loc_layout).setVisibility(View.VISIBLE);
//        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        /*BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(loc_layout);
        BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetDialogFragment();
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
        });*/
//                findViewById(R.id.loc_layout).setVisibility(View.VISIBLE);

        Intent intent_getIform = getIntent();

        poiName_loc = intent_getIform.getStringExtra("loc_name");
        poiAddress_loc = intent_getIform.getStringExtra("loc_addr");
        latitude = intent_getIform.getDoubleExtra("loc_lat",0);
        longitude = intent_getIform.getDoubleExtra("loc_lon", 0);

        Log.d(TAG, "위도, 경도: "+latitude+" | "+longitude);

//        centerPoint(latitude, longitude);


        Bundle bundle = new Bundle();
        bundle.putString("loc_name", poiName_loc);
        bundle.putString("loc_addr", poiAddress_loc);
        bundle.putDouble("loc_lat", latitude);
        bundle.putDouble("loc_lon", longitude);

        Bottom_LocationInform bottom_locationInform = new Bottom_LocationInform();
        bottom_locationInform.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.loc_layout, bottom_locationInform).commit();



        // 위치 관리자(LocationManager) 초기화
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);








        /*tMapView.setOnClickListenerCallback(new TMapView.OnClickListenerCallback() {
            @Override
            public void onPressDown(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                tmapdata.findAllPOI("한남대학교", new TMapData.OnFindAllPOIListener() {
                    @Override
                    public void onFindAllPOI(ArrayList<TMapPOIItem> poiItems) {
                        if(poiItems!=null) {
                            TMapPOIItem tMapPOIItem = poiItems.get(0);

                            String poiName_loc = tMapPOIItem.getPOIName();
                            poiAddress_loc = tMapPOIItem.getPOIAddress();
                            latitude = tMapPOIItem.getPOIPoint().getLatitude();
                            longitude = tMapPOIItem.getPOIPoint().getLongitude();


                            Log.d(TAG, "장소명: "+poiName_loc+" /장소: "+poiAddress_loc);

                            HashMap <Object, Object> poiList = new HashMap<>();
                            poiList.put("loc_name", poiName_loc);
                            poiList.put("loc_addr", poiAddress_loc);
                            poiList.put("loc_lat", latitude);
                            poiList.put("loc_lon", longitude);

                            tMapView.setCenterPoint(latitude, longitude);
                            tMapView.setZoomLevel(15);

                            Bundle bundle = new Bundle();
                            bundle.putString("loc_name", poiName_loc);
                            bundle.putString("loc_addr", poiAddress_loc);
                            bundle.putDouble("loc_lat", latitude);
                            bundle.putDouble("loc_lon", longitude);



                            Bottom_LocationInform bottom_locationInform = new Bottom_LocationInform();
                            bottom_locationInform.setArguments(bundle);
                            getSupportFragmentManager().beginTransaction().replace(R.id.loc_layout, bottom_locationInform).commit();





                        } else {
                            Log.d(TAG, "장소 없소");
                        }
                    }
                });
            }

            @Override
            public void onPressUp(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {

            }
        });*/




        /*tMapView.setOnClickListenerCallback(new TMapView.OnClickListenerCallback() {
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
                    tMapView.getPointerIcon();

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


                                        Log.d(TAG, "장소명: "+poiName_loc+" /장소: "+poiAddress_loc);

                                        HashMap <Object, Object> poiList = new HashMap<>();
                                        poiList.put("loc_name", poiName_loc);
                                        poiList.put("loc_addr", poiAddress_loc);
                                        poiList.put("loc_lat", latitude_loc);
                                        poiList.put("loc_lon", longitude_loc);



                                    } else {
                                        findViewById(R.id.loc_layout).setVisibility(View.GONE);
//                                        bottomNavigationView.setVisibility(View.VISIBLE);
//                                        findViewById(R.id.main_content).setVisibility(View.VISIBLE);
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
                    getSupportFragmentManager().beginTransaction().replace(R.id.loc_layout, new Bottom_LocationInform()).commit();

                    *//*bottomNavigationView.setVisibility(View.GONE);
                    findViewById(R.id.main_content).setVisibility(View.GONE);*//*





                    // 위도, 경도 출력
//                            Toast.makeText(mapview.this, "위도: " + latitude + ", 경도: " + longitude, Toast.LENGTH_SHORT).show();
                } else {
                    findViewById(R.id.loc_layout).setVisibility(View.GONE);
                    *//*bottomNavigationView.setVisibility(View.VISIBLE);
                    findViewById(R.id.main_content).setVisibility(View.VISIBLE);*//*
                }
            }
        });*/
    }

    private void centerPoint(Double lat, Double lon) {
        tMapView.setCenterPoint(lat, lon);
        tMapView.setZoomLevel(15);
    }

    /*private void startTimer() {
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
    }*/


    /*@Override
    public void onLocationChange(Location location) {
        if (m_bTrackingMode) {
            tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        }
    }*/
}