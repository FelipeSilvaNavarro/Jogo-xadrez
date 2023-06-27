package controlador.entidades.xadrez;

import controlador.entidades.tabuleiro.Peca;
import controlador.entidades.tabuleiro.Posicao;
import controlador.entidades.tabuleiro.Tabuleiro;
import controlador.entidades.xadrez.excecoes.XadrezExcecoes;
import controlador.entidades.xadrez.pecas.*;

import java.util.ArrayList;
import java.util.List;

public class PartidaXadrez {

    private Tabuleiro tabuleiro;
    private int turno;
    private Cor playerAtual;
    private boolean xeque;
    private boolean xequeMate;
    private PecaXadrez enPassantVulneravel;
    private PecaXadrez promocao;
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

    public PecaXadrez getEnPassantVulneravel() {
        return enPassantVulneravel;
    }

    public PecaXadrez getPromocao() {
        return promocao;
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
            throw new XadrezExcecoes("Voce nao pode se colocar em XEQUE");
        }

        PecaXadrez pecaMovida = (PecaXadrez) tabuleiro.peca(destino);
        // Movimento especial PROMOÇÃO
        promocao = null;
        if (pecaMovida instanceof Peao) {
            if (pecaMovida.getCor() == Cor.BRANCO && destino.getLinha() == 0 || pecaMovida.getCor() == Cor.PRETO && destino.getLinha() == 7) {
                promocao = (PecaXadrez) tabuleiro.peca(destino);
                promocao = substituirPecaPromovida("r");
            }
        }
        xeque = (testXeque(oponente(playerAtual))) ? true : false;

        if (testXequeMate(oponente(playerAtual))) {
            xequeMate = true;
        } else {
            proximoTurno();
        }

        // Movimento especial ENPASSANT

        if (pecaMovida instanceof Peao && (destino.getLinha() == origem.getLinha() - 2 || destino.getLinha() == origem.getLinha() + 2)) {
            enPassantVulneravel = pecaMovida;
        } else {
            enPassantVulneravel = null;
        }

