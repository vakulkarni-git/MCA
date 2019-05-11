package com.git.mca;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class ListAndSpinnerSample extends ListActivity {

    private Integer[] imageIds = {
      R.drawable.ad,
      R.drawable.ae,
      R.drawable.af
    };

    private String [] myDataset = {
            "Andhra Pradesh",
            "Arunachal Pradesh",
            "Assam",
            "Bihar",
            "Chhattisgarh",
            "Goa",
            "Gujarat",
            "Haryana",
            "Himachal Pradesh",
            "Jammu & Kashmir",
            "Jharkhand",
            "Karnataka",
            "Kerala",
            "Madhya Pradesh",
            "Maharashtra",
            "Manipur",
            "Meghalaya",
            "Mizoram",
            "Nagaland",
            "Odisha",
            "Punjab",
            "Rajasthan",
            "Sikkim",
            "Tamil Nadu",
            "Telangana",
            "Tripura",
            "Uttar Pradesh",
            "Uttarakhand",
            "West Bengal",
            "Andaman and Nicobar Islands",
            "Chandigarh",
            "Dadra and Nagar Haveli",
            "Daman & Diu",
            "Lakshadweep",
            "Puducherry",
            "The Government of NCT of Delhi"
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setupListView();
        setupSpinner();
    }

    private void setupSpinner() {
        setContentView(R.layout.spinner);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, myDataset);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index = parent.getSelectedItemPosition();
                Toast.makeText(getBaseContext(),
                        "You have selected " + myDataset[position],
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void setupListView() {
        setMultiChoiceMode();
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_checked, myDataset));
    }

    public void onListItemClick(ListView parent, View v, int position, long id) {
        parent.setItemChecked(position, parent.isItemChecked(position));
        Toast.makeText(this,
                "You have selected " + myDataset[position],
                Toast.LENGTH_SHORT).show();
    }

    private void setMultiChoiceMode() {
        ListView listView = getListView();
        listView.setChoiceMode(1);
        listView.setTextFilterEnabled(true);
    }
}
