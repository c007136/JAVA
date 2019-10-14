import java.util.*;
import java.lang.annotation.*;
import java.io.*;
import com.sun.mirror.apt.*;
import com.sun.mirror.declaration.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@interface ExtractInterface {
    public String value();
}

@ExtractInterface("IMultiplier")
class Multiplier {
    public int multiply(int x, int y) {
        int total = 0;
        for (int i = 0; i < x; i++) {
            total = add(total, y);
        }
        return total;
    }

    private int add(int x, int y) {
        return x + y;
    }
}

class InterfaceExtractorProcessor implements AnnotationProcessor {
    private final AnnotationProcessorEnvironment env;
    private ArrayList<MethodDeclaration> interfaceMethods = new ArrayList<MethodDeclaration>();

    public InterfaceExtractorProcessor(AnnotationProcessorEnvironment e) {
        env = e;
    }

    public void process() {
        for (TypeDeclaration d : env.getSpecifiedTypeDeclarations()) {
            ExtractInterface a = d.getAnnotation(ExtractInterface.class);
            if (a == null) {
                break;
            }

            for (MethodDeclaration m : d.getMethods()) {
                if (m.getModifiers().contains(Modifier.PUBLIC) && 
                    !(m.getModifiers().contains(Modifier.STATIC))) {
                    interfaceMethods.add(m);
                }
            }

            if (interfaceMethods.size() > 0) {
                try {
                    PrintWriter writer = env.getFiler().createSourceFile(a.value());
                    writer.println("package " + d.getPackage().getQualifiedName() + ";");
                    writer.println("public interface " + a.value() + " {");
                    for (MethodDeclaration m : interfaceMethods) {
                        writer.print("    public ");
                        writer.print(m.getReturnType() + " ");
                        writer.print(m.getSimpleName() + " (");
                        int i = 0;
                        for (ParameterDeclaration param : m.getParameters()) {
                            writer.print(param.getType() + " " + param.getSimpleName());
                            if (++i < m.getParameters().size()) {
                                writer.print(", ");
                            }
                        }
                        writer.println(");");
                    }
                    writer.println("}");
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

public class InterfaceExtractorProcessorFactory implements AnnotationProcessorFactory {
    public AnnotationProcessor getProcessorFor(Set<AnnotationTypeDeclaration> atds, AnnotationProcessorEnvironment env) {
        return new InterfaceExtractorProcessor(env);
    }

    public Collection<String> supportedAnnotationTypes() {
        return Collections.singleton("ExtractInterface");
    }

    public Collection<String> supportedOptions() {
        return Collections.emptySet();
    }
}
