package visualizador.aplicacao;

import controlador.entidades.xadrez.Cor;
import controlador.entidades.xadrez.PartidaXadrez;
import controlador.entidades.xadrez.PecaXadrez;
import controlador.entidades.xadrez.PosicaoXadrez;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UI {
    // https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    // https://stackoverflow.com/questions/2979383/java-clear-the-console
    public static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * @param scanner
     * @return A posicao no xadrez
     * <p>Valores validos sao somente de <b>a1 ate h8</b></p>
     */
    public static PosicaoXadrez lerPosicaoXadrez(Scanner scanner) {
        try {
            String s = scanner.nextLine();
            char coluna = s.charAt(0);
            int linha = Integer.parseInt(s.substring(1));
            return new PosicaoXadrez(coluna, linha);
        } catch (RuntimeException e) {
            throw new InputMismatchException("Valores validos sao de a1 ate h8");
        }
    }

    public static void printPartida(PartidaXadrez partidaXadrez, List<PecaXadrez> capturadas) {
        printTabuleiro(partidaXadrez.getPecas());
        System.out.println();
        printPecasCapturadas(capturadas);
        System.out.println();
        System.out.println("Turno: " + partidaXadrez.getTurno());
        if (!partidaXadrez.getXequeMate()) {
            System.out.println("Esperando jogador: " + partidaXadrez.getPlayerAtual());
            if (partidaXadrez.getXeque()) {
                System.out.println("XEQUE!");
            }
        } else {
            System.out.println("XEQUEMATE!");
            System.out.println("Vencedor: " + partidaXadrez.getPlayerAtual());
        }
    }

    public static void printTabuleiro(PecaXadrez[][] pecaXadrez) {
        for (int i = 0; i < pecaXadrez.length; i++) {
            System.out.print((8 - i) + " ");
            for (int j = 0; j < pecaXadrez.length; j++) {
                printPeca(pecaXadrez[i][j], false);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    /**
     * @param pecaXadrez
     * @param possiveisMovimentos <p>Colore a tela com os possiveis movimentos da peça</p>
     *                            <p>
     *                            OBS: Metodo sobrecarergado
     */
    public static void printTabuleiro(PecaXadrez[][] pecaXadrez, boolean[][] possiveisMovimentos) {
        for (int i = 0; i < pecaXadrez.length; i++) {
            System.out.print((8 - i) + " ");
            for (int j = 0; j < pecaXadrez.length; j++) {
                printPeca(pecaXadrez[i][j], possiveisMovimentos[i][j]);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    /**
     * @param peca
     * @param fundo Possiveis movimentos
     *              <p>A peça de acordo com a sua cor</p>
     *              Colore o fundo de acordo com os possiveis movimentos
     */
    private static void printPeca(PecaXadrez peca, boolean fundo) {
        if (fundo) {
            System.out.print(ANSI_BLUE_BACKGROUND);
        }
        if (peca == null) {
            System.out.print("-" + ANSI_RESET);
        } else {
            if (peca.getCor() == Cor.BRANCO) {
                System.out.print(ANSI_WHITE + peca + ANSI_RESET);
            } else {
                System.out.print(ANSI_YELLOW + peca + ANSI_RESET);
            }
        }
        System.out.print(" ");
    }

    /**
     * @param capturadas <p>Imprime na tela a lista de peças capturadas</p>
     */
    private static void printPecasCapturadas(List<PecaXadrez> capturadas) {
        List<PecaXadrez> branca = capturadas.stream().filter(x -> x.getCor() == Cor.BRANCO).toList();
        List<PecaXadrez> preta = capturadas.stream().filter(x -> x.getCor() == Cor.PRETO).toList();
        System.out.println("Pecas capturadas: ");
        System.out.print("Brancas: ");
        System.out.print(ANSI_WHITE);
        System.out.println(Arrays.toString(branca.toArray()));
        System.out.print(ANSI_RESET);
        System.out.print("Pretas: ");
        System.out.print(ANSI_YELLOW);
        System.out.println(Arrays.toString(preta.toArray()));
        System.out.print(ANSI_RESET);
    }
}