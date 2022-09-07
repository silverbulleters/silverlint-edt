/*
 * SonarLint for Eclipse
 * Copyright (C) 2015-2020 SonarSource SA
 * sonarlint@sonarsource.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.silverbulleters.dt.silverlint.ui.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Adapters;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.silverbulleters.dt.silverlint.ui.BSLPlugin;
import org.silverbulleters.dt.silverlint.ui.utils.SonarLintRuleBrowser;

public class RuleDescriptionWebView extends ViewPart implements ISelectionListener {
	
	public static final String ID = "org.silverbulleters.dt.silverlint.ui.views.RuleDescriptionWebView";

	protected IMarker currentElement;

	private boolean linking = true;

	/**
	 * The last selected element if linking was disabled.
	 */
	private IMarker lastSelection;

	private SonarLintRuleBrowser browser;

	public void setInput(IMarker marker) {
		currentElement = marker;
		if (marker != null) {
			showRuleDescription(marker);
		} else {
			clear();
		}
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		IMarker selectedMarker = findSelectedSonarLintMarker(selection);
		if (selectedMarker != null) {
			lastSelection = selectedMarker;
			if (linking && !Objects.equals(selectedMarker, currentElement)) {
				setInput(selectedMarker);
			}
		}
	}
	
	@Override
	public void dispose() {
		stopListeningForSelectionChanges();

		super.dispose();
	}

	@Override
	public void createPartControl(Composite parent) {
	    createToolbar();
	    browser = new SonarLintRuleBrowser(parent, true);

	    startListeningForSelectionChanges();
	}

	@Override
	public void setFocus() {
		 browser.setFocus();
	}

	protected void startListeningForSelectionChanges() {
		getSite().getPage().addPostSelectionListener(this);
	}

	protected void stopListeningForSelectionChanges() {
		getSite().getPage().removePostSelectionListener(this);
	}
	
	/**
	 * Sets whether this info view reacts to selection changes in the workbench.
	 *
	 * @param enabled if true then the input is set on selection changes
	 * @throws CoreException
	 */
	protected void setLinkingEnabled(boolean enabled) {
		linking = enabled;
		if (linking && (lastSelection != null)) {
			setInput(lastSelection);
		}
	}
	
	private void createToolbar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
		toolbarManager.add(new LinkAction());
		toolbarManager.add(new Separator());
		toolbarManager.update(false);
	}

	private class LinkAction extends Action {
		private static final String LINK_WITH_SELECTION = "Link with Selection";

		public LinkAction() {
			super(LINK_WITH_SELECTION, IAction.AS_CHECK_BOX);
			setTitleToolTip(LINK_WITH_SELECTION);
			setImageDescriptor(
					PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ELCL_SYNCED));
			setChecked(linking);
		}

		@Override
		public void run() {
			setLinkingEnabled(!linking);
		}
	}
	
	private IMarker findSelectedSonarLintMarker(ISelection selection) {
		try {
			if (selection instanceof IStructuredSelection) {
				List<IMarker> selectedSonarMarkers = new ArrayList<>();

				@SuppressWarnings("rawtypes")
				List elems = ((IStructuredSelection) selection).toList();
				for (Object elem : elems) {
					processElement(selectedSonarMarkers, elem);
				}

				if (!selectedSonarMarkers.isEmpty()) {
					return selectedSonarMarkers.get(0);
				}
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	private void showRuleDescription(IMarker element) {
		
		String ruleKey;
		
		try {
			ruleKey = element.getAttribute("CODE_KEY").toString();
		} catch (CoreException e) {
			System.out.println("Unable to open rule description " + e);
			return;
		}
		
		var projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		if (projects.length == 0 || !ruleKey.startsWith("bsl:")) {
			return;
		}
		
		var project = projects[0];
		var core = BSLPlugin.getDefault().getCore();
		var service = core.getLintManager().getService(project);
		var ruleDetails = service.getRuleDescription(ruleKey);
		browser.updateRule(ruleDetails);
	}
	
	private static void processElement(List<IMarker> selectedSonarMarkers, Object elem) {
		IMarker marker = Adapters.adapt(elem, IMarker.class);
		if (marker != null) {
			selectedSonarMarkers.add(marker);
		}
	}

	private void clear() {
		browser.updateRule(null);
	}
}
