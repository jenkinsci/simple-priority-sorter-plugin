package io.jenkins.plugins.simpleprioritysorter;

import hudson.Extension;
import hudson.model.Job;
import jenkins.model.OptionalJobProperty;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

/**
 * A simple job property to hold a priority value.
 */
@Extension
public class BasicPriorityProperty extends OptionalJobProperty<Job<?, ?>> {

    /**
     * The priority value for the job.
     */
    private int priority;

    @DataBoundConstructor
    public BasicPriorityProperty() {}

    BasicPriorityProperty(int priority) {
        this.priority = priority;
    }

    @DataBoundSetter
    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    @Extension
    @Symbol("basicJobPriority")
    public static class DescriptorImpl extends OptionalJobPropertyDescriptor {
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
