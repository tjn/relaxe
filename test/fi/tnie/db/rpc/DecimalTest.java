/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;

import java.util.logging.Logger;

import junit.framework.TestCase;

public class DecimalTest extends TestCase {

	private static Logger logger = Logger.getLogger(DecimalTest.class.getName());
	
	public void testToString() {
		Decimal d = null;

		d = Decimal.valueOf(1000, 1);
		logger().info("testToString: d=" + d);
		assertEquals("100.0", d.toString());
				
//		d = new Decimal(1000, -1);
//		logger().info("testToString: d=" + d);
//		assertEquals("10000", d.toString());

		d = Decimal.valueOf(1000, 0);
		logger().info("testToString: d=" + d);
		assertEquals("1000", d.toString());

		d = Decimal.valueOf(-1000, 1);
		logger().info("testToString: d=" + d);
		assertEquals("-100.0", d.toString());
				
//		d = new Decimal(-1000, -1);
//		logger().info("testToString: d=" + d);
//		assertEquals("-10000", d.toString());

		d = Decimal.valueOf(-1000, 0);
		logger().info("testToString: d=" + d);
		assertEquals("-1000", d.toString());	
	}
	
    public void testAdd1() {
        Decimal a = new Decimal(12345, 3);       
        Decimal b = new Decimal(12345, 2);       
        Decimal c = a.add(b);       
        System.out.println("c: " + c);
        assertEquals("135.795", c.toString());
        assertEquals(3, c.getScale());
        assertEquals(135795, c.getUnscaled());
    }
   
    public void testAdd2() {
        Decimal a = new Decimal(-1, 2);       
        Decimal b = new Decimal(-2, 3); 
        Decimal c = a.add(b);       
        System.out.println("c: " + c);
        assertEquals(3, c.getScale());
        assertEquals(-12, c.getUnscaled());       
        assertEquals("-0.012", c.toString());
    }
    
    public void testAdd3() {
        Decimal a = new Decimal(12345, 3);
        Decimal c = a.add(12);       
        System.out.println("c: " + c);
        assertEquals(24345, c.getUnscaled());
        assertEquals(3, c.getScale());
    }
   
    
    public void testDivide() {
        Decimal a = new Decimal(3, 2);
        Decimal b = new Decimal(2, 3); 
        //  0.03 / 0.002 => 15
        Decimal c = a.divide(b);       
        System.out.println("c: " + c);
        assertEquals(15, c.getUnscaled());
        assertEquals(0, c.getScale());
    }
    
    public void testCompact4() {
    	// 0.02    	
        Decimal a = new Decimal(2, 2);         
        Decimal c = a.normalized();       
        System.out.println("c: " + c);
        
    	// 0.020    	
        a = new Decimal(20, 3);         
        c = a.normalized();       
        System.out.println("c: " + c);
        
    }
    
    public void testValueOf() {
    	Decimal a = null; 
    	Decimal b = null;
        a = Decimal.valueOf(-1, 2);
        b = Decimal.valueOf(-1, 2);        
        assertTrue(a == b);
        
        a = Decimal.valueOf(-1, 2);
        b = Decimal.valueOf(-1, 1);        
        assertFalse(a == b);
        
        a = Decimal.valueOf(-1000, 2);
        assertEquals("-10.00", a.toString());
                
//        assertEquals(3, c.getScale());
//        assertEquals(-12, c.getUnscaled());       
//        assertEquals("-0.012", c.toString());
    }
    
    public void testSubtract() {
    	Decimal a = null;
    	Decimal b = null;
    	Decimal c = null;
    	
        a = new Decimal(3, 0);       
        b = new Decimal(1, 0);        
        c = a.subtract(b);            
        System.out.println("c: " + c);
        assertEquals(0, c.getScale());
        assertEquals(2, c.getUnscaled());      

        a = new Decimal(1, 0);       
        b = new Decimal(3, 0);        
        c = a.subtract(b);            
        System.out.println("c: " + c);
        assertEquals(0, c.getScale());
        assertEquals(-2, c.getUnscaled());      

        a = new Decimal(10, 1);       
        b = new Decimal(100, 2);        
        c = a.subtract(b);            
        System.out.println("c: " + c);
        assertEquals(2, c.getScale());
        assertEquals(0, c.getUnscaled());      

        a = new Decimal(50, 2);       
        b = new Decimal(20, 1);        
        c = a.subtract(b);            
        System.out.println("c: " + c);
        assertEquals(2, c.getScale());
        assertEquals(-150, c.getUnscaled());     

    }
      
