package edu.utah.sci.cyclist.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import edu.utah.sci.cyclist.event.notification.EventBus;
import edu.utah.sci.cyclist.presenter.DatasourcesPresenter;
import edu.utah.sci.cyclist.presenter.WorkspacePresenter;
import edu.utah.sci.cyclist.view.MainScreen;
import edu.utah.sci.cyclist.model.CyclistDatasource;
import edu.utah.sci.cyclist.model.Model;
import edu.utah.sci.cyclist.model.Table;
import edu.utah.sci.cyclist.view.components.DnDIcon;
import edu.utah.sci.cyclist.view.components.Workspace;
import edu.utah.sci.cyclist.view.wizard.DatasourceWizard;
import edu.utah.sci.cyclist.view.wizard.DatatableWizard;

public class CyclistController {

	private final EventBus _eventBus;
	private MainScreen _screen;
	private Model _model = new Model();
	
	private ObservableList<String> _workspaces = FXCollections.observableArrayList();
	
	/**
	 * Constructor
	 * 
	 * @param eventBus
	 */
	public CyclistController(EventBus eventBus) {
		this._eventBus = eventBus;
		_workspaces.add("/Users/yarden/software");
		_workspaces.add("/Users/yarden");
	}

	/**
	 * initialize the main screen
	 * @param screen
	 */
	public void setScreen(final MainScreen screen) {
		this._screen = screen;
		screen.setControler(this);
		addActions();
		
		DnDIcon.getInstance().setRoot(screen);
		
		// wire panels
		DatasourcesPresenter ds = new DatasourcesPresenter(_eventBus);
		ds.setSources(_model.getSources());
		ds.setTables(_model.getTables());
		ds.setView(screen.getDatasourcesPanel());
		
		// set up the main workspace
		Workspace workspace = new Workspace();
		screen.setWorkspace(workspace);
		
		WorkspacePresenter presenter = new WorkspacePresenter(_eventBus, _model);
		presenter.setView(workspace);
		
		// do something?
		//selectWorkspace();
	}
	
	/**
	 * selectWorkspace
	 * 
	 */
	public void selectWorkspace() {
		ObjectProperty<String> selection = _screen.selectWorkspace(_workspaces);
		selection.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String oldVal, String newVal) {
				System.out.println("select:"+newVal);
				
			}
		});
	}	
	
	public void quit() {
		System.exit(0);
	}

	
	private void addActions() {
		_screen.onAddDatasource().set(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				final DatatableWizard wizard = new DatatableWizard();
				wizard.setItems(_model.getSources());
				ObjectProperty<Table> selection = wizard.show(_screen.getWindow());
				
				selection.addListener(new ChangeListener<Table>() {
					@Override
					public void changed(ObservableValue<? extends Table> arg0, Table oldVal, Table newVal) {
						System.out.println("add table: "+newVal.getName());
						_model.getTables().add(newVal);
					}
				});	
				
			}
		});
	}
}
