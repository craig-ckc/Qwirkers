package com.example.qwirkers.Utility;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        // On selecting a spinner item
        String item = adapterView.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
