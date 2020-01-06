package com.bluealien99.knotsandcrosses;

import java.util.concurrent.ThreadLocalRandom;

public class AI {

    private int[] state;
    private int[] stateV;
    private int[] stateH;
    private int totmoves;

    AI(int[] tab, int[] tabV, int[] tabH, int tot) {
        state = tab;
        stateV = tabV;
        stateH = tabH;
        totmoves = tot;
    }

    int getPosition() {

        //First / Second move - middle
        if (totmoves == 0 || (totmoves == 1 && state[4] == 0)) return 4;

        //Second move - corner (random)
        if (totmoves == 1 && state[4] != 0) {
            int r = 4;
            while (r == 4) r = ThreadLocalRandom.current().nextInt(0, 5) * 2;
            return r;
        }

        //Try to win
        for (int i = 0; i < 3; i++) {
            if (stateV[i] == 2)
                for (int j = 3 * i; j < 3 * (i + 1); j++)
                    if (state[j] == 0) return j;
        }

        for (int i = 0; i < 3; i++) {
            if (stateH[i] == 2)
                for (int j = i; j < i + 7; j += 3)
                    if (state[j] == 0) return j;
        }

        if (state[0] + state[4] + state[8] == 2) {
            if (state[0] == 0) return 0;
            if (state[8] == 0) return 8;
        }

        if (state[2] + state[4] + state[6] == 2) {
            if (state[2] == 0) return 2;
            if (state[6] == 0) return 6;
        }

        //Try not to lose
        for (int i = 0; i < 3; i++) {
            if (stateV[i] == -2)
                for (int j = 3 * i; j < 3 * (i + 1); j++)
                    if (state[j] == 0) return j;
        }

        for (int i = 0; i < 3; i++) {
            if (stateH[i] == -2)
                for (int j = i; j < i + 7; j += 3)
                    if (state[j] == 0) return j;
        }

        if (state[0] + state[4] + state[8] == -2) {
            if (state[0] == 0) return 0;
            if (state[8] == 0) return 8;
        }

        if (state[2] + state[4] + state[6] == -2) {
            if (state[2] == 0) return 2;
            if (state[6] == 0) return 6;
        }

        //Block - corners
        if (state[0] == 0 && stateV[0] + stateH[0] == -2) return 0;
        if (state[2] == 0 && stateV[0] + stateH[2] == -2) return 2;
        if (state[6] == 0 && stateV[2] + stateH[0] == -2) return 6;
        if (state[8] == 0 && stateV[2] + stateH[2] == -2) return 8;

        //Block - just block
        if (stateV[0] == -1) {
            if (state[0] == 0) return 0;
            if (state[2] == 0) return 2;
            if (state[1] == 0) return 1;
        }
        if (stateV[2] == -1) {
            if (state[6] == 0) return 6;
            if (state[8] == 0) return 8;
            if (state[7] == 0) return 7;
        }
        if (stateH[0] == -1) {
            if (state[0] == 0) return 0;
            if (state[6] == 0) return 6;
            if (state[3] == 0) return 3;
        }
        if (stateH[2] == -1) {
            if (state[2] == 0) return 2;
            if (state[8] == 0) return 8;
            if (state[5] == 0) return 5;
        }
        if (stateV[1] == -1) {
            if (state[3] == 0) return 3;
            if (state[5] == 0) return 5;
        }
        if (stateH[1] == -1) {
            if (state[1] == 0) return 1;
            if (state[7] == 0) return 7;
        }

        //First empty
        for (int i = 0; i < 9; i++)
            if (state[i] == 0) return i;

        //In case of an error
        return -1;
    }
}
