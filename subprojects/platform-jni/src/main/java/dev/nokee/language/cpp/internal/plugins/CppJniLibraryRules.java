package dev.nokee.language.cpp.internal.plugins;

import dev.nokee.language.jvm.internal.JvmResourceSetInternal;
import dev.nokee.language.nativebase.internal.HeaderExportingSourceSetInternal;
import dev.nokee.platform.jni.JniLibrary;
import dev.nokee.platform.jni.internal.JniLibraryInternal;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.attributes.Usage;
import org.gradle.api.internal.project.ProjectIdentifier;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.language.cpp.CppBinary;
import org.gradle.language.cpp.tasks.CppCompile;
import org.gradle.model.Finalize;
import org.gradle.model.Model;
import org.gradle.model.Mutate;
import org.gradle.model.RuleSource;
import org.gradle.nativeplatform.NativeLibrarySpec;
import org.gradle.nativeplatform.SharedLibraryBinarySpec;
import org.gradle.nativeplatform.StaticLibraryBinarySpec;
import org.gradle.nativeplatform.tasks.LinkSharedLibrary;
import org.gradle.platform.base.ComponentSpecContainer;
import org.gradle.platform.base.internal.BinarySpecInternal;

import java.util.Collection;
import java.util.stream.Collectors;

import static dev.nokee.platform.jni.internal.plugins.JniLibraryPlugin.getJvmIncludeRoots;

public class CppJniLibraryRules extends RuleSource {
    @Model
    public JniLibraryInternal library(ExtensionContainer extensions) {
        return (JniLibraryInternal)extensions.getByType(JniLibrary.class);
    }

    @Mutate
    public void createNativeLibrary(ComponentSpecContainer components, ProjectIdentifier projectIdentifier) {
        components.create("main", NativeLibrarySpec.class, library -> {
            library.setBaseName(projectIdentifier.getName());

            // Disable the StaticLibrary as we don't need it for JNI libraries
            library.getBinaries().withType(StaticLibraryBinarySpec.class, binary -> {
                ((BinarySpecInternal)binary).setBuildable(false);
            });
        });
    }

    @Mutate
    public void configureJniLibrary(JniLibraryInternal library, ComponentSpecContainer components, ProjectIdentifier projectIdentifier) {
        Collection<SharedLibraryBinarySpec> binaries = components.withType(NativeLibrarySpec.class).get("main").getBinaries().withType(SharedLibraryBinarySpec.class).values();
        if (binaries.size() > 1) {
            throw new IllegalStateException("More binaries than predicted");
        }
        library.getBinaries().addAll(binaries.stream().filter(SharedLibraryBinarySpec::isBuildable).collect(Collectors.toList()));


        Project project = (Project) projectIdentifier;
        ConfigurationContainer configurations = project.getConfigurations();

        /*
         * Define some configurations to present the outputs of this build
         * to other Gradle projects.
         */
        final Usage cppApiUsage = project.getObjects().named(Usage.class, Usage.C_PLUS_PLUS_API);
        final Usage linkUsage = project.getObjects().named(Usage.class, Usage.NATIVE_LINK);

        Configuration implementation = configurations.getByName("nativeImplementation");

        // incoming compile time headers - this represents the headers we consume
        Configuration cppCompile = project.getConfigurations().create("cppCompile", it -> {
            it.setCanBeConsumed(false);
            it.extendsFrom(implementation);
            it.getAttributes().attribute(Usage.USAGE_ATTRIBUTE, cppApiUsage);
        });

        // incoming linktime libraries (i.e. static libraries) - this represents the libraries we consume
        Configuration cppLinkDebug = project.getConfigurations().create("cppLinkDebug", it -> {
            it.setCanBeConsumed(false);
            it.extendsFrom(implementation);
            it.getAttributes().attribute(Usage.USAGE_ATTRIBUTE, linkUsage);
            it.getAttributes().attribute(CppBinary.DEBUGGABLE_ATTRIBUTE, true);
            it.getAttributes().attribute(CppBinary.OPTIMIZED_ATTRIBUTE, false);
        });

        library.getSources().withType(JvmResourceSetInternal.class, resourceSet -> {
            binaries.forEach(binary -> {
                resourceSet.getSource().from(binary.getSharedLibraryFile()).builtBy(binary);
            });
        });

        library.getBinaries().all(binary -> {
            binary.getTasks().withType(CppCompile.class, task -> {
                // configure includes using the native incoming compile configuration
                task.setDebuggable(true);
                task.setOptimized(false);
                task.includes(cppCompile);
                library.getSources().withType(HeaderExportingSourceSetInternal.class, sourceSet -> task.getIncludes().from(sourceSet.getSource()));
                getJvmIncludeRoots().forEach(includeRoot -> task.getIncludes().from(includeRoot));
            });

            binary.getTasks().withType(LinkSharedLibrary.class, task -> {
                task.getLibs().from(cppLinkDebug);
            });
        });
    }

    @Finalize
    public void forceResolve(TaskContainer tasks, JniLibraryInternal library) {

    }
}
