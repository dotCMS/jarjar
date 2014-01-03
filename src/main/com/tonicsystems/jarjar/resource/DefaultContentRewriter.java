package com.tonicsystems.jarjar.resource;

import com.tonicsystems.jarjar.Rule;
import com.tonicsystems.jarjar.util.EntryStruct;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DefaultContentRewriter implements ContentRewriter {

    private List<String> allowedFiles = new ArrayList<String>();

    private final List<MatchableRule> rules = new LinkedList<MatchableRule>();

    public DefaultContentRewriter(List<Rule> ruleList) {

        for (Rule rule : ruleList) {
            rules.add(new MatchableRule(rule));
        }

        allowedFiles.add(".xml");
        allowedFiles.add(".xsd");
        allowedFiles.add(".tld");
        allowedFiles.add(".properties");
        allowedFiles.add(".conf");
        allowedFiles.add(".txt");
        allowedFiles.add(".tasks");
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

    public String replace(String content) {

        String replacement = content;
        for (MatchableRule rule : rules) {
            replacement = rule.replace(replacement);
        }

        return replacement;
    }

    @Override
    public List<MatchableRule> getRules() {
        return rules;
    }

}