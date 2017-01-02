package gr.teicrete.istlab.repros.model.microcontroller;

import android.content.Context;

/**
 * Created by Antonis on 25-Dec-16.
 */

public class Window extends NonElectricAppliance {

    public Window(Context context, String id) {
        this(context, id, id);
    }

    public Window(Context context, String id, String name) {
        super(context, id, name);
    }

}
