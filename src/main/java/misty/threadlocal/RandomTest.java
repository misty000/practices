package misty.threadlocal;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-14
 * Time: 上午9:48
 */
public class RandomTest {
	public final static Random rand = new Random();

	public static void test1() {
		Random r = new Random();
		int i = r.nextInt();
		//use i
	}

	public static void main(String[] args) {

	}

	public void test2() {
		int i = rand.nextInt();
		// use i
	}

}
