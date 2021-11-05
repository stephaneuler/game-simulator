package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import basic.MoveVG;

class MoveVGTest {

	@Test
	void testMoveVGString() {
		assertEquals(new MoveVG("2"), new MoveVG(2));
	}

}
