/*
 * Copyright 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.nokee.xcode.project;

import dev.nokee.xcode.objects.buildphase.PBXBuildFile;
import dev.nokee.xcode.objects.buildphase.PBXCopyFilesBuildPhase;
import lombok.EqualsAndHashCode;

import javax.annotation.Nullable;
import java.util.List;

import static dev.nokee.xcode.project.PBXTypeSafety.orEmptyList;
import static dev.nokee.xcode.project.RecodeableKeyedObject.ofIsaAnd;

@EqualsAndHashCode
public final class CodeablePBXCopyFilesBuildPhase implements PBXCopyFilesBuildPhase, Codeable {
	public enum CodingKeys implements CodingKey {
		files,
		dstPath,
		dstSubfolderSpec,
		;

		@Override
		public String getName() {
			return name();
		}
	}

	private final KeyedObject delegate;

	public CodeablePBXCopyFilesBuildPhase(KeyedObject delegate) {
		this.delegate = delegate;
	}

	@Override
	public List<PBXBuildFile> getFiles() {
		return orEmptyList(delegate.tryDecode(CodingKeys.files));
	}

	@Override
	public String getDstPath() {
		return delegate.tryDecode(CodingKeys.dstPath);
	}

	@Override
	public SubFolder getDstSubfolderSpec() {
		return delegate.tryDecode(CodingKeys.dstSubfolderSpec);
	}

	@Override
	public Builder toBuilder() {
		return new Builder(delegate);
	}

	@Override
	public String toString() {
		return String.format("%s isa=%s", super.toString(), this.getClass().getSimpleName());
	}

	@Override
	public String isa() {
		return delegate.isa();
	}

	@Nullable
	@Override
	public String globalId() {
		return delegate.globalId();
	}

	@Override
	public long age() {
		return delegate.age();
	}

	@Override
	public void encode(EncodeContext context) {
		delegate.encode(context);
	}

	@Override
	public <T> T tryDecode(CodingKey key) {
		return delegate.tryDecode(key);
	}

	public static CodeablePBXCopyFilesBuildPhase newInstance(KeyedObject delegate) {
		return new CodeablePBXCopyFilesBuildPhase(new RecodeableKeyedObject(delegate, ofIsaAnd(CodingKeys.values())));
	}
}
