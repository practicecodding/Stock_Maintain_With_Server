package com.hamidul.stockmaintain;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    ProgressBar progressBar;
    public static ArrayList<HashMap<String,String>> stockList = new ArrayList<>();
    SQLiteDatabaseHelper sqLiteDatabaseHelper;
    MyAdapter myAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_stock, container, false);

        recyclerView = myView.findViewById(R.id.recyclerView);
        progressBar = myView.findViewById(R.id.progressBar);
        sqLiteDatabaseHelper = new SQLiteDatabaseHelper(getActivity());

        updateStock();
        viewStock();

        return myView;
    }

    public void viewStock(){

        stockList = new ArrayList<>();

        Cursor cursor = sqLiteDatabaseHelper.getAllStock();

        while (cursor.moveToNext()){
            String sku = cursor.getString(1);
            String unit = String.valueOf(cursor.getInt(2));
            hashMap = new HashMap<>();
            hashMap.put("sku",sku);
            hashMap.put("unit",unit);
            stockList.add(hashMap);
        }

        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }


    @Override
    public void onPause() {
        super.onPause();
        updateStock();
        viewStock();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateStock();
        viewStock();
    }


    public void updateStock(){

        if (MainActivity.firstTime){
            progressBar.setVisibility(View.VISIBLE);
        }

        String url = "https://smhamidulcodding.000webhostapp.com/stock_maintain/stock/view.php";

        JsonArrayRequest jsonArrayRequest  = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {

                progressBar.setVisibility(View.GONE);
                MainActivity.firstTime = false;

//                stockList = new ArrayList<>();
                sqLiteDatabaseHelper.ClearTable();

                for (int x=0; x<jsonArray.length(); x++){
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(x);

                        int id = jsonObject.getInt("id");
                        String sku = jsonObject.getString("sku");
                        int unit = jsonObject.getInt("unit");
                        double tp = jsonObject.getDouble("tp");

//                        hashMap = new HashMap<>();
//                        hashMap.put("id", String.valueOf(id));
//                        hashMap.put("sku",sku);
//                        hashMap.put("unit", String.valueOf(unit));
//                        hashMap.put("tp", String.valueOf(tp));
//                        stockList.add(hashMap);

                        sqLiteDatabaseHelper.InsertStock(id,sku,unit,tp);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }// end for loop

                viewStock();
//                myAdapter = new MyAdapter();
//                recyclerView.setAdapter(myAdapter);
//                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressBar.setVisibility(View.GONE);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(20*1000,2,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonArrayRequest);

    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.myViewHolder>{

        @NonNull
        @Override
        public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();
            View myView = inflater.inflate(R.layout.stock_item,parent,false);
            return new myViewHolder(myView);
        }

        @Override
        public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

            HashMap hashMap = stockList.get(position);

            String sku = (String) hashMap.get("sku");
            String unit = (String) hashMap.get("unit");

            holder.skuName.setText(sku);
            holder.skuUnit.setText(unit);

        }

        @Override
        public int getItemCount() {
            return stockList.size();
        }

        public class myViewHolder extends RecyclerView.ViewHolder{
            TextView skuName,skuUnit;
            public myViewHolder(@NonNull View itemView) {
                super(itemView);

                skuName = itemView.findViewById(R.id.skuName);
                skuUnit = itemView.findViewById(R.id.skuUnit);

            }
        }

    }


}