/**
 * Copyright 2007 Google Inc.
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

package com.tonicsystems.jarjar;

import com.tonicsystems.jarjar.util.EntryStruct;
import com.tonicsystems.jarjar.util.JarProcessor;

import java.io.IOException;

class ResourceProcessor implements JarProcessor {

    public static final String TEMP_EXTENSION = "jarjarTemp";
    private PackageRemapper pr;

    public ResourceProcessor(PackageRemapper pr) {
        this.pr = pr;
    }

    public boolean process(EntryStruct struct) throws IOException {

        if ( !struct.name.endsWith( ".class" ) ) {

            /*
            If we found a file without extension lets add it a temporal one in order
            to be replaced properly by the PackageRemapper preventing the file to be confused with a directory.
             */
            Boolean addedExtension = false;
            int slash = struct.name.lastIndexOf( '/' );
            if ( !struct.isDirectory && slash != (struct.name.length() - 1) && !struct.name.contains( "." ) ) {
                struct.name += "." + TEMP_EXTENSION;
                addedExtension = true;
            }

            //Replace the path
            struct.name = pr.mapPath( struct.name );

            //Remove the temp extension if added
            if ( addedExtension ) {
                struct.name = struct.name.replace( "." + TEMP_EXTENSION, "" );
            }
        }
        return true;
    }

}