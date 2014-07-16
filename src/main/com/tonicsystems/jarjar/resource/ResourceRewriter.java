package com.tonicsystems.jarjar.resource;

import com.tonicsystems.jarjar.util.EntryStruct;
import com.tonicsystems.jarjar.util.JarProcessor;

import java.io.IOException;

public class ResourceRewriter implements JarProcessor {

    private final ContentRewriter input;
    private final boolean verbose;
    private final boolean renameServices;

    public ResourceRewriter(ContentRewriter input, boolean verbose, boolean renameServices) {
        this.input = input;
        this.verbose = verbose;
        this.renameServices = renameServices;
    }

    public ResourceRewriter ( ContentRewriter input, boolean verbose ) {
        this.input = input;
        this.verbose = verbose;
        this.renameServices = false;
    }

    public boolean process(EntryStruct struct) throws IOException {

        if (input.accepts(struct)) {

            //We must rename as well the services files and not just modify it contents
            if (renameServices && struct.name.contains("META-INF/services/")) {
                struct.name = input.replace(struct.name);
            }

            //Reading the file to check
            String fileContent = input.replace(new String(struct.data));
            struct.data = fileContent.getBytes();
        }
        return true;
    }
}