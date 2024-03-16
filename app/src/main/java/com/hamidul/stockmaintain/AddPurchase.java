package com.hamidul.stockmaintain;

import android.database.Cursor;
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
    SQLiteDatabaseHelper sqLiteDatabaseHelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_add_purchase, container, false);

        recyclerView = myView.findViewById(R.id.recyclerView);
        sqLiteDatabaseHelper = new SQLiteDatabaseHelper(getActivity());

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

        Cursor cursor = sqLiteDatabaseHelper.getAllStock();

        purchaseList = new ArrayList<>();

        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String sku = cursor.getString(1);
            int unit = cursor.getInt(2);
            double tp = cursor.getDouble(3);

            hashMap = new HashMap<>();
            hashMap.put("id", String.valueOf(id));
            hashMap.put("sku",sku);
            hashMap.put("unit", String.valueOf(unit));
            hashMap.put("tp", String.valueOf(tp));
            purchaseList.add(hashMap);

        }

        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

}