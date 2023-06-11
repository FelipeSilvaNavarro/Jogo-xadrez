package controlador.entidades.xadrez;

import controlador.entidades.tabuleiro.Peca;
import controlador.entidades.tabuleiro.Posicao;
import controlador.entidades.tabuleiro.Tabuleiro;
import controlador.entidades.xadrez.excecoes.XadrezExcecoes;
import controlador.entidades.xadrez.pecas.Rei;
import controlador.entidades.xadrez.pecas.Torre;

import java.util.ArrayList;
import java.util.List;

public class PartidaXadrez {

    private Tabuleiro tabuleiro;
    private int turno;
    private Cor playerAtual;
    private boolean xeque;
    private boolean xequeMate;
    private List<Peca> pecasNoTabuleiro = new ArrayList<>();
    private List<Peca> pecasCapturadas = new ArrayList<>();

    public PartidaXadrez() {
        tabuleiro = new Tabuleiro(8, 8);
        turno = 1;
        playerAtual = Cor.BRANCO;
        iniciarSetup();
    }

    public boolean getXeque() {
        return xeque;
    }

    public boolean getXequeMate() {
        return xequeMate;
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
        if (testXeque(playerAtual)) {
            desfazerMovimento(origem, destino, pecaCapturada);
            throw new XadrezExcecoes("Voce nao pode se colocar em xeque");
        }
        xeque = testXeque(oponente(playerAtual));
        if (testXeque(oponente(playerAtual))) {
            xequeMate = true;
        } else {
            proximoTurno();
        }
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

        if (pecaCapturada != null) {
            pecasNoTabuleiro.remove(pecaCapturada);
            pecasCapturadas.add(pecaCapturada);
        }
        return pecaCapturada;
    }

    private void desfazerMovimento(Posicao origem, Posicao destino, Peca pecaCapturada) {
        Peca p = tabuleiro.removePeca(destino);
        tabuleiro.colocaPeca(p, origem);

        if (pecaCapturada != null) {
            tabuleiro.colocaPeca(pecaCapturada, destino);
            pecasCapturadas.remove(pecaCapturada);
            pecasNoTabuleiro.add(pecaCapturada);
        }
    }

    private void colocarNovaPeca(char coluna, int linha, PecaXadrez peca) {
        tabuleiro.colocaPeca(peca, new PosicaoXadrez(coluna, linha).toPosicao());
        pecasNoTabuleiro.add(peca);
    }

    /**
     * @param cor
     * @return O oponente de uma cor
     * <p>Se a cor for preta, o oponente é branco</p>
     */
    private Cor oponente(Cor cor) {
        return (cor == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
    }

    /**
     * @param cor
     * @return Varre a lista de peças em jogo e procura o rei da cor passada como parametro
     */
    private PecaXadrez rei(Cor cor) {
        List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == cor).toList();
        for (Peca p : list
        ) {
            if (p instanceof Rei) {
                return (PecaXadrez) p;
            }
        }
        throw new IllegalStateException("Nao existe o rei da cor " + cor + " no tabuleiro");
    }

    /**
     * @param cor
     * @return Verdadeiro se <b>alguma peça oponente pode se movimentar para o rei</b> e falso caso contrario
     * <p>Varre todos os movimentos possiveis das peças e verifica se o rei da cor passada esta em xeque</p>
     */
    private boolean testXeque(Cor cor) {
        Posicao posicaoDoRei = rei(cor).getPosicaoXadrez().toPosicao();
        List<Peca> pecasOponentes = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == oponente(cor)).toList();
        for (Peca p : pecasOponentes
        ) {
            boolean[][] mat = p.possiveisMovimentos();
            if (mat[posicaoDoRei.getLinha()][posicaoDoRei.getColuna()]) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param cor
     * @return Verdadeiro se <b>não tiver nenhuma peça da cor passada como parametro que tire o rei do xeque</b> e falso caso contrario
     * * <p>Move a peça e verifica se o rei da cor passada ainda está em xequemate</p>
     */
    public boolean testXequeMate(Cor cor) {
        if (!testXeque(cor)) {
            return false;
        }
        List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == cor).toList();
        for (Peca p : list
        ) {
            boolean[][] mat = p.possiveisMovimentos();
            for (int i = 0; i < tabuleiro.getLinhas(); i++) {
                for (int j = 0; j < tabuleiro.getColunas(); j++) {
                    if (mat[i][j]) {
                        Posicao origem = ((PecaXadrez) p).getPosicaoXadrez().toPosicao();
                        Posicao destino = new Posicao(i, j);
                        Peca pecaCapturada = fazerMovimento(origem, destino);
                        boolean testXeque = testXeque(cor);
                        desfazerMovimento(origem, destino, pecaCapturada);
                        if (!testXeque) {
                            return false;
                        }
                    }
                }

            }
        }
        return true;
    }

    private void iniciarSetup() {
        colocarNovaPeca('h', 7, new Torre(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('d', 1, new Torre(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCO));

        colocarNovaPeca('b', 8, new Torre(tabuleiro, Cor.PRETO));
        colocarNovaPeca('a', 8, new Rei(tabuleiro, Cor.PRETO));
    }
}