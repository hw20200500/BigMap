package com.example.bigmap;

import com.skt.tmap.TMapPoint;

import java.util.ArrayList;
import java.util.List;

public class Mileage {

    public List<TMapPoint> mileage(){

        List<TMapPoint> tMapPointList = new ArrayList<>();
        // 경부선
        tMapPointList.add(new TMapPoint(36.9425, 127.1941)); // 입장거봉(서울)
        tMapPointList.add(new TMapPoint(36.7872, 127.1736)); // 천안삼거리(서울)
        tMapPointList.add(new TMapPoint(36.7162, 127.3497)); // 청주(서울)
        tMapPointList.add(new TMapPoint(36.4971, 127.4309)); // 죽암(서울)
        tMapPointList.add(new TMapPoint(36.4275, 127.418)); // 신탄진(서울)
        tMapPointList.add(new TMapPoint(36.2962, 127.6002)); // 옥천(서울)
        tMapPointList.add(new TMapPoint(36.2482, 127.8544)); // 황간(서울)
        tMapPointList.add(new TMapPoint(36.6552, 127.1809)); // 망향(부산)
        tMapPointList.add(new TMapPoint(36.7304, 127.2635)); // 천안호두(부산)
        tMapPointList.add(new TMapPoint(36.658, 127.3697)); // 옥산(부산)
        tMapPointList.add(new TMapPoint(36.4862, 127.4299)); // 죽암(부산)
        tMapPointList.add(new TMapPoint(36.2968, 127.5952)); // 옥천(부산)
        tMapPointList.add(new TMapPoint(36.2789, 127.6721)); // 금강(양)
        tMapPointList.add(new TMapPoint(36.2486, 127.8527)); // 황간(부산)

        // 중부내륙
        tMapPointList.add(new TMapPoint(36.8323, 127.96)); // 괴산(양평)
        tMapPointList.add(new TMapPoint(37.0228, 127.8397)); // 충주(양평)
        tMapPointList.add(new TMapPoint(36.8313, 127.9583)); // 괴산(창원)
        tMapPointList.add(new TMapPoint(37.0222, 127.8371)); // 충주(창원)

        // 청주상주
        tMapPointList.add(new TMapPoint(36.5426, 127.4839)); // 문의(영덕)
        tMapPointList.add(new TMapPoint(36.5443, 127.4844)); // 문의(청주)
        tMapPointList.add(new TMapPoint(36.4477, 127.8691)); // 속리산(청주)

        // 서해안
        tMapPointList.add(new TMapPoint(36.9448, 126.8075)); // 행당돔(양)
        tMapPointList.add(new TMapPoint(36.7342, 126.5663)); // 서산(서울)
        tMapPointList.add(new TMapPoint(36.5532, 126.582)); // 홍성(서울)
        tMapPointList.add(new TMapPoint(36.3743, 126.5583)); // 대천(서울)
        tMapPointList.add(new TMapPoint(36.1319, 126.6279)); // 서천(서울)
        tMapPointList.add(new TMapPoint(36.743, 126.5651)); // 서산(목포)
        tMapPointList.add(new TMapPoint(36.5532, 126.582)); // 홍성(목포)
        tMapPointList.add(new TMapPoint(36.3743, 126.5583)); // 대천(목포)
        tMapPointList.add(new TMapPoint(36.1296, 126.6265)); // 서천(목포)

        // 통영대전
        tMapPointList.add(new TMapPoint(36.155, 127.4945)); // 인삼랜드(하남)
        tMapPointList.add(new TMapPoint(36.155, 127.4975)); // 인삼랜드(통영)

        // 중부선
        tMapPointList.add(new TMapPoint(36.7566, 127.482)); // 오창(하남)
        tMapPointList.add(new TMapPoint(37.022, 127.4844)); // 음성(하남)
        tMapPointList.add(new TMapPoint(36.7586, 127.4818)); // 오창(통영)
        tMapPointList.add(new TMapPoint(37.0196, 127.4807)); // 음성(통영)
        return tMapPointList;
    }
    public List<String> mileagename(){

        List<String> mileagename = new ArrayList<>();

        // 휴게소명 추가
        mileagename.add("입장거봉 휴게소 (서울 방향)");
        mileagename.add("천안삼거리 휴게소 (서울 방향)");
        mileagename.add("청주 휴게소 (서울 방향)");
        mileagename.add("죽암 휴게소 (서울 방향)");
        mileagename.add("신탄진 휴게소 (서울 방향)");
        mileagename.add("옥천 휴게소(서울 방향)");
        mileagename.add("황간 휴게소 (서울 방향)");
        mileagename.add("망향 휴게소 (부산 방향)");
        mileagename.add("천안호두 휴게소(부산 방향)");
        mileagename.add("옥산 휴게소 (부산 방향)");
        mileagename.add("죽암 휴게소(부산 방향)");
        mileagename.add("옥천 휴게소(부산 방향)");
        mileagename.add("금강 휴게소(부산 방향)");
        mileagename.add("황간 휴게소(부산 방향)");


        mileagename.add("괴산 휴게소 (양평 방향)");
        mileagename.add("충주 휴게소 (양평 방향)");
        mileagename.add("괴산 휴게소 (창원 방향)");
        mileagename.add("충주 휴게소 (창원 방향)");


        mileagename.add("문의 휴게소 (영덕 방향)");
        mileagename.add("문의(청주 방향)");
        mileagename.add("속리산(청주 방향)");


        mileagename.add("행담도 휴게소");
        mileagename.add("서산 휴게소 (서울 방향)");
        mileagename.add("홍성 휴게소 (서울 방향)");
        mileagename.add("대천 휴게소 (서울 방향)");
        mileagename.add("서천 휴게소 (서울 방향)");
        mileagename.add("서산 휴게소 (목포 방향)");
        mileagename.add("홍성 휴게소 (목포 방향)");
        mileagename.add("대천 휴게소 (목포 방향)");
        mileagename.add("서천 휴게소 (목포 방향)");


        mileagename.add("인삼랜드 휴게소 (하남 방향)");
        mileagename.add("인삼랜드 휴게소 (통영 방향)");


        mileagename.add("오창 휴게소 (하남 방향)");
        mileagename.add("음성 휴게소 (하남 방향)");
        mileagename.add("오창 휴게소 (통영 방향)");
        mileagename.add("음성 휴게소(통영 방향)");
        return mileagename;
    }

}
