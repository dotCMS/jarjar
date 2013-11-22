package com.tonicsystems.jarjar;

import com.tonicsystems.jarjar.resource.MatchableRule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MatchableRuleTest {

    private MatchableRule rule;

    @Test
    public void knowsWhenMatches() {

        rule = new MatchableRule("org.**", "jarjar.org.@1");

        matches("org.xxx.yyy", "jarjar.org.xxx.yyy");
        matches("org.foo", "jarjar.org.foo");

        matches("zzz org.foo baz", "zzz jarjar.org.foo baz");
        matches("<org>org.foo.baz</org>", "<org>jarjar.org.foo.baz</org>");
        matches("<org>org.foo.baz", "<org>jarjar.org.foo.baz");
        matches("xxx=org.foo.baz", "xxx=jarjar.org.foo.baz");
        matches("xxx:org.foo.baz", "xxx:jarjar.org.foo.baz");
        matches("xxx:org.foo.baz,ggggg", "xxx:jarjar.org.foo.baz,ggggg");
        matches(" org.foo.baz;", " jarjar.org.foo.baz;");
        matches("org.foo<xxx>", "jarjar.org.foo<xxx>");
        matches("org.foo!", "jarjar.org.foo!");

        matches("org", "org");
        matches("org.", "jarjar.org.");
        //matches("xorg.foo", "xjarjar.org.foo");

        matches("org.foo2,org.bar1", "jarjar.org.foo2,jarjar.org.bar1");
        matches("org.foo$1,org.bar_x hey", "jarjar.org.foo$1,jarjar.org.bar_x hey");

        matches("META-INF/services/org.test", "META-INF/services/jarjar.org.test");
        matches("org/test/spring/spring-dwr-3.0.xsd", "jarjar/org/test/spring/spring-dwr-3.0.xsd");
        matches("/org/test/spring/spring-dwr-3.0.xsd", "/jarjar/org/test/spring/spring-dwr-3.0.xsd");
        matches("ffff=org/test/spring/spring-dwr-3.0.xsd", "ffff=jarjar/org/test/spring/spring-dwr-3.0.xsd");


        matches("<dd>ddddd</dd><zz>zzzz</zz><validator-class>org.xxx.yyy.JstlCoreTLV</validator-class><yy>yyyy</yy>",
                "<dd>ddddd</dd><zz>zzzz</zz><validator-class>jarjar.org.xxx.yyy.JstlCoreTLV</validator-class><yy>yyyy</yy>");
        String output = rule.replace("<dd>ddddd</dd><zz>zzzz</zz><validator-class>org.xxx.yyy.JstlCoreTLV</validator-class><yy>yyyy</yy>");

        rule = new MatchableRule("org.directwebremoting.**", "com.dotcms.repackage.dwr_3rc2modified.org.directwebremoting.@1");
        matches("http://www.directwebremoting.org/schema/spring-dwr/spring-dwr-3.0.xsd=org/directwebremoting/spring/spring-dwr-3.0.xsd",
                "http://www.directwebremoting.org/schema/spring-dwr/spring-dwr-3.0.xsd=com/dotcms/repackage/dwr_3rc2modified/org/directwebremoting/spring/spring-dwr-3.0.xsd");
        matches("http://www.directwebremoting.org/schema/spring-dwr/spring-dwr-3.0.xsd=org.directwebremoting.spring.spring-dwr-3.0.xsd",
                "http://www.directwebremoting.org/schema/spring-dwr/spring-dwr-3.0.xsd=com.dotcms.repackage.dwr_3rc2modified.org.directwebremoting.spring.spring-dwr-3.0.xsd");

        rule = new MatchableRule("org.directwebremoting.spring.**", "com.dotcms.repackage.dwr_3rc2modified.org.directwebremoting.spring.@1");
        matches("http://www.directwebremoting.org.schema.spring-dwr.spring-dwr-3.0.xsd=com.dotcms.repackage.dwr_3rc2modified.org.directwebremoting.spring.spring-dwr-3.0.xsd",
                "http://www.directwebremoting.org.schema.spring-dwr.spring-dwr-3.0.xsd=com.dotcms.repackage.dwr_3rc2modified.org.directwebremoting.spring.spring-dwr-3.0.xsd");
        matches("http://www.directwebremoting.org/schema/spring-dwr/spring-dwr-3.0.xsd=com/dotcms/repackage/dwr_3rc2modified/org/directwebremoting/spring/spring-dwr-3.0.xsd",
                "http://www.directwebremoting.org/schema/spring-dwr/spring-dwr-3.0.xsd=com/dotcms/repackage/dwr_3rc2modified/org/directwebremoting/spring/spring-dwr-3.0.xsd");
    }

    private void matches(String input, String match) {
        assertEquals(match, rule.replace(input));
    }

}