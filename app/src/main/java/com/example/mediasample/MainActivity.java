package com.example.mediasample;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.io.IOException;
import java.net.URI;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer _player;
    private Button _btPlay;
    private Button _btBack;
    private Button _btForward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _btPlay = findViewById(R.id.btPlay);
        _btBack = findViewById(R.id.btBack);
        _btForward = findViewById(R.id.btForward);

        // メディアプレーヤーオブジェクト生成
        _player = new MediaPlayer();
        // 音声ファイル用のURI文字列を作成
         String mediaFileUriStr =
                 "android.resource://" + getPackageName() + "/" + R.raw.mountain_stream;
        // URIオブジェクト生成
        Uri mediaFileUri = Uri.parse(mediaFileUriStr);
        try {
            // メディアプレーヤーに音声ファイルを指定
            _player.setDataSource(MainActivity.this, mediaFileUri);
            // 非同期でのメディア再生準備が完了した際のリスナを設定
            _player.setOnPreparedListener(new PlayerPreparedListener());
            // メディア再生が終了した際のリスナを設定
            _player.setOnCompletionListener(new PlayerCompletionListener());
            // 非同期でメディア再生を準備
            _player.prepareAsync();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Switch loopSwitch = findViewById(R.id.swLoop);
        loopSwitch.setOnCheckedChangeListener(new LoopSwitchChangedListener());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(_player.isPlaying()) {
            _player.stop();
        }
        // プレーヤーを解放
        _player.release();
        _player = null;
    }

    public void onPlayButtonClick(View view) {
        if(_player.isPlaying()) {
            // プレーヤーを一時停止
            _player.pause();
            _btPlay.setText(R.string.bt_play_play);

        } else {
            _player.start();
            _btPlay.setText(R.string.bt_play_pause);
        }
    }

    public void onBackButtonClick(View view) {
        // 再生位置を先頭に変更
        _player.seekTo(0);
    }

    public void onForwardButtonClick(View view) {
        // 再生中のメディアファイルの長さを取得
        int duration = _player.getDuration();
        // 再生位置を終端に変更
        _player.seekTo(duration);

        if(!_player.isPlaying()) {
            _player.start();
        }
    }

    /* プレーヤーの再生準備が整ったときのリスナクラス */
    private class PlayerPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mb) {
            _btPlay.setEnabled(true);
            _btBack.setEnabled(true);
            _btForward.setEnabled(true);
        }
    }

    /* 再生が終了したときのリスナクラス */
    private class PlayerCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mb) {
            if(!_player.isLooping()) {
                _btPlay.setText(R.string.bt_play_play);
            }
        }
    }

    private class LoopSwitchChangedListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // ループするかどうかを設定
            _player.setLooping(isChecked);
        }
    }
}