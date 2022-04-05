/*
 * Copyright 2020 the original author or authors.
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
package dev.nokee.ide.xcode.internal.xcodeproj;

import com.dd.plist.NSDictionary;
import com.google.common.base.Preconditions;

import java.util.Map;

public class PBXBuildStyle extends PBXProjectItem {
    private final String name;
    private NSDictionary buildSettings;

    public PBXBuildStyle(String name) {
        this.name = Preconditions.checkNotNull(name);
        this.buildSettings = new NSDictionary();
    }

	public PBXBuildStyle(String name, Map<String, ?> buildSettings) {
		this.name = Preconditions.checkNotNull(name);
		this.buildSettings = new NSDictionary();
		buildSettings.forEach((k, v) -> this.buildSettings.put(k, v));
	}

    public String getName() {
        return name;
    }

    public NSDictionary getBuildSettings() {
        return buildSettings;
    }

    public void setBuildSettings(NSDictionary buildSettings) {
        this.buildSettings = buildSettings;
    }

    @Override
    public int stableHash() {
        return name.hashCode();
    }
}
