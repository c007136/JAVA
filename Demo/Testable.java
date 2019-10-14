public class Testable {
	public void execute() {
		System.out.println("Excuting..");
	}

	@Test void testExecute() {
		execute();
	}
}