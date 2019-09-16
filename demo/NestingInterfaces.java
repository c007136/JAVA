class A {
    public interface D {
        void f();
    }
    private class DImp implements D {
        public void f() {}
    }
    public class DImp2 implements D {
        public void f() {
        	System.out.println("D2_f");
        }
    }
    public D getD() { return new DImp2(); }
    private D dRef;
    public void receiveD(D d) {
        dRef = d;
        dRef.f();
    }
}
 
public class NestingInterfaces {
    public static void main(String[] args) {
        A a = new A();

        //A.DImp2 di2 = A.DImp2();

        //The type A.D is not visible
        A.D ad = a.getD();
        ad.f();
        //Cannot convert from A.D to A.DImp2
        //A.DImp2 di2 = a.getD();
        //The type A.D is not visible
        //! a.getD().f();        
        //A a2 = new A();
        //a2.receiveD(a.getD());
    }
}