package basic;

public class GuiHelper {
	static char[] white = {'\u2654', '\u2655', '\u2656','\u2657','\u2658', '\u2659' };
	static char[] black = {'\u265a','\u265b', '\u265c','\u265d', '\u265e', '\u265F' };

	public static String getChessUnicode(int piece) {
		if( piece > 0 ) {
			return "" + white[piece-1];
		} else if( piece < 0 ) {
			return "" + black[-piece-1];
		} else {
			return "";
		}
	}

}
