package basic;

import java.util.List;
import java.util.Vector;

/**
 * Implementation eines passiven 4 Gewinnt spielers
 * @author Dustin Leibnitz
 * @author Maximilian Trautvetter
 *
 * NegaMax-Variante
 */
public class BlockerPlayer extends Player {
    private Helper helper = new Helper();
    private int mID = 0;

    @Override
    Move nextMove(Position p, List<Move> moves) {

        mID = helper.choose(p,moves);
        System.out.println("Move done: "+mID);
        return moves.get(mID);
    }


    private class Helper {
        int em3 = 0;
        int em2 = 0;
        int em1 = 0;
        int move = 0 ;
        int lastFieldCounter = 0;

        public int choose(Position p, List<Move> moves){
            em3 = em2;
            em2 = em1;
            if(p.getLastMove() != null) {
                em1 = p.getLastMove().s - 1;
                if (p.getLastMove().s - 1 == 6){
                    lastFieldCounter++;
                }
            }


        //If first move, place in the Middle
        if (em1 == 0){
            int id = moves.size()/2;
            return moves.size()/2;
        }
        System.out.println("em3: "+em3+" em2: "+em2+" em1: "+em1);

        if(em3 == em1){
            move = em1;
            System.out.println("move 1: "+move);
        }else if (em2 == em1){
            move = em1;
            System.out.println("move 2: "+move);
        }else if(em2 + 1 == em1 || em3 +1 == em1){
            move = em1+1;
            System.out.println("move 3: "+move);
        }else if(em2 -1 == em1 || em3 -1 == em1){
            move = em1 -1;
            System.out.println("move 4: "+move);
        }

        System.out.println("BlaThingy: "+lastFieldCounter);

        if (lastFieldCounter >= 4){
            move = moves.size()/2;
        }
        if(move > 6){
             return 0;
        }else if(move < 0){
            return 0;
        }else{
            return move;
        }

        }



    }
}

 