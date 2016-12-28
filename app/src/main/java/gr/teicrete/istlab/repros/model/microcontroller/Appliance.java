package gr.teicrete.istlab.repros.model.microcontroller;

/**
 * Created by Antonis on 25-Dec-16.
 */

public abstract class Appliance {

    private String id;
    private String name;
    private boolean enabled;

    public Appliance(String id) {
        this.id = id;
        this.name = id;
        enabled = false;
    }

    public Appliance(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void turnOn() {
        if (!enabled) {
            enabled = true;
        }
    }

    public void turnOff() {
        if (enabled) {
            enabled = false;
        }
    }
}
