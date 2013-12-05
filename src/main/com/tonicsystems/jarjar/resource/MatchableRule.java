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

        //xxx.yyy.zzzz
        String pattern = this.pattern.replaceAll("\\.", "\\\\.");
        pattern = pattern.replaceAll("\\.\\*\\*", "\\.([\\\\w\\\\d_\\\\\\$]*)");
        pattern = pattern.replaceAll("\\.\\*", "\\.([\\\\w\\\\d_\\\\\\$]*)");
        String replacement = this.replacement.replaceAll("@1", "\\$1");

        pattern = "(?<!\\.)" + pattern;
        String output = replaceAll(input, pattern, replacement);

        //xxx/yyy/zzzz
        pattern = this.pattern.replaceAll("\\.", "/");
        pattern = pattern.replaceAll("/\\*\\*", "/([\\\\w\\\\d_\\\\\\$]*)");
        pattern = pattern.replaceAll("/\\*", "/([\\\\w\\\\d_\\\\\\$]*)");
        replacement = this.replacement.replaceAll("\\.", "/").replaceAll("@1", "\\$1");

        pattern = "(?<=\\B/|(?<!/))" + pattern;
        output = replaceAll(output, pattern, replacement);

        return output;
    }

    private String replaceAll(String input, String pattern, String replacement) {
        Matcher matcher = Pattern.compile(pattern).matcher(input);
        return matcher.replaceAll(replacement);
    }

}