        return (PecaXadrez) pecaCapturada;
    }

    public PecaXadrez substituirPecaPromovida(String tipo) {
        if (promocao == null) {
            throw new IllegalStateException("Nao ha peca para ser promovida");
        }
        if (!tipo.equals("B") && !tipo.equals("C") && !tipo.equals("r") && !tipo.equals("T")) {
            return promocao;
        }

        Posicao pos = promocao.getPosicaoXadrez().toPosicao();
        Peca p = tabuleiro.removePeca(pos);
        pecasNoTabuleiro.remove(p);

        PecaXadrez novaPeca = novaPeca(tipo, promocao.getCor());
        tabuleiro.colocaPeca(novaPeca, pos);
        pecasNoTabuleiro.add(novaPeca);

        return novaPeca;
    }

    private PecaXadrez novaPeca(String tipo, Cor cor) {
        if (tipo.equals("B")) return new Bispo(tabuleiro, cor);
        if (tipo.equals("C")) return new Cavalo(tabuleiro, cor);
        if (tipo.equals("r")) return new Rainha(tabuleiro, cor);
        return new Torre(tabuleiro, cor);
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
        PecaXadrez p = (PecaXadrez) tabuleiro.removePeca(origem);
        p.addContadorDeMovimentos();
        Peca pecaCapturada = tabuleiro.removePeca(destino);
        tabuleiro.colocaPeca(p, destino);

        if (pecaCapturada != null) {
            pecasNoTabuleiro.remove(pecaCapturada);
            pecasCapturadas.add(pecaCapturada);
        }

        // Movimento especial ROQUE do lado do REI
        if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
            Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() + 3);
            Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() + 1);
            PecaXadrez torre = (PecaXadrez) tabuleiro.removePeca(origemTorre);
            tabuleiro.colocaPeca(torre, destinoTorre);
            torre.addContadorDeMovimentos();
        }
        // Movimento especial ROQUE do lado da RAINHA
        if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
            Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() - 4);
            Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() - 1);
            PecaXadrez torre = (PecaXadrez) tabuleiro.removePeca(origemTorre);
            tabuleiro.colocaPeca(torre, destinoTorre);
            torre.addContadorDeMovimentos();
        }

        // Movimento especial ENPASSANT
        if (p instanceof Peao) {
            if (origem.getColuna() != destino.getColuna() && pecaCapturada == null) {
                Posicao posicaoPeao;
                if (p.getCor() == Cor.BRANCO) {
                    posicaoPeao = new Posicao(destino.getLinha() + 1, destino.getColuna());
                } else {
                    posicaoPeao = new Posicao(destino.getLinha() - 1, destino.getColuna());
                }
                pecaCapturada = tabuleiro.removePeca(posicaoPeao);
                pecasCapturadas.add(pecaCapturada);
                pecasNoTabuleiro.remove(pecaCapturada);
            }
        }

        return pecaCapturada;
    }

    private void desfazerMovimento(Posicao origem, Posicao destino, Peca pecaCapturada) {
        PecaXadrez p = (PecaXadrez) tabuleiro.removePeca(destino);
        p.remContadorDeMovimentos();
        tabuleiro.colocaPeca(p, origem);

        if (pecaCapturada != null) {
            tabuleiro.colocaPeca(pecaCapturada, destino);
            pecasCapturadas.remove(pecaCapturada);
            pecasNoTabuleiro.add(pecaCapturada);
        }
        // Movimento especial ROQUE do lado do REI
        if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
            Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() + 3);
            Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() + 1);
            PecaXadrez torre = (PecaXadrez) tabuleiro.removePeca(destinoTorre);
            tabuleiro.colocaPeca(torre, origemTorre);
            torre.remContadorDeMovimentos();
        }
        // Movimento especial ROQUE do lado da RAINHA
        if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
            Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() - 4);
            Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() - 1);
            PecaXadrez torre = (PecaXadrez) tabuleiro.removePeca(destinoTorre);
            tabuleiro.colocaPeca(torre, origemTorre);
            torre.remContadorDeMovimentos();
        }
        // Movimento especial ENPASSANT
        if (p instanceof Peao) {
            if (origem.getColuna() != destino.getColuna() && pecaCapturada == enPassantVulneravel) {
                PecaXadrez peao = (PecaXadrez) tabuleiro.removePeca(destino);
                Posicao posicaoPeao;
                if (p.getCor() == Cor.BRANCO) {
                    posicaoPeao = new Posicao(3, destino.getColuna());
                } else {
                    posicaoPeao = new Posicao(4, destino.getColuna());
                }
                tabuleiro.colocaPeca(peao, posicaoPeao);
            }
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
        for (Peca p : list) {
            if (p instanceof Rei) {
                return (PecaXadrez) p;
            }
        }
        throw new IllegalStateException("Nao existe rei da cor " + cor + " no tabuleiro");
    }

    /**
     * @param cor
     * @return Verdadeiro se <b>alguma peça oponente pode se movimentar para o rei</b> e falso caso contrario
     * <p>Varre todos os movimentos possiveis das peças e verifica se o rei da cor passada esta em xeque</p>
     */
    private boolean testXeque(Cor cor) {
        Posicao posicaoRei = rei(cor).getPosicaoXadrez().toPosicao();
        List<Peca> pecasOponente = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == oponente(cor)).toList();
        for (Peca p : pecasOponente) {
            boolean[][] mat = p.possiveisMovimentos();
            if (mat[posicaoRei.getLinha()][posicaoRei.getColuna()]) {
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
        colocarNovaPeca('a', 1, new Torre(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('b', 1, new Cavalo(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('c', 1, new Bispo(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('d', 1, new Rainha(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('f', 1, new Bispo(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('g', 1, new Cavalo(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('h', 1, new Torre(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('a', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('b', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('c', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('d', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('e', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('f', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('g', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('h', 2, new Peao(tabuleiro, Cor.BRANCO, this));

        colocarNovaPeca('a', 8, new Torre(tabuleiro, Cor.PRETO));
        colocarNovaPeca('b', 8, new Cavalo(tabuleiro, Cor.PRETO));
        colocarNovaPeca('c', 8, new Bispo(tabuleiro, Cor.PRETO));
        colocarNovaPeca('d', 8, new Rainha(tabuleiro, Cor.PRETO));
        colocarNovaPeca('e', 8, new Rei(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('f', 8, new Bispo(tabuleiro, Cor.PRETO));
        colocarNovaPeca('g', 8, new Cavalo(tabuleiro, Cor.PRETO));
        colocarNovaPeca('h', 8, new Torre(tabuleiro, Cor.PRETO));
        colocarNovaPeca('a', 7, new Peao(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('b', 7, new Peao(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('c', 7, new Peao(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('d', 7, new Peao(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('e', 7, new Peao(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('f', 7, new Peao(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('g', 7, new Peao(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('h', 7, new Peao(tabuleiro, Cor.PRETO, this));
    }
}