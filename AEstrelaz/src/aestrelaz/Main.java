//package aestrelaz;

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
//        long start = System.currentTimeMillis();
        no = new No(blocos);
        no.makeHash();
//        No.printaMatriz(No.getSolucao());
//        No.printaMatriz(no.estado);
        System.out.println(no.solucionar());
//        System.out.println(System.currentTimeMillis() - start);
    }

    static class No implements Comparable<No> {

        private static String solucaoHash;

        private String hashKey;

        public static PriorityQueueMelhorada<No> listaAberta = new PriorityQueueMelhorada<No>();
        public static HashMap<String, No> fechados = new HashMap<String, No>();

        private static short solucao[][] = new short[4][4];

        private int h1;
//        private int h2;
        private int h3;
//        private int h4;
//        private int h5;

        private short funcaoF;
        private int passos;
        private short modPai = 0;

        private No noPai;

        private short estado[][] = new short[4][4];

        public No(short[][] estado) {
            this.passos = 0;
//            this.makeHash();
            this.estado = estado;
//            this.calculaFuncaoHLinha();

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
//
//        public int segundaHeuristica() {
//            int h = 0;
//            int ordem = 1;
//            for (int i = 0; i < 4; i++) {
//                for (int j = 0; j < 4; j++) {
//                    if (estado[i][j] != ordem) {
//                        h++;
//                    }
//                    ordem++;
//                }
//            }
//            this.h2 = h;
//            return h;
//        }

        public int terceiraHeuristica() {
            int h = 0;
            for (int l = 0; l < 4; l++) {
                for (int c = 0; c < 4; c++) {
                    short pos = estado[l][c];
                    if (estado[l][c] == No.getSolucao()[l][c]) {
                        continue;
                    }
                    if (pos == 0) {
                        pos = 16;
                    }
                    short coluna2 = (short) Math.floor(pos / 4.1);
                    short linha2 = (short) (Math.floor((pos - 1) % 4));

                    short hLinha = (short) Math.abs((linha2 - l) + (coluna2 - c));
//                    System.out.println("estado:" + estado[l][c] + "\tl2:" + linha2 + "\t c2:" + coluna2 + "\tH LInha: " + hLinha);
                    h += hLinha;

                }
            }
//            System.out.println("h:" + h);
//            System.exit(0);
            this.h3 = h;
            return h;
        }

//        public int quintaHeuristica() {
//            this.h5 = Math.max(h1, Math.max(h2, h3));
//            return this.h5;
//        }
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
                        if (i != 0) {
                            if (this.getNoPai() == null || this.modPai != -1) {
                                No up = new No(copy_estado(this.estado));
                                up.passos = this.passos + 1;
                                up.setNoPai(this);
                                up.modPai = 1;

                                short aux = up.estado[i][j];
                                up.estado[i][j] = up.estado[i - 1][j];
                                up.estado[i - 1][j] = aux;
                                up.makeHash();

                                sucessores.add(up);
                            }
                        }
                        if (i != 3) {
                            if (this.getNoPai() == null || this.modPai != 1) {

                                No down = new No(copy_estado(this.estado));
                                down.passos = this.passos + 1;
                                down.setNoPai(this);

                                short aux = down.estado[i][j];
                                down.estado[i][j] = down.estado[i + 1][j];
                                down.estado[i + 1][j] = aux;
                                down.modPai = -1;
                                down.makeHash();
                                sucessores.add(down);
                            }
                        }
                        if (j != 0) {
                            if (this.getNoPai() == null || this.modPai != -2) {
                                No left = new No(copy_estado(this.estado));
                                left.passos = this.passos + 1;
                                left.setNoPai(this);

                                short aux = left.estado[i][j];
                                left.estado[i][j] = left.estado[i][j - 1];
                                left.estado[i][j - 1] = aux;
                                left.makeHash();
                                left.modPai = 2;
                                sucessores.add(left);
                            }
                        }
                        if (j != 3) {
                            if (this.getNoPai() == null || this.modPai != 2) {
                                No right = new No(copy_estado(this.estado));

                                right.passos = this.passos + 1;
                                right.setNoPai(this);

                                short aux = right.estado[i][j];
                                right.estado[i][j] = right.estado[i][j + 1];
                                right.estado[i][j + 1] = aux;
                                right.makeHash();
                                right.modPai = -2;
                                sucessores.add(right);
                            }
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
//                    suc.makeHash();
                    No noFechado = fechados.get(suc.hashKey);
                    No noAberto = listaAberta.getElement(suc, suc.getPassos());

                    if (fechados.containsKey(suc.hashKey)) {
                        if (suc.getPassos() < noFechado.getPassos()) {
                            fechados.remove(noFechado.hashKey);
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
