package gr.teicrete.istlab.repros.model.microcontroller;

import android.content.Context;
import android.graphics.Color;

/**
 * Created by Antonis on 25-Dec-16.
 */

public class Lightbulb extends ElectricAppliance {

    private int color = Color.WHITE;

    public Lightbulb(Context context, String id) {
        this(context, id, id);
    }

    public Lightbulb(Context context, String id, String name) {
        super(context, id, name);
    }

    public Lightbulb(Context context, String id, int watt, int color) {
        super(context, id, watt);
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
