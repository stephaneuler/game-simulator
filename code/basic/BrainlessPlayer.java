package basic;

import java.util.List;

public class BrainlessPlayer extends Player {

	private int blocken = 100;
	private int myStone = 1;
	private int empty = 0;

	@Override
	Move nextMove(Position p, List<Move> moves) {

		int myNumber = p.nextPlayer;

		for (int i = 0; i < moves.size(); i++) {
			Position check = new Position(p);
			check.move(moves.get(i));
			if (check.isWin()) {
				return moves.get(i);
			}
		}

		int[] heatmap = new int[moves.size()];
		if(moves.size() == 7)
		{
			heatmap = new int[]{0,1,2,3,2,1,0};
		}
		
		int v = 0, h= 0, lo= 0, ro= 0;
		int v_v= 0, h_v= 0, lo_v= 0, ro_v= 0;
		
		
		for (int i = 0; i < moves.size(); i++) {
			Position check = new Position(p);
			check.nextPlayer = myNumber * -1;
			check.move(moves.get(i));
			if (check.isWin()) {
				heatmap[i] += blocken;
			}
		}

		for (int i = 0; i < moves.size(); i++) {
			// find empty field
			for (int z = 1; z <= p.N; z++) {
				if (p.board[moves.get(i).s][z] == 0) {

					// oben
					for (int y = z + 1; y <= p.N; y++) {
						if (p.board[moves.get(i).s][y] == myNumber) {
							heatmap[i] += myStone;
							v++;
							v_v += myStone;
						} else if (p.board[moves.get(i).s][y] == 0){
							heatmap[i] += empty;
							v++;
							v_v += empty;
						}else{break;}
					}
					
					// unten
					for (int y = z - 1; y >= 1; y--) {
						if (p.board[moves.get(i).s][y] == myNumber) {
							heatmap[i] += myStone;
							v++;
							v_v += myStone;
						} else if (p.board[moves.get(i).s][y] == 0){
							heatmap[i] += empty;
							v++;
							v_v += empty;
						}else{break;}
					}

					// links
					for (int x = moves.get(i).s-1; x >= 1; x--) {
						if (p.board[x][z] == myNumber) {
							heatmap[i] += myStone;
							h++;
							h_v += myStone;
						} else if(p.board[x][z] == 0){
							heatmap[i] += empty;
							h++;
							h_v += empty;
						}else{break;}
					}

					// rechts
					for (int x = moves.get(i).s+1; x <= p.N; x++) {
						if (p.board[x][z] == myNumber) {
							heatmap[i] += myStone;
							h++;
							h_v += myStone;
						} else if(p.board[x][z] == 0){
							heatmap[i] += empty;
							h++;
							h_v += empty;
						}else{break;}
					}

					// rechts oben
					for (int x = moves.get(i).s+1, y = z+1; x <= p.N && y <= p.N; y++, x++) {
						if (p.board[x][y] == myNumber) {
							heatmap[i] += myStone;
							ro++;
							ro_v += myStone;
						} else if(p.board[x][y] == 0){
							heatmap[i] += empty;
							ro++;
							ro_v += empty;
						}else{break;}
					}

					// rechts unten
					for (int x = moves.get(i).s-1, y = z+1; x <= p.N && y >= 1; y--, x++) {
						if (p.board[x][y] == myNumber) {
							heatmap[i] += myStone;
							ro++;
							ro_v += myStone;
						} else if(p.board[x][y] == 0){
							heatmap[i] += empty;
							ro++;
							ro_v += empty;
						}else{break;}
					}

					// links oben
					for (int x = moves.get(i).s+1, y = z-1; x >= 1 && y <= p.N; y++, x--) {
						if (p.board[x][y] == myNumber) {
							heatmap[i] += myStone;
							lo++;
							lo_v += myStone;
						} else if(p.board[x][y] == 0){
							heatmap[i] += empty;
							lo++;
							lo_v += empty;
						}else{break;}
					}

					// links unten
					for (int x = moves.get(i).s-1, y = z-1; x >= 1 && y >= 1; y--, x--) {
						if (p.board[x][y] == myNumber) {
							heatmap[i] += myStone;
							lo++;
							lo_v += myStone;
						} else if(p.board[x][y] == 0){
							heatmap[i] += empty;
							lo++;
							lo_v += empty;
						}else{break;}
					}
					
					// 3 + new 
					if(v < 3){heatmap[i] -= v_v;}
					if(h < 3){heatmap[i] -= h_v;}
					if(ro < 3){heatmap[i] -= ro_v;}
					if(lo < 3){heatmap[i] -= lo_v;}
				}
			}
		}

		System.out.print("{\t");
		for (int i = 0; i < heatmap.length; i++) {
			System.out.print(heatmap[i] + "\t");
		}
		System.out.println("}");
		
		int max = heatmap[0];
		Move m = moves.get(0);
		for (int i = 0; i < moves.size(); i++) {
			if (max < heatmap[i]) {
				max = heatmap[i];
				m = moves.get(i);
			}
		}

		return m;
	}

}