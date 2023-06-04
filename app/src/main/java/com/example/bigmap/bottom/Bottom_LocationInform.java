package com.example.bigmap.bottom;

import static com.airbnb.lottie.L.TAG;
import static com.example.bigmap.bottom.Bottom_Favorite.bookmarks_list;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bigmap.MainActivity;
import com.example.bigmap.R;
import com.example.bigmap.bookmarks.Sub;
import com.example.bigmap.mapview;
import com.example.bigmap.setPath;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
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
    Double latitude;
    Double longitude;
    boolean exist = false;
    Sub n_layout;
    String email;
    Bottom_Favorite bottomFavorite;
    View layout;
    int num;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_location_inform, container, false);



        return rootview;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView star = view.findViewById(R.id.bttn_star);
        TextView loc_title = view.findViewById(R.id.loc_title);
        TextView loc_addr = view.findViewById(R.id.loc_addr);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        email = user.getEmail();

        DocumentReference docR = firestore.collection("즐겨찾기DB").document(email);
        bottomFavorite = (Bottom_Favorite) getActivity().getSupportFragmentManager().findFragmentById(R.id.bottom_sheet);



        // mapview에서 받아온 위치 정보(이름, 주소, 위경도) 가져와서 문자열 및 double 변수에 저장
        String title = this.getArguments().getString("loc_name");
        String addr = this.getArguments().getString("loc_addr");
        latitude = this.getArguments().getDouble("loc_lat");
        longitude = this.getArguments().getDouble("loc_lon");
        //"loc_lon", longitude

        // 장소 이름, 주소는 각각 textview에 저장
        loc_title.setText(title);
        loc_addr.setText(addr);


        Task<QuerySnapshot> docRef = docR.collection("즐겨찾기").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                    String loc_name = ds.getString("location_name");
                    String loc_addr = ds.getString("address");
                    Double loc_lat = ds.getDouble("latitude");
                    Double loc_lon = ds.getDouble("longitude");

                    if (loc_name.equals(title)) {
                        star.setImageResource(R.drawable.location_fill_star);
                        Log.d(getTag(), "즐겨찾기: "+loc_name);
                        exist = true;
                    } else {
                        Log.d(getTag(), "즐겨찾기 X");
                        Log.d(getTag(), "이름: "+ loc_name);
                    }
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
                intent_set_dest.putExtra("name_destP", title);
                intent_set_dest.putExtra("addr_destP", addr);
                intent_set_dest.putExtra("lat_destP", latitude);
                intent_set_dest.putExtra("lon_destP", longitude);
                // 클릭한 버튼이 '출발' 버튼인지 '도착' 버튼인지 구분하기 위한 키워드 문자열
                intent_set_dest.putExtra("title", "destination");

                startActivity(intent_set_dest);
            }
        });

        // '출발' 버튼 클릭 시 MainActivity로 이동 + 장소 이름, 주소, 위경도 정보 같이 intent
        bttn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_set_start = new Intent(getActivity(), setPath.class);
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

                    docR.collection("즐겨찾기").document(title).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            n_layout = new Sub(getActivity().getApplicationContext());
                            TextView bookmarks_title = n_layout.findViewById(R.id.bookmarks_title);
                            TextView bookmarks_addr = n_layout.findViewById(R.id.bookmarks_addr);
                            ImageView bttn_delete = n_layout.findViewById(R.id.bttn_delete);

                            // fragment_favorite_sub에 있는 텍스트뷰 아이디 갖고와서 파이어스토어에 저장된 데이터들 넣어서 출력하기
                            bookmarks_title.setText(title);
                            bookmarks_addr.setText(addr);

                            LinearLayout lists = n_layout.findViewById(R.id.sub_lists);
                            bookmarks_list.addView(n_layout);
                            bookmark_count();
                        }
                    });






                    marker.setId("marker_"+title);
                    marker.setTMapPoint(latitude, longitude);
                    marker.setIcon(BitmapFactory.decodeResource(getResources(),R.drawable.search_bookmark2_icon));
                    tMapView.addTMapMarkerItem(marker);
                    exist=true;


//                    ((Bottom_Favorite) Bottom_Favorite.mContext).set_bookmarkList();
                } else {
                    star.setImageResource(R.drawable.location_inform_star_empty);

                    docR.collection("즐겨찾기").document(title).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String get_locname = document.getString("location_name");
                                    if (get_locname.equals(title)) {
                                        docR.collection("즐겨찾기").document(title).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                String id = "marker_"+title;
                                                tMapView.removeTMapMarkerItem(id);
                                                bookmark_count();
                                                int n = bookmarks_list.getChildCount();
                                                Log.d(getTag(),"존재하는 즐겨찾기 리스트: "+n);
                                                bookmarks_list.removeAllViewsInLayout();
                                                set_bookmarkList();

                                            }
                                        });


                                    }
                                }
                            }
                        }
                    });


                }


