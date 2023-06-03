package com.example.bigmap.bottom;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.example.bigmap.R;
import com.example.bigmap.bookmarks.Sub;
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
    public static Context mContext;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    int num;
    String title;
    String addr;
    Double latitude;
    Double longitude;
    public static LinearLayout bookmarks_list;
    Bundle bundle;
    Sub n_layout;

    String get_title;
    String get_addr;
    Double get_latitude;
    Double get_longitude;
    int count = 0;
    int layout_num = 1;
    TMapView tMapView;
    String email;
    View layout;
    public static TextView list_num;

    //즐겨찾기 bottom sheet
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_bottom__favorite, container, false);

        list_num = rootview.findViewById(R.id.list_num);
        rootview.setTag("bottom_favorite_tag");
        return rootview;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list_num = view.findViewById(R.id.list_num);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        email = user.getEmail();
        bookmarks_list = view.findViewById(R.id.bookmarks_list);



        set_bookmarkList();

        bookmark_count();


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

                    list_num.setText(String.valueOf(num)+"개");
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
                    title = ds.getString("location_name");
                    addr = ds.getString("address");
                    latitude = ds.getDouble("latitude");
                    longitude = ds.getDouble("longitude");

                    tMapView = ((mapview) getActivity()).tMapView;

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
                                    ((mapview) getActivity()).home_height = ((mapview) getActivity()).mainLayoutHeight;


                                    BottomSheetBehavior bottomSheetBehavior_loc = BottomSheetBehavior.from(loc_layout);
                                    bottomSheetBehavior_loc.setPeekHeight(getResources().getDimensionPixelSize(R.dimen.main_layout_height));
                                    int screenHeight = getResources().getDisplayMetrics().heightPixels;

                                    ((mapview) getActivity()).mainLayoutHeight = screenHeight - 400;
                                    tmaplayout.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ((mapview) getActivity()).mainLayoutHeight));

                                    bottomSheetBehavior_loc.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                                        @Override
                                        public void onStateChanged(@NonNull View bottomSheet, int newState) {

                                            int bottomSheetHeight = 400;
                                            if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                                                bottomSheetHeight = (int) (bottomSheet.getHeight()) - 150;
                                            }
                                            ((mapview) getActivity()).mainLayoutHeight = screenHeight - bottomSheetHeight;
                                            tmaplayout.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ((mapview) getActivity()).mainLayoutHeight));
                                        }

                                        @Override
                                        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                                            int bottomSheetHeight = (int) (bottomSheet.getHeight() * slideOffset);
                                            ((mapview) getActivity()).mainLayoutHeight = screenHeight - bottomSheetHeight;
                                            tmaplayout.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ((mapview) getActivity()).mainLayoutHeight));
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
        /*bookmarks_list.removeAllViewsInLayout();
        set_bookmarkList();*/
    }
}