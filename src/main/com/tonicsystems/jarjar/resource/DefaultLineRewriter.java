package com.tonicsystems.jarjar.resource;

import com.tonicsystems.jarjar.Rule;
import com.tonicsystems.jarjar.util.EntryStruct;

import java.util.LinkedList;
import java.util.List;

public class DefaultLineRewriter implements LineRewriter {

    private final List<MatchableRule> rules = new LinkedList<MatchableRule>();

    public DefaultLineRewriter(List<Rule> ruleList) {
        for (Rule rule : ruleList) {
            rules.add(new MatchableRule(rule));
        }
    }

    public boolean accepts(EntryStruct struct) {
        //TODO: No sure about this, but for now only accept files with extensions
        return !struct.name.endsWith(".class") && !struct.name.endsWith(".java") && !struct.name.endsWith("MANIFEST.MF") && struct.name.contains(".");
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