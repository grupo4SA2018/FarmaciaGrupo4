/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestFarmacia;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import ServiciosWeb.ServiciosWebFarmacia;

/**
 *
 * @author Pamela Palacios
 */
public class PruebaUnitaria {
    
    ServiciosWebFarmacia request;
    public PruebaUnitaria() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        request = new ServiciosWebFarmacia();
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void prueba_hello()
    {
        String p="world";
        String r =  this.request.hello(p);
        assertEquals("Hello world !",r);
    }
}
