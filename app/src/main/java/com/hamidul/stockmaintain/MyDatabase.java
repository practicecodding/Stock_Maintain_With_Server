package com.hamidul.stockmaintain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class MyDatabase {

    Context context;
    ArrayList<HashMap<String,String>> arrayList;
    HashMap<String,String> hashMap;
    MyAdapter myAdapter;
    ProgressBar progressBar;
    RecyclerView recyclerView;

    public MyDatabase(Context context,ArrayList<HashMap<String,String>> arrayList,ProgressBar progressBar,RecyclerView recyclerView){

        this.context = context;
        this.arrayList = arrayList;
        this.progressBar = progressBar;
        this.recyclerView = recyclerView;

    }


    public void viewStock(){

        arrayList = new ArrayList<>();

        progressBar.setVisibility(View.VISIBLE);

        String url = "https://smhamidulcodding.000webhostapp.com/stock_maintain/stock/view.php";

        JsonArrayRequest jsonArrayRequest  = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {

                progressBar.setVisibility(View.GONE);

                arrayList = new ArrayList<>();

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
                        arrayList.add(hashMap);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }// end for loop

                myAdapter = new MyAdapter();
                recyclerView.setAdapter(myAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressBar.setVisibility(View.GONE);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(20*1000,2,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonArrayRequest);

    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.myViewHolder>{

        @NonNull
        @Override
        public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myView = inflater.inflate(R.layout.stock_item,parent,false);
            return new myViewHolder(myView);
        }

        @Override
        public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

            HashMap hashMap = arrayList.get(position);

            String id = (String) hashMap.get("id");
            String sku = (String) hashMap.get("sku");
            String unit = (String) hashMap.get("unit");

            holder.skuName.setText(sku);
            holder.skuUnit.setText(unit);


        }

        @Override
        public int getItemCount() {
            return arrayList.size();
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
