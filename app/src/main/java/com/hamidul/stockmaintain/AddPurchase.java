package com.hamidul.stockmaintain;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    ArrayList<HashMap<String,String>> purchaseList = new ArrayList<>();
    public static ArrayList<HashMap<String,String>> purchaseUnit = new ArrayList<>();
    HashMap<String,String> hashMap;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    SQLiteDatabaseHelper sqLiteDatabaseHelper;
    Button button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_add_purchase, container, false);

        recyclerView = myView.findViewById(R.id.recyclerView);
        button = myView.findViewById(R.id.button);
        sqLiteDatabaseHelper = new SQLiteDatabaseHelper(getActivity());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        loadStock();


        return myView;
    }

    public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.myViewHolder>{

        @NonNull
        @Override
        public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();
            View myView = inflater.inflate(R.layout.stock_item,parent,false);
            return new myViewHolder(myView);
        }

        @Override
        public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

            hashMap = purchaseUnit.get(position);
            String id = hashMap.get("id");
            String sku = hashMap.get("sku");
            String unit = hashMap.get("unit");

            holder.skuName.setText(sku);
            holder.skuUnit.setText(unit);

        }

        @Override
        public int getItemCount() {
            return purchaseUnit.size();
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









    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.myViewHolder>{

        @NonNull
        @Override
        public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();
            View myView = inflater.inflate(R.layout.add_item,parent,false);
            return new myViewHolder(myView);
        }

        @Override
        public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

            hashMap = purchaseList.get(position);
            String id = hashMap.get("id");
            String sku = hashMap.get("sku");
            String unit = hashMap.get("unit");

            holder.skuName.setText(sku);

            holder.edUnit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    String string = holder.edUnit.getText().toString();

                    if (!string.isEmpty()){

                        if (purchaseUnit.size()>0){

                            String mID = "";

                            for (int x=0; x<purchaseUnit.size(); x++){
                                HashMap has = purchaseUnit.get(x);
                                mID = (String) has.get("id");
                                if (mID.equals(id)){
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        has.replace("unit",string);
                                    }
                                    break;
                                }// (mID.equals(id))

                            }// end for loop

                            if (!mID.equals(id)){
                                hashMap = new HashMap<>();
                                hashMap.put("id",id);
                                hashMap.put("sku",sku);
                                hashMap.put("unit",string);
                                purchaseUnit.add(hashMap);
                            } // (!mID.equals(id))


                        }// (purchase.size()>0)
                        else {
                            hashMap = new HashMap<>();
                            hashMap.put("id",id);
                            hashMap.put("sku",sku);
                            hashMap.put("unit",string);
                            purchaseUnit.add(hashMap);
                        }

                    }// (!string.isEmpty())
                    else {

                        for (int x=0; x<purchaseUnit.size(); x++){
                            HashMap hashMap1 = purchaseUnit.get(x);
                            String id1 = (String) hashMap1.get("id");
                            if (id1.equals(id)){
                                purchaseUnit.remove(x);
                            }// (id1.equals(id))

                        }// end for loop
                    }


                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }

        @Override
        public int getItemCount() {
            return purchaseList.size();
        }

        public class myViewHolder extends RecyclerView.ViewHolder{
            TextView skuName;
            EditText edUnit;
            public myViewHolder(@NonNull View itemView) {
                super(itemView);

                skuName = itemView.findViewById(R.id.skuName);
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