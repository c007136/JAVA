// typeinfo包
// TI只是个前缀，避免重名

package typeinfo;

class TIC implements TIA {
	public void f() {
		System.out.println("C f()");
	}

	public void g() {
		System.out.println("C g()");
	}

	void u() {
	    System.out.println("C u() -- package");	
	}

	protected void v() {
		System.out.println("C v() -- protected");	
	}

	private void w() {
		System.out.println("C v() -- private");	
	}
}

public class HiddenC extends TIC {
	public static TIA makeA() { return new TIC(); }
}