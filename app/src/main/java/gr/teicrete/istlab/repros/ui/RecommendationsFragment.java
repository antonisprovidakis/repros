package gr.teicrete.istlab.repros.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import gr.teicrete.istlab.repros.R;
import gr.teicrete.istlab.repros.model.db.DBHandler;
import gr.teicrete.istlab.repros.model.profiler.RecommendationsSet;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecommendationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecommendationsFragment extends ListFragment {
    private static final String EXTRA_ROOM_ID = "EXTRA_ROOM_ID";
    private static final String EXTRA_INTRUSIVE = "EXTRA_INTRUSIVE";

    private String roomId;
    private boolean intrusive;

    private DBHandler dbHandler;

    public RecommendationsFragment() {
    }

    public static RecommendationsFragment newInstance(String roomId, boolean intrusive) {
        RecommendationsFragment fragment = new RecommendationsFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_ROOM_ID, roomId);
        args.putBoolean(EXTRA_INTRUSIVE, intrusive);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            roomId = getArguments().getString(EXTRA_ROOM_ID);
            intrusive = getArguments().getBoolean(EXTRA_INTRUSIVE);

            dbHandler = new DBHandler(roomId, intrusive);

        }
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        dbHandler.getlastReadingKeyRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String key = (String) dataSnapshot.getValue();

                dbHandler.getRecommendationsRef(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {

                        final List<String> recommendations = (List<String>) dataSnapshot.getValue();

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ListAdapter listAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, recommendations);
                                setListAdapter(listAdapter);
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
}
