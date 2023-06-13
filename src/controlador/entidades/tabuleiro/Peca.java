package controlador.entidades.tabuleiro;

public abstract class Peca {
    protected Posicao posicao;
    private Tabuleiro tabuleiro;

    public Peca(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
    }

    protected Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public abstract boolean[][] possiveisMovimentos();

    /**
     * @param posicao
     * @return Se é possivel movimento para a posição passada
     */
    public boolean possivelMover(Posicao posicao) {
        return possiveisMovimentos()[posicao.getLinha()][posicao.getColuna()];
    }

    /**
     * @return Se é possivel a peça se mover para algum lugar, caso não, a peça está travada
     */
    public boolean existeAlgumPossivelMovimento() {
        boolean[][] mat = possiveisMovimentos();
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                if (mat[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }
}