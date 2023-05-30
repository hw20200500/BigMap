package com.example.bigmap;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.skt.tmap.TMapPoint;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

public class Search extends AppCompatActivity {
    private EditText editTextSearch;
    private NavigationFragment navigationFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    TMapData tmapdata = new TMapData();
    int count = 0;
    int num=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        System.out.println("실행함");

        firebaseAuth = FirebaseAuth.getInstance();

        EditText search_bar = (EditText) findViewById(R.id.edittext_search);
        LinearLayout recent_view = (LinearLayout) findViewById(R.id.recent_view);
        LinearLayout loc_inform_view = (LinearLayout) findViewById(R.id.loc_inform_view);

        editTextSearch = findViewById(R.id.edittext_search);

        // 검색 버튼 또는 엔터 키를 눌렀을 때 동작하도록 설정
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    System.out.println("엔터누름");
                    handled = true;
                }
                return handled;
            }
        });



//        // edittext의 상태 동적 할당 받음 -> edittext에 글자 입력하면 위치 관련 정보 레이아웃이 보이도록함
//        // -> 입력한 글자가 없으면 최근 검색어 레이아웃이 보이도록 함.
//        search_bar.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                String search_text = s.toString();
//                if (search_text.isEmpty()) {
//                    recent_view.setVisibility(View.VISIBLE);
//                    loc_inform_view.setVisibility(View.GONE);
//                } else {
//                    recent_view.setVisibility(View.GONE);
//                    loc_inform_view.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                String search_text = s.toString();
//                if (search_text.isEmpty()) {
//                    recent_view.setVisibility(View.VISIBLE);
//                    loc_inform_view.setVisibility(View.GONE);
//                } else {
//                    recent_view.setVisibility(View.GONE);
//                    loc_inform_view.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String search_text = s.toString();
//                if (search_text.isEmpty()) {
//                    recent_view.setVisibility(View.VISIBLE);
//                    loc_inform_view.setVisibility(View.GONE);
//                } else {
//                    recent_view.setVisibility(View.GONE);
//                    loc_inform_view.setVisibility(View.VISIBLE);
//                }
//            }
//        });
    }



    private void poisearch(String searchText){
        TMapData tmapdata = new TMapData();
        tmapdata.findAllPOI(searchText, 5, new TMapData.OnFindAllPOIListener() {
            @Override
            public void onFindAllPOI(ArrayList<TMapPOIItem> poiitems) {
                for(int i = 0;i<poiitems.size();i++){
                    TMapPOIItem poiItem = poiitems.get(i);
                    System.out.println(i+":"+poiItem.getPOIName());
                }

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

            } else {
                runOnUiThread(() -> Toast.makeText(Search.this, "검색 결과가 없습니다.", Toast.LENGTH_LONG).show());
            }

        });
    }


    public void delete_text() {
        // 최근 검색어 레이아웃의 'X' 이미지 눌렀을 때, 내용물 삭제되는 함수 코드 작성
    }

    public void go_location() {
//        List<Object> objects = new ArrayList<>();
    }

}