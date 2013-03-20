package misty.classdump;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-12
 * Time: 下午3:29
 */
public class IntrospectorApp {
	public static void main(String[] args) throws IntrospectionException {
		BeanInfo bi = Introspector.getBeanInfo(Person.class);
		for (PropertyDescriptor pd : bi.getPropertyDescriptors()) {
			System.out.println(pd);
		}
	}
}

class Person {
	public String getName() {
		return "0";
	}

	public void setName(String name) {
	}

}