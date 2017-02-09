package jce;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;

import eme.EcoreMetamodelExtraction;
import eme.generator.GeneratedEcoreMetamodel;
import eme.properties.BinaryProperty;
import eme.properties.ExtractionProperties;
import eme.properties.TextProperty;
import jce.codegen.GenModelGenerator;
import jce.codegen.ModelCodeGenerator;
import jce.codegen.WrapperManager;

/**
 * Main class for Java code ecorification.
 * @author Timur Saglam
 */
public class JavaCodeEcorification {
    private static final Logger logger = LogManager.getLogger(JavaCodeEcorification.class.getName());
    private final ExtractionProperties extractionProperties;
    private final GenModelGenerator genModelGenerator;
    private final EcoreMetamodelExtraction metamodelGenerator;

    /**
     * Basic constructor.
     */
    public JavaCodeEcorification() {
        metamodelGenerator = new EcoreMetamodelExtraction();
        genModelGenerator = new GenModelGenerator();
        extractionProperties = metamodelGenerator.getProperties();
        extractionProperties.set(TextProperty.SAVING_STRATEGY, "SameProject");
        extractionProperties.set(TextProperty.DEFAULT_PACKAGE, "ecore");
        extractionProperties.set(BinaryProperty.DUMMY_CLASS, false);
    }

    /**
     * Starts the ecorification for a specific Java project.
     * @param project is the specific Java project as {@link IProject}.
     */
    public void start(IProject project) {
        check(project);
        logger.info("Starting Ecorification...");
        GeneratedEcoreMetamodel metamodel = metamodelGenerator.extractAndSaveFrom(copy(project));
        GenModel genModel = genModelGenerator.generate(metamodel);
        ModelCodeGenerator.generate(genModel);
        new WrapperManager(metamodel, genModel).buildWrappers();
    }

    /**
     * Checks whether a specific {@link IProject} is valid (neither null nor nonexistent)
     * @param project is the specific {@link IProject}.
     */
    private void check(IProject project) {
        if (project == null) {
            throw new IllegalArgumentException("Project can't be null!");
        } else if (!project.exists()) {
            throw new IllegalArgumentException("Project " + project.toString() + "does not exist!");
        }
    }

    /**
     * Copies an specific {@link IProject}.
     * @param project is the specific {@link IProject} to copy to.
     * @return the copy of the original {@link IProject}.
     */
    private IProject copy(IProject project) {
        IProject copy = null;
        try {
            IPath newPath = new Path(project.getFullPath() + "Ecorified");
            project.copy(newPath, false, null);
            logger.info("Copied the project...");
            IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
            copy = workspaceRoot.getProject(newPath.toString());
        } catch (CoreException exception) {
            logger.fatal(exception);
        }
        return copy;
    }
}