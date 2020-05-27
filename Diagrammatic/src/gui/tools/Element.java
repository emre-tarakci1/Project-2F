package gui.tools;

import java.util.ArrayList;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import gui.*;

import logic.object_source.*;
import logic.tools.*;

public class Element extends Group {

	final int SPACE = 5;

	DObject dObject;
	Rectangle rectangle = new Rectangle();
	String nameOfClass = new String("NameOfClass");
	DoubleProperty widthProperty = new SimpleDoubleProperty();
	public DoubleProperty heightProperty = new SimpleDoubleProperty();
	public ArrayList<ComplexLine> startLines = new ArrayList<ComplexLine>();
	public ArrayList<ComplexLine> endLines = new ArrayList<ComplexLine>();
	public boolean listener;
	Line belowName;
	Line belowProperties;

	ArrayList<Label> props = new ArrayList<Label>();
	ArrayList<Label> meths = new ArrayList<Label>();
	HBox contentsH;
	HBox contentsH2;
	HBox contentsH3;
	VBox proper;
	VBox metho;
	VBox contentsV;
	Label l;

	/**
	 * @param listener
	 */
	public Element( boolean listener) {
		if (listener) {
			this.listener = listener;
			setOnMousePressed(me -> {
				DApp.select(this);
				DApp.srBnd.fireEvent(me);
				me.consume();
			});
			setOnMouseDragged(me -> DApp.srBnd.fireEvent(me));
			setOnMouseReleased(me -> DApp.srBnd.fireEvent(me));
			boundsInParentProperty().addListener((v, o, n) -> DApp.updateOverlay());
		}
	}
	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param fill
	 * @param listener
	 */
	public Element(double x, double y, double width, double height, Paint fill, boolean listener) {
		widthProperty.addListener((v, o, n) -> { rectangle.setWidth(n.doubleValue()); });
		heightProperty.addListener((v, o, n) -> { rectangle.setHeight(n.doubleValue()); });

		setLayoutX(x);
		setLayoutY(y);

		this.listener = listener;

		widthProperty.set(width);
		heightProperty.set(height);

		rectangle.setFill(fill);
		rectangle.setStrokeWidth(4.0);
		rectangle.setStroke( Color.BLACK);
		rectangle.setArcHeight(20.0d);
		rectangle.setArcWidth(20.0d);

		l = new Label(  "   " + nameOfClass + "\n");
		/*
        final int fontSize = 9;
        final String fontType = "Arial";
        final Font font = new Font(fontType, fontSize);

        l.setFont(font);
        l2.setFont(font);
        l3.setFont(font);
		 */

		if (dObject != null) {
			for ( int i = 0; i < dObject.getProperties().size(); i++)
				props.add( new Label( dObject.getProperties().get(i).toString()));

			for ( int i = 0; i < dObject.getMethods().size(); i++)
				meths.add( new Label( dObject.getProperties().get(i).toString()));
		}
		contentsH = new HBox();

		contentsH2 = new HBox();
		proper = new VBox();

		contentsH3 = new HBox();
		metho = new VBox();

		contentsV = new VBox();

		contentsH.getChildren().addAll(l);

		belowName = new Line();
		belowProperties = new Line();
		updateSeperators();

		contentsV.getChildren().addAll( contentsH, belowName, contentsH2, proper, belowProperties, contentsH3, metho);

		//setPickOnBounds(true);
		getChildren().addAll(rectangle, contentsV);
		if(listener) {
			setOnMousePressed(me -> {
				DApp.select(this);
				DApp.srBnd.fireEvent(me);
				me.consume();
				System.out.println("S: " + toString());
			});
			setOnMouseDragged(me -> DApp.srBnd.fireEvent(me));
			setOnMouseReleased(me -> DApp.srBnd.fireEvent(me));
			boundsInParentProperty().addListener((v, o, n) -> DApp.updateOverlay());
		}



	}

	/**
	 * @return
	 */
	public DoubleProperty widthProperty() { return widthProperty; }

	/**
	 * @return
	 */
	public DoubleProperty heightProperty() { return heightProperty; }

	/**
	 *
	 */
	@Override
	public String toString() {
		return "[" + getLayoutX() + ", " + getLayoutY() + ", " + widthProperty.get() + ", " + heightProperty.get() + "]";
	}

	/**
	 *
	 */
	public void updateSize() {
		//for( Node n : contentsH.getChildren())
		//((Label) n).setPrefWidth(((Label) n).getText().length() * 7);
		for( Node n : contentsV.getChildren()) {
			if ( n instanceof HBox) {
				//for(Node m : ((HBox)n).getChildren())
				//((Label) m).setPrefHeight( 15);

			}
		}
	}

	/**
	 * @return
	 */
	public double limitWidth() {
		updateSize();
		return contentsH.prefWidth(heightProperty.getValue());
	}

	/**
	 * @return
	 */
	public double limitHeight() {
		updateSize();
		return contentsV.prefHeight(widthProperty.getValue());
	}

	/**
	 * @param dc
	 */
	public void setObject( DObject dc) {
		nameOfClass = dc.getName();
		this.dObject = dc;

		updateObject();
	}

	/**
	 * @return
	 */
	public boolean hasObject() {
		return dObject != null;
	}
	/**
	 * @param prop
	 */
	public void addField( DProperty prop) {
		String out = new String("   -"+prop.getName() + ": ");
		out += prop.getType();
		props.add( new Label(out));
		updateObject();
		heightProperty.set(heightProperty.get() + DApp.size);
	}

