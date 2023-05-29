package com.example.bigmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.skt.tmap.TMapData;
import com.skt.tmap.poi.TMapPOIItem;

import java.util.ArrayList;

public class Test extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 제거 코드 입니다.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        test();
    }

    private void test(){
        String searchText = "호서대학교";
        TMapData tmapdata = new TMapData();
        tmapdata.findAllPOI(searchText, 5, new TMapData.OnFindAllPOIListener() {
            @Override
            public void onFindAllPOI(ArrayList<TMapPOIItem> poiitems) {
                for(int i = 0;i<poiitems.size();i++){
                    TMapPOIItem poiItem = poiitems.get(i);
                    System.out.println(i+":"+poiItem.getPOIName());
                }

            }
        });
    }

}
