import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class TicTacToe {

    private static final  int WIN_COUNT = 3;
    private static final char DOT_HUMAN = 'X';
    private static final char DOT_AI = 'O';
    private static final char DOT_EMPTY = '•';

    private static final Scanner SCANNER = new Scanner(System.in);

    private static char[][] field; // Двумерный массив хранит текущее состояние игрового поля

    private static final Random random = new Random();

    private static int fieldSizeX; // Размерность игрового поля
    private static int fieldSizeY; // Размерность игрового поля
    
    
    
    public static void main(String[] args) {
        while (true){
            initialize();
            printField();
            while (true){
                humanTurn();
                printField();
                if (gameCheck(DOT_HUMAN, "Вы победили!"))
                    break;
                aiTurn();
                printField();
                if (gameCheck(DOT_AI, "Компьютер победил!"))
                    break;
            }
            System.out.println("Желаете сыграть еще раз? (Y - да)");
            if (!SCANNER.next().equalsIgnoreCase("Y"))
                break;
        }
    }

    /**
     * Инициализация игрового поля
     */
    private static void initialize(){
        // Установим размерность игрового поля
        fieldSizeX =5;
        fieldSizeY = 9;


        field = new char[fieldSizeX][fieldSizeY];

        // Пройдем по всем элементам массива
        Arrays.stream(field)
                .parallel()
                .forEach((x) -> Arrays.fill(x,DOT_EMPTY));
    }

    /**
     * Отрисовка игрового поля
     * */

    private static void printField(){
        System.out.print("+");
        for (int i = 0; i < fieldSizeY * 2 + 1; i++){
            System.out.print((i % 2 == 0) ? "-" : i / 2 + 1);
        }
        System.out.println();

        for (int i = 0; i < fieldSizeX; i++){
            System.out.print(i + 1 + "|");

            for (int j = 0; j <  fieldSizeY; j++)
                System.out.print(field[i][j] + "|");

            System.out.println();
        }

        for (int i = 0; i < fieldSizeY * 2 + 2; i++){
            System.out.print("-");
        }
        System.out.println();

    }


    /**
     * Обработка хода игрока (человек)
     */
    private static void humanTurn(){
        int x, y;
        do
        {
            System.out.print("Введите координаты хода X и Y (от 1 до 3) через пробел >>> ");
            x = SCANNER.nextInt() - 1;
            y = SCANNER.nextInt() - 1;
        }
        while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[x][y] = DOT_HUMAN;
    }

    /**
     * Проверка, ячейка является пустой
     * @param x
     * @param y
     * @return
     */
    static boolean isCellEmpty(int x, int y){
        return field[x][y] == DOT_EMPTY;
    }

    /**
     * Проверка корректности ввода
     * (координаты хода не должны превышать размерность массива, игрового поля)
     * @param x
     * @param y
     * @return
     */
    static boolean isCellValid(int x, int y){
        return x >= 0 &&  x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    /**
     * Ход компьютера
     */
    private static void aiTurn(){
        int x, y;
        do
        {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        }
        while (!isCellEmpty(x, y));
        field[x][y] = DOT_AI;
    }

    /**
     * Проверка победы
     * @param c
     * @return
     */
    static boolean checkWin(char c){
        return isHorizonCheckWin(c)
                || isVerticalCheckWin(c)
                || isLеftDiagonalCheckWin(c)
                || isRightDiagonalCheckWin(c);
    }

    //Проверка диагоналей
    private static boolean isLеftDiagonalCheckWin(char c) {
        int count = 0;
        int temp;
        for (int i = fieldSizeX, j = 0; j <= fieldSizeY;) {
            for (int k = 0; (i+k) < fieldSizeX && (j+k) < fieldSizeY; k++) {
                if (field[i+k][j+k] == c) {
                    if(++count == WIN_COUNT) return true;
                    continue;
                }
                count = 0;
            }
            temp = (i == 0) ? ++j : --i;
        }
        return false;
    }

    //Проверка диагоналей 2
    private static boolean isRightDiagonalCheckWin(char c) {
        int count = 0;
        int temp;
        for (int i = 0, j = 0; i < fieldSizeX && j < fieldSizeY;) {
            for (int k = 0; (i+k) < fieldSizeX && (j-k) >= 0; k++) {
                if (field[i+k][j-k] == c) {
                    if(++count == WIN_COUNT) return true;
                    continue;
                }
                count = 0;
            }
            temp = (j == fieldSizeX - 1) ? ++i : ++j;
        }
        return false;
    }

    //Проверка вертикали
    private static boolean isVerticalCheckWin(char c) {
        int count = 0;
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (field[j][i] == c) {
                    if(++count == WIN_COUNT) return true;
                    continue;
                }
                count = 0;
            }
        }
        return false;
    }

    //Проверка горизонтали
    private static boolean isHorizonCheckWin(char c) {
        int count = 0;
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                if (field[i][j] == c) {
                    if(++count == WIN_COUNT) return true;
                    continue;
                }
                count = 0;
            }
        }
        return false;
    }

    /**
     * Проверка на ничью
     * @return
     */
    static boolean checkDraw(){
        for (int x = 0; x < fieldSizeX; x++){
            for (int y = 0; y < fieldSizeY; y++)
                if (isCellEmpty(x, y)) return false;
        }
        return true;
    }

    /**
     * Метод проверки состояния игры
     * @param c
     * @param str
     * @return
     */
    static boolean gameCheck(char c, String str){
        if (checkWin(c)){
            System.out.println(str);
            return true;
        }
        if (checkDraw()){
            System.out.println("Ничья!");
            return true;
        }
        return false; // Игра продолжается

    }
}