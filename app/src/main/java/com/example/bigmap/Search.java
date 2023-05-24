package com.example.bigmap;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.checkerframework.checker.units.qual.Length;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.skt.tmap.TMapData;
import com.skt.tmap.engine.navigation.SDKManager;
import com.skt.tmap.engine.navigation.network.RequestConstant;
import com.skt.tmap.engine.navigation.network.ndds.CarOilType;
import com.skt.tmap.engine.navigation.network.ndds.TollCarType;
import com.skt.tmap.engine.navigation.network.ndds.dto.request.TruckType;
import com.skt.tmap.engine.navigation.route.RoutePlanType;
import com.skt.tmap.engine.navigation.route.data.MapPoint;
import com.skt.tmap.engine.navigation.route.data.WayPoint;
import com.skt.tmap.poi.TMapPOIItem;
import com.skt.tmap.vsm.coordinates.VSMCoordinates;
import com.tmapmobility.tmap.tmapsdk.ui.data.CarOption;
import com.tmapmobility.tmap.tmapsdk.ui.data.TruckInfoKey;
import com.tmapmobility.tmap.tmapsdk.ui.fragment.NavigationFragment;
import com.tmapmobility.tmap.tmapsdk.ui.util.TmapUISDK;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Search extends AppCompatActivity {
    private EditText editTextSearch;
    private NavigationFragment navigationFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    int num=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        textsearch();

        firebaseAuth = FirebaseAuth.getInstance();

        EditText search_bar = (EditText) findViewById(R.id.edittext_search);
        LinearLayout recent_view = (LinearLayout) findViewById(R.id.recent_view);
        LinearLayout loc_inform_view = (LinearLayout) findViewById(R.id.loc_inform_view);

        // edittext의 상태 동적 할당 받음 -> edittext에 글자 입력하면 위치 관련 정보 레이아웃이 보이도록함
        // -> 입력한 글자가 없으면 최근 검색어 레이아웃이 보이도록 함.
        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String search_text = s.toString();
                if (search_text.isEmpty()) {
                    recent_view.setVisibility(View.VISIBLE);
                    loc_inform_view.setVisibility(View.GONE);
                }
                else {
                    recent_view.setVisibility(View.GONE);
                    loc_inform_view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String search_text = s.toString();
                if (search_text.isEmpty()) {
                    recent_view.setVisibility(View.VISIBLE);
                    loc_inform_view.setVisibility(View.GONE);
                }
                else {
                    recent_view.setVisibility(View.GONE);
                    loc_inform_view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String search_text = s.toString();
                if (search_text.isEmpty()) {
                    recent_view.setVisibility(View.VISIBLE);
                    loc_inform_view.setVisibility(View.GONE);
                }
                else {
                    recent_view.setVisibility(View.GONE);
                    loc_inform_view.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    private void textsearch(){
        // EditText를 ID로 찾아와 변수에 할당
        editTextSearch = findViewById(R.id.edittext_search);

        // 검색 버튼 또는 엔터 키를 눌렀을 때 동작하도록 설정
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    String searchText = editTextSearch.getText().toString();
                    performSearch(searchText);
                    return true;
                }
                return false;
            }
        });
    }
    private void performSearch(String searchText) {
        TMapData tmapdata = new TMapData();
        List<Object> poiList = new ArrayList<>();
        tmapdata.findAllPOI(searchText, poiItemList -> {
            if (poiItemList != null && !poiItemList.isEmpty()) {
                TMapPOIItem firstItem = poiItemList.get(0);

                String poiName = firstItem.getPOIName();
                String poiAddress = firstItem.getPOIAddress();
                double latitude = firstItem.getPOIPoint().getLatitude();
                double longitude = firstItem.getPOIPoint().getLongitude();
                poiList.add(poiName);
                poiList.add(poiAddress);
                poiList.add(latitude);
                poiList.add(longitude);

                // TextView에 값을 설정
                //데이터베이스 연동후 데이터 추가로 구성하게됩니다.
                runOnUiThread(() -> {
                    // 파이어베이스 저장
                    firestore = FirebaseFirestore.getInstance();
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    String email = user.getEmail();

                    DocumentReference docR = firestore.collection("최근기록DB").document(email);
                    TextView textViewSearch1 = findViewById(R.id.recent_search_text1);
                    String addressText = poiName;
                    String db_address = poiAddress;
                    double db_latitude = latitude;
                    double db_longitude = longitude;

                    String str_num = String.valueOf(num);


                    HashMap<Object,Object> hashMap = new HashMap<>();

                    hashMap.put("location_name",addressText);
                    hashMap.put("address",db_address);
                    hashMap.put("latitude",db_latitude);
                    hashMap.put("longitude",db_longitude);


                    docR.collection("최근기록").document(str_num).set(hashMap);
                    textViewSearch1.setText(addressText);

                    num++;
                });


                // 길 안내 시작
//                runOnUiThread(() -> {
//                    nav_truck(poiList);
//                });
            } else {
                runOnUiThread(() -> Toast.makeText(Search.this, "검색 결과가 없습니다.", Toast.LENGTH_LONG).show());
            }


        });
    }


    public void delete_text() {
        // 최근 검색어 레이아웃의 'X' 이미지 눌렀을 때, 내용물 삭제되는 함수 코드 작성
    }

    public void go_location() {
        List<Object> objects = new ArrayList<>();
    }
    public void nav_truck(Object poi_search){

        ArrayList<Object> search_data_list = new ArrayList<>();
        Collections.addAll(search_data_list, ((String) poi_search).split(","));
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
        truckDetailInfo.put(TruckInfoKey.TruckWeight.getValue(), "2500.0");    // 단위 kg 화물의 무게
        truckDetailInfo.put(TruckInfoKey.TruckHeight.getValue(), "420.0");     // 단위 cm 화물차 높이
        truckDetailInfo.put(TruckInfoKey.TruckWidth.getValue(), "250.0");      // 단위 cm 화물차 너비
        truckDetailInfo.put(TruckInfoKey.TruckLength.getValue(), "1200.0");    // 단위 cm 화물차 길이

        carOption.setTruckInfo(truckDetailInfo);

        //현재 위치
        Location currentLocation = SDKManager.getInstance().getCurrentPosition();
        String currentName = VSMCoordinates.getAddressOffline(currentLocation.getLongitude(), currentLocation.getLatitude());

        WayPoint startPoint = new WayPoint(currentName, new MapPoint(currentLocation.getLongitude(), currentLocation.getLatitude()));

        //목적지
        WayPoint endPoint = new WayPoint(search_data_list.get(0).toString(), new MapPoint(longi,lati), "", RequestConstant.RpFlagCode.UNKNOWN);

        //네비게이션 화면 구성
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
                Toast.makeText(Search.this, i + "::" + s, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFail " + i + " :: " + s);
                Log.e(TAG, "onFail " + i + " :: " + s);
            }
        });
    }
//    //poi 관련 데이터 (지우지 마세요)
//    public void onClick1(View view){
//        TMapData tmapdata = new TMapData();
//        String strData = "천안시청";  //위치 변경할 주소 (검색창에서 여기로 데이터로 전송하면됨)
//
//        tmapdata.findAllPOI(strData, poiItemList -> {
//            List<Object> poiList = new ArrayList<>();
//
//            for (TMapPOIItem item : poiItemList) {
//                Log.d("Poi Item", "name:" + item.getPOIName() + " address:" + item.getPOIAddress()+
//                        " 위도:" + item.getPOIPoint().getLatitude()+", 경도:"+item.getPOIPoint().getLongitude()/*+" 거리:"+item.getDistance(tMapPoint)*/
//                );
//                poiList.add(item.getPOIName()+","+item.getPOIPoint().getLongitude()+","+item.getPOIPoint().getLatitude());
//            }
//            // selectDataList에 데이터 추가
//            runOnUiThread(() -> {
//                nav_truck(poiList);
//            });
//        });
//    }
}