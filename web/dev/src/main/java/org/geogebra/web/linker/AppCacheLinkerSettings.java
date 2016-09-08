/*
 * Copyright 2011 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.geogebra.web.linker;

/**
 * A custom linker that generates an app cache manifest with the files generated
 * by the GWT compiler and the static files used by this application.
 * <p>
 * Before using this approach with production code be sure that you understand
 * the limitations of {@link AppCacheLinker}, namely that it sends all
 * permutations to the client.
 * 
 * @see AppCacheLinker
 */

public class AppCacheLinkerSettings {
	protected static String[] otherCachedFiles() {
		return new String[] {
				"https://www.geogebra.org/graphing", "https://www.geogebra.org/cas",
				"https://www.geogebra.org/3d", "https://www.geogebra.org/spreadsheet",
				"https://www.geogebra.org/probability", "https://www.geogebra.org/geometry",
				"https://www.geogebra.org/exam",
		};
	}
}