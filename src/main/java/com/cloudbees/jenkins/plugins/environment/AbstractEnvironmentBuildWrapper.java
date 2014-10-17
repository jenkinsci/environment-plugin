package com.cloudbees.jenkins.plugins.environment;

import com.cloudbees.jenkins.plugins.environment.producers.EnvironmentProducer;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Environment;
import hudson.tasks.BuildWrapper;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author <a href="mailto:nicolas.deloof@gmail.com">Nicolas De Loof</a>
 */
public class AbstractEnvironmentBuildWrapper extends BuildWrapper {

    private final Collection<EnvironmentProducer> producers;

    @DataBoundConstructor
    public AbstractEnvironmentBuildWrapper(Collection<EnvironmentProducer> producers) {
        this.producers = producers;
    }

    public Collection<EnvironmentProducer> getProducers() {
        return producers;
    }


    protected Collection<hudson.model.Environment> prepareEnvironments(AbstractBuild build, BuildListener listener) throws IOException, InterruptedException {
        listener.getLogger().println(getDescription());
        final Collection<hudson.model.Environment> envs = new ArrayList<hudson.model.Environment>();
        for (EnvironmentProducer producer : producers) {
            listener.getLogger().println("  - inject environment from " + producer.getDescription());
            envs.add(producer.buildEnvironmentFor(build, listener));
        }
        return envs;
    }

    protected String getDescription() {
        return "Prepare environment for the build";
    }
}
