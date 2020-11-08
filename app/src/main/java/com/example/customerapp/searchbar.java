package com.example.customerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customerapp.api.Constants;
import com.example.customerapp.api.EndlessRecyclerOnScrollListener;
import com.example.customerapp.api.FilterDialog;
import com.example.customerapp.api.GothamTextView;
import com.example.customerapp.api.RestaurantAdapter;
import com.example.customerapp.api.RestaurantModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class searchbar extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ImageView searchImg;
    private EditText searchEdt;
    private RecyclerView searchRv;
    private RecyclerView.LayoutManager layoutManager;
    private String searchInput;
    TextView location23,fht,tkms,Mtkms,fp,ftp,Mtp;
    private FusedLocationProviderClient fusedLocationProviderClient;

    SearchView searchView;
    ArrayList<RestaurantModel> list;
    ProgressDialog dialog;
    RestaurantAdapter adapter;
    RecyclerView rv;
    int total_count=0;
    String qu;
    GothamTextView tvIndicator;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbar);
        fht=findViewById(R.id.fht);
        tkms=findViewById(R.id.tkms);
        Mtkms=findViewById(R.id.Mtkms);
        fp=findViewById(R.id.fp);
        ftp=findViewById(R.id.ftp);
        Mtp=findViewById(R.id.Mtp);
        fht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchRv.clearAnimation();
                fht.setBackground(getResources().getDrawable(R.drawable.text_box2));
                tkms.setBackground(getResources().getDrawable(R.drawable.text_box));
                Mtkms.setBackground(getResources().getDrawable(R.drawable.text_box));
                DatabaseReference searchref = FirebaseDatabase.getInstance().getReference().child("Restaurants");
                final Query query=searchref.orderByChild("distance").startAt("0").endAt("0.5");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       filterData(query);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        tkms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tkms.setBackground(getResources().getDrawable(R.drawable.text_box2));
                fht.setBackground(getResources().getDrawable(R.drawable.text_box));
                Mtkms.setBackground(getResources().getDrawable(R.drawable.text_box));
                DatabaseReference searchref = FirebaseDatabase.getInstance().getReference().child("Restaurants");
                final Query query=searchref.orderByChild("distance").startAt("0.5").endAt("2");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        filterData(query);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        Mtkms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mtkms.setBackground(getResources().getDrawable(R.drawable.text_box2));
                tkms.setBackground(getResources().getDrawable(R.drawable.text_box));
                fht.setBackground(getResources().getDrawable(R.drawable.text_box));
                DatabaseReference searchref = FirebaseDatabase.getInstance().getReference().child("Restaurants");
                final Query query=searchref.orderByChild("distance").startAt("2.1");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        filterData(query);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        fp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fp.setBackground(getResources().getDrawable(R.drawable.text_box2));
                ftp.setBackground(getResources().getDrawable(R.drawable.text_box));
                Mtp.setBackground(getResources().getDrawable(R.drawable.text_box));
                DatabaseReference searchref = FirebaseDatabase.getInstance().getReference().child("Restaurants");
                final Query query=searchref.orderByChild("NOPLive").startAt("0").endAt("4");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        filterData(query);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        ftp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ftp.setBackground(getResources().getDrawable(R.drawable.text_box2));
                fp.setBackground(getResources().getDrawable(R.drawable.text_box));
                Mtp.setBackground(getResources().getDrawable(R.drawable.text_box));
                DatabaseReference searchref = FirebaseDatabase.getInstance().getReference().child("Restaurants");
                final Query query=searchref.orderByChild("NOPBefore").startAt("85").endAt("95");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        filterData(query);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        Mtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mtp.setBackground(getResources().getDrawable(R.drawable.text_box2));
                ftp.setBackground(getResources().getDrawable(R.drawable.text_box));
                fp.setBackground(getResources().getDrawable(R.drawable.text_box));
                DatabaseReference searchref = FirebaseDatabase.getInstance().getReference().child("Restaurants");
                final Query query=searchref.orderByChild("NOPLive").startAt("10.1");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        filterData(query);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        location23=findViewById(R.id.location_txt_detail);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        searchImg=(ImageView)findViewById(R.id.search_img);
        searchEdt=(EditText)findViewById(R.id.search_edt_txt);
        searchRv=(RecyclerView)findViewById(R.id.search_rv);
        searchRv.setLayoutManager(new LinearLayoutManager(searchbar.this));
        searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInput=searchEdt.getText().toString();
                onStart();
            }
        });
        tvIndicator=(GothamTextView)findViewById(R.id.tv_indicator);
        dialog=new ProgressDialog(searchbar.this);
        dialog.setTitle("Loading");
        dialog.setMessage("Fetching data from server");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        list=new ArrayList<>();
        adapter=new RestaurantAdapter(list);


        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
