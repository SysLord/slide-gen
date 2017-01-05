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

import static net.openhft.compiler.CompilerUtils.JAVA_COMPILER;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;

import com.sun.istack.internal.Nullable;

/*
 * I needed to modify the net.openhft.compiler because it used caching which prevented classes and classloaders to be garbage collected.
 */
@SuppressWarnings("rawtypes")
public class Compiler {

	private static final PrintWriter DEFAULT_WRITER = new PrintWriter(System.err);

	public Class loadFromJava(ClassLoader classLoader, String className, String javaCode) throws ClassNotFoundException {
		return loadFromJava(classLoader, className, javaCode, DEFAULT_WRITER);
	}

	Map<String, byte[]> compileFromJava(String className, String javaCode, final PrintWriter writer,
			MyJavaFileManager fileManager) {

		DiagnosticListener<? super JavaFileObject> diagnosticListener = diagnostic -> {
			if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
				writer.println(diagnostic);
			}
		};

		CompilationTask task = JAVA_COMPILER.getTask(
				writer,
				fileManager,
				diagnosticListener,
				null,
				null,
				Arrays.asList(new JavaSourceFromString(className, javaCode)));

		boolean ok = task.call();
		if (!ok) {
			return Collections.emptyMap();
		}
		return fileManager.getAllBuffers();
	}

	public Class loadFromJava(ClassLoader classLoader,
			String className,
			String javaCode,
			@Nullable PrintWriter writer) throws ClassNotFoundException {
		PrintWriter printWriter = writer == null ? DEFAULT_WRITER : writer;

		StandardJavaFileManager standardJavaFileManager = JAVA_COMPILER.getStandardFileManager(null, null, null);
		MyJavaFileManager fileManager = new MyJavaFileManager(standardJavaFileManager);

		for (Map.Entry<String, byte[]> entry : compileFromJava(className, javaCode, printWriter, fileManager).entrySet()) {
			CompilerUtils.defineClass(classLoader, entry.getKey(), entry.getValue());
		}

		return classLoader.loadClass(className);
	}
}
