package io.jenkins.plugins.simpleprioritysorter;

import hudson.Extension;
import hudson.model.Job;
import hudson.model.Queue;
import hudson.model.queue.QueueSorter;
import java.util.Comparator;
import java.util.List;

@Extension
public class BasicPrioritySorter extends QueueSorter {

    /**
     * Default priority value if none is set.
     */
    private static final int DEFAULT_PRIORITY = 10;

    @Override
    public void sortBuildableItems(List<Queue.BuildableItem> items) {
        items.sort(Comparator.comparingInt(this::getPriority));
    }

    private int getPriority(Queue.BuildableItem item) {
        Job<?, ?> job = (Job<?, ?>) item.task;
        BasicPriorityProperty prop = job.getProperty(BasicPriorityProperty.class);
        return prop != null ? prop.getPriority() : DEFAULT_PRIORITY;
    }
}
