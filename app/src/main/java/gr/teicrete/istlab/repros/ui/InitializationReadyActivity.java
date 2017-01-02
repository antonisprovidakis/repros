package gr.teicrete.istlab.repros.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gr.teicrete.istlab.repros.R;
import gr.teicrete.istlab.repros.model.db.DBHandler;
import gr.teicrete.istlab.repros.model.profiler.RoomProfile;

public class InitializationReadyActivity extends AppCompatActivity {

    private TextView tvInitializationReady;
    private Button btn_intrusive_start_profiling;

    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

    private DBHandler dbHandler;

    private RoomProfile roomProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialization_ready);

        final String roomId = getIntent().getStringExtra("EXTRA_ROOM_ID");
        dbHandler = new DBHandler(roomId, true);

        tvInitializationReady = (TextView) findViewById(R.id.tv_initialization_ready);

        btn_intrusive_start_profiling = (Button) findViewById(R.id.btn_intrusive_start_profiling);
        btn_intrusive_start_profiling.setVisibility(View.INVISIBLE);
        btn_intrusive_start_profiling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InitializationReadyActivity.this, IntrusiveProfilingActivity.class);
                intent.putExtra("EXTRA_ROOM_ID", roomId);
                intent.putExtra("EXTRA_ROOM_PROFILE", roomProfile);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Query ref = dbHandler.getRoomRef();

        if (ref != null) {

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    roomProfile = dataSnapshot.getValue(RoomProfile.class);
                    setupRoomInfo(roomProfile);

                    tvInitializationReady.setText("You are ready to start profiling");
                    btn_intrusive_start_profiling.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("Error: " + databaseError.getMessage());
                }
            });
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void setupRoomInfo(RoomProfile roomProfile) {

        List<String> installedAppliancesList = new ArrayList<>();
        for (HashMap<String, String> item : roomProfile.getInstalledAppliances()) {
            installedAppliancesList.add(item.get("id"));
        }

        List<String> installedSensorsList = new ArrayList<>();
        for (HashMap<String, String> item : roomProfile.getInstalledSensors()) {
            installedSensorsList.add(item.get("id"));
        }

        final List<String[]> genericInfoList = new ArrayList<>();


        genericInfoList.add(new String[]{"Country", roomProfile.getCountry()});
        genericInfoList.add(new String[]{"City", roomProfile.getCity()});
        genericInfoList.add(new String[]{"Building Name", roomProfile.getBuildingName()});
        genericInfoList.add(new String[]{"Room Name", roomProfile.getRoomName()});
        genericInfoList.add(new String[]{"Latitude", String.valueOf(roomProfile.getLatitude())});
        genericInfoList.add(new String[]{"Longitude", String.valueOf(roomProfile.getLongitude())});

        genericInfoList.add(new String[]{"Min Temperature (°C)", String.valueOf(roomProfile.getOptimumTemperature().get("min"))});
        genericInfoList.add(new String[]{"Max Temperature (°C)", String.valueOf(roomProfile.getOptimumTemperature().get("max"))});
        genericInfoList.add(new String[]{"Min Humidity (%)", String.valueOf(roomProfile.getOptimumHumidity().get("min"))});
        genericInfoList.add(new String[]{"Max Humidity (%)", String.valueOf(roomProfile.getOptimumHumidity().get("max"))});

        ArrayAdapter<String[]> genericListAdapter = new ArrayAdapter<String[]>(this, android.R.layout.simple_list_item_2, android.R.id.text1, genericInfoList) {
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
        Util.setListViewHeightBasedOnChildren(genericInfoListView);


        ListAdapter appliancesListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, installedAppliancesList);
        ListView appliancesListView = (ListView) findViewById(R.id.list_view_installed_appliances);
        appliancesListView.setAdapter(appliancesListAdapter);
        Util.setListViewHeightBasedOnChildren(appliancesListView);


        ListAdapter sensorsListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, installedSensorsList);
        ListView sensorsListView = (ListView) findViewById(R.id.list_view_installed_sensors);
        sensorsListView.setAdapter(sensorsListAdapter);
        Util.setListViewHeightBasedOnChildren(sensorsListView);
    }

}
