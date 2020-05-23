package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import gui.tools.ArrowHead;
import gui.tools.ComplexLine;
import gui.tools.DashedComplexLine;
import gui.tools.Element;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.object_source.DClass;
import logic.object_source.DGeneralClass;
import logic.object_source.DInterface;
import logic.object_source.DObject;
import logic.tools.DMethod;
import logic.tools.DProject;
import logic.tools.DProperty;
import logic.tools.ProjectManager;

/**
 * DMenuWizard class which controls menu events and has methods about it.
 * @author  Group 2F
 * @version 20.05.2020
 */
public class DMenuWizard {
	/**
	 * This method opens a new project for program
	 * @param d
	 */
	public void openProject( DObject d) {

		// sometimes foreshadowing is relatively obvious

	}

	/**
	 * This method displays the object options in the menu to create classes, methods or properties etc.
	 */
	public static void displayClassOptions() {
		Button create;
		TextField name;


		Stage window = new Stage();

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("New Class");
		window.setMinWidth(400);

		Label messageName = new Label( "Name of the class:");
		name = new TextField();

		Label messageInh = new Label( "This class extends:");
		ChoiceBox<String> choiceBox = new ChoiceBox<>();

		Label messageInh2 = new Label( "This class implements:");
		ChoiceBox<String> choiceBox2 = new ChoiceBox<>();

		// Going to add the elements in project
		choiceBox.getItems().add("Object");
		for ( DObject obj : DApp.project.getObjects())
		{
			if (obj instanceof DGeneralClass)
			{
				choiceBox.getItems().add( obj.getName());
			}
		}


		for ( DObject obj : DApp.project.getObjects())
		{
			if (obj instanceof DInterface)
			{

				choiceBox2.getItems().add( obj.getName());
			}

		}


		create = new Button("create object");

		create.setOnAction( e -> {
			try {
				getInheritanceChoice( choiceBox, createClass( name, choiceBox2));

			} catch (NullPointerException n) {
				window.close();
				n.printStackTrace();
			}
			window.close();
		});



		VBox layout = new VBox();
		layout.getChildren().addAll(  messageName, name, messageInh, choiceBox, messageInh2, choiceBox2, create);
		layout.setAlignment( Pos.CENTER);

		window.setScene( new Scene( layout));
		window.showAndWait();
	}

