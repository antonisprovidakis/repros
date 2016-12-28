package gr.teicrete.istlab.repros.model.profiler;

/**
 * Created by Antonis on 27-Dec-16.
 */

public interface MobileSensor {

    public void startSensing();

    public void stopSensing();

    public void setOnDataSensedListener(Object listener);

}
