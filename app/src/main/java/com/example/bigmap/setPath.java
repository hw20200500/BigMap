package com.example.bigmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigmap.bookmarks.Sub;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.AggregateQuery;
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
import com.skt.tmap.poi.TMapPOIItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class setPath extends AppCompatActivity {
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    String email;
    setPath_sub sub_layout;
    int count;
    LinearLayout recent_loc_lists;
    LinearLayout bookmark_loc_lists;
    TextView loc_title;
//    String id;
    String name_startP;
    String addr_startP;
    Double lat_startP;
    Double lon_startP;
    String name_destP;
    String addr_destP;
    Double lat_destP;
    Double lon_destP;
    TextView text_dest;
    FrameLayout search_layout;
    int num = 1;
    private search_sub r_sub;
    private searched_sub l_sub;

    TextView searched_name;
    TextView searched_lati;
    TextView searched_longi;
    TextView text_startPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_path);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        email = firebaseAuth.getCurrentUser().getEmail();
        recent_loc_lists = findViewById(R.id.recent_loc_lists);
        bookmark_loc_lists = findViewById(R.id.bookmark_loc_lists);

        search_layout = findViewById(R.id.search_layout);

        Intent get_inform_intent = getIntent();
        name_startP = get_inform_intent.getStringExtra("loc_name");
        addr_startP = get_inform_intent.getStringExtra("loc_addr");
        lat_startP = get_inform_intent.getDoubleExtra("loc_lat", 0);
        lon_startP = get_inform_intent.getDoubleExtra("loc_lon", 0);

        text_startPoint = findViewById(R.id.text_startPoint);
        text_dest = findViewById(R.id.text_destPoint);
        text_startPoint.setText(name_startP);


        set_subLayout("최근기록");
        set_subLayout("즐겨찾기");

        LinearLayout linear_start = findViewById(R.id.linear_start);
        LinearLayout linear_dest = findViewById(R.id.linear_dest);

        linear_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searching(text_startPoint);
            }
        });

        linear_dest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searching(text_dest);
            }
        });



    }

    private void search(String searchText){

        TMapData tmapdata = new TMapData();
        /*TMapPoint tpoint = new TMapPoint();
        tpoint.setLatitude(lati);
        tpoint.setLongitude(longi);*/
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
                    runOnUiThread(() -> Toast.makeText(setPath.this, "검색 결과가 없습니다.", Toast.LENGTH_LONG).show());
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

    private void searching(TextView textView) {

        search_layout.setVisibility(View.VISIBLE);
        recent(textView);

        EditText search_bar = search_layout.findViewById(R.id.edittext_search);
        LinearLayout recent_view = search_layout.findViewById(R.id.recent_view);
        LinearLayout loc_inform_view = search_layout.findViewById(R.id.loc_inform_view);
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

        search_bar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND){
                    String text = search_bar.getText().toString();
                    poisearch(text, textView);
                    search(text);
                    return true;
                }
                return false;
            }
        });
    }

    public double truncateDecimal(double value, int decimalPlaces) {
        double factor = Math.pow(10, decimalPlaces);
        return Math.floor(value * factor) / factor;
    }
    private void searched(String name ,String address, Double lati,Double longi,Double distance, TextView textView){

        Double distance1 = truncateDecimal(distance, 2);

        LinearLayout loc_inform_view = (LinearLayout) findViewById(R.id.loc_inform_view);

        l_sub = new searched_sub(getApplicationContext());

        View l_sub = getLayoutInflater().inflate(R.layout.fragment_searched_view, null);
        searched_name = l_sub.findViewById(R.id.loc_inform_text1);
        TextView searched_address = l_sub.findViewById(R.id.loc_inform_detail1);
        TextView searched_distance = l_sub.findViewById(R.id.dist1);
        searched_lati = l_sub.findViewById(R.id.lati1);
        searched_longi = l_sub.findViewById(R.id.longi1);

        searched_name.setText(name);
        searched_address.setText(address);
        searched_lati.setText(lati.toString());
        searched_longi.setText(longi.toString());
        searched_distance.setText(distance1+"km");

        searched_name.getText();

        LinearLayout searched_list = l_sub.findViewById(R.id.linear_list);
        searched_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView click_text = searched_list.findViewById(R.id.loc_inform_text1);
                TextView click_lat = searched_list.findViewById(R.id.lati1);
                TextView click_lon = searched_list.findViewById(R.id.longi1);
                String name = click_text.getText().toString();
                Toast.makeText(setPath.this, "클릭 장소: "+name, Toast.LENGTH_SHORT).show();
                Double lati = Double.parseDouble(click_lat.getText().toString());
                Double longi = Double.parseDouble(click_lon.getText().toString());

                textView.setText(name);

                if (textView.equals(text_startPoint)){
                    name_startP = name;
                    lat_startP = lati;
                    lon_startP = longi;
                } else {
                    name_destP = name;
                    lat_destP = lati;
                    lon_destP = longi;
                }

                go_main(name_startP, lat_startP, lon_startP, name_destP, lat_destP, lon_destP);

                EditText search_bar = search_layout.findViewById(R.id.edittext_search);
                search_bar.setText("");
                search_layout.setVisibility(View.GONE);
            }
        });

        runOnUiThread(() -> {
            loc_inform_view.addView(l_sub);
        });

    }

    private void go_main(String name_start, Double lat_start, Double lon_start, String name_dest, Double lat_dest, Double lon_dest) {
        if (!text_startPoint.getText().equals("출발지 입력") && !text_dest.getText().equals("도착지 입력")) {
            Intent intent_main = new Intent(setPath.this, MainActivity.class);
            intent_main.putExtra("name_startP", name_start);
            intent_main.putExtra("lat_startP", lat_start);
            intent_main.putExtra("lon_startP", lon_start);
            intent_main.putExtra("name_destP", name_dest);
            intent_main.putExtra("lat_destP", lat_dest);
            intent_main.putExtra("lon_destP", lon_dest);

            startActivity(intent_main);
        }
    }

    private void search_end(String name) {

    }

    private void poisearch(String searchText, TextView textView){

        TMapData tmapdata = new TMapData();
        TMapPoint tpoint = new TMapPoint();
        /*tpoint.setLatitude(lati);
        tpoint.setLongitude(longi);*/
        LinearLayout loc_inform_view = search_layout.findViewById(R.id.loc_inform_view);
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
                            searched(poiItem.getPOIName(), poiItem.getPOIAddress(), poiItem.getPOIPoint().getLatitude(), poiItem.getPOIPoint().getLongitude(), poiItem.getDistance(tpoint)/1000, textView);
                        }
                    });
                }
                else{
                    runOnUiThread(() -> Toast.makeText(setPath.this, "검색 결과가 없습니다.", Toast.LENGTH_LONG).show());
                }
            }
        });
    }

    private void recent(TextView textView) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        LinearLayout recent_search_layout1 = search_layout.findViewById(R.id.recent_search_layout1);

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
                                recentget((String) docData.get(1), (String) docData.get(2), (Double) docData.get(3), (Double) docData.get(4), textView);
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

    private void recentget(String name ,String address, Double lati,Double longi, TextView textView){

        LinearLayout recent_view = search_layout.findViewById(R.id.recent_view);
        LinearLayout recent_search_layout1 = search_layout.findViewById(R.id.recent_search_layout1);

        r_sub = new search_sub(getApplicationContext());

        View r_sub = getLayoutInflater().inflate(R.layout.fragment_search_sub, null);

        TextView search_name = r_sub.findViewById(R.id.recent_search_text1);
        TextView search_address = r_sub.findViewById(R.id.recent_search_address);
        TextView search_lati = r_sub.findViewById(R.id.recent_search_lati);
        TextView search_longi = r_sub.findViewById(R.id.recent_search_longi);

        search_name.setText(name);
        search_address.setText(address);
        search_lati.setText(lati.toString());
        search_longi.setText(longi.toString());

        LinearLayout recent_list = r_sub.findViewById(R.id.list_search);
        recent_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView click_text = recent_list.findViewById(R.id.recent_search_text1);
                TextView click_lat = recent_list.findViewById(R.id.recent_search_lati);
                TextView click_lon = recent_list.findViewById(R.id.recent_search_longi);
                String name = click_text.getText().toString();
                Toast.makeText(setPath.this, "클릭 장소: "+name, Toast.LENGTH_SHORT).show();
                Double lati = Double.parseDouble(click_lat.getText().toString());
                Double longi = Double.parseDouble(click_lon.getText().toString());

                textView.setText(name);

                if (textView.equals(text_startPoint)){
                    name_startP = name;
                    lat_startP = lati;
                    lon_startP = longi;
                } else {
                    name_destP = name;
                    lat_destP = lati;
                    lon_destP = longi;
                }

                go_main(name_startP, lat_startP, lon_startP, name_destP, lat_destP, lon_destP);

                EditText search_bar = search_layout.findViewById(R.id.edittext_search);
                search_bar.setText("");
                search_layout.setVisibility(View.GONE);
            }
        });


        runOnUiThread(() -> {
            recent_search_layout1.addView(r_sub);
        });

    }

    private void set_subLayout(String db_name) {
        DocumentReference docR = firestore.collection(db_name+"DB").document(email);
//        int bookmarks_num = bookmarks_count();
//        count = bookmarks_num;

        Task<QuerySnapshot> docRef = docR.collection(db_name).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                count = 5;
                for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                    String title = ds.getString("location_name");
                    String addr = ds.getString("address");
                    Double latitude = ds.getDouble("latitude");
                    Double longitude = ds.getDouble("longitude");

                    String id = ds.getId();
                    System.out.println(db_name+" : "+title);
                    if (count>0) {
                        sub_layout = new setPath_sub(getApplicationContext());
                        loc_title = sub_layout.findViewById(R.id.loc_title);
                        loc_title.setText(title);


                        if (db_name.equals("즐겨찾기")) {
                            bookmark_loc_lists.addView(sub_layout);

                        } else if (db_name.equals("최근기록")){
                            recent_loc_lists.addView(sub_layout);
                        }

                        LinearLayout setPath_sublist = sub_layout.findViewById(R.id.setPath_sublist);
                        setPath_sublist.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String list_title = (String) loc_title.getText();
//                                                Toast.makeText(getActivity().getApplicationContext(), "클릭 장소: "+bookmarks_title_str+" 넘버: "+j_str, Toast.LENGTH_SHORT).show();

                                DocumentReference get_doc = firestore.collection(db_name+"DB").document(email).collection(db_name).document(id);
                                get_doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot document_get = task.getResult();
                                        if (document_get.exists()) {
                                            name_destP = document_get.getString("location_name");

                                            text_dest = findViewById(R.id.text_destPoint);
                                            text_dest.setText(name_destP);

                                            addr_destP = document_get.getString("address");
                                            lat_destP = document_get.getDouble("latitude");
                                            lon_destP = document_get.getDouble("longitude");

                                            Intent intent_main = new Intent(setPath.this, MainActivity.class);
                                            intent_main.putExtra("name_startP", name_startP);
                                            intent_main.putExtra("addr_startP", addr_startP);
                                            intent_main.putExtra("lat_startP", lat_startP);
                                            intent_main.putExtra("lon_startP", lon_startP);
                                            intent_main.putExtra("name_destP", name_destP);
                                            intent_main.putExtra("addr_destP", addr_destP);
                                            intent_main.putExtra("lat_destP", lat_destP);
                                            intent_main.putExtra("lon_destP", lon_destP);

                                            startActivity(intent_main);
                                        }
                                    }
                                });
                            }
                        });
                        count--;


                    }

                }

            }
        });
    }



}