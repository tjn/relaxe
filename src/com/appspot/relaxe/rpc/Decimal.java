/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.rpc;

import java.io.Serializable;
import java.util.Map;

public class Decimal
	implements Comparable<Decimal>, Serializable {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 4742789858945266685L;
	private long unscaled;
    private int scale;
        
//    private static Logger logger = Logger.getLogger(Decimal.class.getName());    
    private static final Decimal[][] values;
    
    enum Result {
    	QUOTIENT,
    	REMAINDER
    }    
            
    static { 
    	// caches for scales 0, 1, 2:
    	values = new Decimal[3][];
    	
    	for (int s = 0; s < values.length; s++) {
    		Decimal[] cached = new Decimal[256];

        	for (int i = 0; i < cached.length; i++) {
        		byte v = (byte) i;
        		Decimal d = new Decimal(s, (long) v);
        		cached[i] = d;        		
//        		logger().info( v + " => " + d);
    		}    	
    		
        	values[s] = cached;
		}    	
    }    
  
    public Decimal(long unscaled) {
        this(unscaled, 0);      
    }

    public Decimal(long unscaled, int scale) {
        this(scale, unscaled);       
        validateScale(scale);       
    }
    
    private Decimal(int scale, long unscaled) {
        this.unscaled = unscaled;
        this.scale = scale;    	
    }

	private static void validateScale(int scale) {
		if (scale < 0) {
            throw new IllegalArgumentException("scale must be >= 0, but was: " + scale);
        }
	}

    public Decimal() {
        super();  
    }

    public long getUnscaled() {
        return unscaled;
    }

    public int getScale() {
        return scale;
    }
    
    public static Decimal valueOf(long unscaled) {
    	return valueOf(unscaled, 0);
    }
    
    public static Decimal valueOf(long unscaled, int scale) {    	
    	Decimal d = valueOf(unscaled, scale, null);
    	
    	if (d == null) {
    		d = new Decimal(scale, unscaled);
    	}    	
    	
    	return d;    	
    }
    
    private static Decimal valueOf(long unscaled, int scale, Decimal d) {
    	validateScale(scale);
    	
    	byte b = (byte) unscaled;
    	    	
    	if (b == unscaled) {
            if (scale < values.length) {
            	Decimal[] cached = values[scale];
            	int i = (0xFF & b);
            	d = cached[i];
            }
    	}
    	
    	return d;    	
    }
  
    @Override
    public String toString() {
        String s = Long.toString(this.unscaled);
       
        if (scale == 0) {
            return s;
        }
       
        StringBuffer buf = new StringBuffer(s.length() + this.scale + 1);
        int digits = s.length();
       
        if (this.unscaled < 0) {
            digits--;
            buf.append("-");
        }
       
        // skip the sign:
        int off = this.unscaled < 0 ? 1 : 0;
       
        if (digits <= scale) {
            buf.append("0.");
           
            for (int i = digits; i < scale; i++) {
                buf.append("0");
            }
           
            String ds = s.substring(off);       
            buf.append(ds);           
        }
        else {
            int ip = digits - scale;
            String ds = s.substring(off, ip + off);
            buf.append(ds);
            buf.append(".");
            buf.append(s.substring(ip + off));
        }       
       
        return buf.toString();
    }
    
    public Decimal multiply(Decimal d) {
        if (d == null) {
            throw new NullPointerException("d");
        }
        
        return Decimal.valueOf(this.unscaled * d.unscaled, this.scale + d.scale);        
    }
    
    public Decimal divide(Decimal d) {
    	return divide(d, Result.QUOTIENT, null);
    }
    
    public Decimal remainder(Decimal d) {
    	return divide(d, Result.REMAINDER, null);
    }
    
    public Decimal divide(Decimal d, Map<Result, Decimal> results) {    	
    	return divide(d, Result.QUOTIENT, results);    	
    }
    
    private Decimal divide(Decimal d, Result ret, Map<Result, Decimal> results) {
        if (d == null) {
            throw new NullPointerException("d");
        }
        
        if (d.unscaled == 0) {
            throw new ArithmeticException("/ by 0");
        }
        
        d = d.normalized();
              
        final Decimal n = this.normalized();
        
    	int ns = n.scale - d.scale;
    	long v = n.unscaled;
    	
    	if (ns < 0) {
    		long f = pow10(-ns);
    		v = v * f;
    		ns = 0;
    	}

    	long r = v / d.unscaled;
    	
    	Decimal result = null;
    	Decimal q = Decimal.valueOf(r, ns); 
    	
    	if (ret == Result.QUOTIENT) {
    		result = q;
    	}
    	
    	if (results != null) {
    		results.put(Result.QUOTIENT, q);
    	}
        
    	long vr = v - (r * d.unscaled);    	
    	Decimal rr = Decimal.valueOf(vr < 0 ? -vr : vr, n.scale);
        	
    	if (ret == Result.REMAINDER) {
    		result = rr;
    	}
    	
    	if (results != null) {
    		results.put(Result.REMAINDER, rr);
    	}
    	
        return result;
    }
  
    public Decimal add(Decimal d) {
        if (d == null) {
            throw new NullPointerException("d");
        }               
       
        if (this.scale == d.scale) {
            return Decimal.valueOf(this.unscaled + d.unscaled, this.scale);
        }
       
        if (this.scale < d.scale) {
            long u = this.unscaled * pow10(d.scale - this.scale);
            return Decimal.valueOf(u + d.unscaled, d.scale);
        }
        else {
            long u = d.unscaled * pow10(this.scale - d.scale);
            return Decimal.valueOf(u + this.unscaled, this.scale);
        }
    }
    
    public Decimal add(long value) {
    	long u = (this.scale == 0) ? value : pow10(this.scale) * value;
    	return Decimal.valueOf(this.unscaled + u, this.scale);
    }
    
    public Decimal subtract(Decimal d) {
        if (d == null) {
            throw new NullPointerException("d");
        }               
       
        if (this.scale == d.scale) {
            return Decimal.valueOf(this.unscaled - d.unscaled, this.scale);
        }
       
        if (this.scale < d.scale) {
            long u = this.unscaled * pow10(d.scale - this.scale);
            return Decimal.valueOf(u - d.unscaled, d.scale);
        }
        else {
            long u = d.unscaled * pow10(this.scale - d.scale);
            return Decimal.valueOf(-u + this.unscaled, this.scale);
        }
    }
    
//    public Decimal multiply(Decimal d) {
//        if (d == null) {
//            throw new NullPointerException("d");
//        }
//        
//        return Decimal.valueOf(this.unscaled * d.unscaled, this.scale + d.scale);        
//    }    

   
    private static long pow10(int pow) {               
        switch (pow) {
        case 0:
            return 1;           
        case 1:
            return 10;
        case 2:
            return 100;
        case 3:
            return 1000;
        case 4:
            return 10000;
        case 5:
            return 100000;
        case 6:
            return 1000000;
        case 7:
            return 10000000;
        case 8:
            return 100000000;
        case 9:
            return 1000000000;
        default:
            long r = 1000000000;
           
            for (int i = 10; i < pow; i++) {
                r *= 10;
            }
           
            return r;
        }       
    }
    
    /**
     * Normalized representation of this decimal.
     *  
     * @return
     */
    
    public Decimal normalized() {
    	Decimal r = this;
    	
    	long u = this.unscaled;
    	
    	if (u == 0) {
    		r = valueOf(0, 0);
    	}
    	else {
	    	for(int i = r.scale; i > 0; i--) {
	    		long p = pow10(i);	    		
	    		long m = u % p;
	    			    		
	    		if (m == 0) {
	    			u = u / p;	    			
	    			r = Decimal.valueOf(u, r.scale - i);
	    			break;
	    		}
	    	}
	    	
	    	r = valueOf(u, r.scale, r);
    	}
    	
    	return r;    	
    }
    
    
    private static long normalize(long unscaled, final int scale) {
    	long u = unscaled;
    	
    	if (u != 0) {
	    	for(int i = scale; i > 1; i--) {
	    		long p = pow10(i);	    		
	    		long m = u % p;
	    		
	    		if (m == 0) {
	    			u = u / p;
	    			break;
	    		}
	    	}
    	}
    	
    	return u;
    }
    
    public Decimal expand(int scale) {
    	Decimal r = this;
    	
    	if (this.scale < scale) {
    		int e = scale - this.scale;    		
    		long p = pow10(e);    		
    		long u = this.unscaled * p;
    		r = Decimal.valueOf(u, scale);
    	}    	
    	
    	return r;    	
    }
        
//    private static Logger logger() {
//		return Decimal.logger;
//	}

	@Override
	public int compareTo(Decimal o) {
		if (o == null) {
			throw new NullPointerException("o");
		}
		
		long a = normalize(this.unscaled, this.scale);
		long b = normalize(o.unscaled, o.scale);
		long r = a - b;
				
		return (r == 0) ? 0 : ((r < 0) ? -1 : 1);
	}
}