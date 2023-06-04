package com.example.bigmap;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;


import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

//import com.example.bigmap.bottom.BoardFragment;
import com.example.bigmap.bottom.BoardFragment;
import com.example.bigmap.bottom.Bottom_Favorite;
import com.example.bigmap.bottom.Bottom_Home;
import com.example.bigmap.bottom.MypageFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skt.tmap.TMapData;
import com.skt.tmap.TMapTapi;
import com.skt.tmap.TMapView;
import com.skt.tmap.engine.navigation.SDKConstant;
import com.skt.tmap.engine.navigation.SDKLocationInitParam;
import com.skt.tmap.engine.navigation.location.TmapLocationManager;
import com.skt.tmap.engine.navigation.location.TmapLocationService;
import com.skt.tmap.engine.navigation.network.RequestConstant;
import com.skt.tmap.engine.navigation.network.ndds.NddsDataType;
import com.skt.tmap.engine.navigation.route.RoutePlanType;
import com.skt.tmap.engine.navigation.route.data.MapPoint;
import com.skt.tmap.engine.navigation.route.data.WayPoint;
import com.skt.tmap.overlay.TMapLocationLayer;
import com.skt.tmap.poi.TMapPOIItem;
import com.skt.tmap.vsm.coordinates.VSMCoordinates;
import com.tmapmobility.tmap.tmapsdk.ui.fragment.NavigationFragment;
import com.tmapmobility.tmap.tmapsdk.ui.util.TmapSdkPreferences;
import com.tmapmobility.tmap.tmapsdk.ui.util.TmapUISDK;
import com.skt.tmap.engine.navigation.SDKManager;
import com.skt.tmap.engine.navigation.network.ndds.CarOilType;
import com.skt.tmap.engine.navigation.network.ndds.TollCarType;
import com.skt.tmap.engine.navigation.network.ndds.dto.request.TruckType;
import com.skt.tmap.engine.navigation.route.RoutePlanType;
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
    String name_destP;
    String loc_addr;
    Double lat_destP;
    Double lon_destP;
    String name_startP;
    Double lat_startP;
    Double lon_startP;
    int num = 0;

    ImageView imageView;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 제거 코드 입니다.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bottom_LocationInform에서 보낸 위치 정보 가져와서 String & Double 변수에 저장하기
        Intent intent_getiform = getIntent();
        name_destP = intent_getiform.getStringExtra("name_destP");
        lat_destP = intent_getiform.getDoubleExtra("lat_destP", 0);
        lon_destP = intent_getiform.getDoubleExtra("lon_destP", 0);

        name_startP = intent_getiform.getStringExtra("name_startP");
        lat_startP = intent_getiform.getDoubleExtra("lat_startP", 0);
        lon_startP = intent_getiform.getDoubleExtra("lon_startP", 0);

        checkPermission();
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

                //bottom_locationinform에서 받아온 위치 정보 List에 저장하기
                List<Object> poiList_dest = new ArrayList<>();
                poiList_dest.add(name_destP+","+lon_destP+","+lat_destP);
                Log.d(TAG, "위도: "+lat_destP+" 경도: "+lon_destP);

                List<Object> poiList_start = new ArrayList<>();
                poiList_start.add(name_startP+","+lon_startP+","+lat_startP);
                Log.d(TAG, "위도: "+lat_startP+" 경도: "+lon_startP);

                if (lat_startP==0.0 || lon_startP==0.0) {
                    // 도착지만 설정해서 네비게이션 구동(출발지는 사용자 현재위치로 설정)
                    if(name_destP == null ||lat_destP == 0.0 || lon_destP == 0.0){
                        runOnUiThread(() -> {
                            Toast.makeText(MainActivity.this, "비정상적인 길안내입니다 ", Toast.LENGTH_SHORT).show();
                        });
                    }
                    else{
                        // nav_truck 실행
                        runOnUiThread(() -> {
                            truck_info(poiList_dest);
                        });
                    }
                } else {
                    // 출발지 & 도착지 설정해서 네비게이션 구동
                    if(name_destP == null ||lat_destP == 0.0 || lon_destP == 0.0){
                        runOnUiThread(() -> {
                            Toast.makeText(MainActivity.this, "비정상적인 길안내입니다 ", Toast.LENGTH_SHORT).show();
                        });
                    }
                    else {
                        // nav_truck 실행
                        runOnUiThread(() -> {
                            truck_info(poiList_start, poiList_dest);
                        });
                    }
                }

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

    // 도착지만 설정
    public void truck_info(List<Object> poi_search){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        // 사용자 정보에 저장되어 있는 email 갖고오기 (이거는 Authentication에 저장되어 있는 정보만 사용 가능. 그 이외 (예: 전화번호, 실명이름, 생년월일 등)는 사용 불가)
        String email = user.getEmail();

        final String[] height1 = new String[1];
        final String[] weight1 = new String[1];
        final String[] width1 = new String[1];
        final String[] length1 = new String[1];


        DocumentReference docRef2 = firestore.collection("화물차DB").document(email);
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    System.out.println("document:"+document);
                    if (document.exists()) {
                        String height = document.getLong("높이").toString();
                        String weight = document.getLong("무게").toString();
                        String width = document.getLong("너비").toString();
                        String length = document.getLong("길이").toString();
                        height1[0] = height;
                        weight1[0] = weight;
                        width1[0] = width;
                        length1[0] = length;

                        List<String> truckinfo = new ArrayList<>();
                        truckinfo.add(0,height1[0]);
                        truckinfo.add(1,weight1[0]);
                        truckinfo.add(2,width1[0]);
                        truckinfo.add(3,length1[0]);
                        System.out.println(truckinfo);

                        runOnUiThread(() -> {
                            nav_truck(poi_search,truckinfo);
                        });

                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

//
    }

    //출발지 & 도착지 둘 다 설정
    public void truck_info(List<Object> poi_search_start, List<Object> poi_search_dest){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        // 사용자 정보에 저장되어 있는 email 갖고오기 (이거는 Authentication에 저장되어 있는 정보만 사용 가능. 그 이외 (예: 전화번호, 실명이름, 생년월일 등)는 사용 불가)
        String email = user.getEmail();

        final String[] height1 = new String[1];
        final String[] weight1 = new String[1];
        final String[] width1 = new String[1];
        final String[] length1 = new String[1];


        DocumentReference docRef2 = firestore.collection("화물차DB").document(email);
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    System.out.println("document:"+document);
                    if (document.exists()) {
                        String height = document.getLong("높이").toString();
                        String weight = document.getLong("무게").toString();
                        String width = document.getLong("너비").toString();
                        String length = document.getLong("길이").toString();
                        height1[0] = height;
                        weight1[0] = weight;
                        width1[0] = width;
                        length1[0] = length;

                        List<String> truckinfo = new ArrayList<>();
                        truckinfo.add(0,height1[0]);
                        truckinfo.add(1,weight1[0]);
                        truckinfo.add(2,width1[0]);
                        truckinfo.add(3,length1[0]);
                        System.out.println(truckinfo);

                        runOnUiThread(() -> {
                            nav_truck(poi_search_start,poi_search_dest, truckinfo);
                        });

                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

//
    }


    //도착지만 설정
    public void nav_truck(List<Object> poi_search,List<String> truck_info){

        Object search_data = poi_search.get(0);

        ArrayList<Object> search_data_list = new ArrayList<>();
        Collections.addAll(search_data_list, ((String) search_data).split(","));
        System.out.println(search_data_list.get(0));
        System.out.println(search_data_list.get(1));
        System.out.println(search_data_list.get(2));

        double longi,lati;
        longi = Double.parseDouble((String) search_data_list.get(1));
        lati = Double.parseDouble((String) search_data_list.get(2));

        CarOption carOption = new CarOption();
        carOption.setCarType(TollCarType.LargeTruck);
        carOption.setOilType(CarOilType.PremiumGasoline);
        carOption.setHipassOn(true);




        //트럭 경로 요청 하기 위한 추가 정보
        HashMap<String, String> truckDetailInfo = new HashMap<>();

        truckDetailInfo.put(TruckInfoKey.TruckType.getValue(), TruckType.Truck.toString());
        truckDetailInfo.put(TruckInfoKey.TruckHeight.getValue(), truck_info.get(0));     // 단위 cm 화물차 높이
        truckDetailInfo.put(TruckInfoKey.TruckWeight.getValue(), truck_info.get(1));    // 단위 kg 화물의 무게
        truckDetailInfo.put(TruckInfoKey.TruckWidth.getValue(),  truck_info.get(2));     // 단위 cm 화물차 너비
        truckDetailInfo.put(TruckInfoKey.TruckLength.getValue(), truck_info.get(3));    // 단위 cm 화물차 길이

        carOption.setTruckInfo(truckDetailInfo);

        //현재 위치
        Location currentLocation = SDKManager.getInstance().getCurrentPosition();
        String currentName = VSMCoordinates.getAddressOffline(currentLocation.getLongitude(), currentLocation.getLatitude());

        WayPoint startPoint = new WayPoint(currentName, new MapPoint(currentLocation.getLongitude(), currentLocation.getLatitude()));
        System.out.println("start:"+startPoint.getMapPoint().getLatitude()+","+startPoint.getMapPoint().getLongitude());
        //목적지
        WayPoint endPoint = new WayPoint(search_data_list.get(0).toString(), new MapPoint(longi,lati), "", RequestConstant.RpFlagCode.UNKNOWN);
        System.out.println("end:"+endPoint.getMapPoint().getLatitude()+","+endPoint.getMapPoint().getLongitude());
//        네비게이션 화면 구성
        navigationFragment.setCarOption(carOption);

        navigationFragment.setRoutePlanType(RoutePlanType.Traffic_Truck);

        //길안내 코드(시작 지점,null,도착 지점,false or true(false 경로 안내 해줌 true 는 경로 안내 안하고 바로 네비시작),TmapUISDK.RouteRequestListener()
        navigationFragment.requestRoute(startPoint, null, endPoint, false, new TmapUISDK.RouteRequestListener() {
            @Override
            public void onSuccess() {
                Log.e(TAG, "requestRoute Success");
            }

            @Override
            public void onFail(int i, @Nullable String s) {
                Toast.makeText(MainActivity.this, i + "::" + s, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFail " + i + " :: " + s);
                Log.e(TAG, "onFail " + i + " :: " + s);
            }
        });
    }

    //출발지 & 도착지 둘 다 설정
    public void nav_truck(List<Object> poi_search_startP,List<Object> poi_search_destP,List<String> truck_info){

        Object search_data_startP = poi_search_startP.get(0);

        ArrayList<Object> search_data_list_startP = new ArrayList<>();
        Collections.addAll(search_data_list_startP, ((String) search_data_startP).split(","));
        System.out.println(search_data_list_startP.get(0));
        System.out.println(search_data_list_startP.get(1));
        System.out.println(search_data_list_startP.get(2));

        Object search_data_destP = poi_search_destP.get(0);

        ArrayList<Object> search_data_list_destP = new ArrayList<>();
        Collections.addAll(search_data_list_destP, ((String) search_data_destP).split(","));
        System.out.println(search_data_list_destP.get(0));
        System.out.println(search_data_list_destP.get(1));
        System.out.println(search_data_list_destP.get(2));

        double longi_startP,lati_startP;
        longi_startP = Double.parseDouble((String) search_data_list_startP.get(1));
        lati_startP = Double.parseDouble((String) search_data_list_startP.get(2));

        double longi_destP,lati_destP;
        longi_destP = Double.parseDouble((String) search_data_list_destP.get(1));
        lati_destP = Double.parseDouble((String) search_data_list_destP.get(2));

        CarOption carOption = new CarOption();
        carOption.setCarType(TollCarType.LargeTruck);
        carOption.setOilType(CarOilType.PremiumGasoline);
        carOption.setHipassOn(true);




        //트럭 경로 요청 하기 위한 추가 정보
        HashMap<String, String> truckDetailInfo = new HashMap<>();

        truckDetailInfo.put(TruckInfoKey.TruckType.getValue(), TruckType.Truck.toString());
        truckDetailInfo.put(TruckInfoKey.TruckHeight.getValue(), truck_info.get(0));     // 단위 cm 화물차 높이
        truckDetailInfo.put(TruckInfoKey.TruckWeight.getValue(), truck_info.get(1));    // 단위 kg 화물의 무게
        truckDetailInfo.put(TruckInfoKey.TruckWidth.getValue(),  truck_info.get(2));     // 단위 cm 화물차 너비
        truckDetailInfo.put(TruckInfoKey.TruckLength.getValue(), truck_info.get(3));    // 단위 cm 화물차 길이

        carOption.setTruckInfo(truckDetailInfo);

        //현재 위치
        /*Location currentLocation = SDKManager.getInstance().getCurrentPosition();
        String currentName = VSMCoordinates.getAddressOffline(currentLocation.getLongitude(), currentLocation.getLatitude());*/

        WayPoint startPoint = new WayPoint(search_data_list_startP.get(0).toString(), new MapPoint(longi_startP,lati_startP), "", RequestConstant.RpFlagCode.UNKNOWN);
        System.out.println("start:"+startPoint.getMapPoint().getLatitude()+","+startPoint.getMapPoint().getLongitude());
        //목적지
        WayPoint endPoint = new WayPoint(search_data_list_destP.get(0).toString(), new MapPoint(longi_destP,lati_destP), "", RequestConstant.RpFlagCode.UNKNOWN);
        System.out.println("end:"+endPoint.getMapPoint().getLatitude()+","+endPoint.getMapPoint().getLongitude());
//        네비게이션 화면 구성
        navigationFragment.setCarOption(carOption);

        navigationFragment.setRoutePlanType(RoutePlanType.Traffic_Truck);

        //길안내 코드(시작 지점,null,도착 지점,false or true(false 경로 안내 해줌 true 는 경로 안내 안하고 바로 네비시작),TmapUISDK.RouteRequestListener()
        navigationFragment.requestRoute(startPoint, null, endPoint, false, new TmapUISDK.RouteRequestListener() {
            @Override
            public void onSuccess() {
                Log.e(TAG, "requestRoute Success");
            }

            @Override
            public void onFail(int i, @Nullable String s) {
                Toast.makeText(MainActivity.this, i + "::" + s, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFail " + i + " :: " + s);
                Log.e(TAG, "onFail " + i + " :: " + s);
            }
        });
    }

    public void searching(View view) {
        Intent intent_searching = new Intent(MainActivity.this, Search.class);
        startActivity(intent_searching);
    }


}