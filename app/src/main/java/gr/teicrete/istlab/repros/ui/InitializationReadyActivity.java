package gr.teicrete.istlab.repros.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import gr.teicrete.istlab.repros.R;

public class InitializationReadyActivity extends AppCompatActivity {

    private Button btn_intrusive_start_profiling;

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

    // TODO: replace ad-hoc roomId with the one coming from previous activity
    DatabaseReference roomInfoRef = rootRef.child("rooms").child("room_1");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialization_ready);

        btn_intrusive_start_profiling = (Button) findViewById(R.id.btn_intrusive_start_profiling);
        btn_intrusive_start_profiling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InitializationReadyActivity.this, IntrusiveProfilingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        roomInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap data = (HashMap) dataSnapshot.getValue();
                setupRoomInfo(data);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error: " + databaseError.getMessage());
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void setupRoomInfo(HashMap data) {
        List<String> installedAppliancesList = (ArrayList<String>) data.get("installed_appliances");
        List<String> installedSensorsList = (ArrayList<String>) data.get("installed_sensors");

        final List<String[]> genericInfoList = new ArrayList<>();


        genericInfoList.add(new String[]{"Country", (String) data.get("country")});
        genericInfoList.add(new String[]{"City", (String) data.get("city")});
        genericInfoList.add(new String[]{"Building Name", (String) data.get("building_name")});
        genericInfoList.add(new String[]{"Room Name", (String) data.get("room_name")});
        genericInfoList.add(new String[]{"Latitude", String.valueOf((double) data.get("latitude"))});
        genericInfoList.add(new String[]{"Longitude", String.valueOf((double) data.get("longitude"))});

        ArrayAdapter<String[]> genericListAdapter = new ArrayAdapter<String[]>(this, android.R.layout.simple_list_item_2, android.R.id.text1, genericInfoList){
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                String[] entry = genericInfoList.get(position);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setTypeface(null, Typeface.BOLD);
                text1.setText(entry[0]);
                text2.setText(entry[1]);

                return view;
            }
        };
        ListView genericInfoListView = (ListView) findViewById(R.id.list_view_room_info);
        genericInfoListView.setAdapter(genericListAdapter);
        setListViewHeightBasedOnChildren(genericInfoListView);


        ListAdapter appliancesListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, installedAppliancesList);
        ListView appliancesListView = (ListView) findViewById(R.id.list_view_installed_appliances);
        appliancesListView.setAdapter(appliancesListAdapter);
        setListViewHeightBasedOnChildren(appliancesListView);


        ListAdapter sensorsListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, installedSensorsList);
        ListView sensorsListView = (ListView) findViewById(R.id.list_view_installed_sensors);
        sensorsListView.setAdapter(sensorsListAdapter);
        setListViewHeightBasedOnChildren(sensorsListView);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
