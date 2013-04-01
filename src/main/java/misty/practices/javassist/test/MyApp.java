package misty.practices.javassist.test;

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-1
 * Time: 下午12:49
 */
class MyApp {
	public static void main(String[] args) {
		System.out.println("MyApp");
		System.out.println(MyApp.class.getClassLoader());
	}
}
