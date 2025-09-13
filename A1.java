import java.util.HashSet;
import java.util.Set;

public class A1 implements A1Interface {
    private char[][] boggleBoard;
    private DictInterface dictionary;
    private int wordLength;
    private Set<String> words;
    private StringBuilder currentSolution = new StringBuilder();

    public Set<String> getWordsOfLength(char[][] board, DictInterface dict, int length) {
        this.boggleBoard = board;
        this.dictionary = dict;
        this.wordLength = length;
        words = new HashSet<String>();

        for (int row = 0; row < boggleBoard.length; row++) {
            for (int col = 0; col < boggleBoard[0].length; col++) {
                resetBoard();
                currentSolution = new StringBuilder();
                if (boggleBoard[row][col] != '*') //Append the first character in the path
                //I handle * in the solve function
                    currentSolution.append(boggleBoard[row][col]);
                solve(row, col);
            }
        }

        return words;
    }

    private void solve(int row, int col) {
        for (int direction = 0; direction < 8; direction++) {
            if (isValid(row, col, direction)) {
                Coordinates next = nextCoordinates(row, col, direction);

                if (boggleBoard[row][col] == '*') { //This cell currently has a wild card!
                    for (char letter = 'A'; letter <= 'Z'; letter++) {
                        boggleBoard[row][col] = letter;
                        currentSolution.append(boggleBoard[row][col]);

                        int result = dictionary.searchPrefix(currentSolution);
                        if (result == 1) //prefix but not word
                            solve(next.row, next.col);

                        if (result == 2) { //word but not prefix
                            if(currentSolution.length() == wordLength){
                                words.add(currentSolution.toString());
                            }
                        }

                        if (result == 3) { //word and prefix
                            if(currentSolution.length() == wordLength){
                                words.add(currentSolution.toString());
                            }
                            else
                                solve(next.row, next.col);
                        }
                        
                        currentSolution.deleteCharAt(currentSolution.length() - 1);
                    }
                    boggleBoard[row][col] = '*';
                }

                else if (boggleBoard[next.row][next.col] == '*') { //The NEXT cell contains a wild card!
                        for (char letter = 'A'; letter <= 'Z'; letter++) {
                            boggleBoard[next.row][next.col] = letter;
                            currentSolution.append(boggleBoard[next.row][next.col]);
    
                            int result = dictionary.searchPrefix(currentSolution);
                            if (result == 1) //prefix but not word
                                solve(next.row, next.col);
    
                            if (result == 2) { //word but not prefix
                                if(currentSolution.length() == wordLength){
                                    words.add(currentSolution.toString());
                                }
                            }
    
                            if (result == 3) { //word and prefix
                                if(currentSolution.length() == wordLength){
                                    words.add(currentSolution.toString());
                                }
                                else
                                    solve(next.row, next.col);
                            }
                            
                            currentSolution.deleteCharAt(currentSolution.length() - 1);
                        }
                    boggleBoard[next.row][next.col] = '*';
                }

                else { //There's not a wild card
                    currentSolution.append(boggleBoard[next.row][next.col]);
                    //mark char as used
                    boggleBoard[next.row][next.col] = Character.toLowerCase(boggleBoard[next.row][next.col]);

                    int result = dictionary.searchPrefix(currentSolution);
                    if (result == 1) //prefix but not word
                        solve(next.row, next.col);

                    if (result == 2) { //word but not prefix
                        if(currentSolution.length() == wordLength){
                            words.add(currentSolution.toString());
                        }
                    }

                    if (result == 3) { //word and prefix
                        if(currentSolution.length() == wordLength){
                            words.add(currentSolution.toString());
                        }
                        else
                            solve(next.row, next.col);
                    }

                    currentSolution.deleteCharAt(currentSolution.length() - 1);
                    boggleBoard[next.row][next.col] = Character.toUpperCase(boggleBoard[next.row][next.col]);
                }
            }
        }
    }

    private Coordinates nextCoordinates(int row, int col, int direction) {
        Coordinates result = null;
		switch(direction){
			case 0: //up left
				result = new Coordinates(row-1, col-1);
				break;
			case 1: //up
				result = new Coordinates(row-1, col);
				break;
			case 2: //up right
				result = new Coordinates(row-1, col+1);
				break;
			case 3: //right
				result = new Coordinates(row, col+1);
				break;
			case 4: //bottom right
				result = new Coordinates(row+1, col+1);
				break;
			case 5: //bottom
				result = new Coordinates(row+1, col);
				break;
			case 6: //bottom left
				result = new Coordinates(row+1, col-1);
				break;
			case 7: //left
				result = new Coordinates(row, col-1);
				break;
		}
		return result;
    }

    private boolean isValid(int row, int col, int direction) {
        Coordinates next = nextCoordinates(row, col, direction);

        if (next.row > boggleBoard.length - 1 || next.row < 0 || next.col > boggleBoard[0].length - 1 || next.col < 0) 
            return false; //If at edge of board
        if (Character.isLowerCase(boggleBoard[next.row][next.col])) 
            return false; //If already used
        if (boggleBoard[next.row][next.col] == '#') 
            return false; //If cell is blocked
        return true;
    }

    private void resetBoard() {
        for (int row = 0; row < boggleBoard.length; row++) {
            for (int col = 0; col < boggleBoard[0].length; col++) {
                boggleBoard[row][col] = Character.toUpperCase(boggleBoard[row][col]);
            }
        }
    }
    private class Coordinates {
        int row;
        int col;

        private Coordinates(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
}