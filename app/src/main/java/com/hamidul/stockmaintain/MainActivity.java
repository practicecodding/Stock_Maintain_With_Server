package com.hamidul.stockmaintain;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.FormattableFlags;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    MaterialToolbar materialToolbar;
    public static BottomNavigationView bottomNavigationView;
    BroadcastReceiver broadcastReceiver;
    Toast toast;
    Dialog dialog;
    public static boolean firstTime = true;
    boolean skuDiffer = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);

        SplashScreen.onSplash = true;

        materialToolbar = findViewById(R.id.materialToolbar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        firstTime = true;

        broadcastReceiver = new InternetConnection();
        registerReceiver();

        bottomNavigationView.setSelectedItemId(R.id.stock);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,new Stock());
        fragmentTransaction.commit();

        //=================================================================================================

        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.no_internet);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);

        //==================================================================================================
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId()==R.id.stock){
                    materialToolbar.setTitle("Stock");
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout,new Stock());
                    fragmentTransaction.commit();
                }
                else if (menuItem.getItemId()==R.id.sell){
                    materialToolbar.setTitle("Sell");
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout,new Sell());
                    fragmentTransaction.commit();
                }else if (menuItem.getItemId()==R.id.purchase){
                    materialToolbar.setTitle("Purchase");
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout,new Purchase());
                    fragmentTransaction.commit();
                }

                return true;
            }
        });

        //=================================================================================================

        materialToolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (materialToolbar.getTitle().equals("Purchase")){
                    final Dialog d = new Dialog(MainActivity.this);
                    d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    d.setContentView(R.layout.item);

                    TextInputLayout skuLayout = d.findViewById(R.id.skuLayout);
                    EditText edSKU = d.findViewById(R.id.edSKU);
                    EditText edUnit = d.findViewById(R.id.edUnit);
                    EditText edTP = d.findViewById(R.id.edTP);
                    Button button = d.findViewById(R.id.button);


                    TextWatcher watcher = new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            String sku = edSKU.getText().toString();
                            String unit = edUnit.getText().toString();
                            String tp = edTP.getText().toString();

                            for (HashMap item : Stock.stockList){
                                String itemSku = (String) item.get("sku");
                                if (itemSku.replaceAll("\\s","").toLowerCase().equals(sku.replaceAll("\\s","").toLowerCase())){
                                    skuLayout.setError("This SKU Already Exists");
                                    skuDiffer = false;
                                    break;
                                }else {
                                    skuLayout.setError(null);
                                    skuLayout.setErrorEnabled(false);
                                    skuDiffer = true;
                                }

                            }//for loop end

                            button.setEnabled(!sku.isEmpty() && !unit.isEmpty() && !tp.isEmpty() && skuDiffer);

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            String string = s.toString();
                            if ( !string.isEmpty() && string.startsWith("0") ){
                                s.delete(0,1);
                            }
                        }
                    };

                    edSKU.addTextChangedListener(watcher);
                    edUnit.addTextChangedListener(watcher);
                    edTP.addTextChangedListener(watcher);

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            d.cancel();

                            String sku = edSKU.getText().toString();
                            String  unit = edUnit.getText().toString();
                            String tp = edTP.getText().toString();

                            String url = "https://smhamidulcodding.000webhostapp.com/stock_maintain/stock/insert.php?s="+sku+"&u="+unit+"&t="+tp;

                            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {

                                    setToast("new sku added successfully");

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {

                                }
                            });

                            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                            requestQueue.add(stringRequest);

                            bottomNavigationView.setSelectedItemId(R.id.stock);
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.frameLayout,new Stock());
                            fragmentTransaction.commit();

                        }


                    });

                    d.show();
                    d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                    d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    d.getWindow().setWindowAnimations(R.style.DialogAnimation);
                    d.getWindow().setGravity(Gravity.BOTTOM);

                }

                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (materialToolbar.getTitle().equals("Stock")){
            super.onBackPressed();
        }
        else {
            bottomNavigationView.setSelectedItemId(R.id.stock);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout,new Stock());
            fragmentTransaction.commit();
        }
    }

    public class InternetConnection extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            if (isConnected()){
                dialog.cancel();
                bottomNavigationView.setSelectedItemId(R.id.stock);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout,new Stock());
                fragmentTransaction.commit();
            }
            else {
                dialog.show();
            }
        }

        public boolean isConnected (){

            try {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return  (networkInfo!=null && networkInfo.isConnected());
            }
            catch (NullPointerException e){
                e.printStackTrace();
                return false;
            }
        }


    }//========================

    protected void registerReceiver(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterReceiver(){
        try {
            unregisterReceiver(broadcastReceiver);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }

    public void setToast(String text){
        if (toast!=null) toast.cancel();
        toast = Toast.makeText(MainActivity.this,text,Toast.LENGTH_SHORT);
        toast.show();
    }


}