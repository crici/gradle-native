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

import dev.nokee.xcode.objects.swiftpackage.XCRemoteSwiftPackageReference;
import lombok.EqualsAndHashCode;

import javax.annotation.Nullable;

import static dev.nokee.xcode.project.RecodeableKeyedObject.of;

@EqualsAndHashCode
public final class CodeableVersionRequirementRevision implements XCRemoteSwiftPackageReference.VersionRequirement.Revision, Codeable {
	public enum CodingKeys implements CodingKey {
		kind,
		revision,
		;


		@Override
		public String getName() {
			return name();
		}
	}

	private final KeyedObject delegate;

	public CodeableVersionRequirementRevision(String revision) {
		this(new DefaultKeyedObject.Builder().put(CodingKeys.kind, Kind.REVISION).put(CodingKeys.revision, revision).build());
	}

	public CodeableVersionRequirementRevision(KeyedObject delegate) {
		this.delegate = delegate;
	}

	@Override
	public Kind getKind() {
		return delegate.tryDecode(CodingKeys.kind);
	}

	public String getRevision() {
		return delegate.tryDecode(CodingKeys.revision);
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
	public void encode(Codeable.EncodeContext context) {
		delegate.encode(context);
	}

	@Override
	public <T> T tryDecode(CodingKey key) {
		return delegate.tryDecode(key);
	}

	@Override
	public String toString() {
		return "require revision '" + getRevision() + "'";
	}

	public static CodeableVersionRequirementRevision newInstance(KeyedObject delegate) {
		return new CodeableVersionRequirementRevision(new RecodeableKeyedObject(delegate, of(CodingKeys.values())));
	}
}
