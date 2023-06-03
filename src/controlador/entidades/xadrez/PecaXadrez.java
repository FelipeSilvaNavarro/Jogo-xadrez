package controlador.entidades.xadrez;

import controlador.entidades.tabuleiro.Peca;
import controlador.entidades.tabuleiro.Posicao;
import controlador.entidades.tabuleiro.Tabuleiro;

public abstract class PecaXadrez extends Peca {
    private Cor cor;

    public PecaXadrez(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro);
        this.cor = cor;
    }

    public Cor getCor() {
        return cor;
    }

    /**
     * @param posicao
     * @return Se existe peça adversaria na posição passada
     */
    protected boolean existeAlgumaPecaOponente(Posicao posicao) {
        PecaXadrez p = (PecaXadrez) getTabuleiro().peca(posicao);
        return p != null && p.getCor() != cor;
    }

}