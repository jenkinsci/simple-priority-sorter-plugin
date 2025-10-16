package io.jenkins.plugins.simpleprioritysorter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import hudson.model.FreeStyleProject;
import hudson.model.Queue;
import java.util.List;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

/**
 * Test for {@link BasicPrioritySorter}.
 */
class BasicPrioritySorterTest {

    private final BasicPrioritySorter sorter = new BasicPrioritySorter();

    @Test
    @WithJenkins
    public void shouldSortByPriority(JenkinsRule rule) throws Exception {

        Jenkins jenkins = rule.jenkins;
        jenkins.setNumExecutors(0); // Force queuing

        // Create jobs with different priorities
        FreeStyleProject low = rule.createFreeStyleProject("low");
        low.addProperty(new BasicPriorityProperty(20));

        FreeStyleProject high = rule.createFreeStyleProject("high");
        high.addProperty(new BasicPriorityProperty(5));

        FreeStyleProject defaultPriorityJob = rule.createFreeStyleProject("default");

        // Schedule all builds
        low.scheduleBuild2(0);
        high.scheduleBuild2(0);
        defaultPriorityJob.scheduleBuild2(0);

        // Wait a bit for them to enter the queue
        Thread.sleep(3000);

        List<Queue.BuildableItem> items = rule.jenkins.getQueue().getBuildableItems();
        assertEquals(3, items.size());
        BasicPrioritySorter sorter = new BasicPrioritySorter();
        sorter.sortBuildableItems(items);
        assertEquals("high", items.get(0).task.getName());
        assertEquals("default", items.get(1).task.getName());
        assertEquals("low", items.get(2).task.getName());
    }

    @Test
    @WithJenkins
    void shouldSetPipelineProperties(JenkinsRule rule) throws Exception {
        WorkflowJob job = rule.jenkins.createProject(WorkflowJob.class, "pipeline");

        String pipelineScript = "" + "properties([basicJobPriority(priority: 10)])\n" + "node { echo 'Hello' }";

        // Validate that pipeline parses successfully
        assertDoesNotThrow(() -> job.setDefinition(new CpsFlowDefinition(pipelineScript, true)));

        // Schedule build and wait for completion
        WorkflowRun run = job.scheduleBuild2(0).get();

        rule.waitForCompletion(run);

        // Validate that the build completed successfully
        rule.assertBuildStatusSuccess(run);
    }
}
