package gr.teicrete.istlab.repros.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import gr.teicrete.istlab.repros.R;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NonIntrusiveResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NonIntrusiveResultsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public NonIntrusiveResultsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NonIntrusiveResultsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NonIntrusiveResultsFragment newInstance(String param1, String param2) {
        NonIntrusiveResultsFragment fragment = new NonIntrusiveResultsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            // variables assignments
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ListView lv = (ListView) getView().findViewById(R.id.listView1);

        ArrayList<ChartItem> list = new ArrayList<ChartItem>();

        list.add(new PieChartItem(generateDataPie(), getContext()));


//        // 30 items
//        for (int i = 0; i < 30; i++) {
//
//            if(i % 3 == 0) {
//                list.add(new LineChartItem(generateDataLine(i + 1), getApplicationContext()));
//            } else if(i % 3 == 1) {
//                list.add(new BarChartItem(generateDataBar(i + 1), getApplicationContext()));
//            } else if(i % 3 == 2) {
//                list.add(new PieChartItem(generateDataPie(i + 1), getApplicationContext()));
//            }
//        }

        ChartDataAdapter cda = new ChartDataAdapter(getContext(), list);
        lv.setAdapter(cda);
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

//        entries.add(new PieEntry((float) ((Math.random() * 70) + 30), "Energy Consumption"));
        entries.add(new PieEntry(20f, "Energy Consumption"));
//        entries.add(new PieEntry((float) ((Math.random() * 70) + 30), "Normal"));
        entries.add(new PieEntry(80f, "Normal"));

        PieDataSet d = new PieDataSet(entries, "");

        // space between slices
        d.setSliceSpace(2f);

        int[] colors = {rgb("#ff3333"), rgb("#2ecc71")};

        d.setColors(colors);

        PieData cd = new PieData(d);
        return cd;
    }

}
