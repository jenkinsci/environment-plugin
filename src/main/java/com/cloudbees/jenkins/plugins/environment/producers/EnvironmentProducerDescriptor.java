package com.cloudbees.jenkins.plugins.environment.producers;

import hudson.model.Descriptor;
import jenkins.model.Jenkins;

import java.util.List;

/**
 * @author <a href="mailto:nicolas.deloof@gmail.com">Nicolas De Loof</a>
 */
public abstract class EnvironmentProducerDescriptor extends Descriptor<EnvironmentProducer> {

    public static List<EnvironmentProducerDescriptor> all() {
        return Jenkins.getInstance().getDescriptorList(EnvironmentProducer.class);
    }
}
