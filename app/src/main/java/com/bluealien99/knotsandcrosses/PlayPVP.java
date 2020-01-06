package com.bluealien99.knotsandcrosses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.lang.Math.abs;
import static java.util.Arrays.fill;

public class PlayPVP extends AppCompatActivity {

    boolean ai;

    int move = 0;
    int totmoves = 0;
    int gamesplayed = 0;
    boolean fin = true;
    boolean animove = true;
    int[] state = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    int[] stateV = {0, 0, 0};
    int[] stateH = {0, 0, 0};
    int[] score = {0, 0};

    private MediaPlayer player;
    private AudioManager auman;

    private AudioManager.OnAudioFocusChangeListener aufocListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_GAIN)
                player.start();
            else if (focusChange == AudioManager.AUDIOFOCUS_LOSS || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT)
                relMP();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_pvp);

        ai = getIntent().getBooleanExtra("AI", false);
        auman = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if (ai) setTitle("Player vs Computer");

        FrameLayout chronofl = (FrameLayout) findViewById(R.id.chronofl);
        final Chrono chrono = new Chrono(new ContextThemeWrapper(PlayPVP.this, R.style.chrono));
        chronofl.addView(chrono);

        GridLayout topgrid = (GridLayout) findViewById(R.id.topgrid);
        topgrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totmoves == 0 && gamesplayed == 0) {
                    changePlayer();
                    if (ai && move == 1) aiMove(chrono);
                }
            }
        });

        GridView playGrid = (GridView) findViewById(R.id.playGridR);
        playGrid.setAdapter(new squareAdapter(this));

        playGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (ai && animove && move == 1) {
                    aiMove(chrono);
                    return;
                }
                if (animove && state[position] == 0) {

                    animove = false;
                    if (totmoves == 0)
                        chrono.start();
                    totmoves++;

                    View korc = view.findViewById(R.id.korc);
                    if (move == 0) {
                        playSound(0, chrono);
                        state[position] = -1;
                        stateV[position / 3]--;
                        stateH[position % 3]--;
                        korc.setBackground(getDrawable(R.drawable.cross));
                    } else if (move == 1) {
                        playSound(1, chrono);
                        state[position] = 1;
                        stateV[position / 3]++;
                        stateH[position % 3]++;
                        korc.setBackground(getDrawable(R.drawable.knot));
                    }

                    final View cover = view.findViewById(R.id.cover);
                    int animtime = getResources().getInteger(android.R.integer.config_shortAnimTime);
                    cover.animate().alpha(0f).setDuration(animtime).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            cover.setVisibility(View.GONE);

                            hasWon(position, chrono);
                            changePlayer();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        relMP();
    }

    void changePlayer() {

        move = (move + 1) % 2;

        cellSquareTxt cross = (cellSquareTxt) findViewById(R.id.crossesPts);
        cellSquareTxt knot = (cellSquareTxt) findViewById(R.id.knotsPts);

        if (move == 0) {
            cross.setBackground(getDrawable(R.drawable.crossmove));
            knot.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.Knot));
        } else {
            cross.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.Cross));
            knot.setBackground(getDrawable(R.drawable.knotsmove));
        }

        animove = true;
    }

    void hasWon(int position, Chrono chrono) {

        if (abs(stateV[position / 3]) == 3 ||
                abs(stateH[position % 3]) == 3 ||
                abs(state[0] + state[4] + state[8]) == 3 ||
                abs(state[2] + state[4] + state[6]) == 3) {

            if (move == 0)
                Toast.makeText(getApplicationContext(), "Crosses won!", Toast.LENGTH_SHORT).show();

            else if (move == 1)
                Toast.makeText(getApplicationContext(), "Knots won!", Toast.LENGTH_SHORT).show();

            score[move]++;
            endgame(chrono, false);
        } else if (totmoves == 9) {
            Toast.makeText(getApplicationContext(), "Draw!", Toast.LENGTH_SHORT).show();
            endgame(chrono, true);
        }
    }

    void endgame(Chrono chrono, boolean draw) {

        playSound(-1, chrono);

        chrono.stop();

        if (!draw) {
            ImageView endline = (ImageView) findViewById(R.id.endgame);
            endline.setVisibility(View.VISIBLE);

            if (abs(stateV[0]) == 3) endline.setImageResource(R.drawable.endh1);
            else if (abs(stateV[1]) == 3) endline.setImageResource(R.drawable.endh2);
            else if (abs(stateV[2]) == 3) endline.setImageResource(R.drawable.endh3);
            else if (abs(stateH[0]) == 3) endline.setImageResource(R.drawable.endv1);
            else if (abs(stateH[1]) == 3) endline.setImageResource(R.drawable.endv2);
            else if (abs(stateH[2]) == 3) endline.setImageResource(R.drawable.endv3);
            else if (abs(state[0] + state[4] + state[8]) == 3)
                endline.setImageResource(R.drawable.endxl);
            else if (abs(state[2] + state[4] + state[6]) == 3)
                endline.setImageResource(R.drawable.endxr);

            int animtime = getResources().getInteger(android.R.integer.config_mediumAnimTime);
            endline.animate().alpha(1f).setDuration(animtime).setListener(null);
        }

        if (totmoves % 2 == 0)
            move = (move + 1) % 2;
        totmoves = 0;

        fill(state, 0);
        fill(stateV, 0);
        fill(stateH, 0);

        cellSquareTxt cross = (cellSquareTxt) findViewById(R.id.crossesPts);
        cross.setText(String.valueOf(score[0]));
        cellSquareTxt knot = (cellSquareTxt) findViewById(R.id.knotsPts);
        knot.setText(String.valueOf(score[1]));
        TextView games = (TextView) findViewById(R.id.gamesPts);
        games.setText(String.valueOf(++gamesplayed));

        TextView crosswr = (TextView) findViewById(R.id.winratecross);
        double z = (double) score[0] / (double) gamesplayed * 100;
        String x = BigDecimal.valueOf(z).setScale(1, RoundingMode.HALF_UP).doubleValue() + "%";
        crosswr.setText(x);

        TextView knotwr = (TextView) findViewById(R.id.winrateknot);
        double zz = (double) score[1] / (double) gamesplayed * 100;
        String xx = BigDecimal.valueOf(zz).setScale(1, RoundingMode.HALF_UP).doubleValue() + "%";
        knotwr.setText(xx);
    }

    void playSound(final int sound, final Chrono chrono) {

        if (!fin)
            endgame1(chrono);
        relMP();

        int permit = auman.requestAudioFocus(aufocListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);

        if (permit == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            if (sound == 0)
                player = MediaPlayer.create(this, R.raw.winharins);
            else if (sound == 1)
                player = MediaPlayer.create(this, R.raw.winharrem);
            else if (sound == -1) {
                fin = false;
                player = MediaPlayer.create(this, R.raw.tada);
            }

            player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    relMP();
                    if (ai && sound == 0 && totmoves != 0) aiMove(chrono);
                    else if (sound == -1) endgame1(chrono);
                }
            });
        }
    }

    void endgame1(final Chrono chrono) {

        chrono.stop();
        fin = true;

        final cellSquareImg fade = (cellSquareImg) findViewById(R.id.fade);
        fade.setVisibility(View.VISIBLE);
        final ImageView endline = (ImageView) findViewById(R.id.endgame);

        final int animtime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        fade.animate().alpha(1f).setDuration(animtime).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                if (totmoves != 0) changePlayer();

                totmoves = 0;
                fill(state, 0);
                fill(stateV, 0);
                fill(stateH, 0);

                endline.setAlpha(0f);
                endline.setVisibility(View.GONE);

                GridView playGrid = (GridView) findViewById(R.id.playGridR);
                ((squareAdapter) playGrid.getAdapter()).notifyDataSetChanged();

                fade.animate().alpha(0f).setDuration(animtime).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        fade.setVisibility(View.GONE);
                        if (ai && move == 1 && animove) aiMove(chrono);
                    }
                });
            }
        });
    }

    void relMP() {
        if (player != null) {
            player.release();
            player = null;
            auman.abandonAudioFocus(aufocListener);
        }
    }

    void aiMove(final Chrono chrono) {

        animove = false;
        if (totmoves == 0) chrono.start();

        AI aiclass = new AI(state, stateV, stateH, totmoves);
        final int position = aiclass.getPosition();

        if (position >= 0 && position < 9 && state[position] == 0) {
            totmoves++;
            playSound(1, chrono);
            state[position] = 1;
            stateV[position / 3]++;
            stateH[position % 3]++;

            GridView playGrid = (GridView) findViewById(R.id.playGridR);
            View view = playGrid.getChildAt(position);
            View korc = view.findViewById(R.id.korc);
            korc.setBackground(getDrawable(R.drawable.knot));

            final View cover = view.findViewById(R.id.cover);
            int animtime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            cover.animate().alpha(0f).setDuration(animtime).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    cover.setVisibility(View.GONE);

                    hasWon(position, chrono);
                    changePlayer();
                }
            });
        } else {
            Toast.makeText(this, "ERROR! Couldn't find position :(", Toast.LENGTH_SHORT).show();
            changePlayer();
        }
    }
}
