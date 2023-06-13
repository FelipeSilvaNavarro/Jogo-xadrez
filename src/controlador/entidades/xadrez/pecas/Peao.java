package controlador.entidades.xadrez.pecas;

import controlador.entidades.tabuleiro.Posicao;
import controlador.entidades.tabuleiro.Tabuleiro;
import controlador.entidades.xadrez.Cor;
import controlador.entidades.xadrez.PecaXadrez;

public class Peao extends PecaXadrez {
    public Peao(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro, cor);
    }

    @Override
    public boolean[][] possiveisMovimentos() {
        boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

        Posicao p = new Posicao(0, 0);

        if (getCor() == Cor.BRANCO) {
            p.setValores(posicao.getLinha() - 1, posicao.getColuna());
            if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.setValores(posicao.getLinha() - 2, posicao.getColuna());
            Posicao p2 = new Posicao(posicao.getLinha() - 1, posicao.getColuna());
            if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p) && getTabuleiro().posicaoExiste(p2) && !getTabuleiro().temUmaPeca(p2) && getContadorDeMovimentos() == 0) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.setValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
            if (getTabuleiro().posicaoExiste(p) && existeAlgumaPecaOponente(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.setValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
            if (getTabuleiro().posicaoExiste(p) && existeAlgumaPecaOponente(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
        } else {
            p.setValores(posicao.getLinha() + 1, posicao.getColuna());
            if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.setValores(posicao.getLinha() + 2, posicao.getColuna());
            Posicao p2 = new Posicao(posicao.getLinha() - 1, posicao.getColuna());
            if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p) && getTabuleiro().posicaoExiste(p2) && !getTabuleiro().temUmaPeca(p2) && getContadorDeMovimentos() == 0) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.setValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
            if (getTabuleiro().posicaoExiste(p) && existeAlgumaPecaOponente(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.setValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
            if (getTabuleiro().posicaoExiste(p) && existeAlgumaPecaOponente(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
        }
        return mat;
    }

    @Override
    public String toString() {
        return "P";
    }
}