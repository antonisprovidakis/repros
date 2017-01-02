package gr.teicrete.istlab.repros.model.profiler;

/**
 * Created by Antonis on 27-Dec-16.
 */

public interface Profiler {

    public void startProfiling();

    public void stopProfiling();

    public boolean isProfiling();

    public void analyzeData();

    public interface DataAnalysisEndedListener {
        public void onDataAnalysisEnd();
    }
}
