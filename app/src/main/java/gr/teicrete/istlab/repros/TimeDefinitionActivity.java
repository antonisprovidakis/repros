package gr.teicrete.istlab.repros;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class TimeDefinitionActivity extends AppCompatActivity {

    private static final String TAG = "TimeDefinitionActivity";
    public final static String PROFILING_SECONDS = "gr.teicrete.istlab.repros.PROFILING_SECONDS";



    private Spinner spinnerTimeSelection;
    private Spinner spinnerTimeUnit;

    private Button btnStartProfiling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_definition);


        spinnerTimeSelection = (Spinner) findViewById(R.id.spinner_time_selection);
        spinnerTimeUnit = (Spinner) findViewById(R.id.spinner_unit_selection);
        populateSpinners();

        btnStartProfiling = (Button) findViewById(R.id.btn_start_profiling);
        btnStartProfiling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TimeDefinitionActivity.this, NonIntrusiveProfilingActivity.class);
//                intent.putExtra(PROFILING_SECONDS, getTimeSelectedToSeconds());
                startActivity(intent);
            }
        });
    }


    private void populateSpinners() {

//        try {
//            Field popup = Spinner.class.getDeclaredField("mPopup");
//            popup.setAccessible(true);
//            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinnerTimeSelection);
//            popupWindow.setHeight(500);
//        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
//        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.timeframe_time_selection_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerTimeSelection.setAdapter(adapter1);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.timeframe_time_units_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerTimeUnit.setAdapter(adapter2);
    }

    private int getTimeSelectedToSeconds() {
        int timeInSeconds = 0;

        int timeSelectionItem = Integer.valueOf((String) spinnerTimeSelection.getSelectedItem());

        switch ((String) spinnerTimeUnit.getSelectedItem()) {
            case "Seconds":
                timeInSeconds = timeSelectionItem;
                break;
            case "Minutes":
                timeInSeconds = timeSelectionItem * 60;
                break;
            case "Hours":
                timeInSeconds = timeSelectionItem * 60 * 60;
                break;
            case "Days":
                timeInSeconds = timeSelectionItem * 60 * 60 * 24;
                break;
            default:
                break;
        }

        return timeInSeconds;
    }


}
