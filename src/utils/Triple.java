package utils;

public class Triple<A, B, C> {
	private A first;
	private B second;
	private C third;
	public Triple(A first, B second, C third) {
		super();
		this.first = first;
		this.second = second;
		this.third = third;
	}
	/**
	 * @return the first
	 */
	public A getFirst() {
		return first;
	}
	/**
	 * @return the second
	 */
	public B getSecond() {
		return second;
	}
	/**
	 * @return the third
	 */
	public C getThird() {
		return third;
	}
}
