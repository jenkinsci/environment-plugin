package com.cloudbees.jenkins.plugins.environment.producers;

import hudson.ExtensionPoint;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Environment;
import hudson.model.Run;
import hudson.model.TaskListener;

/**
 * Extension point for components who define environment variables to be set during the build.
 * @author <a href="mailto:nicolas.deloof@gmail.com">Nicolas De Loof</a>
 */
public abstract class EnvironmentProducer extends AbstractDescribableImpl<EnvironmentProducer>
       implements ExtensionPoint {


    public abstract Environment buildEnvironmentFor(Run run, TaskListener listener);

}
