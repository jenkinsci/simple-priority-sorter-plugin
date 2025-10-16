package io.jenkins.plugins.simpleprioritysorter;

import hudson.Extension;
import hudson.model.Job;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

/**
 * A simple job property to hold a priority value.
 */
@Extension
public class BasicPriorityProperty extends JobProperty<Job<?, ?>> {

    /**
     * The priority value for the job.
     */
    private int priority;

    /**
     * Whether to use priority sorting for this job.
     */
    private boolean usePriority;

    public BasicPriorityProperty() {}

    @DataBoundConstructor
    public BasicPriorityProperty(int priority) {
        this.priority = priority;
        this.usePriority = true;
    }

    @DataBoundSetter
    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    @DataBoundSetter
    public void setUsePriority(boolean usePriority) {
        this.usePriority = usePriority;
    }

    public boolean isUsePriority() {
        return usePriority;
    }

    @Extension
    @Symbol("basicJobPriority")
    public static class DescriptorImpl extends JobPropertyDescriptor {
        @Override
        public String getDisplayName() {
            return "Basic Job Priority";
        }

        @Override
        public boolean isApplicable(Class<? extends Job> jobType) {
            return true;
        }
    }
}
