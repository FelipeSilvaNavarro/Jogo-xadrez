package visualizador.aplicacao;

import controlador.entidades.xadrez.PecaXadrez;

public class UI {

    public static void printBoard(PecaXadrez[][] pecaXadrez) {
        for (int i = 0; i < pecaXadrez.length; i++) {
            System.out.print((8 - i) + " ");
            for (int j = 0; j < pecaXadrez.length; j++) {
                printPiece(pecaXadrez[i][j]);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    private static void printPiece(PecaXadrez pecaXadrez) {
        if (pecaXadrez == null) {
            System.out.print("-");
        } else {
            System.out.print(pecaXadrez);
        }
        System.out.print(" ");
    }
}