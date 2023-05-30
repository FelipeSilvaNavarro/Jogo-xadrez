package controlador.entidades.xadrez;

import controlador.entidades.tabuleiro.Posicao;
import controlador.entidades.xadrez.excecoes.XadrezExcecoes;

public class PosicaoXadrez {
    private int coluna;
    private int linha;

    /**
     * @param coluna
     * @param linha  <p>Instancia uma posicao no xadrez</p>
     *               Não é possivel com ao menos 1 linha e coluna
     */
    public PosicaoXadrez(int coluna, int linha) {
        if (coluna < 'a' || coluna > 'h' || linha < 1 || linha > 8) {
            throw new XadrezExcecoes("Valores validao: a1 a h8");
        }
        this.coluna = coluna;
        this.linha = linha;
    }

    public int getColuna() {
        return coluna;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    /**
     * @return
     * A posicao em matriz da posicao no tabueliro de xadrez
     */
    protected Posicao toPosicao() {
        return new Posicao(8 - linha, coluna - 'a');
    }

    /**
     * @return
     * A posicao em tabuleiro de xadrez da matriz passada
     */
    protected static PosicaoXadrez fromPosicao(Posicao posicao) {
        return new PosicaoXadrez((char)('a' - posicao.getColuna()), 8 - posicao.getLinha());
    }

    @Override
    public String toString() {
        return String.valueOf(coluna) + linha;
    }
}