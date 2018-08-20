package asm;

import com.sun.org.apache.xpath.internal.compiler.OpCodes;
import jdk.internal.org.objectweb.asm.Opcodes;
import org.objectweb.asm.ClassVisitor;

/**
 * @author zhangym
 * @version 1.0  2018/8/9
 */
public class AbstractClassVistor extends ClassVisitor {
    public AbstractClassVistor(int i) {
        super(i);
    }

    public AbstractClassVistor(int i, ClassVisitor classVisitor) {
        super(i, classVisitor);
    }
}
