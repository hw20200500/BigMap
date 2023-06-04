package com.example.bigmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bigmap.bookmarks.Sub;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

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
    TextView text_dest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_path);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        email = firebaseAuth.getCurrentUser().getEmail();
        recent_loc_lists = findViewById(R.id.recent_loc_lists);
        bookmark_loc_lists = findViewById(R.id.bookmark_loc_lists);

        Intent get_inform_intent = getIntent();
        name_startP = get_inform_intent.getStringExtra("loc_name");
        addr_startP = get_inform_intent.getStringExtra("loc_addr");
        lat_startP = get_inform_intent.getDoubleExtra("loc_lat", 0);
        lon_startP = get_inform_intent.getDoubleExtra("loc_lon", 0);

        TextView text_startPoint = findViewById(R.id.text_startPoint);
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

    private void searching(TextView textView) {
        FrameLayout search_layout = findViewById(R.id.search_layout);
        search_layout.setVisibility(View.VISIBLE);
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
                                            String name_destP = document_get.getString("location_name");

                                            text_dest = findViewById(R.id.text_destPoint);
                                            text_dest.setText(name_destP);

                                            String addr_destP = document_get.getString("address");
                                            Double lat_destP = document_get.getDouble("latitude");
                                            Double lon_destP = document_get.getDouble("longitude");

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