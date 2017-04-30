package own;

import basic.Move;

import java.util.List;

/**
 * Created by Nils on 30.04.2017.
 */
public class FirstMoveConsultant implements IMoveConsultant {
    private VirtualGameBoard virtualGameBoard = new VirtualGameBoard();

    @Override
    public void incorporateMoveOfRival(Move lastMove) {
        incorporateMove(lastMove, PlayerColor.Rival);
    }

    @Override
    public void incorporateMoveOfMyself(Move lastMove) {
        incorporateMove(lastMove, PlayerColor.Own);
    }

    @Override
    public Move getBestPossibleMove(List<Move> possibleMoves) {
        //TODO: This is currently dumb - create some awesome implementation!
        return possibleMoves.get(possibleMoves.size()/2);
    }

    private void incorporateMove(Move move, PlayerColor playerColor){
        int columnIndex = MyHelper.extractColumnIndex(move);
        virtualGameBoard.addCoinToColumn(columnIndex, playerColor);

        System.out.println(virtualGameBoard);
    }
}
