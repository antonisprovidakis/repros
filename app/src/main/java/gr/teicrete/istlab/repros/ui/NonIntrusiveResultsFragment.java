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
import gr.teicrete.istlab.repros.model.profiler.NonIntrusiveReadingsSnapshot;
import gr.teicrete.istlab.repros.ui.charts.ChartItem;
import gr.teicrete.istlab.repros.ui.charts.LineChartItem;
import gr.teicrete.istlab.repros.ui.charts.PieChartItem;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NonIntrusiveResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NonIntrusiveResultsFragment extends Fragment {
    private static final String EXTRA_ROOM_ID = "EXTRA_ROOM_ID";

    private String roomId;
    private DBHandler dbHandler;


    public NonIntrusiveResultsFragment() {
        // Required empty public constructor
    }


    //    public static NonIntrusiveResultsFragment newInstance(long[] timestamps, double[] motion, double[] audio, double[] light) {
    public static NonIntrusiveResultsFragment newInstance(String roomId) {
        NonIntrusiveResultsFragment fragment = new NonIntrusiveResultsFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_ROOM_ID, roomId);
//        args.putLongArray(EXTRA_TIMESTAMP, timestamps);
//        args.putDoubleArray(EXTRA_MOTION, motion);
//        args.putDoubleArray(EXTRA_AUDIO, audio);
//        args.putDoubleArray(EXTRA_LIGHT, light);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            roomId = getArguments().getString(EXTRA_ROOM_ID);

            dbHandler = new DBHandler(roomId, false);

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
                                final List<Double> motion = new ArrayList<>();
                                final List<Double> audio = new ArrayList<>();
                                final List<Double> light = new ArrayList<>();

                                for (DataSnapshot readingDataSnapshot : dataSnapshot.getChildren()) {

                                    NonIntrusiveReadingsSnapshot readingsSnapshot = readingDataSnapshot.getValue(NonIntrusiveReadingsSnapshot.class);

                                    timestamps.add(readingsSnapshot.getTimestamp());
                                    motion.add(NonIntrusiveReadingsSnapshot.booleanToDouble(readingsSnapshot.isMotionDetected()));
                                    audio.add(readingsSnapshot.getAudioLevel());
                                    light.add(readingsSnapshot.getLightLevel());
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

                                // motionDetected
                                list.add(new LineChartItem(generateDataLine("Motion Detection", Color.rgb(255, 0, 0), motion), getContext(), iAxisValueFormatter));
                                // audioLevel
                                list.add(new LineChartItem(generateDataLine("Audio Level", Color.rgb(0, 204, 0), audio), getContext(), iAxisValueFormatter));
                                // lightLevel
                                list.add(new LineChartItem(generateDataLine("Light Level", Color.rgb(255, 255, 0), light), getContext(), iAxisValueFormatter));

                                ChartDataAdapter cda = new ChartDataAdapter(getContext(), list);
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
        View rootView = inflater.inflate(R.layout.fragment_non_intrusive_results, container, false);

        // do any UI initializations
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
//            return rootView;

        // Inflate the layout for this fragment
        return rootView;
    }


    /**
     * adapter that supports 3 different item types
     */
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
