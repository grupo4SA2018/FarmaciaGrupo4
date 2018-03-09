/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestFarmacia;

import com.eviware.soapui.tools.SoapUITestCaseRunner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pamela Palacios
 */
public class PruebaFuncional {
    
     
 public PruebaFuncional() {
 }
  @Test
	public void shouldExecuteAllTestCases() throws Exception{
		// given
		SoapUITestCaseRunner runner = new SoapUITestCaseRunner();
             //String path="C:\\Users\\Pamela Palacios\\Documents\\farmacia-soapui-project.xml";
              String path="/home/farmacia-soapui-project.xml";
             runner.setProjectFile(path);
     	//runner.setProjectProperties(properties);
     	runner.run();
	}

}
