package org.alblang.utils;

import org.alblang.models.Node;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by jenaiz on 22/09/14.
 */
public class NodeUtils {

    public static String toJson(final Node node) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final StringWriter sw = new StringWriter();
        final PrintWriter p = new PrintWriter(sw);

        mapper.writeValue(p, node);

        return sw.toString();
    }

    public static Node toJava(final String json) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        return  mapper.readValue(json, Node.class);
    }
}
