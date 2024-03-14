package com.hamidul.stockmaintain;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Stock extends Fragment {

    RecyclerView recyclerView;
    HashMap<String,String> hashMap;
    MyDatabase myDatabase;
    ProgressBar progressBar;
    public static ArrayList<HashMap<String,String>> stockList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_stock, container, false);

        recyclerView = myView.findViewById(R.id.recyclerView);
        progressBar = myView.findViewById(R.id.progressBar);
        myDatabase = new MyDatabase(getContext(),stockList,progressBar,recyclerView);

        myDatabase.viewStock();

        return myView;
    }

    @Override
    public void onPause() {
        super.onPause();
        myDatabase.viewStock();
    }

    @Override
    public void onResume() {
        super.onResume();
        myDatabase.viewStock();
    }





}