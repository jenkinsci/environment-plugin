package com.cloudbees.jenkins.plugins.environment.producers;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.Environment;
import hudson.model.TaskListener;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.Properties;

/**
 * Produce environment variables as set by user as a java.util.Properties key/value pairs set
 * @author <a href="mailto:nicolas.deloof@gmail.com">Nicolas De Loof</a>
 */
public class KeyValueProducer extends EnvironmentProducer {

    private final String value;

    private final Properties properties;


    // TODO expose the key/value pairs as an editable table in UI. Can't get it to work yet, will look into this later.

    @DataBoundConstructor
    public KeyValueProducer(String value) throws IOException {
        super();
        this.value = value;
        this.properties = new Properties();
        properties.load(new StringReader(value));
    }

    public Properties getProperties() {
        return properties;
    }

    @Override
    public Environment buildEnvironmentFor(AbstractBuild build, TaskListener listener) {
        return new Environment() {
            @Override
            public void buildEnvVars(Map env) { // intentionaly not  parameterized, as Properties is not a Map<String,String> for some odd reason
                env.putAll(properties);
            }
        };
    }

    @Override
    public String getDescription() {
        return "Key=Value pairs";
    }

    public String getValue() {
        return value;
    }

    @Extension
    public static class DescriptorImpl extends EnvironmentProducerDescriptor {

        @Override
        public String getDisplayName() {
            return "set of key=value";
        }
    }
}
