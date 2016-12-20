package gr.teicrete.istlab.repros.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import gr.teicrete.istlab.repros.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecommendationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecommendationsFragment extends ListFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String[] recommendationsArray = {"Recommendation 1 ", "Recommendation 2", "Recommendation 3"};


    private ListView listView;

    public RecommendationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecommendationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecommendationsFragment newInstance(String param1, String param2) {
        RecommendationsFragment fragment = new RecommendationsFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListAdapter listAdapter = new ArrayAdapter<String>(this.getContext(), R.layout.list_item_recommendation, recommendationsArray);
        setListAdapter(listAdapter);

    }

}
