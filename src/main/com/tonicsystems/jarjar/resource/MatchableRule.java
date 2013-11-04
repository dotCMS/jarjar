package com.tonicsystems.jarjar.resource;

import com.tonicsystems.jarjar.Rule;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchableRule {

    private final String pattern;
    private final String replacement;

    public MatchableRule(Rule rule) {
        this(rule.getPattern(), rule.getResult());
    }

    public MatchableRule(String pattern, String replacement) {
        this.pattern = pattern;
        this.replacement = replacement;
    }

    public String replace(String input) {
        String p = pattern.replaceAll("\\.", "\\\\.").replaceAll("\\*\\*", "([\\\\w\\\\d_\\\\\\$]*)");
        String replacement = this.replacement.replaceAll("@1", "\\$1");
        Matcher m = Pattern.compile(p).matcher(input);
        return m.replaceAll(replacement);
    }

}