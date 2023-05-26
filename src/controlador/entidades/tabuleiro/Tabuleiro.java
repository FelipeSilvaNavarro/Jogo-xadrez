package controlador.entidades.tabuleiro;

import controlador.entidades.tabuleiro.excecoes.TabuleiroExcecoes;

public class Tabuleiro {
    private int linhas;
    private int colunas;
    private Peca[][] pecas;

    /**
     * @param linhas
     * @param colunas
     *  <p>Cria um novo tabuleiro</p>
     *  Não é possivel criar um tabuleiro com ao menos 1 linha e coluna
     */
    public Tabuleiro(int linhas, int colunas) {
        if (linhas < 1 || colunas < 1) {
            throw new TabuleiroExcecoes("Erro ao criar o tabuleiro: Necessario ao menos 1 linha e coluna");
        }
        this.linhas = linhas;
        this.colunas = colunas;
        pecas = new Peca[linhas][colunas];
    }

    public int getLinhas() {
        return linhas;
    }

    public int getColunas() {
        return colunas;
    }

    /**
     * @param linha
     * @param coluna
     * @return A matriz de peça na linha e coluna passada
     *
     * Não é possivel buscar uma peca que não está na linha e coluna passada
     */
    public Peca peca(int linha, int coluna) {
        if(!posicaoExiste(linha, coluna)) {
            throw new TabuleiroExcecoes("Posicao nao esta no tabuleiro");
        }
        return pecas[linha][coluna];
    }

    /**
     * @param posicao
     * @return A peça que estiver na matriz de peças na posição passada
     *  OBS: Metodo sobrecarergado
     *  <p>Não é possivel buscar uma peca que não está na posicao passada</p>
     */
    public Peca peca(Posicao posicao) {
        if(!posicaoExiste(posicao)) {
            throw new TabuleiroExcecoes("Posicao nao esta no tabuleiro");
        }
        return pecas[posicao.getLinha()][posicao.getColuna()];
    }

    /**
     * @param peca
     * @param posicao
     * <p>Coloca uma peça no tabuleiro na posição passada</p>
     *
     * Não é posivel colocar uma peça em uma posição que já contem uma peça
     *
     */
    public void colocaPeca(Peca peca, Posicao posicao) {
        if (temUmaPeca(posicao)) {
            throw new TabuleiroExcecoes("Ja existe peca na posicao " + posicao);
        }
        pecas[posicao.getLinha()][posicao.getColuna()] = peca;
        peca.posicao = posicao;
    }

    /**
     * @param linha
     * @param coluna
     * @return Verdadeiro se uma posicao <b>existe no tabuleiro</b>, false caso <b>não exista</b>
     */
    private boolean posicaoExiste(int linha, int coluna) {
        return linha >= 0 && linha < linhas && coluna >= 0 && coluna < colunas;
    }

    /**
     * @param posicao
     * @return Verdadeiro se uma posicao posicao <b>existe no tabuleiro</b>, falso caso <b>não exista</b>
     */
    public boolean posicaoExiste(Posicao posicao) {
        return posicaoExiste(posicao.getLinha(), posicao.getColuna());
    }

    /**
     * @param posicao
     * @return Verdadeiro se tiver <b>uma peça</b> na posição passada, falso caso <b>não tenha</b>
     *
     * Não é possivel verificar em uma posição que não existe no tabuleiro
     */
    public boolean temUmaPeca(Posicao posicao) {
        if (!posicaoExiste(posicao)) {
            throw new TabuleiroExcecoes("Posicao nao existe no tabuleiro");
        }
        return peca(posicao) != null;
    }
}
