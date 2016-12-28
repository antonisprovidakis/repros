package gr.teicrete.istlab.repros.model.microcontroller;

/**
 * Created by Antonis on 25-Dec-16.
 */

public abstract class Sensor {

    private String id;

    public Sensor(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
