/*******************************************************************************
 * Copyright (c) 2002, 2003, 2004 QNX Software Systems Ltd. and others. All
 * rights reserved. This program and the accompanying materials are made
 * available under the terms of the Common Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: QNX Software Systems - Initial API and implementation
 ******************************************************************************/
package org.eclipse.cdt.internal.ui.dialogs.cpaths;

import java.util.Arrays;

import org.eclipse.cdt.internal.ui.CPluginImages;
import org.eclipse.cdt.internal.ui.util.SelectionUtil;
import org.eclipse.cdt.internal.ui.viewsupport.ListContentProvider;
import org.eclipse.cdt.ui.CUIPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
  */
public class CPathContainerSelectionPage extends WizardPage {

	private static final String DIALOGSTORE_SECTION= "CPathContainerSelectionPage"; //$NON-NLS-1$
	private static final String DIALOGSTORE_CONTAINER_IDX= "index"; //$NON-NLS-1$


	private static class ClasspathContainerLabelProvider extends LabelProvider {
		public String getText(Object element) {
			return ((CPathContainerDescriptor) element).getName();
		}
	}

	private static class ClasspathContainerSorter extends ViewerSorter {
	}

	private ListViewer fListViewer;
	private CPathContainerDescriptor[] fContainers;
	private IDialogSettings fDialogSettings;

	/**
	 * Constructor for ClasspathContainerWizardPage.
	 * @param containerPages
	 */
	protected CPathContainerSelectionPage(CPathContainerDescriptor[] containerPages) {
		super("CPathContainerWizardPage"); //$NON-NLS-1$
		setTitle(CPathEntryMessages.getString("CPathContainerSelectionPage.title")); //$NON-NLS-1$
		setDescription(CPathEntryMessages.getString("CPathContainerSelectionPage.description")); //$NON-NLS-1$
		setImageDescriptor(CPluginImages.DESC_WIZBAN_ADD_LIBRARY);

		fContainers= containerPages;

		IDialogSettings settings= CUIPlugin.getDefault().getDialogSettings();
		fDialogSettings= settings.getSection(DIALOGSTORE_SECTION);
		if (fDialogSettings == null) {
			fDialogSettings= settings.addNewSection(DIALOGSTORE_SECTION);
			fDialogSettings.put(DIALOGSTORE_CONTAINER_IDX, 0);
		}
		validatePage();
	}

	/* (non-Javadoc)
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		fListViewer= new ListViewer(parent, SWT.SINGLE | SWT.BORDER);
		fListViewer.setLabelProvider(new ClasspathContainerLabelProvider());
		fListViewer.setContentProvider(new ListContentProvider());
		fListViewer.setSorter(new ClasspathContainerSorter());
		fListViewer.setInput(Arrays.asList(fContainers));
		fListViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				validatePage();
			}
		});
		fListViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doDoubleClick();
			}
		});		
		
		int selectionIndex= fDialogSettings.getInt(DIALOGSTORE_CONTAINER_IDX);
		if (selectionIndex >= fContainers.length) {
			selectionIndex= 0;
		}
		fListViewer.getList().select(selectionIndex);
		validatePage();
		setControl(fListViewer.getList());
		Dialog.applyDialogFont(fListViewer.getList());
	}

	/**
	 * Method validatePage.
	 */
	private void validatePage() {
		setPageComplete(getSelected() != null);
	}


	public CPathContainerDescriptor getSelected() {
		if (fListViewer != null) {
			ISelection selection= fListViewer.getSelection();
			return (CPathContainerDescriptor) SelectionUtil.getSingleElement(selection);
		}
		return null;
	}
	
	protected void doDoubleClick() {
		if (canFlipToNextPage()) {
			getContainer().showPage(getNextPage());
		}
	}	

	/* (non-Javadoc)
	 * @see IWizardPage#canFlipToNextPage()
	 */
	public boolean canFlipToNextPage() {
		return isPageComplete(); // avoid the getNextPage call to prevent potential plugin load
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#setVisible(boolean)
	 */
	public void setVisible(boolean visible) {
		if (!visible && fListViewer != null) {
			fDialogSettings.put(DIALOGSTORE_CONTAINER_IDX, fListViewer.getList().getSelectionIndex());
		}
		super.setVisible(visible);
	}

}
