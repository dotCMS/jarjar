package com.tonicsystems.jarjar.resource;

import com.tonicsystems.jarjar.util.EntryStruct;

import java.util.List;

public interface LineRewriter {

    boolean accepts(EntryStruct struct);

    String replaceLine(String line);

    public List<MatchableRule> getRules();

}