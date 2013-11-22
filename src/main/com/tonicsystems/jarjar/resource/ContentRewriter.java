package com.tonicsystems.jarjar.resource;

import com.tonicsystems.jarjar.util.EntryStruct;

import java.util.List;

public interface ContentRewriter {

    boolean accepts(EntryStruct struct);

    String replace(String line);

    public List<MatchableRule> getRules();

}