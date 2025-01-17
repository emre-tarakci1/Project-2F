package logic.object_source;

import logic.tools.*;
import logic.interfaces.*;
import java.util.ArrayList;

import gui.tools.Element;

public class DObject implements Extractable, Accessible{

	// Properties
	protected String name;
	private String accessibility;
	private ArrayList<DMethod> methodCollector;
	private ArrayList<DProperty> propertyCollector;
	private ArrayList<DConstructor> constructorCollector;
	private Element guiElement;

	// Methods
	/**
	 *
	 */
	public ArrayList<String> extract()
	{
		return null;
	}

	/**
	 *
	 */
	public String toString()
	{
		return null;
	}

	/**
	 * @return
	 */
	public ArrayList<DMethod> getMethods()
	{
		return methodCollector;
	}

	/**
	 * @return
	 */
	public ArrayList<DProperty> getProperties()
	{
		return propertyCollector;
	}


	/**
	 * @param m
	 */
	public void addMethod( DMethod m) {
		getMethods().add(m);
	}

	/**
	 * @param ms
	 */
	public void addMethods( ArrayList<DMethod> ms) {
		for ( DMethod m : ms)
			getMethods().add(m);
	}

	/**
	 * @param name
	 */
	public void removeAllMethodsNamed( String name) {
		for (DMethod m : getMethods()) {
			if ( m.getName().equals(name))
				getMethods().remove(m);
		}
	}

	/**
	 * @param name
	 * @param returnType
	 * @param parameters
	 */
	public void removeMethod( String name, String returnType, ArrayList<DProperty> parameters) {
		for ( DMethod m : getMethods()) {
			if ( m.getName().equals(name) && m.getReturnType().equals(returnType) && m.getParameters().equals(parameters))
				getMethods().remove(m);
		}
	}

	/**
	 * @param m
	 */
	public void removeMethod( DMethod m) {
		getMethods().remove(m);
	}


	/**
	 * @param methodCollector
	 */
	public void setMethods( ArrayList<DMethod> methodCollector)
	{
		this.methodCollector = methodCollector;
	}

	/**
	 * @param propertyCollector
	 */
	public void setProperties( ArrayList<DProperty> propertyCollector)
	{
		this.propertyCollector = propertyCollector;
	}

	/**
	 * @return
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 */
	public void setName( String name)
	{
		this.name = name;
	}

	/**
	 * @return
	 */
	public ArrayList<DConstructor> getConstructorCollector() {
		return constructorCollector;
	}

	/**
	 * @param constructorCollector
	 */
	public void setConstructorCollector(ArrayList<DConstructor> constructorCollector) {
		this.constructorCollector = constructorCollector;
	}

	/**
	 *
	 */
	@Override
	public String getAcccessability() {
		return accessibility;
	}

	/**
	 *
	 */
	@Override
	public void setAccessability( String s) {
		if ( s.equals(ProjectManager.PROTECTED) || s.equals( ProjectManager.PUBLIC) || s.equals( ProjectManager.DEFAULT) || s.equals( ProjectManager.PRIVATE) )
			accessibility = s;

	}

	public ArrayList<String> classToString()
	{
		return new ArrayList<String>();
	}

	public int degree()
	{
		return 0;
	}

	public Element getElement()
	{
		return guiElement;
	}

	public void setElement( Element e)
	{
		guiElement = e;
	}
}