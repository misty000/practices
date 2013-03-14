package misty.threadlocal;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-14
 * Time: 上午10:18
 */
public class DateFormatTest {
	public final static ThreadLocal<DateFormat> TL_FORMAT = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};
	private final static DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	public static void main(String[] args) {
		new DateFormatTest().test1();
		new DateFormatTest().test2();
		new DateFormatTest().test3();
		new DateFormatTest().test4();
	}

	public void test1() {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(format.format(new Date()));
	}

	public void test2() {
		System.out.println(format.format(new Date()));
	}

	public void test3() {
		synchronized (format) {
			System.out.println(format.format(new Date()));
		}
	}

	public void test4() {
		DateFormat df = TL_FORMAT.get();
		System.out.println(df.format(new Date()));
	}
}
