package misty.threadlocal;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-14
 * Time: 上午8:59
 */
public class ThreadLocalTest {
	private final static ThreadLocal<My50MB> tl = new ThreadLocal<My50MB>() {
		@Override
		protected My50MB initialValue() {
			System.out.println(new Date() + " - init thread local value. in " + Thread.currentThread());
			return new My50MB();
		}
	};

	public static void main(String[] args) throws InterruptedException {
		Math.random();
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				My50MB m = tl.get();
				// do anything with m
			}
		}, "one");
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				My50MB m = tl.get();
				// do anything with m
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					// ignore
				}
			}
		}, "two");
		t.start();
		t2.start();
		t.join();
		System.gc();
		System.out.println(new Date() + " - GC");
		t2.join();
		System.gc();
		System.out.println(new Date() + " - GC");
		TimeUnit.MILLISECONDS.sleep(100);
	}

	public static class My50MB {
		private String name = Thread.currentThread().getName();
		private byte[] a = new byte[1024 * 1024 * 50];

		@Override
		protected void finalize() throws Throwable {
			System.out.println(new Date() + " - My 50 MB finalized. in " + name);
			super.finalize();
		}
	}
}
