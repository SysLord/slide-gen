/*
 * Copyright 2014 Higher Frequency Trading
 *
 * http://www.higherfrequencytrading.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.openhft.compiler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * This class support loading and debugging Java Classes dynamically.
 */
@SuppressWarnings("rawtypes")
public final class CompilerUtils {

	public static final Compiler COMPILER = new Compiler();

	private static final Method DEFINE_CLASS_METHOD = createDefineClassMethod();

	static JavaCompiler JAVA_COMPILER = createJavaCompiler();

	static StandardJavaFileManager STANDARD_JAVA_FILEMANAGER;

	private static Method createDefineClassMethod() throws AssertionError {
		try {
			Method declaredMethod = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class,
					int.class);
			declaredMethod.setAccessible(true);
			return declaredMethod;
		} catch (NoSuchMethodException e) {
			throw new AssertionError(e);
		}
	}

	private static JavaCompiler createJavaCompiler() {
		JavaCompiler systemJavaCompiler = ToolProvider.getSystemJavaCompiler();
		if (systemJavaCompiler == null) {
			return tryCreateCompilerFallback();
		}
		return systemJavaCompiler;
	}

	private static JavaCompiler tryCreateCompilerFallback() throws AssertionError {
		try {
			Class<?> javacTool = Class.forName("com.sun.tools.javac.api.JavacTool");
			Method create = javacTool.getMethod("create");
			return (JavaCompiler) create.invoke(null);
		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}

	public static Class defineClass(ClassLoader classLoader, String className, byte[] bytes) {
		try {
			return (Class) DEFINE_CLASS_METHOD.invoke(classLoader, className, bytes, 0, bytes.length);
		} catch (IllegalAccessException e) {
			throw new AssertionError(e);
		} catch (InvocationTargetException e) {
			// noinspection ThrowInsideCatchBlockWhichIgnoresCaughtException
			throw new AssertionError(e.getCause());
		}
	}

}
