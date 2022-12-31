package dsa.hcmiu.a2048pets;

import static dsa.hcmiu.a2048pets.entities.model.Features.mySong;
import static dsa.hcmiu.a2048pets.entities.model.Features.sound;
import static dsa.hcmiu.a2048pets.entities.model.Features.user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.github.nkzawa.emitter.Emitter;
//import com.github.nkzawa.socketio.client.IO;
//import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import dsa.hcmiu.a2048pets.entities.adapter.ItemAdapter;
import dsa.hcmiu.a2048pets.entities.handle.HandleFile;
import dsa.hcmiu.a2048pets.entities.handle.HandleGame;
import dsa.hcmiu.a2048pets.entities.handle.OnSwipeTouchListener;
import dsa.hcmiu.a2048pets.entities.model.Features;
import dsa.hcmiu.a2048pets.entities.model.Pets;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class PlayPvpActivity extends Activity implements View.OnClickListener {
    private ArrayList<Pets> matrixPet;
    private GridView gvMatrix;
    private ItemAdapter adapter;
    private View layout;
    private TextView tvScore, tvUndo, tvHammer, tvCompScore, tvMess;
    private Button btnUndo, btnNew, btnSoundPlay;
    private Button btnHammer;
    private Socket mSocket;
    private Boolean isConnected = true;
    private final String TAG = "Play_PVP_activity";

    {
        try {
            mSocket = IO.socket(Features.GAME_SERVER_IP);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        final Dialog MyDialog = new Dialog(PlayPvpActivity.this, R.style.FullHeightDialog);
        LayoutInflater inflater = PlayPvpActivity.this.getLayoutInflater();
        MyDialog.setContentView(R.layout.dialog);
        Button btnyes = (Button) MyDialog.findViewById(R.id.btnyes);
        Button btnno = (Button) MyDialog.findViewById(R.id.btnno);
        TextView tvMess = (TextView) MyDialog.findViewById(R.id.tvMessage);
        tvMess.setText("The game will be unsaved. Are you sure want to quit?");
        Animation zoomin = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        Animation zoomout = AnimationUtils.loadAnimation(this, R.anim.zoom_out);
        ImageView imgIcon = (ImageView) MyDialog.findViewById(R.id.icon_github);
        imgIcon.setAnimation(zoomin);
        imgIcon.setAnimation(zoomout);
        btnyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDialog.cancel();
            }
        });
        MyDialog.show();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_pvp);
        if (sound) mySong.start();
        tvScore = (TextView) findViewById(R.id.tvScorePvp);
        tvUndo = (TextView) findViewById(R.id.tvUndoPvp);
        tvHammer = (TextView) findViewById(R.id.tvHammerPvp);
        tvCompScore = (TextView) findViewById(R.id.tvComponentScore);
        tvMess = (TextView) findViewById(R.id.tvMessPvp);
        btnUndo = (Button) findViewById(R.id.btnUndoPvp);
        btnHammer = (Button) findViewById(R.id.btnHammerPvp);
        btnNew = (Button) findViewById(R.id.btnNewGamePvp);
        btnSoundPlay = (Button) findViewById(R.id.btnSoundPlayPvp);
        dialogShareLink();
        create();
        setData();

        btnUndo.setOnClickListener(this) ;
        btnNew.setOnClickListener(this);
        btnSoundPlay.setOnClickListener(this);
        btnHammer.setOnClickListener(this);
        tvMess.setOnClickListener(this);
    }

    private void create() {
        gvMatrix = (GridView) findViewById(R.id.gvMatrixPvp);
    }

    private void setData() {
        adapter = new ItemAdapter(this, R.layout.item_pet,
                HandleGame.getInstance().curBoard.getMatrix());
        gvMatrix.setAdapter(adapter);
        update();
    }

    public void dialogShareLink() {
        final Dialog myDialog = new Dialog(PlayPvpActivity.this, R.style.FullHeightDialog);
        LayoutInflater inflater = PlayPvpActivity.this.getLayoutInflater();
        myDialog.setContentView(R.layout.dialog);
        Button btnyes = (Button) myDialog.findViewById(R.id.btnyes);
        Button btnno = (Button) myDialog.findViewById(R.id.btnno);
        TextView tvMess = (TextView) myDialog.findViewById(R.id.tvMessage);
        btnyes.setText("Copy");
        btnno.setVisibility(View.GONE);
        tvMess.setText(Features.GAME_SERVER_URL);
        Animation zoomin = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        Animation zoomout = AnimationUtils.loadAnimation(this, R.anim.zoom_out);
        ImageView imgIcon = (ImageView) myDialog.findViewById(R.id.icon_github);
        imgIcon.setAnimation(zoomin);
        imgIcon.setAnimation(zoomout);
        btnyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Share url", Features.GAME_SERVER_URL);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(),
                        "Copied to Clipboard", Toast.LENGTH_LONG).show();
                connect();
                myDialog.cancel();
            }
        });
        myDialog.show();
    }


    private void connect() {
//        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
//        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
//        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on("player-number", playernumber);
        mSocket.connect();

    }

    private void check() {
        update();
        if (HandleGame.getInstance().gameOver()) {
            final Dialog MyDialog = new Dialog(PlayPvpActivity.this, R.style.FullHeightDialog);
            LayoutInflater inflater = PlayPvpActivity.this.getLayoutInflater();
            MyDialog.setContentView(R.layout.dialog_gameover);
            Button btnyes = (Button) MyDialog.findViewById(R.id.btnok);
            TextView tvMess = (TextView) MyDialog.findViewById(R.id.tvMessage);
            tvMess.setText("One more time?");
            btnyes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HandleGame.getInstance().newGame();
                    update();
                    MyDialog.cancel();
                }
            });
            MyDialog.setCanceledOnTouchOutside(false);
            MyDialog.show();
        }
    }

    private void update() {
        adapter.notifyDataSetChanged();
        tvScore.setText(String.valueOf(HandleGame.getInstance().curBoard.getScoreBoard()));
        tvUndo.setText(String.valueOf(user.undo));
        tvHammer.setText(String.valueOf(user.hammer));
        tvCompScore.setText(String.valueOf(user.highScore));
//        tvNameHS.setText(user.UserHighScore);
    }

    @Override
    protected void onResume() {
        super.onResume();
        HandleGame.getInstance().newGame();
        update();
    }

    @Override
    protected void onStart() {
        super.onStart();
        HandleGame.getInstance().newGame();
        update();
    }

    @Override
    protected void onPause() {
        super.onPause();
        HandleGame.getInstance().newGame();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUndo:
                Log.d("Play", "onClick: Undo");
                HandleGame.getInstance().Undo();
                update();
                break;
            case R.id.btnNewGame:
                Log.d("Play", "onClick: New");
                HandleGame.getInstance().newGame();
                update();
                break;
            case R.id.btnSoundPlay:
                Log.d("Play", "onClick: Sound");
                if (sound) mySong.pause();
                else {
                    if (mySong.isPlaying()) mySong.stop();
                    mySong.start();
                }
                sound = !(sound || sound);
                break;
            case R.id.btnHammer:
                Log.d("Play", "onClick: Hammer");
                HandleGame.getInstance().hammer();
                update();
                break;
            case R.id.tvMessPvp:
                dialogShareLink();
                break;
        }
    }

    private Emitter.Listener playernumber = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String playerNumber = (String) args[0];
                    if (playerNumber == "1") {
                        showMess(R.string.message_waiting);
                        mSocket.on("player-connect", startGame);
                    } else if (playerNumber == "-1") {
                        showMess(R.string.message_full);
                    } else { // Immediately start the game if we're player two
                        setUpGame();
                    }
                }
            });
        }
    };
    private void showMess(int id) {
        if (id<0) {
            tvMess.setVisibility(View.GONE);
        } else {
            tvMess.setVisibility(View.VISIBLE);
            tvMess.setText(id);
        }
    }

    private Emitter.Listener startGame = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setUpGame();
                }
            });
        }
    };

    private void setUpGame() {
//         Start a countdown timer
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            int seconds = 4; // Number of seconds + 1 to wait

            @Override
            public void run() {
                showMess(--seconds);
                h.postDelayed(this, 1000);
            }
        }, 1000);
        showMess(-1);
        HandleGame.getInstance().newGame();

        gvMatrix.setOnTouchListener(new OnSwipeTouchListener(PlayPvpActivity.this) { //extend class OnswipeTouch
            public void onSwipeUp() {
                HandleGame.getInstance().moveUp();
                mSocket.emit("actuate", HandleGame.curBoard.getScoreBoard());
                check();
            }

            public void onSwipeRight() {
                HandleGame.getInstance().moveRight();
                mSocket.emit("actuate", HandleGame.curBoard.getScoreBoard());
                check();
            }

            public void onSwipeLeft() {
                HandleGame.getInstance().moveLeft();
                mSocket.emit("actuate", HandleGame.curBoard.getScoreBoard());
                check();
            }

            public void onSwipeDown() {
                HandleGame.getInstance().moveDown();
                mSocket.emit("actuate", HandleGame.curBoard.getScoreBoard());
                check();
            }
        });
    }

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "diconnected");
                    isConnected = false;
                    Toast.makeText(getApplicationContext(),
                            R.string.disconnect, Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "Error connecting");
                    Log.e(TAG, args.toString());
                    Toast.makeText(getApplicationContext(),
                            R.string.error_connect, Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }
//                    removeTyping(username);
//                    addMessage(username, message);
                }
            });
        }
    };

    private Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    int numUsers;
                    try {
                        username = data.getString("username");
                        numUsers = data.getInt("numUsers");
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }
//                    addLog(getResources().getString(R.string.message_user_joined, username));
//                    addParticipantsLog(numUsers);
                }
            });
        }
    };

    private Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    int numUsers;
                    try {
                        username = data.getString("username");
                        numUsers = data.getInt("numUsers");
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }
//                    addLog(getResources().getString(R.string.message_user_left, username));
//                    addParticipantsLog(numUsers);
//                    removeTyping(username);
                }
            });
        }
    };
}