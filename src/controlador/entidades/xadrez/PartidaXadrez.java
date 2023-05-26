package controlador.entidades.xadrez;

import controlador.entidades.tabuleiro.Posicao;
import controlador.entidades.tabuleiro.Tabuleiro;
import controlador.entidades.xadrez.pecas.Rei;
import controlador.entidades.xadrez.pecas.Torre;

public class PartidaXadrez {

    private Tabuleiro tabuleiro;

    public PartidaXadrez() {
        tabuleiro = new Tabuleiro(8, 8);
        iniciarSetup();
    }

    /**
     * @return Retorna uma matriz de <b>peca de xadrez</b>
     */
    public PecaXadrez[][] getPecas() {
        PecaXadrez[][] mat = new PecaXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
        for (int i = 0; i < tabuleiro.getLinhas(); i++) {
            for (int j = 0; j < tabuleiro.getColunas(); j++) {
                mat[i][j] = (PecaXadrez) tabuleiro.peca(i, j);
            }
        }
        return mat;
    }

    private void iniciarSetup() {
        tabuleiro.colocaPeca(new Torre(tabuleiro, Cor.BRANCO), new Posicao(2, 1));
        tabuleiro.colocaPeca(new Rei(tabuleiro,Cor.PRETO), new Posicao(0, 4));
        tabuleiro.colocaPeca(new Rei(tabuleiro, Cor.PRETO), new Posicao(7, 4));
    }
}
