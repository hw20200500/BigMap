package com.example.bigmap.bottom;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bigmap.R;
import com.example.bigmap.bookmarks.Sub;
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

public class Bottom_Favorite extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    int num;
    String title;
    String addr;
    Double latitude;
    Double longitude;

    //즐겨찾기 bottom sheet
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom__favorite, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout bookmarks_list = view.findViewById(R.id.bookmarks_list);
        TextView list_num = view.findViewById(R.id.list_num);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String email = user.getEmail();

        DocumentReference docR = firestore.collection("즐겨찾기DB").document(email);




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
                                        title = document.getString("location_name");
                                        addr = document.getString("address");
                                        latitude = document.getDouble("latitude");
                                        longitude = document.getDouble("longitude");

                                        // Sub 불러와서 linearlayout 여러개 복제하는 코드
                                        Sub n_layout = new Sub(getActivity().getApplicationContext());

                                        // n_layout(=fragment_favorite_sub) 복제본을 넣을 즐겨찾기(fragment_bottom__favorite.xml) 내부 장소
                                        LinearLayout bookmarks_list = view.findViewById(R.id.bookmarks_list);

                                        // fragment_favorite_sub에 있는 텍스트뷰 아이디 갖고와서 파이어스토어에 저장된 데이터들 넣어서 출력하기
                                        TextView bookmarks_title = n_layout.findViewById(R.id.bookmarks_title);
                                        TextView bookmarks_addr = n_layout.findViewById(R.id.bookmarks_addr);
                                        bookmarks_title.setText(title);
                                        bookmarks_addr.setText(addr);

                                        // 파이어스토어 데이터를 넣어서 새로 제작한 n_layout(=fragment_favorite_sub) 즐겨찾기(fragment_bottom__favorite.xml)에 있는 bookmarks_list에 추가하기
                                        bookmarks_list.addView(n_layout);
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



    }
}