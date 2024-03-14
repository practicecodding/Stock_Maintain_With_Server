package com.hamidul.stockmaintain;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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


public class AddPurchase extends Fragment {

    public static ArrayList<HashMap<String,String>> purchaseList = new ArrayList<>();
    HashMap<String,String> hashMap;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    MyDatabase myDatabase;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_add_purchase, container, false);

        recyclerView = myView.findViewById(R.id.recyclerView);

        loadStock();


        return myView;
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

            hashMap = purchaseList.get(position);
            String id = hashMap.get("id");
            String sku = hashMap.get("sku");
            String unit = hashMap.get("unit");

            holder.skuName.setText(sku);
            holder.skuUnit.setVisibility(View.GONE);
            holder.edUnit.setVisibility(View.VISIBLE);

        }

        @Override
        public int getItemCount() {
            return purchaseList.size();
        }

        public class myViewHolder extends RecyclerView.ViewHolder{
            TextView skuName,skuUnit;
            EditText edUnit;
            public myViewHolder(@NonNull View itemView) {
                super(itemView);

                skuName = itemView.findViewById(R.id.skuName);
                skuUnit = itemView.findViewById(R.id.skuUnit);
                edUnit = itemView.findViewById(R.id.edUnit);

            }
        }
    }

    public void loadStock(){
        String url = "https://smhamidulcodding.000webhostapp.com/stock_maintain/stock/view.php";

        JsonArrayRequest jsonArrayRequest  = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {

                purchaseList = new ArrayList<>();

                for (int x=0; x<jsonArray.length(); x++){
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(x);

                        String id = jsonObject.getString("id");
                        String sku = jsonObject.getString("sku");
                        String unit = String.valueOf(jsonObject.getInt("unit"));
                        String tp = String.valueOf(jsonObject.getDouble("tp"));

                        hashMap = new HashMap<>();
                        hashMap.put("id",id);
                        hashMap.put("sku",sku);
                        hashMap.put("unit",unit);
                        hashMap.put("tp",tp);
                        purchaseList.add(hashMap);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }// end for loop

                myAdapter = new MyAdapter();
                recyclerView.setAdapter(myAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                progressBar.setVisibility(View.GONE);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(20*1000,2,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonArrayRequest);
    }

}