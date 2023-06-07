package com.example.bigmap.bottom;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigmap.R;
import com.example.bigmap.Search;
import com.example.bigmap.mapview;
import com.example.bigmap.search_sub;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.skt.tmap.TMapData;
import com.skt.tmap.TMapPoint;
import com.skt.tmap.overlay.TMapMarkerItem;
import com.skt.tmap.poi.TMapPOIItem;

import java.util.ArrayList;


public class Bottom_Home extends Fragment {

    //home bottom sheet
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    int num;
    int j;
    double latitude = 36.35199106;
    double longitude = 127.42223688;
    LinearLayout Layout_gas;
    LinearLayout Layout_rest;
    private fragment_home_sub home_sub;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_bottom__home, container, false);
        View rootview = inflater.inflate(R.layout.fragment_bottom__home, container, false);


        rootview.setTag("bottom_home_tag");
        return rootview;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        TextView recent_title1 = view.findViewById(R.id.recent_title1);
        TextView recent_title2 = view.findViewById(R.id.recent_title2);
        TextView recent_title3 = view.findViewById(R.id.recent_title3);
        TextView[] recent_titles = {recent_title1, recent_title2, recent_title3};

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String email = user.getEmail();

        DocumentReference docR = firestore.collection("최근기록DB").document(email);

        // 데이터베이스에 데이터가 몇 개 있는지 확인하는 코드
        Query query1 = docR.collection("최근기록");
        AggregateQuery countQuery1 = query1.count();
        countQuery1.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Count fetched successfully
                    AggregateQuerySnapshot snapshot = task.getResult();
                    // 데이터베이스에 저장된 데이터의 개수 num에 저장
                    String number = String.valueOf(snapshot.getCount());
                    num = Integer.parseInt(number);
                    j = num;
                    for (TextView textView : recent_titles) {

                        Log.d("TAG", "j: " + j);
                        DocumentReference read_doc = docR.collection("최근기록").document(String.valueOf(j));

                        read_doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        String read_name = document.getString("location_name");
                                        double read_lat = document.getDouble("latitude");
                                        double read_lon = document.getDouble("longitude");

                                        textView.setText(read_name);

                                    }

                                } else {
                                    Log.d(getTag(), "get failed with ", task.getException());
                                }
                            }
                        });
                        j--;
                    }
                } else {
                    Log.d(getTag(), "Count failed: ", task.getException());
                }
            }
        });

        ImageView refuel = view.findViewById(R.id.refuel01);
        ImageView restarea = view.findViewById(R.id.restarea01);

        createGasList("주유소");
        creatrestList("휴게소");

        /*refuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGasList("주유소");
            }
        });

        restarea.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                creatrestList("휴게소");
            }
        });*/

    }


    public void createGasList(String data) {
        Layout_gas = getView().findViewById(R.id.gas_layout);
        if (Layout_gas.getChildCount() > 0) {
            Layout_gas.removeAllViews();
        }
        findpoi(data, Layout_gas);
    }

    public void creatrestList(String data) {
        Layout_rest = getView().findViewById(R.id.resting_layout);
        if (Layout_rest.getChildCount() > 0) {
            Layout_rest.removeAllViews();
        }
        findpoi(data, Layout_rest);
    }
    public TMapPoint getCurrentLocation() {
        mapview mv = new mapview();
        longitude = mv.getLongitude();
        latitude = mv.getLatitude();
        System.out.println(latitude + "," + longitude);
        return new TMapPoint(latitude, longitude);
    }

    public void findpoi(String data, LinearLayout linearLayout) {
        // 현재 위치를 가져오는 메서드를 호출하여 현재 위치를 얻습니다.
        TMapPoint currentLocation = getCurrentLocation();
        // TMapData 객체를 생성합니다.
        TMapData tMapData = new TMapData();

        tMapData.findAroundNamePOI(currentLocation, data, 100, 3, new TMapData.OnFindAroundNamePOIListener() {
            @Override
            public void onFindAroundNamePOI(ArrayList<TMapPOIItem> poiItems) {
                if (poiItems != null && !poiItems.isEmpty()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (TMapPOIItem poiItem : poiItems) {
                                String title = poiItem.getPOIName();
                                String address = poiItem.getPOIAddress();
                                double lati = poiItem.getPOIPoint().getLatitude();
                                double longi = poiItem.getPOIPoint().getLongitude();
                                recentget(title,address,lati,longi, linearLayout);
                            }
                        }
                    });
                }else{
//                    getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "검색 결과가 없습니다.", Toast.LENGTH_LONG).show());
                }
            }
        });
    }

    public void recentget(String name ,String address, Double lati,Double longi, LinearLayout linearLayout){

        home_sub = new fragment_home_sub(getActivity().getApplicationContext());

        View r_sub = getLayoutInflater().inflate(R.layout.fragment_mainbottom_sub, null);

        TextView search_name = r_sub.findViewById(R.id.recent_search_text1);
        TextView search_address = r_sub.findViewById(R.id.recent_search_address);
        TextView search_lati = r_sub.findViewById(R.id.recent_search_lati);
        TextView search_longi = r_sub.findViewById(R.id.recent_search_longi);

        search_name.setText(name);
        search_address.setText(address);
        search_lati.setText(lati.toString());
        search_longi.setText(longi.toString());

        LinearLayout search_list = r_sub.findViewById(R.id.list_search);
        search_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView click_name = search_list.findViewById(R.id.recent_search_text1);
                TextView click_address = search_list.findViewById(R.id.recent_search_address);
                TextView click_lati = search_list.findViewById(R.id.recent_search_lati);
                TextView click_longi = search_list.findViewById(R.id.recent_search_longi);

                String name = click_name.getText().toString();
                String addr = click_address.getText().toString();
                Double lat = Double.parseDouble(click_lati.getText().toString());
                Double lon = Double.parseDouble(click_longi.getText().toString());

                Intent go_mapview = new Intent(getContext(), mapview.class);
                go_mapview.putExtra("loc_name", name);
                go_mapview.putExtra("loc_addr", addr);
                go_mapview.putExtra("loc_lat", lat);
                go_mapview.putExtra("loc_lon", lon);

                TMapMarkerItem marker_search = new TMapMarkerItem();

                startActivity(go_mapview);
            }
        });

        linearLayout.addView(r_sub);

    }

}
