package com.example.bigmap.bottom;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.example.bigmap.R;
import com.example.bigmap.bookmarks.Sub;
import com.example.bigmap.login_register.Login_idfind;
import com.example.bigmap.login_register.foundId;
import com.example.bigmap.mapview;
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

public class Bottom_Favorite extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    int num;
    String title;
    String addr;
    Double latitude;
    Double longitude;
    LinearLayout bookmarks_list;
    Bundle bundle;
    Sub n_layout;

    String get_title;
    String get_addr;
    Double get_latitude;
    Double get_longitude;
    int count = 0;

    //즐겨찾기 bottom sheet
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom__favorite, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView list_num = view.findViewById(R.id.list_num);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String email = user.getEmail();

        DocumentReference docR = firestore.collection("즐겨찾기DB").document(email);
        TMapMarkerItem marker_bookmarker = new TMapMarkerItem();


        Task<QuerySnapshot> docRef = firestore.collection("즐겨찾기DB").document(email).collection("즐겨찾기").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                    title = ds.getString("location_name");
                    addr = ds.getString("address");
                    latitude = ds.getDouble("latitude");
                    longitude = ds.getDouble("longitude");

                    String id = ds.getId();

                    n_layout = new Sub(getActivity().getApplicationContext());

                    // n_layout(=fragment_favorite_sub) 복제본을 넣을 즐겨찾기(fragment_bottom__favorite.xml) 내부 장소
                    bookmarks_list = view.findViewById(R.id.bookmarks_list);
                    TextView bookmarks_title = n_layout.findViewById(R.id.bookmarks_title);
                    TextView bookmarks_addr = n_layout.findViewById(R.id.bookmarks_addr);

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
                                    get_title = document_get.getString("location_name");
                                    get_addr = document_get.getString("address");
                                    get_latitude = document_get.getDouble("latitude");
                                    get_longitude = document_get.getDouble("longitude");

                                    bundle = new Bundle();
                                    bundle.putString("loc_name", get_title);
                                    bundle.putString("loc_addr", get_addr);
                                    bundle.putDouble("loc_lat", get_latitude);
                                    bundle.putDouble("loc_lon", get_longitude);

                                    Bottom_LocationInform bottom_locationInform = new Bottom_LocationInform();
                                    bottom_locationInform.setArguments(bundle);
                                    ((mapview) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.loc_layout, bottom_locationInform).commit();

                                    TMapView tMapView = ((mapview) getActivity()).tMapView;
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

                    // 파이어스토어 데이터를 넣어서 새로 제작한 n_layout(=fragment_favorite_sub) 즐겨찾기(fragment_bottom__favorite.xml)에 있는 bookmarks_list에 추가하기
                    bookmarks_list.addView(n_layout);
                }

            }
        });


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
                    TextView list_num = view.findViewById(R.id.list_num);
                    list_num.setText(String.valueOf(num)+"개");
                    Log.d(getTag(), "num: " + num);


                    /*for (int j=0; j<=num; j++) {
                        Log.d("TAG", "j: "+j);
                        DocumentReference read_doc = docR.collection("즐겨찾기").document(String.valueOf(j));
                        int finalJ = j;
                        read_doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if(document.exists()) {
                                        title = document.getString("location_name");
                                        addr = document.getString("address");
                                        latitude = document.getDouble("latitude");
                                        longitude = document.getDouble("longitude");

                                        n_layout = new Sub(getActivity().getApplicationContext());

                                        // n_layout(=fragment_favorite_sub) 복제본을 넣을 즐겨찾기(fragment_bottom__favorite.xml) 내부 장소
                                        bookmarks_list = view.findViewById(R.id.bookmarks_list);
                                        TextView bookmarks_title = n_layout.findViewById(R.id.bookmarks_title);
                                        TextView bookmarks_addr = n_layout.findViewById(R.id.bookmarks_addr);

                                        // fragment_favorite_sub에 있는 텍스트뷰 아이디 갖고와서 파이어스토어에 저장된 데이터들 넣어서 출력하기

                                        bookmarks_title.setText(title);
                                        bookmarks_addr.setText(addr);

                                        LinearLayout lists = n_layout.findViewById(R.id.sub_lists);
                                        lists.setOnClickListener(new View.OnClickListener() {
                                            String j_str = (String) String.valueOf(finalJ);
                                            @Override
                                            public void onClick(View view) {
                                                String bookmarks_title_str = (String) bookmarks_title.getText();
//                                                Toast.makeText(getActivity().getApplicationContext(), "클릭 장소: "+bookmarks_title_str+" 넘버: "+j_str, Toast.LENGTH_SHORT).show();

                                                DocumentReference get_doc = docR.collection("즐겨찾기").document(j_str);
                                                get_doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        DocumentSnapshot document_get = task.getResult();
                                                        get_title = document_get.getString("location_name");
                                                        get_addr = document_get.getString("address");
                                                        get_latitude = document_get.getDouble("latitude");
                                                        get_longitude = document_get.getDouble("longitude");

                                                        bundle = new Bundle();
                                                        bundle.putString("loc_name", get_title);
                                                        bundle.putString("loc_addr", get_addr);
                                                        bundle.putDouble("loc_lat", get_latitude);
                                                        bundle.putDouble("loc_lon", get_longitude);

                                                        Bottom_LocationInform bottom_locationInform = new Bottom_LocationInform();
                                                        bottom_locationInform.setArguments(bundle);
                                                        ((mapview) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.loc_layout, bottom_locationInform).commit();

                                                        TMapView tMapView = ((mapview) getActivity()).tMapView;
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

                                        // 파이어스토어 데이터를 넣어서 새로 제작한 n_layout(=fragment_favorite_sub) 즐겨찾기(fragment_bottom__favorite.xml)에 있는 bookmarks_list에 추가하기
                                        bookmarks_list.addView(n_layout);
                                    }

                                } else {
                                    Log.d("TAG", "get failed with ", task.getException());
                                }
                            }
                        });
                    }*/

                } else {
                    Log.d(getTag(), "Count failed: ", task.getException());
                }
            }
        });



    }
}