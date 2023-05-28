package com.example.bigmap.bottom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bigmap.R;
import com.example.bigmap.databinding.FragmentBoardBinding;
import com.example.bigmap.login_register.Login;
import com.example.bigmap.mypage.mypage_birth_ch;
import com.example.bigmap.mypage.mypage_email_ch;
import com.example.bigmap.mypage.mypage_name_ch;
import com.example.bigmap.mypage.mypage_phonnumCh;
import com.example.bigmap.mypage.mypage_pw_ch;
import com.example.bigmap.mypage.mypage_pw_check;
import com.example.bigmap.mypage.mypage_truckinfo_ch;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class MypageFragment extends Fragment {
    ImageView imageView;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    //마이페이지
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentBoardBinding binding = FragmentBoardBinding.inflate(getLayoutInflater());

        return inflater.inflate(R.layout.fragment_mypage, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView user_name = view.findViewById(R.id.name);
        TextView user_email = view.findViewById(R.id.email);
        TextView user_name_small = view.findViewById(R.id.user_name);
        TextView user_email_small = view.findViewById(R.id.user_email);
        TextView user_phone = view.findViewById(R.id.user_phoneNum);
        TextView user_birth = view.findViewById(R.id.user_birth);
        TextView user_truck_height = view.findViewById(R.id.car_height);
        TextView user_truck_weight = view.findViewById(R.id.car_weight);
        TextView user_truck_width = view.findViewById(R.id.car_width);
        TextView user_truck_length = view.findViewById(R.id.car_length);


        // user에 현재 로그인되어있는 사용자 정보 저장
        FirebaseUser user = firebaseAuth.getCurrentUser();
        // 사용자 정보에 저장되어 있는 email 갖고오기 (이거는 Authentication에 저장되어 있는 정보만 사용 가능. 그 이외 (예: 전화번호, 실명이름, 생년월일 등)는 사용 불가)
        String email = user.getEmail();

        DocumentReference docRef2 = firestore.collection("화물차DB").document(email);
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    System.out.println("document:"+document);
                    if (document.exists()) {
                        int height = document.getLong("높이").intValue();
                        int weight = document.getLong("무게").intValue();
                        int width = document.getLong("너비").intValue();
                        int length = document.getLong("길이").intValue();

                        user_truck_height.setText("높이:"+height+"cm");
                        user_truck_weight.setText("무게:"+weight+"kg");
                        user_truck_width.setText("너비:"+width+"cm");
                        user_truck_length.setText("길이:"+length+"cm");

                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        // 파이어스토어 데이터베이스 가져오는 코드
        DocumentReference docRef = firestore.collection("사용자DB").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //각 'field'에 해당하는 데이터 값 가져오기
                        String name = document.getString("name");
                        String email = document.getString("email");
                        String phoneNum = document.getString("phone_number");
                        int birthY = document.getLong("birth_year").intValue();
                        int birthM = document.getLong("birth_month").intValue();
                        int birthD = document.getLong("birth_day").intValue();


                        // 가져온 데이터 화면에 출력하기(이미 있는 텍스트뷰 내용을 해당 데이터 값으로 수정)
                        user_name.setText(name+"님");
                        user_email.setText(email);
                        user_name_small.setText(name);
                        user_email_small.setText(email);
                        user_phone.setText(phoneNum);
                        user_birth.setText(birthY+". "+birthM+". "+birthD+".");

                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Button button_nameC = view.findViewById(R.id.button_nameC); // 버튼 id로 찾음
        Button button_phoneC = view.findViewById(R.id.button_phoneC);
        Button button_emailC = view.findViewById(R.id.button_emailC);
        Button button_birthC = view.findViewById(R.id.button_birthC);
        Button button_truckC = view.findViewById(R.id.button_truckC);

        button_truckC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), mypage_truckinfo_ch.class); // 다른 Activity로 이동할 Intent 생성
                startActivity(intent); // Intent 실행
            }
        });
        button_nameC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), mypage_name_ch.class); // 다른 Activity로 이동할 Intent 생성
                startActivity(intent); // Intent 실행
            }
        });

        button_phoneC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), mypage_phonnumCh.class); // 다른 Activity로 이동할 Intent 생성
                startActivity(intent); // Intent 실행
            }
        });

        button_emailC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), mypage_email_ch.class); // 다른 Activity로 이동할 Intent 생성
                startActivity(intent); // Intent 실행
            }
        });

        button_birthC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), mypage_birth_ch.class); // 다른 Activity로 이동할 Intent 생성
                startActivity(intent); // Intent 실행
            }
        });

        LinearLayout password_ch = view.findViewById(R.id.password_ch);
        password_ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_pw = new Intent(getActivity(), mypage_pw_check.class);
                startActivity(intent_pw);
            }
        });

        TextView logout = view.findViewById(R.id.logout);
        TextView user_withdrawal = view.findViewById(R.id.user_withdrawal);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_logout = new Intent(getActivity(), Login.class);
                firebaseAuth.signOut();
                startActivity(intent_logout);
            }
        });

        user_withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_logout = new Intent(getActivity(), Login.class);
                docRef.delete();
                user.delete();
                Toast.makeText(getContext(), "회원 탈퇴되었습니다.", Toast.LENGTH_SHORT).show();
                startActivity(intent_logout);
            }
        });
    }

}