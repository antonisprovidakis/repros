package gr.teicrete.istlab.repros.model.communication;

/**
 * Created by Antonis on 26-Dec-16.
 */

public abstract class CommunicationModule {

    public CommunicationModule() {
    }

    public abstract boolean isEnabled();

    public abstract void enable();

    public abstract boolean isConnected();

}
