package com.cloudbees.jenkins.plugins.environment;

import com.cloudbees.jenkins.plugins.environment.producers.EnvironmentProducer;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Environment;
import hudson.tasks.BuildWrapperDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.util.Collection;

/**
 * @author <a href="mailto:nicolas.deloof@gmail.com">Nicolas De Loof</a>
 */
public class PreSCMEnvironmentBuildWrapper extends AbstractEnvironmentBuildWrapper {

    @DataBoundConstructor
    public PreSCMEnvironmentBuildWrapper(Collection<EnvironmentProducer> producers) {
        super(producers);
    }

    @Override
    public void preCheckout(AbstractBuild build, Launcher launcher, BuildListener listener) throws IOException, InterruptedException {
        build.getEnvironments().addAll(prepareEnvironments(build, listener));
    }

    @Extension(ordinal = 113)
    public static final class DescriptorImpl extends BuildWrapperDescriptor {

        @Override
        public boolean isApplicable(AbstractProject<?, ?> item) {
            return true;
        }

        public String getDisplayName() {
            return "Prepare environment for the build (before SCM checkout)";
        }
    }
}
