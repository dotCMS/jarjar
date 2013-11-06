package com.tonicsystems.jarjar.resource;

import com.tonicsystems.jarjar.util.EntryStruct;
import com.tonicsystems.jarjar.util.JarProcessor;

import java.io.*;

public class ResourceRewriter implements JarProcessor {

    private final LineRewriter input;
    private final boolean verbose;
    private final boolean renameServices;

    public ResourceRewriter(LineRewriter input, boolean verbose, boolean renameServices) {
        this.input = input;
        this.verbose = verbose;
        this.renameServices = renameServices;
    }

    public boolean process(EntryStruct struct) throws IOException {

        if (input.accepts(struct)) {

            //We must rename as well the services files and not just modify it contents
            if (renameServices && struct.name.contains("META-INF/services/")) {
                struct.name = input.replaceLine(struct.name);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(struct.data)));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream printer = new PrintStream(out);
            String line;
            while ((line = reader.readLine()) != null) {
                String replacement = input.replaceLine(line);
                if (!replacement.equals(line) && verbose) {
                    System.out.println("Updating file: " + struct.name + ". Replacement: " + replacement);
                }
                printer.println(replacement);
            }
            reader.close();
            printer.close();
            struct.data = out.toByteArray();
        }
        return true;
    }
}