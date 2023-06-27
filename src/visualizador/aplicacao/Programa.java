package visualizador.aplicacao;

import controlador.entidades.xadrez.PartidaXadrez;
import controlador.entidades.xadrez.PecaXadrez;
import controlador.entidades.xadrez.PosicaoXadrez;
import controlador.entidades.xadrez.excecoes.XadrezExcecoes;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Programa {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        PartidaXadrez partidaXadrez = new PartidaXadrez();
        List<PecaXadrez> capturadas = new ArrayList<>();
        while (!partidaXadrez.getXequeMate()) {
            try {
                UI.limparTela();
                UI.printPartida(partidaXadrez, capturadas);
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

                if (pecaCapturada != null) {
                    capturadas.add(pecaCapturada);
                }
                if (partidaXadrez.getPromocao() != null) {
                    System.out.print("Digite a letra para promocao (B/C/r/T): ");
                    String tipo = scanner.nextLine().toUpperCase();
                    while (!tipo.equals("B") && !tipo.equals("C") && !tipo.equals("r") && !tipo.equals("T")) {
                        System.out.print("Valor invalido! Digite a letra para promocao (B/C/r/T): ");
                        tipo = scanner.nextLine().toUpperCase();
                    }
                    partidaXadrez.substituirPecaPromovida(tipo);
                }
            } catch (XadrezExcecoes | InputMismatchException e) {
                System.out.println(e.getMessage());
                scanner.nextLine();
            }
        }
        UI.limparTela();
        UI.printPartida(partidaXadrez, capturadas);
    }
}