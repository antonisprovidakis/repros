package gr.teicrete.istlab.repros.model.profiler;

/**
 * Created by Antonis on 26-Dec-16.
 */

public class NonIntrusiveProfiler {

    private ProfilingMode profilingMode;


    public NonIntrusiveProfiler() {
        profilingMode = ProfilingMode.NON_INTRUSIVE;
    }



    public enum ProfilingMode{
        INTRUSIVE,
        NON_INTRUSIVE
    }
}
