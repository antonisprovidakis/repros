package gr.teicrete.istlab.repros.model.communication;

/**
 * Created by Antonis on 26-Dec-16.
 */

public abstract class CommunicationModule {

//    private boolean enabled;
//    protected boolean connected;

//    public CommunicationModule() {
//        this.enabled = false;
//    }
    public CommunicationModule() {
    }

//    public boolean isEnabled() {
//        return enabled;
//    }
    public abstract boolean isEnabled();

    public abstract void enable();

//    public void enable() {
//        if (!enabled) {
//            enabled = true;
//        }
//    }

//    public abstract void disable();

//    public void disable() {
//        if (enabled) {
//            enabled = false;
//        }
//    }

    public abstract boolean isConnected();

//    public boolean isConnected(){
//        return connected;
//    }

}
