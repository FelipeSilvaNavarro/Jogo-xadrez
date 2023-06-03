package controlador.entidades.xadrez.pecas;

import controlador.entidades.tabuleiro.Posicao;
import controlador.entidades.tabuleiro.Tabuleiro;
import controlador.entidades.xadrez.Cor;
import controlador.entidades.xadrez.PecaXadrez;

public class Torre extends PecaXadrez {
    public Torre(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro, cor);
    }

    @Override
    public String toString() {
        return "T";
    }

    @Override
    public boolean[][] possiveisMovimentos() {
        boolean[][] aux = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
        Posicao p = new Posicao(0, 0);

        // acima
        p.setValores(posicao.getLinha() - 1, posicao.getColuna());
        while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p)) {
            aux[p.getLinha()][p.getColuna()] = true;
            p.setLinha(p.getLinha() - 1);
        }
        if (getTabuleiro().posicaoExiste(p) && existeAlgumaPecaOponente(p)) {
            aux[p.getLinha()][p.getColuna()] = true;
        }

        // esquerda
        p.setValores(posicao.getLinha(), posicao.getColuna() - 1);
        while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p)) {
            aux[p.getLinha()][p.getColuna()] = true;
            p.setColuna(p.getColuna() - 1);
        }
        if (getTabuleiro().posicaoExiste(p) && existeAlgumaPecaOponente(p)) {
            aux[p.getLinha()][p.getColuna()] = true;
        }
        // direita
        p.setValores(posicao.getLinha(), posicao.getColuna() + 1);
        while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p)) {
            aux[p.getLinha()][p.getColuna()] = true;
            p.setColuna(p.getColuna() + 1);
        }
        if (getTabuleiro().posicaoExiste(p) && existeAlgumaPecaOponente(p)) {
            aux[p.getLinha()][p.getColuna()] = true;
        }
        // abaixo
        p.setValores(posicao.getLinha() + 1, posicao.getColuna());
        while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p)) {
            aux[p.getLinha()][p.getColuna()] = true;
            p.setLinha(p.getLinha() + 1);
        }
        if (getTabuleiro().posicaoExiste(p) && existeAlgumaPecaOponente(p)) {
            aux[p.getLinha()][p.getColuna()] = true;
        }
        return aux;
    }
}