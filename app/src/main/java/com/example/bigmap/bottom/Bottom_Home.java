package com.example.bigmap.bottom;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bigmap.R;
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
import com.skt.tmap.overlay.TMapMarkerItem;


public class Bottom_Home extends Fragment {

    //home bottom sheet
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    int num;
    int j;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom__home, container, false);
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
                    for (TextView textView :recent_titles) {

                        Log.d("TAG", "j: "+ j);
                        DocumentReference read_doc = docR.collection("최근기록").document(String.valueOf(j));

                        read_doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if(document.exists()) {
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



    }
}