//        rv=(RecyclerView) findViewById(R.id.rv_restaurants);
//        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
//        rv.setLayoutManager(layoutManager);
//        rv.setAdapter(adapter);
//        rv.setOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
//            @Override
//            public void onLoadMore(int current_page) {
//                if (total_count<=current_page) // If all the restaurants has been added to the list, do nothing
//                {
//
//                }
//                else
//                {
//                    new LoadMoreTask().execute(qu,current_page+"");
//                }
//            }
//        });
//        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
//        toolbar.setTitleTextColor(Color.parseColor("#dcd9cd"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED){
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location!=null){
                                    Geocoder geocoder=new Geocoder(searchbar.this, Locale.getDefault());
                                    try {
                                        List<Address> addresses= geocoder.getFromLocation(
                                                location.getLatitude(),location.getLongitude(),
                                                1
                                        );
                                        location23.setText(addresses.get(0).getSubLocality());
                                    } catch (IOException e) {
                                        Toast.makeText(searchbar.this, "Exception occured", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                    Double lat=location.getLatitude();
                                    Double longtt=location.getLatitude();
                                }
                                else {
                                    Toast.makeText(searchbar.this, "Device location is turned off", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
            }
        }
        DatabaseReference searchref = FirebaseDatabase.getInstance().getReference().child("Restaurants");
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(searchref.orderByChild("PName").startAt(searchInput),Products.class)
                .build();
        FirebaseRecyclerAdapter<Products, SearchVH> adapter = new FirebaseRecyclerAdapter<Products, SearchVH>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SearchVH productViewHolder, int i, @NonNull final Products products) {
                productViewHolder.textSearchName.setText(products.getPName());
                productViewHolder.textSearchDist.setText(products.getDistance()+" kms");
                productViewHolder.txtSearchPeople.setText(products.getNOPLive()+" peoples");
                Picasso.get().load(products.getImage()).into(productViewHolder.ProductImage);
                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent detintent=new Intent(searchbar.this,RestaurantDetails.class);
                        detintent.putExtra("pid",products.getPid());
                        startActivity(detintent);
                    }
                });
            }

            @NonNull
            @Override
            public SearchVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_card,parent,false);
                SearchVH holder=new SearchVH(view);
                return holder;
            }
        };
        searchRv.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_home,menu);
        searchView= (SearchView) MenuItemCompat.getActionView(menu.getItem(0));
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        new FetchTask().execute(query);
        qu=query;
        searchView.setIconified(true);
        invalidateOptionsMenu();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
    public void processResponse(JSONObject obj) throws JSONException {
        JSONArray jsonArray=obj.getJSONArray("restaurants");
        total_count=obj.getInt("results_found");
        Log.d("Total",total_count+"");
        if (jsonArray.length()!=0)
        {
            tvIndicator.setVisibility(View.GONE);
        }
        else
        {
            tvIndicator.setVisibility(View.VISIBLE);
        }
        for (int i=0;i<jsonArray.length();i++)
        {
            JSONObject object=jsonArray.getJSONObject(i).getJSONObject("restaurant");
            RestaurantModel model=new RestaurantModel();
            model.setName(object.getString("name"));
            JSONObject lobj=object.getJSONObject("location");
            model.setAdd(lobj.getString("address"));
            model.setLocation(lobj.getString("city"));
            Log.d("CITY",lobj.getString("city"));
            JSONObject userObj=object.getJSONObject("user_rating");
            model.setRating(userObj.getDouble("aggregate_rating"));
            model.setCuisine(object.getString("cuisines"));
            list.add(model);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    class FetchTask extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String s;
            HttpURLConnection urlConnection= null;
            BufferedReader bufferedReader=null;
            StringBuilder builder=new StringBuilder();

            try {

                Uri uri=Uri.parse(Constants.BASE_URL).buildUpon().appendQueryParameter("q",strings[0]).build();
                URL url=new URL(uri.toString());
                Log.d("site",uri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("user-key",Constants.API_KEY);

                InputStream in=urlConnection.getInputStream();
                bufferedReader =new BufferedReader(new InputStreamReader(in));
                while ((s=bufferedReader.readLine())!=null)
                {
                    builder.append(s);
                }}
            catch (IOException e)
            {
                e.printStackTrace();
            }

            finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return builder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            try {
                list.clear();
                processResponse(new JSONObject(s));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    class LoadMoreTask extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String s;
            HttpURLConnection urlConnection= null;
            BufferedReader bufferedReader=null;
            StringBuilder builder=new StringBuilder();

            try {

                Uri uri=Uri.parse(Constants.BASE_URL).buildUpon().appendQueryParameter("q",strings[0]).appendQueryParameter("start",strings[1]).build();
                URL url=new URL(uri.toString());
                Log.d("site",uri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("user-key",Constants.API_KEY);

                InputStream in=urlConnection.getInputStream();
                bufferedReader =new BufferedReader(new InputStreamReader(in));
                while ((s=bufferedReader.readLine())!=null)
                {
                    builder.append(s);
                }}
            catch (IOException e)
            {
                e.printStackTrace();
            }

            finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return builder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            try {
                processResponse(new JSONObject(s));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    void filterData(Query query)
    {
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(query,Products.class)
                .build();
        FirebaseRecyclerAdapter<Products, SearchVH> adapter = new FirebaseRecyclerAdapter<Products, SearchVH>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SearchVH productViewHolder, int i, @NonNull final Products products) {
                productViewHolder.textSearchName.setText(products.getPName());
                productViewHolder.textSearchDist.setText(products.getDistance()+" kms");
                productViewHolder.txtSearchPeople.setText(products.getNOPLive()+" peoples");
                Picasso.get().load(products.getImage()).into(productViewHolder.ProductImage);
                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent detintent=new Intent(searchbar.this,RestaurantDetails.class);
                        detintent.putExtra("pid",products.getPid());
                        startActivity(detintent);
                    }
                });
            }

            @NonNull
            @Override
            public SearchVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_card,parent,false);
                SearchVH holder=new SearchVH(view);
                return holder;
            }
        };
        searchRv.setAdapter(adapter);
        adapter.startListening();
    }

}
