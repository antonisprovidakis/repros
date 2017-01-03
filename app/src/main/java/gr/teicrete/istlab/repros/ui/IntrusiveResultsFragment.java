package gr.teicrete.istlab.repros.ui;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import gr.teicrete.istlab.repros.R;
import gr.teicrete.istlab.repros.model.db.DBHandler;
import gr.teicrete.istlab.repros.model.profiler.DateUtils;
import gr.teicrete.istlab.repros.model.profiler.IntrusiveReadingSnapshot;
import gr.teicrete.istlab.repros.ui.charts.ChartItem;
import gr.teicrete.istlab.repros.ui.charts.LineChartItem;
import gr.teicrete.istlab.repros.ui.charts.PieChartItem;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IntrusiveResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IntrusiveResultsFragment extends Fragment {
    private static final String EXTRA_ROOM_ID = "EXTRA_ROOM_ID";

    private String roomId;
    private DBHandler dbHandler;


    public IntrusiveResultsFragment() {
        // Required empty public constructor
    }

    public static IntrusiveResultsFragment newInstance(String roomId) {
        IntrusiveResultsFragment fragment = new IntrusiveResultsFragment();
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

            Utils.init(getContext());
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHandler.getlastReadingKeyRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String key = (String) dataSnapshot.getValue();

                dbHandler.getReadingSnapshotsRef(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                final List<Long> timestamps = new ArrayList<>();
                                final List<Double> temperatureIndoors = new ArrayList<>();
                                final List<Double> temperatureOutdoors = new ArrayList<>();
                                final List<Double> humidityIndoors = new ArrayList<>();
                                final List<Double> humidityOutdoors = new ArrayList<>();
                                final List<Double> audio = new ArrayList<>();
                                final List<Double> light = new ArrayList<>();
                                final List<Double> co = new ArrayList<>();
                                final List<Double> totalEnergyConsumption = new ArrayList<>();


                                for (DataSnapshot readingDataSnapshot : dataSnapshot.getChildren()) {

                                    IntrusiveReadingSnapshot readingsSnapshot = readingDataSnapshot.getValue(IntrusiveReadingSnapshot.class);

                                    timestamps.add(readingsSnapshot.getTimestamp());
                                    temperatureIndoors.add(readingsSnapshot.getTemperatureIndoors());
                                    temperatureOutdoors.add(readingsSnapshot.getTemperatureOutdoors());
                                    humidityIndoors.add(readingsSnapshot.getHumidityIndoors());
                                    humidityOutdoors.add(readingsSnapshot.getHumidityOutdoors());
                                    audio.add(readingsSnapshot.getAudioLevel());
                                    light.add(readingsSnapshot.getLightLevel());
                                    co.add(readingsSnapshot.getCO());
                                    totalEnergyConsumption.add(readingsSnapshot.getTotalEnergyConsumption());
                                }

                                ArrayList<ChartItem> list = new ArrayList<>();

                                list.add(new PieChartItem(generateDataPie(), getContext()));

                                IAxisValueFormatter iAxisValueFormatter = new IAxisValueFormatter() {
                                    @Override
                                    public String getFormattedValue(float value, AxisBase axis) {
                                        Long timestamp = timestamps.get((int) value);
                                        return DateUtils.timestampToDateString(timestamp);
                                    }
                                };

                                // Indoors Temperature
                                list.add(new LineChartItem(generateDataLine("Indoors Temperature", Color.rgb(255, 0, 0), temperatureIndoors), getContext(), iAxisValueFormatter));
                                // Outdoors Temperature
                                list.add(new LineChartItem(generateDataLine("Outdoors Temperature", Color.rgb(200, 21, 0), temperatureOutdoors), getContext(), iAxisValueFormatter));
                                // Indoors Humidity
                                list.add(new LineChartItem(generateDataLine("Indoors Humidity", Color.rgb(0, 0, 255), humidityIndoors), getContext(), iAxisValueFormatter));
                                // Outdoors Humidity
                                list.add(new LineChartItem(generateDataLine("Outdoors Humidity", Color.rgb(51, 153, 255), humidityOutdoors), getContext(), iAxisValueFormatter));
                                // audioLevel
                                list.add(new LineChartItem(generateDataLine("Audio Level", Color.rgb(0, 204, 0), audio), getContext(), iAxisValueFormatter));
                                // lightLevel
                                list.add(new LineChartItem(generateDataLine("Light Level", Color.rgb(255, 255, 0), light), getContext(), iAxisValueFormatter));
                                // co level
                                list.add(new LineChartItem(generateDataLine("CO Level", Color.rgb(204, 0, 153), co), getContext(), iAxisValueFormatter));
                                // total energy consumption
                                list.add(new LineChartItem(generateDataLine("Total Energy Consumption", Color.rgb(102, 0, 102), totalEnergyConsumption), getContext(), iAxisValueFormatter));

                                IntrusiveResultsFragment.ChartDataAdapter cda = new IntrusiveResultsFragment.ChartDataAdapter(getContext(), list);
                                ListView lv = (ListView) getView().findViewById(R.id.listView1);
                                lv.setAdapter(cda);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_intrusive_results, container, false);

        // do any UI initializations
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
//            return rootView;


        // Inflate the layout for this fragment
        return rootView;
    }

    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {

        public ChartDataAdapter(Context context, List<ChartItem> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position) {
            // return the views type
            return getItem(position).getItemType();
        }

        @Override
        public int getViewTypeCount() {
            return 3; // we have 3 different item-types
        }
    }

    private PieData generateDataPie() {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        entries.add(new PieEntry(20f, "Suspicious"));
        entries.add(new PieEntry(80f, "Normal"));

        PieDataSet d = new PieDataSet(entries, "Energy Consumption");

        // space between slices
        d.setSliceSpace(2f);

        int[] colors = {rgb("#ff3333"), rgb("#2ecc71")};

        d.setColors(colors);

        PieData cd = new PieData(d);
        return cd;
    }

    private LineData generateDataLine(String title, int color, List<Double> data) {

        ArrayList<Entry> e1 = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            e1.add(new Entry(i, data.get(i).floatValue()));
        }

        LineDataSet d1 = new LineDataSet(e1, title);
        d1.setLineWidth(4.0f);
        d1.setCircleRadius(4.5f);
        d1.setColor(color);
        d1.setDrawValues(false);

        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);

        LineData cd = new LineData(sets);
        return cd;
    }

}
