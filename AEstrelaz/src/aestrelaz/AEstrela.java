/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aestrelaz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 *
 * @author guest-tqterz
 */
class AEstrela {
// 2 1 5 9 3 6 10 13 4 7 11 14 0 8 12 15

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        No no;
        int blocos[][] = new int[4][4];
        int objetivo[][] = {
            {1, 5, 9, 13},
            {2, 6, 10, 14},
            {3, 7, 11, 15},
            {4, 8, 12, 0}
        };
        No.setSolucao(objetivo);
        Scanner scan = new Scanner(System.in);

        String entrada = scan.nextLine().trim();
        
        String[] entradas = entrada.split(" ");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                blocos[i][j] = Integer.valueOf(entradas[i * 4 + j]);
            }
        }
        no = new No(blocos);

        System.out.println(no.solucionar());

    }

    static class No implements Comparable<No> {

        private static String solucaoHash;

        private int funcaoF;

        private String hashKey;

        public static PriorityQueue<No> listaAberta = new PriorityQueue<No>();
        public static HashMap<String, No> fechados = new HashMap<String, No>();

        private static int solucao[][] = new int[4][4];

        private int h1;
        private int h2;
        private int h3;
        private int h4;
        private int h5;

        private int passos;

        private No noPai;

        private int estado[][] = new int[4][4];

        public No(int[][] estado) {
            this.passos = 0;
            this.hashKey = No.makeHash(estado);
            this.estado = estado;
            this.calculaFuncaoHLinha();

        }

        public void calculaFuncaoG() {

        }

        public void calculaFuncaoHLinha() {
            this.primeiraHeuristica();
        }

        public int[][] getEstado() {
            return estado;
        }

        public void setEstado(int[][] estado) {
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
            return h;
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
                    int correto = (l * 4) + c + 1;
                    //TODO Esse if vai dar prob de otimização =X
                    if (correto == 16) {
                        correto = 0;
                    }
                    if (estado[c][l] != correto) {
                        h += Math.abs(l - (correto / 4)) + Math.abs(c - (correto % 4));
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

        public static void setSolucao(int[][] solucao) {
            No.solucao = solucao;
            No.solucaoHash = No.makeHash(solucao);
        }

        public static int[][] getSolucao() {
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

        public static String makeHash(int[][] matriz) {
            String s = "";
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[i].length; j++) {
                    s += matriz[i][j];
                }

            }
            return s;
        }

        @Override
        public int compareTo(No o) {
            return this.calculaF() - o.calculaF();
        }

        public static int[][] copy_estado(int[][] estadoOriginal) {
            int[][] resultado = new int[4][4];
            for (int i = 0; i < estadoOriginal.length; i++) {
                for (int j = 0; j < estadoOriginal[i].length; j++) {
                    resultado[i][j] = estadoOriginal[i][j];
                }
            }
            return resultado;
        }

        public int calculaF() {
            return this.primeiraHeuristica() + this.passos;
        }

        public ArrayList<No> geraSucessores() {
            ArrayList<No> sucessores = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (this.estado[i][j] == 0) {
                        if (i != 0) { //trocar com a peça de cima
                            // up.parent = &st;
                            No up = new No(copy_estado(this.estado));
                            up.passos = this.passos + 1;
                            up.setNoPai(this);

                            int aux = up.estado[i][j];
                            up.estado[i][j] = up.estado[i - 1][j];
                            up.estado[i - 1][j] = aux;

                            up.hashKey = No.makeHash(up.estado);
                            listaAberta.add(up);
                            sucessores.add(up);
                        }
                        if (i != 3) { //trocar com a peça de  baixo
                            // down.parent = &st;
                            No down = new No(copy_estado(this.estado));
                            down.passos = this.passos + 1;
                            down.setNoPai(this);

                            int aux = down.estado[i][j];
                            down.estado[i][j] = down.estado[i + 1][j];
                            down.estado[i + 1][j] = aux;

                            down.hashKey = No.makeHash(down.estado);
                            listaAberta.add(down);
                            sucessores.add(down);
                        }
                        if (j != 0) { //trocar com a peça da esquerda
                            No left = new No(copy_estado(this.estado));
                            // left.parent = &st;
                            left.passos = this.passos + 1;
                            left.setNoPai(this);

                            int aux = left.estado[i][j];
                            left.estado[i][j] = left.estado[i][j - 1];
                            left.estado[i][j - 1] = aux;

                            left.hashKey = No.makeHash(left.estado);
                            listaAberta.add(left);
                            sucessores.add(left);
                        }
                        if (j != 3) { //trocar com a peça da direita
                            No right = new No(copy_estado(this.estado));
                            // right.parent = &st;
                            right.passos = this.passos + 1;
                            right.setNoPai(this);

                            int aux = right.estado[i][j];
                            right.estado[i][j] = right.estado[i][j + 1];
                            right.estado[i][j + 1] = aux;

                            right.hashKey = No.makeHash(right.estado);
                            listaAberta.add(right);
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

            //A-STAR ALGORITHM BEGIN        
            listaAberta.add(this);
            while (!listaAberta.isEmpty()) {

                No no = listaAberta.remove();
                fechados.put(no.hashKey, no);

                if (no.hashKey.equals(No.solucaoHash)) {
                    return no.passos;
                }

                List<No> sucessores = no.geraSucessores();

                while (!sucessores.isEmpty()) {
                    No suc = sucessores.remove(0);

                    if (fechados.containsKey(suc.hashKey)) {
                        if (suc.getPassos() < suc.getNoPai().getPassos()) {
                            fechados.remove(suc.getNoPai());
                            suc.calculaFuncaoHLinha();
                            listaAberta.add(suc);
                        }
                    } else if (listaAberta.contains(suc)) {
                        if (suc.getPassos() < suc.getNoPai().getPassos()) {
                            listaAberta.remove(suc.getNoPai());
                            suc.calculaFuncaoHLinha();
                            listaAberta.add(suc);
                        }
                    } else {
                        suc.calculaFuncaoHLinha();
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

}
