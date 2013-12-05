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

import junit.framework.TestCase;

import java.util.Collections;

public class PackageRemapperTest
        extends TestCase {
    protected PackageRemapper remapper;

    public void testMapValue() {

        Rule rule = new Rule();
        rule.setPattern("org.**");
        rule.setResult("foo.@1");
        remapper = new PackageRemapper(Collections.singletonList(rule), false);

        assertUnchangedValue("[^\\s;/@&=,.?:+$]");
        assertUnchangedValue("[Ljava/lang/Object;");
        assertUnchangedValue("[Lorg/example/Object;");
        assertUnchangedValue("[Ljava.lang.Object;");
        assertUnchangedValue("[Lorg.example/Object;");
        assertUnchangedValue("[L;");
        assertUnchangedValue("[Lorg.example.Object;;");
        assertUnchangedValue("[Lorg.example.Obj ct;");
        assertUnchangedValue("org.example/Object");

        assertEquals("[Lfoo.example.Object;", remapper.mapValue("[Lorg.example.Object;"));
        assertEquals("foo.example.Object", remapper.mapValue("org.example.Object"));
        assertEquals("foo/example/Object", remapper.mapValue("org/example/Object"));
        assertEquals("foo/example.Object", remapper.mapValue("org/example.Object")); // path match

        assertEquals("foo.example.package-info", remapper.mapValue("org.example.package-info"));
        assertEquals("foo/example/package-info", remapper.mapValue("org/example/package-info"));
        assertEquals("foo/example.package-info", remapper.mapValue("org/example.package-info"));
    }

    public void testMapValueSingle() {

        Rule rule = new Rule();
        rule.setPattern("org.example.*");
        rule.setResult("foo.example.@1");
        remapper = new PackageRemapper(Collections.singletonList(rule), false);

        assertUnchangedValue("[^\\s;/@&=,.?:+$]");
        assertUnchangedValue("[Ljava/lang/Object;");
        assertUnchangedValue("[Lorg/example/Object;");
        assertUnchangedValue("[Ljava.lang.Object;");
        assertUnchangedValue("[Lorg.example/Object;");
        assertUnchangedValue("[L;");
        assertUnchangedValue("[Lorg.example.Object;;");
        assertUnchangedValue("[Lorg.example.Obj ct;");
        assertUnchangedValue("org.example/Object");

        assertEquals("[Lfoo.example.Object;", remapper.mapValue("[Lorg.example.Object;"));
        assertEquals("foo.example.Object", remapper.mapValue("org.example.Object"));
        assertEquals("foo/example/Object", remapper.mapValue("org/example/Object"));
        assertEquals("foo/example/test.Object", remapper.mapValue("org/example/test.Object")); // path match

        assertEquals("foo.example.package-info", remapper.mapValue("org.example.package-info"));
        assertEquals("foo/example/package-info", remapper.mapValue("org/example/package-info"));
        assertEquals("foo/example/test.package-info", remapper.mapValue("org/example/test.package-info"));
    }

    public void testDwrValue() {

        Rule rule = new Rule();
        rule.setPattern("org.directwebremoting.**");
        rule.setResult("com.dotcms.repackage.dwr_3rc2modified.org.directwebremoting.@1");
        PackageRemapper packageRemapper = new PackageRemapper(Collections.singletonList(rule), false);

        assertEquals("/com/dotcms/repackage/dwr_3rc2modified/org/directwebremoting/dwr.xml", packageRemapper.mapValue("/org/directwebremoting/dwr.xml"));
        assertEquals("/com/dotcms/repackage/dwr_3rc2modified/org/directwebremoting", packageRemapper.mapValue("/org/directwebremoting"));
        assertEquals("com/dotcms/repackage/dwr_3rc2modified/org/directwebremoting/spring/spring-dwr-3.0.xsd", packageRemapper.mapValue("org/directwebremoting/spring/spring-dwr-3.0.xsd"));

        rule = new Rule();
        rule.setPattern("org.directwebremoting.spring.*");
        rule.setResult("com.dotcms.repackage.dwr_3rc2modified.org.directwebremoting.spring.@1");
        packageRemapper = new PackageRemapper(Collections.singletonList(rule), false);

        assertEquals("/org/directwebremoting/dwr.xml", packageRemapper.mapValue("/org/directwebremoting/dwr.xml"));
        assertEquals("/org/directwebremoting", packageRemapper.mapValue("/org/directwebremoting"));
        assertEquals("/org/directwebremoting/spring/test", packageRemapper.mapValue("/org/directwebremoting/spring/test"));
        assertEquals("com/dotcms/repackage/dwr_3rc2modified/org/directwebremoting/spring/spring-dwr-3.0.xsd", packageRemapper.mapValue("org/directwebremoting/spring/spring-dwr-3.0.xsd"));
    }

    public void testJnaValue() {

        Rule rule = new Rule();
        rule.setPattern("com.sun.jna.**");
        rule.setResult("com.dotcms.repackage.jna.com.sun.jna.@1");
        PackageRemapper packageRemapper = new PackageRemapper(Collections.singletonList(rule), false);

        assertEquals("com/dotcms/repackage/jna/com/sun/jna/linux-amd64/libjnidispatch.so", packageRemapper.mapValue("com/sun/jna/linux-amd64/libjnidispatch.so"));

        rule = new Rule();
        rule.setPattern("com.sun.jna.linux-amd64.*");
        rule.setResult("com.dotcms.repackage.jna.com.sun.jna.linux-amd64.@1");
        packageRemapper = new PackageRemapper(Collections.singletonList(rule), false);

        assertEquals("com/dotcms/repackage/jna/com/sun/jna/linux-amd64/libjnidispatch.so", packageRemapper.mapValue("com/sun/jna/linux-amd64/libjnidispatch.so"));
    }

    private void assertUnchangedValue(String value) {
        assertEquals(value, remapper.mapValue(value));
    }
}
