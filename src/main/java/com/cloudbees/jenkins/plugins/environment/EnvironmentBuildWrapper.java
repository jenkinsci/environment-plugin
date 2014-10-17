package com.cloudbees.jenkins.plugins.environment;

import com.cloudbees.jenkins.plugins.environment.producers.EnvironmentProducer;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Environment;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildWrapper;
import hudson.tasks.BuildWrapperDescriptor;
import hudson.tasks.Builder;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Add environment variables to the build as a build step. Such variables will only be available to BuildSteps /
 * Publishers to run after this build step.
 *
 * @author <a href="mailto:nicolas.deloof@gmail.com">Nicolas De Loof</a>
 */
public class EnvironmentBuildWrapper extends BuildWrapper {

    private final Collection<EnvironmentProducer> producers;

    @DataBoundConstructor
    public EnvironmentBuildWrapper(Collection<EnvironmentProducer> producers) {
        this.producers = producers;
    }

    public Collection<EnvironmentProducer> getProducers() {
        return producers;
    }

    @Override
    public Environment setUp(final AbstractBuild build, Launcher launcher, final BuildListener listener) throws IOException, InterruptedException {

        listener.getLogger().println("Prepare environment for the build");
        final Collection<hudson.model.Environment> envs = new ArrayList<hudson.model.Environment>();
        for (EnvironmentProducer producer : producers) {
            listener.getLogger().println("  - inject environment from " + producer.getDescription());
            envs.add(producer.buildEnvironmentFor(build, listener));
        }

        return new EnvironmentImpl(envs);
    }

    @Extension
    public static final class DescriptorImpl extends BuildWrapperDescriptor {

        @Override
        public boolean isApplicable(AbstractProject<?, ?> item) {
            return true;
        }

        public String getDisplayName() {
            return "Prepare environment for the build";
        }
    }

    private class EnvironmentImpl extends Environment {
        private final Collection<hudson.model.Environment> envs;

        public EnvironmentImpl(Collection<hudson.model.Environment> envs) {
            this.envs = envs;
        }

        @Override
        public void buildEnvVars(Map<String, String> env) {
            for (hudson.model.Environment environment : envs) {
                environment.buildEnvVars(env);
            }
        }
    }
}

