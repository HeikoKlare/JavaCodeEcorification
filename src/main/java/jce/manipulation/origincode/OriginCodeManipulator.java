package jce.manipulation.origincode;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IPackageFragment;

import jce.manipulation.CodeManipulator;
import jce.properties.EcorificationProperties;
import jce.properties.TextProperty;
import jce.util.PackageFilter;

/**
 * Base class for manipulating origin code.
 * @author Timur Saglam
 */
public abstract class OriginCodeManipulator extends CodeManipulator {

    /**
     * Simple constructor that sets the properties.
     * @param properties are the {@link EcorificationProperties}.
     */
    public OriginCodeManipulator(EcorificationProperties properties) {
        super(properties);
    }

    @Override
    protected List<IPackageFragment> filterPackages(IProject project, EcorificationProperties properties) {
        return PackageFilter.startsNotWith(project, properties.get(TextProperty.ECORE_PACKAGE), properties.get(TextProperty.WRAPPER_PACKAGE));
    }
}