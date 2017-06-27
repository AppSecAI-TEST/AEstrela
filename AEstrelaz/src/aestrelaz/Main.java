package aestrelaz;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 *
 * @author guest-tqterz
 */
class Main {
//2 1  5  9
//3 6 10 13
//4 7 11 14
//0 8 12 15
    // 9 movimentos
    // 2 1 5 9 3 6 10 13 4 7 11 14 0 8 12 15
    // 15 movimentos
    // 6 5 13 0 1 7 9 14 2 8 10 15 3 4 11 12
    // 21 movimentos
    // 2 1 10 9 3 5 11 13 4 0 6 12 7 8 15 14 
    // 25 movimentos
    // 2 1 5 0 7 9 10 13 6 4 3 15 8 11 12 14
    // 39 movimentos
    // 1 5 7 0 4 6 12 10 8 2 15 9 3 14 11 13
    // teste 6
    // 9 13 12 8 0 5 7 14 1 11 15 4 6 10 2 3
    // DE OUTRO SITE
    // 2 1 3 4 5 6 7 8 9 10 11 12 13 14 15 0
    // 0 15 13 1 12 3 11 6 4 8 9 5 2 10 7 14

    /*5 13 6 10 1 7 2 9 4 3 15 14 8 0 11 12

 6 1 13 9 2 10 11 5 4 3 14 15 7 8 0 1

 15 11 8 4 7 6 1 5 14 12 3 2 9 10 13 0

 7 11 4 5 0 6 15 8 14 1 3 13 9 12 10 2

 14 7 4 15 9 11 3 5 0 12 6 10 1 2 13 8

 0 9 3 7 1 14 6 4 2 11 12 15 13 8 10 5

 3 9 0 7 2 1 6 5 11 13 4 12 8 14 15 10

 9 6 7 4 2 1 5 12 8 3 11 0 14 15 10 13

 2 9 4 5 0 7 11 12 14 6 3 13 1 8 15 10

 7 11 5 12 9 8 6 13 2 3 4 10 14 1 15 0
     */
    /**
     * @param args the command line arguments
     */
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
        long start = System.currentTimeMillis();
        no = new No(blocos);
        no.makeHash();
//        No.printaMatriz(No.getSolucao());
//        No.printaMatriz(no.estado);
        System.out.println(no.solucionar());
        System.out.println(System.currentTimeMillis() - start);
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
//        private int h4;
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
            return this.terceiraHeuristica();
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
//            printaMatriz(estado);
            for (int l = 0; l < 4; l++) {
                for (int c = 0; c < 4; c++) {
                    short pos = estado[l][c];
                    if (pos == 0 || estado[l][c] == No.getSolucao()[l][c]) {
                        continue;
                    }
//                    if (pos == 0) {
//                        pos = 16;
//                    }
                    short coluna2 = (short) Math.floor(pos / 4.1);
                    short linha2 = (short) (Math.floor((pos - 1) % 4));

                    short hLinha = (short) (Math.abs(linha2 - l) + Math.abs(coluna2 - c));
//                    System.out.println("estado:" + estado[l][c] + "\tcorreto:" + No.getSolucao()[l][c] + "\tl2:" + linha2 + "\tl>" + l + "\t c2:" + coluna2 + "\t c:" + c + "\tH LInha: " + hLinha);
                    h += hLinha;

                }
            }
//            System.out.println("h:" + h);
//            System.exit(0);
            this.h3 = h;
            return h;
        }

        public int quintaHeuristica() {
//            return Math.max(this.primeiraHeuristica(), Math.max(this.segundaHeuristica(), this.terceiraHeuristica()));
            return Math.max(this.primeiraHeuristica(), this.terceiraHeuristica());
        }

        public static void setSolucao(short[][] solucao) {
            String s = "";
            for (short[] matriz1 : solucao) {
                for (int j = 0; j < 4; j++) {
                    s += matriz1[j];
                }
            }
            No.solucaoHash = s;
            No.solucao = solucao;
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
                    s += matriz1[j];
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
//            long i = 0;

            while (!listaAberta.isEmpty()) {

                No no = listaAberta.poll();

                if (no.getNoPai() != null && !fechados.containsKey(no.getNoPai().hashKey)) {
                    continue;
                }
//                i++;
//                if ((i % 50000) == 0) {
//                    System.out.println("tamanho:" + listaAberta.size() + " \t passos" + no.getPassos());
//                }
                fechados.put(no.hashKey, no);

                if (no.hashKey.equals(No.solucaoHash)) {
                    return no.passos;
                }

                List<No> sucessores = no.geraSucessores();

                while (!sucessores.isEmpty()) {
                    No suc = sucessores.remove(0);
//                    suc.makeHash();
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
//            System.out.println("removendo 1 no");
            for (No no : this.sucessores) {
                if (no.sucessores.size() > 0) {
                    no.removeDaListaFechada();
                } else {
                    listaAberta.remove(no);
//                    System.out.println(listaAberta.size());

                }
            }
            fechados.remove(this.hashKey);
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
