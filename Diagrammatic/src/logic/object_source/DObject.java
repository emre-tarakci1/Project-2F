package logic.object_source;

import logic.tools.*;
import logic.interfaces.*;
import java.util.ArrayList;

public class DObject implements Extractable, Accesible{

	// Properties
	protected String name;
	private String accesibility;
	private ArrayList<DMethod> methodCollector;
	private ArrayList<DProperty> propertyCollector;
	private ArrayList<DConstructor> constructorCollector;

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
	public  String toString()
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
		return accesibility;
	}

	/**
	 *
	 */
	@Override
	public void setAccessability( String s) {
		if ( s.equals(ProjectManager.PROTECTED) || s.equals( ProjectManager.PUBLIC) || s.equals( ProjectManager.DEFAULT) || s.equals( ProjectManager.PRIVATE) )
			accesibility = s;

	}
}