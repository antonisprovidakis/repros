package gr.teicrete.istlab.repros.model.microcontroller;

/**
 * Created by Antonis on 25-Dec-16.
 */

public abstract class NonElectricAppliance extends Appliance {

    public NonElectricAppliance(String id, String name) {
        super(id, name);
    }

    public NonElectricAppliance(String id) {
        super(id);
    }
}
