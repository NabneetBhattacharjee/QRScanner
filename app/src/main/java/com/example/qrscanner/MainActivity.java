package com.example.qrscanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.qrscanner.Adapters.MyAdapter;
import com.example.qrscanner.Databases.DBHelper;
import com.example.qrscanner.Model.ListItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<ListItem> arrayList;
    MyAdapter myAdapter;
    DBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        helper=new DBHelper(this);
        arrayList = helper.getAllInformation();
        if(arrayList.size() > 0){
            myAdapter = new MyAdapter(arrayList,this);
            recyclerView.setAdapter(myAdapter);

        }
        else
            Toast.makeText(getApplicationContext(),"No Data found.",Toast.LENGTH_LONG).show();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public int getMovementFlags( RecyclerView recyclerView,  RecyclerView.ViewHolder viewHolder) {
                return 0;
            }

            @Override
            public boolean onMove( RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped( RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                ListItem listItem = arrayList.get(position);
                helper.deleteRow(listItem.getId());
                arrayList.remove(position);
                myAdapter.notifyItemRemoved(position);
                myAdapter.notifyItemRangeChanged(position,arrayList.size());

            }
        }).attachToRecyclerView(recyclerView);

        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setCameraId(0);

        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.floatingCamButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentIntegrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result!=null){
            if(result.getContents() == null){
                Toast.makeText(getApplicationContext(),"No result found",Toast.LENGTH_SHORT).show();
            }
            else {
                boolean isInserted = helper.insertData(result.getFormatName(), result.getContents());
                if(isInserted){
                    arrayList.clear();
                    arrayList = helper.getAllInformation();
                    myAdapter = new MyAdapter(arrayList,this);
                    recyclerView.setAdapter(myAdapter);
                    myAdapter.notifyDataSetChanged();
                }
            }
        }
        else{
            super.onActivityResult(requestCode,resultCode,data);
        }


    }
}