	/**
	 * @param meth
	 */
	public void addMethod( DMethod meth) {
		String out = new String( "   +"+ meth.getName() + "(");

		if ( !meth.getParameters().isEmpty()) {
			for ( DProperty param : meth.getParameters()) {
				out += param.getType() + ", ";

			}
			out = out.substring(0,out.length() - 2);
		}
		out += ")";

		out += ": "+ meth.getReturnType();

		meths.add( new Label(out));
		updateObject();
		heightProperty.set(heightProperty.get() + DApp.size);
	}

	/**
	 *
	 */
	public void iniObject() {
		if (dObject != null) {
			if ( dObject instanceof DClass) {
				for ( int i = 0; i < dObject.getProperties().size(); i++) {
					if ( !props.contains( dObject.getProperties().get(i)))
						addField(dObject.getProperties().get(i));
				}
				for ( int i = 0; i < dObject.getMethods().size(); i++) {
					if (	 !meths.contains( dObject.getMethods().get(i)))
						addMethod( dObject.getMethods().get(i));
				}
			}
			else if ( dObject instanceof DInterface) {
				for ( int i = 0; i < dObject.getMethods().size(); i++) {
					if (	 !meths.contains( dObject.getMethods().get(i)))
						addMethod( dObject.getMethods().get(i));
				}
			}
		}
	}

	/**
	 *
	 */
	public void updateObject() {
		// updating name
		for ( int i = 0; i < contentsH.getChildren().size(); i++)
			if ( contentsH.getChildren().get(i) instanceof Label)
			{
				if( this.getObject() instanceof DInterface)
				{
					contentsH.getChildren().set( i, new Label( "   " + "<<interface>>\n" + "   " + dObject.getName() + "\n\n"));
				}
				else if ( this.getObject() instanceof DAbstractClass)
				{
					contentsH.getChildren().set( i, new Label( "   " + "<<abstract>>\n" + "   " + dObject.getName() + "\n\n"));
				}
				else
				{
					contentsH.getChildren().set( i, new Label( "   " + dObject.getName() + "\n\n"));
				}
			}


		// updating properties
		for ( int i = 0; i < props.size(); i++)
		{
			Label label = props.get(i);
			boolean remove = true;
			for (DProperty dp : getObject().getProperties())
			{
				if (label.getText().contains(dp.getName()))
					remove = false;
			}
			if( remove)
			{
				props.remove(label);
				proper.getChildren().remove(label);
			}
		}

		for ( Label label : props) {
			if (!proper.getChildren().contains(label))
			{
				proper.getChildren().add(label);
			}
		}

		// updating methods
		for ( int i = 0; i < meths.size(); i++)
		{
			Label label = meths.get(i);
			boolean remove = true;
			for (DMethod dm : getObject().getMethods())
			{
				if (label.getText().contains(dm.getName()))
					remove = false;
			}
			if( remove)
			{
				meths.remove(label);
				metho.getChildren().remove(label);
			}
		}
		for ( Label label : meths) {
			if (!metho.getChildren().contains(label))
				metho.getChildren().addAll(label);
		}
	}

	/**
	 * @return
	 */
	public ArrayList<Label> getMethodLabels() {
		return meths;
	}

	/**
	 * @return
	 */
	public ArrayList<Label> getPropertyLabels() {
		return props;
	}
	/**
	 * @return
	 */
	public ArrayList<ComplexLine> getStartLines() {
		return startLines;
	}

	/**
	 * @return
	 */
	public ArrayList<ComplexLine> getEndLines() {
		return endLines;
	}
	/**
	 * @return
	 */
	public DObject getObject() {
		return this.dObject;
	}

	/**
	 * @return
	 */
	public String elementToString()
	{
		String line = "ELE" +
				" " + getLayoutX() +
				" " + getLayoutY() +
				" " + widthProperty().doubleValue() +
				" " + heightProperty().doubleValue() +
				" " + rectangle.getFill();
		return line;
	}

	public String getColor()
	{
		return rectangle.getFill().toString();
	}

	public void updateSeperators()
	{
		belowName.setStartX(contentsH.getLayoutX());
		belowName.setStartY(contentsH.getLayoutY());
		belowName.setEndX(contentsH.getLayoutX() + widthProperty().get());
		belowName.setEndY(contentsH.getLayoutY());

		System.out.println(contentsH.getLayoutX());
		System.out.println(contentsH.getLayoutY());
		System.out.println(contentsH2.getLayoutX());
		System.out.println(contentsH2.getLayoutY());

		if (getObject() != null)
		{
			if (getObject () instanceof DGeneralClass)
			{
				if (getObject().getProperties().size() == 0)
				{
					belowProperties.setStartX(contentsH2.getLayoutX());
					belowProperties.setStartY(contentsH2.getLayoutY());
					belowProperties.setEndX(contentsH2.getLayoutX() + widthProperty().get());
					belowProperties.setEndY(contentsH2.getLayoutY());
				}
				else
				{
					belowProperties.setStartX(contentsH2.getLayoutX());
					belowProperties.setStartY(contentsH2.getLayoutY() + SPACE);
					belowProperties.setEndX(contentsH2.getLayoutX() + widthProperty().get());
					belowProperties.setEndY(contentsH2.getLayoutY() + SPACE);
				}
			}

		}
		else
		{
			belowProperties.setStartX(contentsH2.getLayoutX());
			belowProperties.setStartY(contentsH2.getLayoutY() + SPACE);
			belowProperties.setEndX(contentsH2.getLayoutX() + widthProperty().get());
			belowProperties.setEndY(contentsH2.getLayoutY() + SPACE);
		}
	}

}

