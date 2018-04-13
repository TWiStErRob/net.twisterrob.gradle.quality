package adsf;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class X {

	@Test
	public void f() {
		I<String> mockI = mock(N.class);
		when(mockI.i()).thenReturn("he");
		new D(mockI);
	}
}
interface I<T> {
	T i();
}
interface N extends I<String> {
}
class D {
	D(I<String> i) {
		String s = i.i();
		System.out.println(s);
	}
}
