/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package aestrelaz;

import java.util.ArrayList;
import java.util.Comparator;
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

    // 9 movimentos
    // 2 1 5 9 3 6 10 13 4 7 11 14 0 8 12 15
    // 15 movimentos
    // 6 5 13 0 1 7 9 14 2 8 10 15 3 4 11 12
    // 21 movimentos
    // 2 1 10 9 3 5 11 13 4 0 6 12 7 8 15 14 
    // 25 movimentos
    // 2 1 5 0 7 9 10 13 6 4 3 15 8 11 12 14
    // teste 5
    //  7 11 3 15 12 14 5 2 8   10 4 9 13 0 6 1
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
        long start = System.currentTimeMillis();
        no = new No(blocos);

        System.out.println(no.solucionar());
        System.out.println(System.currentTimeMillis() - start);
    }

    static class No implements Comparable<No>, Comparator<No> {

        private static String solucaoHash;

        private String hashKey;

        public static PriorityQueueMelhorada<No> listaAberta = new PriorityQueueMelhorada<No>();
        public static HashMap<String, No> fechados = new HashMap<String, No>();

        private static short solucao[][] = new short[4][4];

        private int h1;
        private int h2;
        private int h3;
        private int h4;
        private int h5;

        private int passos;

        private No noPai;

        private short estado[][] = new short[4][4];

        public No(short[][] estado) {
            this.passos = 0;
            this.hashKey = No.makeHash(estado);
            this.estado = estado;
//            this.calculaFuncaoHLinha();

        }

        public int calculaFuncaoHLinha() {
            return this.primeiraHeuristica();
        }

        public int calculaF() {
            return this.calculaFuncaoHLinha() + this.passos;
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

        public boolean equals(Object obj) {
            No n = (No) obj;
            return this.hashKey.equals(n.hashKey);
        }

        public int segundaHeuristica() {
            int h = 0;
            int ordem = 1;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (estado[i][j] != ordem) {
                        h++;
                    }
                    ordem++;
                }
            }
            this.h2 = h;
            return h;
        }

        public int terceiraHeuristica() {
            int h = 0;

            for (int l = 0; l < 4; l++) {
                for (int c = 0; c < 4; c++) {
                    short correto = No.getSolucao()[l][c];
                    if (correto != 0 && estado[l][c] != correto) {
                        short coluna2 = (short) (correto / 4);
                        short linha2 = (short) ((short) (correto % 4) - 1);
                        if(linha2 < 0){
                            linha2 = 3;
                        }
                        
                        
//                        System.out.println("correto:" + correto + "\tl2:" + linha2 + "\t c2:" + coluna2);

                        short hLinha = (short) Math.abs((l - linha2) + (c - coluna2));
//                        System.err.println("HLINHA" + hLinha);
                        h += hLinha;

                    }
                }
            }
            
            this.h3 = h;
            return h;
        }

        public int quintaHeuristica() {
            this.h5 = Math.max(h1, Math.max(h2, h3));
            return this.h5;
        }

        public static void setSolucao(short[][] solucao) {
            No.solucao = solucao;
            No.solucaoHash = No.makeHash(solucao);
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

        public static void printaMatriz(int[][] matriz) {
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[i].length; j++) {
                    System.out.print("\t " + matriz[i][j]);
                }
                System.out.println("");
            }
            System.out.println("");
        }

        public static String makeHash(short[][] matriz) {
            String s = "";
            for (short[] matriz1 : matriz) {
                for (int j = 0; j < 4; j++) {
                    s += matriz1[j];
                }
            }
            return s;
        }

        @Override
        public int compareTo(No o) {
            return this.calculaF() - o.calculaF();
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
                            No up = new No(copy_estado(this.estado));
                            up.passos = this.passos + 1;
                            up.setNoPai(this);

                            short aux = up.estado[i][j];
                            up.estado[i][j] = up.estado[i - 1][j];
                            up.estado[i - 1][j] = aux;

                            up.hashKey = No.makeHash(up.estado);

                            sucessores.add(up);
                        }
                        if (i != 3) {

                            No down = new No(copy_estado(this.estado));
                            down.passos = this.passos + 1;
                            down.setNoPai(this);

                            short aux = down.estado[i][j];
                            down.estado[i][j] = down.estado[i + 1][j];
                            down.estado[i + 1][j] = aux;

                            down.hashKey = No.makeHash(down.estado);

                            sucessores.add(down);
                        }
                        if (j != 0) {
                            No left = new No(copy_estado(this.estado));
                            left.passos = this.passos + 1;
                            left.setNoPai(this);

                            short aux = left.estado[i][j];
                            left.estado[i][j] = left.estado[i][j - 1];
                            left.estado[i][j - 1] = aux;

                            left.hashKey = No.makeHash(left.estado);
                            sucessores.add(left);
                        }
                        if (j != 3) {
                            No right = new No(copy_estado(this.estado));

                            right.passos = this.passos + 1;
                            right.setNoPai(this);

                            short aux = right.estado[i][j];
                            right.estado[i][j] = right.estado[i][j + 1];
                            right.estado[i][j + 1] = aux;

                            right.hashKey = No.makeHash(right.estado);
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
                if(no.getNoPai() != null && !fechados.containsKey(no.getNoPai().hashKey)){
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
//                            System.out.println("F:" + noF.getPassos() + " \t SUC:" + suc.getPassos());
                            
                            fechados.remove(noFechado);
                            listaAberta.add(suc);
                        }
                    } else if (noAberto != null) {
                        if (suc.getPassos() < noAberto.getPassos()) {
//                            System.out.println("A:" + noAberto.getPassos() + " \t SUC:" + suc.getPassos());
                            listaAberta.remove(noAberto);
                            listaAberta.add(suc);
                        }
                    } else {
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

        @Override
        public int compare(No o1, No o2) {
            System.out.println("comparou");
            return (int) (Long.valueOf(o1.hashKey) - Long.valueOf(o2.hashKey));
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
