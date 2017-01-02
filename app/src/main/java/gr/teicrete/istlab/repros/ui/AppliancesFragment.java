package gr.teicrete.istlab.repros.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.HashMap;

import gr.teicrete.istlab.repros.R;
import gr.teicrete.istlab.repros.model.db.DBHandler;
import gr.teicrete.istlab.repros.model.microcontroller.AirConditioner;
import gr.teicrete.istlab.repros.model.microcontroller.Appliance;
import gr.teicrete.istlab.repros.model.microcontroller.ArduinoBoard;
import gr.teicrete.istlab.repros.model.microcontroller.Lightbulb;
import gr.teicrete.istlab.repros.model.microcontroller.Window;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppliancesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppliancesFragment extends Fragment {
    private static final String EXTRA_ROOM_ID = "EXTRA_ROOM_ID";

    private String roomId;
    private DBHandler dbHandler;
    private ArduinoBoard arduinoBoard;


    public AppliancesFragment() {
        // Required empty public constructor
    }


    public static AppliancesFragment newInstance(String roomId) {
        AppliancesFragment fragment = new AppliancesFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_ROOM_ID, roomId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            roomId = getArguments().getString(EXTRA_ROOM_ID);

            dbHandler = new DBHandler(roomId, true);

            Lightbulb lightbulb = new Lightbulb(getActivity().getApplicationContext(), "lightbulb_1");
            AirConditioner airConditioner = new AirConditioner(getActivity().getApplicationContext(), "air_conditioner_1", 100, 17, 20000);
            Window window = new Window(getActivity().getApplicationContext(), "window_1");

            HashMap<String, Appliance> appliances = new HashMap<>();

            appliances.put(lightbulb.getId(), lightbulb);
            appliances.put(airConditioner.getId(), airConditioner);
            appliances.put(window.getId(), window);

            arduinoBoard = new ArduinoBoard(appliances);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_appliances, container, false);

//        final ViewGroup containerLayout = (ViewGroup) rootView.findViewById(R.id.appliances_container_layout);
//
//        dbHandler.getRoomRef().child("installedAppliances").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(final DataSnapshot dataSnapshotInstalledAppliances) {
//
//                for (DataSnapshot readingDataSnapshot : dataSnapshotInstalledAppliances.getChildren()) {
//
//                    Map<String, String> appliance = (Map<String, String>) readingDataSnapshot.getValue();
//
//
//                    View viewAppliance = Util.createApplianceViewFromType(getContext(), appliance.get("type"), containerLayout);
//
//                    System.out.println(viewAppliance.getHeight());
//                    View divider = inflater.inflate(R.layout.simple_divider, containerLayout, false);
//
//                    containerLayout.addView(viewAppliance);
//                    containerLayout.addView(divider);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });

        Switch lightsSwitch = (Switch) rootView.findViewById(R.id.switch_lights);
        lightsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Appliance lightbulb = arduinoBoard.getApplianceWithId("lightbulb_1");

                if (isChecked) {
                    lightbulb.turnOn();
                } else {
                    lightbulb.turnOff();
                }
            }
        });


        Switch acSwitch = (Switch) rootView.findViewById(R.id.switch_ac);
        final DiscreteSeekBar acSeekbar = (DiscreteSeekBar) rootView.findViewById(R.id.seekbar_ac);
        acSeekbar.setEnabled(false);

        acSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                acSeekbar.setEnabled(isChecked);
            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }

}
