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

    /**
     * Applies to a given input a defined rule (pattern and replacement)
     *
     * @param input
     * @return
     */
    public String replace(String input) {

        Boolean strict = true;
        if (this.pattern.contains(".**")) {
            strict = false;
        }

        //xxx.yyy.zzzz
        String pattern = PackagePattern(this.pattern, strict);
        String replacement = PackageReplacement(this.replacement, strict);
        String output = replaceAll(input, pattern, replacement);

        //xxx/yyy/zzzz
        pattern = PathPattern(this.pattern, strict);
        replacement = PathReplacement(this.replacement, strict);
        output = replaceAll(output, pattern, replacement);

        return output;
    }

    /**
     * Returns the regex for a given pattern, this patter will handle packages like formats xxx.yyy.zzz
     *
     * @param initialPattern
     * @param strict
     * @return
     */
    public static String PackagePattern(String initialPattern, Boolean strict) {

        //xxx.yyy.zzzz
        String pattern = initialPattern.replaceAll("\\.", "\\\\.");
        if (strict) {
            pattern = pattern.replaceAll("\\\\.\\*", "(\\\\.((?![\\\\w\\\\d_\\\\\\$]*\\\\.))|(?![\\\\w\\\\.]))");
        } else {
            pattern = pattern.replaceAll("\\.\\*\\*", "\\.([\\\\w\\\\d_\\\\\\$]*)");
        }

        return "(?<![\\.-])" + pattern;
    }

    /**
     * Returns the replacement after some clean up ready to be use for packages like formats xxx.yyy.zzz
     *
     * @param initialReplacement
     * @param strict
     * @return
     */
    public static String PackageReplacement(String initialReplacement, Boolean strict) {

        String replacement = initialReplacement.replaceAll("@1", "\\$1");
        if (strict) {
            replacement = replacement.replaceAll("\\.\\$1", "\\$1");
        }

        return replacement;
    }

    /**
     * Returns the regex for a given pattern, this patter will handle packages like paths xxx/yyy/zzz
     *
     * @param initialPattern
     * @param strict
     * @return
     */
    public static String PathPattern(String initialPattern, Boolean strict) {

        //xxx/yyy/zzzz
        String pattern = initialPattern.replaceAll("\\.", "/");
        if (strict) {
            pattern = pattern.replaceAll("/\\*", "(/((?![\\\\w\\\\d_\\\\\\$]*/))|(?![\\\\w/]))");
            pattern = "(?<![\\./\\w-])" + pattern;
        } else {
            pattern = pattern.replaceAll("/\\*\\*", "/([\\\\w\\\\d_\\\\\\$]*)");
            pattern = "(?<=\\B/|(?<!/))" + pattern;
        }

        return pattern;
    }

    /**
     * Returns the replacement after some clean up ready to be use for packages like paths xxx/yyy/zzz
     *
     * @param initialReplacement
     * @param strict
     * @return
     */
    public static String PathReplacement(String initialReplacement, Boolean strict) {

        String replacement = initialReplacement.replaceAll("\\.", "/").replaceAll("@1", "\\$1");
        if (strict) {
            replacement = replacement.replaceAll("/\\$1", "\\$1");
        }

        return replacement;
    }

    private String replaceAll(String input, String pattern, String replacement) {
        Matcher matcher = Pattern.compile(pattern).matcher(input);
        return matcher.replaceAll(replacement);
    }

}