package org.eclipse.cdt.internal.corext.template;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */

import org.eclipse.core.runtime.CoreException;

/**
 * A template editor implements an action to edit a template buffer in its context.
 */
public interface ITemplateEditor {

	/**
	 * Modifies a template buffer.
	 * 
	 * @param buffer the template buffer
	 * @param context the template context
	 * @throws CoreException if the buffer cannot be successfully modified
	 */
	void edit(TemplateBuffer buffer, TemplateContext context) throws CoreException;
	
	public class TemplateContextKind {
		public static final String C_GLOBAL_CONTEXT_TYPE = "C Global"; //$NON-NLS-1$
		public static final String C_FUNCTION_CONTEXT_TYPE = "C Function"; //$NON-NLS-1$
		public static final String C_STRUCTURE_CONTEXT_TYPE = "C Structure"; //$NON-NLS-1$
		public static final String CPP_GLOBAL_CONTEXT_TYPE = "C++ Global"; //$NON-NLS-1$
		public static final String CPP_FUNCTION_CONTEXT_TYPE = "C++ Function"; //$NON-NLS-1$
		public static final String CPP_STRUCTURE_CONTEXT_TYPE = "C++ Structure";  //$NON-NLS-1$
		
	}

}
