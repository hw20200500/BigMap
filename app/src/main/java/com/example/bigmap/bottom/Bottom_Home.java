package com.example.bigmap.bottom;

import android.annotation.SuppressLint;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.skt.tmap.TMapData;
import com.skt.tmap.TMapPoint;
import com.skt.tmap.overlay.TMapMarkerItem;
import com.skt.tmap.poi.TMapPOIItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


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

    LinearLayout recent_search_layout1;
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

    @SuppressLint("SetTextI18n")
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        LinearLayout recent_title1 = view.findViewById(R.id.recent_linear1);
        LinearLayout recent_title2 = view.findViewById(R.id.recent_linear2);
        LinearLayout recent_title3 = view.findViewById(R.id.recent_linear3);
        LinearLayout[] recent_titles = {recent_title1, recent_title2, recent_title3};

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String email = user.getEmail();

        CollectionReference recentCollectionRef = firestore.collection("최근기록DB").document(email).collection("최근기록");
        // 데이터베이스에 데이터가 몇 개 있는지 확인하는 코드
        recentCollectionRef.get().addOnSuccessListener(querySnapshots -> {
            getActivity().runOnUiThread(() -> {
                List<List<Object>> documentList = new ArrayList<>(); // 각 문서를 담는 리스트


                for (QueryDocumentSnapshot document : querySnapshots) {
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

                // 최대 3개의 기록 화면에 출력
                int count = 0;
                for (List<Object> docData : documentList) {
                    String locationName = (String) docData.get(1);
                    String address = (String) docData.get(2);
                    Double latitude = (Double) docData.get(3);
                    Double longitude = (Double) docData.get(4);

                    count++; // 반복 횟수 증가

                    if (count == 4) {
                        // 3번 반복 후 종료
                        break;
                    }

                    String r_title = "recent_title" + count;
                    int titleId = getResources().getIdentifier(r_title, "id", getActivity().getPackageName());
                    TextView titleTextView = recent_titles[count - 1].findViewById(titleId);
                    titleTextView.setText(locationName);

                    String r_address = "recent_address" + count;
                    int address_Id = getResources().getIdentifier(r_address, "id", getActivity().getPackageName());
                    titleTextView = recent_titles[count - 1].findViewById(address_Id);

                    titleTextView.setText(address);

                    String r_longi = "recent_longi" + count;
                    int longiId = getResources().getIdentifier(r_longi, "id", getActivity().getPackageName());
                    titleTextView = recent_titles[count - 1].findViewById(longiId);
                    System.out.println(longitude);
                    titleTextView.setText(longitude.toString());

                    String r_lati = "recent_lati" + count;
                    int latiId = getResources().getIdentifier(r_lati, "id", getActivity().getPackageName());
                    titleTextView = recent_titles[count - 1].findViewById(latiId);
                    System.out.println(latitude);
                    titleTextView.setText(latitude.toString());

                }

                createGasList("주유소");
                creatrestList("휴게소");
            });
        });

        recent_title1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentdata(recent_title1,1);
            }
        });

        recent_title2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentdata(recent_title2,2);
            }
        });

        recent_title3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentdata(recent_title3,3);
            }
        });

    }

    public void intentdata(LinearLayout llout,int i){
        String getdata = "recent_title" + i;
        int id = getResources().getIdentifier(getdata, "id", getActivity().getPackageName());
        TextView click_name = llout.findViewById(id);

        getdata = "recent_address" + i;
        id = getResources().getIdentifier(getdata, "id", getActivity().getPackageName());
        TextView click_address = llout.findViewById(id);

        getdata = "recent_lati" + i;
        id = getResources().getIdentifier(getdata, "id", getActivity().getPackageName());
        TextView click_lati = llout.findViewById(id);

        getdata = "recent_longi" + i;
        id = getResources().getIdentifier(getdata, "id", getActivity().getPackageName());
        TextView click_longi = llout.findViewById(id);

        String name = click_name.getText().toString();
        String addr = click_address.getText().toString();
        Double lat = Double.parseDouble(click_lati.getText().toString());
        Double lon = Double.parseDouble(click_longi.getText().toString());

        Intent go_mapview = new Intent(getContext(), mapview.class);
        go_mapview.putExtra("loc_name", name);
        go_mapview.putExtra("loc_addr", addr);
        go_mapview.putExtra("loc_lat", lat);
        go_mapview.putExtra("loc_lon", lon);

        startActivity(go_mapview);
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
