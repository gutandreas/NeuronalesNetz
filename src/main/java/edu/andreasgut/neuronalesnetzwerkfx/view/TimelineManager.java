package edu.andreasgut.neuronalesnetzwerkfx.view;

import javafx.animation.Timeline;
import java.util.ArrayList;
import java.util.List;

public class TimelineManager {
    private static final List<Timeline> runningTimelines = new ArrayList<>();


    public static void addTimeline(Timeline timeline) {
        runningTimelines.add(timeline);
    }

    public static void removeTimeline(Timeline timeline) {
        runningTimelines.remove(timeline);
    }

    public static void stopAllTimelines() {
        for (Timeline timeline : runningTimelines) {
            timeline.stop();
        }
        runningTimelines.clear();
    }
}