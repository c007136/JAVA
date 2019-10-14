
import javax.annotation.*;
import java.lang.annotation.*;

@SupportedSourceVersion(SourceVersion.RELEASE_7)
// 指定可以处理这三个 Annotation
@SupportedAnnotationTypes({"Persistent" , "Id" , "Property"})
public class HibernateAnnotationProcessor
	extends AbstractProcessor
{
	// 循环处理每个需要处理的程序对象
	public boolean process(Set<? extends TypeElement> annotations
		, RoundEnvironment roundEnv)
	{
		// 定义文件输出流，用于生成文件
		PrintStream ps = null;
		try
		{
			// 遍历每个被 @Persistent 修饰的 class 文件
			for (Element t : roundEnv.getElementsAnnotatedWith(Persistent.class))
			{
				// 获得正在处理的类名
				Name clazzName = t.getSimpleName();
				// 获取类定义前的 @Persistent Annotation
				Persistent per = t.getAnnotation(Persistent.class);
				
				ps = new PrintStream(new FileOutputStream(clazzName
					+ ".hbm.xml"));
				
				ps.println("<?xml version=\"1.0\"?>");
				ps.println("<!DOCTYPE hibernate-mapping PUBLIC");
				ps.println("	\"-//Hibernate/Hibernate "
					+ "Mapping DTD 3.0//EN\"");
				ps.println("	\"http://www.hibernate.org/dtd/"
					+ "hibernate-mapping-3.0.dtd\">");
				ps.println("<hibernate-mapping>");
				ps.print("	<class name=\"" + t);
				ps.println("\" table=\"" + per.table() + "\">");
				for (Element f : t.getEnclosedElements())
				{
					// 只处理成员变量上的 annotation
					if (f.getKind() == ElementKind.FIELD)   
					{
						// 获取成员变量定义前的 Annotation
						Id id = f.getAnnotation(Id.class);      
						// 当 @Id Annotation 存在时输出 <id.../>元素
						if(id != null)
						{
							ps.println("		<id name=\""
								+ f.getSimpleName()
								+ "\" column=\"" + id.column()
								+ "\" type=\"" + id.type()
								+ "\">");
							ps.println("		<generator class=\""
								+ id.generator() + "\"/>");
							ps.println("		</id>");
						}
						// 获取成员变量定义前的@Property Annotation
						Property p = f.getAnnotation(Property.class);  
						// 输出
						if (p != null)
						{
							ps.println("		<property name=\""
								+ f.getSimpleName()
								+ "\" column=\"" + p.column()
								+ "\" type=\"" + p.type()
								+ "\"/>");
						}
					}
				}
				ps.println("	</class>");
				ps.println("</hibernate-mapping>");
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
		 // close
		}
		return true;
	}
}