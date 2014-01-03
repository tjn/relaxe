/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 */
package com.appspot.relaxe.cli;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Parser {
    
    private TreeMap<String, Option> optionNameMap;    
    private LinkedHashMap<String, Parameter> parameterNameMap;
    
    private static Logger logger = LoggerFactory.getLogger(Parser.class);
    
    public Parser() {        
    }
    
    public Option addOption(String name, String flag) {
        return addOption(name, flag, null);
    }
    
    public Option addOption(String name, String flag, boolean optionalArg) {
        return addOption(name, flag, new Parameter(optionalArg));
    }
    
    public Option addOption(String name, String flag, int min, int max) {
        return addOption(new SimpleOption(name, flag, new Parameter(null, min, max)));        
    }
    
    public Option addOption(String name, String flag, Parameter arg) {
        return addOption(new SimpleOption(name, flag, arg));        
    }
        
    public Parameter addParameter(String name, boolean optional) {
        return addParameter(new Parameter(name, optional));
    }
    
    public List<Parameter> parameters() {
        return new ArrayList<Parameter>(getParameterNameMap().values());
    }   
    
    public boolean containsParameter(String n) {
        return getParameterNameMap().containsKey(n);
    }
    
    public Parameter getParameter(String n) {
        Parameter a = getParameterNameMap().get(n);
        return a;
    }
    
    public Parameter addParameter(Parameter param) {
        if (param == null) {
            throw new NullPointerException("'arg' must not be null");
        }
        
        final String n = param.getName();
        
        if (n == null) {
            throw new IllegalArgumentException("'arg.getName()' must not be null");
        }
        
        Map<String, Parameter> m = getParameterNameMap();
        
        if (m.containsKey(n)) {
            throw new IllegalStateException("parameter " + n + " is already added");
        }                                
        
        getParameterNameMap().put(n, param);
        return param;
    }
    
    public boolean contains(Option opt) {
        if (opt == null) {
            throw new NullPointerException("'opt' must not be null");
        }
        
        String n = opt.name();
        String f = opt.flag();
        
        TreeMap<String, Option> nm = getOptionNameMap();
    
        return 
            (n != null && nm.containsKey(n)) || 
            (f != null && nm.containsKey(f));
    }
    
    public Collection<Option> options() {
        return Collections.unmodifiableCollection(getOptionNameMap().values());
    }
        
    public Option addOption(Option opt)
        throws IllegalStateException, NullPointerException {
        
        if (opt == null) {
            throw new NullPointerException("'opt' must not be null");
        }
        
        TreeMap<String, Option> m = getOptionNameMap();
        
        Option p = null;
        
        final String n = opt.name();        
        p = (n == null) ? null : m.get(n);
                        
        if (p != null) {
            throw new IllegalStateException(
                    "option with name '" + n + "' is already added");
        }
        
        final String f = opt.flag();        
        p = (f == null) ? null : m.get(f);
        
        if (p != null) {
            throw new IllegalStateException(
                    "option with flag '" + f + "' is already added");
        }        
        
        if (n != null) {
            m.put(n, opt);
        }
        
        if (f != null) {
            m.put(f, opt);
        }
        
        return opt;
    }       
    
    public List<Token> tokenize(String[] aa, Collection<String> unknownOptions) {
        List<Token> tokens = new ArrayList<Token>(aa.length);
                
        boolean opts = true;
        
        for (int i = 0; i < aa.length; i++) {
            String a = aa[i];
            
            if (!opts) {
                tokens.add(Token.arg(a));
            }
            else {
                if (a.equals("--")) {
                    opts = false;
                    continue;
                }
                
                // single dash is interpreted as an argument
                if (a.equals("-")) {
                    tokens.add(Token.arg(a));
                    continue;
                }
                
                if (a.startsWith("-")) {
                    normalizeOptions(a, tokens, unknownOptions);
                }
                else {
                    tokens.add(Token.arg(a));
                }
            }                        
        }
        
        return tokens;        
    }
    
    protected void normalizeOptions(String a, List<Token> tokens, Collection<String> unknownOptions) {
                
        if (a.startsWith("--")) {
            Pattern p = Pattern.compile("--(.+)(=(.*))?");
            
            logger().debug("a (" + a + ")");            
            Matcher m = p.matcher(a);
            
            logger().debug("match ? " + m.matches());
            
            if (m.matches()) {                        
                String name = m.group(1);
                String assignment = m.group(2);
                String value = m.group(3);
                
                logger().debug("name: " + name);
                logger().debug("assignment: " + assignment);
                
                Option o = getOption(name);
                
                if (o == null) {
                    unknownOptions.add("--" + name);                 
                }
                else {
                    tokens.add(Token.opt(name));
                    
                    if (assignment != null) {
                        tokens.add(Token.arg(value == null ? "" : value));
                    }
                }
            }
        }
        else {
            // starts with single '-'
            // short option/options
            final String flags = a.substring(1);                        
            String flag = flags.substring(0, 1);
            
            final String remaining = flags.length() > 1 ? flags.substring(1) : "";
            
//            logger().debug("remaining: " + remaining);
            
            Option o = getOption(flag);
            
            if (o == null) {
                unknownOptions.add("-" + flag);
            }
            else {                
                tokens.add(Token.opt(flag));
                
                if (argumentRequired(o)) {
                    if (remaining.length() > 0) {
                        tokens.add(Token.arg(remaining));
                    }
                    
                    return;
                }
            }    
            
            if (remaining.length() > 0) {
                // remaining flags in combined flag
                                        
                for (int i = 0; i < remaining.length(); i++) {
                    flag = remaining.substring(i, i + 1);
                    
                    // logger().debug("remaining flag: " + flag);
                    
                    o = getOption(flag);
                    
                    if (o == null) {
                        unknownOptions.add("-" + flag);
                    }
                    else {
                        tokens.add(Token.opt(flag));
                    }
                }
            }
        }        
    }    
    
    private boolean argumentRequired(Option o) {
        return (o.getParameter() == null) ? false : (!o.getParameter().isOptional());
    }
    
    public CommandLine parse(String[] aa) {
        List<String> unknownOpts = new ArrayList<String>();        
        // List<String> unknownArgs = new ArrayList<String>();
        List<Token> tokens = tokenize(aa, unknownOpts);
        
        logger().debug("parse: " + tokens);
        logger().debug("unknownOpts: " + unknownOpts);
        
        int tc = tokens.size();
        
        final Map<Option, List<String>> options = new HashMap<Option, List<String>>();
        final Map<Parameter, List<String>> parameterMap = new HashMap<Parameter, List<String>>();
        
        List<String> args = new ArrayList<String>();
        // List<String> optArgs = null;
        
        Option current = null;
                
        for (int i = 0; i < tc; i++) {
            Token t = tokens.get(i);
                        
            if (t.getType() == Token.Type.OPTION) {
                current = getOption(t.getToken());
                options.put(current, null);
            }
            else {
                boolean optAccepts = acceptsNextParameter(current, options);
                logger().debug("current ? " + current);
                logger().debug("optAccepts ? " + optAccepts);
                
                List<String> dest = 
                    acceptsNextParameter(current, options) ? 
                    getOptionParameters(current, options) : args;
                 
                dest.add(t.getToken());
            }
        }
        
        // TODO: collect the options without sufficient arguments.
        
        logger().debug("args: " + args);                
        parseArgumentList(args, parameterMap);                
        return createCommandLine(options, parameterMap, unknownOpts);
    }
    
    /**
     * Return true if and only if the option <code>o</code> currently being processed 
     * accepts more arguments.
     *   
     * @param o The option currently being processed.
     * @param am Option arguments processed so far.
     * @return
     */
    
    private boolean acceptsNextParameter(Option o, Map<Option, List<String>> am) {        
        Parameter arg = (o == null) ? null : o.getParameter();
        
        if (arg == null) {
            return false;
        }       
        
        Integer max = arg.getMaxCount();
        return (max == null) || (getOptionParameters(o, am).size() < max.intValue());
    }

    private List<String> getOptionParameters(Option o, Map<Option, List<String>> pm) {
        Parameter p = o.getParameter();
        
        if (p == null) {
            return null;
        }
        
        List<String> params = pm.get(o);
        
        if (params == null) {
            params = new ArrayList<String>();
            pm.put(o, params);
        }
        
        return params;                
    }

    private CommandLine createCommandLine(
            final Map<Option, List<String>> options,
            final Map<Parameter, List<String>> argumentMap, 
            final List<String> unknownOptions) {
        return new CommandLine() {
            @Override
            public String value(Parameter a, String delim) {
                List<String> args = argumentMap.get(a);
                return join(args, delim);
            }
            
            @Override
            public String toString() {             
                return 
                    "options: " + options().toString() + "; " + 
                    "arguments: " + present().toString() + ";";
            }

            @Override
            public String value(Parameter a) {
                return value(a, " ");
            }

            @Override
            public List<String> values(Parameter a) {
                List<String> list = argumentMap.get(a);
                
                if (list == null) {
                    return null;
                }
                                               
                return Collections.unmodifiableList(list);
            }
            
            private List<Parameter> present() {                
                List<Parameter> present = new ArrayList<Parameter>();
                
                for (Parameter a : getParameterNameMap().values()) {                    
                    if (argumentMap.containsKey(a.getName())) {
                        present.add(a);
                    }
                }
                                
                return present;
            }

            @Override
            public Set<Option> options() {            
                return Collections.unmodifiableSet(options.keySet());
            }

            @Override
            public String value(Option o, String delim) {
                List<String> v = options.get(o);
                return join(v, delim);
            }

            @Override
            public String value(Option o) {
                return value(o, null);
            }

            @Override
            public List<String> values(Option o) {                
                if (!options.containsKey(o)) {
                    return null;
                }
                
                List<String> list = options.get(o);
                
                return (list == null) ?
                    list = Collections.emptyList() :
                    Collections.unmodifiableList(list);
            }

            @Override
            public List<String> values() {
                ArrayList<String> values = new ArrayList<String>();
                
                for (Parameter a : getParameterNameMap().values()) {
                    List<String> v = values(a);
                    
                    
                    if (v != null && (!v.isEmpty())) {
                        values.addAll(v);
                    }
                }                

                return Collections.unmodifiableList(values);
            }

            @Override
            public Set<Parameter> parameters() {
                return Collections.unmodifiableSet(argumentMap.keySet());
            }

            @Override
            public boolean isEmpty() {
                return argumentMap.isEmpty() && options.isEmpty();
            }

            @Override
            public List<String> unknownOptions() {
                return Collections.unmodifiableList(unknownOptions);
            }

            @Override
            public boolean needsHelp(Option help) {
                return 
                    isEmpty() || (!unknownOptions().isEmpty()) ||
                    (help != null && this.options().contains(help));
            }
            
            @Override
            public boolean has(Option o) {
                return options().contains(o);
            }            
        };
    }

    private void parseArgumentList(List<String> args, final Map<Parameter, List<String>> argumentMap) {        
        if (getParameterNameMap().isEmpty()) {
            if (!args.isEmpty()) {
                throw new RuntimeException("parse failed, extra arguments");
            }
            
            return;
        }
        
        StringBuffer b = new StringBuffer();
                
        /**
         * Values must be iterated in insert-order, and
         * LinkedHashMap guarantees that:
         */        
        for (Parameter a : getParameterNameMap().values()) {
            b.append("(.");
            b.append("{");
            b.append(a.getMinCount());
            b.append(",");
            Integer max = a.getMaxCount();
            
            if (max != null) {
                b.append(max.toString());
            }
            
            b.append("})");            
        }
        
        Pattern p = Pattern.compile(b.toString());
        char[] buf = new char[args.size()];
        Arrays.fill(buf, 'a');
        b.setLength(0);        
        // b.setLength(args.size());
        b.append(buf);
        
        logger().debug("input-size: " + args.size());
        logger().debug("pattern: " + p.pattern());
        logger().debug("input: " + b);
        Matcher m = p.matcher(b);
                       
        int offset = 0;
        
        if (m.matches()) {
            logger().debug("matches: " + b);
            
            int gi = 1;
        
            for (Parameter a : getParameterNameMap().values()) {
                int size  = groupSize(m.group(gi));
                
                if (size > 0) {
                    List<String> portion = args.subList(offset, offset + size);
                    argumentMap.put(a, portion);
                    offset += size;
                }                
                                                                
                gi++;
            }            
        }
        else {
            logger().debug("no match, parse failed");
            
            // todo: 
//            throw new RuntimeException("parse failed, no match"); 
        }
    }    
    
    
    private int groupSize(String group) {
        return (group == null) ? 0 : group.length();
    }

    public String usage(Class<?> command) {
        StringWriter sw = new StringWriter();
                 
        PrintWriter w = new PrintWriter(sw);
        
        StringBuilder buf = new StringBuilder();
        buf.append("usage: java ");        
        buf.append(command.getName());
        
        if (!getOptionNameMap().isEmpty()) { 
            buf.append(" [options]");
        }
        
        for (Parameter p : getParameterNameMap().values()) {
            buf.append(" ");            
            appendName(buf, p.getName(), p.isOptional());
        }
        
        w.println(buf.toString());
        
        if (!getOptionNameMap().isEmpty()) {
            w.println("Options");
        }
        
        HashMap<String, Option> visited = new HashMap<String, Option>(); 
        
        for (Option o : this.getOptionNameMap().values()) {
            String n = o.name();
            String f = o.flag();
            
            String key = (n != null) ? n : f;
            Option vo = visited.get(key);
            
            if (vo == null) {
                visited.put(key, o);                
            }
            else {
                continue;
            }
                        
            int cw = 80;
            int wc1 = 32; 
            int wc2 = cw - wc1;

            String ot = formatOption(o);
            String d = o.getDescription();
            
            List<String> lines = columns(ot, wc1, d, wc2);
                        
            w.write(join(lines, "\n"));            
        }
        
        w.close();
                
        return sw.getBuffer().toString();
    }    
    
    public List<String> columns(String c1, int w1, String c2, int w2) {
        List<String> a = lines(c1 == null ? "" : c1, w1);
        List<String> b = lines(c2 == null ? "" : c2, w2);
        
        int az = a.size();
        int bz = b.size();
                
        int lc = Math.max(a.size(), b.size());
                
//        System.err.println("az: " + az);
//        System.err.println("c1: " + c1);
//        System.err.println("w1: " + w1);
//        System.err.println("c2: " + c2);
//        System.err.println("w2: " + w2);
//        
//        System.err.println("lines: " + lc);
                
        ArrayList<String> dest = new ArrayList<String>(lc);
                
        StringBuffer buf = new StringBuffer();
        
        for (int i = 0; i < lc; i++) {
            String at = element(a, i, az); 
            String bt = element(b, i, bz);
            
            buf.setLength(0);
            buf.append(pad(at, w1));
            buf.append(pad(bt, w2));
            
//            System.err.println("line:\n" + buf.toString() + "\n");
            
            dest.add(buf.toString());
        }
        
        return dest;
    }
    
    private String pad(final String t, int width) {
        String out = t;
        
        int tl = t.length();
        
        if (tl > width) {
            out = t.substring(0, width);
        }
        else if (tl < width) {        
            int pl = width - tl;        
            StringBuffer b = new StringBuffer(t);
                    
            for (int i = 0; i < pl; i++) {
                b.append(" ");    
            }
            
            out = b.toString();
        }
        
        // System.err.println("padded:\n(" + t + ") =>\n(" + out + ") " + width);
        
        return out;
    }
    
//    private String cut(String t, int width) {
//        return t.length() > width ? t.substring(0, width) : t;        
//    }

    private String element(List<String> arr, int index, int size) {
        return (index < size) ? arr.get(index) : "";
    }

    private String formatOption(Option o) {
        StringBuilder ob = new StringBuilder();
                    
        if (o.flag() != null) {
            ob.append("-");
            ob.append(o.flag());
        }            
        
        if (o.name() != null) {
            if (o.flag() != null) {
                ob.append(", ");
            }
            
            ob.append("--");
            ob.append(o.name());
        }
        
        Parameter p = o.getParameter();
        
        if (p != null && p.getName() != null) {
        	ob.append(" ");
        	appendName(ob, p.getName(), p.isOptional());
        }
        
        
        return ob.toString();
    }
                
    
    private void appendName(StringBuilder ob, String name, boolean optional) {
    	ob.append(optional ? '[' : '<');
    	ob.append(name);
    	ob.append(optional ? ']' : '>');
	}

	public Option getOption(String k) {
        return getOptionNameMap().get(k);
    }

    private TreeMap<String, Option> getOptionNameMap() {
        if (optionNameMap == null) {
            optionNameMap = new TreeMap<String, Option>();            
        }

        return optionNameMap;
    }

    private Map<String, Parameter> getParameterNameMap() {
        if (parameterNameMap == null) {
            parameterNameMap = new LinkedHashMap<String, Parameter>();           
        }

        return parameterNameMap;
    }
    
    protected String join(List<String> elems, String delim) {
        if (elems == null) {
            return null;
        }
        
        if (delim == null) {
            delim = " ";
        }
        
        int size = elems.size();
        
        if (size == 0) {
            return "";
        }
        
        if (size == 1) {
            return elems.get(0);
        }
        
        StringBuilder sb = new StringBuilder();
        
        for (Iterator<String> iter = elems.iterator(); iter.hasNext();) {
            String e = iter.next();                    
            sb.append(e);
            
            if (iter.hasNext()) {
                sb.append(delim);
            }
        }
        
        return sb.toString();
    }
    
    /***
     * Splits the text into lines whose length does not exceed <code>maxLineWidth</code> characters.
     *  
     * @param input
     * @param maxLineWidth
     * @return
     */
    
    public List<String> lines(String input, int maxLineWidth) {
        List<String> lines = new ArrayList<String>();
        
        if (input == null) {
            input = "";
        }
        
        if (input.equals("")) {
            lines.add(input);
            return lines;
        }                
        
        StringTokenizer words = new StringTokenizer(input, " :,.\n\r\t", true);        
        StringBuffer line = new StringBuffer();
                
        while(words.hasMoreTokens()) {
            String t = words.nextToken();
            
            if (t.equals("\t")) {
                // TODO: tab processing
                t = " ";
            }
            
            if (t.equals("\n")) {
                flush(line, lines, true);                
                continue;
            }
            
            int cw = line.length();
            int tl = t.length();
            int w = tl + cw;
            
            if (w <= maxLineWidth) {
                line.append(t);             
            }
            else {                
                if (cw > 0) { 
                    // flush  
                    // throw whitespace away from the end and the beginning of the line                    
                    flush(line, lines, false);
                }
                
                if (tl <= maxLineWidth) {                     
                    line.append(t);
                }
                else {                    
                    // cut
                    while(t.length() > maxLineWidth) {
                        // line-buffer is expected to be empty,
                        // and can thus be by-passed:
                        lines.add(t.substring(0, maxLineWidth));
                        t = t.substring(maxLineWidth);
                    }
                
                    if (t.length() > 0) { // add tail
                        line.append(t);
                    }                                        
                }                
            }
        }        
        
        flush(line, lines, false);
        
        return lines;
    }
        
    
    private void flush(StringBuffer lineBuffer, List<String> lines, boolean force) {
        String part = lineBuffer.toString().trim();
        
        if (force || (part.length() > 0)) {
            lines.add(part);    
        }
        
        lineBuffer.setLength(0);
    }

    public static Logger logger() {
        return Parser.logger;
    }
    
}
