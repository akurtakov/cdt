/*
 *(c) Copyright QNX Software Systems Ltd. 2002.
 * All Rights Reserved.
 * 
 */
package org.eclipse.cdt.debug.ui.sourcelookup;

import org.eclipse.cdt.debug.core.model.ICDebugTarget;
import org.eclipse.cdt.debug.core.sourcelookup.ICSourceLocation;
import org.eclipse.cdt.debug.core.sourcelookup.ICSourceLocator;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.PropertyPage;

/**
 * 
 * Enter type comment.
 * 
 * @since Dec 18, 2002
 */
public class SourcePropertyPage extends PropertyPage
{
	private SourceLookupBlock fBlock = null;
	
	private boolean fHasActiveContents = false;

	/**
	 * Constructor for SourcePropertyPage.
	 */
	public SourcePropertyPage()
	{
		noDefaultAndApplyButton();
		fBlock = new SourceLookupBlock();
	}

	/**
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(Composite)
	 */
	protected Control createContents( Composite parent )
	{
		ICDebugTarget target = getDebugTarget();
		if ( target == null || target.isTerminated() || target.isDisconnected() )
		{
			return createTerminatedContents( parent );
		}
		fHasActiveContents = true;
		return createActiveContents( parent );
	}

	protected Control createTerminatedContents( Composite parent )
	{
		Label label= new Label( parent, SWT.LEFT );
		label.setText( "Terminated." );
		return label;
	}

	protected Control createActiveContents( Composite parent )
	{
		fBlock.initialize( getSourceLocations() );
		fBlock.createControl( parent );
		return fBlock.getControl();
	}
	
	protected ICDebugTarget getDebugTarget()
	{
		IAdaptable element = getElement();
		if ( element != null )
		{
			return (ICDebugTarget)element.getAdapter( ICDebugTarget.class );
		}
		return null;
	}
	
	private ICSourceLocation[] getSourceLocations()
	{
		ICDebugTarget target = getDebugTarget();
		if ( target != null )
		{
			if ( target.getLaunch().getSourceLocator() instanceof IAdaptable )
			{
				ICSourceLocator locator = (ICSourceLocator)((IAdaptable)target.getLaunch().getSourceLocator()).getAdapter( ICSourceLocator.class );
				if ( locator != null )
				{
					return locator.getSourceLocations();
				}
			}
		}
		return new ICSourceLocation[0];
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	public boolean performOk()
	{
		setSourceLocations( fBlock.getSourceLocations() );
		return true;
	}
	
	private void setSourceLocations( ICSourceLocation[] locations )
	{
		ICDebugTarget target = getDebugTarget();
		if ( target != null )
		{
			if ( target.getLaunch().getSourceLocator() instanceof IAdaptable )
			{
				ICSourceLocator locator = (ICSourceLocator)((IAdaptable)target.getLaunch().getSourceLocator()).getAdapter( ICSourceLocator.class );
				if ( locator != null )
				{
					locator.setSourceLocations( locations );
				}
			}
		}
	}
}
