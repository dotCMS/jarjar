package com.tonicsystems.jarjar.resource;

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

        matches("org.foo<xxx>", "jarjar.org.foo<xxx>");
        matches("org.foo!", "jarjar.org.foo!");

        matches("org", "org");
        matches("org.", "jarjar.org.");
        matches("xorg.foo", "xjarjar.org.foo");

        matches("org.foo2,org.bar1", "jarjar.org.foo2,jarjar.org.bar1");
        matches("org.foo$1,org.bar_x hey", "jarjar.org.foo$1,jarjar.org.bar_x hey");
    }

    private void matches(String input, String match) {
        assertEquals(match, rule.replace(input));
    }

}