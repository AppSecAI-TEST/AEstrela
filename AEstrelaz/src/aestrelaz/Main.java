import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 *
 * @author Fernando E. A. de Carvalho RA: 88408
 */
class Main {

    public static void main(String[] args) {
        No no;
        short blocos[][] = new short[4][4];
        short objetivo[][] = {
            {1, 5, 9, 13},
            {2, 6, 10, 14},
            {3, 7, 11, 15},
            {4, 8, 12, 0}
        };
        No.setSolucao(objetivo);
        Scanner scan = new Scanner(System.in);
        for (short i = 0; i < 4; i++) {
            for (short j = 0; j < 4; j++) {
                blocos[i][j] = scan.nextShort();
            }
        }
        no = new No(blocos);
        no.makeHash();
        System.out.println(no.solucionar());
    }

    static class No implements Comparable<No> {

        private static String solucaoHash;

        private String hashKey;

        public static PriorityQueueMelhorada<No> listaAberta = new PriorityQueueMelhorada<No>();
        public static HashMap<String, No> fechados = new HashMap<String, No>();

        private static short solucao[][] = new short[4][4];

        public List<No> sucessores = new ArrayList<No>();

        private int h1;
        private int h2;
        private int h3;
        private int h4;
        private int h5;

        private short funcaoF;
        private int passos;
        private short modPai = 0;

        private No noPai;

        private short estado[][] = new short[4][4];

        public No(short[][] estado) {
            this.passos = 0;
            this.estado = estado;

        }

        public int calculaFuncaoHLinha() {
            return this.quintaHeuristica();
        }

        public void calculaF() {
            this.funcaoF = (short) (this.calculaFuncaoHLinha() + this.passos);
        }

        public short[][] getEstado() {
            return estado;
        }

        public void setEstado(short[][] estado) {
            this.estado = estado;
        }

