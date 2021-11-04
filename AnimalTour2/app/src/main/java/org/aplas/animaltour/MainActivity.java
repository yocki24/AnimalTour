package org.aplas.animaltour;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView rcView;
    DataAdapter mAdapter;

    public static int getId(String resourceName, Class<?> c){
        try {
            Field idField = c.getDeclaredField(resourceName);
            return idField.getInt(idField);
        }catch (Exception e){
            return -1;
        }
    }
    private ArrayList<DataItem> getAnimalData(){
        String[] listTitles = getResources().getStringArray(R.array.animal_titles);
        int[] listColor = getResources().getIntArray(R.array.animal_color);
        String[] listInfo = getResources().getStringArray(R.array.animal_info);
        String[] listIcon = getResources().getStringArray(R.array.animal_icon);

        ArrayList<DataItem> data = new ArrayList<>();
        for (int i = 0;i < listTitles.length;i++){
            data.add(new DataItem(listTitles[i],listInfo[i],listColor[i],getId(listIcon[i], R.drawable.class)));
        }
        data.add(new DataItem("Invertebrates",this.getString(R.string.invert_info), this.getColor(R.color.invert_color), R.drawable.animinverts));
        return data;
    }

    private void loadAnimalData(){
        mAdapter = new DataAdapter(this,getAnimalData());
        ItemTouchHelper.Callback callback = new ItemMoveCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rcView);
        rcView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void openMediaActivity(String title){
        Intent media = new Intent(getApplicationContext(),MediaActivity.class);
        media.putExtra("TITLE_ANIMAL",title);
        startActivity(media);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    private void openInvertActivity(){
        Intent intent = new Intent(getApplicationContext(),InvertActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rcView = (RecyclerView) findViewById(R.id.dataView);
        rcView.setLayoutManager(new LinearLayoutManager(this));
        loadAnimalData();

        mAdapter.setOnItemClickListener(new DataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                TextView itemTitle = (TextView) v.findViewById(R.id.animalTitle);
                if (itemTitle.getText().toString().equals("Invertebrates")){
                    openInvertActivity();
                }else{
                    openMediaActivity(itemTitle.getText().toString());
                }
            }
        });
    }
}