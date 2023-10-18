package cz.ladicek.analysis;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicInterpreter;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.SimpleVerifier;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MyAnalysis {
    private static final String expectedOwner = "cz/ladicek/fwk/MyConsumer";
    private static final String expectedName = "accept";
    private static final String expectedDescriptor = "(Ljava/lang/Object;)V";

    public static void main(String[] args) throws IOException, AnalyzerException {
        ClassNode cn = new ClassNode(Opcodes.ASM9);
        ClassReader cr = new ClassReader(MyAnalysis.class.getResourceAsStream("/cz/ladicek/app/Caller.class"));
        cr.accept(cn, 0);

        for (MethodNode method : cn.methods) {
            if (method.name.equals("doSomething")) {
                analyze(cn, method);
            }
        }
    }

    private static void analyze(ClassNode clazz, MethodNode method) throws AnalyzerException {
        Type currentClass = Type.getObjectType(clazz.name);
        Type currentSuperClass = Type.getObjectType(clazz.superName);
        List<Type> currentInterfaces = clazz.interfaces.stream().map(Type::getObjectType).collect(Collectors.toList());
        boolean isInterface = (clazz.access & Opcodes.ACC_INTERFACE) == Opcodes.ACC_INTERFACE;
        BasicInterpreter interpreter = new SimpleVerifier(Opcodes.ASM9, currentClass, currentSuperClass, currentInterfaces, isInterface) {
            @Override
            public BasicValue naryOperation(AbstractInsnNode insn, List<? extends BasicValue> values) throws AnalyzerException {
                if (insn.getType() == AbstractInsnNode.METHOD_INSN) {
                    MethodInsnNode method = (MethodInsnNode) insn;
                    if (expectedOwner.equals(method.owner)
                            && expectedName.equals(method.name)
                            && expectedDescriptor.equals(method.desc)) {
                        System.out.println("- " + values);
                    }
                }
                return super.naryOperation(insn, values);
            }
        };
        Analyzer<BasicValue> analyzer = new Analyzer<>(interpreter);
        analyzer.analyze(clazz.name, method);
    }
}
