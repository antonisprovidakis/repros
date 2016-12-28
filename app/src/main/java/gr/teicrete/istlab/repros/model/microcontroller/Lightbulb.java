package gr.teicrete.istlab.repros.model.microcontroller;

import android.graphics.Color;

/**
 * Created by Antonis on 25-Dec-16.
 */

public class Lightbulb extends ElectricAppliance {

    private int color;

    public Lightbulb(String id) {
        super(id);
        color = Color.WHITE;
    }

    public Lightbulb(String id, String name) {
        super(id, name);
        color = Color.WHITE;
    }

    public Lightbulb(String id, int watt, int color) {
        super(id, watt);
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
