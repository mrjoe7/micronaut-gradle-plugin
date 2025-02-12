package io.micronaut.gradle.docker;

import io.micronaut.gradle.docker.editor.Editor;
import org.gradle.api.Action;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Internal;

/**
 * Build options for Docker.
 *
 * @author gkrocher
 * @since 1.0.0
 */
public interface DockerBuildOptions {
    /**
     * @return The arguments to use
     */
    @Input
    ListProperty<String> getArgs();

    /**
     * @return The base image to use
     */
    @Input
    Property<String> getBaseImage();

    /**
     * @return The default command to use
     */
    @Input
    Property<String> getDefaultCommand();

    /**
     * @return The exposed ports
     */
    @Input
    ListProperty<Integer> getExposedPorts();

    @Internal
    ListProperty<Action<? super Editor>> getDockerfileTweaks();

    /**
     * Arguments for the entrypoint.
     * @param args The arguments
     * @return This
     */
    DockerBuildOptions args(String... args);

    /**
     * The base image to use.
     * @param imageName The base image name
     * @return This
     */
    DockerBuildOptions baseImage(String imageName);

    /**
     * @param ports The ports to expose
     * @return The ports
     */
    DockerBuildOptions exportPorts(Integer... ports);

    /**
     * The working directory to use in the container.
     * Defaults to /home/app
     * @return the target directory
     */
    Property<String> getTargetWorkingDirectory();

    /**
     * Adds a dockerfile tweak.
     * @param action the edition action
     */
    default void editDockerfile(Action<? super Editor> action) {
        getDockerfileTweaks().add(action);
    }
}