//                num++;
            }
        });
    }

    private void bookmark_count() {
        // 데이터베이스에 데이터가 몇 개 있는지 확인하는 코드
        Query query = firestore.collection("즐겨찾기DB").document(email).collection("즐겨찾기");
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
                    int num = Integer.parseInt(number);

                    bottomFavorite.list_num.setText(String.valueOf(num)+"개");
                    Log.d(getTag(), "num: " + num);


                } else {
                    Log.d(getTag(), "Count failed: ", task.getException());
                }
            }
        });

    }

    public void set_bookmarkList() {
        DocumentReference docR = firestore.collection("즐겨찾기DB").document(email);
        TMapMarkerItem marker_bookmarker = new TMapMarkerItem();



        Task<QuerySnapshot> docRef = firestore.collection("즐겨찾기DB").document(email).collection("즐겨찾기").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                    String title = ds.getString("location_name");
                    String addr = ds.getString("address");

                    TMapView tMapView = ((mapview) getActivity()).tMapView;

                    String id = ds.getId();

                    n_layout = new Sub(getActivity().getApplicationContext());


                    // n_layout(=fragment_favorite_sub) 복제본을 넣을 즐겨찾기(fragment_bottom__favorite.xml) 내부 장소

                    TextView bookmarks_title = n_layout.findViewById(R.id.bookmarks_title);
                    TextView bookmarks_addr = n_layout.findViewById(R.id.bookmarks_addr);
                    ImageView bttn_delete = n_layout.findViewById(R.id.bttn_delete);

                    // fragment_favorite_sub에 있는 텍스트뷰 아이디 갖고와서 파이어스토어에 저장된 데이터들 넣어서 출력하기
                    bookmarks_title.setText(title);
                    bookmarks_addr.setText(addr);

                    LinearLayout lists = n_layout.findViewById(R.id.sub_lists);
                    lists.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String bookmarks_title_str = (String) bookmarks_title.getText();
//                                                Toast.makeText(getActivity().getApplicationContext(), "클릭 장소: "+bookmarks_title_str+" 넘버: "+j_str, Toast.LENGTH_SHORT).show();

                            DocumentReference get_doc = docR.collection("즐겨찾기").document(id);
                            get_doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot document_get = task.getResult();
                                    String get_title = document_get.getString("location_name");
                                    String get_addr = document_get.getString("address");
                                    Double get_latitude = document_get.getDouble("latitude");
                                    Double get_longitude = document_get.getDouble("longitude");

                                    Bundle bundle = new Bundle();
                                    bundle.putString("loc_name", get_title);
                                    bundle.putString("loc_addr", get_addr);
                                    bundle.putDouble("loc_lat", get_latitude);
                                    bundle.putDouble("loc_lon", get_longitude);

                                    Bottom_LocationInform bottom_locationInform = new Bottom_LocationInform();
                                    bottom_locationInform.setArguments(bundle);
                                    ((mapview) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.loc_layout, bottom_locationInform).commit();


                                    tMapView.setCenterPoint(get_latitude, get_longitude, true);


                                    String mk_id = marker_bookmarker.getId();
                                    Log.d(getTag(), "즐겨찾기 마커: "+mk_id);

                                    if (mk_id!=null && mk_id.equals("bookmarker")) {
                                        tMapView.removeTMapMarkerItem("bookmarker");
                                    }
                                    marker_bookmarker.setId("bookmarker");
                                    marker_bookmarker.setTMapPoint(get_latitude, get_longitude);
                                    marker_bookmarker.setIcon(BitmapFactory.decodeResource(getResources(),R.drawable.search_gps_icon));
                                    tMapView.addTMapMarkerItem(marker_bookmarker);

                                    FrameLayout loc_layout = ((mapview) getActivity()).findViewById(R.id.loc_layout);
                                    FrameLayout main_content = ((mapview) getActivity()).findViewById(R.id.main_content);
                                    main_content.setVisibility(View.GONE);
                                    loc_layout.setVisibility(View.VISIBLE);
                                    FrameLayout tmaplayout = ((mapview) getActivity()).findViewById(R.id.tmap_layout);


                                    BottomSheetBehavior bottomSheetBehavior_loc = BottomSheetBehavior.from(loc_layout);
                                    bottomSheetBehavior_loc.setPeekHeight(getResources().getDimensionPixelSize(R.dimen.main_layout_height));
                                    int screenHeight = getResources().getDisplayMetrics().heightPixels;

                                    int mainLayoutHeight = screenHeight - 400;
                                    tmaplayout.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mainLayoutHeight));

                                    bottomSheetBehavior_loc.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
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

                                }
                            });
                        }
                    });

                    bttn_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            docR.collection("즐겨찾기").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    String id = bookmarks_title.getText().toString();
                                    tMapView.removeTMapMarkerItem("marker_"+id);
                                    layout = (View) view.getParent().getParent();

                                    delete_lists();
                                }
                            });



                        }
                    });

                    // 파이어스토어 데이터를 넣어서 새로 제작한 n_layout(=fragment_favorite_sub) 즐겨찾기(fragment_bottom__favorite.xml)에 있는 bookmarks_list에 추가하기

                    bookmarks_list.addView(n_layout);
                }

            }
        });
    }
    public void delete_lists() {
        bookmark_count();
        View sub_list = layout.findViewById(R.id.sub_lists);
        sub_list.setVisibility(View.GONE);
    }


}