package org.silverbulleters.dt.silverlint.ui.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.Adapters;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.silverbulleters.dt.silverlint.ui.views.RuleDescriptionWebView;

public class ShowDescriptionHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelectionChecked(event);
		
		List<IMarker> selectedSonarMarkers = new ArrayList<>();

	    @SuppressWarnings("rawtypes")
	    List elems = selection.toList();
	    for (Object elem : elems) {
	      IMarker marker = Adapters.adapt(elem, IMarker.class);
	      if (marker != null) {
	        selectedSonarMarkers.add(marker);
	      }
	    }

	    if (!selectedSonarMarkers.isEmpty()) {
	      IMarker marker = selectedSonarMarkers.get(0);
	      try {
	          RuleDescriptionWebView view = (RuleDescriptionWebView) PlatformUI.getWorkbench()
	        		  .getActiveWorkbenchWindow().getActivePage().showView(RuleDescriptionWebView.ID);
	          view.setInput(marker);
	        } catch (Exception e) {
	          System.out.println(e.getMessage());
	        }
	    }
		return null;
	}
}
