package com.example.qunxin.erp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.qunxin.erp.R;

/**
 * Created by qunxin on 2019/9/9.
 */

public class ShangpinsFragment extends Fragment {

    ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shangpinliebiaos, container, false);

        listView=view.findViewById(R.id.listView);
        return view;
    }
}
