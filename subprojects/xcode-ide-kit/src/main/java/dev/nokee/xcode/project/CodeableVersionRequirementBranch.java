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

import com.google.common.collect.ImmutableSet;
import dev.nokee.xcode.objects.swiftpackage.XCRemoteSwiftPackageReference;
import lombok.EqualsAndHashCode;

import javax.annotation.Nullable;

@EqualsAndHashCode
public class CodeableVersionRequirementBranch implements XCRemoteSwiftPackageReference.VersionRequirement.Branch, Codeable {
	public enum CodingKeys implements CodingKey {
		kind,
		branch,
		;

		@Override
		public String getName() {
			return name();
		}
	}

	private final KeyedObject delegate;

	public CodeableVersionRequirementBranch(String branch) {
		this(new DefaultKeyedObject.Builder().put(CodingKeys.kind, Kind.BRANCH).put(CodingKeys.branch, branch).build());
	}

	public CodeableVersionRequirementBranch(KeyedObject delegate) {
		this.delegate = delegate;
	}

	@Override
	public Kind getKind() {
		return delegate.tryDecode(CodingKeys.kind);
	}

	public String getBranch() {
		return delegate.tryDecode(CodingKeys.branch);
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
	public void encode(EncodeContext context) {
		delegate.encode(context);
	}

	@Override
	public <T> T tryDecode(CodingKey key) {
		return delegate.tryDecode(key);
	}

	@Override
	public String toString() {
		return "require branch '" + getBranch() + "'";
	}

	public static CodeableVersionRequirementBranch newInstance(KeyedObject delegate) {
		return new CodeableVersionRequirementBranch(new RecodeableKeyedObject(delegate, ImmutableSet.copyOf(CodingKeys.values())));
	}
}
