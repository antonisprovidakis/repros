package gr.teicrete.istlab.repros.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import gr.teicrete.istlab.repros.R;

/**
 * Created by Antonis on 24-Dec-16.
 */

public class Util {

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

//    public static View createApplianceViewFromType(Context context, String type, ViewGroup container) {
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        View view = null;
//
//        if (type.equals("LIGHTBULB")) {
//            view = inflater.inflate(R.layout.appliance_lightbulb, container, false);
//        } else if (type.equals("AIR_CONDITIONER")) {
//            view = inflater.inflate(R.layout.appliance_ac, container, false);
//
//            Switch acSwitch = (Switch) view.findViewById(R.id.switch_ac);
//            final DiscreteSeekBar acSeekbar= (DiscreteSeekBar) view.findViewById(R.id.seekbar_ac);
//            acSeekbar.setEnabled(false);
//
//            acSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    acSeekbar.setEnabled(isChecked);
//                }
//            });
//
//        } else if (type.equals("WINDOW")) {
//            view = inflater.inflate(R.layout.appliance_window, container, false);
//        }
//
//        return view;
//    }
}