    public void testToString2() {
        Decimal a = null;
        a = new Decimal(-12, 3);
        System.out.println("a: " + a);
        assertEquals("-0.012", a.toString());
       
        a = new Decimal(12, 3);
        System.out.println("a: " + a);
        assertEquals("0.012", a.toString());
       
        a = new Decimal(123, 3);
        System.out.println("a: " + a);
        assertEquals("0.123", a.toString());

        a = new Decimal(-123, 3);
        System.out.println("a: " + a);
        assertEquals("-0.123", a.toString());

        a = new Decimal(12000, 3);
        System.out.println("a: " + a);
        assertEquals("12.000", a.toString());

        a = new Decimal(-12000, 3);
        System.out.println("a: " + a);
        assertEquals("-12.000", a.toString());
    }
    
	private static Logger logger() {
		return DecimalTest.logger;
	}
	
    public void testCompact1() {
    	Decimal a = null;
    	Decimal c = null; 

    	a = new Decimal(123000, 4);     
        System.out.println("a: " + a);
        assertEquals(4, a.getScale());
        assertEquals(123000, a.getUnscaled());       
        assertEquals("12.3000", a.toString());
        
        c = a.normalized();        
        System.out.println("c: " + c);
        assertEquals(1, c.getScale());
        assertEquals(123, c.getUnscaled());
        assertEquals("12.3", c.toString());

        a = new Decimal(123000, 6);
        c = a.normalized();        
        System.out.println("c: " + c);
        assertEquals(3, c.getScale());
        assertEquals(123, c.getUnscaled());
        assertEquals("0.123", c.toString());        
    }

	public void testCompact2() {
		Decimal a;
		Decimal c;
		a = new Decimal(123000, 2);
        c = a.normalized();        
        System.out.println("c: " + c);
        assertEquals(0, c.getScale());
        assertEquals(1230, c.getUnscaled());
        assertEquals("1230", c.toString());
	}
	
	public void testCompact3() {
		Decimal a;
		Decimal c;
		a = new Decimal(-123000, 2);
        c = a.normalized();        
        System.out.println("c: " + c);
        assertEquals(0, c.getScale());
        assertEquals(-1230, c.getUnscaled());
        assertEquals("-1230", c.toString());
	}
	
	public void testCompact5() {
		Decimal a;
		Decimal c;
		a = new Decimal(-123000, 4);
        c = a.normalized();        
        System.out.println("c: " + c);
        assertEquals(1, c.getScale());
        assertEquals(-123, c.getUnscaled());
        assertEquals("-12.3", c.toString());
	}
	
	public void testCompact6() {
		Decimal z0 = Decimal.valueOf(0);
				
		for (int i = 0; i < 5; i++) {
			Decimal z = Decimal.valueOf(0, i);
			z = z.normalized();
			assertSame(z0, z);
			Decimal a = new Decimal(0, i).normalized();
			assertSame(z, a);			
		}
	}
	
	public void testDivision() {
		Decimal a = Decimal.valueOf(20, 1);
		Decimal b = Decimal.valueOf(5, 2);
		Decimal c = a.divide(b);
		Decimal d = null;
		
		logger().info("testDivision: c=" + c);
		assertEquals(0, c.getScale());
		assertEquals(40, c.getUnscaled());

		a = Decimal.valueOf(5, 2);
		b = Decimal.valueOf(20, 1);
		c = a.divide(b);
		d = a.remainder(b);
		logger().info("testDivision: c=" + c);
		logger().info("testDivision: d=" + d);
		
		logger().info("testDivision: c=" + c);
								
	}

	public void testDiv2() {
		Decimal a;
		Decimal b;
		Decimal c;
		Decimal d;
		// 0.05 / 2.0 = 0.02|5
		a = Decimal.valueOf(5, 2); 
		b = Decimal.valueOf(20, 1);
		logger().info("testDivision: a=" + a);		
		logger().info("testDivision: b=" + b);
		
		c = a.divide(b);	
		
		logger().info("testDivision: c=" + c);
		assertEquals("0.02", c.toString());
		d = a.remainder(b);
		logger().info("testDivision: d=" + d);
		assertEquals("0.01", d.toString());
	}
	
	public void testNormalize() {
		Decimal a = Decimal.valueOf(20, 1);
		a = a.normalized();
		logger().info("testNormalize: a=" + a);
		assertEquals(2, a.getUnscaled());
		assertEquals(0, a.getScale());
		
		
	}	
	
}
