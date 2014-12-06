/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package learn.bytecode;

import java.lang.reflect.Method;

import learn.bytecode.util.ClassFileUtils;

import org.jboss.classfilewriter.ClassFile;
import org.jboss.classfilewriter.ClassMethod;
import org.jboss.classfilewriter.code.CodeAttribute;

public class Main {

    private static final Method GET_INT_METHOD;

    static {
        try {
            GET_INT_METHOD = Calculator.class.getMethod("getInt", int.class);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException {

        // class CalculatorSubclass extends Calculator {
        ClassFile classFile = new ClassFile("CalculatorSubclass", Calculator.class.getName());

        // generate default constructor
        generateConstructor(classFile);

        // override Calculator.getInt(int value)
        generateGetInt(classFile, GET_INT_METHOD);

        // generate and load CalculatorSubclass
        Class<?> proxyClass = ClassFileUtils.toClass(classFile, Calculator.class.getClassLoader(), Calculator.class.getProtectionDomain());
        // create new instance
        Calculator result = (Calculator) proxyClass.newInstance();

        // See what it returns
        System.out.println("CalculatorSubclass.getInt(3): " + result.getInt(3));
        System.out.println("CalculatorSubclass.getInt(-4): " + result.getInt(-4));
    }

    private static void generateConstructor(ClassFile classFile) throws NoSuchMethodException, SecurityException {
        ClassMethod constructor = classFile.addConstructor(Calculator.class.getConstructor());
        CodeAttribute b = constructor.getCodeAttribute();
        b.aload(0);
        b.invokespecial(Calculator.class.getConstructor());
        b.returnInstruction();
    }

    private static void generateGetInt(ClassFile classFile, Method superclassMethod) {
        // TODO add code here
    }









    /*
     * Example 1: Override Calculator.getInt() to always return 5
     */
//    private static void generateGetInt(ClassFile classFile, Method superclassMethod) {
//        CodeAttribute b = classFile.addMethod(superclassMethod).getCodeAttribute();
//        b.iconst(5); // push 5 on stack
//        b.returnInstruction(); // return
//    }

    /*
     * Example 2: Override Calculator.getInt() to multiply the parameter by 4
     */
//    private static void generateGetInt(ClassFile classFile, Method superclassMethod) {
//        CodeAttribute b = classFile.addMethod(superclassMethod).getCodeAttribute();
//        b.iload(1); // load parameter to stack
//        b.iconst(4); // push 4 to stack
//        b.imul(); // multiply
//        b.returnInstruction(); // return
//    }

    /*
     * Example 3: Override Calculator.getInt() to return the value passed as parameter if positive and to return -value if negative
     */
//    private static void generateGetInt(ClassFile classFile, Method superclassMethod) {
//        CodeAttribute b = classFile.addMethod(superclassMethod).getCodeAttribute();
//        b.iload(1); // load parameter to stack
//        b.dup(); // (value) -> (value, value) first value is consumed by if, the second used later
//        BranchEnd end = b.ifge(); // if the top of stack is >= 0 jump to branchend
//        b.ineg(); // negate the value on stack
//        b.branchEnd(end); // end conditional branch
//        b.returnInstruction(); // return
//    }

    /*
     * Example 4: Override Calculator.getInt() to return an absolute value of the value passed as parameter using Math.abs()
     */
//    private static void generateGetInt(ClassFile classFile, Method superclassMethod) {
//        CodeAttribute b = classFile.addMethod(superclassMethod).getCodeAttribute();
//        b.iload(1); // load parameter to stack
//        b.invokestatic(Math.class.getName(), "abs", "(I)I"); // invoke Math.abs()
//        b.returnInstruction(); // return
//    }

    /*
     * Example 5: Override Calculator.getInt() to invoke super.getInt() twice, sum up the result and return it from the method
     */
//    private static void generateGetInt(ClassFile classFile, Method superclassMethod) {
//        CodeAttribute b = classFile.addMethod(superclassMethod).getCodeAttribute();
//        b.aload(0); // load this
//        b.iload(1); // load parameter to stack
//        b.invokespecial(GET_INT_METHOD); // super.getInt(value)
//        b.aload(0); // load this
//        b.iload(1); // load parameter to stack
//        b.invokespecial(GET_INT_METHOD); // super.getInt(value)
//        b.iadd(); // sums up the two results (result1, result2) -> (sum)
//        b.returnInstruction(); // return
//    }

    /*
     * Example 6: Override Calculator.getInt() to do return new Random.nextInt(super.getInt(value));
     */
//    private static void generateGetInt(ClassFile classFile, Method superclassMethod) {
//        CodeAttribute b = classFile.addMethod(superclassMethod).getCodeAttribute();
//        b.aload(0); // load this
//        b.iload(1); // load parameter to stack
//        b.invokespecial(GET_INT_METHOD); // super.getInt(value)
//        b.istore(2); // store this in a local variable for later use
//        b.newInstruction(Random.class); // allocate a new instance
//        b.dup(); // dup as we need reference to random for constructor invocation and then later for nextInt() invocation
//        b.invokespecial(Random.class.getName(), "<init>", "()V"); // mandatory constructor call
//        b.iload(2); // load previously stored value
//        b.invokevirtual(Random.class.getName(), "nextInt", "(I)I"); // invoke Random.nextInt(int bound)
//        b.returnInstruction(); // return
//    }

}
