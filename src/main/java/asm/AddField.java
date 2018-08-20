package asm;

import org.objectweb.asm.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;

/**
 * @author zhangym
 * @version 1.0  2018/8/9
 */
public class AddField extends AbstractClassVistor {
    private boolean isPresent;
    private int access;
    private String name;
    private String desc;
    private Object value;

    public AddField(int access, String name, String desc, Object value, ClassVisitor classVisitor){
        super(Opcodes.ASM5, classVisitor);
        this.access = access;
        this.desc = desc;
        this.name = name;
        this.value = value;
    }
    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value){
        if(name.equals(this.name)) {
            isPresent = true;
        }
        return super.visitField(access, name, desc, signature, value);
    }
    @Override
    public void visitEnd(){
        if(!isPresent){
            FieldVisitor fieldVisitor = super.visitField(access, name, desc, null, value);
            if(fieldVisitor != null){
                //不是原有属性，故不会有事件发出，直接end
                fieldVisitor.visitEnd();
            }
        }
        super.visitEnd();
    }

    public static void main(String[] args) throws Exception{
        ClassReader classReader = new ClassReader(new FileInputStream("D:\\NettyLearning\\target\\classes\\asm\\TestBean.class"));
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        ClassVisitor addField = new AddField(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC + Opcodes.ACC_FINAL,
                "newField2",
                Type.getDescriptor(String.class),
                "newValue2",
                classWriter);
        classReader.accept(addField, ClassReader.SKIP_DEBUG);
        byte[] newClass = classWriter.toByteArray();
        File newFile = new File("D:\\NettyLearning\\target\\classes\\asm\\TestBean.class");
        new FileOutputStream(newFile).write(newClass);
        TestBean testBean = new TestBean();
        testBean.setId("1");
        testBean.setName("张飒");
        Field[] fields = testBean.getClass().getDeclaredFields();
        for(Field field : fields){
            System.out.println(field.toString());
        }

    }
}
