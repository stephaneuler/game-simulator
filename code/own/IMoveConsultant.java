package own;

import basic.Move;

import java.util.List;

/**
 * Created by Nils on 30.04.2017.
 */
public interface IMoveConsultant {
    void incorporateMoveOfRival(Move lastMove);
    void incorporateMoveOfMyself(Move lastMove);
    Move getBestPossibleMove(List<Move> possibleMoves);
}
