/* *******************************************************************************
 * Copyright (c) 2006 IBM Corporation. All rights reserved.
 * This program and the accompanying materials are made available under the terms
 * of the Eclipse Public License v1.0 which accompanies this distribution, and is 
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * David Dykstal (IBM) - initial contribution.
 * *******************************************************************************/
package org.eclipse.rse.tests.framework;

import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class TestFrameworkPlugin extends AbstractUIPlugin {

	private static TestFrameworkPlugin plugin;
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * @return the shared instance of this plugin.
	 */
	public static TestFrameworkPlugin getDefault() {
		return plugin;
	}

	/**
	 * Logs an unexpected exception.
	 * @param e the exception to log
	 */
	public void logUnexpectedException(Exception e) {
		e.printStackTrace();
		String id = getBundle().getSymbolicName();
		Status status = new Status(Status.ERROR, id, 0, "Unexpected Exception", e);
		getLog().log(status);
	}
	
}
