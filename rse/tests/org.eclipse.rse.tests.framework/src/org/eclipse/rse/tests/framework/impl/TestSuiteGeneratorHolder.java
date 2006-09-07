/* *******************************************************************************
 * Copyright (c) 2006 IBM Corporation. All rights reserved.
 * This program and the accompanying materials are made available under the terms
 * of the Eclipse Public License v1.0 which accompanies this distribution, and is 
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * David Dykstal (IBM) - initial contribution.
 * *******************************************************************************/
package org.eclipse.rse.tests.framework.impl;

import junit.framework.TestSuite;

import org.eclipse.rse.tests.framework.DelegatingTestSuiteHolder;
import org.eclipse.rse.tests.framework.ITestSuiteProvider;

/**
 * A suite generator holder can deliver a test suite when asked.  It uses a suite
 * generator supplied by an extension point to build that test suite.
 */
public class TestSuiteGeneratorHolder extends DelegatingTestSuiteHolder {
	
	private TestSuite suite;
	
	/* (non-Javadoc)
	 * @see org.eclipse.rse.tests.framework.AbstractTestSuiteHolder#getTestSuite()
	 */
	public TestSuite getTestSuite() {
		if (suite == null) {
			ITestSuiteProvider p = (ITestSuiteProvider)getObjectValue("class");
			String arg = getStringValue("argument");
			suite = p.getSuite(arg);
		}
		return suite;
	}

}


