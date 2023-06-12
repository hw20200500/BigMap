package com.example.bigmap;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.skt.tmap.TMapData;
import com.skt.tmap.TMapPoint;
import com.skt.tmap.TMapView;
import com.skt.tmap.engine.navigation.SDKManager;
import com.skt.tmap.engine.navigation.network.RequestConstant;
import com.skt.tmap.engine.navigation.network.ndds.CarOilType;
import com.skt.tmap.engine.navigation.network.ndds.TollCarType;
import com.skt.tmap.engine.navigation.network.ndds.dto.request.TruckType;
import com.skt.tmap.engine.navigation.route.RoutePlanType;
import com.skt.tmap.engine.navigation.route.data.MapPoint;
import com.skt.tmap.engine.navigation.route.data.WayPoint;
import com.skt.tmap.overlay.TMapMarkerItem;
import com.skt.tmap.poi.TMapPOIItem;
import com.skt.tmap.vsm.coordinates.VSMCoordinates;
import com.tmapmobility.tmap.tmapsdk.ui.data.CarOption;
import com.tmapmobility.tmap.tmapsdk.ui.data.TruckInfoKey;
import com.tmapmobility.tmap.tmapsdk.ui.fragment.NavigationFragment;
import com.tmapmobility.tmap.tmapsdk.ui.util.TmapUISDK;
import com.example.bigmap.searched_sub;