	public static void displayLoadOptions() {
		Button load;

		Stage window = new Stage();

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Open Project");
		window.setMinWidth(400);

		Label messageProject = new Label( "Projects");
		ChoiceBox<String> choiceBox = new ChoiceBox<>();

		File folder = new File("Saved Projects");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile())
			{
				choiceBox.getItems().add( listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 5));
			}
		}

		load = new Button("Load Project");

		load.setOnAction( e -> {
			try {
				cleanProjectView();
				loadProject(choiceBox.getValue());
				DApp.updateArrow();
				DApp.updateLines();
				DApp.updateOverlay();
				DApp.updateZoomPane();
				displayErrorMessage("Project Loaded Successfully!");

			} catch (NullPointerException n) {
				displayErrorMessage("Project Wasn't Loaded Successfully!");
				n.printStackTrace();
			}
			window.close();
		});



		VBox layout = new VBox();
		layout.getChildren().addAll( messageProject, choiceBox, load);
		layout.setAlignment( Pos.CENTER);

		window.setScene( new Scene( layout));
		window.showAndWait();
	}

	public static void displayInterfaceOptions() {
		Button create;
		TextField name;


		Stage window = new Stage();

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("New Interface");
		window.setMinWidth(400);

		Label messageName = new Label( "Name of the interface:");
		name = new TextField();

		Label messageInh = new Label( "This interface extends:");
		ChoiceBox<String> choiceBox = new ChoiceBox<>();

		// Going to add the elements in project
		for ( DObject obj : DApp.project.getObjects())
		{
			if (obj instanceof DInterface)
				choiceBox.getItems().add( obj.getName());
		}


		create = new Button("create interface");

		create.setOnAction( e -> {
			try {
				getInheritanceChoice( choiceBox, createInterface( name, choiceBox));
			} catch (NullPointerException n) {
				window.close();
				n.printStackTrace();
			}
			window.close();
		});



		VBox layout = new VBox();
		layout.getChildren().addAll(  messageName, name, messageInh, choiceBox, create);
		layout.setAlignment( Pos.CENTER);

		window.setScene( new Scene( layout));
		window.showAndWait();
	}

	/**
	 * This method creates new object in the program
	 * @param name
	 * @return DObject
	 */
	public static DObject createClass( TextField name, ChoiceBox<String> cb) {
		Element r;
		DClass object = new DClass( name.getText());

		object.setName(name.getText());

		Random rand = new Random();
		if ( object.getName() != null) {
			if ( DApp.selectedElement != null)
				r = new Element(  DApp.selectedElement.getLayoutX() + 30 + rand.nextInt(7) , DApp.selectedElement.getLayoutY()+ 30 + rand.nextInt(7),
						150, 150, Color.web(DApp.colors[0].substring(0, 1) + DApp.colors[rand.nextInt(10)].substring(1).toUpperCase(), 1.0), true);
			else
				r = new Element( DApp.offset + 0 - Math.random()*DApp.RANDOMNESS, DApp.offset + 0 - Math.random()*DApp.RANDOMNESS, 150, 150,
						Color.web(DApp.colors[0].substring(0, 1) + DApp.colors[rand.nextInt(10)].substring(1).toUpperCase(), 1.0), true);


			ProjectManager.connectElement(r, object);
			DApp.elements.add(r);
			DApp.project.addObject(object);

			DApp.group.getChildren().add(r);

			if (cb.getValue() != null)
			{
				String cbS = cb.getValue();
				Element lastAdded = null, selected = null;

				for ( Node n : DApp.group.getChildren()) {
					if ( n instanceof Element && ((Element) n).hasObject()){
						if ( (((Element) n).getObject().getName()).equals( object.getName()) )
							lastAdded = (Element)n;
					}
				}
				for ( Node n : DApp.group.getChildren()) {
					if ( n instanceof Element && ((Element) n).hasObject()){
						if ( (((Element) n).getObject().getName()).equals( cbS) )
						{
							selected = (Element)n;
							DInterface di = (DInterface) selected.getObject();
							object.addInterface(di);
						}
					}
				}

				DApp.drawCenteredDashedLine( lastAdded, selected);


				DApp.select(selected);
				DApp.select(lastAdded);
			}

			DApp.select(r);
		}
		else {
			displayErrorMessage( "Invalid Class Name Declaration");
			return null;
		}
		DApp.updateZoomPane();

		return object;
	}

	public static void editClassOptions()
	{
		if (DApp.selectedElement != null) {
			if (DApp.selectedElement.getObject() instanceof DGeneralClass) {
				Stage window = new Stage();
				window.initModality(Modality.APPLICATION_MODAL);
				window.initStyle(StageStyle.DECORATED);
				window.setTitle("Edit Class");
				window.setMinWidth(200);
				window.setMinHeight(160);

				Button removePropertyButton = new Button("Remove Property");
				Button removeMethodButton = new Button("Remove Method");
				Button editInheritanceButton = new Button("Edit Inheritance");
				Button editInterfaces = new Button("Edit Implemented Interface");

				ArrayList<Label> properties = new ArrayList<>();
				ArrayList<Label> methods = new ArrayList<>();

				VBox propertyVBox = new VBox();
				propertyVBox.getChildren().add( new Label("Properties: "));
				for (DProperty dp : DApp.selectedElement.getObject().getProperties()) {
					propertyVBox.getChildren().add(new Label( dp.getName()));
				}

				VBox methodVBox = new VBox();
				methodVBox.getChildren().add( new Label("Methods: "));
				for (DMethod dm : DApp.selectedElement.getObject().getMethods()) {
					methodVBox.getChildren().add(new Label(dm.getName()));
				}

				HBox propertiesHBox = new HBox();
				propertiesHBox.getChildren().addAll(propertyVBox, removePropertyButton);

				HBox methodsHBox = new HBox();
				methodsHBox.getChildren().addAll(methodVBox, removeMethodButton);

				Label editInheritanceLabel = new Label("Super Class: ");
				ComboBox<String> comboBoxSuperClass = new ComboBox<>();
				for (Element e : DApp.elements) {
					if (!DApp.selectedElement.getObject().getName().equals(e.getObject().getName()))
						comboBoxSuperClass.getItems().add(e.getObject().getName());
				}
				if (((DGeneralClass) DApp.selectedElement.getObject()).getSuperClass() != null)
				{
					comboBoxSuperClass.setPromptText(((DGeneralClass) DApp.selectedElement.getObject()).getSuperClass().getName());
					removeLine( DApp.selectedElement, false);
				}
				comboBoxSuperClass.setOnAction(event -> {
					if (((DGeneralClass) DApp.selectedElement.getObject()).getSuperClass() != null && !comboBoxSuperClass.getItems().get(0).toString().equals( ((DGeneralClass) DApp.selectedElement.getObject()).getSuperClass().getName()))
					{
						System.out.println("asd");
						for (Element e : DApp.elements) {
							System.out.println("aaaaaaaaaaaaaa");
							if (comboBoxSuperClass.getValue().equals(e.getObject().getName())) {
								((DGeneralClass) DApp.selectedElement.getObject()).setSuperClass((DGeneralClass) e.getObject());
								if ( DApp.selectedElement.endLines.isEmpty()) {
									System.out.println( "line 943");
									DApp.drawCenteredLine(DApp.selectedElement, e);
								}
								System.out.println( "line 946");
								DApp.select( e);
								DApp.updateOverlay();
								DApp.updateLines();
								DApp.updateArrow();
								DApp.updateZoomPane();
								DApp.select( null);
							}
						}
					}
				});
				HBox inheritanceHBox = new HBox();
				inheritanceHBox.getChildren().addAll(editInheritanceLabel, comboBoxSuperClass);


				VBox radioButtonVBox = new VBox();
				Label editInterfacesLabel = new Label("Edit Interfaces");

				radioButtonVBox.getChildren().add(editInterfacesLabel);
				for (Element e : DApp.elements) {
					if (e.getObject() instanceof DInterface) {
						RadioButton rb = new RadioButton(e.getObject().getName());
						rb.setOnAction(event -> {
							if (rb.isSelected()) {
								((DGeneralClass) DApp.selectedElement.getObject()).addInterface( (DInterface) e.getObject());
								DApp.drawCenteredDashedLine( e, DApp.selectedElement);
							}
							else {
								((DGeneralClass) DApp.selectedElement.getObject()).removeInterface((DInterface) e.getObject());
								removeInterfaceLine( DApp.selectedElement, e);
							}
							DApp.updateOverlay();
							DApp.updateLines();
							DApp.updateZoomPane();

						});
						radioButtonVBox.getChildren().add(rb);
					}
				}

				VBox vbox = new VBox();
				vbox.setSpacing(5);
				vbox.setPadding(new Insets(10, 10, 10, 10));
				vbox.getChildren().addAll(propertiesHBox, methodsHBox, inheritanceHBox, radioButtonVBox);

				Scene scene = new Scene( vbox);
				window.setScene( scene);
				window.show();
			} else if (DApp.selectedElement.getObject() instanceof DInterface) {

			}
		}
		else {
			displayErrorMessage( "You have to choose an object!!");
		}
	}

	public static DObject createInterface( TextField name, ChoiceBox<String> cb) {
		Element r;
		DInterface object = new DInterface( name.getText());
		System.out.println( object);
		object.setName(name.getText());


		if ( object.getName() != null) {
			if (cb != null)
			{
				String cbS = cb.getValue();

			}
			Random rand = new Random();
			if ( DApp.selectedElement != null)
				r = new Element(  DApp.selectedElement.getLayoutX() + 30 + rand.nextInt(7) , DApp.selectedElement.getLayoutY()+ 30 + rand.nextInt(7),
						150, 150, Color.web(DApp.colors[0].substring(0, 1) + DApp.colors[rand.nextInt(10)].substring(1).toUpperCase(), 1.0), true);
			else
				r = new Element( DApp.offset + 0 - Math.random()*DApp.RANDOMNESS, DApp.offset + 0 - Math.random()*DApp.RANDOMNESS, 150, 150,
						Color.web(DApp.colors[0].substring(0, 1) + DApp.colors[rand.nextInt(10)].substring(1).toUpperCase(), 1.0), true);

			ProjectManager.connectElement(r, object);
			DApp.elements.add(r);
			DApp.project.addObject(object);

			DApp.group.getChildren().add(r);

			DApp.select(r);
		}
		else {
			displayErrorMessage( "Invalid Interface Name Declaration");
		}
		DApp.updateZoomPane();

		return object;
	}

	/**
	 * This method allows user to choose parent class for new class
	 * @param cb
	 * @param child
	 */
	public static void getInheritanceChoice( ChoiceBox<String> cb, DObject child) {
		for ( DObject o : DApp.project.getObjects() ) {
			if ( cb.getValue() != null && cb.getValue().equals(o.getName()))
			{
				System.out.println("Inheritance found!");
				setInheritance(o , child);
			}

		}
	}

	/**
	 * This method allows user to set the inheritance of the class
	 * @param parent
	 * @param child
	 */
	public static void setInheritance( DObject parent, DObject child) {
		Element lastAdded = null, selected = null;

		for ( Node n : DApp.group.getChildren()) {
			if ( n instanceof Element && ((Element) n).hasObject()){
				if ( (((Element) n).getObject().getName()).equals( child.getName()) )
					lastAdded = (Element)n;
			}
		}
		for ( Node n : DApp.group.getChildren()) {
			if ( n instanceof Element && ((Element) n).hasObject()){
				if ( (((Element) n).getObject().getName()).equals( parent.getName()) )
					selected = (Element)n;
			}
		}

		if (child instanceof DGeneralClass && parent instanceof DGeneralClass)
		{
			DGeneralClass childClass= (DGeneralClass) child;
			DGeneralClass parentClass = (DGeneralClass) parent;
			childClass.setSuperClass(parentClass);
		}

		if (child instanceof DInterface && parent instanceof DInterface)
		{

		}

		DApp.drawCenteredLine( selected, lastAdded);
		DApp.select(selected);
		DApp.select(lastAdded);
	}

	/**
	 * This method gives options to the user to demonstrate a field in a class
	 * @param element
	 */
	public static void displayFieldOptions( Element element) {
		Button create;
		TextField name, type;

		Stage window = new Stage();

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("New Property");
		window.setMinWidth(400);
		window.setMinHeight(400);

		Label messageName = new Label( "Name of the property:");
		name = new TextField();
		Label messageType = new Label( "Type of the property:");
		type = new TextField();
		create = new Button("add property");

		create.setOnAction( e -> {

			if ( name.getText().equals("") && type.getText().equals("") ){
				displayErrorMessage( "No name or type found");
				displayFieldOptions( element);
				window.close();
			}
			else if ( type.getText().equals("") ) {
				displayErrorMessage( "No type found");
				displayFieldOptions( element);
				window.close();
			}
			else if ( name.getText().equals("") ) {
				displayErrorMessage( "No name found");
				displayFieldOptions( element);
				window.close();
			}
			else if ( element.getObject() instanceof DInterface)
			{
				displayErrorMessage( "Cannot add a property to an Interface.");
			}
			else {
				addProperty( name.getText(), type.getText(), element);
			}

			window.close();
		});



		VBox layout = new VBox();
		layout.getChildren().addAll( messageName, name, messageType, type, create);
		layout.setAlignment( Pos.CENTER);

		window.setScene( new Scene( layout));
		window.showAndWait();
	}

	/**
	 * This method gives options to the user to demonstrate a method in a class
	 * @param element
	 */
	public static void displayMethodOptions( Element element) {
		Button create;
		TextField name, returnType;
		CheckBox cb, cb2, cb3;
		Stage window = new Stage();

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("New Method");
		window.setMinWidth(400);
		window.setMinHeight(400);

		Label messageName = new Label( "Name of the method:");
		name = new TextField();
		Label messageType = new Label( "Return type of the method:");
		returnType = new TextField();
		//Label messageParam = new Label( "Add parameters below");
		cb = new CheckBox();
		cb2 = new CheckBox();
		cb3 = new CheckBox();
		cb.setTooltip(  new Tooltip("void"));
		cb2.setTooltip(new Tooltip("static"));
		cb3.setTooltip( new Tooltip("no parameters"));
		create = new Button("create method");

		create.setOnAction( e -> {
			final boolean stat = cb2.isSelected();

			if ( name.getText().equals("") && returnType.getText().equals("") ){
				displayErrorMessage( "No name or return type found");
				displayMethodOptions( element);
				window.close();
			}
			else if ( returnType.getText().equals("") ) {
				displayErrorMessage( "No return type found");
				displayMethodOptions( element);
				window.close();
			}
			else if ( name.getText().equals("") ) {
				displayErrorMessage( "No name found");
				displayMethodOptions( element);
				window.close();
			}
			else {
				final DMethod meth = new DMethod( name.getText(), returnType.getText(), stat);
				if ( !cb3.isSelected())
					displayParameterOptions( meth, element);
				else
					addMethod( meth, element);
			}

			window.close();
		});

		VBox layout = new VBox();
		layout.getChildren().addAll( messageName, name, messageType, returnType, cb, cb2, cb3, create);
		layout.setAlignment( Pos.CENTER);

		window.setScene( new Scene( layout));
		window.showAndWait();
	}

	/**
	 * This method displays the parameter options
	 * @param meth
	 * @param element
	 */
	public static void displayParameterOptions( DMethod meth, Element element) {
		Button addParam, addNew;
		ArrayList<Label> fields;
		ArrayList<TextField> params = new ArrayList<TextField>();

		Stage window = new Stage();
		VBox layout = new VBox();

		fields = new ArrayList<Label>();
		for ( int i = 0; i < element.getObject().getProperties().size(); i++) {
			fields.add( new Label( element.getObject().getProperties().get(i).getType() + ":"
					+ element.getObject().getProperties().get(i).getName()));
		}

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Method Parameters for " + meth.getName());
		window.setMinWidth(400);
		window.setMinHeight(400);


		Label messageParam = new Label( "Add parameters below");
		layout.getChildren().add(messageParam);



		addNew = new Button("add another");
		addNew.setOnAction( e -> {
			final TextField tf = new TextField("tpye, name");
			params.add(tf);
			layout.getChildren().add( tf);
			e.consume();
		});

		addParam = new Button("create method");

		addParam.setOnAction( e -> {

			for ( int i = 0; i < params.size(); i++) {
				for ( int j = 0; j < params.get(i).getText().length(); j++){
					if ( params.get(i).getText().charAt(j) == ',') {
						meth.addParameter( params.get(i).getText().substring(j+1), params.get(i).getText().substring(0,j) );
					}
				}
			}

			for ( int i = 0; i < layout.getChildren().size(); i++) {

				if ( layout.getChildren().get(i) instanceof HBox) {
					HBox currentRow = (HBox)layout.getChildren().get(i) ;

					for ( int j = 0; j < currentRow.getChildren().size(); j++) {

						if ( currentRow.getChildren().get(j) instanceof CheckBox) {

							if ( ((CheckBox)currentRow.getChildren().get(j)).isSelected() ){

								for ( int k = 0; k <((Label)currentRow.getChildren().get(j-1)).getText().length(); k++){

									if ( ((Label)currentRow.getChildren().get(j-1)).getText().charAt(k) == ':') {

										meth.addParameter( ((Label)currentRow.getChildren().get(j-1)).getText().substring(k+1),
												((Label)currentRow.getChildren().get(j-1)).getText().substring(0,k));
									}
								}
							}
						}

					}
				}
			}

			addMethod(meth, element);
			window.close();
		});

		for ( Label l : fields) {
			HBox h = new HBox();
			h.getChildren().add( l);
			h.getChildren().add( new CheckBox());
			layout.getChildren().add(h);
		}

		layout.setAlignment( Pos.CENTER_LEFT);
		addNew.setAlignment(Pos.CENTER_RIGHT);
		addParam.setAlignment( Pos.BOTTOM_CENTER);

		BorderPane parentLayout = new BorderPane();
		parentLayout.setLeft( layout);
		parentLayout.setRight( addNew);
		parentLayout.setBottom( addParam);

		window.setScene( new Scene( parentLayout));
		window.showAndWait();

	}

	/**
	 * This method displays error message in a different window
	 * @param message
	 */
	public static void displayErrorMessage(String message) {
		Stage window = new Stage();

		window.initModality(Modality.APPLICATION_MODAL);
		window.initStyle(StageStyle.UNDECORATED);
		window.setTitle("New Object");
		window.setMinWidth(200);
		window.setMinHeight(160);

		Label messageName = new Label( message);

		Button ok = new Button("OK");
		ok.setOnAction( e ->  window.close());

		VBox layout = new VBox();
		layout.getChildren().addAll( messageName, ok);
		layout.setAlignment( Pos.CENTER);

		window.setScene( new Scene( layout));
		window.showAndWait();
	}

	/**
	 * This method adds property to the specified element
	 * @param name
	 * @param type
	 * @param element
	 */
	public static void addProperty( String name, String type, Element element) {
		DProperty prop = new DProperty( name, type);
		element.addField(prop);
		if( element.getObject() instanceof DClass)
		{
			((DClass)element.getObject()).addProperty(prop);
		}
		if( element.getObject() instanceof DInterface)
		{
			displayErrorMessage( "Cannot add a property to an Interface.");
			//            displayFieldOptions( element);
			//            window.close();
		}
		System.out.println( prop);
	}

	/**
	 * This method adds methods to the specified element.
	 * @param m
	 * @param element
	 */
	public static void addMethod( DMethod m, Element element) {
		element.addMethod(m);
		if( element.getObject() instanceof DClass)
		{
			((DClass)element.getObject()).addMethod(m);
		}
		if( element.getObject() instanceof DInterface)
		{
			((DInterface)element.getObject()).addMethod(m);
		}

		System.out.print( m);
	}

	/**
	 * This method extracts the all java code from uml scheme which made by user
	 * @param e
	 * @param prj
	 */
	public static void extractAll( ActionEvent e, DProject prj) {
		displayErrorMessage( "Project extracted successfully.");
		System.out.println("extracting all...");
		prj.extract("");
	}

	/**
	 * This method extracts the java code of only methods from uml scheme which made by user
	 * @param e
	 */
	public static void extractMethods( ActionEvent e) {
		System.out.println("extracting methods...");
	}

	/**
	 * This method extracts the java code of only fields from uml scheme which made by user
	 * @param e
	 */
	public static void extractFields( ActionEvent e) {
		System.out.println("extracting fields...");
	}

	/**
	 * This method sets the line color for DApp
	 * @param color
	 */
	static void setLineColor( Paint color)
	{
		DApp.lineColor = color;
	}
	/**
	 * This method sets the background color for the program
	 * @param color
	 */
	static void setBackgroundColor(Paint color)
	{
		DApp.backgroundColor = color;
		Paint bg1 = color;
		BackgroundFill backgroundFill1 = new BackgroundFill(bg1, null, null);
	}

	/**
	 * This method displays the constructor maker window
	 * @param element
	 */
	public static void displayConstructorMakerWindow( Element element) {
		Stage window = new Stage();
		Scene scene;
		window.initModality(Modality.APPLICATION_MODAL);
		window.initStyle(StageStyle.DECORATED);
		window.setTitle("Constructor Maker");
		window.setMinWidth(200);
		window.setMinHeight(160);
		TableView table = new TableView();

		table.setEditable(true);

		TableColumn firstNameCol = new TableColumn("Accessibility");
		TableColumn lastNameCol = new TableColumn("Parameters");

		firstNameCol.setPrefWidth( 60);
		lastNameCol.setPrefWidth( 60);

		final VBox vbox = new VBox();
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10, 10, 10, 10));
		vbox.getChildren().addAll( table);

		scene = new Scene(vbox);
		//((Group) scene.getRoot()).getChildren().addAll(vbox);

		window.setScene(scene);
		window.show();
	}
	/**
	 * This method displays remove options
	 * @param element
	 */
	public static void displayRemoveOptions( Element element) {
		Stage window = new Stage();

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Are you sure");
		window.setMinWidth(200);
		window.setMinHeight(160);

		Label messageName = new Label( "Deleting " + DApp.selectedElement.getObject().getName() + "...\n" + "Are you sure?");

		Button ok = new Button("Yes");
		Button notOk = new Button("cancel");
		ok.setOnAction(e -> {
			removeObject(element);
			//drawHierarchy(project);
			window.close();
		});
		notOk.setOnAction( e ->  window.close());

		VBox layout = new VBox();
		layout.getChildren().addAll( messageName, ok, notOk);
		layout.setAlignment( Pos.CENTER);

		window.setScene( new Scene( layout));
		window.showAndWait();
	}

	public static void removeObject(Element element)
    {
        DApp.select(null);

        //deleting overlay
        for ( int i =0; i < DApp.group.getChildren().size();i++) {
            if ( DApp.group.getChildren().get(i) instanceof Group && ((Group) DApp.group).getChildren().get(i) == DApp.overlay)
                DApp.group.getChildren().remove(DApp.group.getChildren().get(i));
        }
        // forgeting element
        DApp.elements.remove(element);
        // deleting element
        for ( int i =0; i < DApp.group.getChildren().size();i++) {
            if ( DApp.group.getChildren().get(i) instanceof Element && ((Element) DApp.group.getChildren().get(i)).listener &&((Element)DApp.group.getChildren().get(i)).getObject().getName().equals( element.getObject().getName()))
                DApp.group.getChildren().remove(DApp.group.getChildren().get(i));
        }
        // removing from project
        DApp.project.getObjects().remove(element.getObject());
        //deleting line from group
        removeLine( element, true);
        DApp.updateArrow();
        DApp.updateLines();
        DApp.updateOverlay();
        DApp.updateZoomPane();
    }

	public static void removeLine(Element element, boolean all)
	{
		for ( int i = 0; i < DApp.group.getChildren().size(); i++) {

			if ( DApp.group.getChildren().get(i) instanceof ComplexLine) {

				ComplexLine cl = (ComplexLine)DApp.group.getChildren().get(i);

				for ( int j =0; j < DApp.group.getChildren().size(); j++)
					if ( DApp.group.getChildren().get(j) instanceof ArrowHead ) {
						ArrowHead a = (ArrowHead)DApp.group.getChildren().get(j);

						if (  element.getEndLines().contains( a.getComplexLine()) )
						{
							DApp.group.getChildren().remove( a);
						}
						if ( element.getStartLines().contains( a.getComplexLine()) && all)
						{
							DApp.group.getChildren().remove( a);
						}
					}
				if ( element.startLines.contains(cl) )
				{
					DApp.group.getChildren().remove(cl);
				}
				if ( element.endLines.contains(cl) && all)
				{
					DApp.group.getChildren().remove(cl);
				}
				if ( DApp.lines.contains(cl))
				{
					DApp.lines.remove(cl);
				}
			}
			if ( DApp.group.getChildren().get(i) instanceof DashedComplexLine && all ) {

				DashedComplexLine cdl = (DashedComplexLine)DApp.group.getChildren().get(i);

				for ( int j =0; j < DApp.group.getChildren().size();j++)
					if ( DApp.group.getChildren().get(j) instanceof ArrowHead ) {
						ArrowHead  a = (ArrowHead)DApp.group.getChildren().get(j);

						if (  element.getEndLines().contains( a.getComplexLine()) ||  element.getStartLines().contains( a.getComplexLine())){
							DApp.group.getChildren().remove( a);
						}
					}
				if ( element.startLines.contains(cdl) || element.endLines.contains(cdl))
					DApp.group.getChildren().remove(cdl);
				if ( DApp.lines.contains(cdl))
				{
					DApp.lines.remove(cdl);
				}
			}
		}
	}

	public static void removeInterfaceLine(Element object, Element inf)
	{
		for ( int i =0; i < DApp.group.getChildren().size();i++) {
			if ( DApp.group.getChildren().get(i) instanceof DashedComplexLine) {

				DashedComplexLine cdl = (DashedComplexLine)DApp.group.getChildren().get(i);

				for ( int j =0; j < DApp.group.getChildren().size(); j++){
					if ( DApp.group.getChildren().get(j) instanceof ArrowHead ) {
						ArrowHead  a = (ArrowHead)DApp.group.getChildren().get(j);

						if (  inf.getEndLines().contains( a.getComplexLine()) ){
							DApp.group.getChildren().remove( a);
						}
					}
					if ( inf.endLines.contains(cdl))
					DApp.group.getChildren().remove(cdl);

				}
			}
		}
	}

	/**
	 * This method displays the project options
	 */
	public static void displaySaveOptions() {
		Stage window = new Stage();
		TextField t = new TextField();
		t.setText("New Project");

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("New Project");
		window.setMinWidth(200);
		window.setMinHeight(160);

		Label messageName = new Label( "Enter Project Name");

		Button save = new Button("Save Project");

		save.setOnAction(e -> {
			DApp.project.setName(t.getText());
			saveProject();

			window.close();
		});

		VBox layout = new VBox();
		layout.getChildren().addAll( messageName, t, save);
		layout.setAlignment( Pos.CENTER);

		window.setScene( new Scene( layout));
		window.showAndWait();
	}

	/**
	 * This method allows user to save the project
	 */
	static void saveProject() {

		File f = new File("Saved Projects");
		f.mkdirs();
		try {
			FileWriter oFile = new FileWriter( "Saved Projects/" + DApp.project.getName() + ".diag");
			for(String line : DApp.project.projectToText())
			{
				oFile.write(line);
				oFile.write("\n");
			}
			oFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			displayErrorMessage("INVALID PROJECT NAME");
			e.printStackTrace();
		}
	}

	private static void loadProject(String projectName) {

		ArrayList<String> projectLines = new ArrayList<String>();
		try {
			File myObj = new File("Saved Projects/" + projectName + ".diag");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String line = myReader.nextLine();
				projectLines.add(line);
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		DProject loadedProject = ProjectManager.textToProject(projectLines);
		DApp.project = loadedProject;

	}

	/**
	 * This method clears te view of the project by removing them
	 */
	public static void cleanProjectView() {
		DApp.select(null);

		// forgeting all elements
		DApp.elements.clear();

		// removing from project
		DApp.project.getObjects().clear();

		Element cornerNW = new Element( -100, -100, 1, 1, Color.GOLD, false);
        Element cornerNE = new Element( 10000, -100, 1, 1, Color.GOLD, false);
        Element cornerSW = new Element( -100, 10000, 1, 1, Color.GOLD, false);
        Element cornerSE = new Element( 10000, 10000, 1, 1, Color.GOLD, false);

        System.out.println(cornerSE.elementToString());

        DApp.group.getChildren().removeAll(DApp.group.getChildren());

        DApp.group.getChildren().addAll( cornerNE, cornerNW, cornerSE, cornerSW, DApp.closest);

		DApp.updateArrow();
		DApp.updateLines();
		DApp.updateOverlay();
		DApp.updateZoomPane();

	}

	public static void displayProjectOptions() {
		// TODO Auto-generated method stub

	}

}