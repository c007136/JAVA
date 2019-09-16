import annotation.*;

@TestA(name="type", gid=Long.class)
public class UserAnnotation {
	@TestA(name="param", id=1, gid=Long.class)
	private Integer age;

	@TestA(name="construct", id=2, gid=Long.class)
	public UserAnnotation() {}

	@TestA(name="public method", id=3, gid=Long.class)
	public void a() {
	}

	public void b() {}
}