        public int primeiraHeuristica() {
            int h = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (estado[i][j] != solucao[i][j]) {
                        h++;
                    }
                }
            }
            this.h1 = h;
            return this.h1;
        }

        @Override
        public boolean equals(Object obj) {
            No n = (No) obj;
            return this.hashKey.equals(n.hashKey);
        }

        public int segundaHeuristica() {
            int erradas = 0;
            for (short i = 0; i < 4; i++) {
                for (short j = 0; j < 4; j++) {
                    if (j == 3) {
                        if (i != 3) {
                            if (this.getEstado()[0][i + 1] != this.getEstado()[j][i] + 1
                                    && this.getEstado()[j][i] != 0) {
                                erradas++;
                            }
                        }
                    } else if (this.getEstado()[j + 1][i] != this.getEstado()[j][i] + 1
                            && this.getEstado()[j][i] != 0) {
                        erradas++;
                    }
                }
            }

            this.h2 = erradas;
            return h2;
        }

        public int terceiraHeuristica() {
            int h = 0;
            for (int l = 0; l < 4; l++) {
                for (int c = 0; c < 4; c++) {
                    short pos = estado[l][c];
                    if (pos == 0 || estado[l][c] == No.getSolucao()[l][c]) {
                        continue;
                    }

                    short coluna2 = (short) Math.floor(pos / 4.1);
                    short linha2 = (short) (Math.floor((pos - 1) % 4));

                    short hLinha = (short) (Math.abs(linha2 - l) + Math.abs(coluna2 - c));
                    h += hLinha;

                }
            }
            this.h3 = h;
            return h;

        }

        public int quintaHeuristica() {
            this.h5 = Math.max(this.primeiraHeuristica(), Math.max(this.segundaHeuristica(), this.terceiraHeuristica()));
            return this.h5;
        }

        public static void setSolucao(short[][] solucao) {
            String s = "";
            for (short[] matriz1 : solucao) {
                for (int j = 0; j < 4; j++) {
                    s += No.converteParaHash(matriz1[j]);
                }
            }
            No.solucaoHash = s;
            No.solucao = solucao;
        }

        public static String converteParaHash(Short s) {
            if (s < 10) {
                return String.valueOf(s);
            }
            switch (s) {
                case 10:
                    return "A";
                case 11:
                    return "B";
                case 12:
                    return "C";
                case 13:
                    return "D";
                case 14:
                    return "E";
                default:
                    return "F";
            }
        }

        public static short[][] getSolucao() {
            return solucao;
        }

        public static String getHash() {
            return solucaoHash;
        }

        public boolean solucaoCorreta(No no) {
            return no.hashKey.equals(No.solucaoHash);
        }

        public static void printaMatriz(short[][] matriz) {
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[i].length; j++) {
                    System.out.print("\t " + matriz[i][j]);
                }
                System.out.println("");
            }
            System.out.println("");
        }

        public void makeHash() {
            String s = "";
            for (short[] matriz1 : this.getEstado()) {
                for (int j = 0; j < 4; j++) {
                    s += No.converteParaHash(matriz1[j]);
                }
            }
            this.hashKey = s;
        }

        @Override
        public int compareTo(No o) {
            return this.funcaoF - o.funcaoF;
        }

        public static short[][] copy_estado(short[][] estadoOriginal) {
            short[][] resultado = new short[4][4];
            for (short i = 0; i < 4; i++) {
                System.arraycopy(estadoOriginal[i], 0, resultado[i], 0, 4);
            }
            return resultado;
        }

        public ArrayList<No> geraSucessores() {
            ArrayList<No> sucessores = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (this.estado[i][j] == 0) {
                        if (i != 0 && (this.getNoPai() == null || this.modPai != -1)) {
                            No up = new No(copy_estado(this.estado));
                            up.passos = this.passos + 1;
                            up.setNoPai(this);
                            up.modPai = 1;

                            short aux = up.estado[i][j];
                            up.estado[i][j] = up.estado[i - 1][j];
                            up.estado[i - 1][j] = aux;
                            up.makeHash();
                            this.sucessores.add(up);
                            sucessores.add(up);
                        }
                        if (i != 3 && (this.getNoPai() == null || this.modPai != 1)) {

                            No down = new No(copy_estado(this.estado));
                            down.passos = this.passos + 1;
                            down.setNoPai(this);

                            short aux = down.estado[i][j];
                            down.estado[i][j] = down.estado[i + 1][j];
                            down.estado[i + 1][j] = aux;
                            down.modPai = -1;
                            down.makeHash();
                            this.sucessores.add(down);
                            sucessores.add(down);
                        }
                        if (j != 0 && (this.getNoPai() == null || this.modPai != -2)) {
                            No left = new No(copy_estado(this.estado));
                            left.passos = this.passos + 1;
                            left.setNoPai(this);

                            short aux = left.estado[i][j];
                            left.estado[i][j] = left.estado[i][j - 1];
                            left.estado[i][j - 1] = aux;
                            left.makeHash();
                            left.modPai = 2;
                            this.sucessores.add(left);
                            sucessores.add(left);
                        }
                        if (j != 3 && (this.getNoPai() == null || this.modPai != 2)) {
                            No right = new No(copy_estado(this.estado));

                            right.passos = this.passos + 1;
                            right.setNoPai(this);

                            short aux = right.estado[i][j];
                            right.estado[i][j] = right.estado[i][j + 1];
                            right.estado[i][j + 1] = aux;
                            right.makeHash();
                            right.modPai = -2;
                            this.sucessores.add(right);
                            sucessores.add(right);
                        }
                    }
                }
            }
            return sucessores;

        }

        public int getPassos() {
            return passos;
        }

        public void setPassos(int passos) {
            this.passos = passos;
        }

        public int solucionar() {

            listaAberta.add(this);

            while (!listaAberta.isEmpty()) {

                No no = listaAberta.poll();

                if (no.getNoPai() != null && !fechados.containsKey(no.getNoPai().hashKey)) {
                    continue;
                }

                fechados.put(no.hashKey, no);

                if (no.hashKey.equals(No.solucaoHash)) {
                    return no.passos;
                }

                List<No> sucessores = no.geraSucessores();

                while (!sucessores.isEmpty()) {
                    No suc = sucessores.remove(0);
                    No noFechado = fechados.get(suc.hashKey);
                    No noAberto = listaAberta.getElement(suc, suc.getPassos());

                    if (fechados.containsKey(suc.hashKey)) {
                        if (suc.getPassos() < noFechado.getPassos()) {
                            noFechado.removeDaListaFechada();

                            suc.calculaF();
                            listaAberta.add(suc);
                        }
                    } else if (noAberto != null) {
                        if (suc.getPassos() < noAberto.getPassos()) {
                            listaAberta.remove(noAberto);
                            suc.calculaF();
                            listaAberta.add(suc);
                        }
                    } else {
                        suc.calculaF();
                        listaAberta.add(suc);
                    }
                }
            }
            return 0;
        }

        public No getNoPai() {
            return noPai;
        }

        public void setNoPai(No noPai) {
            this.noPai = noPai;
        }

        private void removeDaListaFechada() {
            for (No no : this.sucessores) {
                if (no.sucessores.size() > 0) {
                    no.removeDaListaFechada();
                } else {
                    listaAberta.remove(no);
                }
            }
            fechados.remove(this.hashKey);
        }

        private void printaHistorico() {
            if (this.getNoPai() != null) {
                this.getNoPai().printaHistorico();
                printaMatriz(this.estado);
            } else {
                printaMatriz(this.estado);
            }
        }

    }

    static class PriorityQueueMelhorada<T> extends PriorityQueue<T> {

        public T getElement(T obj, int passos) {
            Iterator<T> iter = this.iterator();
            while (iter.hasNext()) {
                T current = iter.next();
                No n = (No) current;
                if (current.equals(obj)) {
                    return current;
                } else if (n.passos >= passos) {
                    break;
                }
            }
            return null;
        }

    }

}
