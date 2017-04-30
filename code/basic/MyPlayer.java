package basic;

import own.FirstMoveConsultant;
import own.IMoveConsultant;

import java.util.List;

/**
 * Created by Nils on 30.04.2017.
 */
public class MyPlayer extends Player {
    private IMoveConsultant moveConsultant = new FirstMoveConsultant();

    //Position p => Offers several info about the current game-status
    //List<Move> moves => List of next possible moves (e.g. one column already full, etc.)
    @Override
    Move nextMove(Position p, List<Move> moves) {
        moveConsultant.incorporateMoveOfRival(p.getLastMove());
        Move move = moveConsultant.getBestPossibleMove(moves);
        moveConsultant.incorporateMoveOfMyself(move);
        return move;
    }
}
