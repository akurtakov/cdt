/* *******************************************************************************
 * Copyright (c) 2006 IBM Corporation. All rights reserved.
 * This program and the accompanying materials are made available under the terms
 * of the Eclipse Public License v1.0 which accompanies this distribution, and is 
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * David Dykstal (IBM) - initial contribution.
 * *******************************************************************************/
package org.eclipse.rse.tests.framework.scripting;

import java.net.URL;
import java.text.MessageFormat;

import junit.framework.TestSuite;

import org.eclipse.rse.tests.framework.DelegatingTestSuiteHolder;
import org.osgi.framework.Bundle;

/**
 * A script test suite holder is holds a scripted test case which is present in a file 
 * referenced by the extension point.
 */
public class ScriptTestSuiteHolder extends DelegatingTestSuiteHolder {
		
	private TestSuite suite;
	
	/* (non-Javadoc)
	 * @see org.eclipse.rse.tests.framework.AbstractTestSuiteHolder#getTestSuite()
	 */
	public TestSuite getTestSuite() {
		if (suite == null) {
			String folderName = getStringValue("folder");
			if (folderName != null) {
				if (!folderName.endsWith("/")) {
					folderName += "/";
				}
				String scriptName = getStringValue("script");
				if (scriptName == null) {
					scriptName = "script.txt";
				}
				Bundle bundle = getBundle();
				URL resourceLocation = bundle.getEntry(folderName);
				ScriptContext context = new ConsoleContext(System.out, resourceLocation);
				URL scriptLocation = context.getResourceURL(scriptName);
				ScriptTestCase test = new ScriptTestCase(context, scriptLocation);
				String title = MessageFormat.format("Script from folder {0}", new String[] {folderName});
				suite = new TestSuite(title);
				suite.addTest(test);
			} else {
				suite = new TestSuite("ERROR: Missing folder argument");
			}
		}
		return suite;
	}
}
