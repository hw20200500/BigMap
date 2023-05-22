package com.example.bigmap.bottom;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bigmap.MainActivity;
import com.example.bigmap.R;

import org.w3c.dom.Text;

public class Bottom_LocationInform extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location_inform, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView star = view.findViewById(R.id.bttn_star);
        TextView loc_title = view.findViewById(R.id.loc_title);
        TextView loc_addr = view.findViewById(R.id.loc_addr);


        String title = this.getArguments().getString("loc_name");
        String addr = this.getArguments().getString("loc_addr");
        Double latitude = this.getArguments().getDouble("loc_lat");
        Double longitude = this.getArguments().getDouble("loc_lon");
        //"loc_lon", longitude

        loc_title.setText(title);
        loc_addr.setText(addr);

        Button bttn_start = view.findViewById(R.id.bttn_start);
        Button bttn_dest = view.findViewById(R.id.bttn_dest);

        bttn_dest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_set_dest = new Intent(getActivity(), MainActivity.class);
                intent_set_dest.putExtra("loc_name", title);
                intent_set_dest.putExtra("loc_addr", addr);
                intent_set_dest.putExtra("loc_lat", latitude);
                intent_set_dest.putExtra("loc_lon", longitude);
                intent_set_dest.putExtra("title", "destination");

                startActivity(intent_set_dest);
            }
        });

        bttn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_set_dest = new Intent(getActivity(), MainActivity.class);
                intent_set_dest.putExtra("loc_name", title);
                intent_set_dest.putExtra("loc_addr", addr);
                intent_set_dest.putExtra("loc_lat", latitude);
                intent_set_dest.putExtra("loc_lon", longitude);
                intent_set_dest.putExtra("title", "start");

                startActivity(intent_set_dest);
            }
        });
    }
}