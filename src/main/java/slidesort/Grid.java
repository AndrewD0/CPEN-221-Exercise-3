package slidesort;

import java.util.List;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;


public class Grid {
    private int[][] _grid;



    /**
     * Create a new grid
     * @param seedArray is not null
     *                  and seedArray.length > 0
     *                  and seedArray[0].length > 0
     */
    public Grid(int[][] seedArray) {
        int rows = seedArray.length;
        int cols = seedArray[0].length;
        _grid = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                _grid[i][j] = seedArray[i][j];
            }
        }
    }


    @Override
    public boolean equals(Object other) {
        if (other instanceof Grid) {
            Grid g2 = (Grid) other;
            if (this._grid.length != g2._grid.length) {
                return false;
            }
            if (this._grid[0].length != g2._grid[0].length) {
                return false;
            }
            int rows = _grid.length;
            int cols = _grid[0].length;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (this._grid[i][j] != g2._grid[i][j]) {
                        return false;
                    }
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Check if this grid is a valid grid.
     * A grid is valid if, for c = min(rows, cols),
     * the grid contains zero or more values in [1, c]
     * exactly once and all other entries are 0s.
     *
     * @return true if this is a valid grid and false otherwise
     */
    public boolean isValid() {
        boolean checker = true;
        int rows = this._grid.length;
        int columns = this._grid[0].length;
        int c = Math.min(rows,columns);
        //First, we need to find the minimum value between rows and columns.

        //Now, we can check if the values are singular and between 1 and c.

        HashSet <Integer> valueSet = new HashSet<>();
        Integer zero = Integer.valueOf(0);



        for(int[] i: this._grid) {
            for (int j : i) {

                Integer value = Integer.valueOf(j);

                if (valueSet.contains(value) == true) {

                    checker = false;
                    return checker;

                }
                else if(value < 1 && value != 0 || value > c){

                    checker = false;
                    return checker;
                }
                else if (value.equals(zero) != true){

                    valueSet.add(value);
                }

            }

        }
        return checker;
    }

    /**
     * Check if this grid is sorted.
     * A grid is sorted iff it is valid and,
     *  for all pairs of entries (x, y)
     *  such that x > 0 and y > 0,
     *  if x < y then the position(x) < position(y).
     * If x is at location (i, j) in the grid
     * then position(x) = i * (number of cols) + j.
     *
     * @return true if the grid is sorted and false otherwise.
     */
    public boolean isSorted() {

        boolean checker = true;
        int columnsTotal = this._grid[0].length;
        HashMap<Integer, Integer> mapPositions = new HashMap<Integer, Integer>();

        if(!this.isValid()){
            checker = false;
            return checker;
        }

        for(int row = 0; row <this._grid.length; row++){
            for(int col = 0; col < this._grid[row].length; col++){
                if(this._grid[row][col] != 0) {

                    mapPositions.put(this._grid[row][col], row * columnsTotal + col);
                }
            }
        }

        Integer[] keys = mapPositions.keySet().toArray(new Integer[mapPositions.size()]);

        Integer[] values = mapPositions.values().toArray(new Integer[mapPositions.size()]);

        //Creating two new arrays to store the keys and values of our original hashmap!
        //Now, the iteration of these arrays can begin! Keep in mind, both arrays are the same length!


        //These nested for loops will check each element of the keys array, which denotes the number on the grid.
        //If key x is less than key y, the if statement will run and check if the position of x is less than y.
        //If this does not hold, then the grid is not sorted. Therefore, the method will return false.

        for(int i = 0; i < values.length; i++){
            for(int j = i+1; j< values.length; j++){
                if(keys[i] < keys[j]){
                    if(values[i] > values[j]){
                        checker = false;
                        return checker;
                    }
                }
            }
        }

        return checker;
    }

    /**
     * Check if a list of moves is feasible.
     * A move is feasible if it starts with a non-zero entry,
     * does not move that number off the grid,
     * and it does not involve jumping over another non-zero number.
     *
     * @param   moveList is not null.
     * @return  true if the list of moves are all feasible
     *          and false otherwise.
     *          By definition an empty list is always feasible.
     */
    public boolean validMoves(List<Move> moveList) {
        //Positive numbers, down and right. Negative: up and left!
        boolean checker = true;
        int[][] gridClone = new int[this._grid.length][this._grid[0].length];
        int size = moveList.size();
        int displaceValue;
        int movePosition;
        int fixedColumnValue;
        int fixedRowValue;


        //Creating a separate but same array as the original grid.

        for(int a = 0; a < gridClone.length; a++){
            for(int b = 0; b < gridClone[0].length; b++){
                gridClone[a][b] = this._grid[a][b];
            }
        }




        for(int k = 0; k < size; k++){

            displaceValue = moveList.get(k).displacement;

            if(moveList.get(k).startingPosition.i > this._grid.length-1 || moveList.get(k).startingPosition.j > this._grid[0].length-1){
                checker = false;
                return checker;
            }

            if(gridClone[moveList.get(k).startingPosition.i][moveList.get(k).startingPosition.j] == 0){
                checker = false;
                return checker;
            }


            if(moveList.get(k).rowMove == false){

                movePosition = moveList.get(k).startingPosition.i;
                fixedColumnValue = moveList.get(k).startingPosition.j;

                if(movePosition+displaceValue < 0 || movePosition + displaceValue >= gridClone.length){
                    checker = false;
                    return checker;
                }

                for(int y = movePosition+1; y < movePosition + displaceValue+1; y++){

                    if(gridClone[y][fixedColumnValue] != 0){
                        checker = false;
                        return checker;
                    }
                }
                gridClone[movePosition+displaceValue][fixedColumnValue] = gridClone[moveList.get(k).startingPosition.i][moveList.get(k).startingPosition.j];
                if(displaceValue != 0){
                    gridClone[moveList.get(k).startingPosition.i][moveList.get(k).startingPosition.j] = 0;
                }

            }
            else{
                movePosition = moveList.get(k).startingPosition.j;
                fixedRowValue = moveList.get(k).startingPosition.i;

                if(movePosition + displaceValue < 0 || movePosition + displaceValue >= gridClone[0].length){
                    checker = false;
                    return checker;
                }

                for(int x = movePosition + 1; x < movePosition + displaceValue + 1; x++){
                    if(gridClone[fixedRowValue][x] != 0){
                        checker = false;
                        return checker;
                    }
                }

                gridClone[fixedRowValue][movePosition+displaceValue] = gridClone[moveList.get(k).startingPosition.i][moveList.get(k).startingPosition.j];
                if(displaceValue != 0){
                    gridClone[moveList.get(k).startingPosition.i][moveList.get(k).startingPosition.j] = 0;
                }
            }
        }

        return checker;
    }

    /**
     * Apply the moves in moveList to this grid
     * @param moveList is a valid list of moves
     */
    public void applyMoves(List<Move> moveList) {
        int size = moveList.size();
        int displaceValue;
        int movePosition;
        int fixedColumnValue;
        int fixedRowValue;


        for(int k = 0; k < size; k++){

            displaceValue = moveList.get(k).displacement;

            if(moveList.get(k).rowMove == false){

                movePosition = moveList.get(k).startingPosition.i;
                fixedColumnValue = moveList.get(k).startingPosition.j;


                this._grid[movePosition+displaceValue][fixedColumnValue] = this._grid[moveList.get(k).startingPosition.i][moveList.get(k).startingPosition.j];
                if(displaceValue != 0){
                    this._grid[moveList.get(k).startingPosition.i][moveList.get(k).startingPosition.j] = 0;
                }

            }
            else{
                movePosition = moveList.get(k).startingPosition.j;
                fixedRowValue = moveList.get(k).startingPosition.i;

                this._grid[fixedRowValue][movePosition+displaceValue] = this._grid[moveList.get(k).startingPosition.i][moveList.get(k).startingPosition.j];
                if(displaceValue != 0){
                    this._grid[moveList.get(k).startingPosition.i][moveList.get(k).startingPosition.j] = 0;
                }

            }
        }
    }

    /**
     * Return a list of moves that, when applied, would convert this grid
     * to be sorted
     * @return a list of moves that would sort this grid
     */
    public List<Move> getSortingMoves() {
        List<Move> sortMovesList = new ArrayList<Move>();
        List<Move> oneValueList = new ArrayList<Move>();
        int rows = this._grid.length;
        int columns = this._grid[0].length;
        int max = Math.max(rows,columns);
        int[][] gridClone = new int[rows][columns];
        int displaceValue;
        boolean displacedCondition;
        Random i = new Random();
        Random j = new Random();
        Random displacement = new Random();
        Random trueFalse = new Random();
        Random displacePositive = new Random();
        Random positions = new Random();

        int firstPosition;




        for(int a = 0; a < gridClone.length; a++){
            for(int b = 0; b < gridClone[0].length; b++){
                gridClone[a][b] = this._grid[a][b];
            }
        }

        Grid gridTest = new Grid(gridClone);

        int counter = gridTest.numberOccur();
        int[] arrayPositions = new int[counter*2];


        while(gridTest.isSorted() != true){

            displaceValue = displacement.nextInt(max-1)+1;
            displacedCondition = displacePositive.nextBoolean();

            arrayPositions = gridTest.positionsList(counter);

            firstPosition = i.nextInt(counter*2-1);

            while(firstPosition%2 != 0){
                firstPosition--;
            }



            if(displacedCondition == true){
                displaceValue = displaceValue*(1);
            }
            else{
                displaceValue = displaceValue*(-1);
            }


            Move moveValues = new Move(new Position(arrayPositions[firstPosition],arrayPositions[firstPosition+1]), trueFalse.nextBoolean(), displaceValue);
            sortMovesList.add(moveValues);
            oneValueList.add(moveValues);

            if(gridTest.validMoves(oneValueList) != true){
                sortMovesList.remove(sortMovesList.size()-1);
                oneValueList.remove(0);
            }
            else{
                gridTest.applyMoves(oneValueList);
                oneValueList.remove(0);


            }


        }

        return sortMovesList;

    }

    public int numberOccur(){
        int counter = 0;
        int rows = this._grid.length;
        int columns = this._grid[0].length;

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){

                if(this._grid[i][j] != 0){
                    counter++;
                }
            }
        }


        return counter;
    }
    public int[] positionsList(int counter){

        int[]arrayPositions = new int[counter*2];
        int rows = this._grid.length;
        int columns = this._grid[0].length;
        int arrayCounter = 0;

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                if(this._grid[i][j] != 0){
                    arrayPositions[arrayCounter] = i;
                    arrayPositions[arrayCounter+1] = j;
                    arrayCounter += 2;
                }
            }

        }

        return arrayPositions;


    }



}
