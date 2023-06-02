package visualizador.aplicacao;

import controlador.entidades.xadrez.PartidaXadrez;
import controlador.entidades.xadrez.PecaXadrez;
import controlador.entidades.xadrez.PosicaoXadrez;

import java.util.Scanner;

public class Programa {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        PartidaXadrez partidaXadrez = new PartidaXadrez();
        while (true) {
            UI.printTabuleiro(partidaXadrez.getPecas());
            System.out.println();
            System.out.print("Origem: ");
            PosicaoXadrez origem = UI.lerPosicaoXadrez(scanner);

            System.out.println();
            System.out.print("Destino: ");
            PosicaoXadrez destino = UI.lerPosicaoXadrez(scanner);

            PecaXadrez pecaCapturada = partidaXadrez.executaMovePeca(origem, destino);
        }
    }
}