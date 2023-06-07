package visualizador.aplicacao;

import controlador.entidades.xadrez.PartidaXadrez;
import controlador.entidades.xadrez.PecaXadrez;
import controlador.entidades.xadrez.PosicaoXadrez;
import controlador.entidades.xadrez.excecoes.XadrezExcecoes;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Programa {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        PartidaXadrez partidaXadrez = new PartidaXadrez();
        while (true) {
            try {
                UI.limparTela();
                UI.printPartida(partidaXadrez);
                System.out.println();
                System.out.print("Origem: ");
                PosicaoXadrez origem = UI.lerPosicaoXadrez(scanner);

                boolean[][] possiveisMovimentos = partidaXadrez.possiveisMovimentos(origem);
                UI.limparTela();
                UI.printTabuleiro(partidaXadrez.getPecas(), possiveisMovimentos);

                System.out.println();
                System.out.print("Destino: ");
                PosicaoXadrez destino = UI.lerPosicaoXadrez(scanner);

                PecaXadrez pecaCapturada = partidaXadrez.executaMovePeca(origem, destino);
            } catch (XadrezExcecoes | InputMismatchException e) {
                System.out.println(e.getMessage());
                scanner.nextLine();
            }
        }
    }
}