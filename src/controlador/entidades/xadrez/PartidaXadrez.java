package controlador.entidades.xadrez;

import controlador.entidades.tabuleiro.Peca;
import controlador.entidades.tabuleiro.Posicao;
import controlador.entidades.tabuleiro.Tabuleiro;
import controlador.entidades.xadrez.excecoes.XadrezExcecoes;
import controlador.entidades.xadrez.pecas.Rei;
import controlador.entidades.xadrez.pecas.Torre;

public class PartidaXadrez {

    private Tabuleiro tabuleiro;
    private int turno;
    private Cor playerAtual;

    public PartidaXadrez() {
        tabuleiro = new Tabuleiro(8, 8);
        turno = 1;
        playerAtual = Cor.BRANCO;
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

    public int getTurno() {
        return turno;
    }

    public Cor getPlayerAtual() {
        return playerAtual;
    }

    /**
     * @param posicaoOrigem
     * @return <p>Uma matriz de booleano para onde a peça pode se mover</p>
     * Onde verdadeiro se <b>a peça pode se mover</b> e falso caso o contrario
     */
    public boolean[][] possiveisMovimentos(PosicaoXadrez posicaoOrigem) {
        Posicao posicao = posicaoOrigem.toPosicao();
        validarPosicaoDeOrigem(posicao);
        return tabuleiro.peca(posicao).possiveisMovimentos();
    }

    /**
     * @param posicaoDeOrigem
     * @param posicaoDeDestino
     * @return a peca capturada da posicao de destino passada
     * <p>Não é possivel mover uma peca se na posicao de origem <b>não haver uma peça</b></p>
     */
    public PecaXadrez executaMovePeca(PosicaoXadrez posicaoDeOrigem, PosicaoXadrez posicaoDeDestino) {
        Posicao origem = posicaoDeOrigem.toPosicao();
        Posicao destino = posicaoDeDestino.toPosicao();
        validarPosicaoDeOrigem(origem);
        validarPosicaoDeDestino(origem, destino);
        Peca pecaCapturada = fazerMovimento(origem, destino);
        proximoTurno();
        return (PecaXadrez) pecaCapturada;
    }

    private void validarPosicaoDeOrigem(Posicao posicao) {
        if (!tabuleiro.temUmaPeca(posicao)) {
            throw new XadrezExcecoes("Nao existe peca na posicao de origem");
        }
        if (playerAtual != ((PecaXadrez) tabuleiro.peca(posicao)).getCor()) {
            throw new XadrezExcecoes("A peca escolhida nao e sua");
        }
        if (!tabuleiro.peca(posicao).existeAlgumPossivelMovimento()) {
            throw new XadrezExcecoes("Nao existe movimentos possiveis para a peca escolhida");
        }
    }

    private void validarPosicaoDeDestino(Posicao origem, Posicao destino) {
        if (!tabuleiro.peca(origem).possivelMover(destino)) {
            throw new XadrezExcecoes("A peca escolhida nao pode se mover para a posicao de destino");
        }
    }

    private void proximoTurno() {
        turno++;
        playerAtual = (playerAtual == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
    }

    /**
     * @param origem
     * @param destino
     * @return A peça capturada na posicao de destino
     */
    private Peca fazerMovimento(Posicao origem, Posicao destino) {
        Peca p = tabuleiro.removePeca(origem);
        Peca pecaCapturada = tabuleiro.removePeca(destino);
        tabuleiro.colocaPeca(p, destino);
        return pecaCapturada;
    }

    private void colocarNovaPeca(char coluna, int linha, PecaXadrez pecaXadrez) {
        tabuleiro.colocaPeca(pecaXadrez, new PosicaoXadrez(coluna, linha).toPosicao());
    }

    private void iniciarSetup() {
        colocarNovaPeca('c', 1, new Torre(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('c', 2, new Rei(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('d', 2, new Rei(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('e', 2, new Torre(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('e', 1, new Torre(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('d', 1, new Rei(tabuleiro, Cor.BRANCO));

        colocarNovaPeca('c', 7, new Torre(tabuleiro, Cor.PRETO));
        colocarNovaPeca('c', 8, new Torre(tabuleiro, Cor.PRETO));
        colocarNovaPeca('d', 7, new Torre(tabuleiro, Cor.PRETO));
        colocarNovaPeca('e', 7, new Rei(tabuleiro, Cor.PRETO));
        colocarNovaPeca('e', 8, new Torre(tabuleiro, Cor.PRETO));
        colocarNovaPeca('d', 8, new Torre(tabuleiro, Cor.PRETO));
    }
}