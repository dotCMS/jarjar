package com.tonicsystems.jarjar.resource;

import com.tonicsystems.jarjar.Rule;
import com.tonicsystems.jarjar.util.EntryStruct;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DefaultLineRewriter implements LineRewriter {

    private List<String> allowedFiles = new ArrayList<String>();

    private final List<MatchableRule> rules = new LinkedList<MatchableRule>();

    public DefaultLineRewriter(List<Rule> ruleList) {
        for (Rule rule : ruleList) {
            rules.add(new MatchableRule(rule));
        }

        allowedFiles.add(".xml");
        allowedFiles.add(".xsd");
        allowedFiles.add(".tld");
        allowedFiles.add(".properties");
        allowedFiles.add(".conf");
        allowedFiles.add(".txt");
    }

    public boolean accepts(EntryStruct struct) {

        return !struct.name.endsWith(".class")
                && !struct.name.endsWith(".java")
                && !struct.name.endsWith("MANIFEST.MF")
                && (allow(struct.name) || (struct.name.startsWith("META-INF/") && struct.name.contains(".")));
    }

    private Boolean allow(String name) {

        for (String type : allowedFiles) {
            if (name.endsWith(type)) {
                return true;
            }
        }

        return false;
    }

    public String replaceLine(String line) {
        for (MatchableRule rule : rules) {
            String replacement = rule.replace(line);
            if (!replacement.equals(line)) {
                return replacement;
            }
        }
        return line;
    }

    @Override
    public List<MatchableRule> getRules() {
        return rules;
    }

}