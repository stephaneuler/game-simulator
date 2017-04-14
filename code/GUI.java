package basic;

import jserver.Board;
import jserver.XSendAdapter;
import jserver.XSendDE;

public class GUI {
	Board board;
	int N = 7;
	private XSendDE xsend;

	public GUI() {
		board = new Board();
	    xsend = new XSendAdapter(board);
	    xsend.groesse(N+2, N+2);
	    for( int i=0; i<N+2; i++ ) {
	    	xsend.form2(i, 0, "s");
	    	xsend.form2(i, N+1, "s");
	    	xsend.form2(0, i, "s");
	    	xsend.form2(N+1, i, "s");
	    }
	}
	
	public XSendDE getXsend() {
		return xsend;
	}

	public void show(Position p) {
		xsend.statusText( "Ply " + p.ply );
		board.receiveMessage("clearAllText");
		for( int x=1; x<=N; x++ ) {
			for( int y=1; y<=N; y++ ) {
				int here = p.board[x][y];
				if( here == 1 ) {
					xsend.farbe2( x, y, XSendAdapter.RED );
				} else if( here == -1 ) {
					xsend.farbe2( x, y, XSendAdapter.BLUE );
				} else{
					xsend.farbe2( x, y, XSendAdapter.WHITE );
				}
			}
		}
		
	}


}
