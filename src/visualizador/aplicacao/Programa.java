package visualizador.aplicacao;

import controlador.entidades.xadrez.PartidaXadrez;

public class Programa {

    public static void main(String[] args) {

        PartidaXadrez partidaXadrez = new PartidaXadrez();
        UI.printBoard(partidaXadrez.getPecas());
    }
}