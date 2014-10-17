package com.cloudbees.jenkins.plugins.environment.producers;

import hudson.Extension;
import hudson.FilePath;
import hudson.model.AbstractBuild;
import hudson.model.Environment;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.remoting.VirtualChannel;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * @author <a href="mailto:nicolas.deloof@gmail.com">Nicolas De Loof</a>
 */
public class PropertiesFileProducer extends EnvironmentProducer {

    private String path;

    private boolean onMaster;

    @DataBoundConstructor
    public PropertiesFileProducer(String path, boolean onMaster) {
        this.path = path;
        this.onMaster = onMaster;
    }

    public String getPath() {
        return path;
    }

    public boolean isOnMaster() {
        return onMaster;
    }

    @Override
    public Environment buildEnvironmentFor(Run run, TaskListener listener) throws IOException, InterruptedException {

        final Properties p = load(run, listener);

        return new Environment() {
            @Override
            public void buildEnvVars(Map env) { // intentionaly not parameterized, as Properties is not a Map<String,String> for some odd reason
                env.putAll(p);
            }
        };
    }

    private Properties load(Run run, TaskListener listener) throws IOException, InterruptedException {
        if (onMaster) {
            // TODO validate path is absolute
            return load(new File(path));
        }
        AbstractBuild build = (AbstractBuild) run;
        return build.getWorkspace().child(path).act(new FilePath.FileCallable<Properties>() {
            public Properties invoke(File f, VirtualChannel channel) throws IOException, InterruptedException {
                return load(f);
            }
        });
    }

    private static Properties load(File f) throws IOException {
        Properties p = new Properties();
        p.load(new FileInputStream(f));
        return p;
    }

    @Extension
    public static class DescriptorImpl extends EnvironmentProducerDescriptor {

        @Override
        public String getDisplayName() {
            return "from file";
        }
    }
}
