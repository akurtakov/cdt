/**********************************************************************
 * Copyright (c) 2004 Rational Software Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Common Public License v0.5
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v05.html
 * 
 * Contributors: 
 * IBM Rational Software - Initial API and implementation
***********************************************************************/
package org.eclipse.cdt.internal.corext.refactoring;

import org.eclipse.cdt.core.CConventions;
import org.eclipse.cdt.core.model.CModelException;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.ICElement;
import org.eclipse.cdt.core.model.ITranslationUnit;
import org.eclipse.cdt.internal.corext.refactoring.base.RefactoringStatus;
import org.eclipse.cdt.internal.ui.util.Resources;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;

/**
 * This class defines a set of reusable static checks methods.
 */
public class Checks {
	
	/*
	 * no instances
	 */
	private Checks(){
	}
	
	/* Constants returned by checkExpressionIsRValue */
	public static final int IS_RVALUE= 0;
	public static final int NOT_RVALUE_MISC= 1;
	public static final int NOT_RVALUE_VOID= 2;
		
	/**
	 * Checks if the given name is a valid C field name.
	 *
	 * @param the java field name.
	 * @return a refactoring status containing the error message if the
	 *  name is not a valid java field name.
	 */
	public static RefactoringStatus checkFieldName(String name) {
		return checkName(name, CConventions.validateFieldName(name));
	}

	/**
	 * Checks if the given name is a valid C identifier.
	 *
	 * @param the java identifier.
	 * @return a refactoring status containing the error message if the
	 *  name is not a valid java identifier.
	 */
	public static RefactoringStatus checkIdentifier(String name) {
		return checkName(name, CConventions.validateIdentifier(name));
	}
	
	/**
	 * Checks if the given name is a valid C method name.
	 *
	 * @param the java method name.
	 * @return a refactoring status containing the error message if the
	 *  name is not a valid java method name.
	 */
	public static RefactoringStatus checkMethodName(String name) {
		RefactoringStatus status= checkName(name, CConventions.validateMethodName(name));
		if (status.isOK() && startsWithUpperCase(name))
			return RefactoringStatus.createWarningStatus(RefactoringCoreMessages.getString("Checks.method_names_lowercase")); //$NON-NLS-1$
		else	
			return status;
	}
		
	/**
	 * Checks if the given name is a valid C type name.
	 *
	 * @param the java method name.
	 * @return a refactoring status containing the error message if the
	 *  name is not a valid java type name.
	 */
	public static RefactoringStatus checkClassName(String name) {
		//fix for: 1GF5Z0Z: ITPJUI:WINNT - assertion failed after renameType refactoring
		if (name.indexOf(".") != -1) //$NON-NLS-1$
			return RefactoringStatus.createFatalErrorStatus(RefactoringCoreMessages.getString("Checks.no_dot"));//$NON-NLS-1$
		else	
			return checkName(name, CConventions.validateClassName(name));
	}
		
		
	private static boolean startsWithUpperCase(String s) {
		if (s == null)
			return false;
		else if ("".equals(s)) //$NON-NLS-1$
			return false;
		else
			//workaround for JDK bug (see 26529)
			return s.charAt(0) == Character.toUpperCase(s.charAt(0));
	}
		
	public static boolean startsWithLowerCase(String s){
		if (s == null)
			return false;
		else if ("".equals(s)) //$NON-NLS-1$
			return false;
		else
			//workaround for JDK bug (see 26529)
			return s.charAt(0) == Character.toLowerCase(s.charAt(0));
	}

	public static boolean resourceExists(IPath resourcePath){
		return ResourcesPlugin.getWorkspace().getRoot().findMember(resourcePath) != null;
	}
	

	public static boolean isAlreadyNamed(ICElement element, String name){
		return name.equals(element.getElementName());
	}

	//---- Private helpers ----------------------------------------------------------------------
	
	private static RefactoringStatus checkName(String name, IStatus status) {
		RefactoringStatus result= new RefactoringStatus();
		if ("".equals(name)) //$NON-NLS-1$
			return RefactoringStatus.createFatalErrorStatus(RefactoringCoreMessages.getString("Checks.Choose_name")); //$NON-NLS-1$

		if (status.isOK())
			return result;
		
		switch (status.getSeverity()){
			case IStatus.ERROR: 
				return RefactoringStatus.createFatalErrorStatus(status.getMessage());
			case IStatus.WARNING: 
				return RefactoringStatus.createWarningStatus(status.getMessage());
			case IStatus.INFO:
				return RefactoringStatus.createInfoStatus(status.getMessage());
			default: //no nothing
				return new RefactoringStatus();
		}
	}
	
	public static RefactoringStatus checkIfTuBroken(ICElement element) throws CModelException{
		ITranslationUnit tu= (ITranslationUnit)CoreModel.getDefault().create(element.getUnderlyingResource());
		if (tu == null)
			return RefactoringStatus.createFatalErrorStatus(RefactoringCoreMessages.getString("Checks.cu_not_created"));	 //$NON-NLS-1$
		else if (! tu.isStructureKnown())
			return RefactoringStatus.createFatalErrorStatus(RefactoringCoreMessages.getString("Checks.cu_not_parsed"));	 //$NON-NLS-1$
		return new RefactoringStatus();
	}
	//-------- validateEdit checks ----
	
	public static RefactoringStatus validateModifiesFiles(IFile[] filesToModify) {
		RefactoringStatus result= new RefactoringStatus();
		IStatus status= Resources.checkInSync(filesToModify);
		if (!status.isOK())
			result.merge(RefactoringStatus.create(status));
		status= Resources.makeCommittable(filesToModify, null);
		if (!status.isOK())
			result.merge(RefactoringStatus.create(status));
		return result;
	}
	
	public static boolean isAvailable(ICElement cElement) throws CModelException {
		if (cElement == null)
			return false;
		if (! cElement.exists())
			return false;
		if (cElement.isReadOnly())
			return false;
		return true;
	}
}
