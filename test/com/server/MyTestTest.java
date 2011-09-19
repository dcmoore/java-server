package com.server;

import org.junit.*;
import static org.junit.Assert.*;

public class MyTestTest {
	@Test
	public void test_isTrue() {
	   System.out.println("Should return true");
	   MyTest myTest = new MyTest();
	   assertTrue(myTest.isTrue() == true);
	}
}