import org.checkerframework.checker.units.qual.A;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private searched_sub l_sub;
    private search_sub r_sub;
    public static LinearLayout loc_inform_view;

    public static LinearLayout recent_view;

    public Double longi;
    public Double lati;
    TMapData tmapdata = new TMapData();
    int count = 0;
    int num = 1;
    public static Context context;
    public static EditText search_bar;

    LinearLayout recent_search_layout1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        context = this;

        Intent intent = getIntent();
        longi = intent.getDoubleExtra("longi",0);
        lati = intent.getDoubleExtra("lati",0);

        firebaseAuth = FirebaseAuth.getInstance();

        recent();
        getid();

        search_bar = (EditText) findViewById(R.id.edittext_search);
        recent_view = (LinearLayout) findViewById(R.id.recent_view);
        loc_inform_view = (LinearLayout) findViewById(R.id.loc_inform_view);



        // 검색 버튼 또는 엔터 키를 눌렀을 때 동작하도록 설정
        search_bar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND){
                    String text = search_bar.getText().toString();
                    poisearch(text);
                    search(text);
                    return true;
                }
                return false;
            }
        });

        // edittext의 상태 동적 할당 받음 -> edittext에 글자 입력하면 위치 관련 정보 레이아웃이 보이도록함
        // -> 입력한 글자가 없으면 최근 검색어 레이아웃이 보이도록 함.
        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String search_text = s.toString();
                if (search_text.isEmpty()) {
                    recent_view.setVisibility(View.VISIBLE);
                    loc_inform_view.setVisibility(View.GONE);
                } else {
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
                } else {
                    recent_view.setVisibility(View.GONE);
                    loc_inform_view.setVisibility(View.VISIBLE);
                    loc_inform_view.removeAllViewsInLayout();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String search_text = s.toString();
                if (search_text.isEmpty()) {
                    recent_view.setVisibility(View.VISIBLE);
                    loc_inform_view.setVisibility(View.GONE);
                } else {
                    recent_view.setVisibility(View.GONE);
                    loc_inform_view.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public double truncateDecimal(double value, int decimalPlaces) {
        double factor = Math.pow(10, decimalPlaces);
        return Math.floor(value * factor) / factor;
    }
    private void searched(String name ,String address, Double lati,Double longi,Double distance){

        Double distance1 = truncateDecimal(distance, 2);

        loc_inform_view = (LinearLayout) findViewById(R.id.loc_inform_view);

        l_sub = new searched_sub(getApplicationContext());

        View l_sub = getLayoutInflater().inflate(R.layout.fragment_searched_view, null);
        TextView searched_name = l_sub.findViewById(R.id.loc_inform_text1);
        TextView searched_address = l_sub.findViewById(R.id.loc_inform_detail1);
        TextView searched_distance = l_sub.findViewById(R.id.dist1);
        TextView searched_lati = l_sub.findViewById(R.id.longi1);
        TextView searched_longi = l_sub.findViewById(R.id.lati1);

        searched_name.setText(name);
        searched_address.setText(address);
        searched_lati.setText(lati.toString());
        searched_longi.setText(longi.toString());
        searched_distance.setText(distance1+"km");

        LinearLayout search_list = l_sub.findViewById(R.id.linear_list);
        search_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView click_name = search_list.findViewById(R.id.loc_inform_text1);
                TextView click_address = search_list.findViewById(R.id.loc_inform_detail1);
                TextView click_lati = l_sub.findViewById(R.id.longi1);
                TextView click_longi = l_sub.findViewById(R.id.lati1);

                String name = click_name.getText().toString();
                String addr = click_address.getText().toString();
                Double lat = Double.parseDouble(click_lati.getText().toString());
                Double lon = Double.parseDouble(click_longi.getText().toString());

                Intent go_mapview = new Intent(Search.this, mapview.class);
                go_mapview.putExtra("loc_name", name);
                go_mapview.putExtra("loc_addr", addr);
                go_mapview.putExtra("loc_lat", lat);
                go_mapview.putExtra("loc_lon", lon);

                TMapMarkerItem marker_search = new TMapMarkerItem();

                startActivity(go_mapview);
            }
        });

        runOnUiThread(() -> {
            loc_inform_view.addView(l_sub);
        });

    }

    private void poisearch(String searchText){

        TMapData tmapdata = new TMapData();
        TMapPoint tpoint = new TMapPoint();
        tpoint.setLatitude(lati);
        tpoint.setLongitude(longi);
        loc_inform_view = findViewById(R.id.loc_inform_view);
        tmapdata.findAllPOI(searchText, 8, new TMapData.OnFindAllPOIListener() {
            @Override
            public void onFindAllPOI(ArrayList<TMapPOIItem> poiitems) {
                if (poiitems != null && !poiitems.isEmpty()) {
                    runOnUiThread(() -> {
                        if (loc_inform_view.getChildCount() > 0) {
                            loc_inform_view.removeAllViews();
                        }
                        for(int i = 0; i < poiitems.size(); i++){
                            TMapPOIItem poiItem = poiitems.get(i);
//                            System.out.println(poiItem.getPOIPoint());
                            searched(poiItem.getPOIName(), poiItem.getPOIAddress(), poiItem.getPOIPoint().getLatitude(), poiItem.getPOIPoint().getLongitude(), poiItem.getDistance(tpoint)/1000);
                        }
                    });
                }
                else{
                    runOnUiThread(() -> Toast.makeText(Search.this, "검색 결과가 없습니다.", Toast.LENGTH_LONG).show());
                }
            }
        });
    }

    private void search(String searchText){

        TMapData tmapdata = new TMapData();
        TMapPoint tpoint = new TMapPoint();
        tpoint.setLatitude(lati);
        tpoint.setLongitude(longi);
        if(num != 1){
            runOnUiThread(this::getid);
        }
        tmapdata.findAllPOI(searchText, 8, new TMapData.OnFindAllPOIListener() {
            @Override
            public void onFindAllPOI(ArrayList<TMapPOIItem> poiitems) {
                if (poiitems != null && !poiitems.isEmpty()) {
                    runOnUiThread(() -> {
                        TMapPOIItem firstItem = poiitems.get(0);
                        firestore = FirebaseFirestore.getInstance();
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        String email = user.getEmail();

                        DocumentReference docR = firestore.collection("최근기록DB").document(email);

                        String addressText = firstItem.getPOIName();
                        String db_address = firstItem.getPOIAddress();
                        double db_latitude = firstItem.getPOIPoint().getLatitude();
                        double db_longitude = firstItem.getPOIPoint().getLongitude();

                        System.out.println("addnum"+num);

                        String str_num = String.valueOf(num);

                        if (num == 1){
                            num++;
                        }


                        HashMap<Object,Object> hashMap = new HashMap<>();

                        hashMap.put("location_name",addressText);
                        hashMap.put("address",db_address);
                        hashMap.put("latitude",db_latitude);
                        hashMap.put("longitude",db_longitude);


                        docR.collection("최근기록").document(str_num).set(hashMap);

                    });
                }
                else{
                    runOnUiThread(() -> Toast.makeText(Search.this, "검색 결과가 없습니다.", Toast.LENGTH_LONG).show());
                }
            }
        });
    }
    public void getid(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        final int[] big = {0};
        final int a[] = new int[1];
        if (email != null) {
            CollectionReference recentCollectionRef = firestore.collection("최근기록DB").document(email).collection("최근기록");

            recentCollectionRef
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        if (querySnapshot.isEmpty()) {
                            // 도큐먼트가 없을 때
                            num = 1;
                        } else {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                String idString = document.getId();
                                a[0] = Integer.parseInt(idString);
                                if(big[0] < a[0]){
                                    big[0] = a[0];
                                }
                                System.out.println(a[0]);
                            }
                            num = big[0];
                            num++;
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Recent", "Error retrieving recent records: " + e.getMessage());
                    });
        }
    }

    private void recent() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        recent_search_layout1 = findViewById(R.id.recent_search_layout1);

        if (email != null) {
            CollectionReference recentCollectionRef = firestore.collection("최근기록DB").document(email).collection("최근기록");

            recentCollectionRef
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        runOnUiThread(() -> {
                            List<List<Object>> documentList = new ArrayList<>(); // 각 문서를 담는 리스트

                            if (recent_search_layout1.getChildCount() > 0) {
                                recent_search_layout1.removeAllViews();
                            }

                            for (QueryDocumentSnapshot document : querySnapshot) {
                                List<Object> docData = new ArrayList<>(); // 문서 데이터를 담는 리스트
                                docData.add(document.getId());
                                docData.add(document.getString("location_name"));
                                docData.add(document.getString("address"));
                                docData.add(document.getDouble("latitude"));
                                docData.add(document.getDouble("longitude"));

                                documentList.add(docData);
                            }

                            // 리스트 정렬
                            Collections.sort(documentList, new Comparator<List<Object>>() {
                                @Override
                                public int compare(List<Object> doc1, List<Object> doc2) {
                                    // id를 기준으로 내림차순으로 정렬
                                    int id1 = Integer.parseInt((String) doc1.get(0));
                                    int id2 = Integer.parseInt((String) doc2.get(0));
                                    return id2 - id1;
                                }
                            });

                            // 최대 8개의 기록 화면에 출력
                            int count = 0;
                            for (List<Object> docData : documentList) {
                                recentget((String) docData.get(1), (String) docData.get(2), (Double) docData.get(3), (Double) docData.get(4));
                                count++; // 반복 횟수 증가

                                if (count == 8) {
                                    // 8번 반복 후 종료
                                    break;
                                }
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Recent", "Error retrieving recent records: " + e.getMessage());
                    });
        }
    }



    private void recentget(String name ,String address, Double lati,Double longi){

        recent_search_layout1 = (LinearLayout) findViewById(R.id.recent_search_layout1);

        r_sub = new search_sub(getApplicationContext());

        View r_sub = getLayoutInflater().inflate(R.layout.fragment_search_sub, null);

        TextView search_name = r_sub.findViewById(R.id.recent_search_text1);
        TextView search_address = r_sub.findViewById(R.id.recent_search_address);
        TextView search_lati = r_sub.findViewById(R.id.recent_search_longi);
        TextView search_longi = r_sub.findViewById(R.id.recent_search_lati);

        search_name.setText(name);
        search_address.setText(address);
        search_lati.setText(lati.toString());
        search_longi.setText(longi.toString());

        ImageView bttn_delete = r_sub.findViewById(R.id.bttn_delete);
        bttn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_text(view);
            }
        });
        LinearLayout search_list = r_sub.findViewById(R.id.list_search);
        search_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView click_name = search_list.findViewById(R.id.recent_search_text1);
                TextView click_address = search_list.findViewById(R.id.recent_search_address);
                TextView click_lati = r_sub.findViewById(R.id.recent_search_longi);
                TextView click_longi = r_sub.findViewById(R.id.recent_search_lati);

                String name = click_name.getText().toString();
                String addr = click_address.getText().toString();
                Double lat = Double.parseDouble(click_lati.getText().toString());
                Double lon = Double.parseDouble(click_longi.getText().toString());

                Intent go_mapview = new Intent(Search.this, mapview.class);
                go_mapview.putExtra("loc_name", name);
                go_mapview.putExtra("loc_addr", addr);
                go_mapview.putExtra("loc_lat", lat);
                go_mapview.putExtra("loc_lon", lon);

                TMapMarkerItem marker_search = new TMapMarkerItem();

                startActivity(go_mapview);
            }
        });


        runOnUiThread(() -> {
            recent_search_layout1.addView(r_sub);
        });



    }


    public void delete_text(View v) {
        LinearLayout layout = (LinearLayout) v.getParent().getParent();
        TextView textViewName = layout.findViewById(R.id.recent_search_text1);
        String name = textViewName.getText().toString();

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();

        if (email != null) {
            CollectionReference recentCollectionRef = firestore.collection("최근기록DB").document(email).collection("최근기록");
            Query query = recentCollectionRef.whereEqualTo("location_name", name);

            query.get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            document.getReference().delete();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Recent", "Error deleting recent record: " + e.getMessage());
                    });
        }
        recent();
    }

    public void go_before(View view) {
        Intent intent_before = new Intent(this, mapview.class);
        startActivity(intent_before);
    }
    public void go_location(View v) {
        LinearLayout layout = (LinearLayout) v.getParent();
        TextView textViewName = layout.findViewById(R.id.loc_inform_text1);
        TextView textViewAddress = layout.findViewById(R.id.loc_inform_detail1);
        TextView textViewDistance = layout.findViewById(R.id.dist1);
        TextView textViewLatitude = layout.findViewById(R.id.lati1);
        TextView textViewLongitude = layout.findViewById(R.id.longi1);

        String name = textViewName.getText().toString();
        String address = textViewAddress.getText().toString();
        String distance = textViewDistance.getText().toString();
        String latitude = textViewLatitude.getText().toString();
        String longitude = textViewLongitude.getText().toString();

        Intent mapIntent = new Intent(Search.this, mapview.class);
        mapIntent.putExtra("name", name);
        mapIntent.putExtra("address", address);
        mapIntent.putExtra("distance", distance);
        mapIntent.putExtra("latitude", latitude);
        mapIntent.putExtra("longitude", longitude);

        startActivity(mapIntent);
    }

}