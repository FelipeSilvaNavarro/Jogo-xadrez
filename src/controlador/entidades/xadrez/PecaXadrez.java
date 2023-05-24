package controlador.entidades.xadrez;

import controlador.entidades.tabuleiro.Peca;
import controlador.entidades.tabuleiro.Tabuleiro;

public class PecaXadrez extends Peca {
    private Cor cor;

    public PecaXadrez(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro);
        this.cor = cor;
    }

    public Cor getCor() {
        return cor;
    }

}
