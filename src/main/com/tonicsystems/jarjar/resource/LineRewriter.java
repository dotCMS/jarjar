package com.tonicsystems.jarjar.resource;

import com.tonicsystems.jarjar.util.EntryStruct;

public interface LineRewriter {

    boolean accepts(EntryStruct struct);

    String replaceLine(String line);

}