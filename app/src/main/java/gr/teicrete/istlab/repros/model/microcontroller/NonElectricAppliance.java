package gr.teicrete.istlab.repros.model.microcontroller;

import android.content.Context;

/**
 * Created by Antonis on 25-Dec-16.
 */

public abstract class NonElectricAppliance extends Appliance {

    public NonElectricAppliance(Context context, String id, String name) {
        super(context, id, name);
    }

    public NonElectricAppliance(Context context, String id) {
        this(context, id, id);
    }
}
