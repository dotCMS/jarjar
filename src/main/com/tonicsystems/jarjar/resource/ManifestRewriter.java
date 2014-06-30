package com.tonicsystems.jarjar.resource;

import com.tonicsystems.jarjar.Rule;
import com.tonicsystems.jarjar.util.EntryStruct;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * @author Jonathan Gamba
 *         Date: 10/06/14
 */
public class ManifestRewriter implements ContentRewriter {

    private final List<MatchableRule> rules = new LinkedList<MatchableRule>();

    public ManifestRewriter ( List<Rule> ruleList ) {

        for ( Rule rule : ruleList ) {
            rules.add( new MatchableRule( rule ) );
        }
    }

    @Override
    public boolean accepts ( EntryStruct struct ) {
        return struct.name.endsWith( "MANIFEST.MF" );
    }

    @Override
    public String replace ( String content ) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            //Build a manifest object using the read content
            Manifest manifest = new Manifest( new ByteArrayInputStream( content.getBytes() ) );

            //Apply the rules to each attribute on the manifest
            Attributes attributes = manifest.getMainAttributes();
            for ( Object key : attributes.keySet() ) {

                Attributes.Name foundKey = (Attributes.Name) key;
                String originalValue = manifest.getMainAttributes().getValue( foundKey );

                //Replace the content applying the defined rules
                String replacement = originalValue;
                for ( MatchableRule rule : rules ) {
                    replacement = rule.replace( replacement );
                }
                //Set the content with the applied rules
                attributes.putValue( foundKey.toString(), replacement );
            }

            manifest.write( baos );

        } catch ( IOException e ) {
            System.err.println( "Error rewriting Manifest file." );
            e.printStackTrace();
            return content;
        }

        String newContent;
        try {
            newContent = baos.toString( "UTF8" );
        } catch ( UnsupportedEncodingException e ) {
            newContent = baos.toString();
        }
        return newContent;
    }

    @Override
    public List<MatchableRule> getRules () {
        return rules;
    }

}