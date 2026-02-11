package org.firstinspires.ftc.teamcode.common.utility.profiler.entry;

public class BasicProfilerEntryFactory implements ProfilerEntryFactory {
    @Override
    public ProfilerEntry create(String type, long start, long end) {
        return new BasicProfilerEntry(type, start, end);
    }
}