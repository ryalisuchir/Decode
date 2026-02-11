package org.firstinspires.ftc.teamcode.common.utility.profiler.entry;

public class BasicProfilerEntry extends ProfilerEntry {

    public BasicProfilerEntry(String type, long start, long end) {
        super(type, start, end);
    }

    @Override
    public String[] toCSVRow() {
        return new String[] {
                getType(),
                String.valueOf(getStartTime()),
                String.valueOf(getEndTime()),
                String.valueOf(getDeltaTime())
        };
    }
}