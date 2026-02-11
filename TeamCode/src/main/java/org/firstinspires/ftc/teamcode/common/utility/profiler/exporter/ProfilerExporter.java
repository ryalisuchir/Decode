package org.firstinspires.ftc.teamcode.common.utility.profiler.exporter;

import org.firstinspires.ftc.teamcode.common.utility.profiler.entry.ProfilerEntry;

import java.util.List;

public interface ProfilerExporter {
    void export(List<ProfilerEntry> entries);
}