package jce.manipulation.origincode;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;

import jce.properties.EcorificationProperties;

/**
 * Changes the inheritance of the origin code to let the original classes inherit from the generated unification
 * classes.
 * @author Timur Saglam
 */
public class InheritanceManipulator extends OriginCodeManipulator {

    /**
     * Simple constructor that sets the properties.
     * @param properties are the {@link EcorificationProperties}.
     */
    public InheritanceManipulator(EcorificationProperties properties) {
        super(properties);
    }

    @Override
    protected void manipulate(ICompilationUnit unit) throws JavaModelException {
        applyVisitorModifications(unit, new InheritanceManipulationVisitor(unit.getParent().getElementName(), properties));
    }
}