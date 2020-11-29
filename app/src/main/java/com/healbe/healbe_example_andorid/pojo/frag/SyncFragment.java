package com.healbe.healbe_example_andorid.pojo.frag;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.healbe.healbe_example_andorid.R;
import com.healbe.healbe_example_andorid.pojo.ConverterJson;
import com.healbe.healbe_example_andorid.pojo.acti.GeneralActivity;
import com.healbe.healbe_example_andorid.pojo.tools.AuthenticateResponse;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SyncFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SyncFragment extends Fragment {
    private GeneralActivity general;
    private AuthenticateResponse user;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SyncFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SyncFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SyncFragment newInstance(String param1, String param2) {
        SyncFragment fragment = new SyncFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sync, container, false);

        TextView textView = (TextView) rootView.findViewById(R.id.textViewINFO);
        if(ConverterJson.checkToSubmit())
            textView.setText("Данные были cобраны " + ConverterJson.getLastTimeCreateJson() + " и отправлены.");
        else
            textView.setText("Новые данные cобраны " + ConverterJson.getLastTimeCreateJson());

        // Inflate the layout for this fragment
        return rootView;
    }


    public void onClickJSON(View view) {

    }
    public void onClickSUBMIT(View view) {

    }
}