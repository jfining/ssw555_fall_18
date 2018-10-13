package com.rasodu.gedcom.Utils;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Helper {
    public enum PeriodUnit {
        Days,
        Months,
        Years,
    }

    private static HashMap<PeriodUnit, Float> periodToDays;

    static {
        periodToDays = new HashMap<>();
        periodToDays.put(PeriodUnit.Days, 1f);
        periodToDays.put(PeriodUnit.Months, 30.4f);
        periodToDays.put(PeriodUnit.Years, 365.25f);
    }

    public static boolean DateWithin(Date d1, Date d2, float limit, PeriodUnit unit) {
        long diff = Math.abs(d2.getTime() - d1.getTime());
        long daysDiff = TimeUnit.MILLISECONDS.toDays(diff);
        return DatesWith(daysDiff, limit, unit);
    }

    public static boolean DateWithinOnTimeline(Date earlyDate, Date laterDate, float limit, PeriodUnit unit) {
        long diff = laterDate.getTime() - earlyDate.getTime();
        if(diff < 0)
            return false;
        long daysDiff = TimeUnit.MILLISECONDS.toDays(diff);
        return DatesWith(daysDiff, limit, unit);
    }

    private static boolean DatesWith(long days, float limit, PeriodUnit unit){
        return (days / periodToDays.get(unit)) <= limit;
    }

    public static boolean DateGapLagerThan(Date d1, Date d2, float limit, PeriodUnit unit) {
        return !DateWithin(d1, d2, limit, unit);
    }

    public static boolean DateGapLargerThenOnTimeLine(Date earlyDate, Date laterDate, float limit, PeriodUnit unit) {
        return !DateWithin(earlyDate, laterDate, limit, unit);
    }
}
