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

        Boolean strict = true;
        if (this.pattern.contains(".**")) {
            strict = false;
        }

        //xxx.yyy.zzzz
        String replacement = this.replacement.replaceAll("@1", "\\$1");
        String pattern = this.pattern.replaceAll("\\.", "\\\\.");
        if (strict) {
            pattern = pattern.replaceAll("\\\\.\\*", "(\\\\.((?![\\\\w\\\\d_\\\\\\$]*\\\\.))|(?![\\\\w\\\\.]))");
            replacement = replacement.replaceAll("\\.\\$1", "\\$1");
        } else {
            pattern = pattern.replaceAll("\\.\\*\\*", "\\.([\\\\w\\\\d_\\\\\\$]*)");
        }

        pattern = "(?<![\\.-])" + pattern;
        String output = replaceAll(input, pattern, replacement);

        //xxx/yyy/zzzz
        replacement = this.replacement.replaceAll("\\.", "/").replaceAll("@1", "\\$1");
        pattern = this.pattern.replaceAll("\\.", "/");
        if (strict) {
            pattern = pattern.replaceAll("/\\*", "(/((?![\\\\w\\\\d_\\\\\\$]*/))|(?![\\\\w/]))");
            replacement = replacement.replaceAll("/\\$1", "\\$1");
            pattern = "(?<![\\./\\w-])" + pattern;
        } else {
            pattern = pattern.replaceAll("/\\*\\*", "/([\\\\w\\\\d_\\\\\\$]*)");
            pattern = "(?<=\\B/|(?<!/))" + pattern;
        }

        output = replaceAll(output, pattern, replacement);

        return output;
    }

    private String replaceAll(String input, String pattern, String replacement) {
        Matcher matcher = Pattern.compile(pattern).matcher(input);
        return matcher.replaceAll(replacement);
    }

}