package org.slevental.anaphora.core.opennlp;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;

import java.io.IOException;
import java.io.InputStream;

/**
 * Wrapper around OpenNLP parser
 */
public class OpenNLPParser {
    private final Parser parser;

    public OpenNLPParser() {
        try {
            InputStream modelIn = getClass().getResourceAsStream("/models/en-parser-chunking.bin");
            ParserModel model = new ParserModel(modelIn);
            parser = ParserFactory.create(model);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot create annotator due to error: " + e.getMessage(), e);
        }
    }

    public Parse[] parse(String sentence, int numParses){
        return ParserTool.parseLine(sentence, parser, numParses);
    }
}
