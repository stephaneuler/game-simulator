package own;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nils on 30.04.2017.
 */
public class VirtualGameBoard {
    private int verticalSize                            = 7;
    private int horizontalSize                          = 7;
    private int moveIndex                               = 0;
    private List<List<VirtualPosition>> listOfColumns   = new ArrayList<>();

    public VirtualGameBoard(){
        initializeBoard();
    }

    public void initializeBoard(){
        for(int h = 0; h < horizontalSize; h++)
        {
            List<VirtualPosition> tempColumn = new ArrayList<>();

            for(int v = 0; v < verticalSize; v++)
            {
                tempColumn.add(new VirtualPosition(h,v));
            }

            listOfColumns.add(tempColumn);
        }

        moveIndex = 0;
    }

    public void resetBoard(){
        initializeBoard();
    }

    public int getUsedRowCount(int columnIndex)
    {
        List<VirtualPosition> currentColumn = listOfColumns.get(columnIndex);
        int counter = 0;

        for(int i = currentColumn.size() - 1; i > 0; i--) {
            if(currentColumn.get(i).getPlayerColor() != PlayerColor.Empty) //count positions with valid coins
                counter++;
            else //if first position with no coin found, stop
                break;
        }

        return counter;
    }

    public boolean isColumnEmpty(int columnIndex)
    {
        if(getUsedRowCount(columnIndex) == 0)
            return true;
        else
            return false;
    }

    public void addCoinToColumn(int columnIndex, PlayerColor playerColor){
        List<VirtualPosition> currentColumn     = listOfColumns.get(columnIndex);
        int indexDestination                    = currentColumn.size() - getUsedRowCount(columnIndex) - 1;
        VirtualPosition positionDestination     = currentColumn.get(indexDestination);

        // set coin-color
        positionDestination.setPlayerColor(playerColor);
        // set index of move (to know their order)
        positionDestination.setMoveIndex(moveIndex);
        moveIndex++;
    }

    @Override
    public String toString(){
        //TODO: Board is displayed 90° twisted, columns are rows and vice-versa

        String plainPortrayalOfBoard = "";
        String colorTemp = "";
        PlayerColor playerColorTemp = null;
        int v = 0;

        plainPortrayalOfBoard += '\n';
        plainPortrayalOfBoard += '\n';
        plainPortrayalOfBoard += "VIRTUAL GAMEBOARD EXPORT";
        plainPortrayalOfBoard += '\n';

        plainPortrayalOfBoard += "\\";

        for(int h=0 ;h < horizontalSize; h++)
            plainPortrayalOfBoard += "|" + h;

        plainPortrayalOfBoard += "|";
        plainPortrayalOfBoard += '\n';

        for (List<VirtualPosition> currentColumn:listOfColumns) {

            plainPortrayalOfBoard += v;

            for (VirtualPosition currentPosition:currentColumn) {
                playerColorTemp = currentPosition.getPlayerColor();

                switch(playerColorTemp)
                {
                    case Own:
                        colorTemp = "O";
                        break;
                    case Rival:
                        colorTemp = "R";
                        break;
                    case Empty:
                        colorTemp = " ";
                        break;
                }

                plainPortrayalOfBoard += "|" + colorTemp;

            }

            plainPortrayalOfBoard += "|";
            plainPortrayalOfBoard += '\n';

            v++;
        }

        return plainPortrayalOfBoard;
    }
}
