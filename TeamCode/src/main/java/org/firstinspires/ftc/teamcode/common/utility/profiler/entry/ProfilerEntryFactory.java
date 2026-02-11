package org.firstinspires.ftc.teamcode.common.utility.profiler.entry;

public interface ProfilerEntryFactory {
    ProfilerEntry create(String type, long start, long end);
}