package controlador.entidades.xadrez.pecas;

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
        return aux;
    }
}