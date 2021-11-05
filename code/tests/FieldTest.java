package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import basic.Field;

class FieldTest {
	Field f1 = new Field( 1, 2);
	Field f2 = new Field( 3, 5, 7);
	
	@Test
	void testGetX() {
		assertEquals(1, f1.getX(), "getX");
		assertEquals(3, f2.getX(), "getX");
	}

	@Test
	void testGetY() {
		assertEquals(2, f1.getY(), "getY");
	}

	@Test
	void testGetVal() {
		assertEquals(0, f1.getVal(), "getVal");
	}

	@Test
	void testEqualsObject() {
		assertEquals(f1, new Field(1,2));
		assertNotEquals(f1, new Field(1,2,4));
		assertNotEquals(f1, f2);
	}

}
