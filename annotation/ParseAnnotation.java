// 参考资料
// https://blog.csdn.net/chenchaofuck1/article/details/52006961
// https://blog.csdn.net/briblue/article/details/73824058

import annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ParseAnnotation {
	public static void parseTypeAnnotation() throws ClassNotFoundException {
		Class clazz = Class.forName("UserAnnotation");

		Annotation[] annotations = clazz.getAnnotations();
		for (Annotation annotation : annotations) {
			TestA testA = (TestA)annotation;
			System.out.println("id= " + testA.id() + "; name= " + testA.name()+ "; gid = " + testA.gid()); 
		}
	}

	public static void parseMethodAnnotation() {
		Method[] methods = UserAnnotation.class.getDeclaredMethods();
		for (Method method : methods) {
			boolean hasAnnotation = method.isAnnotationPresent(TestA.class);
			if (hasAnnotation) {
				TestA annotation = method.getAnnotation(TestA.class);
				System.out.println("method = " + method.getName()  
                        + " ; id = " + annotation.id() + " ; description = "  
                        + annotation.name() + "; gid= "+annotation.gid());  
			}
		}
	}

	public static void main(String[] args) throws ClassNotFoundException {
		parseTypeAnnotation();
		parseMethodAnnotation();
	}
}