package gr.teicrete.istlab.repros.model.profiler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Antonis on 01-Jan-17.
 */

public class DateUtils {
    public static String timestampToDateString(long time) {
        Date date = new Date(time);

        SimpleDateFormat sdf = new SimpleDateFormat("d MM yyyy HH:mm:ss"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));

        return sdf.format(date);
    }
}
