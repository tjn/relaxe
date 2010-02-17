/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.EnumSet;

public class Pipe
	implements Runnable {
	
	private InputStream in;
	private OutputStream out;
	
	private EnumSet<Endpoint> close;
	
	public enum Endpoint {
		IN,
		OUT;
		
		public static EnumSet<Endpoint> both() {
			return EnumSet.allOf(Endpoint.class);
		}
		
		public static EnumSet<Endpoint> none() {
			return EnumSet.noneOf(Endpoint.class);
		}
	}
	
	public Pipe(InputStream in, OutputStream out) {
		this(in, out, Endpoint.none());
	}	
	
	public Pipe(InputStream in, OutputStream out, Endpoint close) {
		this(in, out, EnumSet.of(close));
	}

	
	public Pipe(InputStream in, OutputStream out, EnumSet<Endpoint> toClose) {
		super();
		
		if (in == null) {
			throw new NullPointerException("'in' must not be null");
		}
			
		if (out == null) {
			throw new NullPointerException("'out' must not be null");
		}		
		
		this.in = in;
		this.out = out;
		close = (close == null) ? Endpoint.none() : EnumSet.copyOf(toClose);
	}

	@Override
	public void run() {		
		int b;
								
		try {
			try {
				while((b = in.read()) != -1) {
					out.write((byte) b);
				}
			}
			finally {
				if (this.close.contains(Endpoint.IN)) {
					in = doClose(in);
				}
				
				if (this.close.contains(Endpoint.OUT)) {
					out = doClose(out);	
				}					
			}
		}
		catch (IOException e) {
			onError(e);				
		}
	}
	
	private InputStream doClose(InputStream in) {
		if (in != null) {
			try {
				in.close();
			} 
			catch (IOException e) {
				onError(e);
			}
		}
		
		return in;
	}
	
	private OutputStream doClose(OutputStream out) {
		if (out != null) {
			try {
				out.close();
			} 
			catch (IOException e) {	
				onError(e);
			}
		}
		
		return out;
	}
					
	public void onError(IOException e) {		
	}
}