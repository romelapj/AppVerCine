/*
 * Copyright (C) 2014 Pedro Vicente Gómez Sánchez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.romelapj.appvercine.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.github.pedrovgs.DraggableListener;
import com.github.pedrovgs.DraggablePanel;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.romelapj.appvercine.R;
import com.romelapj.appvercine.adapter.ProximaPeliculaAdapter;
import com.romelapj.appvercine.adapter.RecomendadaAdapter;
import com.romelapj.appvercine.fragment.MoviePosterFragment;
import com.romelapj.appvercine.model.ProximaPelicula;
import com.squareup.picasso.Picasso;

import java.util.List;


public class YoutubeActivity extends ActionBarActivity {

  private static final String YOUTUBE_API_KEY = "AIzaSyC1rMU-mkhoyTvBIdTnYU0dss0tU9vtK48";
  private static String VIDEO_KEY = "aO3Kpsy3X5Q";
  private static String VIDEO_POSTER_THUMBNAIL =
      "http://4.bp.blogspot.com/-BT6IshdVsoA/UjfnTo_TkBI/AAAAAAAAMWk/JvDCYCoFRlQ/s1600/"
          + "xmenDOFP.wobbly.1.jpg";
  private static String SECOND_VIDEO_POSTER_THUMBNAIL =
      "http://media.comicbook.com/wp-content/uploads/2013/07/x-men-days-of-future-past"
          + "-wolverine-poster.jpg";
  private static String VIDEO_POSTER_TITLE = "X-Men: Days of Future Past";

  @InjectView(R.id.iv_thumbnail) ImageView thumbnailImageView;
  @InjectView(R.id.draggable_panel) DraggablePanel draggablePanel;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.lvRecomendado)
    ListView lvRecomendado;
  private YouTubePlayer youtubePlayer;
  private YouTubePlayerSupportFragment youtubeFragment;
    private ProximaPelicula proximaPelicula;

    private SystemBarTintManager mTintManager;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_youtube);
    ButterKnife.inject(this);

      setSupportActionBar(toolbar);
      getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.azul)));


    initializeVariable();
    initializeYoutubeFragment();
    initializeDraggablePanel();
    hookDraggablePanelListeners();
    draggablePanel.minimize();
  }


    private void initializeVariable() {
        int pos=getIntent().getIntExtra("pos",0);
        List<ProximaPelicula> list=ProximaPeliculaAdapter.items;
        proximaPelicula = list.remove(pos);
        VIDEO_KEY= proximaPelicula.getTrailer();
        VIDEO_POSTER_THUMBNAIL= proximaPelicula.getImagen();
        SECOND_VIDEO_POSTER_THUMBNAIL= proximaPelicula.getImagen();
        VIDEO_POSTER_TITLE= proximaPelicula.getNombre();
        getSupportActionBar().setTitle(VIDEO_POSTER_TITLE);
        RecomendadaAdapter adapter;
        adapter = new RecomendadaAdapter(this, list);
        lvRecomendado.setAdapter(adapter);
        lvRecomendado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(), YoutubeActivity.class);
                intent.putExtra("pos",position);
                startActivity(intent);
            }
        });

    }


  @OnClick(R.id.iv_thumbnail) void onContainerClicked() {
    draggablePanel.maximize();
  }
    @OnClick(R.id.buttonTrailer) void onTrailerClicked() {
        draggablePanel.maximize();
    }

  private void initializeYoutubeFragment() {
    youtubeFragment = new YouTubePlayerSupportFragment();
    youtubeFragment.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {

      @Override public void onInitializationSuccess(YouTubePlayer.Provider provider,
          YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
          youtubePlayer = player;
          youtubePlayer.loadVideo(VIDEO_KEY);
          youtubePlayer.setShowFullscreenButton(true);
        }
      }

      @Override public void onInitializationFailure(YouTubePlayer.Provider provider,
          YouTubeInitializationResult error) {
      }
    });
  }

  private void initializeDraggablePanel() {
    draggablePanel.setFragmentManager(getSupportFragmentManager());
    draggablePanel.setTopFragment(youtubeFragment);
    MoviePosterFragment moviePosterFragment = new MoviePosterFragment();
    moviePosterFragment.setPoster(VIDEO_POSTER_THUMBNAIL,this);
    moviePosterFragment.setPosterTitle(VIDEO_POSTER_TITLE);
    draggablePanel.setBottomFragment(moviePosterFragment);
    draggablePanel.initializeView();
    Picasso.with(this)
        .load(SECOND_VIDEO_POSTER_THUMBNAIL)
        .placeholder(R.drawable.img_pelicula)
        .into(thumbnailImageView);

  }


  private void hookDraggablePanelListeners() {
    draggablePanel.setDraggableListener(new DraggableListener() {
      @Override public void onMaximized() {
        playVideo();
      }

      @Override public void onMinimized() {
        //Empty
      }

      @Override public void onClosedToLeft() {
        pauseVideo();
      }

      @Override public void onClosedToRight() {
        pauseVideo();
      }
    });
  }

  private void pauseVideo() {
    if (youtubePlayer.isPlaying()) {
      youtubePlayer.pause();
    }
  }

  private void playVideo() {
    if (!youtubePlayer.isPlaying()) {
      youtubePlayer.play();
    }
  }

    public void minimizarVideo() {
        draggablePanel.minimize();
    }
}
