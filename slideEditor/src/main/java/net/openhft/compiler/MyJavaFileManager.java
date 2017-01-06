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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;

class MyJavaFileManager implements JavaFileManager {

	private final StandardJavaFileManager fileManager;

	private final Map<String, ByteArrayOutputStream> buffers = new LinkedHashMap<String, ByteArrayOutputStream>();

	MyJavaFileManager(StandardJavaFileManager fileManager) {
		this.fileManager = fileManager;
	}

	@Override
	public ClassLoader getClassLoader(Location location) {
		return fileManager.getClassLoader(location);
	}

	@Override
	public Iterable<JavaFileObject> list(Location location, String packageName, Set<Kind> kinds, boolean recurse)
			throws IOException {
		return fileManager.list(location, packageName, kinds, recurse);
	}

	@Override
	public String inferBinaryName(Location location, JavaFileObject file) {
		return fileManager.inferBinaryName(location, file);
	}

	@Override
	public boolean isSameFile(FileObject a, FileObject b) {
		return fileManager.isSameFile(a, b);
	}

	@Override
	public boolean handleOption(String current, Iterator<String> remaining) {
		return fileManager.handleOption(current, remaining);
	}

	@Override
	public boolean hasLocation(Location location) {
		return fileManager.hasLocation(location);
	}

	@Override
	public JavaFileObject getJavaFileForInput(Location location, String className, Kind kind) throws IOException {
		if (location == StandardLocation.CLASS_OUTPUT && buffers.containsKey(className) && kind == Kind.CLASS) {
			final byte[] bytes = buffers.get(className).toByteArray();
			return new SimpleJavaFileObject(URI.create(className), kind) {

				@Override
				public InputStream openInputStream() {
					return new ByteArrayInputStream(bytes);
				}
			};
		}
		return fileManager.getJavaFileForInput(location, className, kind);
	}

	@Override
	public JavaFileObject getJavaFileForOutput(Location location, final String className, Kind kind, FileObject sibling)
			throws IOException {
		return new SimpleJavaFileObject(URI.create(className), kind) {

			@Override
			public OutputStream openOutputStream() {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				buffers.put(className, baos);
				return baos;
			}
		};
	}

	@Override
	public FileObject getFileForInput(Location location, String packageName, String relativeName) throws IOException {
		return fileManager.getFileForInput(location, packageName, relativeName);
	}

	@Override
	public FileObject getFileForOutput(Location location, String packageName, String relativeName, FileObject sibling)
			throws IOException {
		return fileManager.getFileForOutput(location, packageName, relativeName, sibling);
	}

	@Override
	public void flush() throws IOException {
		// Do nothing
	}

	@Override
	public void close() throws IOException {
		fileManager.close();
	}

	@Override
	public int isSupportedOption(String option) {
		return fileManager.isSupportedOption(option);
	}

	public void clearBuffers() {
		buffers.clear();
	}

	public Map<String, byte[]> getAllBuffers() {
		Map<String, byte[]> ret = new LinkedHashMap<String, byte[]>(buffers.size() * 2);
		for (Map.Entry<String, ByteArrayOutputStream> entry : buffers.entrySet()) {
			ret.put(entry.getKey(), entry.getValue().toByteArray());
		}
		return ret;
	}
}