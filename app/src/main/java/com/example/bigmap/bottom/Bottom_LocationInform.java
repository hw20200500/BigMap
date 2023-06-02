package com.example.bigmap.bottom;

import android.content.Intent;
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
import android.widget.TextView;

import com.example.bigmap.MainActivity;
import com.example.bigmap.R;
import com.example.bigmap.mapview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.skt.tmap.TMapView;
import com.skt.tmap.overlay.TMapMarkerItem;

import java.util.HashMap;

public class Bottom_LocationInform extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    int del_num;
    int num;
    Double latitude;
    Double longitude;
    boolean exist = false;
    String del_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location_inform, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView star = view.findViewById(R.id.bttn_star);
        TextView loc_title = view.findViewById(R.id.loc_title);
        TextView loc_addr = view.findViewById(R.id.loc_addr);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String email = user.getEmail();

        DocumentReference docR = firestore.collection("즐겨찾기DB").document(email);



        // mapview에서 받아온 위치 정보(이름, 주소, 위경도) 가져와서 문자열 및 double 변수에 저장
        String title = this.getArguments().getString("loc_name");
        String addr = this.getArguments().getString("loc_addr");
        latitude = this.getArguments().getDouble("loc_lat");
        longitude = this.getArguments().getDouble("loc_lon");
        //"loc_lon", longitude

        // 장소 이름, 주소는 각각 textview에 저장
        loc_title.setText(title);
        loc_addr.setText(addr);

        // 데이터베이스에 데이터가 몇 개 있는지 확인하는 코드
        Query query = docR.collection("즐겨찾기");
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Count fetched successfully
                    AggregateQuerySnapshot snapshot = task.getResult();
                    Log.d(getTag(), "Count: " + snapshot.getCount());
                    // 데이터베이스에 저장된 데이터의 개수 num에 저장
                    String number = String.valueOf(snapshot.getCount());
                    num = Integer.parseInt(number);
                    Log.d(getTag(), "num: " + num);

                    for (int j=0; j<=num; j++) {
                        Log.d("TAG", "j: "+j);
                        DocumentReference read_doc = docR.collection("즐겨찾기").document(String.valueOf(j));
                        read_doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if(document.exists()) {
                                        String read_name = document.getString("location_name");
                                        Double read_lat = document.getDouble("latitude");
                                        Double read_lon = document.getDouble("longitude");

                                        if (read_name.equals(title)) {
                                            star.setImageResource(R.drawable.location_fill_star);
                                            Log.d(getTag(), "즐겨찾기: "+read_name);
                                        } else {
                                            Log.d(getTag(), "즐겨찾기 X");
                                            Log.d(getTag(), "이름: "+ read_name);
                                        }
                                    }

                                } else {
                                    Log.d("TAG", "get failed with ", task.getException());
                                }
                            }
                        });
                    }
                } else {
                    Log.d(getTag(), "Count failed: ", task.getException());
                }
            }
        });

        Button bttn_start = view.findViewById(R.id.bttn_start);
        Button bttn_dest = view.findViewById(R.id.bttn_dest);

        // '도착' 버튼 클릭 시 MainActivity로 이동 + 장소 이름, 주소, 위경도 정보 같이 intent
        bttn_dest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_set_dest = new Intent(getActivity(), MainActivity.class);
                intent_set_dest.putExtra("loc_name", title);
                intent_set_dest.putExtra("loc_addr", addr);
                intent_set_dest.putExtra("loc_lat", latitude);
                intent_set_dest.putExtra("loc_lon", longitude);
                // 클릭한 버튼이 '출발' 버튼인지 '도착' 버튼인지 구분하기 위한 키워드 문자열
                intent_set_dest.putExtra("title", "destination");

                startActivity(intent_set_dest);
            }
        });

        // '출발' 버튼 클릭 시 MainActivity로 이동 + 장소 이름, 주소, 위경도 정보 같이 intent
        bttn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_set_start = new Intent(getActivity(), MainActivity.class);
                intent_set_start.putExtra("loc_name", title);
                intent_set_start.putExtra("loc_addr", addr);
                intent_set_start.putExtra("loc_lat", latitude);
                intent_set_start.putExtra("loc_lon", longitude);
                // 클릭한 버튼이 '출발' 버튼인지 '도착' 버튼인지 구분하기 위한 키워드 문자열
                intent_set_start.putExtra("title", "start");

                startActivity(intent_set_start);
            }
        });

        // 즐겨찾기 버튼
        star.setOnClickListener(new View.OnClickListener() {
            TMapMarkerItem marker = new TMapMarkerItem();
            TMapView tMapView = ((mapview) getActivity()).tMapView;
            @Override
            public void onClick(View view) {

                if (exist==false) {
                    star.setImageResource(R.drawable.location_fill_star);
                    // 파이어베이스 저장
                    HashMap<Object,Object> hashMap = new HashMap<>();
                    hashMap.put("location_name",title);
                    hashMap.put("address",addr);
                    hashMap.put("latitude",latitude);
                    hashMap.put("longitude",longitude);

                    docR.collection("즐겨찾기").document(String.valueOf(num)).set(hashMap);



                    marker.setId("marker");
                    marker.setTMapPoint(latitude, longitude);
                    marker.setIcon(BitmapFactory.decodeResource(getResources(),R.drawable.search_bookmark2_icon));
                    tMapView.addTMapMarkerItem(marker);
                    exist=true;
                } else {
                    star.setImageResource(R.drawable.location_inform_star_empty);

                    for (int i = 0; i<=num; i++) {
                        int finalI = i;
                        docR.collection("즐겨찾기").document(String.valueOf(i)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        String get_locname = document.getString("location_name");
                                        if (get_locname.equals(title)) {
                                            del_num = finalI;
                                            Log.d(getTag(), "del_num: "+del_num);
                                            docR.collection("즐겨찾기").document(String.valueOf(del_num)).delete();
                                            String id = "marker"+String.valueOf(del_num);
                                            tMapView.removeTMapMarkerItem(id);
                                        }
                                    }
                                }
                            }
                        });

                    }

                }


//                num++;
            }
        });
    }
}